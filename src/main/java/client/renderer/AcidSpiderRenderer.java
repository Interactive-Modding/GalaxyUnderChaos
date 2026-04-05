package client.renderer;

import client.model.AcidSpiderModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.entity.AcidSpiderEntity;
import server.galaxyunderchaos.galaxyunderchaos;

public class AcidSpiderRenderer extends MobRenderer<AcidSpiderEntity, AcidSpiderModel<AcidSpiderEntity>> {
    public AcidSpiderRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new AcidSpiderModel<>(pContext.bakeLayer(AcidSpiderModel.LAYER_LOCATION)), 0.85f);
    }

    @Override
    public ResourceLocation getTextureLocation(AcidSpiderEntity pEntity) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/acidspider/acid_spider.png");
    }

    @Override
    public void render(AcidSpiderEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            pPoseStack.scale(2f, 2f, 2f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

}
