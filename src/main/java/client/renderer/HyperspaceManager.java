package client.renderer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import server.galaxyunderchaos.ship.HyperspaceJumpController;

/** Backward-compatible wrapper for older code paths that imported the old client package. */
public class HyperspaceManager {
    public static final int DEFAULT_JUMP_TICKS = HyperspaceJumpController.DEFAULT_JUMP_TICKS;

    public static void startHyperspace(Entity entity, ServerLevel destWorld, BlockPos destPos, float entityYaw) {
        HyperspaceJumpController.startHyperspace(entity, destWorld, destPos, entityYaw);
    }

    public static void startHyperspace(Entity entity, int durationTicks) {
        HyperspaceJumpController.startHyperspace(entity, durationTicks);
    }

    public static void finishHyperspace() {
        HyperspaceJumpController.finishHyperspace();
    }

    public static boolean isJumping() {
        return HyperspaceJumpController.isJumping();
    }
}
