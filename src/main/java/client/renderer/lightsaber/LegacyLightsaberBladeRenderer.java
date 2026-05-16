/*
 * SPDX-License-Identifier: LGPL-3.0-only
 *
 * This file is part of Galaxy Under Chaos.
 * It contains code, data, model geometry, behavior, or compatibility logic
 * copied, translated, ported, adapted from, or created to support content
 * derived from Advanced Lightsabers 1.2 by FiskFille, credited to FiskFille
 * and Void Adept.
 *
 * Modifications for Galaxy Under Chaos / Minecraft Forge 1.20.1 by
 *  Vitiate and contributors.
 */

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
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;
import server.galaxyunderchaos.lightsaber.ModularLightsaberData;

import java.util.EnumSet;
import java.util.Random;

public final class LegacyLightsaberBladeRenderer {
    private static final float ROOT_SCALE = 3.0F;
    private static final float HEIGHT_FACTOR = 0.0234375F;
    private static final float BASE_OFFSET = 0.095F;

    private static final float MAIN_LENGTH_SCALE = 1.00F;

    private static final float BODY_HALF = 0.03125F;
    private static final float TIP_SIZE = 0.03125F;
    private static final float TIP_LENGTH = 0.125F;
    private static final int BLADE_CORE_SIDES = 16;

    private static final int FORCE_WHIP_SEGMENTS = 36;
    private static final float FORCE_WHIP_CURVE_X = 0.340F;
    private static final float FORCE_WHIP_CURVE_Z = 0.220F;
    private static final float FORCE_WHIP_TRAIL = 0.330F;
    private static final float FORCE_WHIP_DROOP = 0.340F;
    private static final float FORCE_WHIP_SWING_LAG = 1.050F;
    private static final float FORCE_WHIP_BASE_ANCHOR = 0.175F;
    private static final float FORCE_WHIP_SWING_SNAP = 1.120F;

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
                            LegacyRenderStates.rendertypeLightningShader(),
                            LegacyRenderStates.additiveGlow(),
                            LegacyRenderStates.colorWrite(),
                            LegacyRenderStates.lequalDepth(),
                            LegacyRenderStates.translucentTarget()
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
                            LegacyRenderStates.positionColorShader(),
                            LegacyRenderStates.noTransparency(),
                            LegacyRenderStates.colorDepthWrite(),
                            LegacyRenderStates.lequalDepth(),
                            null
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

        renderOuterGlow(poseStack, buffer, modifiers, bladeColor, bladeLength, tint.red(), tint.green(), tint.blue(), true, outer, WhipRenderState.NONE);
        flushIfPossible(buffer, outer);

        renderInnerCore(poseStack, buffer, modifiers, bladeColor, bladeLength, true, inner, WhipRenderState.NONE);
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
        boolean forceWhip = !crossguard && modifiers.contains(BladeModifierCrystal.FORCE_WHIP);

        if (forceWhip) {
            bladeLength *= 1.65F;
        }

        WhipRenderState whipState = forceWhip ? captureWhipState() : WhipRenderState.NONE;
        LightsaberColorResolver.BladeTint tint = LightsaberColorResolver.resolve(bladeColor);

        RenderType outer = outerBladeType();
        RenderType inner = innerBladeType();

        poseStack.translate(0.0F, BASE_OFFSET, 0.0F);

        renderOuterGlow(poseStack, buffer, modifiers, bladeColor, bladeLength, tint.red(), tint.green(), tint.blue(), crossguard, outer, whipState);
        flushIfPossible(buffer, outer);

        renderInnerCore(poseStack, buffer, modifiers, bladeColor, bladeLength, crossguard, inner, whipState);
        flushIfPossible(buffer, inner);

