package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Opens the client renunciation confirmation screen. */
public class ForceRenounceScreenPacket {
    private final String targetSide;
    private final String fromSide;
    private final String sourceName;
    private final String title;
    private final String body;
    private final String confirmLabel;
    private final String cancelLabel;

    public ForceRenounceScreenPacket(String targetSide, String fromSide, String sourceName, String title, String body, String confirmLabel, String cancelLabel) {
        this.targetSide = targetSide == null ? "UNIVERSAL" : targetSide;
        this.fromSide = fromSide == null ? "UNIVERSAL" : fromSide;
        this.sourceName = sourceName == null ? "" : sourceName;
        this.title = title == null ? "Renounce" : title;
        this.body = body == null ? "" : body;
        this.confirmLabel = confirmLabel == null ? "Confirm" : confirmLabel;
        this.cancelLabel = cancelLabel == null ? "Cancel" : cancelLabel;
    }

    public String targetSide() { return targetSide; }
    public String fromSide() { return fromSide; }
    public String sourceName() { return sourceName; }
    public String title() { return title; }
    public String body() { return body; }
    public String confirmLabel() { return confirmLabel; }
    public String cancelLabel() { return cancelLabel; }

    public static void encode(ForceRenounceScreenPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.targetSide, 32);
        buf.writeUtf(packet.fromSide, 32);
        buf.writeUtf(packet.sourceName, 96);
        buf.writeUtf(packet.title, 128);
        buf.writeUtf(packet.body, 4096);
        buf.writeUtf(packet.confirmLabel, 128);
        buf.writeUtf(packet.cancelLabel, 128);
    }

    public static ForceRenounceScreenPacket decode(FriendlyByteBuf buf) {
        return new ForceRenounceScreenPacket(buf.readUtf(32), buf.readUtf(32), buf.readUtf(96), buf.readUtf(128), buf.readUtf(4096), buf.readUtf(128), buf.readUtf(128));
    }

    public static void handle(ForceRenounceScreenPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> client.screen.ForceRenounceScreen.open(packet)));
        context.setPacketHandled(true);
    }
}
