package client.renderer;

import client.renderer.lightsaber.LightsaberStanceController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LightsaberHandAnimationHandler {
    private LightsaberHandAnimationHandler() {}

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        LightsaberStanceController.applyFirstPersonPose(
                event.getPoseStack(),
                player,
                event.getItemStack(),
                event.getHand(),
                event.getPartialTick(),
                event.getEquipProgress()
        );
    }
}
