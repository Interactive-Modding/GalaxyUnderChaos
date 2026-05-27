package server.galaxyunderchaos.force;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Optional;

public final class ForceNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel NETWORK;

    private ForceNetworking() {}

    public static void registerPackets(FMLCommonSetupEvent event) {
        if (NETWORK != null) {
            return;
        }
        NETWORK = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(galaxyunderchaos.MODID, "force_sync"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        int id = 0;
        NETWORK.registerMessage(id++, SyncForceStatePacket.class,
                SyncForceStatePacket::encode,
                SyncForceStatePacket::decode,
                SyncForceStatePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        NETWORK.registerMessage(id++, CycleForcePowerPacket.class,
                CycleForcePowerPacket::encode,
                CycleForcePowerPacket::decode,
                CycleForcePowerPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK.registerMessage(id++, UseForcePowerPacket.class,
                UseForcePowerPacket::encode,
                UseForcePowerPacket::decode,
                UseForcePowerPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK.registerMessage(id++, SetForceUseStatePacket.class,
                SetForceUseStatePacket::encode,
                SetForceUseStatePacket::decode,
                SetForceUseStatePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK.registerMessage(id++, HolocronPowerActionPacket.class,
                HolocronPowerActionPacket::encode,
                HolocronPowerActionPacket::decode,
                HolocronPowerActionPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK.registerMessage(id++, ForceUserInteractionScreenPacket.class,
                ForceUserInteractionScreenPacket::encode,
                ForceUserInteractionScreenPacket::decode,
                ForceUserInteractionScreenPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        NETWORK.registerMessage(id++, ForceUserInteractionActionPacket.class,
                ForceUserInteractionActionPacket::encode,
                ForceUserInteractionActionPacket::decode,
                ForceUserInteractionActionPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK.registerMessage(id++, ForceUserIdentityUpdatePacket.class,
                ForceUserIdentityUpdatePacket::encode,
                ForceUserIdentityUpdatePacket::decode,
                ForceUserIdentityUpdatePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK.registerMessage(id++, ForceRenounceScreenPacket.class,
                ForceRenounceScreenPacket::encode,
                ForceRenounceScreenPacket::decode,
                ForceRenounceScreenPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        NETWORK.registerMessage(id++, ForceRenounceConfirmPacket.class,
                ForceRenounceConfirmPacket::encode,
                ForceRenounceConfirmPacket::decode,
                ForceRenounceConfirmPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK.registerMessage(id++, PlayerForceIdentitySyncPacket.class,
                PlayerForceIdentitySyncPacket::encode,
                PlayerForceIdentitySyncPacket::decode,
                PlayerForceIdentitySyncPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        NETWORK.registerMessage(id++, SyncForceVisualPacket.class,
                SyncForceVisualPacket::encode,
                SyncForceVisualPacket::decode,
                SyncForceVisualPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        NETWORK.registerMessage(id, BleedingTableLightningPacket.class,
                BleedingTableLightningPacket::encode,
                BleedingTableLightningPacket::decode,
                BleedingTableLightningPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static void sendToServer(Object packet) {
        NETWORK.sendToServer(packet);
    }

    public static void sendToPlayer(ServerPlayer player, SyncForceStatePacket packet) {
        NETWORK.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendVisualToTracking(ServerPlayer player, SyncForceVisualPacket packet) {
        NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), packet);
    }

    public static void sendForceUserScreen(ServerPlayer player, ForceUserInteractionScreenPacket packet) {
        if (NETWORK != null && player != null) {
            NETWORK.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
    }

    public static void sendRenounceScreen(ServerPlayer player, ForceRenounceScreenPacket packet) {
        if (NETWORK != null && player != null) {
            NETWORK.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
    }

    public static void sendIdentityToTracking(ServerPlayer player, PlayerForceIdentitySyncPacket packet) {
        if (NETWORK != null && player != null) {
            NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), packet);
        }
    }
}
