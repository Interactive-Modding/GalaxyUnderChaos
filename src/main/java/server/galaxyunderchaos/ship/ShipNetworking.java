package server.galaxyunderchaos.ship;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Optional;

public final class ShipNetworking {
    private static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel NETWORK;

    private ShipNetworking() {
    }

    public static void registerPackets(FMLCommonSetupEvent event) {
        if (NETWORK != null) {
            return;
        }
        NETWORK = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(galaxyunderchaos.MODID, "ship_controls"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        int id = 0;
        NETWORK.registerMessage(id++, ShipControlPacket.class,
                ShipControlPacket::encode,
                ShipControlPacket::decode,
                ShipControlPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(id++, ShipCustomizationColorPacket.class,
                ShipCustomizationColorPacket::encode,
                ShipCustomizationColorPacket::decode,
                ShipCustomizationColorPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(id++, HyperspaceEffectPacket.class,
                HyperspaceEffectPacket::encode,
                HyperspaceEffectPacket::decode,
                HyperspaceEffectPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendToServer(Object packet) {
        if (Minecraft.getInstance().getConnection() == null || NETWORK == null) {
            return;
        }
        NETWORK.sendToServer(packet);
    }

    public static void sendToPlayer(ServerPlayer player, Object packet) {
        if (player == null || NETWORK == null) {
            return;
        }
        NETWORK.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
