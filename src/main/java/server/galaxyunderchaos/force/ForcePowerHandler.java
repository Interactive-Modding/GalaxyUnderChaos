package server.galaxyunderchaos.force;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import server.galaxyunderchaos.effect.ModEffects;
import server.galaxyunderchaos.entity.ForceBeamEffectEntity;
import server.galaxyunderchaos.entity.ForcePushWaveEntity;
import server.galaxyunderchaos.entity.ThrownLightsaberEntity;
import server.galaxyunderchaos.event.LightsaberCombatEventHandler;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.sound.ModSounds;

import java.util.List;

public final class ForcePowerHandler {
    private static final int DEFAULT_COOLDOWN = 15;

    private ForcePowerHandler() {}

    public static boolean useSelectedPower(ServerPlayer player) {
        return player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(cap -> useSelectedPower(player, cap))
                .orElse(false);
    }

    public static boolean useSelectedPower(ServerPlayer player, ForceCapability cap) {
        ForcePower power = cap.getSelectedPower();
        if (power == null) {
            player.displayClientMessage(Component.literal("No Force power selected."), true);
            return false;
        }
        if (isLightning(power)) {
            return beginUsingSelectedPower(player, cap);
        }
        if (cap.getUseCooldownTicks() > 0) {
            return false;
        }
        if (!cap.consumeForce(power.useCost())) {
            player.displayClientMessage(Component.literal("Not enough Force energy."), true);
            player.level().playSound(null, player.blockPosition(), ModSounds.FORCE_CAST_FAIL.get(), SoundSource.PLAYERS, 0.8F, 1.0F);
            return false;
        }

        boolean success = execute(player, power, cap, false);
        if (!success) {
            cap.addForce(power.useCost());
            return false;
        }

        if (power != ForcePower.THROW1 && power != ForcePower.THROW2) {
            player.swing(InteractionHand.MAIN_HAND, true);
        }
        cap.setUseCooldownTicks(getCooldown(power));
        return true;
    }

    public static boolean beginUsingSelectedPower(ServerPlayer player, ForceCapability cap) {
        ForcePower power = cap.getSelectedPower();
        if (power == null) {
            player.displayClientMessage(Component.literal("No Force power selected."), true);
            return false;
        }
        if (!isLightning(power)) {
            return useSelectedPower(player, cap);
        }
        if (cap.getUseCooldownTicks() > 0) {
            return false;
        }
        if (cap.getCurrentForce() <= 0.5F) {
            player.displayClientMessage(Component.literal("Not enough Force energy."), true);
            player.level().playSound(null, player.blockPosition(), ModSounds.FORCE_CAST_FAIL.get(), SoundSource.PLAYERS, 0.8F, 1.0F);
            return false;
        }
        if (cap.isUsingPower() && power.id().equals(cap.getUsingPowerId())) {
            return true;
        }
        cap.startUsingPower(power);
        cap.beginVisual(power, 3, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM | ForceCapability.VISUAL_LIGHTNING);
        player.level().playSound(null, player.blockPosition(), ModSounds.FORCE_LIGHTNING_START.get(), SoundSource.PLAYERS, 0.8F, lightningPitch(power));
        return true;
    }

    public static void stopUsingPower(ServerPlayer player, ForceCapability cap) {
        ForcePower power = ForcePower.byId(cap.getUsingPowerId());
        if (cap.isUsingPower() && isLightning(power) && cap.getUsingTicks() > 1) {
            cap.setUseCooldownTicks(Math.max(cap.getUseCooldownTicks(), getCooldown(power)));
        }
        cap.stopUsingPower();
    }

    public static void tickUsingPower(ServerPlayer player, ForceCapability cap) {
        if (!cap.isUsingPower()) {
            return;
        }
        ForcePower power = ForcePower.byId(cap.getUsingPowerId());
        if (power == null) {
            cap.stopUsingPower();
            return;
        }
        cap.tickUsingPower();

        if (isLightning(power)) {
            cap.beginVisual(power, 2, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM | ForceCapability.VISUAL_LIGHTNING);
            if (cap.getUsingTicks() == 1) {
                player.swing(InteractionHand.MAIN_HAND, true);
            }
            float costPerTick = switch (power) {
                case LIGHTNING1 -> 0.45F;
                case LIGHTNING2 -> 0.85F;
                case LIGHTNING3 -> 1.35F;
                default -> 0.45F;
            };
            if (!cap.consumeForce(costPerTick)) {
                stopUsingPower(player, cap);
                player.displayClientMessage(Component.literal("Not enough Force energy."), true);
                return;
            }

            switch (power) {
                case LIGHTNING1 -> lightning(player, 4.0F, 0, true);
                case LIGHTNING2 -> lightning(player, 6.0F, 1, true);
                case LIGHTNING3 -> lightning(player, 8.0F, 2, true);
                default -> {
                }
            }

            // Sustained lightning should keep firing into the aimed endpoint when no entity is hit.
            // The only natural limiter is Force energy or the player releasing the use key.
            cap.resetNoTargetUsingTicks();
        } else {
            cap.stopUsingPower();
        }
    }

