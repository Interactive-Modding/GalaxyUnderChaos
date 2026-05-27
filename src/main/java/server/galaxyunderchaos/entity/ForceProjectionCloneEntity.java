package server.galaxyunderchaos.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import server.galaxyunderchaos.galaxyunderchaos;

import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class ForceProjectionCloneEntity extends PathfinderMob implements IEntityAdditionalSpawnData {
    @Nullable
    private UUID ownerUuid;
    private String ownerName = "Projection";
    private int lifeTicks = 160;
    private int powerTier = 0;

    public ForceProjectionCloneEntity(EntityType<? extends ForceProjectionCloneEntity> type, Level level) {
        super(type, level);
        this.noCulling = true;
        this.xpReward = 0;
    }

    public ForceProjectionCloneEntity(ServerLevel level, LivingEntity owner, int lifeTicks, int powerTier) {
        this(galaxyunderchaos.FORCE_PROJECTION_CLONE.get(), level);
        this.ownerUuid = owner.getUUID();
        this.ownerName = resolveOwnerName(owner);
        this.lifeTicks = Math.max(40, lifeTicks);
        this.powerTier = Math.max(0, powerTier);
        this.setCustomName(Component.literal(this.ownerName));
        this.setCustomNameVisible(true);
        copyEquipment(owner);
        setHealth(this.powerTier >= 2 ? 1.0F : getMaxHealth());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.27D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 18.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.18D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.75D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    private static String resolveOwnerName(LivingEntity owner) {
        if (owner instanceof Player player) {
            return player.getGameProfile().getName();
        }
        String display = owner.getDisplayName().getString();
        return display == null || display.isBlank() ? "Projection" : display;
    }

    private void copyEquipment(LivingEntity owner) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = owner.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                this.setItemSlot(slot, stack.copy());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.powerTier >= 2) {
                acquireProjectionTarget();
            }
            if (this.tickCount >= this.lifeTicks || this.getHealth() <= 0.0F) {
                discard();
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide && this.powerTier >= 2 && amount > 0.0F) {
            discard();
            return true;
        }
        boolean result = super.hurt(source, amount);
        if (!this.level().isClientSide && this.getHealth() <= 0.0F) {
            discard();
        }
        return result;
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean result = super.doHurtTarget(target);
        if (result && !this.level().isClientSide && this.powerTier >= 2 && target instanceof LivingEntity livingTarget) {
            livingTarget.invulnerableTime = 0;
        }
        return result;
    }

    private void acquireProjectionTarget() {
        if (!(this.level() instanceof ServerLevel level)) {
            return;
        }
        LivingEntity owner = getOwner(level);
        if (owner == null || !owner.isAlive()) {
            return;
        }
        LivingEntity current = getTarget();
        if (current != null && current.isAlive() && current != owner) {
            return;
        }
        Mob attacker = level.getEntitiesOfClass(Mob.class, getBoundingBox().inflate(18.0D), mob ->
                        mob != this && mob.isAlive() && mob.getTarget() == owner)
                .stream()
                .min(java.util.Comparator.comparingDouble(mob -> mob.distanceToSqr(this)))
                .orElse(null);
        if (attacker != null) {
            setTarget(attacker);
            return;
        }
        Monster nearbyMonster = level.getEntitiesOfClass(Monster.class, getBoundingBox().inflate(10.0D), monster ->
                        monster.isAlive() && monster.hasLineOfSight(this) && monster != owner)
                .stream()
                .min(java.util.Comparator.comparingDouble(monster -> monster.distanceToSqr(this)))
                .orElse(null);
        if (nearbyMonster != null) {
            setTarget(nearbyMonster);
        }
    }

    @Nullable
    private LivingEntity getOwner(ServerLevel level) {
        return this.ownerUuid != null && level.getEntity(this.ownerUuid) instanceof LivingEntity owner ? owner : null;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        // Projection clones are decoys, not loot sources.
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    public UUID getOwnerUuid() {
        return this.ownerUuid != null ? this.ownerUuid : this.getUUID();
    }

    public int getPowerTier() {
        return this.powerTier;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) {
            this.ownerUuid = tag.getUUID("Owner");
        }
        this.ownerName = tag.getString("OwnerName");
        if (this.ownerName.isBlank()) {
            this.ownerName = "Projection";
        }
        this.setCustomName(Component.literal(this.ownerName));
        this.setCustomNameVisible(true);
        this.lifeTicks = Math.max(40, tag.getInt("LifeTicks"));
        this.powerTier = Math.max(0, tag.getInt("PowerTier"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        if (this.ownerUuid != null) {
            tag.putUUID("Owner", this.ownerUuid);
        }
        tag.putString("OwnerName", this.ownerName == null ? "Projection" : this.ownerName);
        tag.putInt("LifeTicks", this.lifeTicks);
        tag.putInt("PowerTier", this.powerTier);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.ownerUuid != null);
        if (this.ownerUuid != null) {
            buffer.writeUUID(this.ownerUuid);
        }
        buffer.writeUtf(this.ownerName == null ? "Projection" : this.ownerName, 64);
        buffer.writeVarInt(this.lifeTicks);
        buffer.writeVarInt(this.powerTier);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.ownerUuid = buffer.readBoolean() ? buffer.readUUID() : null;
        this.ownerName = buffer.readUtf(64);
        if (this.ownerName.isBlank()) {
            this.ownerName = "Projection";
        }
        this.setCustomName(Component.literal(this.ownerName));
        this.setCustomNameVisible(true);
        this.lifeTicks = Math.max(40, buffer.readVarInt());
        this.powerTier = Math.max(0, buffer.readVarInt());
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
