package client.renderer.lightsaber.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public final class LegacyRenderSession {
    private static final ThreadLocal<LegacyRenderSession> CURRENT = new ThreadLocal<>();

    private final PoseStack poseStack;
    private final VertexConsumer consumer;
    private final int packedLight;
    private final int packedOverlay;

    private LegacyRenderSession(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay) {
        this.poseStack = poseStack;
        this.consumer = consumer;
        this.packedLight = packedLight;
        this.packedOverlay = packedOverlay;
    }

    public static void begin(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay) {
        CURRENT.set(new LegacyRenderSession(poseStack, consumer, packedLight, packedOverlay));
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
}
