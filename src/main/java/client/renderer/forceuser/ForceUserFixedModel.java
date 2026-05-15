package client.renderer.forceuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;

import java.util.Map;
import java.util.function.Function;

public class ForceUserFixedModel extends EntityModel<ForceUserEntity> {
    private final EntityModel<ForceUserEntity> model;

    public ForceUserFixedModel(EntityRendererProvider.Context context, String modelId, Map<String, Function<ModelPart, EntityModel<ForceUserEntity>>> factories) {
        this.model = ForceUserModelLayers.bakeModels(context, factories).get(modelId);
    }

    @Override
    public void setupAnim(ForceUserEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (model != null) {
            model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (model != null) {
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
