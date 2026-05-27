package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/** Server -> client sync for player Force identity render state. */
public class PlayerForceIdentitySyncPacket {
    private final UUID playerId;
    private final boolean hasIdentity;
    private final String forceName;
    private final String displayTitle;
    private final String speciesId;
    private final String genderId;
    private final boolean darkEyes;

    public PlayerForceIdentitySyncPacket(UUID playerId, boolean hasIdentity, String forceName, String displayTitle, String speciesId, String genderId, boolean darkEyes) {
        this.playerId = playerId == null ? new UUID(0L, 0L) : playerId;
        this.hasIdentity = hasIdentity;
        this.forceName = forceName == null ? "" : forceName;
        this.displayTitle = displayTitle == null ? "" : displayTitle;
        this.speciesId = speciesId == null ? "human_male" : speciesId;
        this.genderId = genderId == null ? "male" : genderId;
        this.darkEyes = darkEyes;
    }

    public UUID playerId() { return playerId; }
    public boolean hasIdentity() { return hasIdentity; }
    public String forceName() { return forceName; }
    public String displayTitle() { return displayTitle; }
    public String speciesId() { return speciesId; }
    public String genderId() { return genderId; }
    public boolean darkEyes() { return darkEyes; }

    public static void encode(PlayerForceIdentitySyncPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.playerId);
        buf.writeBoolean(packet.hasIdentity);
        buf.writeUtf(packet.forceName, 64);
        buf.writeUtf(packet.displayTitle, 96);
        buf.writeUtf(packet.speciesId, 160);
        buf.writeUtf(packet.genderId, 32);
        buf.writeBoolean(packet.darkEyes);
    }

    public static PlayerForceIdentitySyncPacket decode(FriendlyByteBuf buf) {
        return new PlayerForceIdentitySyncPacket(buf.readUUID(), buf.readBoolean(), buf.readUtf(64), buf.readUtf(96), buf.readUtf(160), buf.readUtf(32), buf.readBoolean());
    }

    public static void handle(PlayerForceIdentitySyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> client.renderer.forceuser.PlayerForceIdentityClientState.update(packet)));
        context.setPacketHandled(true);
    }
}
