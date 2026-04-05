package client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.resources.ResourceLocation;

public class HyperspaceOverlay extends Overlay {
    private static final ResourceLocation HYPERSPACE_TEXTURE =
            new ResourceLocation("galaxyunderchaos", "textures/gui/hyperspace_animation.png");

    private static boolean isWarping = false;
    private static boolean isOverlayActive = false;
    private static long startTime;
    private static final long warpDuration = 5000; // Warp effect duration in milliseconds

    // Start warp effect when hyperspace starts
    public static void startWarpEffect() {
        //System.out.println("Hyperspace overlay started!"); // Debugging statement
        isWarping = true;
        startTime = System.currentTimeMillis();
        isOverlayActive = true;
    }

    // Start overlay manually (if needed)
    public static void startOverlay() {
        //System.out.println("Hyperspace overlay manually started!"); // Debugging statement
        isOverlayActive = true;
    }

    // Stop warp effect
    public static void stopWarpEffect() {
        //System.out.println("Hyperspace overlay stopped!"); // Debugging statement
        isWarping = false;
        isOverlayActive = false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!isWarping && !isOverlayActive) return;

        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > warpDuration) {
            stopWarpEffect();
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        int totalFrames = 3;  // You have 6 frames
        int frameWidth = 1024; // Each frame is exactly 1024px
        int frameHeight = 1024; // Each frame is 1024px tall
        int frameIndex = (int) ((elapsed / 100) % totalFrames); // Cycle through frames

        int drawX = (screenWidth - frameWidth) / 2; // Center horizontally
        int drawY = (screenHeight - frameHeight) / 2; // Center vertically

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, HYPERSPACE_TEXTURE);

        // Correctly extract the frame based on its position in the sprite sheet
        guiGraphics.blit(HYPERSPACE_TEXTURE, drawX, drawY, frameIndex * frameWidth, 0, frameWidth, frameHeight, frameWidth * totalFrames, frameHeight);

        RenderSystem.disableBlend();
    }



}