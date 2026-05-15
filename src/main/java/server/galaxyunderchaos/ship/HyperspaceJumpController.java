package server.galaxyunderchaos.ship;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * Server-side jump coordinator for the client hyperspace visual.
 *
 * It sends a PLAY_TO_CLIENT packet to the player or ship passengers. Teleportation remains owned by
 * HyperdriveUseHelper or the portal teleporter so the ship-copy/rider logic stays server-authoritative.
 */
public final class HyperspaceJumpController {
    public static final int DEFAULT_JUMP_TICKS = 100;

    private static boolean inHyperspace;

    private HyperspaceJumpController() {
    }

    public static void startHyperspace(Entity entity, ServerLevel destWorld, BlockPos destPos, float entityYaw) {
        startHyperspace(entity, DEFAULT_JUMP_TICKS);
    }

    public static void startHyperspace(Entity entity, int durationTicks) {
        if (entity == null || entity.level().isClientSide) {
            return;
        }

        inHyperspace = true;
        sendVisualToEntityPlayers(entity, Math.max(1, durationTicks));
    }

    public static void finishHyperspace() {
        inHyperspace = false;
    }

    public static boolean isJumping() {
        return inHyperspace;
    }

    private static void sendVisualToEntityPlayers(Entity entity, int durationTicks) {
        HyperspaceEffectPacket packet = new HyperspaceEffectPacket(durationTicks);
        if (entity instanceof ServerPlayer player) {
            ShipNetworking.sendToPlayer(player, packet);
        }

        for (Entity passenger : entity.getPassengers()) {
            if (passenger instanceof ServerPlayer player) {
                ShipNetworking.sendToPlayer(player, packet);
            }
        }
    }
}
