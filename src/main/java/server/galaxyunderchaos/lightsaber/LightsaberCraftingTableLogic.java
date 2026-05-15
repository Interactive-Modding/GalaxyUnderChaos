
package server.galaxyunderchaos.lightsaber;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.DoubleLightsaberItem;
import server.galaxyunderchaos.item.HiltItem;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.item.LightsaberPartItem;

import java.util.ArrayList;
import java.util.List;

public final class LightsaberCraftingTableLogic {
    public static final int SLOT_EMITTER = 0;
    public static final int SLOT_SWITCH = 1;
    public static final int SLOT_GRIP = 2;
    public static final int SLOT_POMMEL = 3;
    public static final int SLOT_CORE = 4;
    public static final int SLOT_FLEX = 5;
    public static final int SLOT_MODIFIER_A = 6;
    public static final int SLOT_MODIFIER_B = 7;
    public static final int INPUT_SLOT_COUNT = 8;

    public enum CraftMode {
        NONE,
        MODULAR,
        PRESET_HILT,
        MODIFIERS,
        DOUBLE
    }

    public record Evaluation(ItemStack result, CraftMode mode) {
        public static final Evaluation EMPTY = new Evaluation(ItemStack.EMPTY, CraftMode.NONE);
    }

    private LightsaberCraftingTableLogic() {
    }

    public static Evaluation evaluate(Container container) {
        return evaluate(container,
                ModularLightsaberData.DEFAULT_PART_COLOR,
                ModularLightsaberData.DEFAULT_PART_COLOR,
                ModularLightsaberData.DEFAULT_PART_COLOR,
                ModularLightsaberData.DEFAULT_PART_COLOR);
    }

    public static Evaluation evaluate(Container container, int emitterColor, int switchColor, int gripColor, int pommelColor) {
        ItemStack core = container.getItem(SLOT_CORE);
        ItemStack flex = container.getItem(SLOT_FLEX);
        List<ItemStack> assemblyModifiers = collectModifierStacks(container);

        if (isFinishedSingleLightsaber(core) && isFinishedSingleLightsaber(flex)) {
            ItemStack result = activatePreview(LightsaberCrafting.craftDoubleLightsaber(core, flex));
            if (!result.isEmpty()) {
                return new Evaluation(result, CraftMode.DOUBLE);
            }
        }

        if (isFinishedSingleLightsaber(core)) {
            ItemStack result = activatePreview(LightsaberCrafting.applyModifierCrystals(core, assemblyModifiers));
            if (!result.isEmpty()) {
                return new Evaluation(result, CraftMode.MODIFIERS);
            }
        }

        if (core.getItem() instanceof HiltItem && isKyber(flex)) {
            ItemStack result = activatePreview(applyAssemblyModifiers(
                    LightsaberCrafting.craftLightsaber(core, flex, emitterColor, switchColor, gripColor, pommelColor),
                    assemblyModifiers));
            if (!result.isEmpty()) {
                return new Evaluation(result, CraftMode.PRESET_HILT);
            }
        }

        ItemStack emitter = container.getItem(SLOT_EMITTER);
        ItemStack switchSection = container.getItem(SLOT_SWITCH);
        ItemStack grip = container.getItem(SLOT_GRIP);
        ItemStack pommel = container.getItem(SLOT_POMMEL);
        if (isPartType(emitter, LightsaberPartType.EMITTER)
                && isPartType(switchSection, LightsaberPartType.SWITCH_SECTION)
                && isPartType(grip, LightsaberPartType.GRIP)
                && isPartType(pommel, LightsaberPartType.POMMEL)
                && isCircuitry(core)
                && isKyber(flex)) {
            ItemStack result = activatePreview(applyAssemblyModifiers(
                    LightsaberCrafting.craftLightsaber(emitter, switchSection, grip, pommel, flex, emitterColor, switchColor, gripColor, pommelColor),
                    assemblyModifiers));
            if (!result.isEmpty()) {
                return new Evaluation(result, CraftMode.MODULAR);
            }
        }

        return Evaluation.EMPTY;
    }

    public static void consumeIngredients(Container container, CraftMode mode) {
        switch (mode) {
            case MODULAR -> {
                shrink(container, SLOT_EMITTER);
                shrink(container, SLOT_SWITCH);
                shrink(container, SLOT_GRIP);
                shrink(container, SLOT_POMMEL);
                shrink(container, SLOT_CORE);
                shrink(container, SLOT_FLEX);
                shrinkModifierSlots(container);
            }
            case PRESET_HILT -> {
                shrink(container, SLOT_CORE);
                shrink(container, SLOT_FLEX);
                shrinkModifierSlots(container);
            }
            case MODIFIERS -> {
                shrink(container, SLOT_CORE);
                shrinkModifierSlots(container);
            }
            case DOUBLE -> {
                shrink(container, SLOT_CORE);
                shrink(container, SLOT_FLEX);
            }
            case NONE -> {
            }
        }
    }

    private static void shrinkModifierSlots(Container container) {
        shrink(container, SLOT_MODIFIER_A);
        shrink(container, SLOT_MODIFIER_B);
    }

    private static List<ItemStack> collectModifierStacks(Container container) {
        List<ItemStack> modifiers = new ArrayList<>();
        if (isModifierCrystal(container.getItem(SLOT_MODIFIER_A))) {
            modifiers.add(container.getItem(SLOT_MODIFIER_A));
        }
        if (isModifierCrystal(container.getItem(SLOT_MODIFIER_B))) {
            modifiers.add(container.getItem(SLOT_MODIFIER_B));
        }
        return modifiers;
    }

    private static ItemStack applyAssemblyModifiers(ItemStack result, List<ItemStack> modifiers) {
        if (result.isEmpty() || modifiers.isEmpty()) {
            return result;
        }
        ItemStack modified = LightsaberCrafting.applyModifierCrystals(result, modifiers);
        return modified.isEmpty() ? result : modified;
    }


    public static ItemStack inactivePreview(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack copy = stack.copy();
        if (copy.getItem() instanceof LightsaberItem) {
            copy.getOrCreateTag().putBoolean("LightsaberActive", false);
        }
        return copy;
    }

    public static ItemStack activatePreview(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack copy = stack.copy();
        if (copy.getItem() instanceof LightsaberItem) {
            copy.getOrCreateTag().putBoolean("LightsaberActive", true);
        }
        return copy;
    }

    private static void shrink(Container container, int slot) {
        ItemStack stack = container.getItem(slot);
        if (!stack.isEmpty()) {
            stack.shrink(1);
            container.setItem(slot, stack);
        }
    }

    public static boolean isPartType(ItemStack stack, LightsaberPartType type) {
        return stack.getItem() instanceof LightsaberPartItem partItem && partItem.getPartType() == type;
    }

    public static boolean isCircuitry(ItemStack stack) {
        return !stack.isEmpty() && stack.is(galaxyunderchaos.INTERNAL_LIGHTSABER_CIRCUITRY.get());
    }

    public static boolean isLegacyHilt(ItemStack stack) {
        return stack.getItem() instanceof HiltItem;
    }

    public static boolean isFinishedSingleLightsaber(ItemStack stack) {
        return stack.getItem() instanceof LightsaberItem && !(stack.getItem() instanceof DoubleLightsaberItem);
    }

    public static boolean isKyber(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        net.minecraft.resources.ResourceLocation key = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem());
        return key != null && !"unknown".equals(BladeColorRegistry.getBladeColor(key.getPath()));
    }

    public static boolean isModifierCrystal(ItemStack stack) {
        return BladeModifierCrystal.fromStack(stack) != null;
    }
}