    private static boolean isLightning(ForcePower power) {
        return power == ForcePower.LIGHTNING1 || power == ForcePower.LIGHTNING2 || power == ForcePower.LIGHTNING3;
    }

    private static float lightningPitch(ForcePower power) {
        return switch (power) {
            case LIGHTNING2 -> 1.05F;
            case LIGHTNING3 -> 1.10F;
            default -> 1.0F;
        };
    }

    public static int getCooldown(ForcePower power) {
        return switch (power) {
            case LIGHTNING1, LIGHTNING2, LIGHTNING3, PUSH1, PUSH2, PUSH3, THROW1, THROW2 -> 20;
            case HEAL3, DRAIN3, WOUND3 -> 30;
            default -> DEFAULT_COOLDOWN;
        };
    }

    private static boolean execute(ServerPlayer player, ForcePower power, ForceCapability cap, boolean chained) {
        return switch (power) {
            case HEAL1 -> heal(player, cap, 4.0F, 0, 0);
            case HEAL2 -> heal(player, cap, 7.0F, 1, 0);
            case HEAL3 -> heal(player, cap, 10.0F, 1, 1);
            case FORTIFY1 -> fortify(player, cap, 200, 0);
            case FORTIFY2 -> fortify(player, cap, 240, 1);
            case FORTIFY3 -> fortify(player, cap, 300, 1);
            case STUN1 -> stun(player, cap, 0, 2.0F);
            case STUN2 -> stun(player, cap, 1, 3.5F);
            case STUN3 -> stun(player, cap, 2, 4.5F);
            case DRAIN1 -> drain(player, cap, 5.0F, 0);
            case DRAIN2 -> drain(player, cap, 7.0F, 1);
            case DRAIN3 -> drain(player, cap, 10.0F, 2);
            case LIGHTNING1 -> lightning(player, 4.0F, 0, false);
            case LIGHTNING2 -> lightning(player, 6.0F, 1, false);
            case LIGHTNING3 -> lightning(player, 8.0F, 2, false);
            case WOUND1 -> wound(player, cap, 2.0F, 0);
            case WOUND2 -> wound(player, cap, 4.0F, 1);
            case WOUND3 -> wound(player, cap, 6.0F, 2);
            case STEALTH -> stealth(player, cap, 240);
            case SPEED -> speed(player, cap, 200, 1);
            case SIGHT1 -> sight(player, cap, 240, 0);
            case SIGHT2 -> sight(player, cap, 300, 1);
            case SIGHT3 -> sight(player, cap, 360, 2);
            case MEDITATION1 -> meditation(player, cap, 160, 0);
            case MEDITATION2 -> meditation(player, cap, 220, 1);
            case MEDITATION3 -> meditation(player, cap, 300, 2);
            case THROW1 -> bladeThrow(player, cap, 5.0F, 1.10F);
            case THROW2 -> bladeThrow(player, cap, 7.0F, 1.35F);
            case RESIST1 -> resist(player, cap, 240, 0);
            case RESIST2 -> resist(player, cap, 300, 1);
            case RESIST3 -> resist(player, cap, 360, 2);
            case PUSH1 -> push(player, cap, 4.0F, 0.9D, 12.0D);
            case PUSH2 -> push(player, cap, 6.0F, 1.15D, 16.0D);
            case PUSH3 -> push(player, cap, 8.0F, 1.45D, 20.0D);
            default -> false;
        };
    }

    private static boolean heal(ServerPlayer player, ForceCapability cap, float amount, int regenAmplifier, int absorbAmplifier) {
        player.heal(amount);
        ForcePower effectPower = ForcePower.byId(regenAmplifier > 0 ? (absorbAmplifier > 0 ? "heal3" : "heal2") : "heal1");
        addForceEffect(player, effectPower, 120, regenAmplifier);
        addHiddenVanillaEffect(player, MobEffects.REGENERATION, 120, regenAmplifier);
        if (absorbAmplifier > 0) {
            addHiddenVanillaEffect(player, MobEffects.ABSORPTION, 200, absorbAmplifier - 1);
        }
        cap.beginVisual(effectPower, 8, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.FORCE_HEAL.get(), 1.0F);
        return true;
    }

