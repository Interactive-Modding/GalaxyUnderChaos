package server.galaxyunderchaos.ship;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import server.galaxyunderchaos.entity.NovadiveEntity;
import server.galaxyunderchaos.entity.FlashfireEntity;

import java.util.function.Supplier;

public class ShipControlPacket {
    private final int controls;

    public ShipControlPacket(int controls) {
        this.controls = controls;
    }

    public static void encode(ShipControlPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.controls);
    }

    public static ShipControlPacket decode(FriendlyByteBuf buffer) {
        return new ShipControlPacket(buffer.readVarInt());
    }

    public static void handle(ShipControlPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.getVehicle() instanceof NovadiveEntity ship && ship.isPilot(player)) {
                ship.setControls(packet.controls);
            }
            if (player != null && player.getVehicle() instanceof FlashfireEntity ship && ship.isPilot(player)) {
                ship.setControls(packet.controls);
            }
        });
        context.setPacketHandled(true);
    }
}
