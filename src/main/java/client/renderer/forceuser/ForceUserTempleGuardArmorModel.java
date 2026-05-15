package client.renderer.forceuser;

import client.model.forceuser.ForceUserModelAnimator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.EquipmentSlot;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;

/**
 * Temple Guard armor overlay built on the exact same Blockbench-style hierarchy as the
 * Force-user human model: Root -> Torso -> Head/arms/legs. Do not use vanilla
 * HumanoidModel here; its player-layer hierarchy animates separately and appears too
 * small / detached on the custom ForceUserEntity models.
 */
public class ForceUserTempleGuardArmorModel extends EntityModel<ForceUserEntity> {
    private final ModelPart Root;
    private final ModelPart Torso;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart RightHand;
    private final ModelPart Head;
    private final ModelPart LeftHand;

    public ForceUserTempleGuardArmorModel(ModelPart root) {
        this.Root = root.getChild("Root");
        this.Torso = this.Root.getChild("Torso");
        this.RightLeg = this.Torso.getChild("RightLeg");
        this.LeftLeg = this.Torso.getChild("LeftLeg");
        this.RightHand = this.Torso.getChild("RightHand");
        this.Head = this.Torso.getChild("Head");
        this.LeftHand = this.Torso.getChild("LeftHand");
    }

    public static LayerDefinition createArmorLayer(float inflate) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeDeformation deformation = new CubeDeformation(inflate);

        PartDefinition Root = root.addOrReplaceChild("Root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition Torso = Root.addOrReplaceChild("Torso",
                CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(0.0F, -12.0F, 2.0F));

        Torso.addOrReplaceChild("RightLeg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(-2.0F, 0.0F, -2.0F));
        Torso.addOrReplaceChild("LeftLeg",
                CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(2.0F, 0.0F, -2.0F));
        Torso.addOrReplaceChild("RightHand",
                CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(-4.0F, -12.0F, -2.0F));
        Torso.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation),
                PartPose.offset(0.0F, -12.0F, -2.0F));
        Torso.addOrReplaceChild("LeftHand",
                CubeListBuilder.create().texOffs(40, 16).mirror().addBox(0.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(4.0F, -12.0F, -2.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(ForceUserEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        ForceUserModelAnimator.animate(this, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public void setVisibleForSlot(EquipmentSlot slot) {
        Root.getAllParts().forEach(part -> part.visible = false);
        Root.visible = true;
        Torso.visible = true;
        switch (slot) {
            case HEAD -> Head.visible = true;
            case CHEST -> {
                Torso.visible = true;
                RightHand.visible = true;
                LeftHand.visible = true;
            }
            case LEGS -> {
                Torso.visible = true;
                RightLeg.visible = true;
                LeftLeg.visible = true;
            }
            case FEET -> {
                RightLeg.visible = true;
                LeftLeg.visible = true;
            }
            default -> {
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
