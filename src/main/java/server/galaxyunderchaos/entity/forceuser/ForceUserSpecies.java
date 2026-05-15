package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum ForceUserSpecies {
    CEREAN_MALE("cerean_male", "cerean_male", ForceUserSide.DARK, true),
    CEREAN_FEMALE("cerean_female", "cerean_female", ForceUserSide.DARK, true),
    CHISS_MALE("chiss_male", "chiss_male", ForceUserSide.DARK, true),
    CHISS_FEMALE("chiss_female", "chiss_female", ForceUserSide.DARK, true),
    HUMAN_MALE("human_male", "human_male", ForceUserSide.DARK, true),
    HUMAN_OLD_MALE("human_old_male", "human_old_male", ForceUserSide.DARK, true),
    HUMAN_OLD_FEMALE("human_old_female", "human_old_female", ForceUserSide.DARK, true),
    MIRALUKA_MALE("miraluka_male", "miraluka_male", ForceUserSide.DARK, true),
    MIRALUKA_FEMALE("miraluka_female", "miraluka_female", ForceUserSide.DARK, true),
    MIRIALAN_MALE("mirialan_male", "mirialan_male", ForceUserSide.DARK, true, "mirialan_male", "mirialan_male_2", "mirialan_male_3"),
    MIRIALAN_FEMALE("mirialan_female", "mirialan_female", ForceUserSide.DARK, true, "mirialan_female", "mirialan_female_2", "mirialan_female_3"),
    SITH_MALE("sith_male", "sith_male", ForceUserSide.DARK, true, "sith_male", "sith_younger_male"),
    SITH_FEMALE("sith_female", "sith_younger_female", ForceUserSide.DARK, true),

    RODIAN_MALE("rodian_male", "rodian_male", ForceUserSide.LIGHT, false, "rodian_male", "rodian_male_2", "rodian_male_3"),
    RODIAN_FEMALE("rodian_female", "rodian_female", ForceUserSide.LIGHT, false, "rodian_female", "rodian_female_2", "rodian_female_3"),
    ZABRAK_MALE("zabrak_male", "zabrack_male", ForceUserSide.LIGHT, false),
    ZABRAK_FEMALE("zabrak_female", "zabrack_female", ForceUserSide.LIGHT, false),
    TWILEK_MALE("twilek_male", "twilek_male", ForceUserSide.LIGHT, false, "twilek_male", "twilek_male_2", "twilek_male_3"),
    TWILEK_FEMALE("twilek_female", "twilek_female", ForceUserSide.LIGHT, false, "twilek_female", "twilek_female_2", "twilek_female_3"),
    TOGRUTA_MALE("togruta_male", "togruta_male", ForceUserSide.LIGHT, false, "togruta_male", "togruta_male_2", "togruta_male_3", "togruta_male_4"),
    TOGRUTA_FEMALE("togruta_female", "togruta_female", ForceUserSide.LIGHT, false, "togruta_female", "togruta_female_2", "togruta_female_3", "togruta_female_4");

    private static final List<ForceUserSpecies> VALUES = List.of(values());

    private final String modelId;
    private final String fallbackTextureId;
    private final ForceUserSide originalSide;
    private final boolean externalRobeModel;
    private final String[] textureVariants;

    ForceUserSpecies(String modelId, String fallbackTextureId, ForceUserSide originalSide, boolean externalRobeModel, String... textureVariants) {
        this.modelId = modelId;
        this.fallbackTextureId = fallbackTextureId;
        this.originalSide = originalSide;
        this.externalRobeModel = externalRobeModel;
        this.textureVariants = textureVariants.length == 0 ? new String[] { fallbackTextureId } : textureVariants;
    }

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String modelId() {
        return modelId;
    }

    public ForceUserSide originalSide() {
        return originalSide;
    }

    public boolean usesExternalRobeModel() {
        return externalRobeModel;
    }

    public boolean usesEmbeddedJediOriginalRobe() {
        return this == RODIAN_MALE
                || this == RODIAN_FEMALE
                || this == ZABRAK_MALE
                || this == ZABRAK_FEMALE
                || this == TWILEK_MALE
                || this == TWILEK_FEMALE
                || this == TOGRUTA_MALE
                || this == TOGRUTA_FEMALE;
    }

    public boolean usesSithOriginalRobeSet() {
        return !usesEmbeddedJediOriginalRobe();
    }

    public String randomTexture(RandomSource random) {
        return textureVariants[random.nextInt(textureVariants.length)];
    }

    public ResourceLocation texture(String textureId) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/force_user/" + textureId + ".png");
    }

    public static ForceUserSpecies random(RandomSource random) {
        return VALUES.get(random.nextInt(VALUES.size()));
    }

    public static ForceUserSpecies byModelId(String modelId) {
        return Arrays.stream(values())
                .filter(species -> species.modelId.equals(modelId))
                .findFirst()
                .orElse(HUMAN_MALE);
    }

    public static ForceUserSpecies byId(String id) {
        if (id != null) {
            for (ForceUserSpecies species : values()) {
                if (species.id().equals(id)) {
                    return species;
                }
            }
        }
        return HUMAN_MALE;
    }
}
