package client.renderer.ship;

import client.sound.NovadiveLoopSound;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import server.galaxyunderchaos.entity.NovadiveEntity;
import server.galaxyunderchaos.ship.ShipColorSection;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NovadiveRenderer extends GeoEntityRenderer<NovadiveEntity> {
    public NovadiveRenderer(EntityRendererProvider.Context context) {
        super(context, new NovadiveGeoModel());
        this.shadowRadius = 1.35F;
    }

    @Override
    public RenderType getRenderType(NovadiveEntity ship, ResourceLocation texture, MultiBufferSource buffer, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void render(NovadiveEntity ship, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.02D, 0.0D);

        float yaw = Mth.lerp(partialTick, ship.yRotO, ship.getYRot());

        float visualPitch = -ship.getRenderPitch();
        float visualRoll = -ship.getRenderRoll();

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(visualPitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(visualRoll));
        poseStack.scale(-2.0F, 2.0F, 2.0F);

        renderShipBody(ship, partialTick, poseStack, buffer, packedLight);
        NovadiveExhaustRenderer.render(poseStack, buffer, ship, partialTick);
        poseStack.popPose();

        if (ship.getEnginePower() > 0.02F) {
            NovadiveLoopSound.play(ship);
        }
    }

    private void renderShipBody(NovadiveEntity ship, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        try {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            if (ship.hasCustomShipColors()) {
                NovadiveGeoModel.setTextureOverride(ShipTextureComposer.getNovadiveTexture(
                        ship.getShipColor(ShipColorSection.BASE),
                        ship.getShipColor(ShipColorSection.PRIMARY),
                        ship.getShipColor(ShipColorSection.SECONDARY),
                        ship.getShipColor(ShipColorSection.INTERIOR)));
            } else {
                NovadiveGeoModel.clearTextureOverride();
            }

            super.render(ship, 180.0F, partialTick, poseStack, buffer, packedLight);
        } finally {
            NovadiveGeoModel.clearTextureOverride();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
