package client.jei;

import client.renderer.ModItemRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.LightsaberCraftingTableLogic;

public class LightsaberCraftingRecipeCategory implements IRecipeCategory<LightsaberCraftingJeiRecipe> {
    public static final RecipeType<LightsaberCraftingJeiRecipe> TYPE = RecipeType.create(galaxyunderchaos.MODID, "lightsaber_crafting", LightsaberCraftingJeiRecipe.class);

    private static final int WIDTH = 176;
    private static final int HEIGHT = 112;

    /**
     * JEI-local copy of the new two-column table layout.
     * Mirrors the menu order:
     * 0 emitter, 1 switch, 2 grip, 3 pommel, 4 core/circuitry, 5 kyber/flex,
     * 6 modifier A, 7 modifier B.
     */
    private static final int[][] INPUT_POSITIONS = {
            {12, 22},
            {12, 42},
            {12, 62},
            {12, 82},
            {32, 22},
            {32, 42},
            {32, 62},
            {32, 82}
    };

    private static final int OUTPUT_X = 146;
    private static final int OUTPUT_Y = 80;

    private final IDrawable background;
    private final IDrawable icon;

    public LightsaberCraftingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(galaxyunderchaos.LIGHTSABER_CRAFTING_TABLE_ITEM.get()));
    }

    @Override
    public RecipeType<LightsaberCraftingJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.galaxyunderchaos.lightsaber_crafting.category");
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
    public void setRecipe(IRecipeLayoutBuilder builder, LightsaberCraftingJeiRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < INPUT_POSITIONS.length; ++i) {
            if (!recipe.inputs().get(i).isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, INPUT_POSITIONS[i][0], INPUT_POSITIONS[i][1])
                        .addItemStacks(recipe.inputs().get(i));
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(LightsaberCraftingTableLogic.activatePreview(recipe.output()));
    }

    @Override
    public void draw(LightsaberCraftingJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        drawCodeBackground(graphics);

        graphics.drawString(minecraft.font, recipe.description(), 60, 7, 0xFFFFFF, false);
        graphics.drawString(minecraft.font, Component.literal("Inputs"), 12, 10, 0xB9D2F5, false);
        graphics.drawString(minecraft.font, Component.literal("Output"), 139, 68, 0xB9D2F5, false);
        graphics.drawString(minecraft.font, Component.literal("Part RGB"), 66, 62, 0xB9D2F5, false);

        drawPartSwatch(graphics, 66, 75, recipe.emitterColor(), "E");
        drawPartSwatch(graphics, 98, 75, recipe.switchColor(), "S");
        drawPartSwatch(graphics, 66, 88, recipe.gripColor(), "G");
        drawPartSwatch(graphics, 98, 88, recipe.pommelColor(), "P");

        ItemStack preview = LightsaberCraftingTableLogic.activatePreview(recipe.output());
        if (preview.isEmpty()) {
            return;
        }

        float spin = (minecraft.player == null ? 0.0F : minecraft.player.tickCount) + minecraft.getFrameTime();
        graphics.pose().pushPose();
        graphics.pose().translate(116.0F, 38.0F, 100.0F);
        graphics.pose().mulPose(Axis.YP.rotationDegrees((float) Math.sin(spin / 20.0F) * 2.5F));
        graphics.pose().mulPose(Axis.ZP.rotationDegrees((float) Math.sin(spin / 20.0F + 2.0F) * 2.5F));
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(-90.0F));
        graphics.pose().mulPose(Axis.YP.rotationDegrees(90.0F + spin));
        graphics.pose().scale(-17.0F, 17.0F, 17.0F);

        RenderSystem.enableDepthTest();
        MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
        ModItemRenderer.renderForgePreview(preview, graphics.pose(), buffer, 0xF000F0, OverlayTexture.NO_OVERLAY);
        buffer.endBatch();

        graphics.pose().popPose();
    }

    private static void drawCodeBackground(GuiGraphics graphics) {
        graphics.fill(0, 0, WIDTH, HEIGHT, 0xEE070910);
        graphics.fill(1, 1, WIDTH - 1, HEIGHT - 1, 0xDD111522);
        graphics.fill(5, 5, WIDTH - 5, HEIGHT - 5, 0xAA171E31);

        // Two-column input panel matching the revised lightsaber crafting table layout.
        graphics.fill(8, 17, 55, 104, 0xCC101725);

        // Preview panel.
        graphics.fill(60, 16, 168, 56, 0xCC0F1824);
        graphics.fill(64, 20, 164, 52, 0xAA061019);

        // RGB panel.
        graphics.fill(60, 58, 132, 104, 0xCC132131);

        // Output panel.
        graphics.fill(138, 76, 168, 104, 0xCC101725);
    }

    private static void drawPartSwatch(GuiGraphics graphics, int x, int y, int color, String label) {
        graphics.drawString(Minecraft.getInstance().font, label, x, y + 1, 0xE4EFFD, false);
        graphics.fill(x + 8, y, x + 27, y + 8, 0xFF000000 | (color & 0xFFFFFF));
        graphics.fill(x + 9, y + 1, x + 26, y + 7, 0xC0000000 | (color & 0xFFFFFF));
    }
}
