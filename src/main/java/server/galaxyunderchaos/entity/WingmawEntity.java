package server.galaxyunderchaos.entity;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import server.galaxyunderchaos.sound.ModSounds;

public class WingmawEntity extends Monster {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public WingmawEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FlyingGoal(this));  // Flying behavior
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, true)); // Aggressive melee attack
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0)); // Wanders when no target
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F)); // Watches players
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this)); // Looks around randomly

        // Target selector (Aggressive AI)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this)); // Retaliates when hit
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true)); // Targets players
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D) // Higher health for toughness
                .add(Attributes.MOVEMENT_SPEED, 0.15D) // Fast spider movement (adjust if necessary)
                .add(Attributes.ATTACK_DAMAGE, 6.0D) // Deals 6 damage per hit
                .add(Attributes.ARMOR, 2.0D) // Slight armor to resist hits
                .add(Attributes.FOLLOW_RANGE, 24.0D) // Aggressive range
                .add(Attributes.FLYING_SPEED, 0.5D);  // Adjust flying speed if necessary
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanFloat(true);
        return navigation;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (flag && target instanceof LivingEntity livingTarget) {
            livingTarget.hurt(this.damageSources().mobAttack(this), 3.0F); // Corrected method
            this.playSound(ModSounds.WINGMAW_ATTACK.get(), 1.0F, this.getVoicePitch());
        }
        return flag;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.WINGMAW_LIVING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.WINGMAW_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.WINGMAW_HURT.get();
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public void aiStep() {
        this.setNoGravity(true);
        super.aiStep();
    }

    public static class FlyingGoal extends Goal {
        private final WingmawEntity wingmaw;

        public FlyingGoal(WingmawEntity wingmaw) {
            this.wingmaw = wingmaw;
        }

        @Override
        public boolean canUse() {
            // Example: Entity can fly if it's not in water or on the ground
            return !wingmaw.isInWater() && wingmaw.getTarget() != null;
        }

        @Override
        public void start() {
            wingmaw.getNavigation().setCanFloat(true);
        }

        @Override
        public void stop() {
            // Stop flying behavior
            wingmaw.getNavigation().setCanFloat(false);
        }

        @Override
        public void tick() {
            if (wingmaw.getTarget() != null) {
                wingmaw.getNavigation().moveTo(wingmaw.getTarget(), 1.0);
            }
        }
    }
}
