package client.renderer.ship;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import server.galaxyunderchaos.entity.FlashfireEntity;
import server.galaxyunderchaos.entity.NovadiveEntity;
import server.galaxyunderchaos.item.FlashfireItem;
import server.galaxyunderchaos.item.NovadiveItem;
import server.galaxyunderchaos.ship.ShipCustomization;

public final class ShipItemRendererHelper {
    private static NovadiveEntity novadivePreview;
    private static FlashfireEntity flashfirePreview;
    private static Level previewLevel;

    private ShipItemRendererHelper() {
    }

    public static boolean canRender(ItemStack stack) {
        return stack.getItem() instanceof NovadiveItem || stack.getItem() instanceof FlashfireItem;
    }

    public static void renderShipItem(ItemStack stack,
                                      ItemDisplayContext displayContext,
                                      PoseStack poseStack,
                                      MultiBufferSource buffer,
                                      int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        Entity ship = getPreviewEntity(stack, minecraft.level);
        if (ship == null) {
            return;
        }

        float scale = switch (displayContext) {
            case GUI -> 0.09F;
            case GROUND -> 0.12F;
            case FIXED -> 0.16F;
            case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> 0.16F;
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> 0.18F;
            default -> 0.15F;
        };

        float yaw = switch (displayContext) {
            case GUI -> 155.0F;
            case GROUND -> 20.0F;
            case FIXED -> -25.0F;
            default -> 12.0F;
        };

        float pitch = switch (displayContext) {
            case GUI -> -32.0F;
            case GROUND -> 8.0F;
            case FIXED -> 10.0F;
            default -> 12.0F;
        };


        poseStack.pushPose();
        poseStack.translate(0.5F, 0.46F, 0.5F);
        renderPreparedEntity(ship, poseStack, buffer, packedLight, yaw, pitch, scale, false);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.popPose();
    }

    public static void renderShipPreview(ItemStack stack,
                                         PoseStack poseStack,
                                         MultiBufferSource buffer,
                                         int packedLight,
                                         float yaw,
                                         float pitch,
                                         float scale) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        Entity ship = getPreviewEntity(stack, minecraft.level);
        if (ship == null) {
            return;
        }

        renderPreparedEntity(ship, poseStack, buffer, packedLight, yaw, pitch, scale,true);
    }

    private static Entity getPreviewEntity(ItemStack stack, Level level) {
        if (previewLevel != level) {
            previewLevel = level;
            novadivePreview = null;
            flashfirePreview = null;
        }

        Entity entity;
        if (stack.getItem() instanceof NovadiveItem) {
            if (novadivePreview == null) {
                novadivePreview = new NovadiveEntity(level, 0.0D, 0.0D, 0.0D);
            }
            ShipCustomization.applyToShip(stack, novadivePreview);
            entity = novadivePreview;
        } else if (stack.getItem() instanceof FlashfireItem) {
            if (flashfirePreview == null) {
                flashfirePreview = new FlashfireEntity(level, 0.0D, 0.0D, 0.0D);
            }
            ShipCustomization.applyToShip(stack, flashfirePreview);
            entity = flashfirePreview;
        } else {
            return null;
        }

        entity.tickCount = Minecraft.getInstance().player != null ? Minecraft.getInstance().player.tickCount : 0;
        entity.setYRot(0.0F);
        entity.yRotO = 0.0F;
        entity.setXRot(0.0F);
        entity.xRotO = 0.0F;
        return entity;
    }

    private static void renderPreparedEntity(Entity entity,
                                             PoseStack poseStack,
                                             MultiBufferSource buffer,
                                             int packedLight,
                                             float yaw,
                                             float pitch,
                                             float scale,
                                             boolean screenSpace) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        poseStack.pushPose();
        if (screenSpace) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
        /*
         * Do not apply the usual inventory Z-flip here. The ship entity renderer already
         * mirrors the exported GeckoLib model with scale(-2, 2, 2). Adding another
         * 180-degree screen-space flip is what made the item/preview appear upside down.
         */
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.scale(scale, scale, scale);

        dispatcher.setRenderShadow(false);
        dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, poseStack, buffer, packedLight);
        dispatcher.setRenderShadow(true);
        poseStack.popPose();
    }
}
