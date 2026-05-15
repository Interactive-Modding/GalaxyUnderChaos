package client.screen;

import client.renderer.ModItemRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.AdvancedLightsaberLegacyHilts;
import server.galaxyunderchaos.lightsaber.LightsaberCraftingTableLogic;
import server.galaxyunderchaos.lightsaber.LightsaberCustomizationColorPacket;
import server.galaxyunderchaos.lightsaber.LightsaberFormNetworking;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;
import server.galaxyunderchaos.lightsaber.ModularLightsaberData;
import server.galaxyunderchaos.menu.LightsaberCraftingTableMenu;

import java.util.ArrayList;
import java.util.List;

public class LightsaberCraftingTableScreen extends AbstractContainerScreen<LightsaberCraftingTableMenu> {
    private static final int SECTION_X = 10;
    private static final int SECTION_Y = 118;
    private static final int SECTION_W = 78;
    private static final int SECTION_H = 14;
    private static final int SECTION_GAP = 3;

    private static final int PANEL_X = 94;
    private static final int PANEL_Y = 116;
    private static final int PANEL_W = 154;
    private static final int PANEL_H = 60;

    private static final int SLIDER_X = PANEL_X + 32;
    private static final int SLIDER_W = 78;
    private static final int SLIDER_H = 7;

    private static final int PREVIEW_X = 94;
    private static final int PREVIEW_Y = 19;
    private static final int PREVIEW_W = 154;
    private static final int PREVIEW_H = 84;

    private LightsaberPartType selectedPart = LightsaberPartType.EMITTER;
    private int draggingChannel = -1;

    public LightsaberCraftingTableScreen(LightsaberCraftingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 256;
        this.imageHeight = 270;
        this.inventoryLabelX = LightsaberCraftingTableMenu.PLAYER_INV_X;
        this.inventoryLabelY = LightsaberCraftingTableMenu.PLAYER_INV_Y - 10;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 14;
        this.titleLabelY = 8;
        this.inventoryLabelX = LightsaberCraftingTableMenu.PLAYER_INV_X;
        this.inventoryLabelY = LightsaberCraftingTableMenu.PLAYER_INV_Y - 10;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        renderCodeBasedBackground(graphics);
        renderSlotFrames(graphics);

        if (shouldRenderGhostSlots()) {
            renderGhostSlots(graphics);
        }

        renderPreview(graphics);
        renderSectionButtons(graphics, mouseX, mouseY);
        renderRgbEditor(graphics);
        renderDecorations(graphics);
    }

    private void renderCodeBasedBackground(GuiGraphics graphics) {
        int left = this.leftPos + 4;
        int top = this.topPos;
        int right = this.leftPos + this.imageWidth - 4;
        int bottom = this.topPos + this.imageHeight - 4;

        graphics.fill(left, top, right, bottom, 0xEE070910);
        graphics.fill(left + 1, top + 1, right - 1, bottom - 1, 0xDD111522);
        graphics.fill(left + 5, top + 5, right - 5, bottom - 5, 0xAA171E31);

        graphics.fill(this.leftPos + 10, this.topPos + 24, this.leftPos + 88, this.topPos + 108, 0xAA05070C);
        graphics.fill(this.leftPos + 11, this.topPos + 25, this.leftPos + 87, this.topPos + 107, 0xCC101725);

        int invLeft = this.leftPos + LightsaberCraftingTableMenu.PLAYER_INV_X - 8;
        int invTop = this.topPos + LightsaberCraftingTableMenu.PLAYER_INV_Y - 8;
        int invRight = this.leftPos + LightsaberCraftingTableMenu.PLAYER_INV_X + 9 * 18 + 8;
        int invBottom = this.topPos + LightsaberCraftingTableMenu.HOTBAR_Y + 18 + 8;
        graphics.fill(invLeft, invTop, invRight, invBottom, 0xAA050A10);
        graphics.fill(invLeft + 1, invTop + 1, invRight - 1, invBottom - 1, 0xCC101A27);
    }

    private void renderDecorations(GuiGraphics graphics) {
//        graphics.drawString(this.font, "Parts", this.leftPos + 17, this.topPos + 18, 0x9AB3D4, false);
//        graphics.drawString(this.font, "Preview", this.leftPos + PREVIEW_X + 6, this.topPos + 8, 0xDCE9FF, false);
//        graphics.drawString(this.font, "Result", this.leftPos + LightsaberCraftingTableMenu.RESULT_SLOT_X - 4, this.topPos + LightsaberCraftingTableMenu.RESULT_SLOT_Y - 12, 0x9AB3D4, false);
    }

