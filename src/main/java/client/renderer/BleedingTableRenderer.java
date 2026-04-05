package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import client.model.BleedingTableModel;
import server.galaxyunderchaos.block.BleedingTable;
import server.galaxyunderchaos.entity.BleedingTableBlockEntity;
import server.galaxyunderchaos.galaxyunderchaos;

import static com.mojang.math.Axis.XP;
import static com.mojang.math.Axis.YP;

public class BleedingTableRenderer implements BlockEntityRenderer<BleedingTableBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(galaxyunderchaos.MODID, "textures/block/bleeding_table.png");

    private final BleedingTableModel model;

    public BleedingTableRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new BleedingTableModel(context.bakeLayer(BleedingTableModel.LAYER_LOCATION));
    }

    @Override
    public void render(BleedingTableBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        Direction facing = state.getValue(BleedingTable.FACING);

        poseStack.pushPose();
        poseStack.translate(0.5f, 1.5f, 0.5f);
        poseStack.mulPose(YP.rotationDegrees(-facing.toYRot()));
        poseStack.mulPose(XP.rotationDegrees(180.0F));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