        poseStack.popPose();
    }

    private static void applyRootTransform(PoseStack poseStack, float switchHeightPx, float emitterHeightPx) {
        poseStack.scale(ROOT_SCALE, ROOT_SCALE, ROOT_SCALE);
        poseStack.translate(0.0F, -((switchHeightPx + emitterHeightPx) * HEIGHT_FACTOR), 0.0F);
    }

    private static RenderType createBladeType(String name,
                                              RenderStateShard.ShaderStateShard shader,
                                              RenderStateShard.TransparencyStateShard transparency,
                                              RenderStateShard.WriteMaskStateShard writeMask,
                                              RenderStateShard.DepthTestStateShard depthTest,
                                              RenderStateShard.OutputStateShard outputTarget) {
        var builder = RenderType.CompositeState.builder()
                .setShaderState(shader)
                .setTransparencyState(transparency)
                .setCullState(LegacyRenderStates.noCull())
                .setLightmapState(LegacyRenderStates.noLightmap())
                .setOverlayState(LegacyRenderStates.noOverlay())
                .setWriteMaskState(writeMask)
                .setDepthTestState(depthTest);

        if (outputTarget != null) {
            builder.setOutputState(outputTarget);
        }

        return RenderType.create(
                name,
                DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                builder.createCompositeState(false)
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
                                        RenderType outerType,
                                        WhipRenderState whipState) {
        boolean fineCut = modifiers.contains(BladeModifierCrystal.FINE_CUT);
        boolean compressed = modifiers.contains(BladeModifierCrystal.COMPRESSED);
        boolean forceWhip = !crossguard && modifiers.contains(BladeModifierCrystal.FORCE_WHIP);
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

        if (forceWhip) {
            width *= 0.75F;
            alphaBase *= 1.25F;
            xScale *= 0.70F;
            zScale *= 0.70F;
        }

        /*
         * Normal world-rendered outerglow.
         * This keeps the old shape, depth behavior, and shader-compatible lightning route.
         */
        float whipTick = forceWhip ? whipState.tick() : 0.0F;
        float whipAttackSwing = forceWhip ? whipState.attackSwing() : 0.0F;

        int layerCount = Math.max(1, 5 * smooth);
        VertexConsumer consumer = buffer.getBuffer(outerType);

        for (int i = 0; i < layerCount; ++i) {
            float scale = 1.0F + i * (width / smooth);
            float f4 = (float) i / (float) layerCount * 50.0F;

            poseStack.pushPose();

            if (forceWhip) {
                float whipWidth = scale * xScale;
                emitCurvedBladeBody(poseStack, consumer, bladeLength, red, green, blue, alphaBase / smooth, true, whipWidth, whipTick, whipAttackSwing);
                emitCurvedBladeTip(poseStack, consumer, bladeLength, TIP_SIZE * whipWidth, TIP_LENGTH, red, green, blue, alphaBase / smooth, whipTick, whipAttackSwing);
            } else {
                poseStack.scale(
                        scale * xScale,
                        (crossguard ? (1.0F - f4 * 0.05F + 2.0F) : (1.0F - f4 * 0.005F + 0.2F)) * yScale,
                        scale * zScale
                );

                poseStack.translate(0.0F, -f4 / 400.0F + 0.06F, 0.0F);

                if (fineCut) {
                    poseStack.translate(0.0F, 0.0F, 0.005F + f4 * 0.00001F);
                }

                emitBladeBody(poseStack, consumer, bladeLength, red, green, blue, alphaBase / smooth);
            }

            poseStack.popPose();
        }

        /*
         * Late private 3D color-restoration pass.
         * This replays the same generated glow geometry later to restore shader-resistant
         * color while keeping the force-whip curve values locked to this render pass.
         */
        LegacyLightsaberPrivateGlow.queueOuterGlow(
                new Matrix4f(poseStack.last().pose()),
                bladeLength,
                red,
                green,
                blue,
                alphaBase,
                smooth,
                width,
                xScale,
                yScale,
                zScale,
                crossguard,
                forceWhip,
                fineCut,
                whipTick,
                whipAttackSwing
        );
    }
    private static void renderInnerCore(PoseStack poseStack,
                                        MultiBufferSource buffer,
                                        EnumSet<BladeModifierCrystal> modifiers,
                                        String bladeColor,
                                        float bladeLength,
                                        boolean crossguard,
                                        RenderType innerType,
                                        WhipRenderState whipState) {
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

        boolean forceWhip = !crossguard && modifiers.contains(BladeModifierCrystal.FORCE_WHIP);

        if (modifiers.contains(BladeModifierCrystal.CRACKED)) {
            renderCrackedInnerVariants(poseStack, consumer, modifiers, bladeLength, crossguard, coreRed, coreGreen, coreBlue, whipState);
        }

        poseStack.pushPose();
        if (forceWhip) {
            applyInnerCrystalTransforms(poseStack, modifiers, crossguard, true);
            float whipTick = whipState.tick();
            float whipAttackSwing = whipState.attackSwing();
            float whipWidth = innerWhipWidthScale(modifiers);
            emitCurvedBladeBody(poseStack, consumer, bladeLength, coreRed, coreGreen, coreBlue, 1.0F, false, whipWidth, whipTick, whipAttackSwing);
            emitCurvedBladeTip(poseStack, consumer, bladeLength, TIP_SIZE * whipWidth, TIP_LENGTH, coreRed, coreGreen, coreBlue, 1.0F, whipTick, whipAttackSwing);
        } else {
            applyInnerCrystalTransforms(poseStack, modifiers, crossguard, false);
            if (modifiers.contains(BladeModifierCrystal.FINE_CUT)) {
                emitFineCutInnerBlade(poseStack, consumer, bladeLength, coreRed, coreGreen, coreBlue, 1.0F);
            } else {
                emitBladeBody(poseStack, consumer, bladeLength, coreRed, coreGreen, coreBlue, 1.0F);
                poseStack.translate(0.0F, -bladeLength, 0.0F);
                emitLegacyTip(poseStack, consumer, TIP_SIZE, TIP_LENGTH, coreRed, coreGreen, coreBlue, 1.0F);
            }
        }
        poseStack.popPose();
    }

    private static void applyInnerCrystalTransforms(PoseStack poseStack,
                                                    EnumSet<BladeModifierCrystal> modifiers,
                                                    boolean crossguard,
                                                    boolean forceWhip) {
        if (crossguard && modifiers.contains(BladeModifierCrystal.FINE_CUT)) {
            poseStack.scale(1.0F, 1.2F, 1.0F);
        }

        /*
         * COMPRESSED should make a force-whip thinner, not squash the procedural
         * whip curve itself. Scaling the whole pose on X/Z moves every curved
         * segment inward while the outer glow still follows the original curve,
         * causing the core and glow to visibly separate.
         */
        if (modifiers.contains(BladeModifierCrystal.COMPRESSED) && !forceWhip) {
            poseStack.scale(0.6F, 1.0F, 0.6F);
        }

        if (modifiers.contains(BladeModifierCrystal.FINE_CUT)) {
            poseStack.scale(0.55F, 1.0F, 1.10F);
        }
    }

    private static float innerWhipWidthScale(EnumSet<BladeModifierCrystal> modifiers) {
        float width = 0.58F;

        if (modifiers.contains(BladeModifierCrystal.COMPRESSED)) {
            width *= 0.60F;
        }

        return width;
    }

    private static void renderCrackedInnerVariants(PoseStack poseStack,
                                                   VertexConsumer consumer,
                                                   EnumSet<BladeModifierCrystal> modifiers,
                                                   float bladeLength,
                                                   boolean crossguard,
                                                   float red,
                                                   float green,
                                                   float blue,
                                                   WhipRenderState whipState) {
        Minecraft minecraft = Minecraft.getInstance();
        int ticks = minecraft.player != null ? minecraft.player.tickCount : 0;
        Random random = new Random((ticks % 100L) * 1000L);
        float whipTick = whipState.tick();
        float whipAttackSwing = whipState.attackSwing();

        for (int i = 0; i < 3; ++i) {
            poseStack.pushPose();
            poseStack.translate((random.nextFloat() - 0.5F) / 60.0F, 0.0F, (random.nextFloat() - 0.5F) / 60.0F);
            boolean forceWhip = !crossguard && modifiers.contains(BladeModifierCrystal.FORCE_WHIP);
            applyInnerCrystalTransforms(poseStack, modifiers, crossguard, forceWhip);
            if (forceWhip) {
                emitCurvedBladeBody(poseStack, consumer, bladeLength, red, green, blue, 0.9F, false, innerWhipWidthScale(modifiers), whipTick, whipAttackSwing);
            } else {
                emitBladeBody(poseStack, consumer, bladeLength, red, green, blue, 0.9F);
            }
            poseStack.popPose();
        }
    }

    private static void emitCurvedBladeBody(PoseStack poseStack,
                                            VertexConsumer consumer,
                                            float bladeLength,
                                            float red,
                                            float green,
                                            float blue,
                                            float alpha,
                                            boolean outerGlow,
                                            float widthScale,
                                            float whipTick,
                                            float whipAttackSwing) {
        Matrix4f pose = poseStack.last().pose();
        int segments = FORCE_WHIP_SEGMENTS;
        float glowScale = (outerGlow ? 1.30F : 1.0F) * widthScale;

        for (int i = 0; i < segments; ++i) {
            float t0 = (float) i / (float) segments;
            float t1 = (float) (i + 1) / (float) segments;

            Vec3 p0 = forceWhipPoint(bladeLength, t0, whipTick, whipAttackSwing);
            Vec3 p1 = forceWhipPoint(bladeLength, t1, whipTick, whipAttackSwing);

            float taper = 1.0F - t0 * 0.40F;
            float half0 = BODY_HALF * glowScale * taper;
            float half1 = BODY_HALF * glowScale * (1.0F - t1 * 0.40F);

            if (outerGlow) {
                emitSegmentPrism(consumer, pose, p0, p1, half0, half1, red, green, blue, alpha);
            } else {
                emitSegmentCylinder(consumer, pose, p0, p1, half0, half1, BLADE_CORE_SIDES, red, green, blue, alpha);
            }
        }
    }

    private static void emitCurvedBladeTip(PoseStack poseStack,
                                           VertexConsumer consumer,
                                           float bladeLength,
                                           float half,
                                           float tipLength,
                                           float red,
                                           float green,
                                           float blue,
                                           float alpha,
                                           float whipTick,
                                           float whipAttackSwing) {
        Matrix4f pose = poseStack.last().pose();
        Vec3 tipCenter = forceWhipPoint(bladeLength, 1.0F, whipTick, whipAttackSwing);
        Vec3 prevCenter = forceWhipPoint(bladeLength, 0.94F, whipTick, whipAttackSwing);
        Vec3 direction = tipCenter.subtract(prevCenter).normalize();
        if (direction.lengthSqr() < 1.0E-6D) {
            direction = new Vec3(0.0D, -1.0D, 0.0D);
        }

        float tipHalf = half * 0.70F;
        float y = (float) tipCenter.y;

        float ax = (float) (tipCenter.x + direction.x * tipLength);
        float ay = (float) (tipCenter.y + direction.y * tipLength);
        float az = (float) (tipCenter.z + direction.z * tipLength);

        for (int i = 0; i < BLADE_CORE_SIDES; ++i) {
            double a0 = (Math.PI * 2.0D * i) / BLADE_CORE_SIDES;
            double a1 = (Math.PI * 2.0D * (i + 1)) / BLADE_CORE_SIDES;

            float x0 = (float) (tipCenter.x + Math.cos(a0) * tipHalf);
            float z0 = (float) (tipCenter.z + Math.sin(a0) * tipHalf);
            float x1 = (float) (tipCenter.x + Math.cos(a1) * tipHalf);
            float z1 = (float) (tipCenter.z + Math.sin(a1) * tipHalf);

            emitQuad(consumer, pose,
                    x0, y, z0,
                    x1, y, z1,
                    ax, ay, az,
                    ax, ay, az,
                    red, green, blue, alpha);
        }
    }

    private static WhipRenderState captureWhipState() {
        Minecraft minecraft = Minecraft.getInstance();
        float partialTick = minecraft.getFrameTime();

        if (minecraft.player == null) {
            return WhipRenderState.NONE;
        }

        return new WhipRenderState(
                minecraft.player.tickCount + partialTick,
                minecraft.player.getAttackAnim(partialTick)
        );
    }

    private record WhipRenderState(float tick, float attackSwing) {
        private static final WhipRenderState NONE = new WhipRenderState(0.0F, 0.0F);
    }

    private static Vec3 forceWhipPoint(float bladeLength, float t, float tick, float attackSwing) {

        // Keep the emitter end locked in place, then let the flexible plasma
        // lash pick up motion down the length of the whip. This preserves the
        // hilt connection while bringing back the dramatic swing snap.
        float flexT = Mth.clamp((t - FORCE_WHIP_BASE_ANCHOR) / (1.0F - FORCE_WHIP_BASE_ANCHOR), 0.0F, 1.0F);
        float baseBlend = Mth.clamp(t / FORCE_WHIP_BASE_ANCHOR, 0.0F, 1.0F);
        baseBlend = baseBlend * baseBlend * (3.0F - 2.0F * baseBlend);

        float rootFlex = (float) Math.pow(flexT, 0.38D);
        float midFlex = Mth.sin(flexT * Mth.PI);
        float tailFlex = (float) Math.pow(flexT, 0.72D);
        float tipFlex = (float) Math.pow(flexT, 1.55D);

        float swingEnvelope = Mth.sin(attackSwing * Mth.PI);
        float swingDir = attackSwing < 0.50F ? 1.0F : -1.0F;

        // Traveling wave: the handle moves first, the midsection follows,
        // then the tip cracks outward. This is what gives the swing the
        // visible whip-like throw instead of a small shake.
        float forwardWave = Mth.clamp(attackSwing * 1.95F - flexT * 1.03F + 0.24F, 0.0F, 1.0F);
        float snapWave = Mth.sin(forwardWave * Mth.PI);
        float returnWave = Mth.clamp(attackSwing * 1.72F - flexT * 0.92F - 0.20F, 0.0F, 1.0F);
        float recoilWave = Mth.sin(returnWave * Mth.PI);
        float crackWave = Mth.sin(Mth.clamp((attackSwing - 0.34F) / 0.42F, 0.0F, 1.0F) * Mth.PI);
        float travelSnap = FORCE_WHIP_SWING_SNAP * swingEnvelope * tipFlex;

        float idleWaveA = Mth.sin(flexT * 5.65F + tick * 0.20F);
        float idleWaveB = Mth.sin(flexT * 12.40F - tick * 0.32F);
        float idleWaveC = Mth.cos(flexT * 7.20F + tick * 0.16F);

        float lateral = FORCE_WHIP_CURVE_X * tailFlex;
        lateral += idleWaveA * FORCE_WHIP_TRAIL * 0.42F * rootFlex;
        lateral += idleWaveB * FORCE_WHIP_TRAIL * 0.20F * rootFlex;
        lateral += swingDir * travelSnap * snapWave * (0.30F + 0.70F * tipFlex);
        lateral -= swingDir * travelSnap * 0.70F * recoilWave * (0.08F + 0.92F * tipFlex);
        lateral += swingDir * FORCE_WHIP_SWING_LAG * swingEnvelope * midFlex * rootFlex * 0.30F;
        lateral += swingDir * FORCE_WHIP_SWING_LAG * crackWave * tipFlex * 0.42F;
        lateral *= baseBlend;

        float depth = FORCE_WHIP_CURVE_Z * tailFlex;
        depth += FORCE_WHIP_DROOP * 0.70F * rootFlex;
        depth += idleWaveC * FORCE_WHIP_CURVE_Z * 0.34F * rootFlex;
        depth += Mth.sin(flexT * 10.35F - tick * 0.24F) * FORCE_WHIP_CURVE_Z * 0.20F * rootFlex;
        depth += travelSnap * 0.85F * snapWave * (0.18F + 0.82F * tipFlex);
        depth -= travelSnap * 0.52F * recoilWave * tipFlex;
        depth += FORCE_WHIP_SWING_LAG * crackWave * tipFlex * 0.24F;
        depth *= baseBlend;

        // Sag the lash while idle, then let the tip extend during the snap.
        // This makes swings read as a thrown whip, not a rigid blade wobble.
        float sagCompression = FORCE_WHIP_DROOP * 0.42F * rootFlex * (0.16F + 0.84F * flexT);
        float swingBow = FORCE_WHIP_DROOP * 0.62F * bladeLength * swingEnvelope * midFlex * rootFlex;
        float tipExtension = FORCE_WHIP_DROOP * 0.52F * bladeLength * snapWave * tipFlex;
        float crackExtension = FORCE_WHIP_DROOP * 0.24F * bladeLength * crackWave * tipFlex;

        float extension = t - sagCompression;
        float y = -bladeLength * extension;
        y += Mth.sin(flexT * 3.85F + tick * 0.10F) * FORCE_WHIP_DROOP * 0.13F * bladeLength * rootFlex * baseBlend;
        y += swingBow * baseBlend;
        y -= tipExtension * baseBlend;
        y -= crackExtension * baseBlend;

        return new Vec3(lateral, y, depth);
    }

    private static void emitSegmentPrism(VertexConsumer consumer,
                                         Matrix4f pose,
                                         Vec3 start,
                                         Vec3 end,
                                         float startHalf,
                                         float endHalf,
                                         float red,
                                         float green,
                                         float blue,
                                         float alpha) {
        float sxMin = (float) start.x - startHalf;
        float sxMax = (float) start.x + startHalf;
        float szMin = (float) start.z - startHalf;
        float szMax = (float) start.z + startHalf;
        float sy = (float) start.y;

        float exMin = (float) end.x - endHalf;
        float exMax = (float) end.x + endHalf;
        float ezMin = (float) end.z - endHalf;
        float ezMax = (float) end.z + endHalf;
        float ey = (float) end.y;

        emitQuad(consumer, pose,
                sxMin, sy, szMax,
                sxMax, sy, szMax,
                exMax, ey, ezMax,
                exMin, ey, ezMax,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                sxMax, sy, szMin,
                sxMin, sy, szMin,
                exMin, ey, ezMin,
                exMax, ey, ezMin,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                sxMin, sy, szMin,
                sxMin, sy, szMax,
                exMin, ey, ezMax,
                exMin, ey, ezMin,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                sxMax, sy, szMax,
                sxMax, sy, szMin,
                exMax, ey, ezMin,
                exMax, ey, ezMax,
                red, green, blue, alpha);
    }

    private static void emitFineCutInnerBlade(PoseStack poseStack,
                                             VertexConsumer consumer,
                                             float bladeLength,
                                             float red,
                                             float green,
                                             float blue,
                                             float alpha) {
        /*
         * Advanced Lightsabers did not draw Fine Cut as a normal rounded/capped
         * blade.  It adds a flat faceted cutting wedge along the front and a
         * chisel-like tip, then draws the straight core without the rounded cone.
         * Porting that geometry fixes the "wrong" oval/rounded Fine Cut look.
         */
        Matrix4f pose = poseStack.last().pose();
        float f = BODY_HALF * 2.0F;
        float length = bladeLength * 0.70F;
        float edge = f * 1.5F;
        float edgeAngle = -f * 1.5F;
        float length1 = bladeLength * 0.30F;
        float edge1 = f / 2.0F;
        float tip = f * 1.5F;

        emitQuad(consumer, pose,
                -f / 2.0F, -length, f / 2.0F,
                0.0F, -length, edge,
                0.0F, edgeAngle, edge,
                -f / 2.0F, -f, f / 2.0F,
                red, green, blue, alpha);
        emitQuad(consumer, pose,
                f / 2.0F, -length, f / 2.0F,
                0.0F, -length, edge,
                0.0F, edgeAngle, edge,
                f / 2.0F, -f, f / 2.0F,
                red, green, blue, alpha);
        emitQuad(consumer, pose,
                f / 2.0F, -f, f / 2.0F,
                0.0F, edgeAngle, edge,
                0.0F, edgeAngle, edge,
                -f / 2.0F, -f, f / 2.0F,
                red, green, blue, alpha);
        emitQuad(consumer, pose,
                -f / 2.0F, -length, f / 2.0F,
                -f / 2.0F, -length1 - length, edge1,
                0.0F, -length1 - length, edge1,
                0.0F, -length, edge,
                red, green, blue, alpha);
        emitQuad(consumer, pose,
                f / 2.0F, -length, f / 2.0F,
                f / 2.0F, -length1 - length, edge1,
                0.0F, -length1 - length, edge1,
                0.0F, -length, edge,
                red, green, blue, alpha);

        emitQuad(consumer, pose,
                -f / 2.0F, -bladeLength, f / 2.0F,
                0.0F, -tip - bladeLength, -f / 2.0F,
                0.0F, -tip - bladeLength, -f / 2.0F,
                -f / 2.0F, -bladeLength, -f / 2.0F,
                red, green, blue, alpha);
        emitQuad(consumer, pose,
                f / 2.0F, -bladeLength, f / 2.0F,
                0.0F, -tip - bladeLength, -f / 2.0F,
                0.0F, -tip - bladeLength, -f / 2.0F,
                f / 2.0F, -bladeLength, -f / 2.0F,
                red, green, blue, alpha);
        emitQuad(consumer, pose,
                -f / 2.0F, -bladeLength, -f / 2.0F,
                0.0F, -tip - bladeLength, -f / 2.0F,
                0.0F, -tip - bladeLength, -f / 2.0F,
                f / 2.0F, -bladeLength, -f / 2.0F,
                red, green, blue, alpha);
        emitQuad(consumer, pose,
                -f / 2.0F, -bladeLength, f / 2.0F,
                0.0F, -tip - bladeLength, -f / 2.0F,
                0.0F, -tip - bladeLength, -f / 2.0F,
                f / 2.0F, -bladeLength, f / 2.0F,
                red, green, blue, alpha);

        emitBladeBody(poseStack, consumer, bladeLength, red, green, blue, alpha);
    }

    private static void emitBladeBody(PoseStack poseStack,
                                      VertexConsumer consumer,
                                      float bladeLength,
                                      float red,
                                      float green,
                                      float blue,
                                      float alpha) {
        Matrix4f pose = poseStack.last().pose();
        Vec3 start = new Vec3(0.0D, 0.0D, 0.0D);
        Vec3 end = new Vec3(0.0D, -bladeLength, 0.0D);
        emitSegmentCylinder(consumer, pose, start, end, BODY_HALF, BODY_HALF, BLADE_CORE_SIDES, red, green, blue, alpha);
    }

    private static void emitSegmentCylinder(VertexConsumer consumer,
                                            Matrix4f pose,
                                            Vec3 start,
                                            Vec3 end,
                                            float startHalf,
                                            float endHalf,
                                            int sides,
                                            float red,
                                            float green,
                                            float blue,
                                            float alpha) {
        for (int i = 0; i < sides; ++i) {
            double a0 = (Math.PI * 2.0D * i) / sides;
            double a1 = (Math.PI * 2.0D * (i + 1)) / sides;

            float sx0 = (float) (start.x + Math.cos(a0) * startHalf);
            float sz0 = (float) (start.z + Math.sin(a0) * startHalf);
            float sx1 = (float) (start.x + Math.cos(a1) * startHalf);
            float sz1 = (float) (start.z + Math.sin(a1) * startHalf);
            float ex0 = (float) (end.x + Math.cos(a0) * endHalf);
            float ez0 = (float) (end.z + Math.sin(a0) * endHalf);
            float ex1 = (float) (end.x + Math.cos(a1) * endHalf);
            float ez1 = (float) (end.z + Math.sin(a1) * endHalf);

            emitQuad(consumer, pose,
                    sx0, (float) start.y, sz0,
                    sx1, (float) start.y, sz1,
                    ex1, (float) end.y, ez1,
                    ex0, (float) end.y, ez0,
                    red, green, blue, alpha);
        }
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
        float ax = 0.0F;
        float ay = -tipLength;
        float az = 0.0F;

        for (int i = 0; i < BLADE_CORE_SIDES; ++i) {
            double a0 = (Math.PI * 2.0D * i) / BLADE_CORE_SIDES;
            double a1 = (Math.PI * 2.0D * (i + 1)) / BLADE_CORE_SIDES;

            float x0 = (float) (Math.cos(a0) * half);
            float z0 = (float) (Math.sin(a0) * half);
            float x1 = (float) (Math.cos(a1) * half);
            float z1 = (float) (Math.sin(a1) * half);

            emitQuad(consumer, pose,
                    x0, 0.0F, z0,
                    x1, 0.0F, z1,
                    ax, ay, az,
                    ax, ay, az,
                    red, green, blue, alpha);
        }
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
