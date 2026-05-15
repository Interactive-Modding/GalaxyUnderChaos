package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.block.ShipCraftingTableBlock;
import server.galaxyunderchaos.entity.ShipCraftingTableBlockEntity;
import server.galaxyunderchaos.ship.ShipCraftingTableLogic;

public class ShipCraftingTableRenderer implements BlockEntityRenderer<ShipCraftingTableBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ShipCraftingTableRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ShipCraftingTableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction facing = blockEntity.getBlockState().getValue(ShipCraftingTableBlock.FACING);
        float rotation = -facing.toYRot();

        ItemStack blueprint = blockEntity.getItem(ShipCraftingTableLogic.SLOT_BLUEPRINT);
        if (!blueprint.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 1.03D, 0.5D);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotation));
            poseStack.translate(-0.30D, 0.0D, 0.24D);
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(0.55F, 0.55F, 0.55F);
            this.itemRenderer.renderStatic(blueprint, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }

        // Output ships are intentionally not rendered on top of the table.
        // Only the inserted blueprint/input item is shown here; the finished ship stays in the GUI result slot.
    }
}
