package client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForceClientSoundHandler {
    private static MovingSoundForceLightning lightningSound;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.isPaused()) {
            return;
        }
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            boolean active = cap.isVisualLightning();
            if (active && (lightningSound == null || lightningSound.isStoppedCustom())) {
                lightningSound = new MovingSoundForceLightning(player);
                mc.getSoundManager().play(lightningSound);
            }
        });
    }
}
