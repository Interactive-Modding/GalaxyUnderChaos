package client.renderer;

import client.model.VonskrModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.entity.VonskrEntity;
import server.galaxyunderchaos.galaxyunderchaos;

public class VonskrRenderer extends MobRenderer<VonskrEntity, VonskrModel<VonskrEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/vonskr/vonskr.png");
    private static final ResourceLocation DARK_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/vonskr/dark_vonskr.png");

    public VonskrRenderer(EntityRendererProvider.Context context) {
        super(context, new VonskrModel<>(context.bakeLayer(VonskrModel.LAYER_LOCATION)), 0.55F);
    }

    @Override
    protected void scale(VonskrEntity entity, PoseStack poseStack, float partialTick) {
        float scale = 0.42F;
        if (entity.isBabyVonskr()) {
            float progress = Math.max(0.0F, Math.min(1.0F, (entity.getGrowthTicks() + partialTick) / (float) VonskrEntity.GROWTH_TICKS));
            scale *= 0.48F + progress * 0.52F;
        }
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(VonskrEntity entity) {
        return entity.getTarget() != null ? DARK_TEXTURE : TEXTURE;
    }
}