    private static boolean fortify(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        ForcePower effectPower = amplifier > 0 ? ForcePower.FORTIFY2 : ForcePower.FORTIFY1;
        addForceEffect(player, effectPower, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_BOOST, duration, 0);
        cap.beginVisual(effectPower, 8, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.AMBIENT_FORCE_FORTIFY.get(), 1.0F);
        return true;
    }

    private static boolean stun(ServerPlayer player, ForceCapability cap, int amplifier, float seconds) {
        LivingEntity target = ForceTargeting.findTarget(player, 16.0D, 1.25D);
        if (target == null) {
            return false;
        }
        int duration = Mth.floor(seconds * 20.0F);
        ForcePower effectPower = amplifier > 1 ? ForcePower.STUN3 : amplifier > 0 ? ForcePower.STUN2 : ForcePower.STUN1;
        addForceEffect(target, effectPower, duration, amplifier);
        addHiddenVanillaEffect(target, MobEffects.MOVEMENT_SLOWDOWN, duration, amplifier + 2);
        addHiddenVanillaEffect(target, MobEffects.WEAKNESS, duration, amplifier);
        addHiddenVanillaEffect(target, MobEffects.DIG_SLOWDOWN, duration, amplifier + 1);
        target.hurt(player.damageSources().magic(), 1.0F + amplifier);
//        spawnConnectionParticles(player, target, ParticleTypes.END_ROD);
        cap.beginVisual(effectPower, 7, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.AMBIENT_FORCE_STASIS.get(), 1.1F);
        return true;
    }

    private static boolean drain(ServerPlayer player, ForceCapability cap, float damage, int tier) {
        LivingEntity target = ForceTargeting.findTarget(player, 16.0D, 1.25D);
        if (target == null) {
            return false;
        }
        target.hurt(player.damageSources().magic(), damage);
        player.heal(2.0F + tier * 2.0F);
        cap.addForce(12.0F + tier * 8.0F);
        addForceEffect(target, tier > 1 ? ForcePower.DRAIN3 : tier > 0 ? ForcePower.DRAIN2 : ForcePower.DRAIN1, 80 + tier * 40, tier);
        addHiddenVanillaEffect(target, MobEffects.WEAKNESS, 80 + tier * 40, tier);
//        spawnConnectionParticles(player, target, new DustParticleOptions(new Vector3f(0.6F, 0.1F, 0.8F), 1.1F));
        spawnBeam(player, target, ForceBeamEffectEntity.KIND_DRAIN, 7);
        cap.beginVisual(tier > 1 ? ForcePower.DRAIN3 : tier > 0 ? ForcePower.DRAIN2 : ForcePower.DRAIN1, 7, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_DRAIN);
        playCastSound(player, ModSounds.FORCE_CAST_DARK.get(), 0.9F);
        return true;
    }

    private static boolean lightning(ServerPlayer player, float damage, int tier, boolean sustained) {
        double range = 16.0D;
        LivingEntity target = ForceTargeting.findLightningTarget(player, range);
        int lifeTicks = sustained ? 4 : 7;

        if (target == null) {
            spawnBeam(player, ForceTargeting.findLookEnd(player, range), ForceBeamEffectEntity.KIND_LIGHTNING, lifeTicks);
            if (!sustained) {
                playCastSound(player, ModSounds.FORCE_LIGHTNING_START.get(), 1.0F + tier * 0.05F);
            }
            return false;
        }

        if (target instanceof ServerPlayer defender
                && LightsaberCombatEventHandler.tryBlockForceLightning(defender, player, damage, tier)) {
            spawnBeam(defender, player, ForceBeamEffectEntity.KIND_LIGHTNING, lifeTicks);

            if (!sustained || player.tickCount % 5 == 0) {
                player.invulnerableTime = 0;
                player.hurt(player.damageSources().indirectMagic(defender, defender), Math.max(1.0F, damage * (0.35F + tier * 0.10F)));
            }

            if (!sustained) {
                playCastSound(player, ModSounds.FORCE_LIGHTNING_START.get(), 1.0F + tier * 0.05F);
            }
            return true;
        }

        if (!sustained || player.tickCount % 5 == 0) {
            target.invulnerableTime = 0;
            target.hurt(player.damageSources().indirectMagic(player, player), damage);
        }

        target.setDeltaMovement(0.0D, Math.min(target.getDeltaMovement().y, 0.0D), 0.0D);
        target.hurtMarked = true;
        addForceEffect(target, tier > 1 ? ForcePower.LIGHTNING3 : tier > 0 ? ForcePower.LIGHTNING2 : ForcePower.LIGHTNING1, 12 + tier * 12, tier);
        addHiddenVanillaEffect(target, MobEffects.MOVEMENT_SLOWDOWN, 12 + tier * 12, 4);

//        if (player.tickCount % 2 == 0) {
//            spawnConnectionParticles(player, target, ParticleTypes.ELECTRIC_SPARK);
//        }

        spawnBeam(player, target, ForceBeamEffectEntity.KIND_LIGHTNING, lifeTicks);
        if (!sustained) {
            playCastSound(player, ModSounds.FORCE_LIGHTNING_START.get(), 1.0F + tier * 0.05F);
        }
        return true;
    }

