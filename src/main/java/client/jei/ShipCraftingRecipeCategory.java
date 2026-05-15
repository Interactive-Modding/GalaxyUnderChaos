package client.jei;

import client.renderer.ship.ShipItemRendererHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.galaxyunderchaos;

public class ShipCraftingRecipeCategory implements IRecipeCategory<ShipCraftingJeiRecipe> {
    public static final RecipeType<ShipCraftingJeiRecipe> TYPE = RecipeType.create(galaxyunderchaos.MODID, "ship_customization", ShipCraftingJeiRecipe.class);

    private static final int WIDTH = 176;
    private static final int HEIGHT = 112;

    private final IDrawable background;
    private final IDrawable icon;

    public ShipCraftingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(galaxyunderchaos.SHIP_CRAFTING_TABLE_ITEM.get()));
    }

    @Override
    public RecipeType<ShipCraftingJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.galaxyunderchaos.ship_customization.category");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ShipCraftingJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 20)
                .addItemStack(recipe.blueprint());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 145, 79)
                .addItemStack(recipe.output());
    }

    @Override
    public void draw(ShipCraftingJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        drawCodeBackground(graphics);
        graphics.drawString(minecraft.font, recipe.description(), 43, 7, 0xFFFFFF, false);
        graphics.drawString(minecraft.font, Component.literal("RGB channels"), 13, 65, 0xB9D2F5, false);

        drawColorRow(graphics, 13, 78, "Base", recipe.baseColor());
        drawColorRow(graphics, 13, 89, "Primary", recipe.primaryColor());
        drawColorRow(graphics, 88, 78, "Secondary", recipe.secondaryColor());
        drawColorRow(graphics, 88, 89, "Interior", recipe.interiorColor());

        ItemStack preview = recipe.output();
        if (preview.isEmpty()) {
            return;
        }

        graphics.pose().pushPose();
        graphics.pose().translate(103.0F, 45.0F, 180.0F);
        MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
        ShipItemRendererHelper.renderShipPreview(preview, graphics.pose(), buffer, 0xF000F0, 34.0F, -10.0F, 7.5F);
        buffer.endBatch();
        graphics.pose().popPose();
    }

    private static void drawCodeBackground(GuiGraphics graphics) {
        graphics.fill(0, 0, WIDTH, HEIGHT, 0xEE07101A);
        graphics.fill(1, 1, WIDTH - 1, HEIGHT - 1, 0xDD0D1825);
        graphics.fill(5, 5, WIDTH - 5, HEIGHT - 5, 0xAA111E2E);
        graphics.fill(10, 17, 39, 42, 0xCC101725);
        graphics.fill(43, 17, 137, 60, 0xCC0F1824);
        graphics.fill(10, 62, 166, 104, 0xAA0A1018);
    }

    private static void drawColorRow(GuiGraphics graphics, int x, int y, String label, int color) {
        Minecraft minecraft = Minecraft.getInstance();
        graphics.drawString(minecraft.font, label, x, y, 0xE4EFFD, false);
        graphics.fill(x + 47, y, x + 63, y + 8, 0xFF000000 | (color & 0xFFFFFF));
    }
}
