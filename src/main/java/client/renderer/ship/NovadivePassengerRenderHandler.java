package client.renderer.ship;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.entity.NovadiveEntity;
import server.galaxyunderchaos.entity.FlashfireEntity;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class NovadivePassengerRenderHandler {
    private NovadivePassengerRenderHandler() {
    }

    @SubscribeEvent
    public static void renderPilotWithShipTilt(RenderPlayerEvent.Pre event) {
        float visualPitch;
        float visualRoll;

        if (event.getEntity().getVehicle() instanceof NovadiveEntity ship) {
            if (!ship.isPilot(event.getEntity())) {
                return;
            }

            visualPitch = -ship.getRenderPitch();
            visualRoll = -ship.getRenderRoll();
        } else if (event.getEntity().getVehicle() instanceof FlashfireEntity ship) {
            if (!ship.isPilot(event.getEntity())) {
                return;
            }

            visualPitch = -ship.getRenderPitch();
            visualRoll = -ship.getRenderRoll();
        } else {
            return;
        }

        if (Math.abs(visualPitch) < 0.01F && Math.abs(visualRoll) < 0.01F) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        poseStack.mulPose(Axis.XP.rotationDegrees(visualPitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(visualRoll));
    }
}