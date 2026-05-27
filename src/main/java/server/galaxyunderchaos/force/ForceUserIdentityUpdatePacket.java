package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.entity.forceuser.ForceUserInteractionMenu;
import server.galaxyunderchaos.entity.forceuser.PlayerForceIdentity;

import java.util.function.Supplier;

/** Saves the player's Force identity from the button/text GUI. */
public class ForceUserIdentityUpdatePacket {
    private final int entityId;
    private final String forceName;
    private final String speciesId;

    public ForceUserIdentityUpdatePacket(int entityId, String forceName, String speciesId) {
        this.entityId = entityId;
        this.forceName = forceName == null ? "" : forceName;
        this.speciesId = speciesId == null ? "mirialan_male|jedi_robes|mirialan_male|blue|C68642" : speciesId;
    }

    public static void encode(ForceUserIdentityUpdatePacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.entityId);
        buf.writeUtf(packet.forceName, 64);
        buf.writeUtf(packet.speciesId, 160);
    }

    public static ForceUserIdentityUpdatePacket decode(FriendlyByteBuf buf) {
        return new ForceUserIdentityUpdatePacket(buf.readVarInt(), buf.readUtf(64), buf.readUtf(160));
    }

    public static void handle(ForceUserIdentityUpdatePacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }

            Entity entity = player.level().getEntity(packet.entityId);
            if (!(entity instanceof ForceUserEntity npc) || !npc.isAlive()) {
                player.displayClientMessage(Component.literal("That Force-user is no longer here."), true);
                return;
            }
            if (!npc.isBoundTo(player)) {
                player.displayClientMessage(Component.literal("Only a bonded master or apprentice can record your Force identity."), true);
                return;
            }
            if (npc.distanceToSqr(player) > 1024.0D) {
                player.displayClientMessage(Component.literal("That Force-user is too far away to record your identity."), true);
                return;
            }

            boolean wasLocked = PlayerForceIdentity.hasCustomIdentity(player);
            PlayerForceIdentity.setIdentity(player, packet.forceName, packet.speciesId);
            if (wasLocked) {
                player.displayClientMessage(Component.literal("Updated Force name/robes for " + PlayerForceIdentity.getDisplayTitle(player) + ". Species/details remain locked."), false);
            } else {
                player.displayClientMessage(Component.literal("Your Force identity is now " + PlayerForceIdentity.getDisplayTitle(player) + " (" + PlayerForceIdentity.getSpeciesDisplayName(player) + ", " + PlayerForceIdentity.getRobeDisplayName(player) + "). Species/details are now locked unless an admin resets them."), false);
            }
            ForceUserInteractionMenu.sendIdentity(player, npc);
        });
        context.setPacketHandled(true);
    }
}
