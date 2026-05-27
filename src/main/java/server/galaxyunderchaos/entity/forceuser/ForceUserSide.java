package server.galaxyunderchaos.entity.forceuser;

public enum ForceUserSide {
    LIGHT,
    DARK,
    NEUTRAL;

    public boolean isDark() {
        return this == DARK;
    }

    public boolean isLight() {
        return this == LIGHT;
    }

    public boolean isNeutral() {
        return this == NEUTRAL;
    }

    public boolean isAlignedAgainstDark() {
        return this == LIGHT || this == NEUTRAL;
    }

    public String serializedName() {
        return switch (this) {
            case DARK -> "sith";
            case NEUTRAL -> "neutral";
            case LIGHT -> "jedi";
        };
    }

    public server.galaxyunderchaos.force.ForceSide toCapabilitySide() {
        return switch (this) {
            case DARK -> server.galaxyunderchaos.force.ForceSide.DARK;
            case NEUTRAL -> server.galaxyunderchaos.force.ForceSide.NEUTRAL;
            case LIGHT -> server.galaxyunderchaos.force.ForceSide.LIGHT;
        };
    }

    public static ForceUserSide byName(String name, ForceUserSide fallback) {
        ForceUserSide resolved = bySerializedName(name);
        return resolved == null ? fallback : resolved;
    }

    public static ForceUserSide bySerializedName(String name) {
        if (name == null) {
            return null;
        }
        return switch (name.toLowerCase(java.util.Locale.ROOT)) {
            case "dark", "sith" -> DARK;
            case "neutral", "ancient", "force_user", "force-user" -> NEUTRAL;
            case "light", "jedi" -> LIGHT;
            default -> null;
        };
    }
}
