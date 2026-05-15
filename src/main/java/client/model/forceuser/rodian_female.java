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

public class rodian_female<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(galaxyunderchaos.MODID, "rodian_female"), "main");
	private final ModelPart Root;
	private final ModelPart Torso;
	private final ModelPart Head;
	private final ModelPart BackAppendageTop;
	private final ModelPart BackAppendageMiddle;
	private final ModelPart BackAppendageBottom;
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

	public rodian_female(ModelPart root) {
		this.Root = root.getChild("Root");
		this.Torso = this.Root.getChild("Torso");
		this.Head = this.Torso.getChild("Head");
		this.BackAppendageTop = this.Head.getChild("BackAppendageTop");
		this.BackAppendageMiddle = this.BackAppendageTop.getChild("BackAppendageMiddle");
		this.BackAppendageBottom = this.BackAppendageMiddle.getChild("BackAppendageBottom");
		this.leftFrontAppendageBottom = this.Head.getChild("leftFrontAppendageBottom");
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

		PartDefinition Head = Torso.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(42, 83).addBox(4.6881F, -8.0204F, -5.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.001F))
		.texOffs(6, 73).addBox(4.6881F, -1.0204F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.001F))
		.texOffs(48, 83).addBox(4.6881F, -8.0204F, 3.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.001F))
		.texOffs(0, 0).addBox(-3.3119F, -8.0204F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.001F))
		.texOffs(0, 79).addBox(-5.3119F, -10.0204F, -2.0F, 10.0F, 2.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offset(-0.6881F, -5.9796F, 0.0F));

		PartDefinition cube_r1 = Head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 79).addBox(-2.0F, -1.0F, -2.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-4.2484F, -3.0685F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition BackAppendageTop = Head.addOrReplaceChild("BackAppendageTop", CubeListBuilder.create(), PartPose.offset(-4.2481F, -7.4138F, 0.0F));

		PartDefinition BackAppendageMiddle = BackAppendageTop.addOrReplaceChild("BackAppendageMiddle", CubeListBuilder.create(), PartPose.offset(-0.8165F, 7.9753F, 0.0F));

		PartDefinition BackAppendageBottom = BackAppendageMiddle.addOrReplaceChild("BackAppendageBottom", CubeListBuilder.create(), PartPose.offset(-0.4448F, 4.984F, 0.0F));

		PartDefinition leftFrontAppendageBottom = Head.addOrReplaceChild("leftFrontAppendageBottom", CubeListBuilder.create(), PartPose.offset(4.0895F, -0.0839F, 6.6604F));

		PartDefinition leftBackAppendage = Head.addOrReplaceChild("leftBackAppendage", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.2106F, -7.0016F, -1.4651F, -1.4935F, 1.6172F, -1.0614F));

		PartDefinition leftBackAppendageBottom = leftBackAppendage.addOrReplaceChild("leftBackAppendageBottom", CubeListBuilder.create(), PartPose.offsetAndRotation(3.8001F, 7.2178F, -1.8954F, 0.0F, 0.0F, -0.2618F));

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