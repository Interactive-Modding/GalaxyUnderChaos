package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForceNetworking;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.force.ForceUserInteractionScreenPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Server-side interaction bridge for bonded Force-user companions.
 *
 * Normal use now opens a real client screen with buttons. The older chat command
 * paths remain as fallback/debug entry points so existing worlds and commands do
 * not break if a client packet is missed.
 */
public final class ForceUserInteractionMenu {
    private static final String ROOT = "GUCForceUserMenu";
    private static final String LAST_ENTITY = "LastEntity";

    private ForceUserInteractionMenu() {}

    public static void openMain(ServerPlayer player, ForceUserEntity npc) {
        remember(player, npc);
        sendMain(player, npc);
    }

    public static int openMain(ServerPlayer player) {
        ForceUserEntity npc = getRemembered(player);
        if (npc == null) {
            player.displayClientMessage(Component.literal("No recent Force-user selected. Right-click a bonded mentor or student first."), true);
            return 0;
        }
        sendMain(player, npc);
        return 1;
    }

    public static int openInteract(ServerPlayer player) {
        ForceUserEntity npc = getRemembered(player);
        if (npc == null) {
            player.displayClientMessage(Component.literal("No recent Force-user selected. Right-click a bonded mentor or student first."), true);
            return 0;
        }
        sendInteract(player, npc);
        return 1;
    }

    public static int openOrder(ServerPlayer player) {
        ForceUserEntity npc = getRemembered(player);
        if (npc == null) {
            player.displayClientMessage(Component.literal("No recent Force-user selected. Right-click a bonded mentor or student first."), true);
            return 0;
        }
        sendOrder(player, npc);
        return 1;
    }

    public static int setOrder(ServerPlayer player, String orderName) {
        ForceUserEntity npc = getRemembered(player);
        if (npc == null) {
            player.displayClientMessage(Component.literal("No recent Force-user selected. Right-click a bonded mentor or student first."), true);
            return 0;
        }
        ForceUserEntity.CompanionOrder order = switch (orderName.toLowerCase()) {
            case "stay" -> ForceUserEntity.CompanionOrder.STAY;
            case "wander", "wonder" -> ForceUserEntity.CompanionOrder.WANDER;
            default -> ForceUserEntity.CompanionOrder.FOLLOW_DEFEND;
        };
        npc.setCompanionOrder(order);
        sendOrder(player, npc);
        return 1;
    }

    public static int converse(ServerPlayer player) {
        ForceUserEntity npc = getRemembered(player);
        if (npc == null) {
            player.displayClientMessage(Component.literal("No recent Force-user selected. Right-click a bonded mentor or student first."), true);
            return 0;
        }
        sendConversation(player, npc);
        return 1;
    }

    public static int openQuestColumn(ServerPlayer player) {
        ForceUserEntity npc = getRemembered(player);
        if (npc == null) {
            player.displayClientMessage(Component.literal("No recent Force-user selected. Right-click a bonded mentor or student first."), true);
            return 0;
        }
        sendQuests(player, npc);
        return 1;
    }

    public static void sendMain(ServerPlayer player, ForceUserEntity npc) {
        List<String> lines = List.of(
                "Choose an action.",
                "Interact: conversation and quests.",
                "Order: companion behavior.",
                "Identity: appearance and title."
        );
        send(player, npc, "main", lines, List.of(), List.of(), List.of());
    }

    public static void sendInteract(ServerPlayer player, ForceUserEntity npc) {
        List<String> lines = new ArrayList<>();
        lines.add("Conversation: dialogue choices.");
        lines.add("Quests: active Force tasks.");
        lines.add("Identity: appearance and title.");
        send(player, npc, "interact", lines, List.of(), List.of(), List.of());
    }

