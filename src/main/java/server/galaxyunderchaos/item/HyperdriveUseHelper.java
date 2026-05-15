package server.galaxyunderchaos.item;

import server.galaxyunderchaos.ship.HyperspaceJumpController;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import server.galaxyunderchaos.entity.FlashfireEntity;
import server.galaxyunderchaos.entity.NovadiveEntity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class HyperdriveUseHelper {
    public static final int MIN_HYPERDRIVE_Y = 140;
    public static final int HYPERSPACE_DURATION_TICKS = HyperspaceJumpController.DEFAULT_JUMP_TICKS;
    private static final ScheduledExecutorService HYPERSPACE_SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "GalaxyUnderChaos-HyperspaceJump");
        thread.setDaemon(true);
        return thread;
    });

    private HyperdriveUseHelper() {
    }

    public static boolean canUseHyperdrive(ServerPlayer player) {
        Entity ship = player.getVehicle();
        if (!(ship instanceof NovadiveEntity) && !(ship instanceof FlashfireEntity)) {
            player.displayClientMessage(Component.literal("Hyperdrive must be used from inside a ship."), true);
            return false;
        }

        if (ship.getY() < MIN_HYPERDRIVE_Y) {
            player.displayClientMessage(Component.literal("Climb above Y=" + MIN_HYPERDRIVE_Y + " to use the hyperdrive."), true);
            return false;
        }

        return true;
    }

    public static BlockPos getJumpPosition(ServerPlayer player) {
        Entity ship = player.getVehicle();
        return ship != null ? ship.blockPosition() : player.blockPosition();
    }


    public static void beginHyperspaceJump(ServerPlayer player, ServerLevel targetLevel, BlockPos targetPos) {
        beginHyperspaceJump(player, targetLevel, targetPos, null);
    }

    public static void beginHyperspaceJump(ServerPlayer player, ServerLevel targetLevel, BlockPos targetPos, Runnable beforeTeleport) {
        Entity visualEntity = player.getVehicle() != null ? player.getVehicle() : player;
        HyperspaceJumpController.startHyperspace(visualEntity, HYPERSPACE_DURATION_TICKS);

        if (player.getServer() == null) {
            return;
        }

        HYPERSPACE_SCHEDULER.schedule(() -> player.getServer().execute(() -> {
            if (beforeTeleport != null) {
                beforeTeleport.run();
            }
            teleportPlayerAndShip(player, targetLevel, targetPos);
            HyperspaceJumpController.finishHyperspace();
        }), HYPERSPACE_DURATION_TICKS * 50L, TimeUnit.MILLISECONDS);
    }

    public static void teleportPlayerAndShip(ServerPlayer player, ServerLevel targetLevel, BlockPos targetPos) {
        Entity ship = player.getVehicle();
        double x = targetPos.getX() + 0.5D;
        double y = targetPos.getY();
        double z = targetPos.getZ() + 0.5D;
        float yRot = ship != null ? ship.getYRot() : player.getYRot();
        float xRot = player.getXRot();

        if (ship instanceof NovadiveEntity || ship instanceof FlashfireEntity) {
            EntityType<?> shipType = ship.getType();
            CompoundTag shipData = new CompoundTag();
            ship.saveWithoutId(shipData);
            player.stopRiding();

            Entity copiedShip = shipType.create(targetLevel);
            if (copiedShip != null) {
                copiedShip.load(shipData);
                copiedShip.moveTo(x, y, z, yRot, ship.getXRot());
                copiedShip.setDeltaMovement(ship.getDeltaMovement());
                targetLevel.addFreshEntity(copiedShip);
                ship.discard();

                player.teleportTo(targetLevel, x, y + 0.75D, z, yRot, xRot);
                player.startRiding(copiedShip, true);
                return;
            }
        }

        player.teleportTo(targetLevel, x, y, z, player.getYRot(), player.getXRot());
    }
}
