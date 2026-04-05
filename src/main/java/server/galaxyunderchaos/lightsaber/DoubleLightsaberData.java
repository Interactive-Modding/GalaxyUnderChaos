package server.galaxyunderchaos.lightsaber;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.DoubleLightsaberItem;
import server.galaxyunderchaos.item.LightsaberItem;

public final class DoubleLightsaberData {
    public static final String DOUBLE_ROOT_TAG = "DoubleLightsaberData";
    private static final String UPPER_TAG = "Upper";
    private static final String LOWER_TAG = "Lower";

    private DoubleLightsaberData() {
    }

    public static boolean isDoubleLightsaber(ItemStack stack) {
        return stack.getItem() instanceof DoubleLightsaberItem;
    }

    public static boolean canCreateFrom(ItemStack stack) {
        if (!(stack.getItem() instanceof LightsaberItem) || stack.getItem() instanceof DoubleLightsaberItem) {
            return false;
        }
        return ModularLightsaberData.resolveLegacyFamilyFromItem(stack) != null;
    }

    public static ItemStack create(ItemStack upper, ItemStack lower) {
        ItemStack result = new ItemStack(galaxyunderchaos.DOUBLE_LIGHTSABER.get());
        CompoundTag root = result.getOrCreateTagElement(DOUBLE_ROOT_TAG);
        root.put(UPPER_TAG, createEndTag(upper));
        root.put(LOWER_TAG, createEndTag(lower));
        ModularLightsaberData.copyActivationState(upper, result);
        return result;
    }

    private static CompoundTag createEndTag(ItemStack source) {
        CompoundTag end = new CompoundTag();
        String fallbackFamily = ModularLightsaberData.resolveLegacyFamilyFromItem(source);
        if (fallbackFamily == null) {
            fallbackFamily = "mauler";
        }

        end.putString(ModularLightsaberData.BLADE_COLOR_TAG, LightsaberItem.getBladeColor(source));
        end.putString(ModularLightsaberData.PRIMARY_HILT_ID_TAG, ModularLightsaberData.getPrimaryHiltId(source, fallbackFamily));
        for (LightsaberPartType type : LightsaberPartType.values()) {
            end.putString(type.getSerializedName(), ModularLightsaberData.getPartFamily(source, type, fallbackFamily));
        }
        ModularLightsaberData.writeBladeModifiers(end, ModularLightsaberData.getBladeModifiers(source));
        return end;
    }

    @Nullable
    public static CompoundTag getEndTag(ItemStack stack, boolean upper) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(DOUBLE_ROOT_TAG, 10)) {
            return null;
        }
        CompoundTag root = tag.getCompound(DOUBLE_ROOT_TAG);
        String key = upper ? UPPER_TAG : LOWER_TAG;
        return root.contains(key, 10) ? root.getCompound(key) : null;
    }

    public static String getBladeColor(ItemStack stack, boolean upper, String fallback) {
        CompoundTag end = getEndTag(stack, upper);
        return end != null && end.contains(ModularLightsaberData.BLADE_COLOR_TAG)
                ? end.getString(ModularLightsaberData.BLADE_COLOR_TAG)
                : fallback;
    }

    public static String getPrimaryHiltId(ItemStack stack, boolean upper, String fallback) {
        CompoundTag end = getEndTag(stack, upper);
        return end != null && end.contains(ModularLightsaberData.PRIMARY_HILT_ID_TAG)
                ? end.getString(ModularLightsaberData.PRIMARY_HILT_ID_TAG)
                : fallback;
    }

    public static String getPartFamily(ItemStack stack, boolean upper, LightsaberPartType type, String fallback) {
        CompoundTag end = getEndTag(stack, upper);
        return end != null && end.contains(type.getSerializedName())
                ? end.getString(type.getSerializedName())
                : fallback;
    }

    public static java.util.EnumSet<BladeModifierCrystal> getBladeModifiers(ItemStack stack, boolean upper) {
        return ModularLightsaberData.getBladeModifiers(getEndTag(stack, upper));
    }

    public static ItemStack createRenderStack(ItemStack doubleStack, boolean upper, boolean active) {
        String fallback = getPrimaryHiltId(doubleStack, upper, "mauler");
        ItemStack renderStack = new ItemStack(galaxyunderchaos.CUSTOM_LIGHTSABER.get());
        CompoundTag data = ModularLightsaberData.getOrCreateDataTag(renderStack);
        data.putString(ModularLightsaberData.BLADE_COLOR_TAG, getBladeColor(doubleStack, upper, "red"));
        data.putString(ModularLightsaberData.PRIMARY_HILT_ID_TAG, getPrimaryHiltId(doubleStack, upper, fallback));
        for (LightsaberPartType type : LightsaberPartType.values()) {
            data.putString(type.getSerializedName(), getPartFamily(doubleStack, upper, type, fallback));
        }
        ModularLightsaberData.writeBladeModifiers(data, getBladeModifiers(doubleStack, upper));
        renderStack.getOrCreateTag().putBoolean("LightsaberActive", active);
        return renderStack;
    }

    public static float getTotalHeight(ItemStack stack, boolean upper, String fallback) {
        float total = 0.0F;
        for (LightsaberPartType type : LightsaberPartType.values()) {
            total += ModularLightsaberData.getLegacyHeight(getPartFamily(stack, upper, type, fallback), type);
        }
        return total;
    }
}
