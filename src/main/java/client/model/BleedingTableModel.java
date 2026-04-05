// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
package client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.galaxyunderchaos;

public class BleedingTableModel extends Model {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(galaxyunderchaos.MODID, "bleeding_table"), "main");


    private final ModelPart root;

    private final ModelPart SaberForgeMAIN;
    private final ModelPart SaberForgeMAIN2;
    private final ModelPart SaberForgeMAIN3;

    private final ModelPart part20;
    private final ModelPart part2;

    private final ModelPart part30;
    private final ModelPart part300;
    private final ModelPart part3000;
    private final ModelPart part30000;
    private final ModelPart part300000;

    private final ModelPart part4000000;

    public BleedingTableModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;

        this.SaberForgeMAIN = root.getChild("SaberForgeMAIN");
        this.SaberForgeMAIN2 = SaberForgeMAIN.getChild("SaberForgeMAIN2");
        this.SaberForgeMAIN3 = SaberForgeMAIN2.getChild("SaberForgeMAIN3");

        this.part20 = root.getChild("part20");
        this.part2 = part20.getChild("part2");

        this.part30 = root.getChild("part30");
        this.part300 = part30.getChild("part300");
        this.part3000 = part300.getChild("part3000");
        this.part30000 = part3000.getChild("part30000");
        this.part300000 = part300.getChild("part300000");

        this.part4000000 = root.getChild("part4000000");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition SaberForgeMAIN = root.addOrReplaceChild(
                "SaberForgeMAIN",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-16.0F, 0.0F, 0.0F, 16, 16, 16),
                PartPose.offset(8.0F, 8.0F, -8.0F)
        );

        PartDefinition SaberForgeMAIN2 = SaberForgeMAIN.addOrReplaceChild(
                "SaberForgeMAIN2",
                CubeListBuilder.create()
                        .texOffs(0, 49)
                        .addBox(-16.0F, 0.0F, 0.0F, 12, 16, 11),
                PartPose.offset(16.0F, 0.0F, 5.0F)
        );

        SaberForgeMAIN2.addOrReplaceChild(
                "SaberForgeMAIN3",
                CubeListBuilder.create()
                        .texOffs(46, 49)
                        .addBox(-12.0F, 0.0F, 0.0F, 12, 16, 11),
                PartPose.offset(-32.0F, 0.0F, 0.0F)
        );

        PartDefinition part20 = root.addOrReplaceChild(
                "part20",
                CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(-13.1F, 0.0F, 0.0F, 13, 16, 5),
                PartPose.offsetAndRotation(-8.0F, 8.0F, -8.0F, 0.0F, 0.3519F, 0.0F)
        );

        part20.addOrReplaceChild(
                "part2",
                CubeListBuilder.create()
                        .texOffs(64, 21)
                        .addBox(-13.1F, 0.0F, 0.0F, 13, 16, 5),
                PartPose.offsetAndRotation(25.0F, 0.0F, 14.0F, 0.0F, -0.703F, 0.0F)
        );

        PartDefinition part30 = root.addOrReplaceChild(
                "part30",
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(-20.0F, 0.0F, 0.0F, 20, 1, 13),
                PartPose.offset(10.0F, 7.6F, -6.4F)
        );

        PartDefinition part300 = part30.addOrReplaceChild(
                "part300",
                CubeListBuilder.create()
                        .texOffs(35, 49)
                        .addBox(-9.6F, 0.0F, 0.0F, 10, 1, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 2.7506F, 0.0F)
        );

        PartDefinition part3000 = part300.addOrReplaceChild(
                "part3000",
                CubeListBuilder.create()
                        .texOffs(35, 53)
                        .addBox(-10.0F, 0.0F, 0.4F, 10, 1, 1),
                PartPose.offsetAndRotation(19.0F, 0.0F, -6.3F, 0.0F, -2.3721F, 0.0F)
        );

        part3000.addOrReplaceChild(
                "part30000",
                CubeListBuilder.create()
                        .texOffs(54, 42)
                        .addBox(-14.1F, 0.0F, -9.25F, 10, 1, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1861F, 0.0F)
        );

        part300.addOrReplaceChild(
                "part300000",
                CubeListBuilder.create()
                        .texOffs(35, 51)
                        .addBox(2.4F, 0.0F, -9.4F, 10, 1, 1),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.9291F, 0.0F)
        );

        root.addOrReplaceChild(
                "part4000000",
                CubeListBuilder.create()
                        .texOffs(0, 47)
                        .addBox(-38.3F, 0.0F, 0.0F, 38, 1, 1),
                PartPose.offset(19.0F, 7.6F, 6.9F)
        );

        return LayerDefinition.create(mesh, 128, 128);
    }


    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

}
