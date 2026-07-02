package client.jei;

import client.screen.LightsaberCraftingTableScreen;
import client.screen.ShipCraftingTableScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.LightsaberCraftingTableLogic;
import server.galaxyunderchaos.menu.LightsaberCraftingTableMenu;
import server.galaxyunderchaos.menu.ModMenuTypes;
import server.galaxyunderchaos.menu.ShipCraftingTableMenu;
import server.galaxyunderchaos.ship.ShipCraftingTableLogic;

@JeiPlugin
public class LightsaberCraftingJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(galaxyunderchaos.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new LightsaberCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ShipCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(LightsaberCraftingRecipeCategory.TYPE, LightsaberCraftingJeiRecipe.createAll());
        registration.addRecipes(ShipCraftingRecipeCategory.TYPE, ShipCraftingJeiRecipe.createAll());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(galaxyunderchaos.LIGHTSABER_CRAFTING_TABLE_ITEM.get()), LightsaberCraftingRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(galaxyunderchaos.SHIP_CRAFTING_TABLE_ITEM.get()), ShipCraftingRecipeCategory.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(LightsaberCraftingTableScreen.class, 94, 19, 154, 84, LightsaberCraftingRecipeCategory.TYPE);
        registration.addRecipeClickArea(LightsaberCraftingTableScreen.class, 220, 82, 28, 28, LightsaberCraftingRecipeCategory.TYPE);
        registration.addRecipeClickArea(ShipCraftingTableScreen.class, 204, 7, 42, 10, ShipCraftingRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
                LightsaberCraftingTableMenu.class,
                ModMenuTypes.LIGHTSABER_CRAFTING_TABLE.get(),
                LightsaberCraftingRecipeCategory.TYPE,
                0,
                LightsaberCraftingTableLogic.INPUT_SLOT_COUNT,
                LightsaberCraftingTableMenu.RESULT_SLOT + 1,
                36
        );

        registration.addRecipeTransferHandler(
                ShipCraftingTableMenu.class,
                ModMenuTypes.SHIP_CRAFTING_TABLE.get(),
                ShipCraftingRecipeCategory.TYPE,
                0,
                ShipCraftingTableLogic.INPUT_SLOT_COUNT,
                ShipCraftingTableMenu.RESULT_SLOT + 1,
                36
        );
    }
}
