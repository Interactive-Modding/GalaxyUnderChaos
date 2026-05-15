package server.galaxyunderchaos.ship;

import net.minecraft.network.chat.Component;

public enum ShipColorSection {
    BASE("base", "Base"),
    PRIMARY("primary", "Primary"),
    SECONDARY("secondary", "Secondary"),
    INTERIOR("interior", "Interior");

    private final String serializedName;
    private final String displayName;

    ShipColorSection(String serializedName, String displayName) {
        this.serializedName = serializedName;
        this.displayName = displayName;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public Component getDisplayName() {
        return Component.literal(displayName);
    }

    public String getNbtKey() {
        return "Ship" + displayName + "Color";
    }

    public static ShipColorSection byOrdinal(int ordinal) {
        ShipColorSection[] values = values();
        return ordinal >= 0 && ordinal < values.length ? values[ordinal] : BASE;
    }
}
