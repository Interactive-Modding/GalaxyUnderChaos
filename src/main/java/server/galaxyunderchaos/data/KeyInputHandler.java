package server.galaxyunderchaos.data;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.LightsaberFormNetworking;
import server.galaxyunderchaos.lightsaber.SwitchLightsaberFormPacket;
import server.galaxyunderchaos.lightsaber.ToggleLightsaberPacket;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        while (KeyBindings.SWITCH_FORM_KEY.consumeClick()) {
            LightsaberFormNetworking.sendToServer(new SwitchLightsaberFormPacket());
        }

        while (KeyBindings.TOGGLE_LIGHTSABER.consumeClick()) {
            LightsaberFormNetworking.sendToServer(new ToggleLightsaberPacket());
        }
    }
}
