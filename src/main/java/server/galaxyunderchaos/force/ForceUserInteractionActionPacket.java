package server.galaxyunderchaos.force;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import server.galaxyunderchaos.entity.forceuser.ForceQuestInventory;
import server.galaxyunderchaos.entity.forceuser.ForceUserDialogueEngine;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.entity.forceuser.ForceUserInteractionMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Client button -> server action packet for Force-user menus. */
public class ForceUserInteractionActionPacket {
    private final int entityId;
    private final String action;
    private final int value;

    public ForceUserInteractionActionPacket(int entityId, String action, int value) {
        this.entityId = entityId;
        this.action = action == null ? "main" : action;
        this.value = value;
    }

    public static void encode(ForceUserInteractionActionPacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.entityId);
        buf.writeUtf(packet.action);
        buf.writeVarInt(packet.value);
    }

    public static ForceUserInteractionActionPacket decode(FriendlyByteBuf buf) {
        return new ForceUserInteractionActionPacket(buf.readVarInt(), buf.readUtf(), buf.readVarInt());
    }

    public static void handle(ForceUserInteractionActionPacket packet, Supplier<NetworkEvent.Context> supplier) {
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
                player.displayClientMessage(Component.literal("That Force-user is not bonded to you."), true);
                return;
            }
            if (npc.distanceToSqr(player) > 1024.0D) {
                player.displayClientMessage(Component.literal("That Force-user is too far away to receive orders."), true);
                return;
            }

            switch (packet.action.toLowerCase()) {
                case "interact" -> ForceUserInteractionMenu.sendInteract(player, npc);
                case "order" -> ForceUserInteractionMenu.sendOrder(player, npc);
                case "identity" -> ForceUserInteractionMenu.sendIdentity(player, npc);
                case "quests" -> ForceUserInteractionMenu.sendQuests(player, npc);
                case "conversation", "converse" -> ForceUserInteractionMenu.sendConversation(player, npc);
                case "choice" -> {
                    ForceQuestInventory.chooseDialogueOption(player, Math.max(1, Math.min(3, packet.value)));
                    ForceUserInteractionMenu.sendInteract(player, npc);
                }
                case "stay" -> {
                    npc.setCompanionOrder(ForceUserEntity.CompanionOrder.STAY);
                    ForceUserInteractionMenu.sendOrder(player, npc);
                }
                case "wander", "wonder" -> {
                    npc.setCompanionOrder(ForceUserEntity.CompanionOrder.WANDER);
                    ForceUserInteractionMenu.sendOrder(player, npc);
                }
                case "follow", "follow_defend" -> {
                    npc.setCompanionOrder(ForceUserEntity.CompanionOrder.FOLLOW_DEFEND);
                    ForceUserInteractionMenu.sendOrder(player, npc);
                }
                default -> ForceUserInteractionMenu.sendMain(player, npc);
            }
        });
        context.setPacketHandled(true);
    }
}
