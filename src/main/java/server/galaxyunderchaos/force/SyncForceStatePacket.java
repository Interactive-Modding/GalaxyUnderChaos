package server.galaxyunderchaos.force;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncForceStatePacket {
    private final CompoundTag tag;

    public SyncForceStatePacket(CompoundTag tag) {
        this.tag = tag == null ? new CompoundTag() : tag.copy();
    }

    public static void encode(SyncForceStatePacket packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.tag);
    }

    public static SyncForceStatePacket decode(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        return new SyncForceStatePacket(tag == null ? new CompoundTag() : tag);
    }

    public static void handle(SyncForceStatePacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().player == null) {
                return;
            }
            Minecraft.getInstance().player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> cap.deserializeNBT(packet.tag));
        }));
        context.setPacketHandled(true);
    }
}
