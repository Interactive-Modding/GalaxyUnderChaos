package server.galaxyunderchaos.force;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import server.galaxyunderchaos.entity.forceuser.ForceTrainingManager;
import server.galaxyunderchaos.entity.forceuser.PlayerForceIdentity;

/** Server-authoritative side renunciation flow. */
public final class ForceRenunciationManager {
    private static final String ROOT = "GUCRenunciation";
    private static final String TARGET_SIDE = "TargetSide";
    private static final String FROM_SIDE = "FromSide";
    private static final String TIME = "Time";
    private static final String SOURCE = "Source";
    private static final String POS = "Pos";
    private static final long WINDOW_TICKS = 20L * 90L;

    private ForceRenunciationManager() {}

    public static void request(ServerPlayer player, ForceSide targetSide, ForceSide fromSide, BlockPos pos, String sourceName) {
        if (player == null || targetSide == null || targetSide == ForceSide.UNIVERSAL) {
            return;
        }
        ForceSide current = fromSide == null || fromSide == ForceSide.UNIVERSAL ? currentSide(player) : fromSide;
        if (current == ForceSide.UNIVERSAL || current == targetSide) {
            return;
        }

        CompoundTag tag = root(player);
        tag.putString(TARGET_SIDE, targetSide.name());
        tag.putString(FROM_SIDE, current.name());
        tag.putLong(TIME, player.level().getGameTime());
        tag.putString(SOURCE, sourceName == null || sourceName.isBlank() ? "the Force" : sourceName);
        tag.putLong(POS, pos == null ? BlockPos.ZERO.asLong() : pos.asLong());

        ForceNetworking.sendRenounceScreen(player, new ForceRenounceScreenPacket(
                targetSide.name(),
                current.name(),
                tag.getString(SOURCE),
                titleFor(targetSide),
                bodyFor(current, targetSide),
                confirmLabel(targetSide),
                cancelLabel(current)
        ));
    }

    public static boolean confirm(ServerPlayer player, ForceSide targetSide) {
        if (player == null || targetSide == null || targetSide == ForceSide.UNIVERSAL) {
            return false;
        }
        CompoundTag tag = root(player);
        if (!tag.contains(TARGET_SIDE)) {
            player.displayClientMessage(Component.literal("There is no active renunciation to confirm."), true);
            return false;
        }
        ForceSide pendingTarget = parse(tag.getString(TARGET_SIDE), ForceSide.UNIVERSAL);
        ForceSide fromSide = parse(tag.getString(FROM_SIDE), currentSide(player));
        long started = tag.getLong(TIME);
        if (pendingTarget != targetSide || player.level().getGameTime() > started + WINDOW_TICKS) {
            clear(player);
            player.displayClientMessage(Component.literal("That renunciation has faded. Speak with a teacher or holocron again."), true);
            return false;
        }

        ForceTrainingManager.clearProgressForSide(player, fromSide);
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.renounceAndCommit(targetSide);
            cap.beginAlignmentFlash(targetSide, 120);
            ForceCapabilityManager.sync(player);
        });
        clear(player);
        PlayerForceIdentity.applyTitle(player);
        player.displayClientMessage(Component.literal(resultLine(fromSide, targetSide)), false);
        return true;
    }

    public static void clear(ServerPlayer player) {
        player.getPersistentData().remove(ROOT);
    }

    public static ForceSide currentSide(ServerPlayer player) {
        return player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .orElse(ForceSide.UNIVERSAL);
    }

    public static ForceSide parse(String value, ForceSide fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return ForceSide.valueOf(value);
        } catch (IllegalArgumentException ignored) {
            return fallback;
        }
    }

    private static CompoundTag root(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT)) {
            persistent.put(ROOT, new CompoundTag());
        }
        return persistent.getCompound(ROOT);
    }

    private static String titleFor(ForceSide targetSide) {
        return switch (targetSide) {
            case DARK -> "Embrace the dark side";
            case LIGHT -> "Renounce the dark side";
            case NEUTRAL -> "Embrace balance";
            default -> "Renounce";
        };
    }

    private static String bodyFor(ForceSide fromSide, ForceSide targetSide) {
        if (fromSide == ForceSide.DARK && targetSide == ForceSide.LIGHT) {
            return "You are committed to the dark side. To walk with Jedi teachings, you must renounce the dark side. Dark-side powers you unlocked will be lost; neutral knowledge remains.";
        }
        if (fromSide == ForceSide.DARK && targetSide == ForceSide.NEUTRAL) {
            return "You are committed to the dark side. To seek balance, you must renounce domination and return to the center. Light/Dark path powers are stripped; neutral knowledge remains.";
        }
        if (fromSide == ForceSide.LIGHT && targetSide == ForceSide.DARK) {
            return "You are committed to the light. To follow Sith teachings, you must renounce restraint and embrace the dark side. Light-side powers you unlocked will be lost; neutral knowledge remains.";
        }
        if (targetSide == ForceSide.NEUTRAL) {
            return "To walk the neutral path, you must release strict allegiance and embrace balance. Light/Dark path powers are stripped; neutral knowledge remains.";
        }
        return "Changing your Force allegiance will remove powers tied to the side you renounce. Neutral knowledge remains.";
    }

    private static String confirmLabel(ForceSide targetSide) {
        return switch (targetSide) {
            case DARK -> "Embrace the dark side";
            case LIGHT -> "Embrace the light";
            case NEUTRAL -> "Embrace balance";
            default -> "Confirm";
        };
    }

    private static String cancelLabel(ForceSide fromSide) {
        return switch (fromSide) {
            case DARK -> "Remain dark";
            case LIGHT -> "Remain light";
            case NEUTRAL -> "Remain balanced";
            default -> "Cancel";
        };
    }

    private static String resultLine(ForceSide fromSide, ForceSide targetSide) {
        if (fromSide == ForceSide.DARK && targetSide == ForceSide.LIGHT) {
            return "You renounced the dark side and embraced the light.";
        }
        if (fromSide == ForceSide.DARK && targetSide == ForceSide.NEUTRAL) {
            return "You renounced the dark side and embraced balance.";
        }
        if (targetSide == ForceSide.DARK) {
            return "You renounced your old path and embraced the dark side.";
        }
        if (targetSide == ForceSide.NEUTRAL) {
            return "You released strict allegiance and embraced balance.";
        }
        return "You renounced your old path and embraced the light.";
    }
}
