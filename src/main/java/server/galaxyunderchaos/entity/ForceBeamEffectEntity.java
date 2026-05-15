package server.galaxyunderchaos.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import server.galaxyunderchaos.galaxyunderchaos;

public class ForceBeamEffectEntity extends Entity implements IEntityAdditionalSpawnData {
    public static final int KIND_LIGHTNING = 0;
    public static final int KIND_DRAIN = 1;

    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(ForceBeamEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(ForceBeamEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EFFECT_KIND = SynchedEntityData.defineId(ForceBeamEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_TICKS = SynchedEntityData.defineId(ForceBeamEffectEntity.class, EntityDataSerializers.INT);

    private boolean hasFixedEnd;
    private Vec3 fixedEnd = Vec3.ZERO;

    public ForceBeamEffectEntity(EntityType<? extends ForceBeamEffectEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.noCulling = true;
    }

    public ForceBeamEffectEntity(Level level, LivingEntity owner, LivingEntity target, int kind, int lifetime) {
        this(galaxyunderchaos.FORCE_BEAM_EFFECT.get(), level);
        this.entityData.set(OWNER_ID, owner.getId());
        this.entityData.set(TARGET_ID, target.getId());
        this.entityData.set(EFFECT_KIND, kind);
        this.entityData.set(LIFE_TICKS, Math.max(1, lifetime));
        this.hasFixedEnd = false;
        this.fixedEnd = Vec3.ZERO;
        this.refreshAnchorPosition();
    }

    public ForceBeamEffectEntity(Level level, LivingEntity owner, Vec3 end, int kind, int lifetime) {
        this(galaxyunderchaos.FORCE_BEAM_EFFECT.get(), level);
        this.entityData.set(OWNER_ID, owner.getId());
        this.entityData.set(TARGET_ID, -1);
        this.entityData.set(EFFECT_KIND, kind);
        this.entityData.set(LIFE_TICKS, Math.max(1, lifetime));
        this.hasFixedEnd = true;
        this.fixedEnd = end;
        this.refreshAnchorPosition();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_ID, -1);
        this.entityData.define(TARGET_ID, -1);
        this.entityData.define(EFFECT_KIND, KIND_LIGHTNING);
        this.entityData.define(LIFE_TICKS, 6);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(OWNER_ID, tag.getInt("Owner"));
        this.entityData.set(TARGET_ID, tag.getInt("Target"));
        this.entityData.set(EFFECT_KIND, tag.getInt("Kind"));
        this.entityData.set(LIFE_TICKS, Math.max(1, tag.getInt("Life")));
        this.hasFixedEnd = tag.getBoolean("HasFixedEnd");
        this.fixedEnd = new Vec3(tag.getDouble("EndX"), tag.getDouble("EndY"), tag.getDouble("EndZ"));
        this.refreshAnchorPosition();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Owner", this.entityData.get(OWNER_ID));
        tag.putInt("Target", this.entityData.get(TARGET_ID));
        tag.putInt("Kind", this.entityData.get(EFFECT_KIND));
        tag.putInt("Life", this.entityData.get(LIFE_TICKS));
        tag.putBoolean("HasFixedEnd", this.hasFixedEnd);
        tag.putDouble("EndX", this.fixedEnd.x);
        tag.putDouble("EndY", this.fixedEnd.y);
        tag.putDouble("EndZ", this.fixedEnd.z);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.entityData.get(OWNER_ID));
        buffer.writeVarInt(this.entityData.get(TARGET_ID));
        buffer.writeVarInt(this.entityData.get(EFFECT_KIND));
        buffer.writeVarInt(this.entityData.get(LIFE_TICKS));
        buffer.writeBoolean(this.hasFixedEnd);
        buffer.writeDouble(this.fixedEnd.x);
        buffer.writeDouble(this.fixedEnd.y);
        buffer.writeDouble(this.fixedEnd.z);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.entityData.set(OWNER_ID, additionalData.readVarInt());
        this.entityData.set(TARGET_ID, additionalData.readVarInt());
        this.entityData.set(EFFECT_KIND, additionalData.readVarInt());
        this.entityData.set(LIFE_TICKS, Math.max(1, additionalData.readVarInt()));
        this.hasFixedEnd = additionalData.readBoolean();
        this.fixedEnd = new Vec3(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());
        this.refreshAnchorPosition();
    }

    @Override
    public void tick() {
        super.tick();

        Entity owner = getOwnerEntity();
        Entity target = getTargetEntity();

        if (!(owner instanceof LivingEntity)) {
            if (level().isClientSide && tickCount < 4) {
                return;
            }
            discard();
            return;
        }

        if (!hasFixedEnd && !(target instanceof LivingEntity)) {
            if (level().isClientSide && tickCount < 4) {
                return;
            }
            discard();
            return;
        }

        if (!owner.isAlive() || (!hasFixedEnd && target != null && !target.isAlive())) {
            discard();
            return;
        }

        refreshAnchorPosition();

        if (tickCount >= getLifeTicks()) {
            discard();
        }
    }

    private void refreshAnchorPosition() {
        Entity owner = getOwnerEntity();
        if (owner == null) {
            return;
        }

        Vec3 end = getBeamEnd(1.0F);
        if (end == null) {
            return;
        }

        double x = (owner.getX() + end.x) * 0.5D;
        double y = (owner.getEyeY() + end.y) * 0.5D;
        double z = (owner.getZ() + end.z) * 0.5D;
        setPos(x, y, z);
    }

    public Entity getOwnerEntity() {
        return level().getEntity(this.entityData.get(OWNER_ID));
    }

    public Entity getTargetEntity() {
        int targetId = this.entityData.get(TARGET_ID);
        return targetId < 0 ? null : level().getEntity(targetId);
    }

    public Vec3 getBeamEnd(float partialTick) {
        Entity target = getTargetEntity();
        if (target instanceof LivingEntity livingTarget) {
            return livingTarget.getPosition(partialTick).add(0.0D, livingTarget.getBbHeight() * 0.55D, 0.0D);
        }
        return hasFixedEnd ? fixedEnd : null;
    }

    public int getEffectKind() {
        return this.entityData.get(EFFECT_KIND);
    }

    public int getLifeTicks() {
        return this.entityData.get(LIFE_TICKS);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 8192.0D;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
