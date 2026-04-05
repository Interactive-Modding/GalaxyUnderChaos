package server.galaxyunderchaos.lightsaber;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import server.galaxyunderchaos.galaxyunderchaos;

public class LightsaberFormNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel NETWORK = null;

    public static void registerPackets(FMLCommonSetupEvent event) {
        if (NETWORK != null) return;

        NETWORK = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(galaxyunderchaos.MODID, "lightsaber_form_sync"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        int id = 0;
        NETWORK.registerMessage(id++, SyncLightsaberFormPacket.class,
                SyncLightsaberFormPacket::encode,
                SyncLightsaberFormPacket::decode,
                SyncLightsaberFormPacket::handle,
                java.util.Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        NETWORK.registerMessage(id++, SwitchLightsaberFormPacket.class,
                SwitchLightsaberFormPacket::encode,
                SwitchLightsaberFormPacket::decode,
                SwitchLightsaberFormPacket::handle,
                java.util.Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK.registerMessage(id, ToggleLightsaberPacket.class,
                ToggleLightsaberPacket::encode,
                ToggleLightsaberPacket::decode,
                ToggleLightsaberPacket::handle,
                java.util.Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static void sendToServer(Object message) {
        if (Minecraft.getInstance().getConnection() == null) return;
        NETWORK.sendToServer(message);
    }

    static void sendToPlayer(ServerPlayer player, SyncLightsaberFormPacket packet) {
        NETWORK.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
