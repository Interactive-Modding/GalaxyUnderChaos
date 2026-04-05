package server.galaxyunderchaos.worldgen.portal;

import client.renderer.HyperspaceAnimation;
import client.renderer.HyperspaceManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.worldgen.dimension.ModDimensions;

import java.util.Random;
import java.util.function.Function;

public class ModTeleporter {
    private final BlockPos thisPos;
    private final boolean insideDimension;

    public ModTeleporter(BlockPos pos, boolean insideDim) {
        this.thisPos = pos;
        this.insideDimension = insideDim;
    }

    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destinationWorld, float yaw, Function<Entity, Entity> repositionEntity) {
        entity = repositionEntity.apply(entity);
        int y = insideDimension ? 61 : thisPos.getY();

        BlockPos safePos = findLeveledPosition(destinationWorld, thisPos);
        entity.teleportTo(safePos.getX(), safePos.getY(), safePos.getZ());

        entity.setNoGravity(true); // Temporarily disable gravity to prevent falling
        entity.invulnerableTime = 5; // Short invulnerability after teleport

        Entity finalEntity = entity;
        destinationWorld.getServer().execute(() -> finalEntity.setNoGravity(false)); // Re-enable gravity after a tick

        // Start the hyperspace cutscene
        HyperspaceManager.startHyperspace(entity, destinationWorld, safePos, yaw);

        return entity;
    }

    private BlockPos findLeveledPosition(ServerLevel world, BlockPos pos) {
        int searchRadius = 8; // Search area radius
        for (int dx = -searchRadius; dx <= searchRadius; dx++) {
            for (int dz = -searchRadius; dz <= searchRadius; dz++) {
                BlockPos checkPos = new BlockPos(pos.getX() + dx, findSafeLandingY(world, pos.getX() + dx, pos.getZ() + dz), pos.getZ() + dz);
                if (isLeveledArea(world, checkPos)) {
                    return checkPos;
                }
            }
        }
        return pos; // Fallback if no leveled area found
    }

    private boolean isLeveledArea(ServerLevel world, BlockPos pos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos checkPos = pos.offset(dx, 0, dz);
                if (!world.getBlockState(checkPos).isSolid() || !world.getBlockState(checkPos.above()).isAir() || !world.getBlockState(checkPos.above(2)).isAir()) {
                    return false;
                }
            }
        }
        return true;
    }

    private int findSafeLandingY(ServerLevel world, int x, int z) {
        int y = world.getHeight() - 1;
        while (y > 60) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockPos below = pos.below();
            BlockPos above = pos.above();

            if (world.getBlockState(pos).isAir() &&
                    world.getBlockState(above).isAir() &&
                    world.getBlockState(below).isSolid() &&
                    !world.getBlockState(below).is(Blocks.LAVA) &&
                    !world.getBlockState(below).is(Blocks.WATER)) {
                return y;
            }
            y--;
        }
        return 75; // Raise fallback height
    }



    private void spawnPortalItem(Entity entity, ServerLevel world) {
        if (world.dimension() == ModDimensions.TYTHON_LEVEL_KEY) {
            entity.spawnAtLocation(galaxyunderchaos.TYTHON_PORTAL_ITEM.get().getDefaultInstance());
        } else if (world.dimension() == ModDimensions.NABOO_LEVEL_KEY) {
            entity.spawnAtLocation(galaxyunderchaos.NABOO_PORTAL_ITEM.get().getDefaultInstance());
        } else if (world.dimension() == ModDimensions.ILUM_LEVEL_KEY) {
            entity.spawnAtLocation(galaxyunderchaos.ILUM_PORTAL_ITEM.get().getDefaultInstance());
        } else if (world.dimension() == ModDimensions.MUSTAFAR_LEVEL_KEY) {
            entity.spawnAtLocation(galaxyunderchaos.MUSTAFAR_PORTAL_ITEM.get().getDefaultInstance());
        } else if (world.dimension() == ModDimensions.DANTOOINE_LEVEL_KEY) {
            entity.spawnAtLocation(galaxyunderchaos.DANTOOINE_PORTAL_ITEM.get().getDefaultInstance());
        } else if (world.dimension() == ModDimensions.OSSUS_LEVEL_KEY) {
            entity.spawnAtLocation(galaxyunderchaos.OSSUS_PORTAL_ITEM.get().getDefaultInstance());
        } else if (world.dimension() == ModDimensions.MALACHOR_LEVEL_KEY) {
            entity.spawnAtLocation(galaxyunderchaos.MALACHOR_PORTAL_ITEM.get().getDefaultInstance());
        } else if (world.dimension() == ModDimensions.KORRIBAN_LEVEL_KEY) {
            entity.spawnAtLocation(galaxyunderchaos.KORRIBAN_PORTAL_ITEM.get().getDefaultInstance());
        }
    }
}
