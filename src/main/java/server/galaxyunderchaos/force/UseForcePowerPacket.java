package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UseForcePowerPacket {
    public static void encode(UseForcePowerPacket packet, FriendlyByteBuf buf) {
    }

    public static UseForcePowerPacket decode(FriendlyByteBuf buf) {
        return new UseForcePowerPacket();
    }

    public static void handle(UseForcePowerPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && ForcePowerHandler.useSelectedPower(player)) {
                ForceCapabilityManager.sync(player);
            }
        });
        context.setPacketHandled(true);
    }
}
