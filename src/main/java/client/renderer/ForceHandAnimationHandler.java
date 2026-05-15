/*
 * SPDX-License-Identifier: LGPL-3.0-only
 *
 * This file is part of Galaxy Under Chaos.
 * It contains code, data, model geometry, behavior, or compatibility logic
 * copied, translated, ported, adapted from, or created to support content
 * derived from Advanced Lightsabers 1.2 by FiskFille, credited to FiskFille
 * and Void Adept.
 *
 * Modifications for Galaxy Under Chaos / Minecraft Forge 1.20.1 by
 *  Vitiate and contributors.
 */

package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.entity.ForceBeamEffectEntity;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.galaxyunderchaos;

/**
 * First- and third-person Force casting arm poses.
 *
 * <p>This ports the old AdvancedLightsabers behavior where Force-use timers pull
 * the player's arm rotation toward the camera pitch/yaw. Lightning uses both
 * arms, drain/push/single casts use the right arm unless the visual flags request
 * otherwise. Kept client-only and data-driven from the existing Force visual flags.</p>
 */
@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForceHandAnimationHandler {
    private static final float DEG_TO_RAD = Mth.PI / 180.0F;

    private ForceHandAnimationHandler() {}

    /**
     * Applies the visible third-person arm pose at the tail of HumanoidModel#setupAnim.
     */
    public static void applyHumanoidPose(HumanoidModel<?> model,
                                         LivingEntity entity,
                                         float ageInTicks,
                                         float netHeadYaw,
                                         float headPitch) {
        ForceVisual visual = resolveVisual(entity, 0.0F);
        if (!visual.active()) {
            return;
        }

        float blend = visual.blend();
        float targetX = headPitch * DEG_TO_RAD - Mth.HALF_PI;
        float targetYaw = netHeadYaw * DEG_TO_RAD;
        float pulse = Mth.sin(ageInTicks * 0.28F) * 0.035F;

        if (visual.lightning()) {
            applyCastingArm(model.rightArm, targetX + pulse, targetYaw - 0.24F, 0.10F, blend, HumanoidArm.RIGHT);
            applyCastingArm(model.leftArm, targetX - pulse, targetYaw + 0.24F, -0.10F, blend, HumanoidArm.LEFT);

            model.body.xRot = Mth.lerp(blend * 0.12F, model.body.xRot, -0.06F);
            model.body.yRot = Mth.lerp(blend * 0.10F, model.body.yRot, targetYaw * 0.16F);
            return;
        }

        if (visual.push()) {
            applyCastingArm(model.rightArm, targetX, targetYaw, 0.0F, blend, HumanoidArm.RIGHT);
            return;
        }

        if (visual.drain()) {
            applyCastingArm(model.rightArm, targetX + 0.08F, targetYaw - 0.06F, 0.0F, blend, HumanoidArm.RIGHT);
            return;
        }

        if ((visual.flags() & ForceCapability.VISUAL_RIGHT_ARM) != 0) {
            applyCastingArm(model.rightArm, targetX + 0.10F, targetYaw - 0.10F, 0.08F, blend, HumanoidArm.RIGHT);
        }
        if ((visual.flags() & ForceCapability.VISUAL_LEFT_ARM) != 0) {
            applyCastingArm(model.leftArm, targetX + 0.10F, targetYaw + 0.10F, -0.08F, blend, HumanoidArm.LEFT);
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ForceVisual visual = resolveVisual(player, event.getPartialTick());
        if (!visual.active()) {
            return;
        }

        HumanoidArm physicalArm = physicalArmFor(player, event.getHand());
        boolean rightArm = physicalArm == HumanoidArm.RIGHT;
        boolean armRequested = visual.lightning()
                || (rightArm && (visual.flags() & ForceCapability.VISUAL_RIGHT_ARM) != 0)
                || (!rightArm && (visual.flags() & ForceCapability.VISUAL_LEFT_ARM) != 0);
        if (!armRequested) {
            return;
        }

        applyFirstPersonForcePose(event.getPoseStack(), physicalArm, visual, player.tickCount + event.getPartialTick());
    }

    private static void applyCastingArm(ModelPart arm,
                                        float targetX,
                                        float targetY,
                                        float targetZ,
                                        float blend,
                                        HumanoidArm side) {
        arm.xRot = Mth.lerp(blend, arm.xRot, targetX);
        arm.yRot = Mth.lerp(blend, arm.yRot, targetY);
        arm.zRot = Mth.lerp(blend, arm.zRot, targetZ);

        // Pull both hands slightly forward and apart so beam origins line up with the palms.
        arm.y += 1.0F * blend;
        arm.z -= 0.75F * blend;
        arm.x += (side == HumanoidArm.RIGHT ? -0.35F : 0.35F) * blend;
    }

    private static void applyFirstPersonForcePose(PoseStack poseStack,
                                                  HumanoidArm arm,
                                                  ForceVisual visual,
                                                  float ageInTicks) {
        float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        float blend = visual.blend();
        float pulse = Mth.sin(ageInTicks * 0.22F) * 0.015F;

        if (visual.lightning()) {
            // Two-hand lightning cast: hands come up, move toward center, and angle forward.
            poseStack.translate(-side * 0.055F * blend, (0.095F + pulse) * blend, -0.135F * blend);
            poseStack.mulPose(Axis.XP.rotationDegrees((-34.0F + Mth.sin(ageInTicks * 0.18F) * 1.5F) * blend));
            poseStack.mulPose(Axis.YP.rotationDegrees(side * 14.0F * blend));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-side * 10.0F * blend));
        } else if (visual.push()) {
            poseStack.translate(-side * 0.025F * blend, 0.065F * blend, -0.105F * blend);
            poseStack.mulPose(Axis.XP.rotationDegrees(-26.0F * blend));
            poseStack.mulPose(Axis.YP.rotationDegrees(side * 8.0F * blend));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-side * 5.0F * blend));
        } else if (visual.drain()) {
            poseStack.translate(-side * 0.020F * blend, 0.060F * blend, -0.095F * blend);
            poseStack.mulPose(Axis.XP.rotationDegrees(-22.0F * blend));
            poseStack.mulPose(Axis.YP.rotationDegrees(side * 6.0F * blend));
        } else {
            poseStack.translate(-side * 0.015F * blend, 0.045F * blend, -0.070F * blend);
            poseStack.mulPose(Axis.XP.rotationDegrees(-16.0F * blend));
            poseStack.mulPose(Axis.YP.rotationDegrees(side * 5.0F * blend));
        }
    }

    private static ForceVisual resolveVisual(LivingEntity entity, float partialTick) {
        ForceVisual capVisual = resolveCapabilityVisual(entity, partialTick);
        boolean beamLightning = hasActiveBeam(entity, ForceBeamEffectEntity.KIND_LIGHTNING);
        boolean beamDrain = hasActiveBeam(entity, ForceBeamEffectEntity.KIND_DRAIN);

        if (beamLightning) {
            return capVisual.mergeBeam(ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM | ForceCapability.VISUAL_LIGHTNING);
        }
        if (beamDrain) {
            return capVisual.mergeBeam(ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_DRAIN);
        }
        return capVisual;
    }

    private static ForceVisual resolveCapabilityVisual(LivingEntity entity, float partialTick) {
        ForceVisual[] result = {ForceVisual.NONE};
        entity.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            int flags = cap.getVisualFlags();
            boolean active = cap.getVisualTicks() > 0 || cap.isUsingPower();
            if (!active || flags == 0) {
                return;
            }

            float blend;
            if (cap.isUsingPower()) {
                blend = Mth.clamp((cap.getUsingTicks() + partialTick) / 4.0F, 0.0F, 1.0F);
            } else {
                blend = Mth.clamp((cap.getVisualTicks() + partialTick) / 5.0F, 0.0F, 1.0F);
            }
            result[0] = new ForceVisual(flags, blend);
        });
        return result[0];
    }

    private static boolean hasActiveBeam(LivingEntity entity, int kind) {
        if (entity.level() == null) {
            return false;
        }
        return !entity.level().getEntitiesOfClass(
                ForceBeamEffectEntity.class,
                entity.getBoundingBox().inflate(12.0D),
                beam -> beam.getEffectKind() == kind && beam.getOwnerEntity() == entity
        ).isEmpty();
    }

    private static HumanoidArm physicalArmFor(LocalPlayer player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return player.getMainArm();
        }
        return opposite(player.getMainArm());
    }

    private static HumanoidArm opposite(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }

    private record ForceVisual(int flags, float blend) {
        static final ForceVisual NONE = new ForceVisual(0, 0.0F);

        boolean active() {
            return flags != 0 && blend > 0.0F;
        }

        boolean lightning() {
            return (flags & ForceCapability.VISUAL_LIGHTNING) != 0;
        }

        boolean drain() {
            return (flags & ForceCapability.VISUAL_DRAIN) != 0;
        }

        boolean push() {
            return (flags & ForceCapability.VISUAL_PUSH) != 0;
        }

        ForceVisual mergeBeam(int beamFlags) {
            if (!active()) {
                return new ForceVisual(beamFlags, 1.0F);
            }
            return new ForceVisual(flags | beamFlags, Math.max(blend, 0.85F));
        }
    }
}
