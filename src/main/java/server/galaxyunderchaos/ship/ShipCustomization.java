package server.galaxyunderchaos.ship;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.ShipBlueprintItem;

import java.util.List;

public final class ShipCustomization {
    public static final int DEFAULT_BASE = 0xFFFFFF;
    public static final int DEFAULT_PRIMARY = 0xFFFFFF;
    public static final int DEFAULT_SECONDARY = 0xFFFFFF;
    public static final int DEFAULT_INTERIOR = 0xFFFFFF;

    public static final String TAG_CUSTOMIZED = "CustomizedShip";
    public static final String TAG_SHIP_ID = "ShipId";

    private ShipCustomization() {
    }

    public static int clampColor(int color) {
        return color & 0xFFFFFF;
    }

    public static int getDefaultColor(ShipColorSection section) {
        return switch (section) {
            case BASE -> DEFAULT_BASE;
            case PRIMARY -> DEFAULT_PRIMARY;
            case SECONDARY -> DEFAULT_SECONDARY;
            case INTERIOR -> DEFAULT_INTERIOR;
        };
    }

    public static int getColor(ItemStack stack, ShipColorSection section) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(section.getNbtKey())) {
            return clampColor(tag.getInt(section.getNbtKey()));
        }
        return getDefaultColor(section);
    }

    public static void setColor(ItemStack stack, ShipColorSection section, int color) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(TAG_CUSTOMIZED, true);
        tag.putInt(section.getNbtKey(), clampColor(color));
    }

    public static void setShipId(ItemStack stack, String shipId) {
        stack.getOrCreateTag().putString(TAG_SHIP_ID, shipId);
    }

    public static String getShipId(ItemStack stack, String fallback) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TAG_SHIP_ID)) {
            return tag.getString(TAG_SHIP_ID);
        }
        return fallback;
    }

    public static boolean isCustomized(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(TAG_CUSTOMIZED);
    }

    public static boolean isCustomized(CustomizableShip ship) {
        for (ShipColorSection section : ShipColorSection.values()) {
            if (ship.getShipColor(section) != getDefaultColor(section)) {
                return true;
            }
        }
        return false;
    }

    public static void applyToShip(ItemStack stack, CustomizableShip ship) {
        for (ShipColorSection section : ShipColorSection.values()) {
            ship.setShipColor(section, getColor(stack, section));
        }
    }

    public static void saveToStack(CustomizableShip ship, ItemStack stack, String shipId) {
        setShipId(stack, shipId);
        boolean customized = false;
        for (ShipColorSection section : ShipColorSection.values()) {
            int color = clampColor(ship.getShipColor(section));
            stack.getOrCreateTag().putInt(section.getNbtKey(), color);
            if (color != getDefaultColor(section)) {
                customized = true;
            }
        }
        stack.getOrCreateTag().putBoolean(TAG_CUSTOMIZED, customized);
    }

    public static ItemStack createShipStack(String shipId, int base, int primary, int secondary, int interior) {
        Item shipItem = getShipItem(shipId);
        if (shipItem == null) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack(shipItem);
        setShipId(stack, shipId);
        setColor(stack, ShipColorSection.BASE, base);
        setColor(stack, ShipColorSection.PRIMARY, primary);
        setColor(stack, ShipColorSection.SECONDARY, secondary);
        setColor(stack, ShipColorSection.INTERIOR, interior);
        return stack;
    }

    public static Item getShipItem(String shipId) {
        return switch (shipId) {
            case "novadive" -> galaxyunderchaos.NOVADIVE.get();
            case "flashfire" -> galaxyunderchaos.FLASHFIRE.get();
            default -> null;
        };
    }

    public static String getBlueprintShipId(ItemStack stack) {
        if (stack.getItem() instanceof ShipBlueprintItem blueprintItem) {
            return blueprintItem.getShipId();
        }
        return "";
    }

    public static boolean isBlueprint(ItemStack stack) {
        return stack.getItem() instanceof ShipBlueprintItem;
    }

    public static void addTooltip(ItemStack stack, List<Component> tooltip) {
        if (!isCustomized(stack)) {
            tooltip.add(Component.literal("Default factory colors").withStyle(ChatFormatting.GRAY));
            return;
        }

        tooltip.add(Component.literal("Custom Ship Colors").withStyle(ChatFormatting.AQUA));
        for (ShipColorSection section : ShipColorSection.values()) {
            tooltip.add(Component.literal(section.getDisplayName().getString() + ": #" + toHex(getColor(stack, section))).withStyle(ChatFormatting.GRAY));
        }
    }

    public static String toHex(int color) {
        return String.format("%06X", clampColor(color));
    }
}
