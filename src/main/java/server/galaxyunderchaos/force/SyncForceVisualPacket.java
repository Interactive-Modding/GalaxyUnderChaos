package server.galaxyunderchaos.force;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncForceVisualPacket {
    private final int entityId;
    private final String activeVisualPowerId;
    private final int visualTicks;
    private final int visualFlags;
    private final boolean usingPower;
    private final String usingPowerId;
    private final int usingTicks;

    public SyncForceVisualPacket(int entityId, ForceCapability cap) {
        this(entityId,
                cap == null ? "" : cap.getActiveVisualPowerId(),
                cap == null ? 0 : cap.getVisualTicks(),
                cap == null ? 0 : cap.getVisualFlags(),
                cap != null && cap.isUsingPower(),
                cap == null ? "" : cap.getUsingPowerId(),
                cap == null ? 0 : cap.getUsingTicks());
    }

    public SyncForceVisualPacket(int entityId,
                                 String activeVisualPowerId,
                                 int visualTicks,
                                 int visualFlags,
                                 boolean usingPower,
                                 String usingPowerId,
                                 int usingTicks) {
        this.entityId = entityId;
        this.activeVisualPowerId = activeVisualPowerId == null ? "" : activeVisualPowerId;
        this.visualTicks = Math.max(visualTicks, 0);
        this.visualFlags = Math.max(visualFlags, 0);
        this.usingPower = usingPower;
        this.usingPowerId = usingPowerId == null ? "" : usingPowerId;
        this.usingTicks = Math.max(usingTicks, 0);
    }

    public static void encode(SyncForceVisualPacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.entityId);
        buf.writeUtf(packet.activeVisualPowerId);
        buf.writeVarInt(packet.visualTicks);
        buf.writeVarInt(packet.visualFlags);
        buf.writeBoolean(packet.usingPower);
        buf.writeUtf(packet.usingPowerId);
        buf.writeVarInt(packet.usingTicks);
    }

    public static SyncForceVisualPacket decode(FriendlyByteBuf buf) {
        return new SyncForceVisualPacket(
                buf.readVarInt(),
                buf.readUtf(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readBoolean(),
                buf.readUtf(),
                buf.readVarInt());
    }

    public static void handle(SyncForceVisualPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().level == null) {
                return;
            }
            Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
            if (entity == null) {
                return;
            }
            entity.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap ->
                    cap.applyRemoteVisualState(
                            packet.activeVisualPowerId,
                            packet.visualTicks,
                            packet.visualFlags,
                            packet.usingPower,
                            packet.usingPowerId,
                            packet.usingTicks));
        }));
        context.setPacketHandled(true);
    }
}
