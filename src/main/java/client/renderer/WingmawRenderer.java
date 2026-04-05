package client.renderer;

import client.model.WingmawModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.entity.WingmawEntity;
import server.galaxyunderchaos.galaxyunderchaos;

public class WingmawRenderer extends MobRenderer<WingmawEntity, WingmawModel<WingmawEntity>> {
    public WingmawRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WingmawModel<>(pContext.bakeLayer(WingmawModel.LAYER_LOCATION)), 0.85f);
    }

    @Override
    public ResourceLocation getTextureLocation(WingmawEntity pEntity) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/wingmaw/wingmaw.png");
    }

    @Override
    public void render(WingmawEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            pPoseStack.scale(1f, 1f, 1f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

}
