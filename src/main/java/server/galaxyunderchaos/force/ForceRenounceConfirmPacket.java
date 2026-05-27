package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Client confirmation for a pending renunciation. */
public class ForceRenounceConfirmPacket {
    private final String targetSide;
    private final boolean confirm;

    public ForceRenounceConfirmPacket(String targetSide, boolean confirm) {
        this.targetSide = targetSide == null ? "UNIVERSAL" : targetSide;
        this.confirm = confirm;
    }

    public static void encode(ForceRenounceConfirmPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.targetSide, 32);
        buf.writeBoolean(packet.confirm);
    }

    public static ForceRenounceConfirmPacket decode(FriendlyByteBuf buf) {
        return new ForceRenounceConfirmPacket(buf.readUtf(32), buf.readBoolean());
    }

    public static void handle(ForceRenounceConfirmPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            if (!packet.confirm) {
                ForceRenunciationManager.clear(player);
                return;
            }
            ForceRenunciationManager.confirm(player, ForceRenunciationManager.parse(packet.targetSide, ForceSide.UNIVERSAL));
        });
        context.setPacketHandled(true);
    }
}
