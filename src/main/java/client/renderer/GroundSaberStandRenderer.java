package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import server.galaxyunderchaos.block.GroundSaberStandBlock;
import server.galaxyunderchaos.entity.GroundSaberStandBlockEntity;

import static com.mojang.math.Axis.XN;
import static com.mojang.math.Axis.XP;
import static com.mojang.math.Axis.YN;
import static com.mojang.math.Axis.YP;
import static com.mojang.math.Axis.ZP;

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

        if (stand.isEmpty()) {
            return;
        }

        pose.pushPose();

        BlockState state = stand.getBlockState();
        Direction facing = state.getValue(GroundSaberStandBlock.FACING);
        AttachFace face = state.getValue(GroundSaberStandBlock.FACE);

        pose.translate(0.5D, 0.5D, 0.5D);

        float yRot = switch (facing) {
            case NORTH -> 0.0F;
            case WEST  -> 90.0F;
            case SOUTH -> 180.0F;
            case EAST  -> 270.0F;
            default    -> 0.0F;
        };
        pose.mulPose(YP.rotationDegrees(yRot));
        pose.mulPose(XP.rotationDegrees(90.0F));
        pose.translate(-0.5D, -0.5D, -0.5D);

        if (face == AttachFace.WALL) {
            pose.mulPose(XP.rotationDegrees(90.0F));
            pose.translate(0.60D, 0.8D, 0.78D);
            switch (facing){
                case NORTH, SOUTH -> {
                    pose.translate(0.0D, 0.0D, -2.0D);
                }
                case EAST, WEST -> {
                        pose.translate(0.0D, 0.0D, -2.0D);
                    }
            }
        } else {
            pose.translate(0.60D, 0.8D, 0.6D);

        }

        pose.mulPose(YN.rotationDegrees(90.0F));
        pose.mulPose(XN.rotationDegrees(90.0F));
        pose.mulPose(ZP.rotationDegrees(135.0F));

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