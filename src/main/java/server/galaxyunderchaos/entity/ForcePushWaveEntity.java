package server.galaxyunderchaos.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import server.galaxyunderchaos.galaxyunderchaos;

public class ForcePushWaveEntity extends Entity {
    private static final EntityDataAccessor<Float> MAX_RADIUS = SynchedEntityData.defineId(ForcePushWaveEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> LIFE_TICKS = SynchedEntityData.defineId(ForcePushWaveEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(ForcePushWaveEntity.class, EntityDataSerializers.INT);

    public ForcePushWaveEntity(EntityType<? extends ForcePushWaveEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public ForcePushWaveEntity(Level level, LivingEntity owner, float maxRadius, int lifeTicks) {
        this(galaxyunderchaos.FORCE_PUSH_WAVE.get(), level);
        this.entityData.set(MAX_RADIUS, Math.max(1.0F, maxRadius));
        this.entityData.set(LIFE_TICKS, Math.max(1, lifeTicks));
        this.entityData.set(OWNER_ID, owner.getId());
        setPos(owner.getX(), owner.getY(0.1D), owner.getZ());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAX_RADIUS, 4.0F);
        this.entityData.define(LIFE_TICKS, 10);
        this.entityData.define(OWNER_ID, -1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(MAX_RADIUS, tag.getFloat("MaxRadius"));
        this.entityData.set(LIFE_TICKS, tag.getInt("Life"));
        this.entityData.set(OWNER_ID, tag.getInt("Owner"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("MaxRadius", getMaxRadius());
        tag.putInt("Life", getLifeTicks());
        tag.putInt("Owner", this.entityData.get(OWNER_ID));
    }

    @Override
    public void tick() {
        super.tick();
        Entity owner = getOwnerEntity();
        if (owner != null) {
            setPos(owner.getX(), owner.getY(0.1D), owner.getZ());
        }
        if (tickCount >= getLifeTicks()) {
            discard();
        }
    }

    public float getMaxRadius() {
        return this.entityData.get(MAX_RADIUS);
    }

    public int getLifeTicks() {
        return this.entityData.get(LIFE_TICKS);
    }

    public float getCurrentRadius(float partialTick) {
        float progress = Math.min(1.0F, (tickCount + partialTick) / (float) getLifeTicks());
        return getMaxRadius() * progress;
    }

    public Entity getOwnerEntity() {
        return level().getEntity(this.entityData.get(OWNER_ID));
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096.0D;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
