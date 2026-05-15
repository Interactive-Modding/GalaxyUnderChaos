package client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;

public final class ForceAlignmentOverlay {
    private ForceAlignmentOverlay() {
    }

    public static void render(GuiGraphics graphics, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) {
            return;
        }

        minecraft.player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            int ticks = cap.getAlignmentFlashTicks();
            if (ticks <= 0 || cap.getAlignmentFlashSide() != ForceSide.DARK) {
                return;
            }

            int width = minecraft.getWindow().getGuiScaledWidth();
            int height = minecraft.getWindow().getGuiScaledHeight();
            float life = Math.min(1.0F, (ticks + partialTick) / 120.0F);
            float pulse = 0.65F + (float)Math.sin((minecraft.player.tickCount + partialTick) * 0.35F) * 0.18F;
            int alpha = Math.max(35, Math.min(145, (int)(life * pulse * 150.0F)));

            graphics.fill(0, 0, width, height, (alpha << 24) | 0x8F0000);
            graphics.fill(0, 0, width, 4, 0xAAFF0000);
            graphics.fill(0, height - 4, width, height, 0xAAFF0000);
            graphics.fill(0, 0, 4, height, 0xAAFF0000);
            graphics.fill(width - 4, 0, width, height, 0xAAFF0000);

            int textColor = 0xFFFF3030;
            Component message = Component.literal("Embracing dark side...");
            graphics.drawCenteredString(minecraft.font, message, width / 2, height / 2 - 5, textColor);
        });
    }
}
