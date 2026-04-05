package client.renderer.lightsaber;

import client.renderer.lightsaber.legacy.LegacyRenderStates;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;
import server.galaxyunderchaos.lightsaber.ModularLightsaberData;

import java.util.EnumSet;
import java.util.Random;

public final class LegacyLightsaberBladeRenderer {
    private static final float ROOT_SCALE = 3.0F;
    private static final float HEIGHT_FACTOR = 0.0234375F;
    private static final float BASE_OFFSET = 0.095F;

    private static final float MAIN_LENGTH_SCALE = 1.20F;

    private static final float BODY_HALF = 0.03125F;
    private static final float TIP_SIZE = 0.03125F;
    private static final float TIP_LENGTH = 0.125F;

    private static final int MAIN_SMOOTH = 10;
    private static final int CROSS_SMOOTH = 10;
    private static final float MAIN_OUTER_WIDTH = 0.60F;
    private static final float CROSS_OUTER_WIDTH = 0.40F;
    private static final float MAIN_OUTER_ALPHA = 0.10F;
    private static final float CROSS_OUTER_ALPHA = 0.10F;

    private static final Object TYPE_LOCK = new Object();
    private static RenderType outerBladeType;
    private static RenderType innerBladeType;

    private LegacyLightsaberBladeRenderer() {
    }

    private static RenderType outerBladeType() {
        RenderType type = outerBladeType;
        if (type == null) {
            synchronized (TYPE_LOCK) {
                type = outerBladeType;
                if (type == null) {
                    type = createBladeType(
                            "legacy_lightsaber_blade_outer",
                            LegacyRenderStates.lightningTransparency(),
                            LegacyRenderStates.colorWrite()
                    );
                    outerBladeType = type;
                }
            }
        }
        return type;
    }

    private static RenderType innerBladeType() {
        RenderType type = innerBladeType;
        if (type == null) {
            synchronized (TYPE_LOCK) {
                type = innerBladeType;
                if (type == null) {
                    type = createBladeType(
                            "legacy_lightsaber_blade_inner",
                            LegacyRenderStates.noTransparency(),
                            LegacyRenderStates.colorDepthWrite()
                    );
                    innerBladeType = type;
                }
            }
        }
        return type;
    }

    public static void renderMainBlade(PoseStack poseStack,
                                       MultiBufferSource buffer,
                                       ItemStack stack,
                                       String bladeColor,
                                       float switchHeightPx,
                                       float emitterHeightPx,
                                       float bladeLength,
                                       boolean downward,
                                       int overlay) {
        renderBlade(
                poseStack,
                buffer,
                stack,
                bladeColor,
                switchHeightPx,
                emitterHeightPx,
                bladeLength * MAIN_LENGTH_SCALE,
                downward,
                false
        );
    }

    public static void applyBladeRootTransform(PoseStack poseStack,
                                               float switchHeightPx,
                                               float emitterHeightPx) {
        applyRootTransform(poseStack, switchHeightPx, emitterHeightPx);
        poseStack.translate(0.0F, BASE_OFFSET, 0.0F);
    }

    public static void renderCrossguardGeometry(PoseStack poseStack,
                                                MultiBufferSource buffer,
                                                ItemStack stack,
                                                String bladeColor,
                                                float bladeLength,
                                                int overlay) {
        EnumSet<BladeModifierCrystal> modifiers = ModularLightsaberData.getBladeModifiers(stack);
        LightsaberColorResolver.BladeTint tint = LightsaberColorResolver.resolve(bladeColor);

        RenderType outer = outerBladeType();
        RenderType inner = innerBladeType();

        renderOuterGlow(poseStack, buffer, modifiers, bladeColor, bladeLength, tint.red(), tint.green(), tint.blue(), true, outer);
        flushIfPossible(buffer, outer);

        renderInnerCore(poseStack, buffer, modifiers, bladeColor, bladeLength, true, inner);
        flushIfPossible(buffer, inner);
    }

