package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.sound.ModSounds;

import java.util.UUID;

public class VonskrEntity extends Monster {
    public static final int GROWTH_TICKS = 24_000;

    private static final byte TAMING_FAILED_EVENT = 6;
    private static final byte TAMING_SUCCESS_EVENT = 7;
    private static final byte BREED_READY_EVENT = 18;
    private static final byte BABY_BORN_EVENT = 19;

    private static final int BREED_READY_TICKS = 600;
    private static final int BREED_COOLDOWN_TICKS = 6_000;
    private static final int ATTACK_ANIMATION_TICKS = 10;

    private static final EntityDataAccessor<Boolean> DATA_SITTING = SynchedEntityData.defineId(VonskrEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_FEMALE = SynchedEntityData.defineId(VonskrEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_GROWTH_TICKS = SynchedEntityData.defineId(VonskrEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICKS = SynchedEntityData.defineId(VonskrEntity.class, EntityDataSerializers.INT);

    @Nullable
    private UUID ownerUuid;
    private int ownerLastHurtByTimestamp;
    private int ownerLastHurtTargetTimestamp;
    private int loveTicks;
    private int breedCooldownTicks;

    public VonskrEntity(EntityType<? extends VonskrEntity> type, Level level) {
        super(type, level);
        this.xpReward = 8;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 28.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.34D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.55D)
                .add(Attributes.FOLLOW_RANGE, 34.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    public static boolean checkVonskrSpawnRules(EntityType<VonskrEntity> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && Mob.checkMobSpawnRules(type, level, reason, pos, random);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SITTING, false);
        this.entityData.define(DATA_FEMALE, false);
        this.entityData.define(DATA_GROWTH_TICKS, GROWTH_TICKS);
        this.entityData.define(DATA_ATTACK_TICKS, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.45F) {
            @Override
            public boolean canUse() {
                return !VonskrEntity.this.isSitting() && !VonskrEntity.this.isBabyVonskr() && super.canUse();
            }
        });
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.22D, true) {
            @Override
            public boolean canUse() {
                return !VonskrEntity.this.isSitting() && !VonskrEntity.this.isBabyVonskr() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !VonskrEntity.this.isSitting() && !VonskrEntity.this.isBabyVonskr() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.90D) {
            @Override
            public boolean canUse() {
                return !VonskrEntity.this.isSitting() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !VonskrEntity.this.isSitting() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ForceUserEntity.class, 10, true, false,
                target -> target instanceof ForceUserEntity forceUser && shouldHuntForceUser(forceUser)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                target -> target instanceof Player player && shouldHuntPlayer(player)));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        setFemale(this.random.nextBoolean());
        setAdultVonskr();
        if (reason == MobSpawnType.SPAWN_EGG) {
            this.setPersistenceRequired();
        }
        return result;
    }

    @Override
    public void tick() {
        super.tick();
        tickVonskrAgeAndTimers();
        if (this.level().isClientSide || !isTamed()) {
            return;
        }

        Player owner = this.level().getPlayerByUUID(this.ownerUuid);
        if (owner == null) {
            return;
        }

        if (getTarget() == owner) {
            setTarget(null);
        }

        if (isSitting()) {
            setTarget(null);
            getNavigation().stop();
            return;
        }

        tickOwnerDefense(owner);

        double distance = distanceToSqr(owner);
        if (distance > 256.0D) {
            teleportNearOwner(owner);
        } else if (distance > 9.0D && getTarget() == null) {
            getNavigation().moveTo(owner, 1.12D);
        }
    }

    private void tickVonskrAgeAndTimers() {
        if (getAttackAnimationTicks() > 0) {
            this.entityData.set(DATA_ATTACK_TICKS, getAttackAnimationTicks() - 1);
        }

        if (isBabyVonskr()) {
            int nextAge = Math.min(GROWTH_TICKS, getGrowthTicks() + 1);
            this.entityData.set(DATA_GROWTH_TICKS, nextAge);
        }

        if (this.breedCooldownTicks > 0) {
            this.breedCooldownTicks--;
        }

        if (this.loveTicks > 0) {
            this.loveTicks--;
            if (!this.level().isClientSide && this.loveTicks % 20 == 0) {
                tryBreedWithNearbyMate();
            }
        }
    }

    private void tickOwnerDefense(Player owner) {
        LivingEntity attacker = owner.getLastHurtByMob();
        int hurtByTimestamp = owner.getLastHurtByMobTimestamp();
        if (attacker != null && hurtByTimestamp != this.ownerLastHurtByTimestamp && wantsToAttackForOwner(attacker, owner)) {
            this.ownerLastHurtByTimestamp = hurtByTimestamp;
            setTarget(attacker);
            return;
        }

        LivingEntity target = owner.getLastHurtMob();
        int hurtTargetTimestamp = owner.getLastHurtMobTimestamp();
        if (target != null && hurtTargetTimestamp != this.ownerLastHurtTargetTimestamp && wantsToAttackForOwner(target, owner)) {
            this.ownerLastHurtTargetTimestamp = hurtTargetTimestamp;
            setTarget(target);
        }
    }

    private boolean wantsToAttackForOwner(LivingEntity target, Player owner) {
        if (isBabyVonskr() || !target.isAlive() || target == this || target == owner) {
            return false;
        }
        if (target instanceof VonskrEntity otherVonskr && otherVonskr.isOwnedBy(owner)) {
            return false;
        }
        return true;
    }

    private void teleportNearOwner(Player owner) {
        for (int attempt = 0; attempt < 10; attempt++) {
            int x = owner.getBlockX() + this.random.nextInt(7) - 3;
            int z = owner.getBlockZ() + this.random.nextInt(7) - 3;
            int y = owner.getBlockY() + this.random.nextInt(3) - 1;
            if (this.randomTeleport(x + 0.5D, y, z + 0.5D, true)) {
                getNavigation().stop();
                return;
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isTamed()) {
            if (isOwnedBy(player)) {
                if (isBreedingFood(stack)) {
                    if (!this.level().isClientSide) {
                        tryStartBreeding(player, stack);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }

                if (!this.level().isClientSide) {
                    setSitting(!isSitting());
                    setTarget(null);
                    getNavigation().stop();
                    player.displayClientMessage(Component.literal(isSitting() ? "The Vonskr sits." : "The Vonskr follows."), true);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            return super.mobInteract(player, hand);
        }

        if (!canTameByAlignment(player)) {
            if (!this.level().isClientSide) {
                setTarget(player);
                player.displayClientMessage(Component.literal("The Vonskr rejects Jedi and neutral Force users."), true);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (!isTamingFood(stack)) {
            return super.mobInteract(player, hand);
        }

        if (!this.level().isClientSide) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (this.random.nextInt(3) == 0) {
                tame(player);
                this.level().broadcastEntityEvent(this, TAMING_SUCCESS_EVENT);
            } else {
                this.level().broadcastEntityEvent(this, TAMING_FAILED_EVENT);
                this.level().playSound(null, blockPosition(), ModSounds.VONSKR_ATTACK.get(), SoundSource.NEUTRAL, 0.18F, 0.7F);
            }
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    private void tame(Player player) {
        this.ownerUuid = player.getUUID();
        setSitting(true);
        this.setPersistenceRequired();
        this.setTarget(null);
        this.getNavigation().stop();
    }

    public boolean isTamed() {
        return this.ownerUuid != null;
    }

    public boolean isSitting() {
        return this.entityData.get(DATA_SITTING);
    }

    private void setSitting(boolean sitting) {
        this.entityData.set(DATA_SITTING, sitting);
    }

    public boolean isFemale() {
        return this.entityData.get(DATA_FEMALE);
    }

    private void setFemale(boolean female) {
        this.entityData.set(DATA_FEMALE, female);
    }

    public int getGrowthTicks() {
        return this.entityData.get(DATA_GROWTH_TICKS);
    }

    public boolean isBabyVonskr() {
        return getGrowthTicks() < GROWTH_TICKS;
    }

    private void setAdultVonskr() {
        this.entityData.set(DATA_GROWTH_TICKS, GROWTH_TICKS);
    }

    private void setBabyVonskr() {
        this.entityData.set(DATA_GROWTH_TICKS, 0);
    }

    public int getAttackAnimationTicks() {
        return this.entityData.get(DATA_ATTACK_TICKS);
    }

    private boolean isOwnedBy(Player player) {
        return this.ownerUuid != null && this.ownerUuid.equals(player.getUUID());
    }

    private boolean shouldHuntForceUser(ForceUserEntity forceUser) {
        if (isTamed() || isSitting() || isBabyVonskr() || !forceUser.isAlive() || forceUser.isGhost()) {
            return false;
        }
        return !forceUser.getForceUserSide().isDark();
    }

    private boolean shouldHuntPlayer(Player player) {
        if (isTamed() || isSitting() || isBabyVonskr() || isOwnedBy(player)) {
            return false;
        }
        return !canTameByAlignment(player);
    }

    private static boolean canTameByAlignment(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return true;
        }
        return serverPlayer.getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(cap -> !cap.hasPower(ForcePower.FORCE_SENSITIVITY)
                        || cap.getCommittedSide() == ForceSide.DARK
                        || cap.hasPower(ForcePower.DARK_SIDE))
                .orElse(true);
    }

    private static boolean isTamingFood(ItemStack stack) {
        return stack.is(Items.BEEF)
                || stack.is(Items.PORKCHOP)
                || stack.is(Items.MUTTON)
                || stack.is(Items.CHICKEN)
                || stack.is(Items.RABBIT)
                || stack.is(Items.COD)
                || stack.is(Items.SALMON)
                || stack.is(Items.COOKED_BEEF)
                || stack.is(Items.COOKED_PORKCHOP)
                || stack.is(Items.COOKED_MUTTON)
                || stack.is(Items.COOKED_CHICKEN)
                || stack.is(Items.COOKED_RABBIT)
                || stack.is(Items.COOKED_COD)
                || stack.is(Items.COOKED_SALMON)
                || stack.is(Items.ROTTEN_FLESH);
    }

    private static boolean isBreedingFood(ItemStack stack) {
        return stack.is(galaxyunderchaos.WINGMAW_FEATHER.get());
    }

    private void tryStartBreeding(Player player, ItemStack stack) {
        if (isBabyVonskr()) {
            player.displayClientMessage(Component.literal("This Vonskr is still too young to breed."), true);
            return;
        }
        if (this.breedCooldownTicks > 0) {
            player.displayClientMessage(Component.literal("This Vonskr is not ready to breed again yet."), true);
            return;
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        this.loveTicks = BREED_READY_TICKS;
        setSitting(false);
        this.level().broadcastEntityEvent(this, BREED_READY_EVENT);
        player.displayClientMessage(Component.literal("The Vonskr is ready to breed."), true);
        tryBreedWithNearbyMate();
    }

    private void tryBreedWithNearbyMate() {
        if (!(this.level() instanceof ServerLevel serverLevel) || !canBreedNow()) {
            return;
        }

        VonskrEntity mate = serverLevel.getEntitiesOfClass(VonskrEntity.class, this.getBoundingBox().inflate(8.0D), this::canBreedWith)
                .stream()
                .findFirst()
                .orElse(null);
        if (mate == null) {
            return;
        }

        spawnBabyWith(serverLevel, mate);
    }
    @Override
    protected float getSoundVolume() {
        return 0.18F;
    }
    private boolean canBreedNow() {
        return isTamed() && !isSitting() && !isBabyVonskr() && this.loveTicks > 0 && this.breedCooldownTicks <= 0 && this.ownerUuid != null;
    }

    private boolean canBreedWith(VonskrEntity mate) {
        return mate != this
                && mate.canBreedNow()
                && mate.ownerUuid != null
                && mate.ownerUuid.equals(this.ownerUuid)
                && mate.isFemale() != this.isFemale();
    }

    private void spawnBabyWith(ServerLevel serverLevel, VonskrEntity mate) {
        VonskrEntity baby = galaxyunderchaos.VONSKR.get().create(serverLevel);
        if (baby == null) {
            return;
        }

        this.loveTicks = 0;
        mate.loveTicks = 0;
        this.breedCooldownTicks = BREED_COOLDOWN_TICKS;
        mate.breedCooldownTicks = BREED_COOLDOWN_TICKS;

        baby.ownerUuid = this.ownerUuid;
        baby.setFemale(this.random.nextBoolean());
        baby.setBabyVonskr();
        baby.setSitting(false);
        baby.setPersistenceRequired();
        baby.moveTo((this.getX() + mate.getX()) * 0.5D, this.getY(), (this.getZ() + mate.getZ()) * 0.5D, this.getYRot(), 0.0F);
        serverLevel.addFreshEntity(baby);
        serverLevel.broadcastEntityEvent(this, BABY_BORN_EVENT);
        serverLevel.broadcastEntityEvent(mate, BABY_BORN_EVENT);
        serverLevel.broadcastEntityEvent(baby, BABY_BORN_EVENT);
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt) {
            this.entityData.set(DATA_ATTACK_TICKS, ATTACK_ANIMATION_TICKS);
            this.swing(InteractionHand.MAIN_HAND);
            this.playSound(ModSounds.VONSKR_ATTACK.get(), 0.22F, this.getVoicePitch());
        }
        return hurt;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == TAMING_SUCCESS_EVENT) {
            spawnClientParticles(ParticleTypes.HEART, 7);
        } else if (id == TAMING_FAILED_EVENT) {
            spawnClientParticles(ParticleTypes.SMOKE, 7);
        } else if (id == BREED_READY_EVENT || id == BABY_BORN_EVENT) {
            spawnClientParticles(ParticleTypes.HEART, id == BABY_BORN_EVENT ? 10 : 5);
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void spawnClientParticles(net.minecraft.core.particles.ParticleOptions particle, int count) {
        for (int i = 0; i < count; i++) {
            double x = this.getRandomX(0.8D);
            double y = this.getRandomY() + 0.25D;
            double z = this.getRandomZ(0.8D);
            double dx = (this.random.nextDouble() - 0.5D) * 0.06D;
            double dy = this.random.nextDouble() * 0.08D + 0.02D;
            double dz = (this.random.nextDouble() - 0.5D) * 0.06D;
            this.level().addParticle(particle, x, y, z, dx, dy, dz);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.VONSKR_LIVING.get();
    }
    @Override
    public int getAmbientSoundInterval() {
        return 500 + this.random.nextInt(400);
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.VONSKR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.VONSKR_HURT.get();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return isTamed() || super.requiresCustomPersistence();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUuid != null) {
            tag.putUUID("Owner", this.ownerUuid);
        }
        tag.putBoolean("Sitting", isSitting());
        tag.putBoolean("Female", isFemale());
        tag.putInt("GrowthTicks", getGrowthTicks());
        tag.putInt("BreedCooldownTicks", this.breedCooldownTicks);
        tag.putInt("LoveTicks", this.loveTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner")) {
            this.ownerUuid = tag.getUUID("Owner");
        } else {
            this.ownerUuid = null;
        }
        setSitting(tag.getBoolean("Sitting"));
        setFemale(tag.getBoolean("Female"));
        if (tag.contains("GrowthTicks")) {
            this.entityData.set(DATA_GROWTH_TICKS, Math.min(GROWTH_TICKS, Math.max(0, tag.getInt("GrowthTicks"))));
        } else {
            setAdultVonskr();
        }
        this.breedCooldownTicks = Math.max(0, tag.getInt("BreedCooldownTicks"));
        this.loveTicks = Math.max(0, tag.getInt("LoveTicks"));
    }
}
