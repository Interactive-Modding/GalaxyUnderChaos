package client.renderer.lightsaber;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.sound.ModSounds;

/**
 * Plays lightsaber swing sounds when the player swings an active lightsaber.
 * This covers air swings (no target hit) which previously had no sound.
 */
@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LightsaberSwingSoundHandler {

    private static float lastSwingProgress;
    private static int cooldownTicks;

    private LightsaberSwingSoundHandler() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        if (cooldownTicks > 0) {
            cooldownTicks--;
        }

        float currentSwing = player.getAttackAnim(0.0F);

        if (currentSwing > 0.04F && lastSwingProgress < 0.02F && cooldownTicks <= 0) {
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();

            boolean mainActive = mainHand.getItem() instanceof LightsaberItem saber && saber.isActive(mainHand);
            boolean offActive = offHand.getItem() instanceof LightsaberItem saber2 && saber2.isActive(offHand);

            if (mainActive || offActive) {
                float pitch = 0.85F + player.getRandom().nextFloat() * 0.30F;
                player.level().playLocalSound(
                        player.getX(), player.getY(), player.getZ(),
                        ModSounds.LIGHTSABER_SWING.get(),
                        SoundSource.PLAYERS,
                        0.60F,
                        pitch,
                        false
                );
                cooldownTicks = 8;
            }
        }

        lastSwingProgress = currentSwing;
    }
}