    private static void renderBlade(PoseStack poseStack,
                                    MultiBufferSource buffer,
                                    ItemStack stack,
                                    String bladeColor,
                                    float switchHeightPx,
                                    float emitterHeightPx,
                                    float bladeLength,
                                    boolean downward,
                                    boolean crossguard) {
        poseStack.pushPose();

        applyRootTransform(poseStack, switchHeightPx, emitterHeightPx);

        if (downward) {
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }

        EnumSet<BladeModifierCrystal> modifiers = ModularLightsaberData.getBladeModifiers(stack);
        LightsaberColorResolver.BladeTint tint = LightsaberColorResolver.resolve(bladeColor);

        RenderType outer = outerBladeType();
        RenderType inner = innerBladeType();

        poseStack.translate(0.0F, BASE_OFFSET, 0.0F);

        renderOuterGlow(poseStack, buffer, modifiers, bladeColor, bladeLength, tint.red(), tint.green(), tint.blue(), crossguard, outer);
        flushIfPossible(buffer, outer);

        renderInnerCore(poseStack, buffer, modifiers, bladeColor, bladeLength, crossguard, inner);
        flushIfPossible(buffer, inner);

        poseStack.popPose();
    }

    private static void applyRootTransform(PoseStack poseStack, float switchHeightPx, float emitterHeightPx) {
        poseStack.scale(ROOT_SCALE, ROOT_SCALE, ROOT_SCALE);
        poseStack.translate(0.0F, -((switchHeightPx + emitterHeightPx) * HEIGHT_FACTOR), 0.0F);
    }

    private static RenderType createBladeType(String name,
                                              RenderStateShard.TransparencyStateShard transparency,
                                              RenderStateShard.WriteMaskStateShard writeMask) {
        return RenderType.create(
                name,
                DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(LegacyRenderStates.positionColorShader())
                        .setTransparencyState(transparency)
                        .setCullState(LegacyRenderStates.noCull())
                        .setLightmapState(LegacyRenderStates.noLightmap())
                        .setOverlayState(LegacyRenderStates.noOverlay())
                        .setWriteMaskState(writeMask)
                        .createCompositeState(false)
        );
    }

    private static void flushIfPossible(MultiBufferSource buffer, RenderType type) {
        if (buffer instanceof MultiBufferSource.BufferSource source) {
            source.endBatch(type);
        }
    }