    private void renderSlotFrames(GuiGraphics graphics) {
        for (Slot slot : this.menu.slots) {
            drawSlotFrame(graphics, slot.x, slot.y);
        }
    }

    private void drawSlotFrame(GuiGraphics graphics, int slotX, int slotY) {
        int x = this.leftPos + slotX;
        int y = this.topPos + slotY;
        graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF26384D);
        graphics.fill(x, y, x + 16, y + 16, 0xFF071019);
        graphics.fill(x + 1, y + 1, x + 15, y + 15, 0xFF111D2B);
    }

    private boolean shouldRenderGhostSlots() {
        if (!this.menu.getCarried().isEmpty()) {
            return false;
        }

        for (int i = 0; i < LightsaberCraftingTableLogic.INPUT_SLOT_COUNT; i++) {
            if (this.menu.getSlot(i).hasItem()) {
                return false;
            }
        }

        return true;
    }

    private ItemStack getLivePreviewStack() {
        SimpleContainer liveInputs = new SimpleContainer(LightsaberCraftingTableLogic.INPUT_SLOT_COUNT);

        for (int i = 0; i < LightsaberCraftingTableLogic.INPUT_SLOT_COUNT; i++) {
            liveInputs.setItem(i, this.menu.getSlot(i).getItem().copy());
        }

        return LightsaberCraftingTableLogic.activatePreview(
                LightsaberCraftingTableLogic.evaluate(liveInputs,
                        this.menu.getPartColor(LightsaberPartType.EMITTER),
                        this.menu.getPartColor(LightsaberPartType.SWITCH_SECTION),
                        this.menu.getPartColor(LightsaberPartType.GRIP),
                        this.menu.getPartColor(LightsaberPartType.POMMEL)).result()
        );
    }

    private void renderPreview(GuiGraphics graphics) {
        ItemStack preview = getLivePreviewStack();
        int panelLeft = this.leftPos + PREVIEW_X;
        int panelTop = this.topPos + PREVIEW_Y;
        int panelRight = panelLeft + PREVIEW_W;
        int panelBottom = panelTop + PREVIEW_H;

        graphics.fill(panelLeft, panelTop, panelRight, panelBottom, 0xB0081018);
        graphics.fill(panelLeft + 1, panelTop + 1, panelRight - 1, panelBottom - 1, 0xCC0F1824);
        graphics.fill(panelLeft + 6, panelTop + 6, panelRight - 6, panelBottom - 6, 0xAA061019);

        if (preview.isEmpty()) {
            graphics.drawCenteredString(this.font, Component.literal("Assemble a saber"), panelLeft + (PREVIEW_W / 2), panelTop + 30, 0xC3D4E8);
            graphics.drawCenteredString(this.font, Component.literal("then tune its RGB"), panelLeft + (PREVIEW_W / 2), panelTop + 42, 0x8FA5C0);
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        float spin = (minecraft.player == null ? 0.0F : minecraft.player.tickCount) + minecraft.getFrameTime();

        graphics.enableScissor(panelLeft + 4, panelTop + 4, panelRight - 4, panelBottom - 4);
        graphics.flush();
        graphics.pose().pushPose();

        graphics.pose().translate(panelLeft + PREVIEW_W * 0.52F, panelTop + PREVIEW_H * 0.52F, 130.0F);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(-90.0F));
        graphics.pose().mulPose(Axis.YP.rotationDegrees(90.0F + spin));
        graphics.pose().mulPose(Axis.YP.rotationDegrees((float) Math.sin(spin / 18.0F) * 2.0F));
        graphics.pose().mulPose(Axis.ZP.rotationDegrees((float) Math.sin(spin / 22.0F + 1.7F) * 2.0F));
        graphics.pose().scale(-17.0F, 17.0F, 17.0F);

        RenderSystem.enableDepthTest();
        MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
        ModItemRenderer.renderForgePreview(preview, graphics.pose(), buffer, 0xF000F0, OverlayTexture.NO_OVERLAY);
        buffer.endBatch();

        graphics.pose().popPose();
        graphics.flush();
        graphics.disableScissor();
    }

    private void renderSectionButtons(GuiGraphics graphics, int mouseX, int mouseY) {
        LightsaberPartType[] parts = LightsaberPartType.values();
        for (int i = 0; i < parts.length; i++) {
            LightsaberPartType part = parts[i];
            int x = this.leftPos + SECTION_X;
            int y = this.topPos + SECTION_Y + i * (SECTION_H + SECTION_GAP);
            boolean selected = part == selectedPart;
            boolean hovered = mouseX >= x && mouseX < x + SECTION_W && mouseY >= y && mouseY < y + SECTION_H;
            int color = this.menu.getPartColor(part);

            int background = selected ? 0xFF324C72 : hovered ? 0xE2273951 : 0xD91A2738;
            graphics.fill(x, y, x + SECTION_W, y + SECTION_H, 0xFF090E14);
            graphics.fill(x + 1, y + 1, x + SECTION_W - 1, y + SECTION_H - 1, background);
            graphics.fill(x + 4, y + 3, x + 15, y + SECTION_H - 3, 0xFF000000 | color);
            graphics.fill(x + 5, y + 4, x + 14, y + SECTION_H - 4, 0x90000000 | color);
            graphics.drawString(this.font, getCompactPartName(part), x + 20, y + 3, selected ? 0xFFFFFF : 0xD6E6F7, false);
        }
    }

    private void renderRgbEditor(GuiGraphics graphics) {
        int panelLeft = this.leftPos + PANEL_X;
        int panelTop = this.topPos + PANEL_Y;
        int color = this.menu.getPartColor(selectedPart);
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;
        int swatchLeft = panelLeft + PANEL_W - 36;

        graphics.fill(panelLeft, panelTop, panelLeft + PANEL_W, panelTop + PANEL_H, 0xAA0A1018);
        graphics.fill(panelLeft + 1, panelTop + 1, panelLeft + PANEL_W - 1, panelTop + PANEL_H - 1, 0xCC132131);
        graphics.drawString(this.font, getCompactPartName(selectedPart) + " RGB", panelLeft + 8, panelTop + 6, 0xF0F6FF, false);
        graphics.fill(swatchLeft, panelTop + 6, swatchLeft + 28, panelTop + 22, 0xFF000000 | color);
        graphics.drawString(this.font, "#" + toHex(color), panelLeft + 8, panelTop + 22, 0xB9D2F5, false);

        renderSlider(graphics, 0, "R", red, 0xFFFF5555);
        renderSlider(graphics, 1, "G", green, 0xFF55FF55);
        renderSlider(graphics, 2, "B", blue, 0xFF5555FF);
    }

    private void renderSlider(GuiGraphics graphics, int channel, String label, int value, int fillColor) {
        int x = this.leftPos + SLIDER_X;
        int y = getSliderY(channel);
        int fill = Math.round((value / 255.0F) * SLIDER_W);
        int knobX = x + fill;

        graphics.drawString(this.font, label, x - 14, y - 1, 0xE4EFFD, false);
        graphics.fill(x, y, x + SLIDER_W, y + SLIDER_H, 0xFF0B1119);
        graphics.fill(x + 1, y + 1, x + SLIDER_W - 1, y + SLIDER_H - 1, 0xFF1A2635);
        graphics.fill(x + 1, y + 1, x + Math.max(1, fill), y + SLIDER_H - 1, fillColor);
        graphics.fill(knobX - 2, y - 2, knobX + 2, y + SLIDER_H + 2, 0xFFF3F8FF);
        graphics.drawString(this.font, Integer.toString(value), x + SLIDER_W + 6, y - 1, 0xDCE9FF, false);
    }

    private int getSliderY(int channel) {
        return this.topPos + PANEL_Y + 34 + channel * 9;
    }

    private void renderGhostSlots(GuiGraphics graphics) {
        if (this.minecraft == null || this.minecraft.player == null || AdvancedLightsaberLegacyHilts.HILTS.isEmpty()) {
            return;
        }

        int cycle = (this.minecraft.player.tickCount / 30) % AdvancedLightsaberLegacyHilts.HILTS.size();
        List<String> families = new ArrayList<>(AdvancedLightsaberLegacyHilts.HILTS.keySet());
        String family = families.get(cycle);

        maybeRenderGhost(graphics, LightsaberCraftingTableLogic.SLOT_EMITTER,
                new ItemStack(galaxyunderchaos.LIGHTSABER_PARTS.get(family + "_" + LightsaberPartType.EMITTER.getSerializedName()).get()));
        maybeRenderGhost(graphics, LightsaberCraftingTableLogic.SLOT_SWITCH,
                new ItemStack(galaxyunderchaos.LIGHTSABER_PARTS.get(family + "_" + LightsaberPartType.SWITCH_SECTION.getSerializedName()).get()));
        maybeRenderGhost(graphics, LightsaberCraftingTableLogic.SLOT_GRIP,
                new ItemStack(galaxyunderchaos.LIGHTSABER_PARTS.get(family + "_" + LightsaberPartType.GRIP.getSerializedName()).get()));
        maybeRenderGhost(graphics, LightsaberCraftingTableLogic.SLOT_POMMEL,
                new ItemStack(galaxyunderchaos.LIGHTSABER_PARTS.get(family + "_" + LightsaberPartType.POMMEL.getSerializedName()).get()));
        maybeRenderGhost(graphics, LightsaberCraftingTableLogic.SLOT_CORE, ScreenGhostStacks.circuitryStack());

        List<ItemStack> kybers = ScreenGhostStacks.kyberStacks();
        if (!kybers.isEmpty()) {
            maybeRenderGhost(graphics, LightsaberCraftingTableLogic.SLOT_FLEX, kybers.get(cycle % kybers.size()));
        }

        List<ItemStack> modifiers = ScreenGhostStacks.modifierStacks();
        if (!modifiers.isEmpty()) {
            maybeRenderGhost(graphics, LightsaberCraftingTableLogic.SLOT_MODIFIER_A, modifiers.get(cycle % modifiers.size()));
            maybeRenderGhost(graphics, LightsaberCraftingTableLogic.SLOT_MODIFIER_B, modifiers.get((cycle + 1) % modifiers.size()));
        }
    }

    private void maybeRenderGhost(GuiGraphics graphics, int slotIndex, ItemStack stack) {
        if (this.menu.getSlot(slotIndex).hasItem() || stack.isEmpty()) {
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.18F);
        graphics.renderItem(stack, this.leftPos + this.menu.getSlot(slotIndex).x, this.topPos + this.menu.getSlot(slotIndex).y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            LightsaberPartType clickedPart = getClickedPart(mouseX, mouseY);
            if (clickedPart != null) {
                selectedPart = clickedPart;
                return true;
            }

            int slider = getClickedSlider(mouseX, mouseY);
            if (slider >= 0) {
                draggingChannel = slider;
                updateSliderFromMouse(mouseX, slider);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 && draggingChannel >= 0) {
            updateSliderFromMouse(mouseX, draggingChannel);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingChannel = -1;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private LightsaberPartType getClickedPart(double mouseX, double mouseY) {
        LightsaberPartType[] parts = LightsaberPartType.values();
        for (int i = 0; i < parts.length; i++) {
            int x = this.leftPos + SECTION_X;
            int y = this.topPos + SECTION_Y + i * (SECTION_H + SECTION_GAP);
            if (mouseX >= x && mouseX < x + SECTION_W && mouseY >= y && mouseY < y + SECTION_H) {
                return parts[i];
            }
        }
        return null;
    }

    private int getClickedSlider(double mouseX, double mouseY) {
        int x = this.leftPos + SLIDER_X;
        for (int channel = 0; channel < 3; channel++) {
            int y = getSliderY(channel);
            if (mouseX >= x && mouseX <= x + SLIDER_W && mouseY >= y - 3 && mouseY <= y + SLIDER_H + 3) {
                return channel;
            }
        }
        return -1;
    }

    private void updateSliderFromMouse(double mouseX, int channel) {
        int x = this.leftPos + SLIDER_X;
        int value = Math.round((float) ((mouseX - x) / SLIDER_W) * 255.0F);
        value = Math.max(0, Math.min(255, value));

        int color = this.menu.getPartColor(selectedPart);
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;

        if (channel == 0) {
            red = value;
        } else if (channel == 1) {
            green = value;
        } else {
            blue = value;
        }

        int updated = ModularLightsaberData.clampPartColor((red << 16) | (green << 8) | blue);
        this.menu.setPartColor(selectedPart, updated);
        LightsaberFormNetworking.sendToServer(new LightsaberCustomizationColorPacket(selectedPart, updated));
    }

    private static String getCompactPartName(LightsaberPartType part) {
        return part == LightsaberPartType.SWITCH_SECTION ? "Switch" : part.getDisplayName();
    }

    private static String toHex(int color) {
        return String.format("%06X", color & 0xFFFFFF);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xF4F8FF, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);

        LightsaberPartType hoveredPart = getClickedPart(mouseX, mouseY);
        if (hoveredPart != null) {
            int color = this.menu.getPartColor(hoveredPart);
            graphics.renderTooltip(this.font,
                    List.of(
                            Component.literal(hoveredPart.getDisplayName()),
                            Component.literal("#" + toHex(color))
                    ),
                    java.util.Optional.empty(),
                    mouseX,
                    mouseY);
        } else {
            this.renderTooltip(graphics, mouseX, mouseY);
        }
    }
}
