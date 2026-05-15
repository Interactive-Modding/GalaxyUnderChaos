
package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.block.LightsaberCraftingTableBlock;
import server.galaxyunderchaos.entity.LightsaberCraftingTableBlockEntity;
import server.galaxyunderchaos.lightsaber.LightsaberCraftingTableLogic;

public class LightsaberCraftingTableRenderer implements BlockEntityRenderer<LightsaberCraftingTableBlockEntity> {
    private static final float[][] POSITIONS = {
            {-0.27F, 0.25F}, {-0.09F, 0.25F}, {0.09F, 0.25F}, {0.27F, 0.25F},
            {-0.18F, -0.04F}, {0.00F, -0.04F}, {0.18F, -0.04F}, {0.33F, -0.04F}
    };

    private final ItemRenderer itemRenderer;

    public LightsaberCraftingTableRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(LightsaberCraftingTableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction facing = blockEntity.getBlockState().getValue(LightsaberCraftingTableBlock.FACING);
        float rotation = -facing.toYRot();

        for (int slot = 0; slot < LightsaberCraftingTableLogic.INPUT_SLOT_COUNT; ++slot) {
            ItemStack stack = blockEntity.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }

            poseStack.pushPose();
            poseStack.translate(0.5D, 1.02D, 0.5D);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotation));
            poseStack.translate(POSITIONS[slot][0], 0.0D, POSITIONS[slot][1]);
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(0.45F, 0.45F, 0.45F);
            this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }

        LightsaberCraftingTableLogic.Evaluation evaluation = LightsaberCraftingTableLogic.evaluate(blockEntity);
        ItemStack previewResult = LightsaberCraftingTableLogic.inactivePreview(evaluation.result());
        if (!previewResult.isEmpty()) {
            renderCompletedHiltOnStand(blockEntity, previewResult, poseStack, buffer, packedLight);
        }
    }
    /**
     * The completed preview belongs on the two stand rests, not hovering above the table.
     * This uses the raw hilt preview path so the finished saber is always inactive and never
     * renders a full active blade in-world.
     */
    private void renderCompletedHiltOnStand(LightsaberCraftingTableBlockEntity blockEntity,
                                            ItemStack previewResult,
                                            PoseStack poseStack,
                                            MultiBufferSource buffer,
                                            int packedLight) {
        Direction facing = blockEntity.getBlockState().getValue(LightsaberCraftingTableBlock.FACING);
        float rotation = -facing.toYRot();

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.055D, 0.5D);
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotation));
        poseStack.translate(0.0D, 0.016D, 0.045D);
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.72F, 0.72F, 0.72F);
        ModItemRenderer.renderForgePreview(previewResult, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

}
