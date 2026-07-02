package client.screen;

import client.renderer.ship.ShipItemRendererHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.menu.ShipCraftingTableMenu;
import server.galaxyunderchaos.ship.ShipColorSection;
import server.galaxyunderchaos.ship.ShipCustomization;
import server.galaxyunderchaos.ship.ShipCustomizationColorPacket;
import server.galaxyunderchaos.ship.ShipNetworking;

public class ShipCraftingTableScreen extends AbstractContainerScreen<ShipCraftingTableMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(galaxyunderchaos.MODID, "textures/gui/container/ship_crafting_table.png");

    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 222;

    private static final int SECTION_X = 9;
    private static final int SECTION_Y = 36;
    private static final int SECTION_W = 72;
    private static final int SECTION_H = 18;
    private static final int SECTION_GAP = 4;

    private static final int PANEL_X = 93;
    private static final int PANEL_Y = 112;
    private static final int PANEL_W = 154;
    private static final int PANEL_H = 62;

    private static final int SLIDER_X = PANEL_X + 34;
    private static final int SLIDER_W = 80;
    private static final int SLIDER_H = 8;

    private static final int PREVIEW_X = 93;
    private static final int PREVIEW_Y = 18;
    private static final int PREVIEW_W = 154;
    private static final int PREVIEW_H = 90;

    private ShipColorSection selectedSection = ShipColorSection.BASE;
    private int draggingChannel = -1;
    private boolean rotatingPreview;
    private float previewYaw = 34.0F;
    private float previewPitch = -10.0F;

    public ShipCraftingTableScreen(ShipCraftingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 256;
        this.imageHeight = 270;
        this.inventoryLabelX = ShipCraftingTableMenu.PLAYER_INV_X;
        this.inventoryLabelY = ShipCraftingTableMenu.PLAYER_INV_Y - 12;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 14;
        this.titleLabelY = 10;
        this.inventoryLabelX = ShipCraftingTableMenu.PLAYER_INV_X;
        this.inventoryLabelY = ShipCraftingTableMenu.PLAYER_INV_Y - 12;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        RenderSystem.disableBlend();

        renderLowerBackdrop(graphics);
        renderPreview(graphics);
        renderSectionButtons(graphics, mouseX, mouseY);
        renderRgbEditor(graphics);
        renderInventoryPanel(graphics);
        renderDecorations(graphics);
    }

    private void renderLowerBackdrop(GuiGraphics graphics) {
        int left = this.leftPos + 4;
        int top = this.topPos + 0;
        int right = this.leftPos + this.imageWidth - 4;
        int bottom = this.topPos + this.imageHeight - 4;

        graphics.fill(left, top, right, bottom, 0xEE07101A);
        graphics.fill(left + 1, top + 1, right - 1, bottom - 1, 0xDD0D1825);
        graphics.fill(left + 5, top + 5, right - 5, bottom - 5, 0xAA111E2E);
    }

    private void renderInventoryPanel(GuiGraphics graphics) {
        int panelLeft = this.leftPos + ShipCraftingTableMenu.PLAYER_INV_X - 8;
        int panelTop = this.topPos + ShipCraftingTableMenu.PLAYER_INV_Y - 8;
        int panelRight = this.leftPos + ShipCraftingTableMenu.PLAYER_INV_X + 9 * 18 + 8;
        int panelBottom = this.topPos + ShipCraftingTableMenu.HOTBAR_Y + 18 + 8;

        graphics.fill(panelLeft, panelTop, panelRight, panelBottom, 0xAA050A10);
        graphics.fill(panelLeft + 1, panelTop + 1, panelRight - 1, panelBottom - 1, 0xCC101A27);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlotFrame(graphics, ShipCraftingTableMenu.PLAYER_INV_X + col * 18, ShipCraftingTableMenu.PLAYER_INV_Y + row * 18);
            }
        }

        for (int col = 0; col < 9; col++) {
            drawSlotFrame(graphics, ShipCraftingTableMenu.PLAYER_INV_X + col * 18, ShipCraftingTableMenu.HOTBAR_Y);
        }
    }

    private void drawSlotFrame(GuiGraphics graphics, int slotX, int slotY) {
        int x = this.leftPos + slotX;
        int y = this.topPos + slotY;
        graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF26384D);
        graphics.fill(x, y, x + 16, y + 16, 0xFF071019);
        graphics.fill(x + 1, y + 1, x + 15, y + 15, 0xFF111D2B);
    }

    private void renderDecorations(GuiGraphics graphics) {
//        graphics.drawString(this.font, "Ship Sections", this.leftPos + 11, this.topPos + 41, 0xDCE9FF, false);
        graphics.drawString(this.font, "Blueprint", this.leftPos + 11, this.topPos + 7, 0x9AB3D4, false);
        graphics.drawString(this.font, "Output", this.leftPos + 56, this.topPos + 7, 0x9AB3D4, false);
        graphics.drawString(this.font, "Preview", this.leftPos + 98, this.topPos + 7, 0xDCE9FF, false);
        graphics.drawString(this.font, "Recipes", this.leftPos + 205, this.topPos + 7, 0x90A9C9, false);
        graphics.drawString(this.font, "Drag ship preview to rotate", this.leftPos + 101, this.topPos + 97, 0x90A9C9, false);

        drawSlotFrame(graphics, ShipCraftingTableMenu.BLUEPRINT_SLOT_X, ShipCraftingTableMenu.BLUEPRINT_SLOT_Y);
        drawSlotFrame(graphics, ShipCraftingTableMenu.RESULT_SLOT_X, ShipCraftingTableMenu.RESULT_SLOT_Y);
    }

    private void renderSectionButtons(GuiGraphics graphics, int mouseX, int mouseY) {
        ShipColorSection[] sections = ShipColorSection.values();
        for (int i = 0; i < sections.length; i++) {
            ShipColorSection section = sections[i];
            int x = this.leftPos + SECTION_X;
            int y = this.topPos + SECTION_Y + i * (SECTION_H + SECTION_GAP);
            boolean selected = section == selectedSection;
            boolean hovered = mouseX >= x && mouseX < x + SECTION_W && mouseY >= y && mouseY < y + SECTION_H;
            int color = this.menu.getColor(section);

            int background = selected ? 0xFF324C72 : hovered ? 0xE2273951 : 0xD91A2738;
            graphics.fill(x, y, x + SECTION_W, y + SECTION_H, 0xFF090E14);
            graphics.fill(x + 1, y + 1, x + SECTION_W - 1, y + SECTION_H - 1, background);
            graphics.fill(x + 4, y + 4, x + 16, y + SECTION_H - 4, 0xFF000000 | color);
            graphics.fill(x + 5, y + 5, x + 15, y + SECTION_H - 5, 0x90000000 | color);
            graphics.drawString(this.font, section.getDisplayName(), x + 21, y + 5, selected ? 0xFFFFFF : 0xD6E6F7, false);
        }
    }

    private void renderRgbEditor(GuiGraphics graphics) {
        int panelLeft = this.leftPos + PANEL_X;
        int panelTop = this.topPos + PANEL_Y;
        int color = this.menu.getColor(selectedSection);
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;

        graphics.fill(panelLeft, panelTop, panelLeft + PANEL_W, panelTop + PANEL_H, 0xAA0A1018);
        graphics.fill(panelLeft + 1, panelTop + 1, panelLeft + PANEL_W - 1, panelTop + PANEL_H - 1, 0xCC132131);
        graphics.drawString(this.font, selectedSection.getDisplayName().getString() + " Color", panelLeft + 8, panelTop + 7, 0xF0F6FF, false);
        graphics.fill(panelLeft + 118, panelTop + 7, panelLeft + 146, panelTop + 25, 0xFF000000 | color);
        graphics.drawString(this.font, "#" + ShipCustomization.toHex(color), panelLeft + 8, panelTop + 25, 0xB9D2F5, false);

        renderSlider(graphics, 0, "R", red, 0xFFFF5555);
        renderSlider(graphics, 1, "G", green, 0xFF55FF55);
        renderSlider(graphics, 2, "B", blue, 0xFF5555FF);
    }

    private void renderSlider(GuiGraphics graphics, int channel, String label, int value, int fillColor) {
        int x = this.leftPos + SLIDER_X;
        int y = getSliderY(channel);
        int fill = Math.round((value / 255.0F) * SLIDER_W);
        int knobX = x + fill;

        graphics.drawString(this.font, label, x - 16, y - 1, 0xE4EFFD, false);
        graphics.fill(x, y, x + SLIDER_W, y + SLIDER_H, 0xFF0B1119);
        graphics.fill(x + 1, y + 1, x + SLIDER_W - 1, y + SLIDER_H - 1, 0xFF1A2635);
        graphics.fill(x + 1, y + 1, x + Math.max(1, fill), y + SLIDER_H - 1, fillColor);
        graphics.fill(knobX - 2, y - 2, knobX + 2, y + SLIDER_H + 2, 0xFFF3F8FF);
        graphics.drawString(this.font, Integer.toString(value), x + SLIDER_W + 8, y - 1, 0xDCE9FF, false);
    }

    private int getSliderY(int channel) {
        return this.topPos + PANEL_Y + 28 + channel * 11;
    }

    private void renderPreview(GuiGraphics graphics) {
        ItemStack preview = this.menu.getPreviewStack();
        int panelLeft = this.leftPos + PREVIEW_X;
        int panelTop = this.topPos + PREVIEW_Y;
        int panelRight = panelLeft + PREVIEW_W;
        int panelBottom = panelTop + PREVIEW_H;

        graphics.fill(panelLeft, panelTop, panelRight, panelBottom, 0xB0081018);
        graphics.fill(panelLeft + 1, panelTop + 1, panelRight - 1, panelBottom - 1, 0xCC0F1824);
        graphics.fill(panelLeft + 6, panelTop + 6, panelRight - 6, panelBottom - 6, 0xAA061019);

        if (preview.isEmpty()) {
            graphics.drawCenteredString(this.font, Component.literal("Insert a ship blueprint"), panelLeft + (PREVIEW_W / 2), panelTop + 34, 0xC3D4E8);
            graphics.drawCenteredString(this.font, Component.literal("to preview and craft"), panelLeft + (PREVIEW_W / 2), panelTop + 46, 0x8FA5C0);
            return;
        }

        graphics.enableScissor(panelLeft + 4, panelTop + 4, panelRight - 4, panelBottom - 4);
        graphics.flush();
        graphics.pose().pushPose();
        graphics.pose().translate(panelLeft + (PREVIEW_W / 2.0F), panelTop + 66.0F, 250.0F);

        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
        ShipItemRendererHelper.renderShipPreview(preview, graphics.pose(), buffer, 0xF000F0, previewYaw, previewPitch, 14.0F);
        buffer.endBatch();

        graphics.pose().popPose();
        graphics.flush();
        graphics.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (isMouseOverMenuSlot(mouseX, mouseY)) {
                return super.mouseClicked(mouseX, mouseY, button);
            }

            ShipColorSection clickedSection = getClickedSection(mouseX, mouseY);
            if (clickedSection != null) {
                selectedSection = clickedSection;
                return true;
            }

            int slider = getClickedSlider(mouseX, mouseY);
            if (slider >= 0) {
                draggingChannel = slider;
                updateSliderFromMouse(mouseX, slider);
                return true;
            }

            if (isMouseOverPreview(mouseX, mouseY)) {
                rotatingPreview = true;
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
        if (button == 0 && rotatingPreview) {
            this.previewYaw += (float) dragX * 1.35F;
            this.previewPitch = Math.max(-50.0F, Math.min(35.0F, this.previewPitch + (float) dragY * 0.75F));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingChannel = -1;
        rotatingPreview = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isMouseOverPreview(double mouseX, double mouseY) {
        int x = this.leftPos + PREVIEW_X;
        int y = this.topPos + PREVIEW_Y;
        return mouseX >= x && mouseX < x + PREVIEW_W && mouseY >= y && mouseY < y + PREVIEW_H;
    }

    private boolean isMouseOverMenuSlot(double mouseX, double mouseY) {
        for (net.minecraft.world.inventory.Slot slot : this.menu.slots) {
            int x = this.leftPos + slot.x;
            int y = this.topPos + slot.y;
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                return true;
            }
        }
        return false;
    }

    private ShipColorSection getClickedSection(double mouseX, double mouseY) {
        ShipColorSection[] sections = ShipColorSection.values();
        for (int i = 0; i < sections.length; i++) {
            int x = this.leftPos + SECTION_X;
            int y = this.topPos + SECTION_Y + i * (SECTION_H + SECTION_GAP);
            if (mouseX >= x && mouseX < x + SECTION_W && mouseY >= y && mouseY < y + SECTION_H) {
                return sections[i];
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

        int color = this.menu.getColor(selectedSection);
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

        int updated = (red << 16) | (green << 8) | blue;
        this.menu.setColor(selectedSection, updated);
        ShipNetworking.sendToServer(new ShipCustomizationColorPacket(selectedSection, updated));
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xF4F8FF, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xD5E7FF, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);

        if (isMouseOverPreview(mouseX, mouseY) && !isMouseOverMenuSlot(mouseX, mouseY) && !this.menu.getPreviewStack().isEmpty()) {
            graphics.renderTooltip(this.font, Component.literal("Left click + drag to rotate"), mouseX, mouseY);
        } else {
            ShipColorSection hoveredSection = getClickedSection(mouseX, mouseY);
            if (hoveredSection != null) {
                int color = this.menu.getColor(hoveredSection);
                graphics.renderTooltip(this.font,
                        java.util.List.of(
                                hoveredSection.getDisplayName(),
                                Component.literal("#" + ShipCustomization.toHex(color))
                        ),
                        java.util.Optional.empty(),
                        mouseX,
                        mouseY);
            } else {
                this.renderTooltip(graphics, mouseX, mouseY);
            }
        }
    }
}
