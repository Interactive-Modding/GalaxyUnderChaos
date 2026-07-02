package server.galaxyunderchaos.force;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import server.galaxyunderchaos.effect.ModEffects;
import server.galaxyunderchaos.entity.ForceAbilityEffectEntity;
import server.galaxyunderchaos.entity.ForceBeamEffectEntity;
import server.galaxyunderchaos.entity.ForcePushWaveEntity;
import server.galaxyunderchaos.entity.ForceProjectionCloneEntity;
import server.galaxyunderchaos.entity.ThrownLightsaberEntity;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.event.LightsaberCombatEventHandler;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.sound.ModSounds;

import java.util.List;

public final class ForcePowerHandler {
    private static final int DEFAULT_COOLDOWN = 15;
    private static final int TUTAMINIS_SABER_BLOCK_COOLDOWN_TICKS = 100;
    private static final String LEAP_CHAIN_TAG = "GalaxyUnderChaosForceLeapChain";
    private static final String TUTAMINIS_SABER_BLOCK_READY_TIME_TAG = "GalaxyUnderChaosTutaminisSaberBlockReadyTime";

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
        if (isForceSuppressed(player)) {
            player.displayClientMessage(Component.literal("Your Force connection is suppressed."), true);
            return false;
        }
        if (isHoldAbility(power)) {
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
        if (isForceSuppressed(player)) {
            player.displayClientMessage(Component.literal("Your Force connection is suppressed."), true);
            return false;
        }
        if (!isHoldAbility(power)) {
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
        int visualFlags = isLightning(power)
                ? ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM | ForceCapability.VISUAL_LIGHTNING
                : ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM;
        cap.beginVisual(power, 3, visualFlags);
        player.level().playSound(null, player.blockPosition(),
                isLightning(power) ? ModSounds.FORCE_LIGHTNING_START.get() : ModSounds.AMBIENT_FORCE_ENERGY_RESIST.get(),
                SoundSource.PLAYERS, 0.8F, lightningPitch(power));
        return true;
    }

    public static void stopUsingPower(ServerPlayer player, ForceCapability cap) {
        ForcePower power = ForcePower.byId(cap.getUsingPowerId());
        if (cap.isUsingPower() && isHoldAbility(power) && cap.getUsingTicks() > 1) {
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

        if (isForceSuppressed(player)) {
            player.displayClientMessage(Component.literal("Your Force connection is suppressed."), true);
            stopUsingPower(player, cap);
            return;
        }

        if (isLightning(power)) {
            cap.beginVisual(power, 2, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM | ForceCapability.VISUAL_LIGHTNING);
            if (cap.getUsingTicks() == 1) {
                player.swing(InteractionHand.MAIN_HAND, true);
            }
            float costPerTick = switch (power) {
                case LIGHTNING1 -> 0.45F;
                case LIGHTNING2 -> 0.85F;
                case LIGHTNING3 -> 1.35F;
                case ELECTRIC_JUDGMENT1 -> 0.60F;
                case ELECTRIC_JUDGMENT2 -> 0.95F;
                case ELECTRIC_JUDGMENT3 -> 1.30F;
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
                case ELECTRIC_JUDGMENT1 -> electricJudgment(player, 3.5F, 0, true);
                case ELECTRIC_JUDGMENT2 -> electricJudgment(player, 5.0F, 1, true);
                case ELECTRIC_JUDGMENT3 -> electricJudgment(player, 6.5F, 2, true);
                default -> {
                }
            }

            // Sustained lightning/judgment should keep firing into the aimed endpoint when no entity is hit.
            // The only natural limiter is Force energy or the player releasing the use key.
            cap.resetNoTargetUsingTicks();
        } else if (power == ForcePower.TUTAMINIS) {
            cap.beginVisual(power, 2, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM);
            if (cap.getUsingTicks() == 1) {
                player.swing(InteractionHand.MAIN_HAND, true);
            }
            if (!cap.consumeForce(0.55F)) {
                stopUsingPower(player, cap);
                player.displayClientMessage(Component.literal("Not enough Force energy."), true);
                return;
            }
            addForceEffect(player, ForcePower.TUTAMINIS, 8, 0);
            addHiddenVanillaEffect(player, MobEffects.FIRE_RESISTANCE, 8, 0);
            addHiddenVanillaEffect(player, MobEffects.DAMAGE_RESISTANCE, 8, 1);
            if (player.tickCount % 4 == 0) {
                spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_TUTAMINIS, 6, 0.165F);
            }
            cap.resetNoTargetUsingTicks();
        } else {
            cap.stopUsingPower();
        }
    }

    private static boolean isLightning(ForcePower power) {
        return power == ForcePower.LIGHTNING1
                || power == ForcePower.LIGHTNING2
                || power == ForcePower.LIGHTNING3
                || power == ForcePower.ELECTRIC_JUDGMENT1
                || power == ForcePower.ELECTRIC_JUDGMENT2
                || power == ForcePower.ELECTRIC_JUDGMENT3;
    }

    public static boolean isHoldAbility(ForcePower power) {
        return isLightning(power) || power == ForcePower.TUTAMINIS;
    }

    public static boolean isForceSuppressed(LivingEntity entity) {
        return hasForceEffect(entity, ForcePower.FORCE_SHACKLES) || hasForceEffect(entity, ForcePower.WALL_OF_LIGHT);
    }

    private static float lightningPitch(ForcePower power) {
        return switch (power) {
            case LIGHTNING2 -> 1.05F;
            case LIGHTNING3 -> 1.10F;
            case ELECTRIC_JUDGMENT1 -> 1.18F;
            case ELECTRIC_JUDGMENT2 -> 1.24F;
            case ELECTRIC_JUDGMENT3 -> 1.30F;
            case TUTAMINIS -> 1.10F;
            default -> 1.0F;
        };
    }

    public static int getCooldown(ForcePower power) {
        return switch (power) {
            case LIGHTNING1, LIGHTNING2, LIGHTNING3, ELECTRIC_JUDGMENT1, ELECTRIC_JUDGMENT2, ELECTRIC_JUDGMENT3, TUTAMINIS, PUSH1, PUSH2, PUSH3, PULL1, PULL2, PULL3, THROW1, THROW2 -> 20;
            case FORCE_LEAP -> 30;
            case WALL_OF_LIGHT -> 200;
            case HEAL3, DRAIN3, WOUND3, FORCE_DESTRUCTION2, FORCE_DESTRUCTION3 -> 30;
            case FORCE_SCREAM1, FORCE_SCREAM2, FORCE_SCREAM3, FORCE_DESTRUCTION1, FORCE_PROJECTION1, FORCE_PROJECTION2, FORCE_PROJECTION3 -> 25;
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
            case ELECTRIC_JUDGMENT1 -> electricJudgment(player, 3.5F, 0, false);
            case ELECTRIC_JUDGMENT2 -> electricJudgment(player, 5.0F, 1, false);
            case ELECTRIC_JUDGMENT3 -> electricJudgment(player, 6.5F, 2, false);
            case TUTAMINIS -> tutaminis(player, cap, 12);
            case WALL_OF_LIGHT -> wallOfLight(player, cap);
            case DRAIN1 -> drain(player, cap, 5.0F, 0);
            case DRAIN2 -> drain(player, cap, 7.0F, 1);
            case DRAIN3 -> drain(player, cap, 10.0F, 2);
            case LIGHTNING1 -> lightning(player, 4.0F, 0, false);
            case LIGHTNING2 -> lightning(player, 6.0F, 1, false);
            case LIGHTNING3 -> lightning(player, 8.0F, 2, false);
            case WOUND1 -> wound(player, cap, 2.0F, 0);
            case WOUND2 -> wound(player, cap, 4.0F, 1);
            case WOUND3 -> wound(player, cap, 6.0F, 2);
            case FORCE_SCREAM1 -> forceScream(player, cap, 0);
            case FORCE_SCREAM2 -> forceScream(player, cap, 1);
            case FORCE_SCREAM3 -> forceScream(player, cap, 2);
            case FORCE_DESTRUCTION1 -> forceDestruction(player, cap, 0);
            case FORCE_DESTRUCTION2 -> forceDestruction(player, cap, 1);
            case FORCE_DESTRUCTION3 -> forceDestruction(player, cap, 2);
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
            case PULL1 -> pull(player, cap, 3.5F, 0.95D, 12.0D);
            case PULL2 -> pull(player, cap, 5.5F, 1.20D, 16.0D);
            case PULL3 -> pull(player, cap, 7.5F, 1.48D, 20.0D);
            case FORCE_LEAP -> forceLeap(player, cap);
            case FORCE_PROJECTION1 -> forceProjection(player, cap, 0);
            case FORCE_PROJECTION2 -> forceProjection(player, cap, 1);
            case FORCE_PROJECTION3 -> forceProjection(player, cap, 2);
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
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_HEAL, 18, 1.20F);
        playCastSound(player, ModSounds.FORCE_HEAL.get(), 1.0F);
        return true;
    }

    private static boolean fortify(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        ForcePower effectPower = amplifier > 0 ? ForcePower.FORTIFY2 : ForcePower.FORTIFY1;
        addForceEffect(player, effectPower, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_BOOST, duration, 0);
        cap.beginVisual(effectPower, 8, ForceCapability.VISUAL_RIGHT_ARM);
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_FORTIFY, 20, 1.30F);
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
        target.hurt(player.damageSources().indirectMagic(player, player), 1.0F + amplifier);
        spawnTargetAbilityEffect(player, target, ForceAbilityEffectEntity.KIND_STUN, Math.max(12, duration), 1.10F);
        cap.beginVisual(effectPower, 7, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.AMBIENT_FORCE_STASIS.get(), 1.1F);
        return true;
    }

