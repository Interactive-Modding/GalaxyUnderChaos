package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetForceUseStatePacket {
    private final boolean using;

    public SetForceUseStatePacket(boolean using) {
        this.using = using;
    }

    public static void encode(SetForceUseStatePacket packet, FriendlyByteBuf buf) {
        buf.writeBoolean(packet.using);
    }

    public static SetForceUseStatePacket decode(FriendlyByteBuf buf) {
        return new SetForceUseStatePacket(buf.readBoolean());
    }

    public static void handle(SetForceUseStatePacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                if (packet.using) {
                    ForcePowerHandler.beginUsingSelectedPower(player, cap);
                } else {
                    ForcePowerHandler.stopUsingPower(player, cap);
                }
                ForceCapabilityManager.sync(player);
            });
        });
        context.setPacketHandled(true);
    }
}
