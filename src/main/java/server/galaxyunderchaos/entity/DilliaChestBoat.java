package server.galaxyunderchaos.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import server.galaxyunderchaos.galaxyunderchaos;

import static server.galaxyunderchaos.entity.DilliaBoat.Type.DILLIA;

public class DilliaChestBoat extends ChestBoat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE =
            SynchedEntityData.defineId(DilliaChestBoat.class, EntityDataSerializers.INT);

    public DilliaChestBoat(EntityType<? extends ChestBoat> type, Level level) {
        super(type, level);
    }

    public DilliaChestBoat(Level level, double x, double y, double z) {
        this(ModEntityTypes.DILLIA_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, DILLIA.ordinal());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Type", getModVariant().getSerializedName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Type", CompoundTag.TAG_STRING)) {
            setVariant(DilliaBoat.Type.byName(tag.getString("Type")));
        }
    }

    @Override
    public Item getDropItem() {
        return galaxyunderchaos.DILLIA_CHEST_BOAT.get();
    }

    public DilliaBoat.Type getModVariant() {
        return DilliaBoat.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public void setVariant(DilliaBoat.Type variant) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal());
    }
}
