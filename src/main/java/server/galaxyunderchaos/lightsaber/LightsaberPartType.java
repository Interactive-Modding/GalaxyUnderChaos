package server.galaxyunderchaos.lightsaber;

public enum LightsaberPartType {
    EMITTER("emitter", "Emitter"),
    SWITCH_SECTION("switch_section", "Switch Section"),
    GRIP("grip", "Grip"),
    POMMEL("pommel", "Pommel");

    private final String serializedName;
    private final String displayName;

    LightsaberPartType(String serializedName, String displayName) {
        this.serializedName = serializedName;
        this.displayName = displayName;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLegacyTexturePrefix() {
        return this == GRIP ? "body" : serializedName;
    }

    public static LightsaberPartType bySerializedName(String name) {
        for (LightsaberPartType type : values()) {
            if (type.serializedName.equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown lightsaber part type: " + name);
    }
}