    private static void renderOuterGlow(PoseStack poseStack,
                                        MultiBufferSource buffer,
                                        EnumSet<BladeModifierCrystal> modifiers,
                                        String bladeColor,
                                        float bladeLength,
                                        float red,
                                        float green,
                                        float blue,
                                        boolean crossguard,
                                        RenderType outerType) {
        boolean fineCut = modifiers.contains(BladeModifierCrystal.FINE_CUT);
        boolean compressed = modifiers.contains(BladeModifierCrystal.COMPRESSED);
        boolean invertedPrismatic = modifiers.contains(BladeModifierCrystal.INVERTING) && modifiers.contains(BladeModifierCrystal.PRISMATIC);

        int smooth = compressed ? 7 : (crossguard ? CROSS_SMOOTH : MAIN_SMOOTH);
        float width = compressed ? (crossguard ? 0.20F : 0.40F) : (crossguard ? CROSS_OUTER_WIDTH : MAIN_OUTER_WIDTH);
        float alphaBase = compressed ? 0.07F : (crossguard ? CROSS_OUTER_ALPHA : MAIN_OUTER_ALPHA);

        if ("blood_orange".equals(bladeColor)) {
            alphaBase = crossguard ? 0.15F : 0.17F;
        } else if ("amber".equals(bladeColor)) {
            alphaBase = crossguard ? 0.16F : 0.18F;
        } else if ("pink".equals(bladeColor)) {
            alphaBase = crossguard ? 0.16F : 0.18F;
        } else if ("maroon".equals(bladeColor)) {
            alphaBase = 0.11F;
        } else if ("dark_blue".equals(bladeColor)) {
            alphaBase = crossguard ? 0.11F : 0.12F;
        } else if ("turquoise".equals(bladeColor)) {
            alphaBase = 0.11F;
        } else if ("arctic_blue".equals(bladeColor)) {
            alphaBase = 0.09F;
        } else if ("white".equals(bladeColor)) {
            alphaBase = 0.07F;
        }

        if (invertedPrismatic) {
            red = 0.0F;
            green = 0.0F;
            blue = 0.0F;
            alphaBase *= 1.5F;
        }

        float xScale = fineCut ? 0.55F : 1.0F;
        float yScale = fineCut ? 0.925F : 1.0F;
        float zScale = fineCut ? (crossguard ? 1.30F : 1.10F) : 1.0F;
        if (compressed && crossguard) {
            yScale *= 0.90F;
        }

        int layerCount = Math.max(1, 5 * smooth);
        VertexConsumer consumer = buffer.getBuffer(outerType);

        for (int i = 0; i < layerCount; ++i) {
            float scale = 1.0F + i * (width / smooth);
            float f4 = (float) i / (float) layerCount * 50.0F;

            poseStack.pushPose();
            poseStack.scale(scale * xScale,
                    (crossguard ? (1.0F - f4 * 0.05F + 2.0F) : (1.0F - f4 * 0.005F + 0.2F)) * yScale,
                    scale * zScale);
            poseStack.translate(0.0F, -f4 / 400.0F + 0.06F, 0.0F);

            if (fineCut) {
                poseStack.translate(0.0F, 0.0F, 0.005F + f4 * 0.00001F);
            }

            emitBladeBody(poseStack, consumer, bladeLength, red, green, blue, alphaBase / smooth);
            poseStack.popPose();
        }
    }

    private static void renderInnerCore(PoseStack poseStack,
                                        MultiBufferSource buffer,
                                        EnumSet<BladeModifierCrystal> modifiers,
                                        String bladeColor,
                                        float bladeLength,
                                        boolean crossguard,
                                        RenderType innerType) {
        VertexConsumer consumer = buffer.getBuffer(innerType);
        LightsaberColorResolver.BladeTint tint = LightsaberColorResolver.resolve(bladeColor);

        float coreRed = 1.0F;
        float coreGreen = 1.0F;
        float coreBlue = 1.0F;

        if (modifiers.contains(BladeModifierCrystal.INVERTING)) {
            coreRed = 0.0F;
            coreGreen = 0.0F;
            coreBlue = 0.0F;
        } else if (modifiers.contains(BladeModifierCrystal.PRISMATIC)) {
            coreRed = tint.red();
            coreGreen = tint.green();
            coreBlue = tint.blue();
        }

        if (modifiers.contains(BladeModifierCrystal.CRACKED)) {
            renderCrackedInnerVariants(poseStack, consumer, modifiers, bladeLength, crossguard, coreRed, coreGreen, coreBlue);
        }

        poseStack.pushPose();
        applyInnerCrystalTransforms(poseStack, modifiers, crossguard);
        emitBladeBody(poseStack, consumer, bladeLength, coreRed, coreGreen, coreBlue, 1.0F);
        poseStack.translate(0.0F, -bladeLength, 0.0F);
        emitLegacyTip(poseStack, consumer, TIP_SIZE, TIP_LENGTH, coreRed, coreGreen, coreBlue, 1.0F);
        poseStack.popPose();
    }

    private static void applyInnerCrystalTransforms(PoseStack poseStack,
                                                    EnumSet<BladeModifierCrystal> modifiers,
                                                    boolean crossguard) {
        if (crossguard && modifiers.contains(BladeModifierCrystal.FINE_CUT)) {
            poseStack.scale(1.0F, 1.2F, 1.0F);
        }
        if (modifiers.contains(BladeModifierCrystal.COMPRESSED)) {
            poseStack.scale(0.6F, 1.0F, 0.6F);
        }
        if (modifiers.contains(BladeModifierCrystal.FINE_CUT)) {
            poseStack.scale(0.55F, 1.0F, 1.10F);
        }
    }

