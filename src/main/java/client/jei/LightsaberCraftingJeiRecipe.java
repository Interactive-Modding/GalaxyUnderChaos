
package client.jei;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.AdvancedLightsaberLegacyHilts;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;
import server.galaxyunderchaos.lightsaber.LightsaberCrafting;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;
import server.galaxyunderchaos.lightsaber.ModularLightsaberData;

import java.util.ArrayList;
import java.util.List;

public record LightsaberCraftingJeiRecipe(Component description,
                                           List<List<ItemStack>> inputs,
                                           ItemStack output,
                                           int emitterColor,
                                           int switchColor,
                                           int gripColor,
                                           int pommelColor) {
    public LightsaberCraftingJeiRecipe(Component description, List<List<ItemStack>> inputs, ItemStack output) {
        this(description, inputs, output,
                ModularLightsaberData.DEFAULT_PART_COLOR,
                ModularLightsaberData.DEFAULT_PART_COLOR,
                ModularLightsaberData.DEFAULT_PART_COLOR,
                ModularLightsaberData.DEFAULT_PART_COLOR);
    }

    public static List<LightsaberCraftingJeiRecipe> createAll() {
        List<LightsaberCraftingJeiRecipe> recipes = new ArrayList<>();
        List<ItemStack> kybers = kyberStacks();
        List<ItemStack> modifiers = modifierStacks();
        ItemStack circuitry = new ItemStack(galaxyunderchaos.INTERNAL_LIGHTSABER_CIRCUITRY.get());
        List<String> families = new ArrayList<>(AdvancedLightsaberLegacyHilts.HILTS.keySet());

        for (String family : families) {
            ItemStack legacyHilt = new ItemStack(galaxyunderchaos.LEGACY_HILTS.get(family).get());
            for (ItemStack kyber : kybers) {
                ItemStack output = LightsaberCrafting.craftLightsaber(legacyHilt, kyber);
                if (!output.isEmpty()) {
                    recipes.add(new LightsaberCraftingJeiRecipe(
                            Component.translatable("jei.galaxyunderchaos.lightsaber_crafting.preset"),
                            slotLists(empty(), empty(), empty(), empty(), stackList(legacyHilt), stackList(kyber), empty(), empty()),
                            lit(output)));
                }
            }
        }

        for (String family : families) {
            ItemStack emitter = part(family, LightsaberPartType.EMITTER);
            ItemStack switchSection = part(family, LightsaberPartType.SWITCH_SECTION);
            ItemStack grip = part(family, LightsaberPartType.GRIP);
            ItemStack pommel = part(family, LightsaberPartType.POMMEL);
            for (ItemStack kyber : kybers) {
                ItemStack output = LightsaberCrafting.craftLightsaber(emitter, switchSection, grip, pommel, kyber);
                if (!output.isEmpty()) {
                    recipes.add(new LightsaberCraftingJeiRecipe(
                            Component.translatable("jei.galaxyunderchaos.lightsaber_crafting.modular"),
                            slotLists(stackList(emitter), stackList(switchSection), stackList(grip), stackList(pommel), stackList(circuitry), stackList(kyber), empty(), empty()),
                            lit(output)));
                }
            }
        }

        int sampleEmitter = 0xE6E6E6;
        int sampleSwitch = 0x2DDAC3;
        int sampleGrip = 0x3F4653;
        int samplePommel = 0xC8A25A;
        ItemStack mixedPreview = LightsaberCrafting.craftLightsaber(
                part(families.get(0), LightsaberPartType.EMITTER),
                part(families.get(Math.min(1, families.size() - 1)), LightsaberPartType.SWITCH_SECTION),
                part(families.get(Math.min(2, families.size() - 1)), LightsaberPartType.GRIP),
                part(families.get(Math.min(3, families.size() - 1)), LightsaberPartType.POMMEL),
                kybers.get(0),
                sampleEmitter,
                sampleSwitch,
                sampleGrip,
                samplePommel
        );
        recipes.add(new LightsaberCraftingJeiRecipe(
                Component.translatable("jei.galaxyunderchaos.lightsaber_crafting.mixed"),
                slotLists(allParts(LightsaberPartType.EMITTER), allParts(LightsaberPartType.SWITCH_SECTION), allParts(LightsaberPartType.GRIP), allParts(LightsaberPartType.POMMEL), stackList(circuitry), kybers, modifierStacks(), modifierStacks()),
                lit(mixedPreview),
                sampleEmitter,
                sampleSwitch,
                sampleGrip,
                samplePommel));

        ItemStack baseSaber = LightsaberCrafting.craftLightsaber(
                part(families.get(0), LightsaberPartType.EMITTER),
                part(families.get(0), LightsaberPartType.SWITCH_SECTION),
                part(families.get(0), LightsaberPartType.GRIP),
                part(families.get(0), LightsaberPartType.POMMEL),
                kybers.get(0)
        );
        for (ItemStack modifier : modifiers) {
            ItemStack output = LightsaberCrafting.applyModifierCrystals(baseSaber, List.of(modifier));
            if (!output.isEmpty()) {
                recipes.add(new LightsaberCraftingJeiRecipe(
                        Component.translatable("jei.galaxyunderchaos.lightsaber_crafting.modifiers"),
                        slotLists(empty(), empty(), empty(), empty(), stackList(baseSaber), empty(), stackList(modifier), modifierStacks()),
                        lit(output)));
            }
        }

        if (families.size() >= 2) {
            ItemStack upper = LightsaberCrafting.craftLightsaber(
                    part(families.get(0), LightsaberPartType.EMITTER),
                    part(families.get(0), LightsaberPartType.SWITCH_SECTION),
                    part(families.get(0), LightsaberPartType.GRIP),
                    part(families.get(0), LightsaberPartType.POMMEL),
                    kybers.get(0)
            );
            ItemStack lower = LightsaberCrafting.craftLightsaber(
                    part(families.get(1), LightsaberPartType.EMITTER),
                    part(families.get(1), LightsaberPartType.SWITCH_SECTION),
                    part(families.get(1), LightsaberPartType.GRIP),
                    part(families.get(1), LightsaberPartType.POMMEL),
                    kybers.get(Math.min(1, kybers.size() - 1))
            );
            ItemStack output = LightsaberCrafting.craftDoubleLightsaber(upper, lower);
            if (!output.isEmpty()) {
                recipes.add(new LightsaberCraftingJeiRecipe(
                        Component.translatable("jei.galaxyunderchaos.lightsaber_crafting.double"),
                        slotLists(empty(), empty(), empty(), empty(), stackList(upper), stackList(lower), empty(), empty()),
                        lit(output)));
            }
        }

        return recipes;
    }

    private static ItemStack lit(ItemStack stack) {
        return server.galaxyunderchaos.lightsaber.LightsaberCraftingTableLogic.activatePreview(stack);
    }

    private static List<List<ItemStack>> slotLists(List<ItemStack> a, List<ItemStack> b, List<ItemStack> c, List<ItemStack> d, List<ItemStack> e, List<ItemStack> f, List<ItemStack> g, List<ItemStack> h) {
        return List.of(a, b, c, d, e, f, g, h);
    }

    private static List<ItemStack> empty() {
        return List.of();
    }

    private static List<ItemStack> stackList(ItemStack stack) {
        return stack.isEmpty() ? List.of() : List.of(stack.copy());
    }

    private static ItemStack part(String family, LightsaberPartType type) {
        return new ItemStack(galaxyunderchaos.LIGHTSABER_PARTS.get(family + "_" + type.getSerializedName()).get());
    }

    private static List<ItemStack> allParts(LightsaberPartType type) {
        List<ItemStack> stacks = new ArrayList<>();
        for (String family : AdvancedLightsaberLegacyHilts.HILTS.keySet()) {
            stacks.add(part(family, type));
        }
        return stacks;
    }

    private static List<ItemStack> kyberStacks() {
        String[] ids = {
                "red_kyber", "blue_kyber", "green_kyber", "yellow_kyber", "cyan_kyber",
                "white_kyber", "magenta_kyber", "purple_kyber", "pink_kyber", "lime_green_kyber",
                "turquoise_kyber", "orange_kyber", "blood_orange_kyber", "amber_kyber", "gold_kyber",
                "light_blue_kyber", "dark_blue_kyber", "maroon_kyber", "deep_violet_kyber", "arctic_blue_kyber", "rose_pink_kyber"
        };
        List<ItemStack> stacks = new ArrayList<>();
        for (String id : ids) {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(galaxyunderchaos.MODID, id));
            if (item != null && item != Items.AIR) {
                stacks.add(new ItemStack(item));
            }
        }
        return stacks;
    }

    private static List<ItemStack> modifierStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (BladeModifierCrystal crystal : BladeModifierCrystal.values()) {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(galaxyunderchaos.MODID, crystal.getRegistryPath()));
            if (item != null && item != Items.AIR) {
                stacks.add(new ItemStack(item));
            }
        }
        return stacks;
    }
}
