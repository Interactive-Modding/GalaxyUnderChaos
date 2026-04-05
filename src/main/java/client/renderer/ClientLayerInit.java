package client.renderer;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;

/** Registers our custom lightsaber pose layer on every player model. */
@Mod.EventBusSubscriber(
        modid = galaxyunderchaos.MODID,
        value = Dist.CLIENT,
        bus   = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientLayerInit {
    private ClientLayerInit() {}   // no instances

    private static boolean DONE = false;          // simple duplicate‑guard

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        if (DONE) return;                         // already added once
        DONE = true;

        for (String skinId : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skinId);
            if (renderer != null) {
                renderer.addLayer(new LightsaberFirstPersonLayer(renderer));
            }
        }
    }
}
