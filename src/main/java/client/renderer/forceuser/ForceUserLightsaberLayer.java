package client.renderer.forceuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.entity.forceuser.ForceUserLoadout;

public class ForceUserLightsaberLayer extends RenderLayer<ForceUserEntity, ForceUserLayeredModel> {
    /**
     * Keep NPC belt sabers at the same readable waist size as player belt sabers.
     * The hand transform still uses its own context scale, but belt rendering uses
     * FIXED just like the player belt renderer.
     */
    private static final float HAND_OUTER_SCALE = 1.00F;
    private static final float BELT_OUTER_SCALE = 0.50F;

    public ForceUserLightsaberLayer(RenderLayerParent<ForceUserEntity, ForceUserLayeredModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ForceUserEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isSaberDrawn()) {
            ItemStack held = entity.getMainHandItem();
            if (!held.isEmpty()) {
                renderInRightHand(entity, held, poseStack, buffer, packedLight);
            }
        } else {
            ItemStack belt = entity.getBeltLightsaber();
            if (!belt.isEmpty()) {
                renderOnBelt(entity, belt, poseStack, buffer, packedLight);
            }
        }
    }

    private void renderOnBelt(ForceUserEntity entity, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack beltStack = stack.copy();
        ForceUserLoadout.setLightsaberActive(beltStack, false);

        poseStack.pushPose();

        // Match the player belt placement instead of using the larger hand transform. This keeps
        // the hilt at the waist whenever the NPC has not drawn it.
        poseStack.translate(-0.19D, 0.65D, -0.15D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(135.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(12.0F));
        poseStack.scale(BELT_OUTER_SCALE, BELT_OUTER_SCALE, BELT_OUTER_SCALE);

        itemRenderer.renderStatic(entity, beltStack, ItemDisplayContext.FIXED, false, poseStack, buffer, entity.level(), packedLight, OverlayTexture.NO_OVERLAY, entity.getId());
        poseStack.popPose();
    }

    private void renderInRightHand(ForceUserEntity entity, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();

        // Follow the Blockbench arm instead of using a static entity-space offset. This fixes sabers
        // appearing down in the legs or pointing sideways during the attack animation.
        this.getParentModel().translateToRightHand(poseStack);
        poseStack.translate(0.0D, 0.66D, 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.translate(0.0D, 0.04D, -0.08D);
        poseStack.scale(HAND_OUTER_SCALE, HAND_OUTER_SCALE, HAND_OUTER_SCALE);

        itemRenderer.renderStatic(entity, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, buffer, entity.level(), packedLight, OverlayTexture.NO_OVERLAY, entity.getId());
        poseStack.popPose();
    }
}
