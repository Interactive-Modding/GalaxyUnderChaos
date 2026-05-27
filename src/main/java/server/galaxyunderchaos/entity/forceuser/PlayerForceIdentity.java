package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForceNetworking;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.force.PlayerForceIdentitySyncPacket;

import java.util.Locale;

/**
 * Persistent Force identity for players.
 *
 * Current identity format: species|robe|textureVariant|eyeColor|skinHex.
 * Old species|robe saves still load and are expanded with safe defaults.
 */
public final class PlayerForceIdentity {
    private static final String ROOT = "GUCForceIdentity";
    private static final String FORCE_NAME = "ForceName";
    private static final String SPECIES_ID = "SpeciesId";
    private static final String GENDER_ID = "GenderId";
    private static final String HAS_CUSTOM_IDENTITY = "HasCustomIdentity";
    private static final String DEFAULT_SPECIES = "mirialan_male";
    private static final String DEFAULT_ROBE = "jedi_robes";
    private static final String DEFAULT_EYE = "blue";
    private static final String DEFAULT_SKIN_HEX = "C68642";

    private PlayerForceIdentity() {}

    public static void setIdentity(ServerPlayer player, String requestedName, String requestedSpeciesId) {
        CompoundTag tag = data(player);
        String name = sanitizeName(requestedName, player.getGameProfile().getName());
        String identityId = hasCustomIdentity(player) && tag.contains(SPECIES_ID)
                ? updateLockedIdentity(player, tag.getString(SPECIES_ID), requestedSpeciesId)
                : normalizePlayerIdentity(player, requestedSpeciesId);
        tag.putString(FORCE_NAME, name);
        tag.putString(SPECIES_ID, identityId);
        tag.putString(GENDER_ID, genderFromSpecies(speciesPart(identityId)));
        tag.putBoolean(HAS_CUSTOM_IDENTITY, true);
        applyTitle(player);
        syncIdentity(player);
    }

    public static void clearIdentity(ServerPlayer player) {
        CompoundTag tag = data(player);
        tag.remove(FORCE_NAME);
        tag.remove(SPECIES_ID);
        tag.remove(GENDER_ID);
        tag.putBoolean(HAS_CUSTOM_IDENTITY, false);
        player.setCustomName(null);
        player.setCustomNameVisible(false);
        syncIdentity(player);
    }

    public static boolean hasCustomIdentity(ServerPlayer player) {
        return data(player).getBoolean(HAS_CUSTOM_IDENTITY);
    }

    public static String getForceName(ServerPlayer player) {
        CompoundTag tag = data(player);
        if (tag.contains(FORCE_NAME)) {
            return sanitizeName(tag.getString(FORCE_NAME), player.getGameProfile().getName());
        }
        return player.getGameProfile().getName();
    }

    public static String getSpeciesId(ServerPlayer player) {
        CompoundTag tag = data(player);
        if (tag.contains(SPECIES_ID)) {
            return normalizePlayerIdentity(player, tag.getString(SPECIES_ID));
        }
        return normalizePlayerIdentity(player, DEFAULT_SPECIES + "|" + DEFAULT_ROBE);
    }

    public static String getSpeciesDisplayName(ServerPlayer player) {
        return speciesDisplayName(speciesPart(getSpeciesId(player)));
    }

    public static String getRobeDisplayName(ServerPlayer player) {
        return robeDisplayName(robePart(getSpeciesId(player)));
    }

    public static String getTextureDisplayName(ServerPlayer player) {
        return textureDisplayName(texturePart(getSpeciesId(player)));
    }

    public static String getGenderId(ServerPlayer player) {
        CompoundTag tag = data(player);
        if (tag.contains(GENDER_ID)) {
            String gender = tag.getString(GENDER_ID).toLowerCase(Locale.ROOT).trim();
            if ("female".equals(gender) || "male".equals(gender) || "player".equals(gender)) {
                return gender;
            }
        }
        return genderFromSpecies(speciesPart(getSpeciesId(player)));
    }

    public static String getGenderDisplayName(ServerPlayer player) {
        return switch (getGenderId(player)) {
            case "female" -> "Female";
            case "player" -> "Player";
            default -> "Male";
        };
    }

