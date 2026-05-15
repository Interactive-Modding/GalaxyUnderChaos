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

import static server.galaxyunderchaos.entity.PolarBoat.Type.POLAR;

public class PolarChestBoat extends ChestBoat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE =
            SynchedEntityData.defineId(PolarChestBoat.class, EntityDataSerializers.INT);

    public PolarChestBoat(EntityType<? extends ChestBoat> type, Level level) {
        super(type, level);
    }

    public PolarChestBoat(Level level, double x, double y, double z) {
        this((EntityType<? extends ChestBoat>) ModEntityTypes.POLAR_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, POLAR.ordinal());
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
            setVariant(PolarBoat.Type.byName(tag.getString("Type")));
        }
    }

    @Override
    public Item getDropItem() {
        return switch (getModVariant()) {
            case POLAR -> galaxyunderchaos.POLAR_CHEST_BOAT.get();
        };
    }

    public PolarBoat.Type getModVariant() {
        return PolarBoat.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public void setVariant(PolarBoat.Type variant) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal());
    }
}
