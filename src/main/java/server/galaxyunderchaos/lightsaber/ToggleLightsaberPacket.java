package server.galaxyunderchaos.lightsaber;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import server.galaxyunderchaos.item.LightsaberItem;

import java.util.function.Supplier;

public class ToggleLightsaberPacket {
    public ToggleLightsaberPacket() {}

    public static void encode(ToggleLightsaberPacket packet, FriendlyByteBuf buf) {}

    public static ToggleLightsaberPacket decode(FriendlyByteBuf buf) {
        return new ToggleLightsaberPacket();
    }

    public static void handle(ToggleLightsaberPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }

            ItemStack preferred = player.getMainHandItem();
            ItemStack fallback = player.getOffhandItem();

            if (preferred.getItem() instanceof LightsaberItem saber) {
                saber.setActive(preferred, !saber.isActive(preferred), player.level(), player);
                return;
            }
            if (fallback.getItem() instanceof LightsaberItem saber) {
                saber.setActive(fallback, !saber.isActive(fallback), player.level(), player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
