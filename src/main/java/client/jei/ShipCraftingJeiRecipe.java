package client.jei;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.ship.ShipColorSection;
import server.galaxyunderchaos.ship.ShipCustomization;

import java.util.ArrayList;
import java.util.List;

public record ShipCraftingJeiRecipe(Component description,
                                    ItemStack blueprint,
                                    ItemStack output,
                                    int baseColor,
                                    int primaryColor,
                                    int secondaryColor,
                                    int interiorColor) {
    public static List<ShipCraftingJeiRecipe> createAll() {
        List<ShipCraftingJeiRecipe> recipes = new ArrayList<>();
        addShip(recipes, "novadive", new ItemStack(galaxyunderchaos.NOVADIVE_BLUEPRINT.get()));
        addShip(recipes, "flashfire", new ItemStack(galaxyunderchaos.FLASHFIRE_BLUEPRINT.get()));
        return recipes;
    }

    private static void addShip(List<ShipCraftingJeiRecipe> recipes, String shipId, ItemStack blueprint) {
        int defaultBase = ShipCustomization.getDefaultColor(ShipColorSection.BASE);
        int defaultPrimary = ShipCustomization.getDefaultColor(ShipColorSection.PRIMARY);
        int defaultSecondary = ShipCustomization.getDefaultColor(ShipColorSection.SECONDARY);
        int defaultInterior = ShipCustomization.getDefaultColor(ShipColorSection.INTERIOR);

        ItemStack defaultOutput = ShipCustomization.createShipStack(shipId, defaultBase, defaultPrimary, defaultSecondary, defaultInterior);
        if (!defaultOutput.isEmpty()) {
            recipes.add(new ShipCraftingJeiRecipe(
                    Component.translatable("jei.galaxyunderchaos.ship_customization.default"),
                    blueprint.copy(),
                    defaultOutput,
                    defaultBase,
                    defaultPrimary,
                    defaultSecondary,
                    defaultInterior));
        }

        int sampleBase = 0x343B49;
        int samplePrimary = 0x2DDAC3;
        int sampleSecondary = 0xE5C15B;
        int sampleInterior = 0xC9D5E8;
        ItemStack customOutput = ShipCustomization.createShipStack(shipId, sampleBase, samplePrimary, sampleSecondary, sampleInterior);
        if (!customOutput.isEmpty()) {
            recipes.add(new ShipCraftingJeiRecipe(
                    Component.translatable("jei.galaxyunderchaos.ship_customization.rgb"),
                    blueprint.copy(),
                    customOutput,
                    sampleBase,
                    samplePrimary,
                    sampleSecondary,
                    sampleInterior));
        }
    }
}
