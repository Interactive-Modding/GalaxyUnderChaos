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

/**
 * Client-rendered Force ability visual.  This is intentionally not a vanilla
 * particle effect and not a visible potion-effect particle.  Each Force power
 * gets a short-lived entity renderer so the visuals stay authored by GUC.
 */
public class ForceAbilityEffectEntity extends Entity implements IEntityAdditionalSpawnData {
    public static final int KIND_HEAL = 0;
    public static final int KIND_FORTIFY = 1;
    public static final int KIND_STUN = 2;
    public static final int KIND_WOUND = 3;
    public static final int KIND_STEALTH = 4;
    public static final int KIND_SPEED = 5;
    public static final int KIND_SIGHT = 6;
    public static final int KIND_MEDITATION = 7;
    public static final int KIND_RESIST = 8;
    public static final int KIND_REBOUND = 9;
    public static final int KIND_THROW = 10;

    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(ForceAbilityEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(ForceAbilityEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EFFECT_KIND = SynchedEntityData.defineId(ForceAbilityEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_TICKS = SynchedEntityData.defineId(ForceAbilityEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(ForceAbilityEffectEntity.class, EntityDataSerializers.FLOAT);

    public ForceAbilityEffectEntity(EntityType<? extends ForceAbilityEffectEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.noCulling = true;
    }

    public ForceAbilityEffectEntity(Level level, LivingEntity owner, int kind, int lifetime, float radius) {
        this(level, owner, null, kind, lifetime, radius);
    }

    public ForceAbilityEffectEntity(Level level, LivingEntity owner, LivingEntity target, int kind, int lifetime, float radius) {
        this(galaxyunderchaos.FORCE_ABILITY_EFFECT.get(), level);
        this.entityData.set(OWNER_ID, owner.getId());
        this.entityData.set(TARGET_ID, target == null ? -1 : target.getId());
        this.entityData.set(EFFECT_KIND, kind);
        this.entityData.set(LIFE_TICKS, Math.max(1, lifetime));
        this.entityData.set(RADIUS, Math.max(0.15F, radius));
        refreshAnchorPosition();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_ID, -1);
        this.entityData.define(TARGET_ID, -1);
        this.entityData.define(EFFECT_KIND, KIND_HEAL);
        this.entityData.define(LIFE_TICKS, 12);
        this.entityData.define(RADIUS, 1.0F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(OWNER_ID, tag.getInt("Owner"));
        this.entityData.set(TARGET_ID, tag.getInt("Target"));
        this.entityData.set(EFFECT_KIND, tag.getInt("Kind"));
        this.entityData.set(LIFE_TICKS, Math.max(1, tag.getInt("Life")));
        this.entityData.set(RADIUS, Math.max(0.15F, tag.getFloat("Radius")));
        refreshAnchorPosition();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Owner", this.entityData.get(OWNER_ID));
        tag.putInt("Target", this.entityData.get(TARGET_ID));
        tag.putInt("Kind", this.entityData.get(EFFECT_KIND));
        tag.putInt("Life", this.entityData.get(LIFE_TICKS));
        tag.putFloat("Radius", this.entityData.get(RADIUS));
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.entityData.get(OWNER_ID));
        buffer.writeVarInt(this.entityData.get(TARGET_ID));
        buffer.writeVarInt(this.entityData.get(EFFECT_KIND));
        buffer.writeVarInt(this.entityData.get(LIFE_TICKS));
        buffer.writeFloat(this.entityData.get(RADIUS));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.entityData.set(OWNER_ID, buffer.readVarInt());
        this.entityData.set(TARGET_ID, buffer.readVarInt());
        this.entityData.set(EFFECT_KIND, buffer.readVarInt());
        this.entityData.set(LIFE_TICKS, Math.max(1, buffer.readVarInt()));
        this.entityData.set(RADIUS, Math.max(0.15F, buffer.readFloat()));
        refreshAnchorPosition();
    }

    @Override
    public void tick() {
        super.tick();

        Entity owner = getOwnerEntity();
        Entity anchor = getAnchorEntity();
        if (!(owner instanceof LivingEntity) || !(anchor instanceof LivingEntity)) {
            if (level().isClientSide && tickCount < 4) {
                return;
            }
            discard();
            return;
        }

        if (!owner.isAlive() || !anchor.isAlive()) {
            discard();
            return;
        }

        refreshAnchorPosition();
        if (tickCount >= getLifeTicks()) {
            discard();
        }
    }

    private void refreshAnchorPosition() {
        Entity anchor = getAnchorEntity();
        if (anchor != null) {
            setPos(anchor.getX(), anchor.getY(), anchor.getZ());
        }
    }

    public Entity getOwnerEntity() {
        return level().getEntity(this.entityData.get(OWNER_ID));
    }

    public Entity getTargetEntity() {
        int targetId = this.entityData.get(TARGET_ID);
        return targetId < 0 ? null : level().getEntity(targetId);
    }

    public Entity getAnchorEntity() {
        Entity target = getTargetEntity();
        return target == null ? getOwnerEntity() : target;
    }

    public Vec3 getRenderAnchor(float partialTick) {
        Entity anchor = getAnchorEntity();
        return anchor == null ? position() : anchor.getPosition(partialTick);
    }

    public int getEffectKind() {
        return this.entityData.get(EFFECT_KIND);
    }

    public int getLifeTicks() {
        return this.entityData.get(LIFE_TICKS);
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public float getProgress(float partialTick) {
        return Math.min(1.0F, Math.max(0.0F, (tickCount + partialTick) / Math.max(1.0F, getLifeTicks())));
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
