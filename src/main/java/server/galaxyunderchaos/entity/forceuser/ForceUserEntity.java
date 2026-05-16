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
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.force.ForceProvider;
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
    private static final EntityDataAccessor<Boolean> DATA_SABER_DRAWN = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_CASTING_TICKS = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_FOLLOW_MASTER = SynchedEntityData.defineId(ForceUserEntity.class, EntityDataSerializers.BOOLEAN);

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
    private boolean fullyTrainedHandled;

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
        this.entityData.define(DATA_SABER_DRAWN, false);
        this.entityData.define(DATA_CASTING_TICKS, 0);
        this.entityData.define(DATA_FOLLOW_MASTER, true);
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
            this.npcLightsaber = server.galaxyunderchaos.lightsaber.DoubleLightsaberData.create(
                    server.galaxyunderchaos.lightsaber.ModularLightsaberData.createCustomLightsaber("yellow", "temple_guard", "temple_guard", "temple_guard", "temple_guard"),
                    server.galaxyunderchaos.lightsaber.ModularLightsaberData.createCustomLightsaber("yellow", "temple_guard", "temple_guard", "temple_guard", "temple_guard"));
            ForceUserLoadout.setLightsaberActive(this.npcLightsaber, false);
            this.powers.clear();
            this.powers.addAll(ForceUserLoadout.randomPowers(random, ForceUserSide.LIGHT, 2));
            applyRoleStatsAndName(random);
            return;
        }
        if (this.getType() == galaxyunderchaos.SITH_GUARD.get()) {
            setForceUserSide(ForceUserSide.DARK);
            setSpecies(ForceUserSpecies.HUMAN_MALE, "human_male");
            equipSithGuardArmor();
            setEyeColor("sith");
            this.npcLightsaber = server.galaxyunderchaos.lightsaber.DoubleLightsaberData.create(
                    server.galaxyunderchaos.lightsaber.ModularLightsaberData.createCustomLightsaber("red", "temple_guard", "temple_guard", "temple_guard", "temple_guard"),
                    server.galaxyunderchaos.lightsaber.ModularLightsaberData.createCustomLightsaber("red", "temple_guard", "temple_guard", "temple_guard", "temple_guard"));
            ForceUserLoadout.setLightsaberActive(this.npcLightsaber, false);
            this.powers.clear();
            this.powers.addAll(ForceUserLoadout.randomPowers(random, ForceUserSide.DARK, 2));
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
        this.npcLightsaber = ForceUserLoadout.randomLightsaber(random, side, modifierChance, role.isBoss());
        this.powers.clear();
        this.powers.addAll(ForceUserLoadout.randomPowers(random, side, role.maxPowerTier()));
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
            tickStudentBond();
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
            this.npcLightsaber = combat.copy();
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.entityData.set(DATA_SABER_DRAWN, false);
        this.level().playSound(null, this.blockPosition(), ModSounds.LIGHTSABER_TURN_OFF.get(), SoundSource.HOSTILE, 0.55F, 0.9F + this.random.nextFloat() * 0.15F);
    }

    private void tickForcePowers() {
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
            case RESIST1, RESIST2, RESIST3 -> castResist();
            case STUN1, STUN2, STUN3 -> castStun(target);
            case DRAIN1, DRAIN2, DRAIN3 -> castDrain(target);
            case LIGHTNING1, LIGHTNING2, LIGHTNING3 -> castLightning(target);
            case WOUND1, WOUND2, WOUND3 -> castWound(target);
            case SPEED -> castSpeed();
            case PUSH1, PUSH2, PUSH3 -> castPush(target);
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
        if (target instanceof Player player && masterUuid != null && masterUuid.equals(player.getUUID()) && player.getHealth() <= 6.0F) {
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
            if (this.masterUuid != null && this.masterUuid.equals(other.masterUuid)) {
                return true;
            }
            return other.getForceUserSide() == this.getForceUserSide();
        }
        if (entity instanceof Player player) {
            if (masterUuid != null && masterUuid.equals(player.getUUID())) {
                return true;
            }
            ForceSide playerSide = getPlayerForceSide(player);
            if (getForceUserSide().isDark()) {
                return playerSide == ForceSide.DARK;
            }
            return playerSide == ForceSide.LIGHT;
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
        if (!this.level().isClientSide && player instanceof ServerPlayer serverPlayer && getForceUserRole().isStudent()) {
            if (hasMaster() && masterUuid != null && masterUuid.equals(serverPlayer.getUUID()) && player.isShiftKeyDown()) {
                setFollowingMaster(!isFollowingMaster());
                serverPlayer.displayClientMessage(Component.literal(this.getDisplayName().getString() + (isFollowingMaster() ? " will now follow and defend you." : " will now stay here and only defend itself.")), true);
                return InteractionResult.CONSUME;
            }
            if (hasMaster()) {
                player.displayClientMessage(Component.literal("This student already follows a master."), true);
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
                    + (getForceUserRole().isApprentice() ? "Sith apprentice. " : "Jedi padawan. ") + "Sneak-right-click after bonding to toggle follow/stay."), false);
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    public void bindToMaster(ServerPlayer player) {
        this.masterUuid = player.getUUID();
        this.setTarget(null);
        this.setPersistenceRequired();
        this.studentAgeTicks = 0;
        this.fullyTrainedHandled = false;
        this.entityData.set(DATA_FOLLOW_MASTER, true);
    }

    public boolean hasMaster() {
        return masterUuid != null;
    }

    private void tickStudentBond() {
        if (!getForceUserRole().isStudent() || masterUuid == null) {
            return;
        }
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        Entity entity = serverLevel.getEntity(masterUuid);
        if (!(entity instanceof ServerPlayer master)) {
            return;
        }

        studentAgeTicks++;
        if (studentAgeTicks >= STUDENT_GROWTH_TICKS && !fullyTrainedHandled) {
            completeTraining(master);
            return;
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

    private void completeTraining(ServerPlayer master) {
        fullyTrainedHandled = true;
        ForceUserRole role = getForceUserRole();
        ForceTrainingManager.release(master, role);
        ForceTrainingManager.markStudentFullyTrained(master);
        masterUuid = null;

        if (role.isPadawan()) {
            if (this.random.nextFloat() < 0.02F) {
                setForceUserSide(ForceUserSide.DARK);
                this.npcLightsaber = ForceUserLoadout.randomLightsaber(this.random, ForceUserSide.DARK, 0.05F, false);
                this.powers.clear();
                this.powers.addAll(ForceUserLoadout.randomPowers(this.random, ForceUserSide.DARK, 2));
                setEyeColor("sith");
                drawLightsaber();
                this.setTarget(master);
                master.displayClientMessage(Component.literal(this.getDisplayName().getString() + " has fallen and is ambushing you!"), false);
            } else {
                this.discard();
            }
            return;
        }

        if (role.isApprentice()) {
            if (this.random.nextFloat() < 0.12F) {
                drawLightsaber();
                this.setTarget(master);
                master.displayClientMessage(Component.literal(this.getDisplayName().getString() + " believes you are weak and tries to overthrow you!"), false);
            } else {
                this.discard();
            }
        }
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
            case SITH_APPRENTICE -> "Sith Acolyte " + name;
            case JEDI_PADAWAN -> "Jedi Padawan " + name;
            default -> "Jedi Knight " + name;
        };
    }

    public ForceUserRole getForceUserRole() {
        EntityType<?> type = this.getType();
        if (type == galaxyunderchaos.SITH_GHOST.get()) return ForceUserRole.SITH_GHOST;
        if (type == galaxyunderchaos.SITH_LORD_GHOST.get()) return ForceUserRole.SITH_LORD_GHOST;
        if (type == galaxyunderchaos.SITH_LORD.get()) return ForceUserRole.SITH_LORD;
        if (type == galaxyunderchaos.JEDI_MASTER.get()) return ForceUserRole.JEDI_MASTER;
        if (type == galaxyunderchaos.SITH_APPRENTICE.get()) return ForceUserRole.SITH_APPRENTICE;
        if (type == galaxyunderchaos.JEDI_PADAWAN.get()) return ForceUserRole.JEDI_PADAWAN;
        if (type == galaxyunderchaos.JEDI_TEMPLE_GUARD.get()) return ForceUserRole.JEDI;
        if (type == galaxyunderchaos.SITH_GUARD.get()) return ForceUserRole.SITH;
        if (type == galaxyunderchaos.SITH_FORCE_USER.get()) return ForceUserRole.SITH;
        return ForceUserRole.JEDI;
    }

    public boolean isGhost() {
        return getForceUserRole().isGhost();
    }

    public float getRenderScale() {
        return getForceUserRole().renderScale();
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
        tag.putBoolean("FullyTrainedHandled", fullyTrainedHandled);
        tag.putBoolean("FollowMaster", isFollowingMaster());
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

        // The registered entity type is authoritative. This prevents older/stale NBT from
        // loading a Sith Force user as visually Jedi or a Jedi Force user as visually Sith.
        setForceUserSide(defaultSide());
        this.studentAgeTicks = tag.getInt("StudentAgeTicks");
        this.fullyTrainedHandled = tag.getBoolean("FullyTrainedHandled");
        this.masterUuid = tag.hasUUID("Master") ? tag.getUUID("Master") : null;
        if (this.masterUuid != null) {
            this.setPersistenceRequired();
        }
        this.entityData.set(DATA_FOLLOW_MASTER, !tag.contains("FollowMaster") || tag.getBoolean("FollowMaster"));

        ForceUserSpecies species = ForceUserSpecies.byId(tag.getString("Species"));
        String texture = tag.contains("Texture") ? tag.getString("Texture") : species.randomTexture(this.random);
        setSpecies(species, texture);
        setEyeColor(tag.contains("EyeColor") ? tag.getString("EyeColor") : randomEyeColor(this.random));
        if (tag.contains("Lightsaber", Tag.TAG_COMPOUND)) {
            this.npcLightsaber = ItemStack.of(tag.getCompound("Lightsaber"));
            ForceUserLoadout.setLightsaberActive(this.npcLightsaber, false);
        } else {
            this.npcLightsaber = ForceUserLoadout.randomLightsaber(this.random, getForceUserSide());
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
            setSpecies(ForceUserSpecies.HUMAN_MALE, "human_male");
            equipTempleGuardArmor();
        }
        if (this.getType() == galaxyunderchaos.SITH_GUARD.get()) {
            setForceUserSide(ForceUserSide.DARK);
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
            case "blue", "green", "brown", "sith" -> color;
            default -> "blue";
        };
    }

    private void setEyeColor(String color) {
        this.entityData.set(DATA_EYE_COLOR, switch (color) {
            case "blue", "green", "brown", "sith" -> color;
            default -> "blue";
        });
    }

    private static String randomEyeColor(RandomSource random) {
        return switch (random.nextInt(3)) {
            case 0 -> "blue";
            case 1 -> "green";
            default -> "brown";
        };
    }

    public boolean isSaberDrawn() {
        return this.entityData.get(DATA_SABER_DRAWN);
    }

    public int getCastingPowerTicks() {
        return this.entityData.get(DATA_CASTING_TICKS);
    }

    public ItemStack getBeltLightsaber() {
        return isSaberDrawn() ? ItemStack.EMPTY : npcLightsaber;
    }

    public boolean isFollowingMaster() {
        return this.entityData.get(DATA_FOLLOW_MASTER);
    }

    public void setFollowingMaster(boolean follow) {
        this.entityData.set(DATA_FOLLOW_MASTER, follow);
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
        if (masterUuid != null && masterUuid.equals(player.getUUID())) {
            return false;
        }
        if (getForceUserSide().isDark()) {
            return getPlayerForceSide(player) != ForceSide.DARK;
        }
        return getPlayerForceSide(player) == ForceSide.DARK;
    }

    private boolean shouldAttackForceUser(ForceUserEntity other) {
        return other != null && other != this && other.isAlive() && !isAlliedTo(other) && other.getForceUserSide() != this.getForceUserSide();
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

}
