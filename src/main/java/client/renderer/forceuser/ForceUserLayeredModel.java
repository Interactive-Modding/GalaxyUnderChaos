package client.renderer.forceuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import client.model.forceuser.ForceUserModelAnimator;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Map;
import java.util.function.Function;

public class ForceUserLayeredModel extends EntityModel<ForceUserEntity> {
    private final Map<String, EntityModel<ForceUserEntity>> models;
    private EntityModel<ForceUserEntity> activeModel;
    private boolean hideBodyLegs;

    public ForceUserLayeredModel(EntityRendererProvider.Context context, Map<String, Function<ModelPart, EntityModel<ForceUserEntity>>> factories) {
        this.models = ForceUserModelLayers.bakeModels(context, factories);
    }

    @Override
    public void setupAnim(ForceUserEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.activeModel = models.get(entity.getBodyModelId());
        if (this.activeModel == null) {
            this.activeModel = models.get("human_male");
        }
        this.hideBodyLegs = entity.getType() == galaxyunderchaos.JEDI_TEMPLE_GUARD.get()
                || entity.getType() == galaxyunderchaos.SITH_GUARD.get();
        if (this.activeModel != null) {
            this.activeModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (activeModel == null) {
            return;
        }

        if (hideBodyLegs) {
            ForceUserModelAnimator.setLegVisibility(activeModel, false);
            activeModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            ForceUserModelAnimator.setLegVisibility(activeModel, true);
            return;
        }

        activeModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void translateToRightHand(PoseStack poseStack) {
        if (activeModel != null) {
            ForceUserModelAnimator.translateToRightHand(activeModel, poseStack);
        }
    }

    public void translateToBelt(PoseStack poseStack) {
        if (activeModel != null) {
            ForceUserModelAnimator.translateToBelt(activeModel, poseStack);
        }
    }

    public boolean usesSideAxis() {
        return activeModel != null && ForceUserModelAnimator.usesSideAxis(activeModel);
    }
}