    private static boolean wound(ServerPlayer player, ForceCapability cap, float damage, int tier) {
        LivingEntity target = ForceTargeting.findTarget(player, 16.0D, 1.2D);
        if (target == null) {
            return false;
        }
        target.hurt(player.damageSources().magic(), damage);
        ForcePower effectPower = tier > 1 ? ForcePower.WOUND3 : tier > 0 ? ForcePower.WOUND2 : ForcePower.WOUND1;
        addForceEffect(target, effectPower, 80 + tier * 20, tier);
        addHiddenVanillaEffect(target, MobEffects.LEVITATION, 10 + tier * 10, 0);
        addHiddenVanillaEffect(target, MobEffects.WEAKNESS, 80 + tier * 20, tier + 1);
        addHiddenVanillaEffect(target, MobEffects.MOVEMENT_SLOWDOWN, 80 + tier * 20, 4);
        spawnConnectionParticles(player, target, new DustParticleOptions(new Vector3f(0.95F, 0.1F, 0.1F), 1.0F));
        cap.beginVisual(effectPower, 7, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.FORCE_CAST_DARK.get(), 1.2F);
        return true;
    }

    private static boolean stealth(ServerPlayer player, ForceCapability cap, int duration) {
        addForceEffect(player, ForcePower.STEALTH, duration, 0);
        addHiddenVanillaEffect(player, MobEffects.INVISIBILITY, duration, 0);
        addHiddenVanillaEffect(player, MobEffects.MOVEMENT_SPEED, duration, 0);
        cap.beginVisual(ForcePower.STEALTH, 8, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.FORCE_STEALTH_ON.get(), 1.0F);
        return true;
    }

    private static boolean speed(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        addForceEffect(player, ForcePower.SPEED, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.MOVEMENT_SPEED, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.JUMP, duration, amplifier);
        cap.beginVisual(ForcePower.SPEED, 6, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 1.15F);
        return true;
    }

    private static boolean sight(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        addForceEffect(player, amplifier > 1 ? ForcePower.SIGHT3 : amplifier > 0 ? ForcePower.SIGHT2 : ForcePower.SIGHT1, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.NIGHT_VISION, duration, 0);
        List<LivingEntity> targets = ForceTargeting.findTargetsAlongRay(player, 24.0D + amplifier * 6.0D, 4.0D + amplifier);
        for (LivingEntity target : targets) {
            addHiddenVanillaEffect(target, MobEffects.GLOWING, 80 + amplifier * 40, 0);
        }
        cap.beginVisual(amplifier > 1 ? ForcePower.SIGHT3 : amplifier > 0 ? ForcePower.SIGHT2 : ForcePower.SIGHT1, 6, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 1.25F);
        return true;
    }

    private static boolean meditation(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        addForceEffect(player, amplifier > 1 ? ForcePower.MEDITATION3 : amplifier > 0 ? ForcePower.MEDITATION2 : ForcePower.MEDITATION1, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.REGENERATION, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, 0);
        cap.addForce(20.0F + amplifier * 15.0F);
        cap.beginVisual(amplifier > 1 ? ForcePower.MEDITATION3 : amplifier > 0 ? ForcePower.MEDITATION2 : ForcePower.MEDITATION1, 8, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 0.85F);
        return true;
    }

