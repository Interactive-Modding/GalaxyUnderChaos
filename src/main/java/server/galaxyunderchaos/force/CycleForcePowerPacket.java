package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CycleForcePowerPacket {
    public static void encode(CycleForcePowerPacket packet, FriendlyByteBuf buf) {
    }

    public static CycleForcePowerPacket decode(FriendlyByteBuf buf) {
        return new CycleForcePowerPacket();
    }

    public static void handle(CycleForcePowerPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                ForcePower next = cap.cycleSelectedPower();
                if (next != null) {
                    player.displayClientMessage(Component.literal("Selected Force power: " + next.displayName()), true);
                    ForceCapabilityManager.sync(player);
                }
            });
        });
        context.setPacketHandled(true);
    }
}
