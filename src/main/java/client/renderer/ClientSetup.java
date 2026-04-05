package client.renderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.MinecraftForge;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientSetup {

    private static final HyperspaceOverlay hyperspaceOverlay = new HyperspaceOverlay();

    @SubscribeEvent
    public static void onRenderGuiOverlay(CustomizeGuiOverlayEvent event) { // ✅ Correct event type
        //System.out.println("Rendering Hyperspace Overlay..."); // Debugging
        hyperspaceOverlay.render(event.getGuiGraphics(), 0, 0, event.getPartialTick());
    }

    public static void setup() {
        //System.out.println("Registering ClientSetup...");
        MinecraftForge.EVENT_BUS.register(ClientSetup.class);
    }
}
