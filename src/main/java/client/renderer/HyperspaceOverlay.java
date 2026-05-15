package client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

/**
 * Fully coded hyperspace sequence.
 *
 * This intentionally does not rely on an external PNG/sprite sheet.  The old overlay referenced
 * textures/gui/hyperspace_animation.png, but that file is not present in the resource pack, so the
 * jump effect silently failed or rendered as missing texture.  This renderer builds the charge,
 * tunnel streaks, blue-white flare, and exit fade directly from the current frame time.
 */
public class HyperspaceOverlay extends Overlay {
    public static final int DEFAULT_DURATION_TICKS = 100;

    private static boolean active;
    private static long startMs;
    private static long durationMs = DEFAULT_DURATION_TICKS * 50L;

    public static void startWarpEffect() {
        startWarpEffect(DEFAULT_DURATION_TICKS);
    }

    public static void startWarpEffect(int durationTicks) {
        active = true;
        startMs = Util.getMillis();
        durationMs = Math.max(20L, durationTicks * 50L);
    }

    public static void startOverlay() {
        startWarpEffect(DEFAULT_DURATION_TICKS);
    }

    public static void stopWarpEffect() {
        active = false;
    }

    public static boolean isActive() {
        return active;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!active) {
            return;
        }

        long elapsed = Util.getMillis() - startMs;
        float progress = Mth.clamp((float) elapsed / (float) durationMs, 0.0F, 1.0F);
        if (progress >= 1.0F) {
            stopWarpEffect();
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        float charge = smoothstep(0.00F, 0.24F, progress);
        float tunnel = smoothstep(0.16F, 0.52F, progress) * (1.0F - smoothstep(0.82F, 1.00F, progress));
        float flash = pulse(progress, 0.46F, 0.08F) + pulse(progress, 0.88F, 0.07F);
        float exitFade = smoothstep(0.70F, 1.00F, progress);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        int darkAlpha = (int) (Mth.clamp(0.35F + tunnel * 0.38F - exitFade * 0.20F, 0.0F, 0.85F) * 255.0F);
        guiGraphics.fill(0, 0, width, height, argb(darkAlpha, 4, 8, 20));

        renderStarTunnel(guiGraphics, width, height, centerX, centerY, progress, charge, tunnel);
        renderCenterFlare(guiGraphics, centerX, centerY, progress, charge, tunnel, flash);
        renderExitWash(guiGraphics, width, height, flash, exitFade);

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private static void renderStarTunnel(GuiGraphics guiGraphics, int width, int height, int centerX, int centerY,
                                         float progress, float charge, float tunnel) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        float maxRadius = (float) Math.sqrt(width * width + height * height);
        int streakCount = 104;
        for (int i = 0; i < streakCount; i++) {
            double angle = i * 2.399963229728653D + Math.sin(i * 12.9898D) * 0.18D;
            float lane = fract((float) (Math.sin(i * 78.233D) * 43758.5453D));
            float phase = fract(lane + progress * (1.85F + tunnel * 4.75F));
            float radial = 14.0F + phase * maxRadius;
            float length = (12.0F + phase * 92.0F) * (0.35F + tunnel * 1.85F + charge * 0.50F);
            float thickness = 0.45F + tunnel * 1.15F + phase * 0.85F;
            float alpha = Mth.clamp((1.0F - phase) * (0.16F + tunnel * 0.82F + charge * 0.22F), 0.0F, 0.92F);

            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle) * 0.62F;
            float inv = (float) (1.0D / Math.sqrt(dx * dx + dy * dy));
            dx *= inv;
            dy *= inv;

            float startX = centerX + dx * radial;
            float startY = centerY + dy * radial;
            float endX = centerX + dx * (radial + length);
            float endY = centerY + dy * (radial + length);

            // Alternate between blue-white and cold cyan lanes to keep the tunnel alive without a texture.
            int r = (i & 3) == 0 ? 180 : 225;
            int g = (i & 3) == 0 ? 225 : 245;
            int b = 255;
            quadLine(buffer, matrix, startX, startY, endX, endY, thickness, r, g, b, (int) (alpha * 255.0F));
        }

        tesselator.end();
    }

    private static void renderCenterFlare(GuiGraphics guiGraphics, int centerX, int centerY,
                                          float progress, float charge, float tunnel, float flash) {
        float collapse = 1.0F - smoothstep(0.00F, 0.22F, progress);
        int ring = (int) (12.0F + charge * 30.0F + tunnel * 18.0F + flash * 55.0F);
        int core = (int) (3.0F + collapse * 18.0F + flash * 24.0F);

        int ringAlpha = (int) (Mth.clamp(0.18F + charge * 0.34F + tunnel * 0.30F + flash * 0.38F, 0.0F, 0.78F) * 255.0F);
        int coreAlpha = (int) (Mth.clamp(0.30F + charge * 0.40F + tunnel * 0.25F + flash * 0.60F, 0.0F, 1.0F) * 255.0F);

        guiGraphics.fill(centerX - ring, centerY - 2, centerX + ring, centerY + 2, argb(ringAlpha, 120, 210, 255));
        guiGraphics.fill(centerX - 2, centerY - ring, centerX + 2, centerY + ring, argb(ringAlpha, 120, 210, 255));
        guiGraphics.fill(centerX - core, centerY - core, centerX + core, centerY + core, argb(coreAlpha, 245, 250, 255));
    }

    private static void renderExitWash(GuiGraphics guiGraphics, int width, int height, float flash, float exitFade) {
        int alpha = (int) (Mth.clamp(flash * 0.75F + exitFade * 0.18F, 0.0F, 0.86F) * 255.0F);
        if (alpha > 0) {
            guiGraphics.fill(0, 0, width, height, argb(alpha, 210, 238, 255));
        }
    }

    private static void quadLine(BufferBuilder buffer, Matrix4f matrix, float x1, float y1, float x2, float y2,
                                 float thickness, int r, int g, int b, int a) {
        if (a <= 0) {
            return;
        }
        float dx = x2 - x1;
        float dy = y2 - y1;
        float inv = (float) (1.0D / Math.sqrt(dx * dx + dy * dy));
        float ox = -dy * inv * thickness;
        float oy = dx * inv * thickness;
        float z = 0.0F;

        buffer.vertex(matrix, x1 - ox, y1 - oy, z).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1 + ox, y1 + oy, z).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2 + ox, y2 + oy, z).color(r, g, b, 0).endVertex();
        buffer.vertex(matrix, x2 - ox, y2 - oy, z).color(r, g, b, 0).endVertex();
    }

    private static float smoothstep(float edge0, float edge1, float value) {
        float x = Mth.clamp((value - edge0) / (edge1 - edge0), 0.0F, 1.0F);
        return x * x * (3.0F - 2.0F * x);
    }

    private static float pulse(float value, float center, float width) {
        return 1.0F - Mth.clamp(Math.abs(value - center) / width, 0.0F, 1.0F);
    }

    private static float fract(float value) {
        return value - Mth.floor(value);
    }

    private static int argb(int alpha, int red, int green, int blue) {
        return (Mth.clamp(alpha, 0, 255) << 24)
                | (Mth.clamp(red, 0, 255) << 16)
                | (Mth.clamp(green, 0, 255) << 8)
                | Mth.clamp(blue, 0, 255);
    }
}
