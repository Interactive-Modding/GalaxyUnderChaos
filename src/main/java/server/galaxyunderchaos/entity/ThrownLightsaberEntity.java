package server.galaxyunderchaos.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.HashSet;
import java.util.Set;

public class ThrownLightsaberEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<ItemStack> THROWN_STACK = SynchedEntityData.defineId(ThrownLightsaberEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> RETURNING = SynchedEntityData.defineId(ThrownLightsaberEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(ThrownLightsaberEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RETURN_SPEED = SynchedEntityData.defineId(ThrownLightsaberEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> OUTBOUND_TICKS = SynchedEntityData.defineId(ThrownLightsaberEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_LIFE_TICKS = SynchedEntityData.defineId(ThrownLightsaberEntity.class, EntityDataSerializers.INT);

    private final Set<Integer> hitEntityIds = new HashSet<>();

    public ThrownLightsaberEntity(EntityType<? extends ThrownLightsaberEntity> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    public ThrownLightsaberEntity(Level level, LivingEntity owner, ItemStack stack, float damage, float returnSpeed) {
        super(galaxyunderchaos.THROWN_LIGHTSABER.get(), owner, level);
        this.setNoGravity(true);
        this.entityData.set(THROWN_STACK, stack.copyWithCount(1));
        this.entityData.set(DAMAGE, damage);
        this.entityData.set(RETURN_SPEED, returnSpeed);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(THROWN_STACK, ItemStack.EMPTY);
        this.entityData.define(RETURNING, false);
        this.entityData.define(DAMAGE, 6.0F);
        this.entityData.define(RETURN_SPEED, 1.2F);
        this.entityData.define(OUTBOUND_TICKS, 10);
        this.entityData.define(MAX_LIFE_TICKS, 40);
    }

    @Override
    public void tick() {
        if (!isReturning()) {
            super.tick();
            updateRotationFromMotion();
            if (this.tickCount >= getOutboundTicks()) {
                setReturning(true);
            }
        } else {
            Entity owner = getOwner();
            if (owner == null || !owner.isAlive()) {
                dropOrDiscard();
                return;
            }
            Vec3 target = owner.getEyePosition().subtract(position());
            double distance = target.length();
            if (distance < 1.2D) {
                returnToOwner();
                return;
            }
            Vec3 motion = target.normalize().scale(this.entityData.get(RETURN_SPEED));
            setDeltaMovement(motion);
            updateRotationFromMotion();
            setPos(getX() + motion.x, getY() + motion.y, getZ() + motion.z);
            checkReturningHits();
        }

        if (this.tickCount >= getMaxLifeTicks()) {
            setReturning(true);
        }
    }

    public void setOutboundTicks(int outboundTicks) {
        this.entityData.set(OUTBOUND_TICKS, Math.max(4, outboundTicks));
    }

    public int getOutboundTicks() {
        return Math.max(4, this.entityData.get(OUTBOUND_TICKS));
    }

    public void setMaxLifeTicks(int maxLifeTicks) {
        this.entityData.set(MAX_LIFE_TICKS, Math.max(getOutboundTicks() + 10, maxLifeTicks));
    }

    public int getMaxLifeTicks() {
        return Math.max(getOutboundTicks() + 10, this.entityData.get(MAX_LIFE_TICKS));
    }

    private void updateRotationFromMotion() {
        Vec3 motion = getDeltaMovement();
        if (motion.lengthSqr() < 1.0E-5D) {
            return;
        }
        float horizontal = (float) Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        float yaw = (float) (net.minecraft.util.Mth.atan2(motion.z, motion.x) * (180.0F / Math.PI));
        float pitch = (float) (net.minecraft.util.Mth.atan2(motion.y, horizontal) * (180.0F / Math.PI));
        this.setYRot(yaw);
        this.setXRot(pitch);
    }

    private void checkReturningHits() {
        for (Entity entity : level().getEntities(this, getBoundingBox().inflate(0.45D), e -> e instanceof LivingEntity && e != getOwner())) {
            if (hitEntityIds.add(entity.getId()) && entity instanceof LivingEntity living) {
                living.hurt(damageSources().mobProjectile(this, (LivingEntity) getOwner()), this.entityData.get(DAMAGE));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() == getOwner()) {
            return;
        }
        if (hitEntityIds.add(result.getEntity().getId()) && result.getEntity() instanceof LivingEntity living) {
            living.hurt(damageSources().mobProjectile(this, (LivingEntity) getOwner()), this.entityData.get(DAMAGE));
        }
        setReturning(true);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        setReturning(true);
    }

    private void returnToOwner() {
        Entity owner = getOwner();
        if (owner instanceof ServerPlayer player && !player.getAbilities().instabuild) {
            ItemStack stack = getItem().copy();
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }
        discard();
    }

    private void dropOrDiscard() {
        if (!level().isClientSide && !getItem().isEmpty()) {
            spawnAtLocation(getItem().copy());
        }
        discard();
    }

    public void setReturning(boolean returning) {
        this.entityData.set(RETURNING, returning);
    }

    public boolean isReturning() {
        return this.entityData.get(RETURNING);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.STICK;
    }

    @Override
    public ItemStack getItem() {
        return this.entityData.get(THROWN_STACK);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("ThrownStack", getItem().save(new CompoundTag()));
        tag.putBoolean("Returning", isReturning());
        tag.putFloat("Damage", this.entityData.get(DAMAGE));
        tag.putFloat("ReturnSpeed", this.entityData.get(RETURN_SPEED));
        tag.putInt("OutboundTicks", getOutboundTicks());
        tag.putInt("MaxLifeTicks", getMaxLifeTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ThrownStack")) {
            this.entityData.set(THROWN_STACK, ItemStack.of(tag.getCompound("ThrownStack")));
        }
        this.entityData.set(RETURNING, tag.getBoolean("Returning"));
        this.entityData.set(DAMAGE, tag.getFloat("Damage"));
        this.entityData.set(RETURN_SPEED, tag.getFloat("ReturnSpeed"));
        if (tag.contains("OutboundTicks")) {
            setOutboundTicks(tag.getInt("OutboundTicks"));
        }
        if (tag.contains("MaxLifeTicks")) {
            setMaxLifeTicks(tag.getInt("MaxLifeTicks"));
        }
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
