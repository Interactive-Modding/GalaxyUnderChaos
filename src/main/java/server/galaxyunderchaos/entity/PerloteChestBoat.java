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

import static server.galaxyunderchaos.entity.PerloteBoat.Type.PERLOTE;

public class PerloteChestBoat extends ChestBoat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE =
            SynchedEntityData.defineId(PerloteChestBoat.class, EntityDataSerializers.INT);

    public PerloteChestBoat(EntityType<? extends ChestBoat> type, Level level) {
        super(type, level);
    }

    public PerloteChestBoat(Level level, double x, double y, double z) {
        this((EntityType<? extends ChestBoat>) ModEntityTypes.PERLOTE_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, PERLOTE.ordinal());
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
            setVariant(PerloteBoat.Type.byName(tag.getString("Type")));
        }
    }

    @Override
    public Item getDropItem() {
        return switch (getModVariant()) {
            case PERLOTE -> galaxyunderchaos.PERLOTE_CHEST_BOAT.get();
        };
    }

    public PerloteBoat.Type getModVariant() {
        return PerloteBoat.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public void setVariant(PerloteBoat.Type variant) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal());
    }
}
