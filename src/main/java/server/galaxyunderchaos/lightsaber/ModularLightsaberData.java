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
    public static final String PART_COLORS_TAG = "PartColors";
    public static final int MAX_BLADE_MODIFIERS = 2;
    public static final int DEFAULT_PART_COLOR = 0xFFFFFF;
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
        applyPreset(stack, bladeColor, legacyHiltId, DEFAULT_PART_COLOR, DEFAULT_PART_COLOR, DEFAULT_PART_COLOR, DEFAULT_PART_COLOR);
    }

    public static void applyPreset(ItemStack stack, String bladeColor, String legacyHiltId,
                                   int emitterColor, int switchColor, int gripColor, int pommelColor) {
        CompoundTag data = getOrCreateDataTag(stack);
        data.putString(BLADE_COLOR_TAG, bladeColor);
        data.putString(PRIMARY_HILT_ID_TAG, legacyHiltId);
        for (LightsaberPartType type : LightsaberPartType.values()) {
            data.putString(type.getSerializedName(), legacyHiltId);
        }
        writePartColors(data, emitterColor, switchColor, gripColor, pommelColor);

        AdvancedLightsaberLegacyHilts.LegacyHiltSpec spec = getLegacySpec(legacyHiltId);
        if (spec != null) {
            writeBladeModifiers(data, Arrays.asList(spec.defaultBladeModifiers()));
        } else {
            writeBladeModifiers(data, Collections.emptySet());
        }
    }

    public static void applyAssembly(ItemStack stack, String bladeColor, String emitter, String switchSection, String grip, String pommel) {
        applyAssembly(stack, bladeColor, emitter, switchSection, grip, pommel,
                DEFAULT_PART_COLOR, DEFAULT_PART_COLOR, DEFAULT_PART_COLOR, DEFAULT_PART_COLOR);
    }

    public static void applyAssembly(ItemStack stack, String bladeColor, String emitter, String switchSection, String grip, String pommel,
                                     int emitterColor, int switchColor, int gripColor, int pommelColor) {
        CompoundTag data = getOrCreateDataTag(stack);
        data.putString(BLADE_COLOR_TAG, bladeColor);
        data.putString(PRIMARY_HILT_ID_TAG, emitter);
        data.putString(LightsaberPartType.EMITTER.getSerializedName(), emitter);
        data.putString(LightsaberPartType.SWITCH_SECTION.getSerializedName(), switchSection);
        data.putString(LightsaberPartType.GRIP.getSerializedName(), grip);
        data.putString(LightsaberPartType.POMMEL.getSerializedName(), pommel);
        writePartColors(data, emitterColor, switchColor, gripColor, pommelColor);
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

    public static int clampPartColor(int color) {
        return color & 0xFFFFFF;
    }

    public static int getDefaultPartColor(LightsaberPartType type) {
        return DEFAULT_PART_COLOR;
    }

    public static int getPartColor(ItemStack stack, LightsaberPartType type) {
        return getPartColor(getDataTag(stack), type);
    }

    public static int getPartColor(@Nullable CompoundTag data, LightsaberPartType type) {
        if (data != null && data.contains(PART_COLORS_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag colors = data.getCompound(PART_COLORS_TAG);
            if (colors.contains(type.getSerializedName(), Tag.TAG_INT)) {
                return clampPartColor(colors.getInt(type.getSerializedName()));
            }
        }
        return getDefaultPartColor(type);
    }

    public static void setPartColor(ItemStack stack, LightsaberPartType type, int color) {
        writePartColor(getOrCreateDataTag(stack), type, color);
    }

    public static void writePartColor(CompoundTag data, LightsaberPartType type, int color) {
        CompoundTag colors = data.contains(PART_COLORS_TAG, Tag.TAG_COMPOUND)
                ? data.getCompound(PART_COLORS_TAG)
                : new CompoundTag();
        colors.putInt(type.getSerializedName(), clampPartColor(color));
        data.put(PART_COLORS_TAG, colors);
    }

    public static void writePartColors(CompoundTag data, int emitterColor, int switchColor, int gripColor, int pommelColor) {
        writePartColor(data, LightsaberPartType.EMITTER, emitterColor);
        writePartColor(data, LightsaberPartType.SWITCH_SECTION, switchColor);
        writePartColor(data, LightsaberPartType.GRIP, gripColor);
        writePartColor(data, LightsaberPartType.POMMEL, pommelColor);
    }

    public static void applyPartColors(ItemStack stack, int emitterColor, int switchColor, int gripColor, int pommelColor) {
        writePartColors(getOrCreateDataTag(stack), emitterColor, switchColor, gripColor, pommelColor);
    }

    public static void copyPartColors(CompoundTag fromData, CompoundTag toData) {
        for (LightsaberPartType type : LightsaberPartType.values()) {
            writePartColor(toData, type, getPartColor(fromData, type));
        }
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
        if (spec == null || !spec.doubleBladed()) {
            return false;
        }

        if (stack.getItem() instanceof server.galaxyunderchaos.item.DoubleLightsaberItem) {
            return false;
        }

        CompoundTag data = getDataTag(stack);
        if (data == null) {
            return false;
        }

        for (LightsaberPartType type : LightsaberPartType.values()) {
            if (!primary.equals(getPartFamily(stack, type, fallbackFamily))) {
                return false;
            }
        }

        return true;
    }

    public static ItemStack createCustomLightsaber(String bladeColor, String emitter, String switchSection, String grip, String pommel) {
        return createCustomLightsaber(bladeColor, emitter, switchSection, grip, pommel,
                DEFAULT_PART_COLOR, DEFAULT_PART_COLOR, DEFAULT_PART_COLOR, DEFAULT_PART_COLOR);
    }

    public static ItemStack createCustomLightsaber(String bladeColor, String emitter, String switchSection, String grip, String pommel,
                                                   int emitterColor, int switchColor, int gripColor, int pommelColor) {
        ItemStack result = new ItemStack(galaxyunderchaos.CUSTOM_LIGHTSABER.get());
        applyAssembly(result, bladeColor, emitter, switchSection, grip, pommel, emitterColor, switchColor, gripColor, pommelColor);
        return result;
    }

    public static ItemStack createCustomLightsaberFromPreset(String bladeColor, String legacyHiltId) {
        return createCustomLightsaberFromPreset(bladeColor, legacyHiltId,
                DEFAULT_PART_COLOR, DEFAULT_PART_COLOR, DEFAULT_PART_COLOR, DEFAULT_PART_COLOR);
    }

    public static ItemStack createCustomLightsaberFromPreset(String bladeColor, String legacyHiltId,
                                                             int emitterColor, int switchColor, int gripColor, int pommelColor) {
        ItemStack result = new ItemStack(galaxyunderchaos.CUSTOM_LIGHTSABER.get());
        applyPreset(result, bladeColor, legacyHiltId, emitterColor, switchColor, gripColor, pommelColor);
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
