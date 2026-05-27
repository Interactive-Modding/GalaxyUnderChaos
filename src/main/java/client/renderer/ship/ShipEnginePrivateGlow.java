package client.renderer.ship;

import client.renderer.lightsaber.legacy.LegacyRenderStates;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;

/**
 * Ship engine exhaust glow.
 *
 * Important: this is intentionally NOT a late RenderLevelStage private pass.
 * The previous ship engine implementation queued engine cones and replayed
 * them after the level render. Shader stacks such as Oculus can make entity
 * rendering / level stages run in extra passes, which caused the same engine
 * cone to stack or replay from the wrong captured matrix.
 *
 * This now follows the stable part of the working lightsaber glow path:
 * a normal POSITION_COLOR RenderType using the vanilla lightning shader,
 * additive blending, no cull, no lightmap, and LEQUAL depth. It renders once
 * through the entity's regular buffer instead of being replayed later.
 *
 * The glow writes color and depth in one visible pass. Do NOT add a separate
 * invisible depth-only pass here: Oculus/Iris shader stacks can treat that as
 * a broken translucent/depth prepass and make the exhaust appear see-through.
 *
 * The exhaust callers draw the small bright cone first, then the middle cone,
 * then the outer cone. That keeps the core visible while still placing a real
 * depth value in the scene so later water and translucent entity passes do not
 * blend over the engine jet when they are behind it.
 */
public final class ShipEnginePrivateGlow {
    private static final Object TYPE_LOCK = new Object();
    private static RenderType engineGlowType;

    private ShipEnginePrivateGlow() {
    }

    private static RenderType engineGlowType() {
        RenderType type = engineGlowType;
        if (type == null) {
            synchronized (TYPE_LOCK) {
                type = engineGlowType;
                if (type == null) {
                    type = createEngineGlowType();
                    engineGlowType = type;
                }
            }
        }
        return type;
    }

    private static RenderType createEngineGlowType() {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(LegacyRenderStates.rendertypeLightningShader())
                .setTransparencyState(LegacyRenderStates.additiveGlow())
                .setCullState(LegacyRenderStates.noCull())
                .setLightmapState(LegacyRenderStates.noLightmap())
                .setOverlayState(LegacyRenderStates.noOverlay())
                .setWriteMaskState(LegacyRenderStates.colorDepthWrite())
                .setDepthTestState(LegacyRenderStates.lequalDepth())
                .setOutputState(LegacyRenderStates.translucentTarget())
                .createCompositeState(false);

        return RenderType.create(
                "guc_ship_engine_glow",
                DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.TRIANGLES,
                512,
                false,
                true,
                state
        );
    }

    public static void renderCone(PoseStack poseStack,
                                  MultiBufferSource buffer,
                                  float x,
                                  float y,
                                  float z,
                                  float radius,
                                  float length,
                                  int red,
                                  int green,
                                  int blue,
                                  int alpha) {
        if (poseStack == null || buffer == null || length <= 0.0F || radius <= 0.0F || alpha <= 0) {
            return;
        }

        VertexConsumer consumer = buffer.getBuffer(engineGlowType());
        Matrix4f pose = poseStack.last().pose();
        int segments = 16;
        float tipZ = z - length;

        for (int i = 0; i < segments; i++) {
            double a0 = (Math.PI * 2.0D * i) / segments;
            double a1 = (Math.PI * 2.0D * (i + 1)) / segments;

            float x0 = x + (float) Math.cos(a0) * radius;
            float y0 = y + (float) Math.sin(a0) * radius;
            float x1 = x + (float) Math.cos(a1) * radius;
            float y1 = y + (float) Math.sin(a1) * radius;

            vertex(consumer, pose, x0, y0, z, red, green, blue, alpha);
            vertex(consumer, pose, x1, y1, z, red, green, blue, alpha);
            vertex(consumer, pose, x, y, tipZ, red, green, blue, 0);
        }
    }

    /**
     * Kept only so older call sites fail soft instead of crashing if one was
     * missed during a partial merge. New ship exhaust code uses renderCone().
     */
    public static void queueCone(Matrix4f ignoredLocalPose,
                                 float x,
                                 float y,
                                 float z,
                                 float radius,
                                 float length,
                                 int red,
                                 int green,
                                 int blue,
                                 int alpha) {
        // No-op by design. The old queued private pass caused duplicate / wrong-pass engine glow under Oculus.
    }

    public static void flush(MultiBufferSource buffer) {
        if (buffer instanceof MultiBufferSource.BufferSource source) {
            source.endBatch(engineGlowType());
        }
    }

    private static void vertex(VertexConsumer consumer,
                               Matrix4f pose,
                               float x,
                               float y,
                               float z,
                               int red,
                               int green,
                               int blue,
                               int alpha) {
        consumer.vertex(pose, x, y, z).color(red, green, blue, alpha).endVertex();
    }
}
