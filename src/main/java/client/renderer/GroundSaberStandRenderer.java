package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import server.galaxyunderchaos.block.GroundSaberStandBlock;
import server.galaxyunderchaos.entity.GroundSaberStandBlockEntity;

import static com.mojang.math.Axis.*;

public class GroundSaberStandRenderer implements BlockEntityRenderer<GroundSaberStandBlockEntity> {

    private final ItemRenderer itemRenderer;

    public GroundSaberStandRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(GroundSaberStandBlockEntity stand,
                       float partialTick,
                       PoseStack pose,
                       MultiBufferSource buffer,
                       int light,
                       int overlay) {

        if (stand.isEmpty()) return;

        pose.pushPose();

        BlockState state = stand.getBlockState();
        Direction facing = state.getValue(GroundSaberStandBlock.FACING);

        switch (facing) {
            case EAST  -> pose.translate(-0.18D, 0.0D, 0.017D);
            case WEST  -> pose.translate(0.02D, 0.0D, 0.183D);
            case NORTH, SOUTH -> {}
        }

// 2. NOW apply your original saber rotations
        pose.translate(0.58D, 0.07D, 0.4D);
        pose.mulPose(YN.rotationDegrees(90.0F));
        pose.mulPose(XN.rotationDegrees(90.0F));
        pose.mulPose(ZP.rotationDegrees(135.0F));

// 3. NOW apply Z rotation for facing EAST/WEST
        switch (facing) {
            case EAST  -> pose.mulPose(ZP.rotationDegrees(90));
            case WEST  -> pose.mulPose(ZP.rotationDegrees(270));
            default -> {}
        }

        itemRenderer.renderStatic(
                stand.getItem(),
                ItemDisplayContext.GROUND,
                light,
                OverlayTexture.NO_OVERLAY,
                pose,
                buffer,
                stand.getLevel(),
                0
        );

        pose.popPose();
    }


}
