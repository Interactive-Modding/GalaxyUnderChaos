package client.renderer.forceuser;

import server.galaxyunderchaos.force.PlayerForceIdentitySyncPacket;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Client cache for player Force identity render overrides. */
public final class PlayerForceIdentityClientState {
    private static final Map<UUID, Entry> ENTRIES = new ConcurrentHashMap<>();
    private static final String DEFAULT_SPECIES = "mirialan_male";
    private static final String DEFAULT_ROBE = "jedi_robes";
    private static final String DEFAULT_EYE = "blue";
    private static final String DEFAULT_SKIN_HEX = "C68642";

    private PlayerForceIdentityClientState() {}

    public static void update(PlayerForceIdentitySyncPacket packet) {
        if (packet == null || packet.playerId().getLeastSignificantBits() == 0L && packet.playerId().getMostSignificantBits() == 0L) {
            return;
        }
        if (!packet.hasIdentity()) {
            ENTRIES.remove(packet.playerId());
            return;
        }
        String normalized = normalizeIdentity(packet.speciesId());
        ENTRIES.put(packet.playerId(), new Entry(packet.forceName(), packet.displayTitle(), normalized, packet.genderId(), packet.darkEyes()));
    }

    public static Entry get(UUID uuid) {
        return uuid == null ? null : ENTRIES.get(uuid);
    }

    /**
     * Temporarily replaces a client-side identity only while a GUI preview is rendering.
     * The caller must restore the returned entry in a finally block.
     */
    public static Entry putTemporaryPreview(UUID uuid, Entry preview) {
        if (uuid == null || preview == null) {
            return null;
        }
        return ENTRIES.put(uuid, preview);
    }

    public static void restoreTemporaryPreview(UUID uuid, Entry previous) {
        if (uuid == null) {
            return;
        }
        if (previous == null) {
            ENTRIES.remove(uuid);
        } else {
            ENTRIES.put(uuid, previous);
        }
    }

    public static boolean hasVisibleOverride(UUID uuid) {
        Entry entry = get(uuid);
        return entry != null && (isPlayerRobeMode(entry.speciesId()) || hasAlienSpecies(entry.speciesId()));
    }

    public static boolean hasAlienSpecies(UUID uuid) {
        Entry entry = get(uuid);
        return entry != null && hasAlienSpecies(entry.speciesId());
    }

    public static boolean hasAlienSpecies(String identityId) {
        return isAlienSpecies(speciesId(identityId));
    }

    public static boolean isPlayerRobeMode(String identityId) {
        return "player".equals(speciesId(identityId));
    }

    public static String modelId(String identityId) {
        String s = speciesId(identityId);
        return switch (s) {
            case "zabrak_male" -> "zabrak_male";
            case "zabrak_female" -> "zabrak_female";
            default -> s;
        };
    }

    public static String textureId(String identityId) {
        Parsed parsed = parse(identityId);
        if (isAlienSpecies(parsed.speciesId())) {
            return normalizeTextureForSpecies(parsed.speciesId(), parsed.textureId());
        }
        return defaultTextureForSpecies(parsed.speciesId());
    }

    /**
     * Returns the model that should be rendered for the selected robe look.
     * Neutral robes do not need duplicate model classes: they reuse the existing
     * Jedi/Sith-compatible robe rigs and only swap texture sheets.
     */
    public static String robeModelId(String identityId) {
        Parsed parsed = parse(identityId);
        String species = parsed.speciesId();
        String robe = parsed.robeId();
        if ("player".equals(species)) {
            return playerRobeModel(robe);
        }

        Side wanted = robeSide(robe);
        Side origin = originalSide(species);
        if (wanted == origin) {
            return "";
        }
        if (wanted == Side.NEUTRAL) {
            return origin == Side.LIGHT ? "jedi_robes" : "sith_robes";
        }
        return origin == Side.LIGHT ? "sith_robes_alt" : "jedi_robes_alt";
    }

    public static String robeTextureId(String identityId) {
        Parsed parsed = parse(identityId);
        String model = robeModelId(identityId);
        if (model == null || model.isBlank()) {
            return "";
        }
        return parsed.robeId();
    }

    public static String skinOverlayTextureId(String identityId) {
        String model = robeModelId(identityId);
        return usesWideRobeSheet(model) ? "jedi_robes_overlay" : "sith_robes_overlay";
    }

    public static float skinRed(String identityId) {
        return ((skinColor(identityId) >> 16) & 0xFF) / 255.0F;
    }

    public static float skinGreen(String identityId) {
        return ((skinColor(identityId) >> 8) & 0xFF) / 255.0F;
    }

    public static float skinBlue(String identityId) {
        return (skinColor(identityId) & 0xFF) / 255.0F;
    }

    public static Side selectedLookSide(String identityId) {
        return robeSide(robeId(identityId));
    }

    public static String speciesId(String identityId) {
        return parse(identityId).speciesId();
    }

    public static String robeId(String identityId) {
        return parse(identityId).robeId();
    }

    public static String textureVariantId(String identityId) {
        Parsed parsed = parse(identityId);
        return normalizeTextureForSpecies(parsed.speciesId(), parsed.textureId());
    }

