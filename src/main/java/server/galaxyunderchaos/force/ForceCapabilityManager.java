package server.galaxyunderchaos.force;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.sound.ModSounds;

public final class ForceCapabilityManager {
    private ForceCapabilityManager() {}

    public static void sync(ServerPlayer player) {
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            ForceNetworking.sendToPlayer(player, new SyncForceStatePacket(cap.serializeNBT()));
            ForceNetworking.sendVisualToTracking(player, new SyncForceVisualPacket(player.getId(), cap));
            cap.clearDirty();
        });
    }

    public static void unlockPower(ServerPlayer player, ForcePower power) {
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            boolean unlocked = cap.unlockPower(power);
            if (unlocked) {
                if (power == ForcePower.FORCE_SENSITIVITY) {
                    player.displayClientMessage(Component.literal("You feel the Force awaken within you."), false);
                }
                galaxyunderchaos.LOGGER.info("Unlocked force power {} for {}", power.id(), player.getGameProfile().getName());
            }
            sync(player);
        });
    }

    public static void unlockBranch(ServerPlayer player, ForceSide side) {
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            boolean alreadyUnlocked = switch (side) {
                case LIGHT -> cap.hasPower(ForcePower.LIGHT_SIDE);
                case DARK -> cap.hasPower(ForcePower.DARK_SIDE);
                case NEUTRAL -> cap.hasPower(ForcePower.NEUTRAL);
                default -> true;
            };
            cap.unlockStarterBranch(side);
            if (!alreadyUnlocked && side == ForceSide.DARK) {
                cap.beginAlignmentFlash(ForceSide.DARK, 120);
                player.displayClientMessage(Component.literal("Embracing dark side..."), false);
            }
            player.level().playSound(null, player.blockPosition(), alreadyUnlocked ? ModSounds.HOLOCRON_INVEST.get() : ModSounds.HOLOCRON_UNLOCK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!alreadyUnlocked) {
                player.displayClientMessage(Component.literal("Holocron knowledge unlocked: " + side.name()), false);
            }
            sync(player);
        });
    }

    public static void unlockAll(ServerPlayer player) {
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.unlockAll();
            sync(player);
        });
    }

    public static void refill(ServerPlayer player) {
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.setCurrentForce(cap.getMaxForce());
            sync(player);
        });
    }
}