    /** Renderer hook for dark-side eye overlay/layer. */
    public static boolean hasDarkSideEyes(ServerPlayer player) {
        ForceSide side = player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .orElse(ForceSide.UNIVERSAL);
        return side == ForceSide.DARK && (ForceTrainingManager.hasSithApprenticeStanding(player) || ForceTrainingManager.hasTrainedStudent(player));
    }

    public static String getEyeDisplayName(ServerPlayer player) {
        if (hasDarkSideEyes(player)) {
            return "Sith";
        }
        return eyeDisplayName(eyePart(getSpeciesId(player)));
    }

    public static String getSkinHex(ServerPlayer player) {
        return skinPart(getSpeciesId(player));
    }

    public static String getDisplayTitle(ServerPlayer player) {
        return rankTitle(player) + " " + getForceName(player);
    }

    public static void applyTitle(ServerPlayer player) {
        if (!hasCustomIdentity(player)) {
            return;
        }
        player.setCustomName(Component.literal(getDisplayTitle(player)));
        player.setCustomNameVisible(true);
        syncIdentity(player);
    }

    public static void copyIdentity(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        CompoundTag oldRoot = oldPlayer.getPersistentData().getCompound(ROOT);
        if (!oldRoot.isEmpty()) {
            newPlayer.getPersistentData().put(ROOT, oldRoot.copy());
            applyTitle(newPlayer);
            syncIdentity(newPlayer);
        }
    }

    public static void syncIdentity(ServerPlayer player) {
        if (player == null) {
            return;
        }
        ForceNetworking.sendIdentityToTracking(player, new PlayerForceIdentitySyncPacket(
                player.getUUID(),
                hasCustomIdentity(player),
                getForceName(player),
                hasCustomIdentity(player) ? getDisplayTitle(player) : "",
                getSpeciesId(player),
                getGenderId(player),
                hasDarkSideEyes(player)
        ));
    }

    public static String rankTitle(ServerPlayer player) {
        ForceSide side = player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .orElse(ForceSide.UNIVERSAL);
        boolean trainedStudent = ForceTrainingManager.hasTrainedStudent(player);
        boolean knight = switch (side) {
            case LIGHT, DARK, NEUTRAL -> ForceTrainingManager.hasKnightStanding(player, side);
            default -> false;
        };

        return switch (side) {
            case DARK -> trainedStudent ? "Sith Lord" : ForceTrainingManager.hasSithApprenticeStanding(player) ? "Sith Apprentice" : "Acolyte";
            case LIGHT -> trainedStudent ? "Jedi Master" : knight ? "Jedi Knight" : "Padawan";
            case NEUTRAL -> trainedStudent ? "Neutral Master" : knight ? "Neutral Knight" : "Neutral Initiate";
            default -> "Force Sensitive";
        };
    }

    public static String speciesDisplayName(String speciesId) {
        return switch (normalizeSpecies(speciesId)) {
            case "cerean_male" -> "Cerean Male";
            case "cerean_female" -> "Cerean Female";
            case "chiss_male" -> "Chiss Male";
            case "chiss_female" -> "Chiss Female";
            case "miraluka_male" -> "Miraluka Male";
            case "miraluka_female" -> "Miraluka Female";
            case "mirialan_male" -> "Mirialan Male";
            case "mirialan_female" -> "Mirialan Female";
            case "rodian_male" -> "Rodian Male";
            case "rodian_female" -> "Rodian Female";
            case "sith_male" -> "Sith Male";
            case "sith_female" -> "Sith Female";
            case "togruta_male" -> "Togruta Male";
            case "togruta_female" -> "Togruta Female";
            case "twilek_male" -> "Twi'lek Male";
            case "twilek_female" -> "Twi'lek Female";
            case "zabrak_male" -> "Zabrak Male";
            case "zabrak_female" -> "Zabrak Female";
            default -> "Robe Body";
        };
    }

    public static String robeDisplayName(String robeId) {
        return switch (normalizeRobe(robeId)) {
            case "jedi_robes" -> "Jedi Robes";
            case "jedi_robes_alt" -> "Jedi Robes Alt";
            case "sith_robes" -> "Sith Robes";
            case "sith_robes_alt" -> "Sith Robes Alt";
            case "neutral_robes" -> "Neutral Robes";
            case "neutral_robes_alt" -> "Neutral Robes Alt";
            default -> "Jedi Robes";
        };
    }

