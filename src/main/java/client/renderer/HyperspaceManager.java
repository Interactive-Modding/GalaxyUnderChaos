package client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HyperspaceManager {
    private static boolean inHyperspace = false;
    private static Entity entityToTeleport;
    private static ServerLevel destinationWorld;
    private static BlockPos destinationPos;
    private static float yaw;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startHyperspace(Entity entity, ServerLevel destWorld, BlockPos destPos, float entityYaw) {
        if (entity.level().isClientSide) return; // Ensure this runs on the server

        inHyperspace = true;
        entityToTeleport = entity;
        destinationWorld = destWorld;
        destinationPos = destPos;
        yaw = entityYaw;

        //System.out.println("Starting Hyperspace Jump..."); // Debugging
        HyperspaceOverlay.startWarpEffect(); // 🔹 Ensure overlay starts

        scheduler.schedule(HyperspaceManager::performTeleport, 5, TimeUnit.SECONDS);
    }



    public static void performTeleport() {
        if (entityToTeleport != null) {
            if (entityToTeleport instanceof ServerPlayer player) {
                // Correct Teleport Method for Players (Forge 1.21)
                player.teleportTo(destinationWorld, destinationPos.getX() + 0.5, destinationPos.getY(), destinationPos.getZ() + 0.5, yaw, player.getXRot());
            } else {
                // Correct Teleport Method for Entities (Forge 1.21)
                teleportEntity(entityToTeleport, destinationWorld, destinationPos);
            }
        }

        inHyperspace = false;
    }

    private static void teleportEntity(Entity entity, ServerLevel destinationWorld, BlockPos destinationPos) {
        // Unmount any passengers before teleporting
        entity.unRide();
        entity.stopRiding();

        // Remove the entity from the current world safely
        entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);

        // Recreate the entity in the new dimension
        Entity newEntity = entity.getType().create(destinationWorld);
        if (newEntity != null) {
            newEntity.restoreFrom(entity);
            newEntity.moveTo(destinationPos.getX() + 0.5, destinationPos.getY(), destinationPos.getZ() + 0.5, yaw, entity.getXRot());
            destinationWorld.addFreshEntity(newEntity);
        }
    }

    public static boolean isJumping() {
        return inHyperspace;
    }
}