    public static String eyeColorId(String identityId) {
        return parse(identityId).eyeColor();
    }

    public static String skinColorHex(String identityId) {
        return parse(identityId).skinHex();
    }

    public static String normalizeIdentity(String identityId) {
        Parsed parsed = parse(identityId);
        String species = normalizeSpecies(parsed.speciesId());
        String robe = normalizeRobeForSpecies(species, parsed.robeId());
        String texture = normalizeTextureForSpecies(species, parsed.textureId());
        String eye = normalizeEyeColor(parsed.eyeColor());
        String skin = normalizeSkinHex(parsed.skinHex());
        return species + "|" + robe + "|" + texture + "|" + eye + "|" + skin;
    }

    public static boolean isRobe(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return switch (id.toLowerCase(Locale.ROOT).trim()) {
            case "jedi_robes", "jedi_robes_alt", "sith_robes", "sith_robes_alt", "neutral_robes", "neutral_robes_alt" -> true;
            default -> false;
        };
    }

    public static boolean isAlienSpecies(String id) {
        return switch (normalizeSpecies(id)) {
            case "cerean_male", "cerean_female", "chiss_male", "chiss_female",
                 "miraluka_male", "miraluka_female", "mirialan_male", "mirialan_female",
                 "rodian_male", "rodian_female", "sith_male", "sith_female",
                 "togruta_male", "togruta_female", "twilek_male", "twilek_female",
                 "zabrak_male", "zabrak_female" -> true;
            default -> false;
        };
    }

    public static boolean isLightOriginalSpecies(String id) {
        return originalSide(normalizeSpecies(id)) == Side.LIGHT;
    }

    public static boolean isDarkOriginalSpecies(String id) {
        return originalSide(normalizeSpecies(id)) == Side.DARK;
    }

    public static boolean hasEyeColorOptions(String identityId) {
        String species = speciesId(identityId);
        return hasEyeColorOptionsForSpecies(species);
    }

    public static boolean hasEyeColorOptionsForSpecies(String speciesId) {
        String species = normalizeSpecies(speciesId);
        return isAlienSpecies(species) && !species.startsWith("chiss_") && !species.startsWith("miraluka_");
    }

    public static String speciesDisplayName(String id) {
        return switch (normalizeSpecies(id)) {
            case "player" -> "Robe Body";
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
            default -> "Mirialan Male";
        };
    }

    public static String robeDisplayName(String id) {
        return switch (normalizeRobe(id)) {
            case "jedi_robes" -> "Jedi Robes";
            case "jedi_robes_alt" -> "Jedi Robes Alt";
            case "sith_robes" -> "Sith Robes";
            case "sith_robes_alt" -> "Sith Robes Alt";
            case "neutral_robes" -> "Neutral Robes";
            case "neutral_robes_alt" -> "Neutral Robes Alt";
            default -> "Jedi Robes";
        };
    }

    public static boolean isJediOriginSpecies(String identityId) {
        return originalSide(speciesId(identityId)) == Side.LIGHT;
    }

    public static boolean isChiss(String identityId) {
        return speciesId(identityId).startsWith("chiss_");
    }

    public static String normalizeRobeForSpecies(String speciesId, String robeId) {
        String species = normalizeSpecies(speciesId);
        String robe = normalizeRobe(robeId);
        if ("player".equals(species)) {
            return robe;
        }
        Side wanted = robeSide(robe);
        Side origin = originalSide(species);
        if (origin == Side.LIGHT) {
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

    private static String normalizeSpecies(String speciesId) {
        if (speciesId == null || speciesId.isBlank()) {
            return DEFAULT_SPECIES;
        }
        String s = speciesId.toLowerCase(Locale.ROOT).trim();
        if (s.contains("|")) {
            s = s.split("\\|", 2)[0];
        }
        if (s.contains(":")) {
            s = s.split(":", 2)[0];
        }
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

    private static int skinColor(String identityId) {
        try {
            return Integer.parseInt(skinColorHex(identityId), 16);
        } catch (NumberFormatException ignored) {
            return Integer.parseInt(DEFAULT_SKIN_HEX, 16);
        }
    }

    private static boolean usesWideRobeSheet(String modelId) {
        return "jedi_robes".equals(modelId) || "sith_robes_alt".equals(modelId);
    }

    private static String playerRobeModel(String robeId) {
        return switch (normalizeRobe(robeId)) {
            case "neutral_robes" -> "jedi_robes";
            case "neutral_robes_alt" -> "sith_robes";
            default -> normalizeRobe(robeId);
        };
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

    private static Side originalSide(String speciesId) {
        return switch (normalizeSpecies(speciesId)) {
            case "rodian_male", "rodian_female", "zabrak_male", "zabrak_female",
                 "twilek_male", "twilek_female", "togruta_male", "togruta_female" -> Side.LIGHT;
            default -> Side.DARK;
        };
    }

    public enum Side {
        LIGHT,
        DARK,
        NEUTRAL
    }

    private record Parsed(String speciesId, String robeId, String textureId, String eyeColor, String skinHex) {}

    public record Entry(String forceName, String displayTitle, String speciesId, String genderId, boolean darkEyes) {}
}
