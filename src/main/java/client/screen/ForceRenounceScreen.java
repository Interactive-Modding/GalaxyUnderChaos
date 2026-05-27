package client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import server.galaxyunderchaos.force.ForceNetworking;
import server.galaxyunderchaos.force.ForceRenounceConfirmPacket;
import server.galaxyunderchaos.force.ForceRenounceScreenPacket;

import java.util.Locale;

/** Full-screen renunciation confirmation. */
public class ForceRenounceScreen extends Screen {
    private final ForceRenounceScreenPacket data;

    public ForceRenounceScreen(ForceRenounceScreenPacket data) {
        super(Component.literal(data.title()));
        this.data = data;
    }

    public static void open(ForceRenounceScreenPacket data) {
        Minecraft.getInstance().setScreen(new ForceRenounceScreen(data));
    }

    @Override
    protected void init() {
        int panelW = Math.min(430, this.width - 32);
        int panelH = 190;
        int x = (this.width - panelW) / 2;
        int y = (this.height - panelH) / 2;
        addRenderableWidget(Button.builder(Component.literal(data.confirmLabel()), b -> {
            ForceNetworking.sendToServer(new ForceRenounceConfirmPacket(data.targetSide(), true));
            this.onClose();
        }).bounds(x + 28, y + panelH - 42, 180, 22).build());
        addRenderableWidget(Button.builder(Component.literal(data.cancelLabel()), b -> {
            ForceNetworking.sendToServer(new ForceRenounceConfirmPacket(data.targetSide(), false));
            this.onClose();
        }).bounds(x + panelW - 208, y + panelH - 42, 180, 22).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int overlay = overlayColor(data.targetSide());
        graphics.fill(0, 0, this.width, this.height, overlay);
        int panelW = Math.min(430, this.width - 32);
        int panelH = 190;
        int x = (this.width - panelW) / 2;
        int y = (this.height - panelH) / 2;
        int accent = accent(data.targetSide());

        graphics.fill(x, y, x + panelW, y + panelH, 0xF0101320);
        graphics.fill(x + 2, y + 2, x + panelW - 2, y + panelH - 2, 0xF0182030);
        graphics.fill(x, y, x + panelW, y + 4, accent);
        graphics.fill(x, y + panelH - 4, x + panelW, y + panelH, accent);
        graphics.drawCenteredString(this.font, data.title(), x + panelW / 2, y + 16, 0xFFFFFFFF);
        graphics.drawCenteredString(this.font, "Source: " + data.sourceName(), x + panelW / 2, y + 32, 0xFFC9CED8);
        graphics.drawWordWrap(this.font, Component.literal(data.body()), x + 24, y + 58, panelW - 48, 0xFFE7E8EF);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private static int overlayColor(String side) {
        return switch (normalized(side)) {
            case "DARK" -> 0xAA5A0000;
            case "LIGHT" -> 0xAA063A80;
            case "NEUTRAL" -> 0x99F0F4FF;
            default -> 0xAA000000;
        };
    }

    private static int accent(String side) {
        return switch (normalized(side)) {
            case "DARK" -> 0xFFFF2020;
            case "LIGHT" -> 0xFF4EA3FF;
            case "NEUTRAL" -> 0xFFE8ECF4;
            default -> 0xFFFFFFFF;
        };
    }

    private static String normalized(String side) {
        return side == null ? "" : side.toUpperCase(Locale.ROOT);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
