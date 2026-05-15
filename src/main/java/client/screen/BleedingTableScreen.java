package client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.force.BleedingTableLightningPacket;
import server.galaxyunderchaos.force.ForceNetworking;
import server.galaxyunderchaos.menu.BleedingTableMenu;

public class BleedingTableScreen extends AbstractContainerScreen<BleedingTableMenu> {
    private static final ResourceLocation ALT_FONT = new ResourceLocation("minecraft", "alt");
    private Button lightningButton;

    public BleedingTableScreen(BleedingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 196;
        this.imageHeight = 224;
        this.titleLabelX = 10000;
        this.titleLabelY = 10000;
        this.inventoryLabelY = 122;
    }

    @Override
    protected void init() {
        super.init();
        this.lightningButton = Button.builder(Component.literal("Use Force Lightning"), button ->
                        ForceNetworking.sendToServer(new BleedingTableLightningPacket(this.menu.getBlockPos())))
                .bounds(this.leftPos + 36, this.topPos + 82, 124, 20)
                .build();
        this.addRenderableWidget(this.lightningButton);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(this.leftPos, this.topPos, this.leftPos + this.imageWidth, this.topPos + this.imageHeight, 0xF00A0506);
        graphics.fill(this.leftPos + 2, this.topPos + 2, this.leftPos + this.imageWidth - 2, this.topPos + this.imageHeight - 2, 0xEC1A090B);
        graphics.fill(this.leftPos + 7, this.topPos + 7, this.leftPos + this.imageWidth - 7, this.topPos + this.imageHeight - 7, 0xDD09090D);

        graphics.fill(this.leftPos, this.topPos, this.leftPos + this.imageWidth, this.topPos + 3, 0xA0F33A3A);
        graphics.fill(this.leftPos, this.topPos + this.imageHeight - 3, this.leftPos + this.imageWidth, this.topPos + this.imageHeight, 0xA0F33A3A);
        graphics.fill(this.leftPos, this.topPos, this.leftPos + 3, this.topPos + this.imageHeight, 0xA0F33A3A);
        graphics.fill(this.leftPos + this.imageWidth - 3, this.topPos, this.leftPos + this.imageWidth, this.topPos + this.imageHeight, 0xA0F33A3A);

        graphics.fill(this.leftPos + 10, this.topPos + 10, this.leftPos + this.imageWidth - 10, this.topPos + 35, 0x8005070E);
        graphics.fill(this.leftPos + 14, this.topPos + 39, this.leftPos + this.imageWidth - 14, this.topPos + 74, 0x65000000);
        graphics.fill(this.leftPos + 14, this.topPos + 120, this.leftPos + this.imageWidth - 14, this.topPos + 217, 0x5C000000);

        int slotX = this.leftPos + 89;
        int slotY = this.topPos + 48;
        graphics.fill(slotX - 7, slotY - 7, slotX + 25, slotY + 25, 0xAA000000);
        graphics.fill(slotX - 5, slotY - 5, slotX + 23, slotY + 23, 0xFFBD3C3C);
        graphics.fill(slotX - 3, slotY - 3, slotX + 21, slotY + 21, 0xFF25090A);
        graphics.fill(slotX, slotY, slotX + 18, slotY + 18, 0xFF120B0B);

        int altarX = this.leftPos + this.imageWidth / 2;
        int altarY = this.topPos + 58;
        graphics.fill(altarX - 60, altarY - 21, altarX + 60, altarY - 20, 0x33FF2E2E);
        graphics.fill(altarX - 60, altarY + 22, altarX + 60, altarY + 23, 0x33FF2E2E);
        graphics.fill(altarX - 1, altarY - 20, altarX + 1, altarY + 23, 0x44FF2E2E);
        graphics.fill(altarX - 36, altarY + 16, altarX + 36, altarY + 17, 0x66FF2E2E);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawCenteredString(this.font,
                Component.literal("BLEEDING TABLE").withStyle(style -> style.withFont(ALT_FONT).withBold(true)),
                this.imageWidth / 2,
                13,
                0xFFF06B6B);
        graphics.drawCenteredString(this.font,
                Component.literal("Channel lightning into one kyber crystal."),
                this.imageWidth / 2,
                27,
                0xFFD8B7B7);
        graphics.drawCenteredString(this.font, Component.literal("Crystal Vessel"), this.imageWidth / 2, 68, 0xFFE0C0C0);
        graphics.drawCenteredString(this.font, Component.literal("Requires Force Lightning + 35 Force"), this.imageWidth / 2, 106, 0xFFD6A0A0);
        graphics.drawString(this.font, this.playerInventoryTitle, 17, this.inventoryLabelY, 0xFFC8C8C8, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