    private static void renderCrackedInnerVariants(PoseStack poseStack,
                                                   VertexConsumer consumer,
                                                   EnumSet<BladeModifierCrystal> modifiers,
                                                   float bladeLength,
                                                   boolean crossguard,
                                                   float red,
                                                   float green,
                                                   float blue) {
        Minecraft minecraft = Minecraft.getInstance();
        int ticks = minecraft.player != null ? minecraft.player.tickCount : 0;
        Random random = new Random((ticks % 100L) * 1000L);

        for (int i = 0; i < 3; ++i) {
            poseStack.pushPose();
            poseStack.translate((random.nextFloat() - 0.5F) / 60.0F, 0.0F, (random.nextFloat() - 0.5F) / 60.0F);
            applyInnerCrystalTransforms(poseStack, modifiers, crossguard);
            emitBladeBody(poseStack, consumer, bladeLength, red, green, blue, 0.9F);
            poseStack.popPose();
        }
    }

    private static void emitBladeBody(PoseStack poseStack,
                                      VertexConsumer consumer,
                                      float bladeLength,
                                      float red,
                                      float green,
                                      float blue,
                                      float alpha) {
        float minX = -BODY_HALF;
        float maxX = BODY_HALF;
        float minY = -bladeLength;
        float maxY = 0.0F;
        float minZ = -BODY_HALF;
        float maxZ = BODY_HALF;

        Matrix4f pose = poseStack.last().pose();

        emitQuad(consumer, pose,
                minX, minY, maxZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                maxX, minY, minZ,
                minX, minY, minZ,
                minX, maxY, minZ,
                maxX, maxY, minZ,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                minX, minY, minZ,
                minX, minY, maxZ,
                minX, maxY, maxZ,
                minX, maxY, minZ,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                maxX, minY, maxZ,
                maxX, minY, minZ,
                maxX, maxY, minZ,
                maxX, maxY, maxZ,
                red, green, blue, alpha);
    }

    private static void emitLegacyTip(PoseStack poseStack,
                                      VertexConsumer consumer,
                                      float half,
                                      float tipLength,
                                      float red,
                                      float green,
                                      float blue,
                                      float alpha) {
        Matrix4f pose = poseStack.last().pose();

        // Square base at y = 0, centered on the blade
        float x1 = -half, z1 = -half;
        float x2 =  half, z2 = -half;
        float x3 =  half, z3 =  half;
        float x4 = -half, z4 =  half;

        // Apex extends forward along blade direction
        float ax = 0.0F;
        float ay = -tipLength;
        float az = 0.0F;

        // 4 triangular sides, emitted as quads by repeating the apex once
        emitQuad(consumer, pose,
                x4, 0.0F, z4,
                x3, 0.0F, z3,
                ax, ay, az,
                ax, ay, az,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                x3, 0.0F, z3,
                x2, 0.0F, z2,
                ax, ay, az,
                ax, ay, az,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                x2, 0.0F, z2,
                x1, 0.0F, z1,
                ax, ay, az,
                ax, ay, az,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                x1, 0.0F, z1,
                x4, 0.0F, z4,
                ax, ay, az,
                ax, ay, az,
                red, green, blue, alpha);
    }

    private static void emitQuad(VertexConsumer consumer,
                                 Matrix4f pose,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float x3, float y3, float z3,
                                 float x4, float y4, float z4,
                                 float red,
                                 float green,
                                 float blue,
                                 float alpha) {
        consumer.vertex(pose, x1, y1, z1).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose, x2, y2, z2).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose, x3, y3, z3).color(red, green, blue, alpha).endVertex();
        consumer.vertex(pose, x4, y4, z4).color(red, green, blue, alpha).endVertex();
    }
}
