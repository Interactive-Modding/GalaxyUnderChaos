package client.model.forceuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import server.galaxyunderchaos.galaxyunderchaos;

public class jedi_robes<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(galaxyunderchaos.MODID, "jedi_robes"), "main");
	private final ModelPart Root;
	private final ModelPart Torso;
	private final ModelPart leftArm;
	private final ModelPart leftArmRobe;
	private final ModelPart leftLeg;
	private final ModelPart leftLegRobe;
	private final ModelPart rightLeg;
	private final ModelPart rightLegRobe;
	private final ModelPart rightArm;
	private final ModelPart rightArmRobe;

	public jedi_robes(ModelPart root) {
		this.Root = root.getChild("Root");
		this.Torso = this.Root.getChild("Torso");
		this.leftArm = this.Torso.getChild("leftArm");
		this.leftArmRobe = this.leftArm.getChild("leftArmRobe");
		this.leftLeg = this.Torso.getChild("leftLeg");
		this.leftLegRobe = this.leftLeg.getChild("leftLegRobe");
		this.rightLeg = this.Torso.getChild("rightLeg");
		this.rightLegRobe = this.rightLeg.getChild("rightLegRobe");
		this.rightArm = this.Torso.getChild("rightArm");
		this.rightArmRobe = this.rightArm.getChild("rightArmRobe");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Root = partdefinition.addOrReplaceChild("Root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Torso = Root.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -6.0F, -4.0F, 4.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition leftArm = Torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(36, 44).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 4.0F));

		PartDefinition leftArmRobe = leftArm.addOrReplaceChild("leftArmRobe", CubeListBuilder.create().texOffs(56, 10).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.5F));

		PartDefinition leftLeg = Torso.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(20, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 2.0F));

		PartDefinition leftLegRobe = leftLeg.addOrReplaceChild("leftLegRobe", CubeListBuilder.create().texOffs(24, 28).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 0.0F, 0.5F));

		PartDefinition rightLeg = Torso.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(44, 28).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));

		PartDefinition rightLegRobe = rightLeg.addOrReplaceChild("rightLegRobe", CubeListBuilder.create().texOffs(32, 0).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 0.0F, -0.5F));

		PartDefinition rightArm = Torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(50, 44).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -4.0F));

		PartDefinition rightArmRobe = rightArm.addOrReplaceChild("rightArmRobe", CubeListBuilder.create().texOffs(36, 59).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, -1.5F));

		return LayerDefinition.create(meshdefinition, 96, 96);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		ForceUserModelAnimator.animate(this, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}