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
import static server.galaxyunderchaos.entity.HeartBerryBoat.Type.HEART_BERRY;

public class HeartBerryChestBoat extends ChestBoat {
    // Use HeartBerryChestBoat.class here, not Boat.class
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE =
            SynchedEntityData.defineId(HeartBerryChestBoat.class, EntityDataSerializers.INT);

    public HeartBerryChestBoat(EntityType<? extends ChestBoat> type, Level level) {
        super(type, level);
    }

    public HeartBerryChestBoat(Level level, double x, double y, double z) {
        this((EntityType<? extends ChestBoat>) ModEntityTypes.HEART_BERRY_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, HEART_BERRY.ordinal());
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
            setVariant(HeartBerryBoat.Type.byName(tag.getString("Type")));
        }
    }

    @Override
    public Item getDropItem() {
        // drop the correct chest boat item
        return switch (getModVariant()) {
            case HEART_BERRY -> galaxyunderchaos.HEART_BERRY_CHEST_BOAT.get();
        };
    }

    public HeartBerryBoat.Type getModVariant() {
        return HeartBerryBoat.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public void setVariant(HeartBerryBoat.Type variant) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal());
    }
}
