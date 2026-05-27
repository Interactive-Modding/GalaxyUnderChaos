package server.galaxyunderchaos.data;

import client.ShipRenderingHandler;
import client.renderer.ForceAlignmentOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.entity.NovadiveEntity;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.entity.FlashfireEntity;
import server.galaxyunderchaos.force.*;
import server.galaxyunderchaos.lightsaber.LightsaberFormNetworking;
import server.galaxyunderchaos.lightsaber.SwitchLightsaberFormPacket;
import server.galaxyunderchaos.lightsaber.ToggleLightsaberPacket;
import server.galaxyunderchaos.ship.ShipControlPacket;
import server.galaxyunderchaos.ship.ShipNetworking;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyInputHandler {
    private static boolean wasForceUseDown;
    private static int lastShipControls = -1;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getVehicle() instanceof NovadiveEntity ship) {
            int controls = 0;
            if (mc.options.keyUp.isDown()) controls |= NovadiveEntity.CONTROL_FORWARD;
            if (mc.options.keyDown.isDown()) controls |= NovadiveEntity.CONTROL_BACKWARD;
            if (mc.options.keyLeft.isDown()) controls |= NovadiveEntity.CONTROL_LEFT;
            if (mc.options.keyRight.isDown()) controls |= NovadiveEntity.CONTROL_RIGHT;
            if (mc.options.keyJump.isDown() || KeyBindings.SHIP_ASCEND.isDown()) controls |= NovadiveEntity.CONTROL_ASCEND;
            if (KeyBindings.SHIP_DESCEND.isDown()) controls |= NovadiveEntity.CONTROL_DESCEND;
            if (KeyBindings.SHIP_ROLL_LEFT.isDown()) controls |= NovadiveEntity.CONTROL_ROLL_LEFT;
            if (KeyBindings.SHIP_ROLL_RIGHT.isDown()) controls |= NovadiveEntity.CONTROL_ROLL_RIGHT;
            if (mc.options.keySprint.isDown() || KeyBindings.SHIP_BOOST.isDown()) controls |= NovadiveEntity.CONTROL_BOOST;

            ship.setControls(controls);
            handleShipCameraZoom();

            if (controls != lastShipControls) {
                ShipNetworking.sendToServer(new ShipControlPacket(controls));
                lastShipControls = controls;
            }
            wasForceUseDown = false;
            return;
        }

        if (mc.player != null && mc.player.getVehicle() instanceof FlashfireEntity ship) {
            int controls = 0;
            if (mc.options.keyUp.isDown()) controls |= FlashfireEntity.CONTROL_FORWARD;
            if (mc.options.keyDown.isDown()) controls |= FlashfireEntity.CONTROL_BACKWARD;
            if (mc.options.keyLeft.isDown()) controls |= FlashfireEntity.CONTROL_LEFT;
            if (mc.options.keyRight.isDown()) controls |= FlashfireEntity.CONTROL_RIGHT;
            if (mc.options.keyJump.isDown() || KeyBindings.SHIP_ASCEND.isDown()) controls |= FlashfireEntity.CONTROL_ASCEND;
            if (KeyBindings.SHIP_DESCEND.isDown()) controls |= FlashfireEntity.CONTROL_DESCEND;
            if (KeyBindings.SHIP_ROLL_LEFT.isDown()) controls |= FlashfireEntity.CONTROL_ROLL_LEFT;
            if (KeyBindings.SHIP_ROLL_RIGHT.isDown()) controls |= FlashfireEntity.CONTROL_ROLL_RIGHT;
            if (mc.options.keySprint.isDown() || KeyBindings.SHIP_BOOST.isDown()) controls |= FlashfireEntity.CONTROL_BOOST;

            ship.setControls(controls);
            handleShipCameraZoom();

            if (controls != lastShipControls) {
                ShipNetworking.sendToServer(new ShipControlPacket(controls));
                lastShipControls = controls;
            }
            wasForceUseDown = false;
            return;
        }
        lastShipControls = -1;

        while (KeyBindings.SWITCH_FORM_KEY.consumeClick()) {
            LightsaberFormNetworking.sendToServer(new SwitchLightsaberFormPacket());
        }

        while (KeyBindings.TOGGLE_LIGHTSABER.consumeClick()) {
            LightsaberFormNetworking.sendToServer(new ToggleLightsaberPacket());
        }

        while (KeyBindings.CYCLE_FORCE_POWER.consumeClick()) {
            ForceNetworking.sendToServer(new CycleForcePowerPacket());
        }

        while (KeyBindings.SHOW_FORCE_ALIGNMENT.consumeClick()) {
            Entity lookedAt = mc.crosshairPickEntity;
            if (lookedAt instanceof ForceUserEntity forceUser) {
                ForceAlignmentOverlay.showEntityStatus(forceUser, 180);
            } else {
                ForceAlignmentOverlay.showStatus(180);
            }
        }

        boolean holdAbility = mc.player != null && mc.player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(cap -> ForcePowerHandler.isHoldAbility(cap.getSelectedPower()))
                .orElse(false);
        boolean down = KeyBindings.USE_FORCE_POWER.isDown();

        if (down && !wasForceUseDown) {
            if (holdAbility) {
                ForceNetworking.sendToServer(new SetForceUseStatePacket(true));
            } else {
                ForceNetworking.sendToServer(new UseForcePowerPacket());
            }
        }

        if (!down && wasForceUseDown && holdAbility) {
            ForceNetworking.sendToServer(new SetForceUseStatePacket(false));
        }

        wasForceUseDown = down;
    }

    private static void handleShipCameraZoom() {
        if (KeyBindings.SHIP_THIRD_PERSON_VIEW_ZOOM_OUT.consumeClick()) {
            ShipRenderingHandler.INSTANCE.setThirdPersonViewDistance(
                    ShipRenderingHandler.INSTANCE.getThirdPersonViewDistance() + 1);
        }
        if (KeyBindings.SHIP_THIRD_PERSON_VIEW_ZOOM_IN.consumeClick()) {
            ShipRenderingHandler.INSTANCE.setThirdPersonViewDistance(
                    ShipRenderingHandler.INSTANCE.getThirdPersonViewDistance() - 1);
        }
    }
}