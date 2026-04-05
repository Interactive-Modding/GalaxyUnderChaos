package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.item.LightsaberItem;

@Mod.EventBusSubscriber(modid = "galaxyunderchaos", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LightsaberBeltRenderer {

    @SubscribeEvent
    public static void renderLightsaberOnBelt(RenderLivingEvent.Post<?, ?> event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // Skip rendering if player holds the saber in hand
        if (player.getMainHandItem().getItem() instanceof LightsaberItem || player.getOffhandItem().getItem() instanceof LightsaberItem) {
            return;
        }

        // Find the first lightsaber in hotbar
        ItemStack saberStack = ItemStack.EMPTY;
        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() instanceof LightsaberItem) {
                saberStack = stack;
                break;
            }
        }

        if (saberStack.isEmpty()) return;

        renderOnBelt(player, saberStack, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
    }

    private static void renderOnBelt(Player player, ItemStack lightsaberStack, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        poseStack.pushPose();

        // Apply body rotation (Y axis, horizontal rotation)
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - player.yBodyRot));

        // Adjust for crouching posture
        Vec3 velocity = player.getDeltaMovement(); // Get player's movement vector
        float pitch = player.getXRot(); // Player's vertical rotation (-90 to 90)
        float yaw = player.getYRot(); // Player's horizontal rotation

// Default waist position
        float xOffset = -0.19F;
        float yOffset = 0.65F;
        float zOffset = -0.15F;

        if (player.isSwimming()) {
            // Adjust dynamically based on movement direction
            if (!velocity.equals(Vec3.ZERO)) {
                xOffset += (float) velocity.x * 0.2F;
                yOffset += (float) velocity.y * 0F;
                zOffset += (float) velocity.z * 0.2F;
            }

            // Rotate dynamically based on swimming direction
            poseStack.mulPose(Axis.XP.rotationDegrees(-pitch + 90F));
        } else if (player.isFallFlying()) {
            // Elytra flight (back attachment)
            xOffset += 0.1F;
            yOffset += 0.3F;
            zOffset += 0.5F;
            poseStack.mulPose(Axis.XP.rotationDegrees(180F)); // Rotate for back positioning
        } else if (player.isCrouching()) {
            zOffset += 0.4F;
            yOffset += 0F; // Adjust for crouching
            poseStack.mulPose(Axis.XP.rotationDegrees(-15F)); // Slight forward tilt
        }

// Apply final transformation
        poseStack.translate(xOffset, yOffset, zOffset);


        // Position the lightsaber at the waist
//        poseStack.translate(-0.19F, 0.45F, -.14F);

        // Rotate horizontally, laying flat against waist
        poseStack.mulPose(Axis.ZP.rotationDegrees(135.0F));

        // Scale to appropriate size (adjust as necessary)
        poseStack.scale(0.5F, 0.5F, 0.5F);

        // Render the model
        itemRenderer.renderStatic(player, lightsaberStack, ItemDisplayContext.FIXED, false, poseStack, bufferSource, player.level(), packedLight, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }
}
