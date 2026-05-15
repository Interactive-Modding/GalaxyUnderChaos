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

package client.renderer.lightsaber.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public final class LegacyRenderSession {
    private static final ThreadLocal<LegacyRenderSession> CURRENT = new ThreadLocal<>();

    /**
     * Advanced Lightsabers' generated ModelRenderer classes already encode their
     * intended depth relationships through cube size, glScaled(...) wrapper blocks,
     * and render order.  Do not add a cumulative per-box normal offset here: that
     * made later inner/detail boxes punch in front of earlier outer shells and was
     * the source of the visible hilt Z-order corruption.
     */
    private static final float MODEL_PART_DEPTH_BIAS = 0.0F;

    private final PoseStack poseStack;
    private final VertexConsumer consumer;
    private final int packedLight;
    private final int packedOverlay;
    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;

    private LegacyRenderSession(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay,
                                float red, float green, float blue, float alpha) {
        this.poseStack = poseStack;
        this.consumer = consumer;
        this.packedLight = packedLight;
        this.packedOverlay = packedOverlay;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public static void begin(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay) {
        begin(poseStack, consumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void begin(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay,
                             float red, float green, float blue, float alpha) {
        CURRENT.set(new LegacyRenderSession(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha));
    }

    public static void end() {
        CURRENT.remove();
    }

    public static LegacyRenderSession get() {
        return CURRENT.get();
    }

    public PoseStack poseStack() {
        return poseStack;
    }

    public VertexConsumer consumer() {
        return consumer;
    }

    public int packedLight() {
        return packedLight;
    }

    public int packedOverlay() {
        return packedOverlay;
    }

    public float red() {
        return red;
    }

    public float green() {
        return green;
    }

    public float blue() {
        return blue;
    }

    public float alpha() {
        return alpha;
    }

    public float nextModelPartDepthBias() {
        return MODEL_PART_DEPTH_BIAS;
    }

}
