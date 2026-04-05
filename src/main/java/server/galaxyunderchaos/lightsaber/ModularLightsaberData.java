package server.galaxyunderchaos.lightsaber;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.HiltItem;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.item.LightsaberPartItem;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public final class ModularLightsaberData {
    public static final String MODULAR_ROOT_TAG = "AdvancedLightsaberData";
    public static final String BLADE_COLOR_TAG = "BladeColor";
    public static final String PRIMARY_HILT_ID_TAG = "PrimaryHiltId";
    public static final String BLADE_MODIFIERS_TAG = "BladeModifiers";
    public static final int MAX_BLADE_MODIFIERS = 2;
    private static final String ACTIVE_TAG = "LightsaberActive";

    private ModularLightsaberData() {
    }

    public static boolean hasData(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(MODULAR_ROOT_TAG, Tag.TAG_COMPOUND);
    }

    public static CompoundTag getOrCreateDataTag(ItemStack stack) {
        return stack.getOrCreateTagElement(MODULAR_ROOT_TAG);
    }

    @Nullable
    public static CompoundTag getDataTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(MODULAR_ROOT_TAG, Tag.TAG_COMPOUND) ? tag.getCompound(MODULAR_ROOT_TAG) : null;
    }

    public static void applyPreset(ItemStack stack, String bladeColor, String legacyHiltId) {
        CompoundTag data = getOrCreateDataTag(stack);
        data.putString(BLADE_COLOR_TAG, bladeColor);
        data.putString(PRIMARY_HILT_ID_TAG, legacyHiltId);
        for (LightsaberPartType type : LightsaberPartType.values()) {
            data.putString(type.getSerializedName(), legacyHiltId);
        }

        AdvancedLightsaberLegacyHilts.LegacyHiltSpec spec = getLegacySpec(legacyHiltId);
        if (spec != null) {
            writeBladeModifiers(data, Arrays.asList(spec.defaultBladeModifiers()));
        } else {
            writeBladeModifiers(data, Collections.emptySet());
        }
    }

    public static void applyAssembly(ItemStack stack, String bladeColor, String emitter, String switchSection, String grip, String pommel) {
        CompoundTag data = getOrCreateDataTag(stack);
        data.putString(BLADE_COLOR_TAG, bladeColor);
        data.putString(PRIMARY_HILT_ID_TAG, emitter);
        data.putString(LightsaberPartType.EMITTER.getSerializedName(), emitter);
        data.putString(LightsaberPartType.SWITCH_SECTION.getSerializedName(), switchSection);
        data.putString(LightsaberPartType.GRIP.getSerializedName(), grip);
        data.putString(LightsaberPartType.POMMEL.getSerializedName(), pommel);
        writeBladeModifiers(data, Collections.emptySet());
    }

    public static void copyActivationState(ItemStack from, ItemStack to) {
        if (from.hasTag() && from.getTag().contains(ACTIVE_TAG)) {
            to.getOrCreateTag().putBoolean(ACTIVE_TAG, from.getTag().getBoolean(ACTIVE_TAG));
        }
    }

    public static String getBladeColor(ItemStack stack, String fallback) {
        CompoundTag data = getDataTag(stack);
        if (data != null && data.contains(BLADE_COLOR_TAG)) {
            return data.getString(BLADE_COLOR_TAG);
        }
        return fallback;
    }

    public static String getPrimaryHiltId(ItemStack stack, String fallback) {
        CompoundTag data = getDataTag(stack);
        if (data != null && data.contains(PRIMARY_HILT_ID_TAG)) {
            return data.getString(PRIMARY_HILT_ID_TAG);
        }
        return fallback;
    }

    public static String getPartFamily(ItemStack stack, LightsaberPartType type, String fallback) {
        CompoundTag data = getDataTag(stack);
        if (data != null && data.contains(type.getSerializedName())) {
            return data.getString(type.getSerializedName());
        }
        return fallback;
    }

    public static Map<LightsaberPartType, String> getPartFamilies(ItemStack stack, String fallback) {
        Map<LightsaberPartType, String> map = new EnumMap<>(LightsaberPartType.class);
        for (LightsaberPartType type : LightsaberPartType.values()) {
            map.put(type, getPartFamily(stack, type, fallback));
        }
        return map;
    }

    public static EnumSet<BladeModifierCrystal> getBladeModifiers(ItemStack stack) {
        return getBladeModifiers(getDataTag(stack));
    }

    public static EnumSet<BladeModifierCrystal> getBladeModifiers(@Nullable CompoundTag data) {
        EnumSet<BladeModifierCrystal> modifiers = EnumSet.noneOf(BladeModifierCrystal.class);
        if (data == null || !data.contains(BLADE_MODIFIERS_TAG, Tag.TAG_LIST)) {
            return modifiers;
        }

        ListTag list = data.getList(BLADE_MODIFIERS_TAG, Tag.TAG_STRING);
        for (int i = 0; i < list.size(); ++i) {
            BladeModifierCrystal crystal = BladeModifierCrystal.fromSerializedName(list.getString(i));
            if (crystal != null) {
                modifiers.add(crystal);
            }
        }
        return modifiers;
    }

    public static boolean hasBladeModifier(ItemStack stack, BladeModifierCrystal crystal) {
        return getBladeModifiers(stack).contains(crystal);
    }

    public static void setBladeModifiers(ItemStack stack, Collection<BladeModifierCrystal> modifiers) {
        writeBladeModifiers(getOrCreateDataTag(stack), modifiers);
    }

    public static boolean addBladeModifier(ItemStack stack, BladeModifierCrystal crystal) {
        EnumSet<BladeModifierCrystal> modifiers = getBladeModifiers(stack);
        if (modifiers.contains(crystal) || modifiers.size() >= MAX_BLADE_MODIFIERS) {
            return false;
        }
        modifiers.add(crystal);
        setBladeModifiers(stack, modifiers);
        return true;
    }

    public static void writeBladeModifiers(CompoundTag data, Collection<BladeModifierCrystal> modifiers) {
        ListTag list = new ListTag();
        int added = 0;
        for (BladeModifierCrystal crystal : modifiers) {
            if (added >= MAX_BLADE_MODIFIERS) {
                break;
            }
            list.add(StringTag.valueOf(crystal.getSerializedName()));
            added++;
        }
        data.put(BLADE_MODIFIERS_TAG, list);
    }

    public static boolean isLegacyFamily(String familyId) {
        return AdvancedLightsaberLegacyHilts.HILTS.containsKey(familyId);
    }

    public static AdvancedLightsaberLegacyHilts.LegacyHiltSpec getLegacySpec(String familyId) {
        return AdvancedLightsaberLegacyHilts.HILTS.get(familyId);
    }

    public static float[] getPommelAlignmentOps(String familyId) {
        AdvancedLightsaberLegacyHilts.LegacyHiltSpec spec = getLegacySpec(familyId);
        return spec == null ? new float[0] : spec.pommelAlignmentOps();
    }

    public static float getLegacyHeight(String familyId, LightsaberPartType type) {
        AdvancedLightsaberLegacyHilts.LegacyHiltSpec spec = getLegacySpec(familyId);
        if (spec == null) {
            return 10.0F;
        }
        return switch (type) {
            case EMITTER -> spec.emitterHeight();
            case SWITCH_SECTION -> spec.switchSectionHeight();
            case GRIP -> spec.bodyHeight();
            case POMMEL -> spec.pommelHeight();
        };
    }

    public static ResourceLocation getPreferredPartTexture(String familyId, LightsaberPartType type) {
        return new ResourceLocation(
                galaxyunderchaos.MODID,
                "textures/models/lightsaber/" + type.getLegacyTexturePrefix() + "_" + familyId + ".png"
        );
    }

    public static ResourceLocation getFamilyTexture(String familyId) {
        return new ResourceLocation(
                galaxyunderchaos.MODID,
                "textures/models/lightsaber/" + familyId + ".png"
        );
    }

    public static ResourceLocation getPartTexture(String familyId, LightsaberPartType type) {
        return isLegacyFamily(familyId) ? getPreferredPartTexture(familyId, type) : getFamilyTexture(familyId);
    }

    public static boolean shouldRenderCrossguard(ItemStack stack, String fallbackFamily) {
        String emitter = getPartFamily(stack, LightsaberPartType.EMITTER, fallbackFamily);
        AdvancedLightsaberLegacyHilts.LegacyHiltSpec spec = getLegacySpec(emitter);
        return spec != null && spec.hasCrossguard();
    }

    public static boolean shouldRenderSecondBlade(ItemStack stack, String fallbackFamily) {
        String primary = getPrimaryHiltId(stack, fallbackFamily);
        AdvancedLightsaberLegacyHilts.LegacyHiltSpec spec = getLegacySpec(primary);
        return spec != null && spec.doubleBladed();
    }

    public static ItemStack createCustomLightsaber(String bladeColor, String emitter, String switchSection, String grip, String pommel) {
        ItemStack result = new ItemStack(galaxyunderchaos.CUSTOM_LIGHTSABER.get());
        applyAssembly(result, bladeColor, emitter, switchSection, grip, pommel);
        return result;
    }

    public static ItemStack createCustomLightsaberFromPreset(String bladeColor, String legacyHiltId) {
        ItemStack result = new ItemStack(galaxyunderchaos.CUSTOM_LIGHTSABER.get());
        applyPreset(result, bladeColor, legacyHiltId);
        return result;
    }

    @Nullable
    public static String resolveLegacyFamilyFromItem(ItemStack stack) {
        if (stack.getItem() instanceof LightsaberPartItem partItem) {
            return partItem.getFamilyId();
        }
        if (stack.getItem() instanceof HiltItem hiltItem) {
            String hiltId = hiltItem.getHiltId();
            if (isLegacyFamily(hiltId)) {
                return hiltId;
            }
        }
        if (stack.getItem() instanceof LightsaberItem lightsaberItem) {
            String hiltId = lightsaberItem.getHiltId(stack);
            if (isLegacyFamily(hiltId)) {
                return hiltId;
            }
        }
        ResourceLocation key = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (key == null) {
            return null;
        }
        String path = key.getPath();
        for (String family : AdvancedLightsaberLegacyHilts.HILTS.keySet()) {
            if (path.equals(family + "_hilt") || path.startsWith(family + "_")) {
                return family;
            }
        }
        return null;
    }
}
