package server.galaxyunderchaos.ship;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import server.galaxyunderchaos.menu.ShipCraftingTableMenu;

import java.util.function.Supplier;

public class ShipCustomizationColorPacket {
    private final int sectionOrdinal;
    private final int color;

    public ShipCustomizationColorPacket(ShipColorSection section, int color) {
        this(section.ordinal(), color);
    }

    private ShipCustomizationColorPacket(int sectionOrdinal, int color) {
        this.sectionOrdinal = sectionOrdinal;
        this.color = ShipCustomization.clampColor(color);
    }

    public static void encode(ShipCustomizationColorPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.sectionOrdinal);
        buffer.writeVarInt(packet.color);
    }

    public static ShipCustomizationColorPacket decode(FriendlyByteBuf buffer) {
        return new ShipCustomizationColorPacket(buffer.readVarInt(), buffer.readVarInt());
    }

    public static void handle(ShipCustomizationColorPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu instanceof ShipCraftingTableMenu menu) {
                menu.setColor(ShipColorSection.byOrdinal(packet.sectionOrdinal), packet.color);
            }
        });
        context.setPacketHandled(true);
    }
}
