package client.renderer.forceuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.galaxyunderchaos;

public class ForceUserRenderer extends MobRenderer<ForceUserEntity, ForceUserLayeredModel> {
    private static final float GHOST_R = 112.0F / 255.0F;
    private static final float GHOST_G = 170.0F / 255.0F;
    private static final float GHOST_B = 255.0F / 255.0F;
    private static final float GHOST_A = 0.48F;

    public ForceUserRenderer(EntityRendererProvider.Context context) {
        super(context, new ForceUserLayeredModel(context, ForceUserModelLayers.bodyFactories()), 0.45F);
        this.addLayer(new ForceUserRobeLayer(this, context));
        this.addLayer(new ForceUserTempleGuardArmorLayer(this, context));
        this.addLayer(new ForceUserEyeLayer(this));
        this.addLayer(new ForceUserLightsaberLayer(this));
    }

    @Override
    public void render(ForceUserEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float scale = entity.getRenderScale();
        if (scale != 1.0F) {
            poseStack.scale(scale, scale, scale);
        }

        if (entity.isGhost()) {
            ResourceLocation bodyTexture = getTextureLocation(entity);
            MultiBufferSource ghostBuffer = renderType -> {
                String key = renderType.toString();
                if (key.contains(bodyTexture.toString())) {
                    VertexConsumer base = buffer.getBuffer(RenderType.entityTranslucent(bodyTexture));
                    return new TintingVertexConsumer(base, GHOST_R, GHOST_G, GHOST_B, GHOST_A);
                }
                return buffer.getBuffer(renderType);
            };
            super.render(entity, entityYaw, partialTicks, poseStack, ghostBuffer, LightTexture.pack(15, 15));
        } else {
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ForceUserEntity entity) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/force_user/" + entity.getBodyTextureId() + ".png");
    }

    private static final class TintingVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float tr;
        private final float tg;
        private final float tb;
        private final float ta;

        private TintingVertexConsumer(VertexConsumer delegate, float tr, float tg, float tb, float ta) {
            this.delegate = delegate;
            this.tr = tr;
            this.tg = tg;
            this.tb = tb;
            this.ta = ta;
        }

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            return delegate.vertex(x, y, z);
        }

        @Override
        public VertexConsumer color(int r, int g, int b, int a) {
            int nr = Math.min(255, Math.round(r * tr));
            int ng = Math.min(255, Math.round(g * tg));
            int nb = Math.min(255, Math.round(b * tb));
            int na = Math.min(255, Math.round(a * ta));
            return delegate.color(nr, ng, nb, na);
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            return delegate.uv(u, v);
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            return delegate.overlayCoords(u, v);
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            return delegate.uv2(u, v);
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return delegate.normal(x, y, z);
        }

        @Override
        public void endVertex() {
            delegate.endVertex();
        }

        @Override
        public void defaultColor(int r, int g, int b, int a) {
            // Do not leak default colors onto the shared buffer. Each vertex receives its own tint.
        }

        @Override
        public void unsetDefaultColor() {
            // Do not leak default colors onto the shared buffer.
        }
    }
}
