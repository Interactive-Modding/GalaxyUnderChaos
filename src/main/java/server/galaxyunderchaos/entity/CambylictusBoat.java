package server.galaxyunderchaos.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import server.galaxyunderchaos.galaxyunderchaos;

import static server.galaxyunderchaos.entity.CambylictusBoat.Type.CAMBYLICTUS;

public class CambylictusBoat extends Boat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE =
            SynchedEntityData.defineId(CambylictusBoat.class, EntityDataSerializers.INT);

    public CambylictusBoat(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    public CambylictusBoat(Level level, double x, double y, double z) {
        this((EntityType<? extends Boat>) ModEntityTypes.CAMBYLICTUS_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, CAMBYLICTUS.ordinal());
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
            setVariant(Type.byName(tag.getString("Type")));
        }
    }

    @Override
    public Item getDropItem() {
        return switch (getModVariant()) {
            case CAMBYLICTUS -> galaxyunderchaos.CAMBYLICTUS_BOAT.get();
        };
    }

    public void setVariant(Type variant) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal());
    }

    public Type getModVariant() {
        return Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public enum Type implements StringRepresentable {
        CAMBYLICTUS("cambylictus", galaxyunderchaos.CAMBYLICTUS_PLANKS.get());

        private final String name;
        private final Block planks;

        Type(String name, Block planks) {
            this.name = name;
            this.planks = planks;
        }

        @Override
        public String getSerializedName() { return this.name; }

        public Block getPlanks() { return this.planks; }

        public static final net.minecraft.util.StringRepresentable.EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        public static Type byName(String name) { return CODEC.byName(name, CAMBYLICTUS); }
        public static Type byId(int id) { return values()[id < 0 || id >= values().length ? 0 : id]; }
    }
}
