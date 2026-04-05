package server.galaxyunderchaos.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.galaxyunderchaos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import server.galaxyunderchaos.sound.ModSounds;

public class AcidSpiderEntity extends Monster {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public AcidSpiderEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, true)); // Aggressive melee attack
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0)); // Wanders when no target
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F)); // Watches players
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this)); // Looks around randomly

        // Target selector (Aggressive AI)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this)); // Retaliates when hit
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true)); // Targets players
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D) // Higher health for toughness
                .add(Attributes.MOVEMENT_SPEED, 0.35D) // Fast spider movement
                .add(Attributes.ATTACK_DAMAGE, 6.0D) // Deals 6 damage per hit
                .add(Attributes.ARMOR, 2.0D) // Slight armor to resist hits
                .add(Attributes.FOLLOW_RANGE, 24.0D); // Aggressive range
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (flag && target instanceof LivingEntity livingTarget) {
            livingTarget.hurt(this.damageSources().mobAttack(this), 3.0F); // Corrected method
            this.playSound(ModSounds.ACID_SPIDER_ATTACK.get(), 1.0F, this.getVoicePitch());
        }
        return flag;
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
    protected SoundEvent getAmbientSound() {
        return ModSounds.ACID_SPIDER_LIVING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.ACID_SPIDER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ACID_SPIDER_HURT.get();
    }

}
