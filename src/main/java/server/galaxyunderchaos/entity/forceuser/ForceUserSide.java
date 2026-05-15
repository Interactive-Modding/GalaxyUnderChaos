package server.galaxyunderchaos.entity.forceuser;

public enum ForceUserSide {
    LIGHT,
    DARK;

    public boolean isDark() {
        return this == DARK;
    }

    public boolean isLight() {
        return this == LIGHT;
    }

    public String serializedName() {
        return this == DARK ? "sith" : "jedi";
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
            case "light", "jedi" -> LIGHT;
            default -> null;
        };
    }
}
