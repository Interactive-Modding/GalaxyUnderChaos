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

public class zabrak_male<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(galaxyunderchaos.MODID, "zabrak_male"), "main");
	private final ModelPart Root;
	private final ModelPart Torso;
	private final ModelPart Head;
	private final ModelPart leftFrontAppendage;
	private final ModelPart leftFrontAppendageBottom;
	private final ModelPart leftBackAppendage;
	private final ModelPart leftBackAppendageBottom;
	private final ModelPart leftArm;
	private final ModelPart leftArmRobe;
	private final ModelPart leftLeg;
	private final ModelPart leftLegRobe;
	private final ModelPart rightLeg;
	private final ModelPart rightLegRobe;
	private final ModelPart rightArm;
	private final ModelPart rightArmRobe;

	public zabrak_male(ModelPart root) {
		this.Root = root.getChild("Root");
		this.Torso = this.Root.getChild("Torso");
		this.Head = this.Torso.getChild("Head");
		this.leftFrontAppendage = this.Head.getChild("leftFrontAppendage");
		this.leftFrontAppendageBottom = this.leftFrontAppendage.getChild("leftFrontAppendageBottom");
		this.leftBackAppendage = this.Head.getChild("leftBackAppendage");
		this.leftBackAppendageBottom = this.leftBackAppendage.getChild("leftBackAppendageBottom");
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

		PartDefinition Head = Torso.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.3119F, -8.0204F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.001F)), PartPose.offset(-0.6881F, -5.9796F, 0.0F));

		PartDefinition cube_r1 = Head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 36).addBox(-0.75F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.1881F, -4.5204F, -3.5F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r2 = Head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 36).addBox(-0.75F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(1.25F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.1881F, -7.5204F, -3.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r3 = Head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 36).addBox(-0.75F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(-0.75F, -2.0F, 1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(-0.75F, -2.0F, 3.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(-0.75F, -2.0F, 5.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-1.8119F, -7.5204F, -3.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cube_r4 = Head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(20, 36).addBox(-0.75F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.1881F, -4.5204F, 3.5F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r5 = Head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(20, 36).addBox(-0.75F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(1.25F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.1881F, -7.5204F, 3.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r6 = Head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(20, 36).addBox(-0.75F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(-0.75F, -2.0F, 5.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(-0.75F, -2.0F, 5.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(4.1881F, -5.5204F, -3.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r7 = Head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(20, 36).addBox(-0.75F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(-0.75F, -2.0F, 1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(-0.75F, -2.0F, 3.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F))
		.texOffs(20, 36).addBox(-0.75F, -2.0F, 5.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(4.1881F, -7.5204F, -3.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r8 = Head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(45, 54).addBox(-0.75F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(4.9381F, -2.2704F, 0.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition leftFrontAppendage = Head.addOrReplaceChild("leftFrontAppendage", CubeListBuilder.create(), PartPose.offset(0.2894F, -7.3016F, 3.7651F));

		PartDefinition leftFrontAppendageBottom = leftFrontAppendage.addOrReplaceChild("leftFrontAppendageBottom", CubeListBuilder.create(), PartPose.offset(3.8001F, 7.2178F, 1.8953F));

		PartDefinition leftBackAppendage = Head.addOrReplaceChild("leftBackAppendage", CubeListBuilder.create(), PartPose.offset(0.2894F, -7.3016F, -3.7651F));

		PartDefinition leftBackAppendageBottom = leftBackAppendage.addOrReplaceChild("leftBackAppendageBottom", CubeListBuilder.create(), PartPose.offset(3.8001F, 7.2178F, -1.8953F));

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