package server.galaxyunderchaos.ship;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Starts the client-side hyperspace overlay for the pilot/passenger receiving this packet. */
public class HyperspaceEffectPacket {
    private final int durationTicks;

    public HyperspaceEffectPacket(int durationTicks) {
        this.durationTicks = Math.max(1, durationTicks);
    }

    public static void encode(HyperspaceEffectPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.durationTicks);
    }

    public static HyperspaceEffectPacket decode(FriendlyByteBuf buffer) {
        return new HyperspaceEffectPacket(buffer.readVarInt());
    }

    public static void handle(HyperspaceEffectPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> client.renderer.HyperspaceOverlay.startWarpEffect(packet.durationTicks)));
        context.setPacketHandled(true);
    }
}
