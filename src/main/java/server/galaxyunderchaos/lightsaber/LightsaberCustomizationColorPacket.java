package server.galaxyunderchaos.lightsaber;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import server.galaxyunderchaos.menu.LightsaberCraftingTableMenu;

import java.util.function.Supplier;

public class LightsaberCustomizationColorPacket {
    private final int partOrdinal;
    private final int color;

    public LightsaberCustomizationColorPacket(LightsaberPartType part, int color) {
        this(part.ordinal(), color);
    }

    private LightsaberCustomizationColorPacket(int partOrdinal, int color) {
        this.partOrdinal = partOrdinal;
        this.color = ModularLightsaberData.clampPartColor(color);
    }

    public static void encode(LightsaberCustomizationColorPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.partOrdinal);
        buffer.writeVarInt(packet.color);
    }

    public static LightsaberCustomizationColorPacket decode(FriendlyByteBuf buffer) {
        return new LightsaberCustomizationColorPacket(buffer.readVarInt(), buffer.readVarInt());
    }

    public static void handle(LightsaberCustomizationColorPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu instanceof LightsaberCraftingTableMenu menu) {
                LightsaberPartType[] values = LightsaberPartType.values();
                int ordinal = Math.max(0, Math.min(values.length - 1, packet.partOrdinal));
                menu.setPartColor(values[ordinal], packet.color);
            }
        });
        context.setPacketHandled(true);
    }
}
