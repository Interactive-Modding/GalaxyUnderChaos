// Made with Blockbench 4.12.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package client.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import server.galaxyunderchaos.entity.AcidSpiderEntity;
import server.galaxyunderchaos.galaxyunderchaos;

import javax.swing.text.html.parser.Entity;
public class AcidSpiderModel<T extends AcidSpiderEntity> extends HierarchicalModel<T> {    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(galaxyunderchaos.MODID, "acid_spiders"), "main");

	private final ModelPart MainBody;
	private final ModelPart LegONE;
	private final ModelPart LEG1A2;
	private final ModelPart LEG1B;
	private final ModelPart LEG1C1;
	private final ModelPart LEG1C2;
	private final ModelPart LegTWO;
	private final ModelPart LEG2C2;
	private final ModelPart LEG2C1;
	private final ModelPart LEG2B;
	private final ModelPart LEG2A;
	private final ModelPart LegTHREE;
	private final ModelPart LEG3A;
	private final ModelPart LEG3C;
	private final ModelPart LEG3B;
	private final ModelPart LEG3C2;
	private final ModelPart LegFOUR;
	private final ModelPart LEG4A;
	private final ModelPart LEG4B1;
	private final ModelPart LEG4C1;
	private final ModelPart LEG4C2;
	private final ModelPart LegFIVE;
	private final ModelPart LEG5A;
	private final ModelPart LEG5B;
	private final ModelPart LEG5C1;
	private final ModelPart LEG5C2;
	private final ModelPart LEGSIX;
	private final ModelPart LEG6A;
	private final ModelPart LEG6B;
	private final ModelPart LEG6C1;
	private final ModelPart LEG6C2;
	private final ModelPart LEGSEVEN2;
	private final ModelPart LEG7A2;
	private final ModelPart LEG7C3;
	private final ModelPart LEG7B2;
	private final ModelPart LEG7C4;
	private final ModelPart LEGEIGHT;
	private final ModelPart LEG8A;
	private final ModelPart LEG8C1;
	private final ModelPart LEG8B;
	private final ModelPart LEG8C2;
	private final ModelPart FANG1;
	private final ModelPart FANG2;


	public AcidSpiderModel(ModelPart root) {
		this.MainBody = root.getChild("MainBody");
		this.LegONE = this.MainBody.getChild("LegONE");
		this.LEG1A2 = this.LegONE.getChild("LEG1A2");
		this.LEG1B = this.LegONE.getChild("LEG1B");
		this.LEG1C1 = this.LegONE.getChild("LEG1C1");
		this.LEG1C2 = this.LegONE.getChild("LEG1C2");
		this.LegTWO = this.MainBody.getChild("LegTWO");
		this.LEG2C2 = this.LegTWO.getChild("LEG2C2");
		this.LEG2C1 = this.LegTWO.getChild("LEG2C1");
		this.LEG2B = this.LegTWO.getChild("LEG2B");
		this.LEG2A = this.LegTWO.getChild("LEG2A");
		this.LegFOUR = this.MainBody.getChild("LegFOUR");
		this.LEG4A = this.LegFOUR.getChild("LEG4A");
		this.LEG4B1 = this.LegFOUR.getChild("LEG4B1");
		this.LEG4C1 = this.LegFOUR.getChild("LEG4C1");
		this.LEG4C2 = this.LegFOUR.getChild("LEG4C2");
		this.LegFIVE = this.MainBody.getChild("LegFIVE");
		this.LEG5A = this.LegFIVE.getChild("LEG5A");
		this.LEG5B = this.LegFIVE.getChild("LEG5B");
		this.LEG5C1 = this.LegFIVE.getChild("LEG5C1");
		this.LEG5C2 = this.LegFIVE.getChild("LEG5C2");
		this.LEGSIX = this.MainBody.getChild("LEGSIX");
		this.LEG6A = this.LEGSIX.getChild("LEG6A");
		this.LEG6B = this.LEGSIX.getChild("LEG6B");
		this.LEG6C1 = this.LEGSIX.getChild("LEG6C1");
		this.LEG6C2 = this.LEGSIX.getChild("LEG6C2");
		this.LEGSEVEN2 = this.MainBody.getChild("LEGSEVEN2");
		this.LEG7A2 = this.LEGSEVEN2.getChild("LEG7A2");
		this.LEG7C3 = this.LEGSEVEN2.getChild("LEG7C3");
		this.LEG7B2 = this.LEGSEVEN2.getChild("LEG7B2");
		this.LEG7C4 = this.LEGSEVEN2.getChild("LEG7C4");
		this.LEGEIGHT = this.MainBody.getChild("LEGEIGHT");
		this.LEG8A = this.LEGEIGHT.getChild("LEG8A");
		this.LEG8C1 = this.LEGEIGHT.getChild("LEG8C1");
		this.LEG8B = this.LEGEIGHT.getChild("LEG8B");
		this.LEG8C2 = this.LEGEIGHT.getChild("LEG8C2");
		this.LegTHREE = this.MainBody.getChild("LegTHREE");
		this.LEG3A = this.LegTHREE.getChild("LEG3A");
		this.LEG3B = this.LegTHREE.getChild("LEG3B");
		this.LEG3C = this.LegTHREE.getChild("LEG3C1");
		this.LEG3C2 = this.LegTHREE.getChild("LEG3C2");
		this.FANG1 = this.MainBody.getChild("FANG1");
		this.FANG2 = this.FANG1.getChild("FANG2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition MainBody = partdefinition.addOrReplaceChild("MainBody", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -18.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(20, 23).addBox(-2.5F, -16.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(64, 27).addBox(0.5F, -16.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(40, 42).addBox(-3.0F, -16.0F, 0.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(44, 0).addBox(-3.0F, -16.0F, -2.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(64, 39).addBox(0.5F, -16.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(64, 31).addBox(-2.5F, -16.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(64, 35).addBox(-2.5F, -16.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(-3.0F, -25.0F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 23).addBox(-2.5F, -31.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Skull2B_r1 = MainBody.addOrReplaceChild("Skull2B_r1", CubeListBuilder.create().texOffs(20, 31).addBox(-1.0F, -6.0F, -0.5F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -25.0F, -2.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition Skull2A_r1 = MainBody.addOrReplaceChild("Skull2A_r1", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -6.0F, -0.5F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -25.0F, -2.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition Skull2D_r1 = MainBody.addOrReplaceChild("Skull2D_r1", CubeListBuilder.create().texOffs(32, 46).addBox(-4.5F, -6.0F, -1.0F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -25.0F, -2.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition Skull2C_r1 = MainBody.addOrReplaceChild("Skull2C_r1", CubeListBuilder.create().texOffs(44, 4).addBox(-4.5F, -6.0F, -1.0F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -25.0F, 3.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition LegONE = MainBody.addOrReplaceChild("LegONE", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, -8.5F, -7.0F, 0.0F, 0.8727F, 0.0F));

		PartDefinition LEG1A2 = LegONE.addOrReplaceChild("LEG1A2", CubeListBuilder.create(), PartPose.offset(3.0F, 0.0F, 6.0F));

		PartDefinition LegOneA_r1 = LEG1A2.addOrReplaceChild("LegOneA_r1", CubeListBuilder.create().texOffs(44, 46).addBox(-1.0F, -8.0F, -3.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -8.5F, -4.0F, 0.0F, 0.48F, 0.9163F));

		PartDefinition LEG1B = LegONE.addOrReplaceChild("LEG1B", CubeListBuilder.create(), PartPose.offset(3.0F, 0.0F, 6.0F));

		PartDefinition LegOneB_r1 = LEG1B.addOrReplaceChild("LegOneB_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -7.5F, -6.0F, 0.0F, 0.48F, -0.3054F));

		PartDefinition LEG1C1 = LegONE.addOrReplaceChild("LEG1C1", CubeListBuilder.create(), PartPose.offset(3.0F, 0.0F, 6.0F));

		PartDefinition LegOneC1_r1 = LEG1C1.addOrReplaceChild("LegOneC1_r1", CubeListBuilder.create().texOffs(56, 56).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 9.0F, -6.0F, 0.0F, 0.3491F, -0.0436F));

		PartDefinition LEG1C2 = LegONE.addOrReplaceChild("LEG1C2", CubeListBuilder.create(), PartPose.offset(3.0F, 0.0F, 6.0F));

		PartDefinition LegOneC2_r1 = LEG1C2.addOrReplaceChild("LegOneC2_r1", CubeListBuilder.create().texOffs(32, 31).addBox(-1.0F, -8.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, -6.0F, 0.0F, 0.3491F, 0.2182F));

		PartDefinition LegTWO = MainBody.addOrReplaceChild("LegTWO", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, -8.5F, -1.0F, 0.0F, 0.2182F, 0.0F));

		PartDefinition LEG2C2 = LegTWO.addOrReplaceChild("LEG2C2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LegTWOC2_r1 = LEG2C2.addOrReplaceChild("LegTWOC2_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-1.0F, -8.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.2182F));

		PartDefinition LEG2C1 = LegTWO.addOrReplaceChild("LEG2C1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LegTWOC1_r1 = LEG2C1.addOrReplaceChild("LegTWOC1_r1", CubeListBuilder.create().texOffs(0, 58).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, 0.0F, 0.3491F, -0.0436F));

		PartDefinition LEG2B = LegTWO.addOrReplaceChild("LEG2B", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LegTWOB_r1 = LEG2B.addOrReplaceChild("LegTWOB_r1", CubeListBuilder.create().texOffs(48, 11).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -7.5F, 0.0F, 0.0F, 0.48F, -0.3054F));

		PartDefinition LEG2A = LegTWO.addOrReplaceChild("LEG2A", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LegTWOA_r1 = LEG2A.addOrReplaceChild("LegTWOA_r1", CubeListBuilder.create().texOffs(8, 48).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -8.5F, 0.0F, 0.0F, 0.48F, 0.7418F));

		PartDefinition LegFOUR = MainBody.addOrReplaceChild("LegFOUR", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -8.5F, 8.0F, 0.0F, -1.2654F, 0.0F));

		PartDefinition LEG4A = LegFOUR.addOrReplaceChild("LEG4A", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -9.0F));

		PartDefinition LegFOURA_r1 = LEG4A.addOrReplaceChild("LegFOURA_r1", CubeListBuilder.create().texOffs(52, 46).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -8.5F, 9.0F, 0.0F, 0.48F, 0.7418F));

		PartDefinition LEG4B1 = LegFOUR.addOrReplaceChild("LEG4B1", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -9.0F));

		PartDefinition LegFOURB_r1 = LEG4B1.addOrReplaceChild("LegFOURB_r1", CubeListBuilder.create().texOffs(32, 53).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -7.5F, 9.0F, 0.0F, 0.48F, -0.3054F));

		PartDefinition LEG4C1 = LegFOUR.addOrReplaceChild("LEG4C1", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -9.0F));

		PartDefinition LegFOURC1_r1 = LEG4C1.addOrReplaceChild("LegFOURC1_r1", CubeListBuilder.create().texOffs(60, 44).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 9.0F, 9.0F, 0.0F, 0.3491F, -0.0436F));

		PartDefinition LEG4C2 = LegFOUR.addOrReplaceChild("LEG4C2", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -9.0F));

		PartDefinition LegFOURC2_r1 = LEG4C2.addOrReplaceChild("LegFOURC2_r1", CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, -8.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 9.0F, 0.0F, 0.3491F, 0.2182F));

		PartDefinition LegFIVE = MainBody.addOrReplaceChild("LegFIVE", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, -8.5F, 8.0F, 0.0F, -2.0944F, 0.0F));

		PartDefinition LEG5A = LegFIVE.addOrReplaceChild("LEG5A", CubeListBuilder.create(), PartPose.offset(14.0F, 0.0F, -9.0F));

		PartDefinition LegFIVEA_r1 = LEG5A.addOrReplaceChild("LegFIVEA_r1", CubeListBuilder.create().texOffs(56, 4).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.0F, -8.5F, 9.0F, 0.0F, 0.48F, 0.7418F));

		PartDefinition LEG5B = LegFIVE.addOrReplaceChild("LEG5B", CubeListBuilder.create(), PartPose.offset(14.0F, 0.0F, -9.0F));

		PartDefinition LegFIVEB_r1 = LEG5B.addOrReplaceChild("LegFIVEB_r1", CubeListBuilder.create().texOffs(56, 14).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -7.5F, 9.0F, 0.0F, 0.48F, -0.3054F));

		PartDefinition LEG5C1 = LegFIVE.addOrReplaceChild("LEG5C1", CubeListBuilder.create(), PartPose.offset(14.0F, 0.0F, -9.0F));

		PartDefinition LegFIVEC1_r1 = LEG5C1.addOrReplaceChild("LegFIVEC1_r1", CubeListBuilder.create().texOffs(32, 63).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 9.0F, 9.0F, 0.0F, 0.3491F, -0.0436F));

		PartDefinition LEG5C2 = LegFIVE.addOrReplaceChild("LEG5C2", CubeListBuilder.create(), PartPose.offset(14.0F, 0.0F, -9.0F));

		PartDefinition LegFIVEC2_r1 = LEG5C2.addOrReplaceChild("LegFIVEC2_r1", CubeListBuilder.create().texOffs(40, 14).addBox(-1.0F, -8.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.0F, 9.0F, 0.0F, 0.3491F, 0.2182F));

		PartDefinition LEGSIX = MainBody.addOrReplaceChild("LEGSIX", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, -8.5F, 3.0F, 0.0F, -2.8362F, 0.0F));

		PartDefinition LEG6A = LEGSIX.addOrReplaceChild("LEG6A", CubeListBuilder.create(), PartPose.offset(17.0F, 0.0F, -4.0F));

		PartDefinition LegSIXA_r1 = LEG6A.addOrReplaceChild("LegSIXA_r1", CubeListBuilder.create().texOffs(16, 56).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.0F, -8.5F, 4.0F, 0.0F, 0.48F, 0.7418F));

		PartDefinition LEG6B = LEGSIX.addOrReplaceChild("LEG6B", CubeListBuilder.create(), PartPose.offset(17.0F, 0.0F, -4.0F));

		PartDefinition LegSIXB_r1 = LEG6B.addOrReplaceChild("LegSIXB_r1", CubeListBuilder.create().texOffs(24, 56).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -7.5F, 4.0F, 0.0F, 0.48F, -0.3054F));

		PartDefinition LEG6C1 = LEGSIX.addOrReplaceChild("LEG6C1", CubeListBuilder.create(), PartPose.offset(17.0F, 0.0F, -4.0F));

		PartDefinition LegSIXC1_r1 = LEG6C1.addOrReplaceChild("LegSIXC1_r1", CubeListBuilder.create().texOffs(64, 0).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, 9.0F, 4.0F, 0.0F, 0.3491F, -0.0436F));

		PartDefinition LEG6C2 = LEGSIX.addOrReplaceChild("LEG6C2", CubeListBuilder.create(), PartPose.offset(17.0F, 0.0F, -4.0F));

		PartDefinition LegSIXC2_r1 = LEG6C2.addOrReplaceChild("LegSIXC2_r1", CubeListBuilder.create().texOffs(40, 28).addBox(-1.0F, -8.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, 0.0F, 4.0F, 0.0F, 0.3491F, 0.2182F));

		PartDefinition LEGSEVEN2 = MainBody.addOrReplaceChild("LEGSEVEN2", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, -8.5F, -3.0F, 0.0F, 2.7925F, 0.0F));

		PartDefinition LEG7A2 = LEGSEVEN2.addOrReplaceChild("LEG7A2", CubeListBuilder.create(), PartPose.offset(17.0F, 0.0F, 2.0F));

		PartDefinition LegSEVENA_r1 = LEG7A2.addOrReplaceChild("LegSEVENA_r1", CubeListBuilder.create().texOffs(40, 56).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.0F, -8.5F, -2.0F, 0.0F, 0.48F, 0.7418F));

		PartDefinition LEG7C3 = LEGSEVEN2.addOrReplaceChild("LEG7C3", CubeListBuilder.create(), PartPose.offset(17.0F, 0.0F, 2.0F));

		PartDefinition LegSEVENC1_r1 = LEG7C3.addOrReplaceChild("LegSEVENC1_r1", CubeListBuilder.create().texOffs(64, 18).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, 9.0F, -2.0F, 0.0F, 0.3491F, -0.0436F));

		PartDefinition LEG7B2 = LEGSEVEN2.addOrReplaceChild("LEG7B2", CubeListBuilder.create(), PartPose.offset(17.0F, 0.0F, 2.0F));

		PartDefinition LegSEVENB_r1 = LEG7B2.addOrReplaceChild("LegSEVENB_r1", CubeListBuilder.create().texOffs(48, 56).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -7.5F, -2.0F, 0.0F, 0.48F, -0.3054F));

		PartDefinition LEG7C4 = LEGSEVEN2.addOrReplaceChild("LEG7C4", CubeListBuilder.create(), PartPose.offset(17.0F, 0.0F, 2.0F));

		PartDefinition LegSEVENC2_r1 = LEG7C4.addOrReplaceChild("LegSEVENC2_r1", CubeListBuilder.create().texOffs(24, 42).addBox(-1.0F, -8.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, 0.0F, -2.0F, 0.0F, 0.3491F, 0.2182F));

		PartDefinition LEGEIGHT = MainBody.addOrReplaceChild("LEGEIGHT", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, -8.5F, -7.0F, 0.0F, 2.2689F, 0.0F));

		PartDefinition LEG8A = LEGEIGHT.addOrReplaceChild("LEG8A", CubeListBuilder.create(), PartPose.offset(14.0F, 0.0F, 6.0F));

		PartDefinition LegEIGHTA_r1 = LEG8A.addOrReplaceChild("LegEIGHTA_r1", CubeListBuilder.create().texOffs(56, 24).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.0F, -8.5F, -6.0F, 0.0F, 0.48F, 0.7418F));

		PartDefinition LEG8C1 = LEGEIGHT.addOrReplaceChild("LEG8C1", CubeListBuilder.create(), PartPose.offset(14.0F, 0.0F, 6.0F));

		PartDefinition LegEIGHTC1_r1 = LEG8C1.addOrReplaceChild("LegEIGHTC1_r1", CubeListBuilder.create().texOffs(64, 9).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 9.0F, -6.0F, 0.0F, 0.3491F, -0.0436F));

		PartDefinition LEG8B = LEGEIGHT.addOrReplaceChild("LEG8B", CubeListBuilder.create(), PartPose.offset(14.0F, 0.0F, 6.0F));

		PartDefinition LegEIGHTB_r1 = LEG8B.addOrReplaceChild("LegEIGHTB_r1", CubeListBuilder.create().texOffs(56, 34).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -7.5F, -6.0F, 0.0F, 0.48F, -0.3054F));

		PartDefinition LEG8C2 = LEGEIGHT.addOrReplaceChild("LEG8C2", CubeListBuilder.create(), PartPose.offset(14.0F, 0.0F, 6.0F));

		PartDefinition LegEIGHTC2_r1 = LEG8C2.addOrReplaceChild("LegEIGHTC2_r1", CubeListBuilder.create().texOffs(16, 42).addBox(-1.0F, -8.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.0F, -6.0F, 0.0F, 0.3491F, 0.2182F));

		PartDefinition LegTHREE = MainBody.addOrReplaceChild("LegTHREE", CubeListBuilder.create(), PartPose.offsetAndRotation(8.0F, -8.5F, 4.0F, 0.0F, -0.48F, 0.0F));

		PartDefinition LEG3A = LegTHREE.addOrReplaceChild("LEG3A", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -9.0F));

		PartDefinition LegTHREEA_r1 = LEG3A.addOrReplaceChild("LegTHREEA_r1", CubeListBuilder.create().texOffs(52, 46).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -8.5F, 9.0F, 0.0F, 0.48F, 0.7418F));

		PartDefinition LEG3B = LegTHREE.addOrReplaceChild("LEG3B", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -9.0F));

		PartDefinition LegTHREEB_r1 = LEG3B.addOrReplaceChild("LegTHREEB_r1", CubeListBuilder.create().texOffs(32, 53).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -7.5F, 9.0F, 0.0F, 0.48F, -0.3054F));

		PartDefinition LEG3C1 = LegTHREE.addOrReplaceChild("LEG3C1", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -9.0F));

		PartDefinition LegTHREEC1_r1 = LEG3C1.addOrReplaceChild("LegTHREEC1_r1", CubeListBuilder.create().texOffs(60, 44).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 9.0F, 9.0F, 0.0F, 0.3491F, -0.0436F));

		PartDefinition LEG3C2 = LegTHREE.addOrReplaceChild("LEG3C2", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -9.0F));

		PartDefinition LegTHREEC2_r1 = LEG3C2.addOrReplaceChild("LegTHREEC2_r1", CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, -8.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 9.0F, 0.0F, 0.3491F, 0.2182F));

		PartDefinition FANG1 = MainBody.addOrReplaceChild("FANG1", CubeListBuilder.create().texOffs(1, 0).addBox(-2.1679F, -14.0F, -3.9366F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0873F, 0.0F));

		PartDefinition FANG1B_r1 = FANG1.addOrReplaceChild("FANG1B_r1", CubeListBuilder.create().texOffs(1, 0).addBox(0.3F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1679F, -13.0F, -4.9366F, 0.0F, -0.3054F, 0.0F));

		PartDefinition FANG2 = FANG1.addOrReplaceChild("FANG2", CubeListBuilder.create().texOffs(1, 0).addBox(-2.1679F, -14.0F, -3.9366F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 0.0F, -2.9671F, 0.0F));

		PartDefinition FANG1B_r2 = FANG2.addOrReplaceChild("FANG1B_r2", CubeListBuilder.create().texOffs(1, 0).addBox(0.3F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1679F, -13.0F, -4.9366F, 0.0F, -0.3054F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(AcidSpiderEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose); // Reset any previous transformations.

		// Walking animation based on leg movement
		float swingSpeed = 1.0f; // The speed of the swing (how fast the legs move).
		float swingAmount = 0.5f; // The amount of leg swing (how far they move).

		// Animate each leg separately for walking
		animateLegs(limbSwing, limbSwingAmount, swingSpeed, swingAmount);

		// Optional: Head or body movement logic (if needed).
	}

	private void animateLegs(float limbSwing, float limbSwingAmount, float swingSpeed, float swingAmount) {
		// Sinusoidal motion to simulate walking. Adjust the offsets as needed.

		// Leg ONE (front-left)
		this.LegONE.xRot = Mth.cos(limbSwing * swingSpeed + 0.0f) * swingAmount * limbSwingAmount;

		// Leg TWO (front-right)
		this.LegTWO.xRot = Mth.cos(limbSwing * swingSpeed + 3.14159f) * swingAmount * limbSwingAmount; // Offset for opposite leg

		// Leg THREE (middle-left)
		this.LegTHREE.xRot = Mth.cos(limbSwing * swingSpeed + 1.5708f) * swingAmount * limbSwingAmount; // Offset for middle leg

		// Leg FOUR (middle-right)
		this.LegFOUR.xRot = Mth.cos(limbSwing * swingSpeed + 4.71239f) * swingAmount * limbSwingAmount; // Opposite middle leg

		// Leg FIVE (back-left)
		this.LegFIVE.xRot = Mth.cos(limbSwing * swingSpeed + 2.0f) * swingAmount * limbSwingAmount; // Offset for back leg

		// Leg SIX (back-right)
		this.LEGSIX.xRot = Mth.cos(limbSwing * swingSpeed + 5.0f) * swingAmount * limbSwingAmount; // Opposite back leg

		// Leg SEVEN (secondary back-left, for additional symmetry)
		this.LEGSEVEN2.xRot = Mth.cos(limbSwing * swingSpeed + 0.5f) * swingAmount * limbSwingAmount;

		// Leg EIGHT (secondary back-right, opposite of secondary back-left)
		this.LEGEIGHT.xRot = Mth.cos(limbSwing * swingSpeed + 3.5f) * swingAmount * limbSwingAmount;

		// Optional: Control the head or fang movements (if needed)
		// this.FANG1.xRot = someHeadMovementLogic;
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		MainBody.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return MainBody;
	}
}