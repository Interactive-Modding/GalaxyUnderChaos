package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import server.galaxyunderchaos.entity.ThrownLightsaberEntity;
import server.galaxyunderchaos.galaxyunderchaos;

public class ThrownLightsaberRenderer extends EntityRenderer<ThrownLightsaberEntity> {
    private final ItemRenderer itemRenderer;

    public ThrownLightsaberRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ThrownLightsaberEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        float spin = (entity.tickCount + partialTick) * (entity.isReturning() ? 55.0F : 35.0F);

        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch));
        // Lay the saber flat across the travel axis so it reads as a horizontal throw.
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        // Spin around the flight vector instead of wobbling end-over-end.
        poseStack.mulPose(Axis.YP.rotationDegrees(spin));
        poseStack.scale(1.15F, 1.15F, 1.15F);
        this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.FIXED, packedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownLightsaberEntity entity) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/gui/icons.png");
    }
}
