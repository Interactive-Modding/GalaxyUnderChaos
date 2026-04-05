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
import static server.galaxyunderchaos.entity.AkBoat.Type.AK;

public class AkChestBoat extends ChestBoat {
    // Use AkChestBoat.class here, not Boat.class
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE =
            SynchedEntityData.defineId(AkChestBoat.class, EntityDataSerializers.INT);

    public AkChestBoat(EntityType<? extends ChestBoat> type, Level level) {
        super(type, level);
    }

    public AkChestBoat(Level level, double x, double y, double z) {
        this((EntityType<? extends ChestBoat>) ModEntityTypes.AK_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, AK.ordinal());
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
            setVariant(AkBoat.Type.byName(tag.getString("Type")));
        }
    }

    @Override
    public Item getDropItem() {
        // drop the correct chest boat item
        return switch (getModVariant()) {
            case AK -> galaxyunderchaos.AK_CHEST_BOAT.get();
        };
    }

    public AkBoat.Type getModVariant() {
        return AkBoat.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public void setVariant(AkBoat.Type variant) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal());
    }
}
