package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import server.galaxyunderchaos.entity.ForcePushWaveEntity;
import server.galaxyunderchaos.galaxyunderchaos;

public class ForcePushWaveRenderer extends EntityRenderer<ForcePushWaveEntity> {
    public ForcePushWaveRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ForcePushWaveEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float radius = entity.getCurrentRadius(partialTick);
        float life = Math.max(0.0F, 1.0F - (entity.tickCount + partialTick) / Math.max(1.0F, entity.getLifeTicks()));
        int alpha = Math.max(24, (int) (life * 180.0F));
        VertexConsumer consumer = buffer.getBuffer(RenderType.lightning());
        Matrix4f pose = poseStack.last().pose();
        int segments = 48;
        float width = 0.18F;
        for (int i = 0; i < segments; i++) {
            double a0 = (Math.PI * 2.0D * i) / segments;
            double a1 = (Math.PI * 2.0D * (i + 1)) / segments;
            float x0 = (float) (Math.cos(a0) * radius);
            float z0 = (float) (Math.sin(a0) * radius);
            float x1 = (float) (Math.cos(a1) * radius);
            float z1 = (float) (Math.sin(a1) * radius);
            float x0i = (float) (Math.cos(a0) * Math.max(0.0F, radius - width));
            float z0i = (float) (Math.sin(a0) * Math.max(0.0F, radius - width));
            float x1i = (float) (Math.cos(a1) * Math.max(0.0F, radius - width));
            float z1i = (float) (Math.sin(a1) * Math.max(0.0F, radius - width));
            quad(consumer, pose, x0, 0.08F, z0, x1, 0.08F, z1, x1i, 0.02F, z1i, x0i, 0.02F, z0i, 120, 220, 255, alpha);
        }
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private static void quad(VertexConsumer consumer, Matrix4f pose,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float x3, float y3, float z3,
                             float x4, float y4, float z4,
                             int r, int g, int b, int a) {
        vertex(consumer, pose, x1, y1, z1, r, g, b, a);
        vertex(consumer, pose, x2, y2, z2, r, g, b, a);
        vertex(consumer, pose, x3, y3, z3, r, g, b, a);
        vertex(consumer, pose, x4, y4, z4, r, g, b, a);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, float x, float y, float z, int r, int g, int b, int a) {
        consumer.vertex(pose, x, y, z).color(r, g, b, a).endVertex();
    }

    @Override
    public boolean shouldRender(ForcePushWaveEntity entity, net.minecraft.client.renderer.culling.Frustum frustum, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(ForcePushWaveEntity entity) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/gui/icons.png");
    }
}
