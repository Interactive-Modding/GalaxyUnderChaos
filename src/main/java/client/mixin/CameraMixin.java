package client.mixin;

import client.ShipRenderingHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import server.galaxyunderchaos.entity.NovadiveEntity;
import server.galaxyunderchaos.entity.FlashfireEntity;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @ModifyArg(
            method = "setup",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(D)D"),
            index = 0
    )
    private double galaxyunderchaos$useShipZoomDistance(double vanillaDistance) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && (minecraft.player.getVehicle() instanceof NovadiveEntity || minecraft.player.getVehicle() instanceof FlashfireEntity)) {
            return ShipRenderingHandler.INSTANCE.getThirdPersonViewDistance();
        }
        return vanillaDistance;
    }
}