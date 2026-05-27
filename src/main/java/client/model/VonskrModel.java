package client.model;

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
import server.galaxyunderchaos.entity.VonskrEntity;
import server.galaxyunderchaos.galaxyunderchaos;

// Made with Blockbench 5.0.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class VonskrModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(galaxyunderchaos.MODID, "vonskr"), "main");
	private final ModelPart Vornskr;
	private final ModelPart Body;
	private final ModelPart Neck;
	private final ModelPart Head;
	private final ModelPart Jaw;
	private final ModelPart bone5;
	private final ModelPart bone4;
	private final ModelPart Hips;
	private final ModelPart Tail1;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bone3;
	private final ModelPart Neck2;
	private final ModelPart LeftLeg;
	private final ModelPart LeftLeg2;
	private final ModelPart LeftLeg3;
	private final ModelPart RightLeg;
	private final ModelPart RightLeg2;
	private final ModelPart RightLeg3;
	private final ModelPart LeftBackLeg;
	private final ModelPart bone6;
	private final ModelPart RightBackLeg;
	private final ModelPart bone7;

	public VonskrModel(ModelPart root) {
		this.Vornskr = root.getChild("Vornskr");
		this.Body = this.Vornskr.getChild("Body");
		this.Neck = this.Body.getChild("Neck");
		this.Head = this.Neck.getChild("Head");
		this.Jaw = this.Head.getChild("Jaw");
		this.bone5 = this.Head.getChild("bone5");
		this.bone4 = this.Head.getChild("bone4");
		this.Hips = this.Body.getChild("Hips");
		this.Tail1 = this.Hips.getChild("Tail1");
		this.bone = this.Tail1.getChild("bone");
		this.bone2 = this.bone.getChild("bone2");
		this.bone3 = this.bone2.getChild("bone3");
		this.Neck2 = this.Body.getChild("Neck2");
		this.LeftLeg = this.Vornskr.getChild("LeftLeg");
		this.LeftLeg2 = this.LeftLeg.getChild("LeftLeg2");
		this.LeftLeg3 = this.LeftLeg2.getChild("LeftLeg3");
		this.RightLeg = this.Vornskr.getChild("RightLeg");
		this.RightLeg2 = this.RightLeg.getChild("RightLeg2");
		this.RightLeg3 = this.RightLeg2.getChild("RightLeg3");
		this.LeftBackLeg = this.Vornskr.getChild("LeftBackLeg");
		this.bone6 = this.LeftBackLeg.getChild("bone6");
		this.RightBackLeg = this.Vornskr.getChild("RightBackLeg");
		this.bone7 = this.RightBackLeg.getChild("bone7");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Vornskr = partdefinition.addOrReplaceChild("Vornskr", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition Body = Vornskr.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(40, 29).addBox(-3.0F, -0.9232F, -0.2693F, 6.0F, 6.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.6F, 0.0F, 0.0698F, 0.0F, 0.0F));

		PartDefinition cube_r1 = Body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(94, 61).addBox(0.0F, -2.0F, -12.0F, 0.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-4.0F, 0.0F, -12.0F, 8.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9232F, 12.7307F, -0.4887F, 0.0F, 0.0F));

		PartDefinition cube_r2 = Body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 110).addBox(-0.5F, -1.997F, -6.1144F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 67).addBox(-5.0F, 0.003F, -6.1144F, 9.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -10.072F, -14.0306F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r3 = Body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(36, 48).addBox(-0.5F, -9.0F, -16.0F, 0.0F, 2.0F, 17.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -7.0F, -16.0F, 11.0F, 12.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, -0.2094F, 0.0F, 0.0F));

		PartDefinition Neck = Body.addOrReplaceChild("Neck", CubeListBuilder.create(), PartPose.offset(0.0F, -4.5551F, -19.6857F));

		PartDefinition cube_r4 = Neck.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(78, 86).addBox(3.0F, 3.0F, -9.0F, 0.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 47).addBox(-3.0F, -4.0F, -9.0F, 6.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition Head = Neck.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -4.4877F, -7.9442F));

		PartDefinition cube_r5 = Head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(56, 17).addBox(-3.0F, -1.5F, -1.0F, 3.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1F, 1.9917F, -9.9027F, 0.14F, -0.0691F, -0.0097F));

		PartDefinition cube_r6 = Head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(70, 61).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.4226F, -8.5815F, -0.0349F, 0.0F, 0.0F));

		PartDefinition cube_r7 = Head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(84, 61).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.9723F, -9.7236F, 0.2967F, 0.0F, 0.0F));

		PartDefinition cube_r8 = Head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.5F, -10.0F, 0.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(52, 86).addBox(-4.5F, -3.5F, -4.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.6F, 0.0F, 0.1396F, 0.0F, 0.0F));

		PartDefinition cube_r9 = Head.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(40, 98).addBox(0.0F, -1.5F, -1.0F, 3.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1F, 1.9917F, -9.9027F, 0.14F, 0.0691F, 0.0097F));

		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.7973F, 2.3285F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r10 = Jaw.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(52, 110).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5913F, -12.0579F, -1.3614F, 0.0F, 0.0F));

		PartDefinition cube_r11 = Jaw.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(26, 80).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.7252F, -12.5579F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r12 = Jaw.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(74, 109).addBox(1.5F, 0.5825F, -7.4708F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(-5, -5).addBox(-1.5F, -1.4175F, -7.4708F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(88, 109).addBox(-1.5F, 0.5825F, -7.4708F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(84, 100).addBox(-1.5F, -1.4175F, -7.4708F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.2732F, -5.0781F, -0.0175F, 0.0F, 0.0F));

		PartDefinition cube_r13 = Jaw.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(114, 0).addBox(-3.5F, 2.0F, -6.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(24, 111).addBox(3.5F, 2.0F, -6.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(94, 75).addBox(-3.5F, 0.0F, -6.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1396F, 0.0F, 0.0F));

		PartDefinition bone5 = Head.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, -2.154F, 1.6326F, 0.2303F, -0.17F, -0.0396F));

		PartDefinition cube_r14 = bone5.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(12, 111).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1418F, -0.1728F, -0.0245F));

		PartDefinition bone4 = Head.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -2.154F, 1.6326F, 0.2303F, 0.17F, 0.0396F));

		PartDefinition cube_r15 = bone4.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 111).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1418F, 0.1728F, 0.0245F));

		PartDefinition Hips = Body.addOrReplaceChild("Hips", CubeListBuilder.create().texOffs(70, 48).addBox(-5.0F, -8.0F, 11.0F, 10.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0768F, -0.2693F));

		PartDefinition Tail1 = Hips.addOrReplaceChild("Tail1", CubeListBuilder.create().texOffs(78, 33).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 16.0F));

		PartDefinition bone = Tail1.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(26, 83).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.5F, 9.0F));

		PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 15.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.0F, 9.0F));

		PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 67).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(68, 67).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 14.0F));

		PartDefinition Neck2 = Body.addOrReplaceChild("Neck2", CubeListBuilder.create(), PartPose.offset(0.0F, -4.5551F, -19.6857F));

		PartDefinition cube_r16 = Neck2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(90, 0).addBox(-3.0F, 3.0F, -9.0F, 0.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftLeg = Vornskr.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 96).addBox(-3.0F, -4.0F, -2.0F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 5.0F, -16.0F));

		PartDefinition LeftLeg2 = LeftLeg.addOrReplaceChild("LeftLeg2", CubeListBuilder.create().texOffs(104, 27).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 6.0F, 0.5F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftLeg3 = LeftLeg2.addOrReplaceChild("LeftLeg3", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition cube_r17 = LeftLeg3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(100, 46).addBox(-1.5F, -1.0F, -4.5F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.8978F, 0.7765F, 0.3316F, 0.0F, 0.0F));

		PartDefinition cube_r18 = LeftLeg3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(114, 7).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition RightLeg = Vornskr.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(20, 96).addBox(-2.0F, -4.0F, -2.0F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 5.0F, -16.0F));

		PartDefinition RightLeg2 = RightLeg.addOrReplaceChild("RightLeg2", CubeListBuilder.create().texOffs(104, 96).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 6.0F, 0.5F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightLeg3 = RightLeg2.addOrReplaceChild("RightLeg3", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition cube_r19 = RightLeg3.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(62, 100).addBox(-3.5F, -1.0F, -4.5F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 2.8978F, 0.7765F, 0.3316F, 0.0F, 0.0F));

		PartDefinition cube_r20 = RightLeg3.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(116, 108).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition LeftBackLeg = Vornskr.addOrReplaceChild("LeftBackLeg", CubeListBuilder.create(), PartPose.offset(6.0F, 3.3F, 8.4F));

		PartDefinition cube_r21 = LeftBackLeg.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(102, 83).addBox(-2.0F, -0.7575F, -0.4069F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 7.1331F, -0.4125F, 1.1519F, 0.0F, 0.0F));

		PartDefinition cube_r22 = LeftBackLeg.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(78, 17).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.2269F, 0.0F, 0.0F));

		PartDefinition bone6 = LeftBackLeg.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(-0.5F, 7.2032F, 8.5789F));

		PartDefinition cube_r23 = bone6.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(100, 54).addBox(-2.5F, 0.0F, -3.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.6152F, 0.404F, 0.0175F, 0.0F, 0.0F));

		PartDefinition cube_r24 = bone6.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(62, 108).addBox(-2.0F, 0.0F, -3.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.2094F, 0.0F, 0.0F));

		PartDefinition RightBackLeg = Vornskr.addOrReplaceChild("RightBackLeg", CubeListBuilder.create(), PartPose.offset(-6.0F, 3.3F, 8.4F));

		PartDefinition cube_r25 = RightBackLeg.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(104, 14).addBox(-2.0F, -0.7575F, -0.4069F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 7.1331F, -0.4125F, 1.1519F, 0.0F, 0.0F));

		PartDefinition cube_r26 = RightBackLeg.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 80).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.2269F, 0.0F, 0.0F));

		PartDefinition bone7 = RightBackLeg.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offset(0.5F, 7.2032F, 8.5789F));

		PartDefinition cube_r27 = bone7.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(104, 39).addBox(-2.5F, 0.0F, -3.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.6152F, 0.404F, 0.0175F, 0.0F, 0.0F));

		PartDefinition cube_r28 = bone7.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(104, 108).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.2094F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		resetBasePose();
		this.Head.yRot = netHeadYaw * Mth.DEG_TO_RAD * 0.45F;
		this.Head.xRot = 0.14F + headPitch * Mth.DEG_TO_RAD * 0.45F;
		this.Jaw.xRot = -0.08F;

		if (entity instanceof VonskrEntity vonskr) {
			int attackTicks = vonskr.getAttackAnimationTicks();
			if (attackTicks > 0 || this.attackTime > 0.0F) {
				float attackPulse = attackTicks > 0
						? Mth.sin((1.0F - attackTicks / 10.0F) * Mth.PI)
						: Mth.sin(this.attackTime * Mth.PI);
				this.Jaw.xRot = 0.3054F + attackPulse * 0.10F;
			}

			if (vonskr.isSitting()) {
				setupSittingPose(ageInTicks);
				return;
			}
		}

		float walk = Math.min(limbSwingAmount, 1.0F);
		this.LeftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.75F * walk;
		this.RightLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 0.75F * walk;
		this.LeftBackLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 0.70F * walk;
		this.RightBackLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.70F * walk;
		float tailWave = Mth.sin(ageInTicks * 0.16F) * 0.10F + Mth.cos(limbSwing * 0.35F) * 0.12F * walk;
		this.Tail1.yRot = tailWave;
		this.bone.yRot = tailWave * 0.85F;
		this.bone2.yRot = tailWave * 0.70F;
	}

	private void resetBasePose() {
		this.Vornskr.y = 3.0F;
		this.Body.y = 3.6F;
		this.Body.xRot = 0.0698F;
		this.Hips.y = 6.0768F;
		this.Hips.xRot = 0.0F;
		this.Tail1.xRot = 0.0F;
		this.Tail1.yRot = 0.0F;
		this.bone.xRot = 0.0F;
		this.bone.yRot = 0.0F;
		this.bone2.xRot = 0.0F;
		this.bone2.yRot = 0.0F;
		this.LeftLeg.y = 5.0F;
		this.LeftLeg.xRot = 0.0F;
		this.LeftLeg2.xRot = -0.3491F;
		this.LeftLeg3.xRot = 0.0F;
		this.RightLeg.y = 5.0F;
		this.RightLeg.xRot = 0.0F;
		this.RightLeg2.xRot = -0.3491F;
		this.RightLeg3.xRot = 0.0F;
		this.LeftBackLeg.y = 3.3F;
		this.LeftBackLeg.xRot = 0.0F;
		this.bone6.xRot = 0.0F;
		this.RightBackLeg.y = 3.3F;
		this.RightBackLeg.xRot = 0.0F;
		this.bone7.xRot = 0.0F;
	}

	private void setupSittingPose(float ageInTicks) {
		this.Vornskr.y = 5.0F;
		this.Body.y = 4.4F;
		this.Body.xRot = -0.10F;
		this.Hips.y = 7.4F;
//		this.Hips.xRot = -0.42F;

		this.LeftLeg.xRot = -0.55F;
		this.RightLeg.xRot = -0.55F;
		this.LeftLeg2.xRot = 0.32F;
		this.RightLeg2.xRot = 0.32F;
		this.LeftLeg3.xRot = 0.18F;
		this.RightLeg3.xRot = 0.18F;

		this.LeftBackLeg.y = 6.2F;
		this.RightBackLeg.y = 6.2F;
		this.LeftBackLeg.xRot = -1.18F;
		this.RightBackLeg.xRot = -1.18F;
		this.bone6.xRot = 1.05F;
		this.bone7.xRot = 1.05F;

		float tailWave = Mth.sin(ageInTicks * 0.10F) * 0.04F;
		this.Tail1.xRot = 0.34F;
		this.Tail1.yRot = tailWave;
		this.bone.yRot = tailWave * 0.75F;
		this.bone2.yRot = tailWave * 0.55F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Vornskr.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}