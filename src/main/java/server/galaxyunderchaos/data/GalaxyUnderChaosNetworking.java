//package server.galaxyunderchaos.data;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.network.Connection;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.network.ChannelBuilder;
//import net.minecraftforge.network.SimpleChannel;
//import server.galaxyunderchaos.force.CycleForcePowerPacket;
//import server.galaxyunderchaos.force.UseForcePowerPacket;
//import server.galaxyunderchaos.galaxyunderchaos;
//
//public class GalaxyUnderChaosNetworking {
//    public static SimpleChannel NETWORK = null;
//
//    public static void registerPackets() {
//        if (NETWORK != null) return;
//
//        NETWORK = ChannelBuilder
//                .named(new ResourceLocation(galaxyunderchaos.MODID, "force_power_sync"))
//                .clientAcceptedVersions((version, netVersion) -> true)
//                .serverAcceptedVersions((version, netVersion) -> true)
//                .simpleChannel();
//
//        NETWORK.messageBuilder(CycleForcePowerPacket.class, 0)
//                .encoder(CycleForcePowerPacket::encode)
//                .decoder(CycleForcePowerPacket::decode)
//                .consumerMainThread(CycleForcePowerPacket::handle)
//                .add();
//
//        NETWORK.messageBuilder(UseForcePowerPacket.class, 1)
//                .encoder(UseForcePowerPacket::encode)
//                .decoder(UseForcePowerPacket::decode)
//                .consumerMainThread(UseForcePowerPacket::handle)
//                .add();
//
//        NETWORK.build();
//    }
//
//    public static void sendToServer(Object message) {
//        if (NETWORK == null || Minecraft.getInstance().getConnection() == null) return;
//        Connection connection = Minecraft.getInstance().getConnection().getConnection();
//        NETWORK.send(message, connection);
//    }
//}
