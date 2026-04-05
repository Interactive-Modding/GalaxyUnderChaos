package server.galaxyunderchaos.lightsaber;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import server.galaxyunderchaos.lightsaber.LightsaberFormNetworking;
import server.galaxyunderchaos.lightsaber.SyncLightsaberFormPacket;

import java.util.List;

public class LightsaberFormCapabilityManager {
    public static void syncCapability(ServerPlayer player) {
        player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).ifPresent(cap -> {
            LightsaberFormNetworking.sendToPlayer(player, new SyncLightsaberFormPacket(cap.getSelectedForm()));
            LightsaberFormEffects.applyEffects(player, cap.getSelectedForm());
        });
    }

    public static void unlockForm(ServerPlayer player, String form) {
        player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).ifPresent(cap -> {
            cap.unlockForm(form);
            syncCapability(player);
        });
    }

    public static void changeForm(ServerPlayer player, String form) {
        player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).ifPresent(cap -> {
            if (cap.hasForm(form)) {
                cap.setSelectedForm(form);
                syncCapability(player);
            }
        });
    }
}