    public static void sendIdentity(ServerPlayer player, ForceUserEntity npc) {
        List<String> lines = new ArrayList<>();
        boolean locked = PlayerForceIdentity.hasCustomIdentity(player);
        if (locked) {
            lines.add("Identity is locked: species/details require /force identity reset <player>.");
            lines.add("You may still change your Force name and current-side robes.");
        } else {
            lines.add("Choose carefully: species/details are saved once.");
            lines.add("After saving, only name/robes change unless admin reset.");
        }
        lines.add("Display title: " + PlayerForceIdentity.getDisplayTitle(player));
        lines.add("Current name: " + PlayerForceIdentity.getForceName(player));
        String identityId = PlayerForceIdentity.getSpeciesId(player);
        lines.add("Identity locked: " + locked);
        lines.add("Current force side: " + committedSide(player).name().toLowerCase(java.util.Locale.ROOT));
        lines.add("Current species id: " + PlayerForceIdentity.speciesPart(identityId));
        lines.add("Current robe id: " + PlayerForceIdentity.robePart(identityId));
        lines.add("Current texture id: " + PlayerForceIdentity.texturePart(identityId));
        lines.add("Current eye id: " + PlayerForceIdentity.eyePart(identityId));
        lines.add("Current skin hex: " + PlayerForceIdentity.skinPart(identityId));
        lines.add("Species: " + PlayerForceIdentity.getSpeciesDisplayName(player));
        lines.add("Robe: " + PlayerForceIdentity.getRobeDisplayName(player));
        lines.add("Variant: " + PlayerForceIdentity.getTextureDisplayName(player));
        lines.add("Eyes: " + PlayerForceIdentity.getEyeDisplayName(player));
        lines.add("Skin: #" + PlayerForceIdentity.getSkinHex(player));
        send(player, npc, "identity", lines, List.of(), List.of(), List.of());
    }

    public static void sendOrder(ServerPlayer player, ForceUserEntity npc) {
        List<String> lines = List.of(
                "Current order: " + npc.getCompanionOrder().displayName(),
                "Stay: hold position and defend only when threatened.",
                "Wander: stay bonded and persistent, but roam locally.",
                "Follow & Defend: follow you, protect you, and board ships with you."
        );
        send(player, npc, "order", lines, List.of(), List.of(), List.of());
    }

    public static void sendConversation(ServerPlayer player, ForceUserEntity npc) {
        ForceUserDialogueEngine.DialogueScreenPayload payload = ForceUserDialogueEngine.buildScreenPayload(npc, player, npc.isForceMentorBond());
        List<String> choices = new ArrayList<>();
        List<String> sides = new ArrayList<>();
        List<Integer> points = new ArrayList<>();
        for (ForceQuestInventory.DialogueChoice choice : payload.choices()) {
            choices.add(choice.text());
            sides.add(choice.side().name());
            points.add(choice.points());
        }
        send(player, npc, "conversation", payload.lines(), choices, sides, points);
    }

    public static void sendQuests(ServerPlayer player, ForceUserEntity npc) {
        List<String> lines = ForceQuestInventory.buildQuestColumnLines(player, npc);
        send(player, npc, "quests", lines, List.of(), List.of(), List.of());
    }

    private static void send(ServerPlayer player, ForceUserEntity npc, String mode, List<String> lines,
                             List<String> choiceTexts, List<String> choiceSides, List<Integer> choicePoints) {
        remember(player, npc);
        ForceNetworking.sendForceUserScreen(player, new ForceUserInteractionScreenPacket(
                npc.getId(),
                mode,
                npc.getDisplayName().getString(),
                npc.getRankDisplayName() + " | " + sideLabel(npc.getForceUserSide()) + " | " + npc.getAlignmentLeaningLabel(),
                npc.getCompanionOrder().displayName(),
                lines,
                choiceTexts,
                choiceSides,
                choicePoints
        ));
    }

    private static void remember(ServerPlayer player, ForceUserEntity npc) {
        CompoundTag root = root(player);
        root.putUUID(LAST_ENTITY, npc.getUUID());
    }

    private static ForceUserEntity getRemembered(ServerPlayer player) {
        CompoundTag root = root(player);
        if (!root.hasUUID(LAST_ENTITY)) {
            return null;
        }
        UUID id = root.getUUID(LAST_ENTITY);
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return null;
        }
        Entity entity = serverLevel.getEntity(id);
        if (!(entity instanceof ForceUserEntity npc) || !npc.isAlive()) {
            return null;
        }
        if (!npc.isBoundTo(player)) {
            player.displayClientMessage(Component.literal("That Force-user is not bonded to you."), true);
            return null;
        }
        if (npc.distanceToSqr(player) > 1024.0D) {
            player.displayClientMessage(Component.literal("That Force-user is too far away to receive orders."), true);
            return null;
        }
        return npc;
    }

    private static CompoundTag root(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT)) {
            persistent.put(ROOT, new CompoundTag());
        }
        return persistent.getCompound(ROOT);
    }

    private static ForceSide committedSide(ServerPlayer player) {
        return player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .filter(ForceSide::isSelectableBranch)
                .orElse(ForceSide.LIGHT);
    }

    private static String sideLabel(ForceUserSide side) {
        return switch (side) {
            case DARK -> "Sith";
            case NEUTRAL -> "Neutral";
            case LIGHT -> "Jedi";
        };
    }

    /** Fallback-only chat button helper retained for old command/debug flows. */
    private static MutableComponent button(String label, String command, String hover) {
        return Component.literal(label).withStyle(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(hover))));
    }
}