    private static boolean resist(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        addForceEffect(player, amplifier > 1 ? ForcePower.RESIST3 : amplifier > 0 ? ForcePower.RESIST2 : ForcePower.RESIST1, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.FIRE_RESISTANCE, duration, 0);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, amplifier);
        cap.beginVisual(amplifier > 1 ? ForcePower.RESIST3 : amplifier > 0 ? ForcePower.RESIST2 : ForcePower.RESIST1, 6, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.AMBIENT_FORCE_ENERGY_RESIST.get(), 1.0F);
        return true;
    }

    private static boolean push(ServerPlayer player, ForceCapability cap, float damage, double force, double range) {
        List<LivingEntity> targets = ForceTargeting.findTargetsAlongRay(player, range, 2.60D);
        if (targets.isEmpty()) {
            return false;
        }
        Vec3 push = player.getLookAngle().normalize().scale(force);
        double upwardLift = Math.max(0.22D, 0.25D + Math.max(0.0D, push.y) * 0.70D);
        for (LivingEntity target : targets) {
            target.hurt(player.damageSources().magic(), damage);
            target.push(push.x, upwardLift, push.z);
            target.hurtMarked = true;
        }
        spawnPushWave(player, (float) range);
        cap.beginVisual(damage >= 8.0F ? ForcePower.PUSH3 : damage >= 6.0F ? ForcePower.PUSH2 : ForcePower.PUSH1, 6, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_PUSH);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 0.95F);
        return true;
    }

    private static boolean bladeThrow(ServerPlayer player, ForceCapability cap, float damage, float returnSpeed) {
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof LightsaberItem saber) || !saber.isActive(held)) {
            player.displayClientMessage(Component.literal("You need an active lightsaber to use Blade Throw."), true);
            return false;
        }

        ItemStack thrownStack = held.copyWithCount(1);
        if (!player.getAbilities().instabuild) {
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }

        ThrownLightsaberEntity thrown = new ThrownLightsaberEntity(player.level(), player, thrownStack, damage, returnSpeed);
        thrown.setPos(player.getX(), player.getEyeY() - 0.15D, player.getZ());
        thrown.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.8F, 0.0F);
        player.level().addFreshEntity(thrown);
        player.getCooldowns().addCooldown(thrownStack.getItem(), 20);
        player.swing(InteractionHand.MAIN_HAND);
        cap.beginVisual(returnSpeed > 1.2F ? ForcePower.THROW2 : ForcePower.THROW1, 8, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.LIGHTSABER_SWING.get(), 1.1F);
        return true;
    }

    private static void addForceEffect(LivingEntity entity, ForcePower power, int duration, int amplifier) {
        net.minecraft.world.effect.MobEffect effect = ModEffects.getForceEffect(power);
        if (effect != null) {
            entity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, true, true));
        }
    }

    private static void addHiddenVanillaEffect(LivingEntity entity, net.minecraft.world.effect.MobEffect effect, int duration, int amplifier) {
        entity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false, false));
    }

    private static void spawnBeam(ServerPlayer player, LivingEntity target, int kind, int lifeTicks) {
        if (player.level() instanceof ServerLevel level) {
            level.addFreshEntity(new ForceBeamEffectEntity(level, player, target, kind, lifeTicks));
        }
    }

    private static void spawnBeam(ServerPlayer player, Vec3 end, int kind, int lifeTicks) {
        if (player.level() instanceof ServerLevel level) {
            level.addFreshEntity(new ForceBeamEffectEntity(level, player, end, kind, lifeTicks));
        }
    }

    private static void spawnPushWave(ServerPlayer player, float radius) {
        if (player.level() instanceof ServerLevel level) {
            level.addFreshEntity(new ForcePushWaveEntity(level, player, radius, 10));
        }
    }

    private static void playCastSound(Player player, net.minecraft.sounds.SoundEvent sound, float pitch) {
        player.level().playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS, 0.8F, pitch);
    }

    private static void spawnConnectionParticles(ServerPlayer player, LivingEntity target, net.minecraft.core.particles.ParticleOptions particle) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        Vec3 start = player.getEyePosition();
        Vec3 end = target.getBoundingBox().getCenter();
        Vec3 delta = end.subtract(start);
        int steps = Math.max(6, Mth.floor(delta.length() * 4.0D));
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3 pos = start.add(delta.scale(t));
            level.sendParticles(particle, pos.x, pos.y, pos.z, 1, 0.01D, 0.01D, 0.01D, 0.0D);
        }
    }
}
