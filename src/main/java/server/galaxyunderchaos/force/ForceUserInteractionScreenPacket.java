package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Opens/refreshes the client-side Force-user interaction screen.
 *
 * The server remains authoritative: the client only renders buttons and sends
 * requested actions back through ForceUserInteractionActionPacket.
 */
public class ForceUserInteractionScreenPacket {
    private final int entityId;
    private final String mode;
    private final String title;
    private final String subtitle;
    private final String currentOrder;
    private final List<String> lines;
    private final List<String> choiceTexts;
    private final List<String> choiceSides;
    private final List<Integer> choicePoints;

    public ForceUserInteractionScreenPacket(int entityId, String mode, String title, String subtitle, String currentOrder,
                                            List<String> lines, List<String> choiceTexts, List<String> choiceSides,
                                            List<Integer> choicePoints) {
        this.entityId = entityId;
        this.mode = mode;
        this.title = title;
        this.subtitle = subtitle;
        this.currentOrder = currentOrder;
        this.lines = safe(lines);
        this.choiceTexts = safe(choiceTexts);
        this.choiceSides = safe(choiceSides);
        this.choicePoints = safeIntegers(choicePoints);
    }

    public int entityId() {
        return entityId;
    }

    public String mode() {
        return mode;
    }

    public String title() {
        return title;
    }

    public String subtitle() {
        return subtitle;
    }

    public String currentOrder() {
        return currentOrder;
    }

    public List<String> lines() {
        return lines;
    }

    public List<String> choiceTexts() {
        return choiceTexts;
    }

    public List<String> choiceSides() {
        return choiceSides;
    }

    public List<Integer> choicePoints() {
        return choicePoints;
    }

    public static void encode(ForceUserInteractionScreenPacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.entityId);
        buf.writeUtf(packet.mode);
        buf.writeUtf(packet.title);
        buf.writeUtf(packet.subtitle);
        buf.writeUtf(packet.currentOrder);
        writeStrings(buf, packet.lines);
        writeStrings(buf, packet.choiceTexts);
        writeStrings(buf, packet.choiceSides);
        buf.writeVarInt(packet.choicePoints.size());
        for (Integer value : packet.choicePoints) {
            buf.writeVarInt(value == null ? 0 : value);
        }
    }

    public static ForceUserInteractionScreenPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        String mode = buf.readUtf();
        String title = buf.readUtf();
        String subtitle = buf.readUtf();
        String currentOrder = buf.readUtf();
        List<String> lines = readStrings(buf);
        List<String> choiceTexts = readStrings(buf);
        List<String> choiceSides = readStrings(buf);
        int pointCount = buf.readVarInt();
        List<Integer> choicePoints = new ArrayList<>();
        for (int i = 0; i < pointCount; i++) {
            choicePoints.add(buf.readVarInt());
        }
        return new ForceUserInteractionScreenPacket(entityId, mode, title, subtitle, currentOrder, lines, choiceTexts, choiceSides, choicePoints);
    }

    public static void handle(ForceUserInteractionScreenPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> client.screen.ForceUserInteractionScreen.open(packet)));
        context.setPacketHandled(true);
    }

    private static void writeStrings(FriendlyByteBuf buf, List<String> values) {
        buf.writeVarInt(values.size());
        for (String value : values) {
            buf.writeUtf(value == null ? "" : value, 32767);
        }
    }

    private static List<String> readStrings(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<String> values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            values.add(buf.readUtf(32767));
        }
        return values;
    }

    private static List<String> safe(List<String> values) {
        return values == null ? List.of() : List.copyOf(values);
    }

    private static List<Integer> safeIntegers(List<Integer> values) {
        return values == null ? List.of() : List.copyOf(values);
    }
}
