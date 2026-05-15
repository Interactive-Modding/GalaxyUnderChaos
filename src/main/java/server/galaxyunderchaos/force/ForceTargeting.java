package server.galaxyunderchaos.force;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class ForceTargeting {
    private ForceTargeting() {}

    public static Vec3 findLookEnd(Player player, double range) {
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 end = start.add(look.scale(range));
        return player.level().clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        )).getLocation();
    }


    public static LivingEntity findTarget(Player player, double range, double radius) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 end = eye.add(look.scale(range));
        AABB box = player.getBoundingBox().expandTowards(look.scale(range)).inflate(radius);

        List<LivingEntity> candidates = player.level().getEntitiesOfClass(LivingEntity.class, box, entity ->
                entity != player && entity.isAlive() && !entity.isSpectator() && player.hasLineOfSight(entity));

        return candidates.stream()
                .filter(entity -> distanceFromSegmentSqr(eye, end, entity.getBoundingBox().getCenter()) <= radius * radius)
                .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(player)))
                .orElse(null);
    }

    public static LivingEntity findLightningTarget(Player player, double range) {
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 blockedEnd = findLookEnd(player, range);
        double maxHitDistanceSqr = start.distanceToSqr(blockedEnd) + 0.01D;

        LivingEntity best = null;
        double bestDistanceSqr = maxHitDistanceSqr + 1.0D;
        AABB searchBox = player.getBoundingBox().expandTowards(look.scale(range)).inflate(1.35D);

        for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, searchBox, candidate ->
                candidate != player && candidate.isAlive() && !candidate.isSpectator())) {
            AABB hitBox = entity.getBoundingBox().inflate(0.65D);
            Optional<Vec3> hit = hitBox.clip(start, blockedEnd);
            double distanceSqr;

            if (hit.isPresent()) {
                distanceSqr = start.distanceToSqr(hit.get());
            } else {
                // Forgiving fallback for mobs whose center is close to the ray but whose exact inflated box
                // missed because the player is aiming across an edge/corner while both entities move.
                double radius = Math.max(0.75D, entity.getBbWidth() * 0.65D);
                if (distanceFromSegmentSqr(start, blockedEnd, entity.getBoundingBox().getCenter()) > radius * radius) {
                    continue;
                }
                distanceSqr = entity.distanceToSqr(player);
            }

            if (distanceSqr <= maxHitDistanceSqr && distanceSqr < bestDistanceSqr) {
                bestDistanceSqr = distanceSqr;
                best = entity;
            }
        }

        return best;
    }

    public static List<LivingEntity> findTargetsAlongRay(Player player, double range, double radius) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 end = eye.add(look.scale(range));
        AABB box = player.getBoundingBox().expandTowards(look.scale(range)).inflate(radius);
        return player.level().getEntitiesOfClass(LivingEntity.class, box, entity ->
                        entity != player && entity.isAlive() && !entity.isSpectator() && player.hasLineOfSight(entity)
                                && distanceFromSegmentSqr(eye, end, entity.getBoundingBox().getCenter()) <= radius * radius)
                .stream()
                .sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(player)))
                .toList();
    }

    private static double distanceFromSegmentSqr(Vec3 start, Vec3 end, Vec3 point) {
        Vec3 segment = end.subtract(start);
        Vec3 toPoint = point.subtract(start);
        double segmentLengthSqr = segment.lengthSqr();
        if (segmentLengthSqr < 1.0E-7D) {
            return point.distanceToSqr(start);
        }
        double t = clamp(toPoint.dot(segment) / segmentLengthSqr, 0.0D, 1.0D);
        Vec3 projection = start.add(segment.scale(t));
        return point.distanceToSqr(projection);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