    public static String textureDisplayName(String textureId) {
        if (textureId == null || textureId.isBlank()) {
            return "Default";
        }
        String cleaned = textureId.replace('_', ' ');
        String[] parts = cleaned.split(" ");
        StringBuilder out = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) continue;
            if (out.length() > 0) out.append(' ');
            out.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return out.length() == 0 ? "Default" : out.toString();
    }

    public static String eyeDisplayName(String eyeId) {
        return switch (normalizeEyeColor(eyeId)) {
            case "light_blue" -> "Light Blue";
            case "grey_blue" -> "Grey Blue";
            case "green" -> "Green";
            case "hazel" -> "Hazel";
            case "brown" -> "Brown";
            case "dark_brown" -> "Dark Brown";
            default -> "Blue";
        };
    }

    public static String speciesPart(String identityId) {
        return parse(identityId).speciesId();
    }

    public static String robePart(String identityId) {
        return parse(identityId).robeId();
    }

    public static String texturePart(String identityId) {
        return parse(identityId).textureId();
    }

    public static String eyePart(String identityId) {
        return parse(identityId).eyeColor();
    }

    public static String skinPart(String identityId) {
        return parse(identityId).skinHex();
    }

    private static CompoundTag data(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT)) {
            persistent.put(ROOT, new CompoundTag());
        }
        return persistent.getCompound(ROOT);
    }

    private static String sanitizeName(String value, String fallback) {
        String base = value == null ? "" : value.trim();
        if (base.isEmpty()) {
            base = fallback == null || fallback.isBlank() ? "Wanderer" : fallback;
        }
        base = base.replaceAll("[^A-Za-z0-9 _'\\-]", "").replaceAll("\\s+", " ").trim();
        if (base.isEmpty()) {
            base = "Wanderer";
        }
        if (base.length() > 24) {
            base = base.substring(0, 24).trim();
        }
        return base;
    }

    private static String genderFromSpecies(String speciesId) {
        String s = normalizeSpecies(speciesId);
        if ("player".equals(s)) {
            return "player";
        }
        return s.endsWith("_female") ? "female" : "male";
    }

    private static String normalizePlayerIdentity(String identityId) {
        Parsed parsed = parse(identityId);
        return parsed.speciesId() + "|" + parsed.robeId() + "|" + parsed.textureId() + "|" + parsed.eyeColor() + "|" + parsed.skinHex();
    }

    private static String normalizePlayerIdentity(ServerPlayer player, String identityId) {
        Parsed parsed = parse(identityId);
        String species = parsed.speciesId();
        String lockedRobe = lockRobeForSide(parsed.robeId(), committedSide(player));
        String robe = normalizeRobeForSpecies(species, lockedRobe);
        String texture = normalizeTextureForSpecies(species, parsed.textureId());
        String eye = normalizeEyeColor(parsed.eyeColor());
        String skin = normalizeSkinHex(parsed.skinHex());
        return species + "|" + robe + "|" + texture + "|" + eye + "|" + skin;
    }

    /**
     * After the first save, the player's biological identity is immutable.
     * Later saves are intentionally limited to name + robe choice; species,
     * texture variant, eye color, and skin color stay exactly as first chosen
     * until an admin clears the identity data.
     */
    private static String updateLockedIdentity(ServerPlayer player, String currentIdentityId, String requestedIdentityId) {
        Parsed current = parse(currentIdentityId);
        Parsed requested = parse(requestedIdentityId);
        String species = current.speciesId();
        String lockedRobe = lockRobeForSide(requested.robeId(), committedSide(player));
        String robe = normalizeRobeForSpecies(species, lockedRobe);
        String texture = normalizeTextureForSpecies(species, current.textureId());
        String eye = normalizeEyeColor(current.eyeColor());
        String skin = normalizeSkinHex(current.skinHex());
        return species + "|" + robe + "|" + texture + "|" + eye + "|" + skin;
    }

    private static ForceSide committedSide(ServerPlayer player) {
        return player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .filter(ForceSide::isSelectableBranch)
                .orElse(ForceSide.LIGHT);
    }

    private static String lockRobeForSide(String robeId, ForceSide side) {
        String robe = normalizeRobe(robeId);
        return switch (side) {
            case DARK -> robe.startsWith("sith_") ? robe : "sith_robes";
            case NEUTRAL -> robe.startsWith("neutral_") ? robe : "neutral_robes";
            case LIGHT, UNIVERSAL -> robe.startsWith("jedi_") ? robe : "jedi_robes";
        };
    }

    private static Parsed parse(String identityId) {
        if (identityId == null || identityId.isBlank()) {
            String species = DEFAULT_SPECIES;
            return new Parsed(species, DEFAULT_ROBE, defaultTextureForSpecies(species), DEFAULT_EYE, DEFAULT_SKIN_HEX);
        }
        String normalized = identityId.toLowerCase(Locale.ROOT).trim().replace(':', '|');
        String[] parts = normalized.split("\\|");
        if (parts.length >= 2) {
            String species = normalizeSpecies(parts[0]);
            String robe = normalizeRobeForSpecies(species, parts[1]);
            String texture = normalizeTextureForSpecies(species, parts.length >= 3 ? parts[2] : defaultTextureForSpecies(species));
            String eye = normalizeEyeColor(parts.length >= 4 ? parts[3] : DEFAULT_EYE);
            String skin = normalizeSkinHex(parts.length >= 5 ? parts[4] : DEFAULT_SKIN_HEX);
            return new Parsed(species, robe, texture, eye, skin);
        }
        if (isRobe(normalized)) {
            return new Parsed("player", normalizeRobe(normalized), "", DEFAULT_EYE, DEFAULT_SKIN_HEX);
        }
        if (normalized.startsWith("human_")) {
            return new Parsed("player", DEFAULT_ROBE, "", DEFAULT_EYE, DEFAULT_SKIN_HEX);
        }
        String species = normalizeSpecies(normalized);
        return new Parsed(species, normalizeRobeForSpecies(species, DEFAULT_ROBE), defaultTextureForSpecies(species), DEFAULT_EYE, DEFAULT_SKIN_HEX);
    }

    private static boolean isRobe(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return switch (id.toLowerCase(Locale.ROOT).trim()) {
            case "jedi_robes", "jedi_robes_alt", "sith_robes", "sith_robes_alt", "neutral_robes", "neutral_robes_alt" -> true;
            default -> false;
        };
    }

    private static String normalizeRobeForSpecies(String speciesId, String robeId) {
        String species = normalizeSpecies(speciesId);
        String robe = normalizeRobe(robeId);
        if ("player".equals(species)) {
            return robe;
        }
        Side wanted = robeSide(robe);
        if (isLightOriginalSpecies(species)) {
            return switch (wanted) {
                case DARK -> "sith_robes_alt";
                case NEUTRAL -> "neutral_robes";
                default -> "jedi_robes";
            };
        }
        return switch (wanted) {
            case LIGHT -> "jedi_robes_alt";
            case NEUTRAL -> "neutral_robes_alt";
            default -> "sith_robes";
        };
    }

    private static boolean isLightOriginalSpecies(String speciesId) {
        return switch (normalizeSpecies(speciesId)) {
            case "rodian_male", "rodian_female", "zabrak_male", "zabrak_female",
                 "twilek_male", "twilek_female", "togruta_male", "togruta_female" -> true;
            default -> false;
        };
    }

    private static String normalizeSpecies(String speciesId) {
        if (speciesId == null || speciesId.isBlank()) {
            return DEFAULT_SPECIES;
        }
        String s = speciesId.toLowerCase(Locale.ROOT).trim();
        return switch (s) {
            case "player" -> "player";
            case "cerean_male", "cerean_female", "chiss_male", "chiss_female",
                 "miraluka_male", "miraluka_female", "mirialan_male", "mirialan_female",
                 "rodian_male", "rodian_female", "sith_male", "sith_female",
                 "togruta_male", "togruta_female", "twilek_male", "twilek_female",
                 "zabrak_male", "zabrak_female" -> s;
            case "human_male", "human_female", "human_old_male", "human_old_female" -> "player";
            default -> DEFAULT_SPECIES;
        };
    }

    private static String normalizeRobe(String robeId) {
        if (robeId == null || robeId.isBlank()) {
            return DEFAULT_ROBE;
        }
        String s = robeId.toLowerCase(Locale.ROOT).trim();
        if (s.contains("|")) {
            String[] parts = s.split("\\|", 2);
            s = parts.length > 1 && !parts[1].isBlank() ? parts[1] : DEFAULT_ROBE;
        }
        if (s.contains(":")) {
            String[] parts = s.split(":", 2);
            s = parts.length > 1 && !parts[1].isBlank() ? parts[1] : DEFAULT_ROBE;
        }
        return switch (s) {
            case "jedi_robes", "jedi_robes_alt", "sith_robes", "sith_robes_alt", "neutral_robes", "neutral_robes_alt" -> s;
            default -> DEFAULT_ROBE;
        };
    }

    private static String normalizeTextureForSpecies(String speciesId, String textureId) {
        String species = normalizeSpecies(speciesId);
        if ("player".equals(species)) {
            return "";
        }
        String texture = textureId == null ? "" : textureId.toLowerCase(Locale.ROOT).trim();
        for (String option : textureVariants(species)) {
            if (option.equals(texture)) {
                return texture;
            }
        }
        return defaultTextureForSpecies(species);
    }

    private static String[] textureVariants(String speciesId) {
        return switch (normalizeSpecies(speciesId)) {
            case "mirialan_male" -> new String[] { "mirialan_male", "mirialan_male_2", "mirialan_male_3" };
            case "mirialan_female" -> new String[] { "mirialan_female", "mirialan_female_2", "mirialan_female_3" };
            case "rodian_male" -> new String[] { "rodian_male", "rodian_male_2", "rodian_male_3" };
            case "rodian_female" -> new String[] { "rodian_female", "rodian_female_2", "rodian_female_3" };
            case "zabrak_male" -> new String[] { "zabrack_male", "zabrack_male_2", "zabrack_male_3" };
            case "zabrak_female" -> new String[] { "zabrack_female", "zabrack_female_2", "zabrack_female_3" };
            case "twilek_male" -> new String[] { "twilek_male", "twilek_male_2", "twilek_male_3" };
            case "twilek_female" -> new String[] { "twilek_female", "twilek_female_2", "twilek_female_3" };
            case "togruta_male" -> new String[] { "togruta_male", "togruta_male_2", "togruta_male_3", "togruta_male_4" };
            case "togruta_female" -> new String[] { "togruta_female", "togruta_female_2", "togruta_female_3", "togruta_female_4" };
            case "sith_male" -> new String[] { "sith_male", "sith_younger_male" };
            default -> new String[] { defaultTextureForSpecies(speciesId) };
        };
    }

    private static String defaultTextureForSpecies(String speciesId) {
        return switch (normalizeSpecies(speciesId)) {
            case "zabrak_male" -> "zabrack_male";
            case "zabrak_female" -> "zabrack_female";
            case "sith_female" -> "sith_younger_female";
            case "player" -> "";
            default -> normalizeSpecies(speciesId);
        };
    }

    private static String normalizeEyeColor(String eyeColor) {
        if (eyeColor == null || eyeColor.isBlank()) {
            return DEFAULT_EYE;
        }
        return switch (eyeColor.toLowerCase(Locale.ROOT).trim()) {
            case "blue", "light_blue", "grey_blue", "green", "hazel", "brown", "dark_brown" -> eyeColor.toLowerCase(Locale.ROOT).trim();
            default -> DEFAULT_EYE;
        };
    }

    private static String normalizeSkinHex(String value) {
        String hex = value == null ? "" : value.trim().toUpperCase(Locale.ROOT).replace("#", "");
        if (!hex.matches("[0-9A-F]{6}")) {
            return DEFAULT_SKIN_HEX;
        }
        return hex;
    }

    private static Side robeSide(String robeId) {
        String robe = normalizeRobe(robeId);
        if (robe.startsWith("sith_")) {
            return Side.DARK;
        }
        if (robe.startsWith("neutral_")) {
            return Side.NEUTRAL;
        }
        return Side.LIGHT;
    }

    private enum Side {
        LIGHT,
        DARK,
        NEUTRAL
    }

    private record Parsed(String speciesId, String robeId, String textureId, String eyeColor, String skinHex) {}
}
