package client.renderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import client.renderer.CustomRenderGuiOverlayEvent;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HyperspaceOverlayRenderer {

    private static final HyperspaceOverlay hyperspaceOverlay = new HyperspaceOverlay();

    @SubscribeEvent
    public static void onRenderGuiOverlay(CustomRenderGuiOverlayEvent event) { // ✅ Our custom event
        //System.out.println("Rendering Hyperspace Overlay..."); // Debugging
        hyperspaceOverlay.render(event.getGuiGraphics(), 0, 0, event.getPartialTick());
    }
}