    private static boolean drain(ServerPlayer player, ForceCapability cap, float damage, int tier) {
        LivingEntity target = ForceTargeting.findTarget(player, 16.0D, 1.25D);
        if (target == null) {
            return false;
        }
        target.hurt(player.damageSources().indirectMagic(player, player), damage);
        player.heal(2.0F + tier * 2.0F);
        cap.addForce(12.0F + tier * 8.0F);
        addForceEffect(target, tier > 1 ? ForcePower.DRAIN3 : tier > 0 ? ForcePower.DRAIN2 : ForcePower.DRAIN1, 80 + tier * 40, tier);
        addHiddenVanillaEffect(target, MobEffects.WEAKNESS, 80 + tier * 40, tier);
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

        if (tryTutaminisAbsorb(target, player, damage, ForceBeamEffectEntity.KIND_LIGHTNING)) {
            spawnBeam(player, target, ForceBeamEffectEntity.KIND_LIGHTNING, lifeTicks);
            return true;
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


        spawnBeam(player, target, ForceBeamEffectEntity.KIND_LIGHTNING, lifeTicks);
        if (!sustained) {
            playCastSound(player, ModSounds.FORCE_LIGHTNING_START.get(), 1.0F + tier * 0.05F);
        }
        return true;
    }


    private static boolean electricJudgment(ServerPlayer player, float damage, int tier, boolean sustained) {
        double range = 16.0D + tier * 3.0D;
        LivingEntity target = ForceTargeting.findLightningTarget(player, range);
        int lifeTicks = sustained ? 4 : 7;

        if (target == null) {
            spawnBeam(player, ForceTargeting.findLookEnd(player, range), ForceBeamEffectEntity.KIND_JUDGMENT, lifeTicks);
            if (!sustained) {
                playCastSound(player, ModSounds.FORCE_LIGHTNING_START.get(), 1.18F + tier * 0.06F);
            }
            return false;
        }

        if (tryTutaminisAbsorb(target, player, damage, ForceBeamEffectEntity.KIND_JUDGMENT)) {
            spawnBeam(player, target, ForceBeamEffectEntity.KIND_JUDGMENT, lifeTicks);
            return true;
        }

        if (target instanceof ServerPlayer defender
                && LightsaberCombatEventHandler.tryBlockForceLightning(defender, player, damage, tier)) {
            spawnBeam(defender, player, ForceBeamEffectEntity.KIND_JUDGMENT, lifeTicks);
            return true;
        }

        if (!sustained || player.tickCount % 5 == 0) {
            target.invulnerableTime = 0;
            target.hurt(player.damageSources().indirectMagic(player, player), damage);
        }
        target.setDeltaMovement(0.0D, Math.min(target.getDeltaMovement().y, 0.0D), 0.0D);
        target.hurtMarked = true;
        ForcePower effectPower = tier > 1 ? ForcePower.ELECTRIC_JUDGMENT3 : tier > 0 ? ForcePower.ELECTRIC_JUDGMENT2 : ForcePower.ELECTRIC_JUDGMENT1;
        addForceEffect(target, effectPower, 30 + tier * 12, tier);
        addHiddenVanillaEffect(target, MobEffects.MOVEMENT_SLOWDOWN, 30 + tier * 10, 2 + tier);
        addHiddenVanillaEffect(target, MobEffects.WEAKNESS, 50 + tier * 15, tier);
        spawnBeam(player, target, ForceBeamEffectEntity.KIND_JUDGMENT, lifeTicks);
        if (!sustained) {
            playCastSound(player, ModSounds.FORCE_LIGHTNING_START.get(), 1.18F + tier * 0.06F);
        }
        return true;
    }

    private static boolean tryTutaminisAbsorb(LivingEntity defender, LivingEntity caster, float incomingDamage, int beamKind) {
        if (!hasForceEffect(defender, ForcePower.TUTAMINIS)) {
            return false;
        }
        defender.invulnerableTime = 0;
        addHiddenVanillaEffect(defender, MobEffects.DAMAGE_RESISTANCE, 30, 2);
        LivingEntity livingCaster = caster;
        if (livingCaster.isAlive()) {
            livingCaster.invulnerableTime = 0;
            livingCaster.hurt(defender.damageSources().indirectMagic(defender, defender), Math.max(1.0F, incomingDamage * 0.55F));
            Vec3 recoil = livingCaster.position().subtract(defender.position()).normalize().scale(0.35D);
            livingCaster.push(recoil.x, 0.15D, recoil.z);
            livingCaster.hurtMarked = true;
            if (defender.level() instanceof ServerLevel level) {
                level.addFreshEntity(new ForceBeamEffectEntity(level, defender, livingCaster, beamKind, 5));
            }
        }
        if (defender instanceof ServerPlayer player) {
            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                cap.addForce(Math.max(2.0F, incomingDamage * 1.15F));
                ForceCapabilityManager.sync(player);
            });
            spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_TUTAMINIS, 12, 0.165F);
        } else if (caster instanceof ServerPlayer player) {
            spawnTargetAbilityEffect(player, defender, ForceAbilityEffectEntity.KIND_TUTAMINIS, 12, 0.165F);
        }
        return true;
    }

    public static boolean isTutaminisActive(LivingEntity entity) {
        return hasForceEffect(entity, ForcePower.TUTAMINIS);
    }

    public static boolean tryConsumeTutaminisSaberBlock(LivingEntity defender) {
        if (!isTutaminisActive(defender)) {
            return false;
        }
        long gameTime = defender.level().getGameTime();
        CompoundTag data = defender.getPersistentData();
        long readyTime = data.getLong(TUTAMINIS_SABER_BLOCK_READY_TIME_TAG);
        if (readyTime > gameTime) {
            return false;
        }
        data.putLong(TUTAMINIS_SABER_BLOCK_READY_TIME_TAG, gameTime + TUTAMINIS_SABER_BLOCK_COOLDOWN_TICKS);
        return true;
    }

    public static void spawnTutaminisVisual(ServerPlayer player) {
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_TUTAMINIS, 14, 0.145F);
    }

    private static boolean tutaminis(ServerPlayer player, ForceCapability cap, int duration) {
        addForceEffect(player, ForcePower.TUTAMINIS, duration, 0);
        addHiddenVanillaEffect(player, MobEffects.FIRE_RESISTANCE, duration, 0);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, 1);
        cap.beginVisual(ForcePower.TUTAMINIS, 8, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM);
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_TUTAMINIS, 24, 0.155F);
        playCastSound(player, ModSounds.AMBIENT_FORCE_ENERGY_RESIST.get(), 1.10F);
        return true;
    }

    private static boolean wallOfLight(ServerPlayer player, ForceCapability cap) {
        float availableForce = cap.getCurrentForce();
        if (availableForce < 25.0F) {
            player.displayClientMessage(Component.literal("Wall of Light requires more Force energy."), true);
            return false;
        }

        LivingEntity target = ForceTargeting.findTarget(player, 22.0D, 1.65D);
        if (target == null || target == player || !target.isAlive() || !isLivingForceUser(target)) {
            player.displayClientMessage(Component.literal("Wall of Light must be focused on a living Force user."), true);
            return false;
        }

        cap.setCurrentForce(0.0F);
        drainWallOfLightCasterHealth(player);

        int severTicks = 20 * 60;
        addForceEffect(target, ForcePower.WALL_OF_LIGHT, severTicks, 0);
        addHiddenVanillaEffect(target, MobEffects.GLOWING, severTicks, 0);
        addHiddenVanillaEffect(target, MobEffects.WEAKNESS, severTicks, 1);
        addHiddenVanillaEffect(target, MobEffects.DIG_SLOWDOWN, severTicks, 1);

        if (target instanceof ServerPlayer severedPlayer) {
            severedPlayer.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(targetCap -> {
                targetCap.stopUsingPower();
                targetCap.setUseCooldownTicks(Math.max(targetCap.getUseCooldownTicks(), severTicks));
                ForceCapabilityManager.sync(severedPlayer);
            });
            severedPlayer.displayClientMessage(Component.literal("Your Force connection has been severed by a Wall of Light."), true);
        }

        spawnTargetAbilityEffect(player, target, ForceAbilityEffectEntity.KIND_WALL_OF_LIGHT, 52, Math.max(1.55F, target.getBbWidth() + 1.20F));
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_WALL_OF_LIGHT, 34, 1.35F);
        cap.beginVisual(ForcePower.WALL_OF_LIGHT, 14, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM);
        playCastSound(player, ModSounds.AMBIENT_FORCE_ENERGY_RESIST.get(), 1.35F);
        return true;
    }

    private static void drainWallOfLightCasterHealth(ServerPlayer player) {
        float healthCost = Math.min(8.0F, Math.max(0.0F, player.getHealth() - 1.0F));
        if (healthCost > 0.0F) {
            player.hurt(player.damageSources().magic(), healthCost);
        }
        addHiddenVanillaEffect(player, MobEffects.WEAKNESS, 120, 0);
        addHiddenVanillaEffect(player, MobEffects.MOVEMENT_SLOWDOWN, 50, 0);
    }

    private static boolean forceScream(ServerPlayer player, ForceCapability cap, int tier) {
        double radius = 6.0D + tier * 2.0D;
        AABB area = player.getBoundingBox().inflate(radius);
        Vec3 origin = player.position().add(0.0D, player.getBbHeight() * 0.55D, 0.0D);
        List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, area, target ->
                target != player && target.isAlive() && !target.isSpectator() && player.hasLineOfSight(target));
        if (targets.isEmpty()) {
            return false;
        }
        ForcePower effectPower = tier > 1 ? ForcePower.FORCE_SCREAM3 : tier > 0 ? ForcePower.FORCE_SCREAM2 : ForcePower.FORCE_SCREAM1;
        for (LivingEntity target : targets) {
            double dist = Math.max(0.35D, Math.sqrt(target.distanceToSqr(player)));
            float falloff = Mth.clamp((float) (1.0D - dist / radius), 0.25F, 1.0F);
            target.hurt(player.damageSources().indirectMagic(player, player), 3.5F + tier * 2.0F + 4.0F * falloff);
            Vec3 push = target.position().add(0.0D, target.getBbHeight() * 0.45D, 0.0D).subtract(origin).normalize().scale(0.75D + tier * 0.20D + falloff * 0.55D);
            target.push(push.x, 0.24D + falloff * 0.18D + tier * 0.05D, push.z);
            target.hurtMarked = true;
            addForceEffect(target, effectPower, 70 + tier * 30, tier);
            addHiddenVanillaEffect(target, MobEffects.WEAKNESS, 80 + tier * 25, 1 + tier);
            addHiddenVanillaEffect(target, MobEffects.MOVEMENT_SLOWDOWN, 60 + tier * 20, 1 + tier);
            spawnTargetAbilityEffect(player, target, ForceAbilityEffectEntity.KIND_SCREAM, 18 + tier * 4, 1.20F + tier * 0.15F);
        }
        cap.beginVisual(effectPower, 8, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM);
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_SCREAM, 18 + tier * 4, 2.10F + tier * 0.25F);
        playCastSound(player, ModSounds.FORCE_SCERAM.get(), 0.70F - tier * 0.05F);
        return true;
    }

    private static boolean forceDestruction(ServerPlayer player, ForceCapability cap, int tier) {
        double range = 16.0D + tier * 4.0D;
        Vec3 impact = ForceTargeting.findLookEnd(player, range);
        LivingEntity direct = ForceTargeting.findTarget(player, range, 1.45D + tier * 0.20D);
        if (direct != null) {
            impact = direct.position().add(0.0D, direct.getBbHeight() * 0.45D, 0.0D);
        }
        double radius = 3.25D + tier * 0.85D;
        Vec3 finalImpact = impact;

        // Visual red sphere travels from the user's hand to the impact point before the impact flare appears.
        spawnBeam(player, finalImpact, ForceBeamEffectEntity.KIND_DESTRUCTION_ORB, 10 + tier * 2);

        List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(impact, impact).inflate(radius), target ->
                target != player && target.isAlive() && !target.isSpectator() && target.distanceToSqr(finalImpact) <= radius * radius);
        if (targets.isEmpty()) {
            cap.beginVisual(tier > 1 ? ForcePower.FORCE_DESTRUCTION3 : tier > 0 ? ForcePower.FORCE_DESTRUCTION2 : ForcePower.FORCE_DESTRUCTION1, 8, ForceCapability.VISUAL_RIGHT_ARM);
            spawnAbilityEffect(player, null, ForceAbilityEffectEntity.KIND_DESTRUCTION, 14 + tier * 4, (float) radius);
            playCastSound(player, ModSounds.FORCE_CAST_DARK.get(), 1.25F + tier * 0.08F);
            return true;
        }
        ForcePower effectPower = tier > 1 ? ForcePower.FORCE_DESTRUCTION3 : tier > 0 ? ForcePower.FORCE_DESTRUCTION2 : ForcePower.FORCE_DESTRUCTION1;
        for (LivingEntity target : targets) {
            double dist = Math.sqrt(Math.max(0.0D, target.position().distanceToSqr(finalImpact)));
            float falloff = Mth.clamp((float) (1.0D - dist / radius), 0.30F, 1.0F);
            target.hurt(player.damageSources().indirectMagic(player, player), 6.0F + tier * 3.0F + 6.0F * falloff);
            target.setSecondsOnFire(3 + tier * 2);
            Vec3 push = target.position().subtract(finalImpact).normalize().scale(0.50D + tier * 0.15D + falloff * 0.55D);
            target.push(push.x, 0.32D + falloff * 0.22D + tier * 0.05D, push.z);
            target.hurtMarked = true;
            addForceEffect(target, effectPower, 70 + tier * 30, tier);
            spawnTargetAbilityEffect(player, target, ForceAbilityEffectEntity.KIND_DESTRUCTION, 18 + tier * 4, 1.30F + tier * 0.25F);
        }
        cap.beginVisual(effectPower, 8, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.FORCE_CAST_DARK.get(), 1.25F + tier * 0.08F);
        return true;
    }

    public static boolean canApplyForceShackles(LivingEntity target) {
        if (target == null || !target.isAlive() || target.isSpectator()) {
            return false;
        }
        if (target instanceof ForceUserEntity forceUser && forceUser.isGhost()) {
            return false;
        }
        if (!isLivingForceUser(target)) {
            return false;
        }
        float lowHealthThreshold = Math.max(4.0F, target.getMaxHealth() * 0.35F);
        return target.getHealth() <= lowHealthThreshold;
    }

    public static boolean applyForceShackles(LivingEntity target, LivingEntity source, int duration, int amplifier) {
        if (!canApplyForceShackles(target)) {
            return false;
        }
        target.setDeltaMovement(0.0D, Math.min(target.getDeltaMovement().y, 0.0D), 0.0D);
        target.hurtMarked = true;
        addForceEffect(target, ForcePower.FORCE_SHACKLES, duration, amplifier);
        addHiddenVanillaEffect(target, MobEffects.MOVEMENT_SLOWDOWN, duration, 5 + amplifier);
        addHiddenVanillaEffect(target, MobEffects.WEAKNESS, duration, 2 + amplifier);
        addHiddenVanillaEffect(target, MobEffects.DIG_SLOWDOWN, duration, 2 + amplifier);
        if (target instanceof ServerPlayer shackledPlayer) {
            shackledPlayer.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                cap.stopUsingPower();
                cap.setUseCooldownTicks(Math.max(cap.getUseCooldownTicks(), 40));
                ForceCapabilityManager.sync(shackledPlayer);
            });
            if (source instanceof ServerPlayer sourcePlayer) {
                spawnTargetAbilityEffect(sourcePlayer, shackledPlayer, ForceAbilityEffectEntity.KIND_SHACKLES, duration, 1.15F);
            }
        } else if (source instanceof ServerPlayer sourcePlayer) {
            spawnTargetAbilityEffect(sourcePlayer, target, ForceAbilityEffectEntity.KIND_SHACKLES, Math.min(duration, 160), 1.15F);
        }
        return true;
    }

    private static boolean isLivingForceUser(LivingEntity target) {
        if (target instanceof ForceUserEntity) {
            return true;
        }
        if (target instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getCapability(ForceProvider.FORCE_CAPABILITY)
                    .map(cap -> cap.hasPower(ForcePower.FORCE_SENSITIVITY))
                    .orElse(false);
        }
        return false;
    }

    private static boolean forceLeap(ServerPlayer player, ForceCapability cap) {
        CompoundTag persistentData = player.getPersistentData();
        if (player.onGround()) {
            persistentData.putInt(LEAP_CHAIN_TAG, 0);
        }
        int leapChain = persistentData.getInt(LEAP_CHAIN_TAG);
        if (!player.onGround() && leapChain >= 3) {
            player.displayClientMessage(Component.literal("Force Leap cannot exceed a triple-jump chain."), true);
            return false;
        }
        persistentData.putInt(LEAP_CHAIN_TAG, leapChain + 1);

        Vec3 look = player.getLookAngle().normalize();
        Vec3 current = player.getDeltaMovement();
        player.setDeltaMovement(current.x * 0.25D + look.x * 1.38D, Math.max(0.62D, current.y + 0.74D + Math.max(0.0D, look.y) * 0.30D), current.z * 0.25D + look.z * 1.38D);
        player.hurtMarked = true;
        player.resetFallDistance();
        addForceEffect(player, ForcePower.FORCE_LEAP, 80, 0);
        addHiddenVanillaEffect(player, MobEffects.SLOW_FALLING, 90, 0);
        addHiddenVanillaEffect(player, MobEffects.JUMP, 90, 1);
        cap.beginVisual(ForcePower.FORCE_LEAP, 6, ForceCapability.VISUAL_RIGHT_ARM);
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_LEAP, 16, 1.20F);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 1.35F);
        return true;
    }

    private static boolean forceProjection(ServerPlayer player, ForceCapability cap, int tier) {
        int duration = 160 + tier * 60;
        int cloneCount = 1 + tier;
        ForcePower effectPower = tier > 1 ? ForcePower.FORCE_PROJECTION3 : tier > 0 ? ForcePower.FORCE_PROJECTION2 : ForcePower.FORCE_PROJECTION1;
        addForceEffect(player, effectPower, duration, tier);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_RESISTANCE, 60 + tier * 20, 0);
        addHiddenVanillaEffect(player, MobEffects.MOVEMENT_SPEED, 80 + tier * 20, 0);

        List<ForceProjectionCloneEntity> clones = new java.util.ArrayList<>();
        if (player.level() instanceof ServerLevel level) {
            Vec3 look = player.getLookAngle().normalize();
            Vec3 side = new Vec3(0.0D, 1.0D, 0.0D).cross(look);
            if (side.lengthSqr() < 1.0E-6D) {
                side = new Vec3(1.0D, 0.0D, 0.0D);
            } else {
                side = side.normalize();
            }
            for (int i = 0; i < cloneCount; i++) {
                double spread = cloneCount == 1 ? 0.0D : (i - (cloneCount - 1) * 0.5D) * 1.75D;
                Vec3 pos = player.position().add(side.scale(spread)).add(look.scale(1.35D + i * 0.20D));
                ForceProjectionCloneEntity clone = new ForceProjectionCloneEntity(level, player, duration, tier);
                clone.moveTo(pos.x, player.getY(), pos.z, player.getYRot(), player.getXRot());
                clone.setDeltaMovement(look.scale(0.03D + tier * 0.01D));
                level.addFreshEntity(clone);
                clones.add(clone);
                spawnTargetAbilityEffect(player, clone, ForceAbilityEffectEntity.KIND_PROJECTION, 28 + tier * 8, 1.45F + tier * 0.20F);
            }

            List<Mob> nearbyMobs = player.level().getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(16.0D + tier * 4.0D), mob -> mob.getTarget() == player);
            for (int i = 0; i < nearbyMobs.size(); i++) {
                if (!clones.isEmpty()) {
                    nearbyMobs.get(i).setTarget(clones.get(i % clones.size()));
                }
            }
        }

        cap.beginVisual(effectPower, 8, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_LEFT_ARM);
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_PROJECTION, 32 + tier * 8, 1.60F + tier * 0.20F);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 0.78F + tier * 0.04F);
        return true;
    }

    private static boolean wound(ServerPlayer player, ForceCapability cap, float damage, int tier) {
        LivingEntity target = ForceTargeting.findTarget(player, 16.0D, 1.2D);
        if (target == null) {
            return false;
        }
        target.hurt(player.damageSources().indirectMagic(player, player), damage);
        ForcePower effectPower = tier > 1 ? ForcePower.WOUND3 : tier > 0 ? ForcePower.WOUND2 : ForcePower.WOUND1;
        addForceEffect(target, effectPower, 80 + tier * 20, tier);
        addHiddenVanillaEffect(target, MobEffects.LEVITATION, 10 + tier * 10, 0);
        addHiddenVanillaEffect(target, MobEffects.WEAKNESS, 80 + tier * 20, tier + 1);
        addHiddenVanillaEffect(target, MobEffects.MOVEMENT_SLOWDOWN, 80 + tier * 20, 4);
        spawnTargetAbilityEffect(player, target, ForceAbilityEffectEntity.KIND_WOUND, 16 + tier * 6, 1.05F);
        cap.beginVisual(effectPower, 7, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.FORCE_CAST_DARK.get(), 1.2F);
        return true;
    }

    private static boolean stealth(ServerPlayer player, ForceCapability cap, int duration) {
        addForceEffect(player, ForcePower.STEALTH, duration, 0);
        addHiddenVanillaEffect(player, MobEffects.INVISIBILITY, duration, 0);
        addHiddenVanillaEffect(player, MobEffects.MOVEMENT_SPEED, duration, 0);
        cap.beginVisual(ForcePower.STEALTH, 8, ForceCapability.VISUAL_RIGHT_ARM);
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_STEALTH, 22, 1.20F);
        playCastSound(player, ModSounds.FORCE_STEALTH_ON.get(), 1.0F);
        return true;
    }

    private static boolean speed(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        addForceEffect(player, ForcePower.SPEED, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.MOVEMENT_SPEED, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.JUMP, duration, amplifier);
        cap.beginVisual(ForcePower.SPEED, 6, ForceCapability.VISUAL_RIGHT_ARM);
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_SPEED, 14, 1.15F);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 1.15F);
        return true;
    }

    private static boolean sight(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        ForcePower effectPower = amplifier > 1 ? ForcePower.SIGHT3 : amplifier > 0 ? ForcePower.SIGHT2 : ForcePower.SIGHT1;
        addForceEffect(player, effectPower, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.NIGHT_VISION, duration, 0);

        // Force Sight is a perception/highlight power, not a forward cast beam.
        // It intentionally does not spawn a ForceAbilityEffectEntity renderer.
        // Every living entity in a radius around the player is highlighted, regardless of facing direction.
        applySightHighlights(player, amplifier, 100 + amplifier * 50);

        cap.beginVisual(effectPower, 4, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 1.25F);
        return true;
    }

    public static void tickSightHighlights(ServerPlayer player) {
        int amplifier = getActiveSightAmplifier(player);
        if (amplifier >= 0) {
            applySightHighlights(player, amplifier, 30 + amplifier * 10);
        }
    }

    private static int getActiveSightAmplifier(ServerPlayer player) {
        if (hasForceEffect(player, ForcePower.SIGHT3)) {
            return 2;
        }
        if (hasForceEffect(player, ForcePower.SIGHT2)) {
            return 1;
        }
        if (hasForceEffect(player, ForcePower.SIGHT1)) {
            return 0;
        }
        return -1;
    }

    private static boolean hasForceEffect(LivingEntity entity, ForcePower power) {
        net.minecraft.world.effect.MobEffect effect = ModEffects.getForceEffect(power);
        return effect != null && entity.hasEffect(effect);
    }

    private static void applySightHighlights(ServerPlayer player, int amplifier, int glowDuration) {
        double radius = 18.0D + amplifier * 8.0D;
        List<LivingEntity> targets = player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(radius),
                target -> target != player && target.isAlive() && target.distanceToSqr(player) <= radius * radius
        );
        for (LivingEntity target : targets) {
            addHiddenVanillaEffect(target, MobEffects.GLOWING, glowDuration, 0);
        }
    }

    private static boolean meditation(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        addForceEffect(player, amplifier > 1 ? ForcePower.MEDITATION3 : amplifier > 0 ? ForcePower.MEDITATION2 : ForcePower.MEDITATION1, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.REGENERATION, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, 0);
        cap.addForce(20.0F + amplifier * 15.0F);
        cap.beginVisual(amplifier > 1 ? ForcePower.MEDITATION3 : amplifier > 0 ? ForcePower.MEDITATION2 : ForcePower.MEDITATION1, 8, ForceCapability.VISUAL_RIGHT_ARM);
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_MEDITATION, 26, 1.45F);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 0.85F);
        return true;
    }

    private static boolean resist(ServerPlayer player, ForceCapability cap, int duration, int amplifier) {
        addForceEffect(player, amplifier > 1 ? ForcePower.RESIST3 : amplifier > 0 ? ForcePower.RESIST2 : ForcePower.RESIST1, duration, amplifier);
        addHiddenVanillaEffect(player, MobEffects.FIRE_RESISTANCE, duration, 0);
        addHiddenVanillaEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, amplifier);
        cap.beginVisual(amplifier > 1 ? ForcePower.RESIST3 : amplifier > 0 ? ForcePower.RESIST2 : ForcePower.RESIST1, 6, ForceCapability.VISUAL_RIGHT_ARM);
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_RESIST, 20, 1.35F);
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
            target.hurt(player.damageSources().indirectMagic(player, player), damage);
            target.push(push.x, upwardLift, push.z);
            target.hurtMarked = true;
        }
        spawnPushWave(player, (float) range);
        cap.beginVisual(damage >= 8.0F ? ForcePower.PUSH3 : damage >= 6.0F ? ForcePower.PUSH2 : ForcePower.PUSH1, 6, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_PUSH);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 0.95F);
        return true;
    }

    private static boolean pull(ServerPlayer player, ForceCapability cap, float damage, double force, double range) {
        List<LivingEntity> targets = ForceTargeting.findTargetsAlongRay(player, range, 2.60D);
        if (targets.isEmpty()) {
            return false;
        }
        Vec3 pullOrigin = player.position().add(0.0D, player.getBbHeight() * 0.55D, 0.0D);
        for (LivingEntity target : targets) {
            Vec3 targetCenter = target.position().add(0.0D, target.getBbHeight() * 0.45D, 0.0D);
            Vec3 pullVector = pullOrigin.subtract(targetCenter);
            if (pullVector.lengthSqr() < 1.0E-5D) {
                pullVector = player.getLookAngle().scale(-1.0D);
            }
            Vec3 motion = pullVector.normalize().scale(force);
            target.hurt(player.damageSources().indirectMagic(player, player), damage);
            target.push(motion.x, Math.max(0.18D, 0.24D + Math.max(0.0D, motion.y) * 0.45D), motion.z);
            target.hurtMarked = true;
            addForceEffect(target, damage >= 7.5F ? ForcePower.PULL3 : damage >= 5.5F ? ForcePower.PULL2 : ForcePower.PULL1, 35, Math.max(0, (int) ((damage - 3.5F) / 2.0F)));
            addHiddenVanillaEffect(target, MobEffects.MOVEMENT_SLOWDOWN, 20, 1);
        }
        spawnPushWave(player, (float) Math.min(range, 10.0D));
        cap.beginVisual(damage >= 7.5F ? ForcePower.PULL3 : damage >= 5.5F ? ForcePower.PULL2 : ForcePower.PULL1, 6, ForceCapability.VISUAL_RIGHT_ARM | ForceCapability.VISUAL_PUSH);
        playCastSound(player, ModSounds.FORCE_CAST.get(), 0.88F);
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
        boolean secondTier = returnSpeed > 1.2F;
        thrown.setOutboundTicks(secondTier ? 20 : 10);
        thrown.setMaxLifeTicks(secondTier ? 80 : 40);
        thrown.setPos(player.getX(), player.getEyeY() - 0.15D, player.getZ());
        thrown.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, secondTier ? 2.0F : 1.8F, 0.0F);
        player.level().addFreshEntity(thrown);
        player.getCooldowns().addCooldown(thrownStack.getItem(), secondTier ? 30 : 20);
        player.swing(InteractionHand.MAIN_HAND);
        cap.beginVisual(secondTier ? ForcePower.THROW2 : ForcePower.THROW1, 6, ForceCapability.VISUAL_RIGHT_ARM);
        playCastSound(player, ModSounds.LIGHTSABER_SWING.get(), 1.1F);
        return true;
    }

    private static void addForceEffect(LivingEntity entity, ForcePower power, int duration, int amplifier) {
        net.minecraft.world.effect.MobEffect effect = ModEffects.getForceEffect(power);
        if (effect != null) {
            entity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false, true));
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

    private static void spawnSelfAbilityEffect(ServerPlayer player, int kind, int lifeTicks, float radius) {
        spawnAbilityEffect(player, player, kind, lifeTicks, radius);
    }

    private static void spawnTargetAbilityEffect(ServerPlayer player, LivingEntity target, int kind, int lifeTicks, float radius) {
        spawnAbilityEffect(player, target, kind, lifeTicks, radius);
    }

    public static void spawnReboundVisual(ServerPlayer player, LivingEntity attacker) {
        spawnSelfAbilityEffect(player, ForceAbilityEffectEntity.KIND_REBOUND, 14, 1.75F);
        if (attacker != null) {
            spawnTargetAbilityEffect(player, attacker, ForceAbilityEffectEntity.KIND_REBOUND, 10, 1.10F);
        }
    }

    private static void spawnAbilityEffect(ServerPlayer player, LivingEntity anchor, int kind, int lifeTicks, float radius) {
        if (!(player.level() instanceof ServerLevel level) || anchor == null) {
            return;
        }
        level.addFreshEntity(new ForceAbilityEffectEntity(level, player, anchor == player ? null : anchor, kind, lifeTicks, radius));
    }
}
