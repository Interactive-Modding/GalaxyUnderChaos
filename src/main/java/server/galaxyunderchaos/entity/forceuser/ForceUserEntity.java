package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.entity.ForceAbilityEffectEntity;
import server.galaxyunderchaos.entity.ForceBeamEffectEntity;
import server.galaxyunderchaos.entity.ForcePushWaveEntity;
import server.galaxyunderchaos.entity.ForceProjectionCloneEntity;
import server.galaxyunderchaos.entity.FlashfireEntity;
import server.galaxyunderchaos.entity.NovadiveEntity;
import server.galaxyunderchaos.effect.ModEffects;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForcePowerHandler;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.sound.ModSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ForceUserEntity extends PathfinderMob {
    private static final EntityDataAccessor<String> DATA_SIDE = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_SPECIES = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_MODEL = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_TEXTURE = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_EYE_COLOR = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<ItemStack> DATA_BELT_LIGHTSABER = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_SABER_DRAWN = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_CASTING_TICKS = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_FOLLOW_MASTER = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_ORDER = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_ALIGNMENT_LEAN = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_RESPECT_BOW_TICKS = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.INT);

    private static final int STUDENT_GROWTH_TICKS = 100 * 24000;

    private ItemStack npcLightsaber = ItemStack.EMPTY;
    private final List<ForcePower> powers = new ArrayList<>();
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.literal("Force Master"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private int forceCooldownTicks;
    private int saberIdleSoundTicks;
    private UUID masterUuid;
    private UUID offerPlayerUuid;
    private int offerTicks;
    private int studentAgeTicks;
    private int studentQuestCompletions;
    private boolean fullyTrainedHandled;
    private boolean forceMentorBond;
    private long personalitySeed;
    private int teachingCooldownTicks;
    private int lowHealthHelpCooldownTicks;
    private int respectGreetingCooldownTicks;
    private boolean sithApprenticeReady;
    private boolean sithRivalChallenge;

    public ForceUserEntity(EntityType<? extends ForceUserEntity> type, Level level) {
        super(type, level);
        // Do not mark every naturally-spawned Force user as persistent.
        // Structure spawn overrides can roll these mobs repeatedly; making all of
        // them persistent caused Sith to accumulate permanently around every Sith
        // spawn location. Bonded students are still made persistent in bindToMaster().
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 34.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.31D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_SPEED, 1.6D)
                .add(Attributes.FOLLOW_RANGE, 28.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.18D);
    }

    public static AttributeSupplier.Builder createBossAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 68.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.31D)
                .add(Attributes.ATTACK_DAMAGE, 11.0D)
                .add(Attributes.ATTACK_SPEED, 1.8D)
                .add(Attributes.FOLLOW_RANGE, 36.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.32D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SIDE, defaultSide().serializedName());
        this.entityData.define(DATA_SPECIES, ForceUserSpecies.HUMAN_MALE.id());
        this.entityData.define(DATA_MODEL, ForceUserSpecies.HUMAN_MALE.modelId());
        this.entityData.define(DATA_TEXTURE, "human_male");
        this.entityData.define(DATA_EYE_COLOR, "blue");
        this.entityData.define(DATA_BELT_LIGHTSABER, ItemStack.EMPTY);
        this.entityData.define(DATA_SABER_DRAWN, false);
        this.entityData.define(DATA_CASTING_TICKS, 0);
        this.entityData.define(DATA_FOLLOW_MASTER, true);
        this.entityData.define(DATA_ORDER, CompanionOrder.FOLLOW_DEFEND.id());
        this.entityData.define(DATA_ALIGNMENT_LEAN, 0);
        this.entityData.define(DATA_RESPECT_BOW_TICKS, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.18D, true));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.85D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(ForceUserEntity.class));
        if (getForceUserRole().isGhost()) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true,
                    target -> target != this && target.isAlive() && !(target instanceof ForceUserEntity other && other.getForceUserRole().isGhost())));
        } else {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ForceUserEntity.class, 10, true, false,
                    target -> target instanceof ForceUserEntity other && shouldAttackForceUser(other)));
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                    target -> target instanceof Player player && shouldAggroPlayer(player)));
            this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Monster.class, true,
                    target -> this.getForceUserSide().isLight()));
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        if (dataTag == null || !dataTag.contains("ForceUserInitialized")) {
            randomizeLoadout(level.getRandom());
        } else {
            applyRoleStatsAndName(level.getRandom());
        }

        // Spawn-egg Force users are player-placed NPCs and should not despawn.
        // Natural/structure Jedi, Sith, and neutral Force users remain allowed to
        // despawn so planets do not accumulate permanent mobs over time.
        if (reason == MobSpawnType.SPAWN_EGG) {
            this.setPersistenceRequired();
        }

        // Force ghosts are meant to be persistent encounters/rewards: they should
        // not vanish just because the player walks away. Bonded students are still
        // handled by bindToMaster().
        if (getForceUserRole().isGhost()) {
            this.setPersistenceRequired();
        }

        return result;
    }

    private void randomizeLoadout(RandomSource random) {
        ForceUserRole role = getForceUserRole();
        ForceUserSide side = role.side();
        if (this.getType() == galaxyunderchaos.JEDI_TEMPLE_GUARD.get()) {
            setForceUserSide(ForceUserSide.LIGHT);
            setSpecies(ForceUserSpecies.HUMAN_MALE, "human_male");
            equipTempleGuardArmor();
            setEyeColor("brown");
            setNpcLightsaber(server.galaxyunderchaos.lightsaber.DoubleLightsaberData.create(
                    server.galaxyunderchaos.lightsaber.ModularLightsaberData.createCustomLightsaber("yellow", "temple_guard", "temple_guard", "temple_guard", "temple_guard"),
                    server.galaxyunderchaos.lightsaber.ModularLightsaberData.createCustomLightsaber("yellow", "temple_guard", "temple_guard", "temple_guard", "temple_guard")));
            this.powers.clear();
            this.powers.addAll(ForceUserLoadout.randomPowers(random, ForceUserSide.LIGHT, 2));
            setAlignmentLeaning(defaultAlignmentLeaning(random, role, ForceUserSide.LIGHT));
            applyRoleStatsAndName(random);
            return;
        }
        if (this.getType() == galaxyunderchaos.SITH_GUARD.get()) {
            setForceUserSide(ForceUserSide.DARK);
            setSpecies(ForceUserSpecies.HUMAN_MALE, "human_male");
            equipSithGuardArmor();
            setEyeColor("sith");
            setNpcLightsaber(server.galaxyunderchaos.lightsaber.DoubleLightsaberData.create(
                    server.galaxyunderchaos.lightsaber.ModularLightsaberData.createCustomLightsaber("red", "temple_guard", "temple_guard", "temple_guard", "temple_guard"),
                    server.galaxyunderchaos.lightsaber.ModularLightsaberData.createCustomLightsaber("red", "temple_guard", "temple_guard", "temple_guard", "temple_guard")));
            this.powers.clear();
            this.powers.addAll(ForceUserLoadout.randomPowers(random, ForceUserSide.DARK, 2));
            setAlignmentLeaning(defaultAlignmentLeaning(random, role, ForceUserSide.DARK));
            applyRoleStatsAndName(random);
            return;
        }
        ForceUserSpecies species = ForceUserSpecies.random(random);
        setForceUserSide(side);
        setSpecies(species, species.randomTexture(random));
        setEyeColor(randomEyeColor(random));

        float modifierChance;
        if (role.isGhost()) {
            modifierChance = role.isBoss() ? 0.30F : 0.12F;
        } else if (side.isDark()) {
            modifierChance = role.isBoss() ? 0.18F : 0.005F;
        } else {
            modifierChance = role.isBoss() ? 0.10F : 0.005F;
        }
        setNpcLightsaber(side.isNeutral()
                ? ForceUserLoadout.randomNeutralLightsaber(random)
                : ForceUserLoadout.randomLightsaber(random, side, modifierChance, role.isBoss()));
        this.powers.clear();
        this.powers.addAll(ForceUserLoadout.randomPowers(random, side, role.maxPowerTier()));
        if (this.personalitySeed == 0L) {
            this.personalitySeed = random.nextLong();
        }
        setAlignmentLeaning(defaultAlignmentLeaning(random, role, side));
        applyRoleStatsAndName(random);
    }

    private ForceUserSide defaultSide() {
        return getForceUserRole().side();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entityData.get(DATA_CASTING_TICKS) > 0) {
            this.entityData.set(DATA_CASTING_TICKS, this.entityData.get(DATA_CASTING_TICKS) - 1);
        }

        if (!this.level().isClientSide) {
            if (offerTicks > 0) {
                offerTicks--;
                if (offerTicks == 0) {
                    offerPlayerUuid = null;
                }
            }
            if (teachingCooldownTicks > 0) teachingCooldownTicks--;
            if (lowHealthHelpCooldownTicks > 0) lowHealthHelpCooldownTicks--;
            if (respectGreetingCooldownTicks > 0) respectGreetingCooldownTicks--;
            if (getRespectBowTicks() > 0) this.entityData.set(DATA_RESPECT_BOW_TICKS, getRespectBowTicks() - 1);
            tickRareAllegianceShift();
            tickPlayerBond();
            tickCombatLoadout();
            tickForcePowers();
            tickBossBar();
        }
    }

    private void tickCombatLoadout() {
        LivingEntity target = this.getTarget();
        boolean shouldDraw = target != null && target.isAlive() && this.distanceToSqr(target) <= 256.0D;
        if (shouldDraw) {
            drawLightsaber();
        } else {
            holsterLightsaber();
        }

        if (isSaberDrawn() && ++this.saberIdleSoundTicks >= 80) {
            this.saberIdleSoundTicks = 0;
            this.level().playSound(null, this.blockPosition(), ModSounds.LIGHTSABER_IDLE.get(), SoundSource.HOSTILE, 0.28F, 0.72F + this.random.nextFloat() * 0.18F);
        }
    }

    private void drawLightsaber() {
        if (isSaberDrawn() || this.npcLightsaber.isEmpty()) {
            return;
        }
        ItemStack combat = this.npcLightsaber.copy();
        ForceUserLoadout.setLightsaberActive(combat, true);
        this.setItemSlot(EquipmentSlot.MAINHAND, combat);
        this.entityData.set(DATA_SABER_DRAWN, true);
        this.level().playSound(null, this.blockPosition(), ModSounds.LIGHTSABER_TURN_ON.get(), SoundSource.HOSTILE, 0.65F, 0.9F + this.random.nextFloat() * 0.15F);
    }

    private void holsterLightsaber() {
        if (!isSaberDrawn()) {
            return;
        }
        ItemStack combat = this.getMainHandItem();
        if (!combat.isEmpty()) {
            ForceUserLoadout.setLightsaberActive(combat, false);
            setNpcLightsaber(combat);
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.entityData.set(DATA_SABER_DRAWN, false);
        this.level().playSound(null, this.blockPosition(), ModSounds.LIGHTSABER_TURN_OFF.get(), SoundSource.HOSTILE, 0.55F, 0.9F + this.random.nextFloat() * 0.15F);
    }

    private void tickForcePowers() {
        if (ForcePowerHandler.isForceSuppressed(this)) {
            this.forceCooldownTicks = Math.max(this.forceCooldownTicks, 30);
            return;
        }
        if (forceCooldownTicks > 0) {
            forceCooldownTicks--;
            return;
        }
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive() || this.powers.isEmpty()) {
            return;
        }
        if (!this.hasLineOfSight(target) || this.distanceToSqr(target) > 225.0D) {
            return;
        }

        ForcePower power = this.powers.get(this.random.nextInt(this.powers.size()));
        if (tryUsePower(power, target)) {
            this.entityData.set(DATA_CASTING_TICKS, 12);
            this.swing(InteractionHand.MAIN_HAND, true);
            this.forceCooldownTicks = 55 + this.random.nextInt(55);
        } else {
            this.forceCooldownTicks = 20 + this.random.nextInt(20);
        }
    }

    private boolean tryUsePower(ForcePower power, LivingEntity target) {
        return switch (power) {
            case HEAL1, HEAL2, HEAL3 -> castHeal();
            case FORTIFY1, FORTIFY2, FORTIFY3 -> castFortify();
            case RESIST1, RESIST2, RESIST3, TUTAMINIS -> castTutaminis();
            case STUN1, STUN2, STUN3 -> castStun(target);
            case ELECTRIC_JUDGMENT1 -> castElectricJudgment(target, 0);
            case ELECTRIC_JUDGMENT2 -> castElectricJudgment(target, 1);
            case ELECTRIC_JUDGMENT3 -> castElectricJudgment(target, 2);
            case DRAIN1, DRAIN2, DRAIN3 -> castDrain(target);
            case LIGHTNING1, LIGHTNING2, LIGHTNING3 -> castLightning(target);
            case WOUND1, WOUND2, WOUND3 -> castWound(target);
            case FORCE_SCREAM1 -> castScream(target, 0);
            case FORCE_SCREAM2 -> castScream(target, 1);
            case FORCE_SCREAM3 -> castScream(target, 2);
            case FORCE_DESTRUCTION1 -> castDestruction(target, 0);
            case FORCE_DESTRUCTION2 -> castDestruction(target, 1);
            case FORCE_DESTRUCTION3 -> castDestruction(target, 2);
            case SPEED -> castSpeed();
            case FORCE_LEAP -> castSpeedLeap(target);
            case FORCE_PROJECTION1 -> castProjection(0);
            case FORCE_PROJECTION2 -> castProjection(1);
            case FORCE_PROJECTION3 -> castProjection(2);
            case PUSH1, PUSH2, PUSH3 -> castPush(target);
            case PULL1, PULL2, PULL3 -> castPull(target);
            default -> false;
        };
    }

    private boolean castHeal() {
        if (this.getHealth() > this.getMaxHealth() * 0.65F) {
            return false;
        }
        this.heal(6.0F);
        this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, false, false, false));
        spawnAbilityEffect(this, ForceAbilityEffectEntity.KIND_HEAL, 18, 1.20F);
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_HEAL.get(), SoundSource.HOSTILE, 0.8F, 1.0F);
        return true;
    }

    private boolean castFortify() {
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 160, 0, false, false, false));
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 0, false, false, false));
        spawnAbilityEffect(this, ForceAbilityEffectEntity.KIND_FORTIFY, 20, 1.30F);
        this.level().playSound(null, this.blockPosition(), ModSounds.AMBIENT_FORCE_FORTIFY.get(), SoundSource.HOSTILE, 0.75F, 1.0F);
        return true;
    }

    private boolean castResist() {
        this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 160, 0, false, false, false));
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 160, 0, false, false, false));
        spawnAbilityEffect(this, ForceAbilityEffectEntity.KIND_RESIST, 24, 1.50F);
        this.level().playSound(null, this.blockPosition(), ModSounds.AMBIENT_FORCE_ENERGY_RESIST.get(), SoundSource.HOSTILE, 0.75F, 1.0F);
        return true;
    }


    private boolean castTutaminis() {
        net.minecraft.world.effect.MobEffect tutaminisEffect = ModEffects.getForceEffect(ForcePower.TUTAMINIS);
        if (tutaminisEffect != null) {
            this.addEffect(new MobEffectInstance(tutaminisEffect, 220, 0, false, false, true));
        }
        this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 220, 0, false, false, false));
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 220, 1, false, false, false));
        spawnAbilityEffect(this, ForceAbilityEffectEntity.KIND_TUTAMINIS, 26, 0.155F);
        this.level().playSound(null, this.blockPosition(), ModSounds.AMBIENT_FORCE_ENERGY_RESIST.get(), SoundSource.HOSTILE, 0.80F, 1.10F);
        return true;
    }

    private boolean castSpeed() {
        this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 140, 1, false, false, false));
        this.addEffect(new MobEffectInstance(MobEffects.JUMP, 140, 0, false, false, false));
        spawnAbilityEffect(this, ForceAbilityEffectEntity.KIND_SPEED, 14, 1.15F);
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_CAST.get(), SoundSource.HOSTILE, 0.75F, 1.15F);
        return true;
    }

    private boolean castStun(LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3, false, false, false));
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, false, false, false));
        target.hurt(this.damageSources().magic(), 2.0F);
        spawnAbilityEffect(target, ForceAbilityEffectEntity.KIND_STUN, 60, 1.10F);
        this.level().playSound(null, this.blockPosition(), ModSounds.AMBIENT_FORCE_STASIS.get(), SoundSource.HOSTILE, 0.8F, 1.05F);
        return true;
    }


    private boolean castElectricJudgment(LivingEntity target, int tier) {
        float damage = 3.5F + tier * 1.5F;
        target.invulnerableTime = 0;
        target.hurt(this.damageSources().indirectMagic(this, this), damage);
        target.setDeltaMovement(0.0D, Math.min(target.getDeltaMovement().y, 0.0D), 0.0D);
        target.hurtMarked = true;
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40 + tier * 12, 2 + tier, false, false, false));
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 70 + tier * 18, tier, false, false, false));
        spawnBeam(target, ForceBeamEffectEntity.KIND_JUDGMENT, 7);
        spawnAbilityEffect(target, ForceAbilityEffectEntity.KIND_STUN, 18 + tier * 4, 1.05F + tier * 0.15F);
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_LIGHTNING_START.get(), SoundSource.HOSTILE, 0.75F, 1.22F + tier * 0.05F);
        return true;
    }

    private boolean castDrain(LivingEntity target) {
        target.hurt(this.damageSources().indirectMagic(this, this), 5.0F);
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, false, false, false));
        this.heal(4.0F);
        spawnBeam(target, ForceBeamEffectEntity.KIND_DRAIN, 7);
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_CAST_DARK.get(), SoundSource.HOSTILE, 0.8F, 0.95F);
        return true;
    }

    private boolean castLightning(LivingEntity target) {
        target.invulnerableTime = 0;
        target.hurt(this.damageSources().indirectMagic(this, this), 4.0F);
        target.setDeltaMovement(0.0D, Math.min(target.getDeltaMovement().y, 0.0D), 0.0D);
        target.hurtMarked = true;
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 3, false, false, false));
        spawnBeam(target, ForceBeamEffectEntity.KIND_LIGHTNING, 6);
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_LIGHTNING_START.get(), SoundSource.HOSTILE, 0.85F, 1.0F);
        return true;
    }

    private boolean castWound(LivingEntity target) {
        target.hurt(this.damageSources().magic(), 4.0F);
        target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 12, 0, false, false, false));
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1, false, false, false));
        spawnAbilityEffect(target, ForceAbilityEffectEntity.KIND_WOUND, 18, 1.05F);
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_CAST_DARK.get(), SoundSource.HOSTILE, 0.85F, 1.1F);
        return true;
    }


    private boolean castScream(LivingEntity primaryTarget, int tier) {
        double radius = 6.0D + tier * 2.0D;
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius), target ->
                target != this && target.isAlive() && !target.isSpectator() && !isAlliedTo(target) && this.hasLineOfSight(target));
        if (targets.isEmpty() && primaryTarget != null && primaryTarget.isAlive()) {
            targets = List.of(primaryTarget);
        }
        if (targets.isEmpty()) {
            return false;
        }
        Vec3 origin = this.position().add(0.0D, this.getBbHeight() * 0.55D, 0.0D);
        for (LivingEntity target : targets) {
            target.hurt(this.damageSources().indirectMagic(this, this), 4.5F + tier * 2.0F);
            Vec3 push = target.position().add(0.0D, target.getBbHeight() * 0.45D, 0.0D).subtract(origin).normalize().scale(1.05D + tier * 0.20D);
            target.push(push.x, 0.30D + tier * 0.05D, push.z);
            target.hurtMarked = true;
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80 + tier * 25, 1 + tier, false, false, false));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60 + tier * 20, 1 + tier, false, false, false));
            spawnAbilityEffect(target, ForceAbilityEffectEntity.KIND_SCREAM, 18 + tier * 4, 1.20F + tier * 0.15F);
        }
        spawnAbilityEffect(this, ForceAbilityEffectEntity.KIND_SCREAM, 18 + tier * 4, 2.10F + tier * 0.25F);
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_SCERAM.get(), SoundSource.HOSTILE, 0.85F, 0.70F - tier * 0.05F);
        return true;
    }

    private boolean castDestruction(LivingEntity target, int tier) {
        Vec3 impact = target.position().add(0.0D, target.getBbHeight() * 0.45D, 0.0D);
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.addFreshEntity(new ForceBeamEffectEntity(serverLevel, this, impact, ForceBeamEffectEntity.KIND_DESTRUCTION_ORB, 10 + tier * 2));
        }
        double radius = 3.0D + tier * 0.80D;
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, new net.minecraft.world.phys.AABB(impact, impact).inflate(radius), hit ->
                hit != this && hit.isAlive() && !hit.isSpectator() && !isAlliedTo(hit));
        if (targets.isEmpty()) {
            targets = List.of(target);
        }
        for (LivingEntity hit : targets) {
            hit.hurt(this.damageSources().indirectMagic(this, this), 7.0F + tier * 3.0F);
            hit.setSecondsOnFire(3 + tier * 2);
            Vec3 push = hit.position().subtract(this.position()).normalize().scale(0.85D + tier * 0.20D);
            hit.push(push.x, 0.38D + tier * 0.06D, push.z);
            hit.hurtMarked = true;
            spawnAbilityEffect(hit, ForceAbilityEffectEntity.KIND_DESTRUCTION, 20 + tier * 4, 1.35F + tier * 0.20F);
        }
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_CAST_DARK.get(), SoundSource.HOSTILE, 0.90F, 1.35F + tier * 0.05F);
        return true;
    }

    private boolean castPush(LivingEntity target) {
        Vec3 push = target.position().subtract(this.position()).normalize().scale(1.15D);
        target.hurt(this.damageSources().magic(), 3.5F);
        target.push(push.x, 0.42D, push.z);
        target.hurtMarked = true;
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.addFreshEntity(new ForcePushWaveEntity(serverLevel, this, 6.0F, 10));
        }
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_CAST.get(), SoundSource.HOSTILE, 0.8F, 0.95F);
        return true;
    }

    private boolean castPull(LivingEntity target) {
        Vec3 pull = this.position().add(0.0D, this.getBbHeight() * 0.55D, 0.0D)
                .subtract(target.position().add(0.0D, target.getBbHeight() * 0.45D, 0.0D));
        if (pull.lengthSqr() < 1.0E-5D) {
            pull = this.getLookAngle().scale(-1.0D);
        }
        Vec3 motion = pull.normalize().scale(1.10D);
        target.hurt(this.damageSources().magic(), 3.0F);
        target.push(motion.x, 0.34D, motion.z);
        target.hurtMarked = true;
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.addFreshEntity(new ForcePushWaveEntity(serverLevel, this, 5.0F, 10));
        }
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_CAST.get(), SoundSource.HOSTILE, 0.8F, 0.88F);
        return true;
    }


    private boolean castSpeedLeap(LivingEntity target) {
        Vec3 direction = target != null && target.isAlive()
                ? target.position().subtract(this.position()).normalize()
                : this.getLookAngle().normalize();
        Vec3 current = this.getDeltaMovement();
        this.setDeltaMovement(current.x * 0.20D + direction.x * 1.15D, Math.max(0.50D, current.y + 0.62D), current.z * 0.20D + direction.z * 1.15D);
        this.hurtMarked = true;
        this.resetFallDistance();
        this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false, false));
        this.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 80, 0, false, false, false));
        spawnAbilityEffect(this, ForceAbilityEffectEntity.KIND_LEAP, 16, 1.20F);
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_CAST.get(), SoundSource.HOSTILE, 0.75F, 1.35F);
        return true;
    }

    private boolean castProjection(int tier) {
        int duration = 120 + tier * 50;
        int cloneCount = 1 + tier;
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120 + tier * 30, 0, false, false, false));
        if (this.level() instanceof ServerLevel level) {
            Vec3 look = this.getLookAngle().normalize();
            Vec3 side = new Vec3(0.0D, 1.0D, 0.0D).cross(look);
            if (side.lengthSqr() < 1.0E-6D) {
                side = new Vec3(1.0D, 0.0D, 0.0D);
            } else {
                side = side.normalize();
            }
            List<ForceProjectionCloneEntity> clones = new ArrayList<>();
            for (int i = 0; i < cloneCount; i++) {
                double spread = cloneCount == 1 ? 0.0D : (i - (cloneCount - 1) * 0.5D) * 1.65D;
                Vec3 pos = this.position().add(side.scale(spread)).add(look.scale(1.15D + i * 0.20D));
                ForceProjectionCloneEntity clone = new ForceProjectionCloneEntity(level, this, duration, tier);
                clone.moveTo(pos.x, this.getY(), pos.z, this.getYRot(), this.getXRot());
                level.addFreshEntity(clone);
                clones.add(clone);
                spawnAbilityEffect(clone, ForceAbilityEffectEntity.KIND_PROJECTION, 28 + tier * 8, 1.45F + tier * 0.20F);
            }
            LivingEntity currentTarget = this.getTarget();
            if (currentTarget instanceof Mob mob && !clones.isEmpty()) {
                mob.setTarget(clones.get(this.random.nextInt(clones.size())));
            }
        }
        spawnAbilityEffect(this, ForceAbilityEffectEntity.KIND_PROJECTION, 28 + tier * 8, 1.60F + tier * 0.20F);
        this.level().playSound(null, this.blockPosition(), ModSounds.FORCE_CAST.get(), SoundSource.HOSTILE, 0.70F, 0.78F + tier * 0.04F);
        return true;
    }

    private void spawnBeam(LivingEntity target, int kind, int lifeTicks) {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.addFreshEntity(new ForceBeamEffectEntity(serverLevel, this, target, kind, lifeTicks));
        }
    }

    private void spawnAbilityEffect(LivingEntity anchor, int kind, int lifeTicks, float radius) {
        if (this.level() instanceof ServerLevel serverLevel && anchor != null) {
            serverLevel.addFreshEntity(new ForceAbilityEffectEntity(serverLevel, this, anchor == this ? null : anchor, kind, lifeTicks, radius));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof Player player && masterUuid != null && masterUuid.equals(player.getUUID()) && !isSithRivalOf(player) && player.getHealth() <= 6.0F) {
            this.setTarget(null);
            return false;
        }
        boolean result = super.doHurtTarget(target);
        if (!this.level().isClientSide && target instanceof LivingEntity living) {
            living.invulnerableTime = 0;
            if (this.getMainHandItem().getItem() instanceof LightsaberItem) {
                this.level().playSound(null, this.blockPosition(), ModSounds.LIGHTSABER_HIT.get(), SoundSource.HOSTILE, 0.9F, 0.9F + this.random.nextFloat() * 0.2F);
            }
        }
        return result;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        ForceUserRole role = getForceUserRole();
        if (source.is(DamageTypes.FALL) && (role.isStudent() || role.isBoss())) {
            return false;
        }

        Entity attacker = source.getEntity() != null ? source.getEntity() : source.getDirectEntity();
        if (canSaberBlock(source)) {
            amount *= getForceUserRole().isBoss() ? 0.18F : 0.33F;
            this.level().playSound(null, this.blockPosition(), ModSounds.LIGHTSABER_HIT.get(), SoundSource.HOSTILE, 0.25F, 1.35F + this.random.nextFloat() * 0.15F);
        }
        boolean result = super.hurt(source, amount);
        if (result && attacker instanceof LivingEntity living && living != this) {
            drawLightsaber();
            if (attacker instanceof Player player && masterUuid != null && masterUuid.equals(player.getUUID())) {
                if (this.getHealth() <= this.getMaxHealth() * 0.35F) {
                    this.setTarget(player);
                } else {
                    this.setTarget(null);
                }
            } else if (!isAlliedTo(living)) {
                this.setTarget(living);
            }
        }
        return result;
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity == this) {
            return true;
        }
        if (entity instanceof ForceUserEntity other) {
            if (this.getForceUserRole().isGhost() && !other.getForceUserRole().isGhost()) {
                return false;
            }
            if (this.masterUuid != null && this.masterUuid.equals(other.masterUuid)) {
                return true;
            }
            ForceUserSide mine = this.getForceUserSide();
            ForceUserSide theirs = other.getForceUserSide();
            if (mine == theirs) {
                return true;
            }
            return mine.isAlignedAgainstDark() && theirs.isAlignedAgainstDark();
        }
        if (entity instanceof Player player) {
            if (this.getForceUserRole().isGhost()) {
                return false;
            }
            if (isSithRivalOf(player)) {
                return false;
            }
            if (masterUuid != null && masterUuid.equals(player.getUUID())) {
                return true;
            }
            ForceSide playerSide = getPlayerForceSide(player);
            if (getForceUserSide().isDark()) {
                return playerSide == ForceSide.DARK;
            }
            return playerSide == ForceSide.LIGHT || playerSide == ForceSide.NEUTRAL;
        }
        return super.isAlliedTo(entity);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        ForceUserRole role = getForceUserRole();
        RandomSource random = this.getRandom();

        if (role.isGhost()) {
            // Ghosts do not drop normal loot, but their equipped saber is the guaranteed reward.
            dropStoredLightsaber();
            return;
        }

        super.dropCustomDeathLoot(source, looting, recentlyHit);
        ForceUserSide side = this.getForceUserSide();

        // 100% regular side holobook, not saber form holobooks.
        this.spawnAtLocation(ForceUserLoadout.holobookFor(side));

        // Sith apprentices are trained weapon carriers, so their saber drop is much higher.
        float saberDropChance = role.isApprentice() ? 0.50F : 0.05F;
        if (random.nextFloat() < saberDropChance) {
            dropStoredLightsaber();
        }

        // 10% side datacron.
        if (random.nextFloat() < 0.10F) {
            this.spawnAtLocation(ForceUserLoadout.datacronFor(side));
        }

        // 0.1% hyperdrive / portal item.
        if (random.nextFloat() < 0.001F) {
            this.spawnAtLocation(new ItemStack(galaxyunderchaos.PORTAL_ITEM.get()));
        }
    }

    private void dropStoredLightsaber() {
        ItemStack drop = getStoredLightsaber().copy();
        if (!drop.isEmpty()) {
            ForceUserLoadout.setLightsaberActive(drop, false);
            this.spawnAtLocation(drop);
        }
    }


    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            greetRespectfulMaster(serverPlayer);
            if (isBoundTo(serverPlayer)) {
                ForceUserInteractionMenu.openMain(serverPlayer, this);
                return InteractionResult.CONSUME;
            }

            if (canUnlockForceSensitivityForPlayer()) {
                if (hasMaster()) {
                    player.displayClientMessage(Component.literal("This Force user already follows someone else."), true);
                    return InteractionResult.CONSUME;
                }
                if (ForceTrainingManager.hasForceMentor(serverPlayer)) {
                    player.displayClientMessage(Component.literal("You already have a living Force mentor. If that mentor dies, you may choose another."), true);
                    return InteractionResult.CONSUME;
                }
                UUID playerId = serverPlayer.getUUID();
                if (playerId.equals(offerPlayerUuid) && offerTicks > 0) {
                    ForceTrainingManager.tryChooseForceMentor(serverPlayer, this);
                    offerTicks = 0;
                    offerPlayerUuid = null;
                    return InteractionResult.CONSUME;
                }
                offerPlayerUuid = playerId;
                offerTicks = 200;
                String reason = playerHasForceSensitivity(serverPlayer)
                        ? "continue your Force training"
                        : "unlock Force Sensitivity";
                player.displayClientMessage(Component.literal("Right-click again to choose " + this.getDisplayName().getString() + " as your Force mentor and " + reason + ". After bonding, right-click to open Interact / Order menus."), false);
                return InteractionResult.CONSUME;
            }

            if (getForceUserRole().isStudent()) {
                if (hasMaster()) {
                    player.displayClientMessage(Component.literal("This student already follows a master."), true);
                    return InteractionResult.CONSUME;
                }
                if (!ForceTrainingManager.hasKnightStandingForStudent(serverPlayer, getForceUserRole())) {
                    player.displayClientMessage(Component.literal("You cannot take a student yet. Complete mentor quests and earn Knight-equivalent standing first."), true);
                    player.displayClientMessage(Component.literal(ForceTrainingManager.mentorProgressLine(serverPlayer, getForceUserRole().side().toCapabilitySide())), false);
                    return InteractionResult.CONSUME;
                }
                UUID playerId = serverPlayer.getUUID();
                if (playerId.equals(offerPlayerUuid) && offerTicks > 0) {
                    ForceTrainingManager.tryAccept(serverPlayer, this);
                    offerTicks = 0;
                    offerPlayerUuid = null;
                    return InteractionResult.CONSUME;
                }
                offerPlayerUuid = playerId;
                offerTicks = 200;
                player.displayClientMessage(Component.literal("Right-click again to accept " + this.getDisplayName().getString() + " as your "
                        + studentLabel() + ". After bonding, right-click to open Interact / Order menus."), false);
                return InteractionResult.CONSUME;
            }
        }
        return super.mobInteract(player, hand);
    }

    public void bindToMaster(ServerPlayer player) {
        this.masterUuid = player.getUUID();
        this.forceMentorBond = false;
        this.setTarget(null);
        this.setPersistenceRequired();
        this.studentAgeTicks = 0;
        this.studentQuestCompletions = 0;
        this.fullyTrainedHandled = false;
        this.sithApprenticeReady = false;
        this.sithRivalChallenge = false;
        if (getForceUserRole().isApprentice()) {
            this.setCustomName(Component.literal(namedWithRank("Sith Acolyte")));
        }
        setFollowingMaster(true);
        setCompanionOrder(CompanionOrder.FOLLOW_DEFEND);
    }

    public void bindAsForceMentor(ServerPlayer player) {
        this.masterUuid = player.getUUID();
        this.forceMentorBond = true;
        this.setTarget(null);
        this.setPersistenceRequired();
        setCompanionOrder(CompanionOrder.FOLLOW_DEFEND);
    }

    /**
     * Despawn policy:
     * - Force ghosts persist until killed.
     * - Bonded mentors/students persist because they belong to a player.
     * - Natural Jedi/Sith, including unbonded apprentices/padawans, may despawn
     *   when far away and later respawn from biome/structure rules.
     */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        ForceUserRole role = getForceUserRole();
        if (role.isGhost()) {
            return false;
        }
        if (hasMaster()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean requiresCustomPersistence() {
        ForceUserRole role = getForceUserRole();
        return role.isGhost() || hasMaster() || super.requiresCustomPersistence();
    }

    public boolean hasMaster() {
        return masterUuid != null;
    }

    public boolean canUnlockForceSensitivityForPlayer() {
        EntityType<?> type = this.getType();
        ForceUserRole role = getForceUserRole();
        // Mentor-capable Force users must be actual teachers. A graduated Jedi or
        // neutral padawan is promoted in-place into a Knight, so it can mentor even
        // though the underlying entity type is still a padawan type.
        if (role.isPadawan() && fullyTrainedHandled) {
            return true;
        }
        // SITH_APPRENTICE is the acolyte/student entity in this system, so it
        // must never offer first-step mentorship. The Sith non-lord teacher is
        // SITH_FORCE_USER, which displays as a Sith Apprentice.
        return type == galaxyunderchaos.JEDI_FORCE_USER.get()
                || type == galaxyunderchaos.JEDI_MASTER.get()
                || type == galaxyunderchaos.NEUTRAL_FORCE_USER.get()
                || type == galaxyunderchaos.NEUTRAL_MASTER.get()
                || type == galaxyunderchaos.SITH_FORCE_USER.get()
                || type == galaxyunderchaos.SITH_LORD.get();
    }

    private boolean playerHasForceSensitivity(ServerPlayer player) {
        return player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(cap -> cap.hasPower(ForcePower.FORCE_SENSITIVITY))
                .orElse(false);
    }

    private void tickPlayerBond() {
        if (masterUuid == null) {
            return;
        }
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        Entity entity = serverLevel.getEntity(masterUuid);
        if (!(entity instanceof ServerPlayer master)) {
            return;
        }

        tickMasterShipRiding(master);

        if (getCompanionOrder() == CompanionOrder.STAY) {
            this.getNavigation().stop();
        }

        if (getForceUserRole().isApprentice() && sithApprenticeReady && !sithRivalChallenge && ForceTrainingManager.isDarkLord(master)) {
            beginSithRivalChallenge(master);
        }
        if (sithRivalChallenge && getForceUserRole().isApprentice()) {
            drawLightsaber();
            setFollowingMaster(false);
            if (master.isAlive()) {
                this.setTarget(master);
                if (this.distanceToSqr(master) > 4.0D) {
                    this.getNavigation().moveTo(master, 1.25D);
                }
            }
            return;
        }

        boolean bondedSupporter = hasMaster() && (getForceUserRole().isStudent() || forceMentorBond);
        if (bondedSupporter && lowHealthHelpCooldownTicks <= 0) {
            boolean masterLow = master.getHealth() <= master.getMaxHealth() * 0.45F;
            boolean selfLow = this.getHealth() <= this.getMaxHealth() * 0.35F;
            if (getForceUserSide().isAlignedAgainstDark() && masterLow) {
                lowHealthHelpCooldownTicks = 180;
                master.heal(4.0F);
                master.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 0, false, false, true));
                master.displayClientMessage(Component.literal(this.getDisplayName().getString() + ": Hold on — I can still mend your wounds."), true);
            } else if (getForceUserSide().isAlignedAgainstDark() && selfLow) {
                lowHealthHelpCooldownTicks = 220;
                master.displayClientMessage(Component.literal(this.getDisplayName().getString() + ": I am wounded. I will recover and rejoin you."), false);
                this.heal(3.0F);
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 1, false, false, false));
                this.setTarget(null);
                if (this.distanceToSqr(master) > 9.0D) {
                    this.getNavigation().moveTo(master, 1.35D);
                }
            } else if (getForceUserSide().isDark() && (masterLow || selfLow)) {
                LivingEntity drainTarget = supportDrainTarget(master);
                if (drainTarget != null) {
                    lowHealthHelpCooldownTicks = 180;
                    castDrain(drainTarget);
                    if (masterLow) {
                        master.heal(2.0F);
                    }
                    master.displayClientMessage(Component.literal(this.getDisplayName().getString() + ": I will take strength from our enemy."), true);
                }
            }
        }

        if (isFollowingMaster()) {
            double dist = this.distanceToSqr(master);
            if (dist > 144.0D) {
                this.teleportTo(master.getX() + (this.random.nextDouble() - 0.5D) * 2.0D, master.getY(), master.getZ() + (this.random.nextDouble() - 0.5D) * 2.0D);
            } else if (dist > 16.0D && this.getTarget() == null) {
                this.getNavigation().moveTo(master, 1.15D);
            }
            if (this.getTarget() == null && master.getLastHurtByMob() != null && master.getLastHurtByMob().isAlive() && !isAlliedTo(master.getLastHurtByMob())) {
                drawLightsaber();
                this.setTarget(master.getLastHurtByMob());
            }
        }
    }

    @Nullable
    private LivingEntity supportDrainTarget(ServerPlayer master) {
        LivingEntity target = this.getTarget();
        if (isValidSupportDrainTarget(master, target)) {
            return target;
        }
        LivingEntity attacker = master.getLastHurtByMob();
        if (isValidSupportDrainTarget(master, attacker)) {
            this.setTarget(attacker);
            return attacker;
        }
        List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(10.0D), candidate -> isValidSupportDrainTarget(master, candidate));
        return nearby.isEmpty() ? null : nearby.get(0);
    }

    private boolean isValidSupportDrainTarget(ServerPlayer master, @Nullable LivingEntity candidate) {
        return candidate != null
                && candidate.isAlive()
                && candidate != this
                && candidate != master
                && !isAlliedTo(candidate)
                && this.hasLineOfSight(candidate)
                && this.distanceToSqr(candidate) <= 144.0D;
    }

    private void tickMasterShipRiding(ServerPlayer master) {
        Entity masterVehicle = master.getVehicle();
        boolean masterInShip = isTravelShip(masterVehicle);

        if (!isFollowingMaster()) {
            if (isTravelShip(this.getVehicle())) {
                this.stopRiding();
            }
            return;
        }

        if (masterInShip) {
            if (this.getVehicle() != masterVehicle) {
                this.stopRiding();
                this.setTarget(null);
                this.getNavigation().stop();
                this.startRiding(masterVehicle, true);
            }
            return;
        }

        Entity currentVehicle = this.getVehicle();
        if (isTravelShip(currentVehicle)) {
            this.stopRiding();
            this.teleportTo(master.getX() + (this.random.nextDouble() - 0.5D) * 2.0D, master.getY(), master.getZ() + (this.random.nextDouble() - 0.5D) * 2.0D);
        }
    }

    private static boolean isTravelShip(Entity entity) {
        return entity instanceof NovadiveEntity || entity instanceof FlashfireEntity;
    }

    private void completeTraining(ServerPlayer master) {
        ForceUserRole role = getForceUserRole();

        if (role.isApprentice()) {
            fullyTrainedHandled = true;
            sithApprenticeReady = true;
            sithRivalChallenge = false;
            this.studentQuestCompletions = ForceTrainingManager.APPRENTICE_QUESTS_TO_READY;
            this.studentAgeTicks = STUDENT_GROWTH_TICKS;
            this.setCustomName(Component.literal(namedWithRank("Sith Apprentice")));
            setFollowingMaster(true);
            ForceTrainingManager.markSithAscensionReady(master, this);
            master.displayClientMessage(Component.literal(this.getDisplayName().getString() + ": My acolyte trials are complete. I remain under you as a Sith Apprentice, but I will seek an apprentice of my own. When you claim the Dark Lord mantle, I will challenge you."), false);
            return;
        }

        fullyTrainedHandled = true;
        ForceTrainingManager.release(master, role);
        this.setCustomName(Component.literal(graduatedNameFor(role)));
        ForceTrainingManager.markStudentFullyTrained(master);
        master.displayClientMessage(Component.literal(this.getDisplayName().getString() + ": My training under you is complete. I leave as a " + (role.side().isNeutral() ? "Neutral Knight" : "Jedi Knight") + ", and may our paths cross again."), false);
        masterUuid = null;
        setFollowingMaster(false);
        this.setTarget(null);
        this.getNavigation().stop();
        this.setPersistenceRequired();
    }

    private void tickBossBar() {
        if (!getForceUserRole().isBoss()) {
            return;
        }
        bossEvent.setName(this.getDisplayName());
        bossEvent.setColor(getForceUserSide().isDark() ? BossEvent.BossBarColor.RED : BossEvent.BossBarColor.BLUE);
        bossEvent.setProgress(Math.max(0.0F, this.getHealth() / this.getMaxHealth()));
        bossEvent.setVisible(this.getLastHurtByMob() != null || this.getTarget() instanceof Player || this.getHealth() < this.getMaxHealth());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (getForceUserRole().isBoss()) {
            bossEvent.addPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        if (getForceUserRole().isBoss()) {
            bossEvent.removePlayer(player);
        }
    }

    private void applyRoleStatsAndName(RandomSource random) {
        ForceUserRole role = getForceUserRole();
        if (role.isBoss()) {
            if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(68.0D);
            }
            if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(11.0D);
            }
            this.setHealth(this.getMaxHealth());
        }
        if (!this.hasCustomName()) {
            if (this.getType() == galaxyunderchaos.JEDI_TEMPLE_GUARD.get()) {
                this.setCustomName(Component.literal("Jedi Temple Guard"));
            } else if (this.getType() == galaxyunderchaos.SITH_GUARD.get()) {
                this.setCustomName(Component.literal("Sith Guard"));
            } else {
                this.setCustomName(Component.literal(defaultNameFor(role, random)));
            }
        }
        normalizeLegacyForceUserName();
    }

    private static String defaultNameFor(ForceUserRole role, RandomSource random) {
        String[] names = {
                "Aelath", "Aeldor", "Aelren", "Aelvek", "Aethan", "Aethdran",
                "Aethrith", "Aethtor", "Ardan", "Arek", "Arnath", "Arven",
                "Ashdar", "Ashen", "Ashron", "Ashzar", "Balden", "Balian",
                "Balsar", "Balxan", "Belreth", "Belik", "Belsek", "Belwyn",
                "Borin", "Borken", "Bortal", "Borvor", "Caelis", "Caelkar",
                "Caelthar", "Caelrix", "Callen", "Callor", "Calther", "Calvan",
                "Cyrmar", "Cyrmir", "Cyrnor", "Cyrrek", "Daelath", "Daeldor",
                "Daelren", "Daelvek", "Daran", "Dardran", "Darrith", "Dartor",
                "Daxar", "Daxek", "Daxnath", "Daxven", "Drendar", "Drenor",
                "Drenron", "Drenzar", "Eldren", "Eldian", "Eldsar", "Eldxar",
                "Eryneth", "Erynik", "Erynsek", "Erynwyn", "Falin", "Falkan",
                "Faltal", "Falvor", "Fennis", "Fennkar", "Fennthar", "Fennrix",
                "Gavlen", "Gavlor", "Gavther", "Gavvan", "Garrmar", "Garrmir",
                "Garrnor", "Garrek", "Halath", "Haldor", "Halren", "Halvek",
                "Haran", "Hardran", "Harrith", "Hartor", "Ithar", "Ithek",
                "Ithnath", "Ithven", "Jordar", "Joren", "Jorron", "Jorzar",
                "Kaelden", "Kaelian", "Kaelsar", "Kaelxan", "Karneth", "Karnik",
                "Karnsek", "Karnwyn", "Kelin", "Kelkan", "Keltal", "Kelvor",
                "Kethis", "Kethkar", "Kethar", "Kethrix", "Kyrlen", "Kyrlor",
                "Kyrther", "Kyrvan", "Laenmar", "Laenmir", "Laenor", "Laenrek",
                "Lathran", "Lathdor", "Lathren", "Lathvek", "Maelan", "Maeldran",
                "Maelrith", "Maeltor", "Marnar", "Marnek", "Marnath", "Marnven",
                "Mekdar", "Meken", "Mekron", "Mekzar", "Morden", "Morian",
                "Morsar", "Morxan", "Nahleth", "Nahlik", "Nahlsek", "Nahlwyn",
                "Narethin", "Narethkan", "Narethtal", "Narethvor", "Nyxis", "Nyxkar",
                "Nyxthar", "Nyxris", "Oranlen", "Oranlor", "Oranther", "Oranvan",
                "Praxmar", "Praxmir", "Praxnor", "Praxrek", "Qorath", "Qordor",
                "Qorren", "Qorvek", "Raelan", "Raeldran", "Raelrith", "Raeltor",
                "Rathar", "Rathek", "Rathnar", "Rathven", "Renndar", "Rennor",
                "Rennron", "Rennzar", "Rhelden", "Rhelian", "Rhelsar", "Rhelxan",
                "Saeleth", "Saelik", "Saelsek", "Saelwyn", "Sarnin", "Sarnkan",
                "Sarntal", "Sarnvor", "Sevis", "Sevkar", "Sevthar", "Sevrix",
                "Shaellen", "Shaelor", "Shaelther", "Shaelvan", "Talmar", "Talmir",
                "Talnor", "Talrek", "Tarnath", "Tarndor", "Tarnren", "Tarnvek",
                "Thalan", "Thaldran", "Thalrith", "Thaltor", "Theran", "Therek",
                "Thernath", "Therven", "Tordar", "Toren", "Torron", "Torzar",
                "Tyrden", "Tyrian", "Tyrsar", "Tyrxan", "Vaeleth", "Vaelik",
                "Vaelsek", "Vaelwyn", "Varrin", "Varrkan", "Varrtal", "Varrvor",
                "Velis", "Velkar", "Velthar", "Velxis", "Veylen", "Veylor",
                "Veyther", "Veyvan", "Vormar", "Vormir", "Vornor", "Vorrek",
                "Xalath", "Xaldor", "Xalren", "Xalvek", "Xevan", "Xevdran",
                "Xevrith", "Xevtor", "Yardan", "Yarek", "Yarnath", "Yarven",
                "Zandar", "Zanen", "Zanron", "Zanzar", "Zarden", "Zarian",
                "Zarsar", "Zarxan", "Zeveth", "Zevik", "Zevsek", "Zevwyn",
                "Zorin", "Zorkan", "Zortal", "Zorvor", "Zyris", "Zyrkar",
                "Zyrthar", "Zyrix", "Vael-Tor", "Ael-Reth", "Zyr-Ven", "Keth-Mor",
                "Nyx-Raal", "Dren-Kai", "Thal-Vek", "Qor-Nath", "Sael-Ron", "Mek-Ther",
                "Rath-Vor", "Cael-Dren", "Ar-Vey", "Tyr-Marn", "Vel-Kareth", "Xal-Ren",
                "Oran-Sek", "Jor-Vannis", "Laen-Qor", "Har-Zeth", "Nahl-Kar", "Kyr-Aven",
                "Bel-Thoris", "Ash-Varn", "Zev-Kalen", "Prax-Dor", "Fal-Rith", "Eld-Varr"
        };
        String name = names[random.nextInt(names.length)];
        return switch (role) {
            case SITH, SITH_GHOST -> "Sith Apprentice " + name;
            case SITH_LORD, SITH_LORD_GHOST -> "Darth " + name;
            case JEDI_MASTER -> "Jedi Master " + name;
            case NEUTRAL_MASTER -> "Neutral Master " + name;
            case NEUTRAL_FORCE_USER -> "Neutral Knight " + name;
            case SITH_APPRENTICE -> "Sith Acolyte " + name;
            case JEDI_PADAWAN -> "Jedi Padawan " + name;
            case NEUTRAL_PADAWAN -> "Neutral Padawan " + name;
            default -> "Jedi Knight " + name;
        };
    }

    public ForceUserRole getForceUserRole() {
        EntityType<?> type = this.getType();
        if (type == galaxyunderchaos.SITH_GHOST.get()) return ForceUserRole.SITH_GHOST;
        if (type == galaxyunderchaos.SITH_LORD_GHOST.get()) return ForceUserRole.SITH_LORD_GHOST;
        if (type == galaxyunderchaos.SITH_LORD.get()) return ForceUserRole.SITH_LORD;
        if (type == galaxyunderchaos.JEDI_MASTER.get()) return ForceUserRole.JEDI_MASTER;
        if (type == galaxyunderchaos.NEUTRAL_MASTER.get()) return ForceUserRole.NEUTRAL_MASTER;
        if (type == galaxyunderchaos.NEUTRAL_FORCE_USER.get()) return ForceUserRole.NEUTRAL_FORCE_USER;
        if (type == galaxyunderchaos.SITH_APPRENTICE.get()) return ForceUserRole.SITH_APPRENTICE;
        if (type == galaxyunderchaos.JEDI_PADAWAN.get()) return ForceUserRole.JEDI_PADAWAN;
        if (type == galaxyunderchaos.NEUTRAL_PADAWAN.get()) return ForceUserRole.NEUTRAL_PADAWAN;
        if (type == galaxyunderchaos.JEDI_TEMPLE_GUARD.get()) return ForceUserRole.JEDI;
        if (type == galaxyunderchaos.SITH_GUARD.get()) return ForceUserRole.SITH;
        if (type == galaxyunderchaos.SITH_FORCE_USER.get()) return ForceUserRole.SITH;
        return ForceUserRole.JEDI;
    }

    public boolean isGhost() {
        return getForceUserRole().isGhost();
    }

    public float getRenderScale() {
        ForceUserRole role = getForceUserRole();
        if ((role.isPadawan() && fullyTrainedHandled) || (role.isApprentice() && sithApprenticeReady)) {
            return 1.0F;
        }
        return role.renderScale();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("ForceUserInitialized", true);
        tag.putString("Side", getForceUserSide().serializedName());
        tag.putString("Species", getSpeciesId());
        tag.putString("Model", getBodyModelId());
        tag.putString("Texture", getBodyTextureId());
        tag.putString("EyeColor", getEyeColor());
        tag.putBoolean("SaberDrawn", isSaberDrawn());
        tag.putInt("StudentAgeTicks", studentAgeTicks);
        tag.putInt("StudentQuestCompletions", studentQuestCompletions);
        tag.putBoolean("FullyTrainedHandled", fullyTrainedHandled);
        tag.putBoolean("ForceMentorBond", forceMentorBond);
        tag.putBoolean("SithApprenticeReady", sithApprenticeReady);
        tag.putBoolean("SithRivalChallenge", sithRivalChallenge);
        tag.putLong("PersonalitySeed", getPersonalitySeed());
        tag.putBoolean("FollowMaster", isFollowingMaster());
        tag.putString("CompanionOrder", getCompanionOrder().id());
        tag.putInt("AlignmentLeaning", getAlignmentLeaning());
        if (masterUuid != null) {
            tag.putUUID("Master", masterUuid);
        }
        if (!getStoredLightsaber().isEmpty()) {
            tag.put("Lightsaber", getStoredLightsaber().save(new CompoundTag()));
        }
        ListTag powersTag = new ListTag();
        for (ForcePower power : powers) {
            powersTag.add(StringTag.valueOf(power.id()));
        }
        tag.put("Powers", powersTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        // Newer NPCs can rarely change allegiance, so saved Side is respected.
        // Missing/old tags still fall back to the registered entity type.
        setForceUserSide(tag.contains("Side") ? ForceUserSide.byName(tag.getString("Side"), defaultSide()) : defaultSide());
        this.studentAgeTicks = tag.getInt("StudentAgeTicks");
        if (tag.contains("StudentQuestCompletions", Tag.TAG_INT)) {
            this.studentQuestCompletions = tag.getInt("StudentQuestCompletions");
        } else if (getForceUserRole().isStudent() && this.studentAgeTicks > 0) {
            this.studentQuestCompletions = (int) Math.min(ForceTrainingManager.STUDENT_QUESTS_TO_GRADUATE,
                    Math.max(0L, (this.studentAgeTicks * (long) ForceTrainingManager.STUDENT_QUESTS_TO_GRADUATE) / STUDENT_GROWTH_TICKS));
        } else {
            this.studentQuestCompletions = 0;
        }
        this.fullyTrainedHandled = tag.getBoolean("FullyTrainedHandled");
        this.forceMentorBond = tag.getBoolean("ForceMentorBond");
        this.sithApprenticeReady = tag.getBoolean("SithApprenticeReady");
        this.sithRivalChallenge = tag.getBoolean("SithRivalChallenge");
        this.personalitySeed = tag.contains("PersonalitySeed") ? tag.getLong("PersonalitySeed") : 0L;
        setAlignmentLeaning(tag.contains("AlignmentLeaning") ? tag.getInt("AlignmentLeaning") : defaultAlignmentLeaning(this.random, getForceUserRole(), getForceUserSide()));
        this.masterUuid = tag.hasUUID("Master") ? tag.getUUID("Master") : null;
        if (this.masterUuid != null || getForceUserRole().isGhost()) {
            this.setPersistenceRequired();
        }
        normalizeLegacyForceUserName();
        if (tag.contains("CompanionOrder")) {
            setCompanionOrder(CompanionOrder.byId(tag.getString("CompanionOrder")));
        } else {
            setCompanionOrder((!tag.contains("FollowMaster") || tag.getBoolean("FollowMaster")) ? CompanionOrder.FOLLOW_DEFEND : CompanionOrder.STAY);
        }

        ForceUserSpecies species = ForceUserSpecies.byId(tag.getString("Species"));
        String texture = tag.contains("Texture") ? tag.getString("Texture") : species.randomTexture(this.random);
        setSpecies(species, texture);
        setEyeColor(tag.contains("EyeColor") ? tag.getString("EyeColor") : randomEyeColor(this.random));
        if (tag.contains("Lightsaber", Tag.TAG_COMPOUND)) {
            setNpcLightsaber(ItemStack.of(tag.getCompound("Lightsaber")));
        } else {
            setNpcLightsaber(getForceUserSide().isNeutral()
                    ? ForceUserLoadout.randomNeutralLightsaber(this.random)
                    : ForceUserLoadout.randomLightsaber(this.random, getForceUserSide()));
        }
        this.powers.clear();
        if (tag.contains("Powers", Tag.TAG_LIST)) {
            ListTag powersTag = tag.getList("Powers", Tag.TAG_STRING);
            for (int i = 0; i < powersTag.size(); i++) {
                ForcePower power = ForcePower.byId(powersTag.getString(i));
                if (ForceUserLoadout.isAllowedForSide(power, getForceUserSide())) {
                    this.powers.add(power);
                }
            }
        }
        if (this.powers.isEmpty()) {
            this.powers.addAll(ForceUserLoadout.randomPowers(this.random, getForceUserSide(), getForceUserRole().maxPowerTier()));
        }
        if (this.getType() == galaxyunderchaos.JEDI_TEMPLE_GUARD.get()) {
            setForceUserSide(ForceUserSide.LIGHT);
            setAlignmentLeaning(85);
            setSpecies(ForceUserSpecies.HUMAN_MALE, "human_male");
            equipTempleGuardArmor();
        }
        if (this.getType() == galaxyunderchaos.SITH_GUARD.get()) {
            setForceUserSide(ForceUserSide.DARK);
            setAlignmentLeaning(-90);
            setSpecies(ForceUserSpecies.HUMAN_MALE, "human_male");
            setEyeColor("sith");
            equipSithGuardArmor();
        }
        this.entityData.set(DATA_SABER_DRAWN, false);
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        applyRoleStatsAndName(this.random);
    }

    private void equipTempleGuardArmor() {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(galaxyunderchaos.TEMPLE_GUARD_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(galaxyunderchaos.TEMPLE_GUARD_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(galaxyunderchaos.TEMPLE_GUARD_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(galaxyunderchaos.TEMPLE_GUARD_BOOTS.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);
    }

    private void equipSithGuardArmor() {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(galaxyunderchaos.SITH_GUARD_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(galaxyunderchaos.SITH_GUARD_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(galaxyunderchaos.SITH_GUARD_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(galaxyunderchaos.SITH_GUARD_BOOTS.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);
    }

    private ItemStack getStoredLightsaber() {
        ItemStack mainHand = this.getMainHandItem();
        if (isSaberDrawn() && !mainHand.isEmpty()) {
            return mainHand;
        }
        return npcLightsaber;
    }

    public ItemStack getStoredLightsaberCopy() {
        ItemStack copy = getStoredLightsaber().copy();
        ForceUserLoadout.setLightsaberActive(copy, false);
        return copy;
    }

    public ForceUserSide getForceUserSide() {
        ForceUserSide synced = ForceUserSide.bySerializedName(this.entityData.get(DATA_SIDE));
        return synced == null ? defaultSide() : synced;
    }

    private void setForceUserSide(ForceUserSide side) {
        this.entityData.set(DATA_SIDE, side.serializedName());
    }


    public int getAlignmentLeaning() {
        return this.entityData.get(DATA_ALIGNMENT_LEAN);
    }

    private void setAlignmentLeaning(int value) {
        this.entityData.set(DATA_ALIGNMENT_LEAN, Math.max(-100, Math.min(100, value)));
    }

    public String getAlignmentLeaningLabel() {
        int lean = getAlignmentLeaning();
        if (lean >= 70) return "devout light";
        if (lean >= 35) return "light-leaning";
        if (lean <= -70) return "devout dark";
        if (lean <= -35) return "dark-leaning";
        return "balanced";
    }

    public String getRankDisplayName() {
        ForceUserRole role = getForceUserRole();
        return switch (role) {
            case SITH, SITH_GHOST -> "Sith Apprentice";
            case SITH_APPRENTICE -> sithApprenticeReady ? "Sith Apprentice" : "Sith Acolyte";
            case SITH_LORD, SITH_LORD_GHOST -> "Sith Lord";
            case JEDI_MASTER -> "Jedi Master";
            case JEDI_PADAWAN -> fullyTrainedHandled ? "Jedi Knight" : "Jedi Padawan";
            case NEUTRAL_MASTER -> "Neutral Master";
            case NEUTRAL_FORCE_USER -> "Neutral Knight";
            case NEUTRAL_PADAWAN -> fullyTrainedHandled ? "Neutral Knight" : "Neutral Padawan";
            default -> "Jedi Knight";
        };
    }

    private static int defaultAlignmentLeaning(RandomSource random, ForceUserRole role, ForceUserSide side) {
        int variance = role.isBoss() ? 14 : 24;
        if (side.isDark()) {
            return -80 + random.nextInt(variance * 2 + 1) - variance;
        }
        if (side.isLight()) {
            return 80 + random.nextInt(variance * 2 + 1) - variance;
        }
        return random.nextInt(41) - 20;
    }

    public String getSpeciesId() {
        return this.entityData.get(DATA_SPECIES);
    }

    public ForceUserSpecies getSpecies() {
        return ForceUserSpecies.byId(getSpeciesId());
    }

    private void setSpecies(ForceUserSpecies species, String textureId) {
        this.entityData.set(DATA_SPECIES, species.id());
        this.entityData.set(DATA_MODEL, species.modelId());
        this.entityData.set(DATA_TEXTURE, textureId);
    }

    public String getBodyModelId() {
        return this.entityData.get(DATA_MODEL);
    }

    public String getBodyTextureId() {
        return this.entityData.get(DATA_TEXTURE);
    }

    public String getEyeColor() {
        String color = this.entityData.get(DATA_EYE_COLOR);
        return switch (color) {
            case "blue", "light_blue", "grey_blue", "green", "hazel", "brown", "dark_brown", "sith" -> color;
            default -> "blue";
        };
    }

    private void setEyeColor(String color) {
        this.entityData.set(DATA_EYE_COLOR, switch (color) {
            case "blue", "light_blue", "grey_blue", "green", "hazel", "brown", "dark_brown", "sith" -> color;
            default -> "blue";
        });
    }

    private static String randomEyeColor(RandomSource random) {
        return switch (random.nextInt(7)) {
            case 0 -> "blue";
            case 1 -> "light_blue";
            case 2 -> "grey_blue";
            case 3 -> "green";
            case 4 -> "hazel";
            case 5 -> "brown";
            default -> "dark_brown";
        };
    }

    public boolean isSaberDrawn() {
        return this.entityData.get(DATA_SABER_DRAWN);
    }

    public int getCastingPowerTicks() {
        return this.entityData.get(DATA_CASTING_TICKS);
    }

    private void setNpcLightsaber(ItemStack stack) {
        ItemStack copy = stack == null ? ItemStack.EMPTY : stack.copy();
        if (!copy.isEmpty()) {
            ForceUserLoadout.setLightsaberActive(copy, false);
        }
        this.npcLightsaber = copy;
        this.entityData.set(DATA_BELT_LIGHTSABER, copy.copy());
    }

    public ItemStack getBeltLightsaber() {
        if (isSaberDrawn()) {
            return ItemStack.EMPTY;
        }
        ItemStack synced = this.entityData.get(DATA_BELT_LIGHTSABER);
        if (!synced.isEmpty()) {
            return synced;
        }
        return npcLightsaber;
    }

    public boolean isFollowingMaster() {
        return getCompanionOrder() == CompanionOrder.FOLLOW_DEFEND;
    }

    public CompanionOrder getCompanionOrder() {
        return CompanionOrder.byId(this.entityData.get(DATA_ORDER));
    }

    public void setFollowingMaster(boolean follow) {
        setCompanionOrder(follow ? CompanionOrder.FOLLOW_DEFEND : CompanionOrder.STAY);
    }

    public void setCompanionOrder(CompanionOrder order) {
        if (order == null) {
            order = CompanionOrder.FOLLOW_DEFEND;
        }
        this.entityData.set(DATA_ORDER, order.id());
        this.entityData.set(DATA_FOLLOW_MASTER, order == CompanionOrder.FOLLOW_DEFEND);
        if (order == CompanionOrder.STAY) {
            this.getNavigation().stop();
            this.setTarget(null);
        } else if (order == CompanionOrder.WANDER && this.getTarget() instanceof Player player && masterUuid != null && masterUuid.equals(player.getUUID())) {
            this.setTarget(null);
        }
    }

    public boolean isBoundTo(ServerPlayer player) {
        return player != null && masterUuid != null && masterUuid.equals(player.getUUID());
    }

    public boolean isForceMentorBond() {
        return forceMentorBond;
    }

    public int getStudentTrainingProgressPercent() {
        if (!getForceUserRole().isStudent()) {
            return 0;
        }
        return Math.max(0, Math.min(100, (int) ((getStudentQuestCompletions() * 100L) / ForceTrainingManager.STUDENT_QUESTS_TO_GRADUATE)));
    }

    public int getStudentQuestCompletions() {
        return Math.max(0, Math.min(ForceTrainingManager.STUDENT_QUESTS_TO_GRADUATE, studentQuestCompletions));
    }

    public java.util.UUID getBoundMasterUuid() {
        return masterUuid;
    }

    public void advanceTrainingFromQuest(ServerPlayer player, int reward) {
        if (!getForceUserRole().isStudent() || forceMentorBond || fullyTrainedHandled) {
            return;
        }
        int required = ForceTrainingManager.STUDENT_QUESTS_TO_GRADUATE;
        this.studentQuestCompletions = Math.min(required, this.studentQuestCompletions + 1);
        this.studentAgeTicks = (int) Math.min(STUDENT_GROWTH_TICKS, (STUDENT_GROWTH_TICKS * (long) this.studentQuestCompletions) / required);
        int percent = getStudentTrainingProgressPercent();
        String label = getForceUserRole().isApprentice() ? "Sith acolyte" : getForceUserRole().side().isNeutral() ? "neutral padawan" : "Jedi padawan";
        player.displayClientMessage(Component.literal(this.getDisplayName().getString() + " completes " + label + " trial " + this.studentQuestCompletions + "/" + required + " (" + percent + "%)."), false);
        if (this.studentQuestCompletions >= required) {
            completeTraining(player);
        }
    }

    private ForceSide getPlayerForceSide(Player player) {
        return player.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::getCommittedSide)
                .orElse(ForceSide.NEUTRAL);
    }

    private boolean shouldAggroPlayer(Player player) {
        if (player == null || !player.isAlive() || player.isCreative() || player.isSpectator()) {
            return false;
        }
        if (isSithRivalOf(player)) {
            return true;
        }
        if (masterUuid != null && masterUuid.equals(player.getUUID())) {
            return false;
        }
        if (getForceUserSide().isDark()) {
            return getPlayerForceSide(player) != ForceSide.DARK;
        }
        return getPlayerForceSide(player) == ForceSide.DARK;
    }

    private boolean shouldAttackForceUser(ForceUserEntity other) {
        return other != null && other != this && other.isAlive() && !isAlliedTo(other);
    }



    private String graduatedNameFor(ForceUserRole role) {
        String current = this.getDisplayName().getString();
        String[] parts = current.split(" ");
        String name = parts.length == 0 ? "Wanderer" : parts[parts.length - 1];
        if (role.isApprentice()) {
            return "Sith Apprentice " + name;
        }
        if (role.side().isNeutral()) {
            return "Neutral Knight " + name;
        }
        return "Jedi Knight " + name;
    }

    private String studentLabel() {
        ForceUserRole role = getForceUserRole();
        if (role.isApprentice()) {
            return sithApprenticeReady ? "Sith apprentice" : "Sith acolyte";
        }
        if (role.side().isNeutral()) {
            return fullyTrainedHandled ? "neutral knight" : "neutral padawan";
        }
        return fullyTrainedHandled ? "Jedi knight" : "Jedi padawan";
    }

    public int getRespectBowTicks() {
        return this.entityData.get(DATA_RESPECT_BOW_TICKS);
    }

    private void beginRespectBow() {
        this.entityData.set(DATA_RESPECT_BOW_TICKS, 32);
    }

    private void greetRespectfulMaster(ServerPlayer player) {
        if (respectGreetingCooldownTicks > 0 || isSithRivalOf(player)) {
            return;
        }
        ForceSide npcSide = getForceUserSide().toCapabilitySide();
        if (!ForceTrainingManager.shouldBeRecognizedAsMaster(player, npcSide)) {
            return;
        }
        respectGreetingCooldownTicks = 120;
        beginRespectBow();
        String name = PlayerForceIdentity.getForceName(player);
        player.displayClientMessage(Component.literal(this.getDisplayName().getString() + ": Hello Master, " + name + "."), false);
    }

    private void normalizeLegacyForceUserName() {
        if (!this.hasCustomName() || this.getCustomName() == null) {
            return;
        }
        String current = this.getCustomName().getString();
        if (!current.contains("Force-user") && !current.contains("Force User")) {
            return;
        }
        ForceUserRole role = getForceUserRole();
        String rank = switch (role) {
            case SITH, SITH_GHOST -> "Sith Apprentice";
            case SITH_APPRENTICE -> sithApprenticeReady ? "Sith Apprentice" : "Sith Acolyte";
            case SITH_LORD, SITH_LORD_GHOST -> "Darth";
            case JEDI_MASTER -> "Jedi Master";
            case JEDI_PADAWAN -> fullyTrainedHandled ? "Jedi Knight" : "Jedi Padawan";
            case NEUTRAL_MASTER -> "Neutral Master";
            case NEUTRAL_FORCE_USER -> "Neutral Knight";
            case NEUTRAL_PADAWAN -> fullyTrainedHandled ? "Neutral Knight" : "Neutral Padawan";
            default -> "Jedi Knight";
        };
        this.setCustomName(Component.literal(namedWithRank(rank)));
    }

    private String namedWithRank(String rank) {
        String current = this.getDisplayName().getString();
        String[] parts = current.split(" ");
        String name = parts.length == 0 ? "Wanderer" : parts[parts.length - 1];
        return rank + " " + name;
    }

    public boolean isSithApprenticeReady() {
        return sithApprenticeReady;
    }

    public boolean isSithRivalChallenge() {
        return sithRivalChallenge;
    }

    private boolean isSithRivalOf(Player player) {
        return player != null
                && sithRivalChallenge
                && getForceUserRole().isApprentice()
                && masterUuid != null
                && masterUuid.equals(player.getUUID());
    }

    public void beginSithRivalChallenge(ServerPlayer player) {
        if (player == null || !getForceUserRole().isApprentice() || !isBoundTo(player)) {
            return;
        }
        if (this.sithRivalChallenge) {
            this.setTarget(player);
            return;
        }
        this.sithApprenticeReady = true;
        this.sithRivalChallenge = true;
        this.forceMentorBond = false;
        setFollowingMaster(false);
        drawLightsaber();
        this.setTarget(player);
        this.setCustomName(Component.literal(namedWithRank("Sith Apprentice")));
        player.displayClientMessage(Component.literal(this.getDisplayName().getString() + ": You wear the Dark Lord mantle now. I will take it from you or die proving I cannot."), false);
    }

    private void tickRareAllegianceShift() {
        if (this.level().isClientSide || this.tickCount % 1200 != 0 || getForceUserRole().isGhost() || hasMaster()) {
            return;
        }
        ForceUserSide side = getForceUserSide();
        if (side.isDark()) {
            return;
        }
        if (this.random.nextFloat() >= 0.001F) {
            return;
        }
        List<ForceUserEntity> nearby = this.level().getEntitiesOfClass(ForceUserEntity.class, this.getBoundingBox().inflate(10.0D), other -> other != this && other.isAlive() && other.getForceUserSide().isDark());
        if (nearby.isEmpty()) {
            return;
        }
        setForceUserSide(ForceUserSide.DARK);
        setAlignmentLeaning(-90);
        setEyeColor("sith");
        setNpcLightsaber(ForceUserLoadout.randomLightsaber(this.random, ForceUserSide.DARK, 0.025F, false));
        this.powers.clear();
        this.powers.addAll(ForceUserLoadout.randomPowers(this.random, ForceUserSide.DARK, 2));
        drawLightsaber();
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level().isClientSide && masterUuid != null && this.level() instanceof ServerLevel serverLevel) {
            ServerPlayer player = serverLevel.getServer().getPlayerList().getPlayer(masterUuid);
            if (player != null) {
                if (forceMentorBond) {
                    boolean ascended = ForceTrainingManager.tryCompleteSithLordAscension(player, this, source.getEntity());
                    ForceTrainingManager.releaseForceMentor(player);
                    if (!ascended) {
                        player.displayClientMessage(Component.literal(this.getDisplayName().getString() + " has died. Your completed Force trials and rank progress remain saved; choose another Force mentor to continue."), false);
                    }
                } else {
                    ForceTrainingManager.release(player, getForceUserRole());
                    if (sithRivalChallenge && source.getEntity() == player) {
                        player.displayClientMessage(Component.literal(this.getDisplayName().getString() + " challenged your rule and fell. Your Sith Lord rank remains secure."), false);
                    } else {
                        player.displayClientMessage(Component.literal(this.getDisplayName().getString() + " has died. You may take another student."), false);
                    }
                }
            }
        }
        super.die(source);
    }

    public long getPersonalitySeed() {
        if (personalitySeed == 0L) {
            personalitySeed = this.getUUID().getMostSignificantBits() ^ this.getUUID().getLeastSignificantBits();
        }
        return personalitySeed;
    }

    private boolean canSaberBlock(DamageSource source) {
        if (getStoredLightsaber().isEmpty()) {
            return false;
        }
        Entity attacker = source.getEntity() != null ? source.getEntity() : source.getDirectEntity();
        if (!(attacker instanceof LivingEntity living) || !living.isAlive()) {
            return false;
        }
        drawLightsaber();
        Vec3 look = this.getLookAngle().normalize();
        Vec3 toAttacker = attacker.position().subtract(this.position()).normalize();
        return look.dot(toAttacker) > -0.35D;
    }



    public enum CompanionOrder {
        STAY("stay", "Stay"),
        WANDER("wander", "Wander"),
        FOLLOW_DEFEND("follow_defend", "Follow & Defend");

        private final String id;
        private final String displayName;

        CompanionOrder(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public String id() {
            return id;
        }

        public String displayName() {
            return displayName;
        }

        public static CompanionOrder byId(String id) {
            if (id == null) {
                return FOLLOW_DEFEND;
            }
            return switch (id.toLowerCase(java.util.Locale.ROOT)) {
                case "stay" -> STAY;
                case "wander", "wonder" -> WANDER;
                default -> FOLLOW_DEFEND;
            };
        }
    }

}
