package server.galaxyunderchaos.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import server.galaxyunderchaos.galaxyunderchaos;

import static server.galaxyunderchaos.entity.AkBoat.Type.AK;

public class AkBoat extends Boat {
    // exactly one accessor, registered against AkBoat.class
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE =
            SynchedEntityData.defineId(AkBoat.class, EntityDataSerializers.INT);

    public AkBoat(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    public AkBoat(Level level, double x, double y, double z) {
        this((EntityType<? extends Boat>) ModEntityTypes.AK_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        // let Boat.defineSynchedData do its thing (registers vanilla fields)
        super.defineSynchedData();
        // now register our variant field, defaulting to AK
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
            setVariant(Type.byName(tag.getString("Type")));
        }
    }

    @Override
    public Item getDropItem() {
        return switch (getModVariant()) {
            case AK -> galaxyunderchaos.AK_BOAT.get();
        };
    }

    public void setVariant(Type variant) {
        this.entityData.set(DATA_ID_TYPE, variant.ordinal());
    }

    public Type getModVariant() {
        return Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public enum Type implements net.minecraft.util.StringRepresentable {
        AK("ak", galaxyunderchaos.AK_PLANKS.get());

        private final String name;
        private final net.minecraft.world.level.block.Block planks;

        Type(String name, net.minecraft.world.level.block.Block planks) {
            this.name = name;
            this.planks = planks;
        }

        @Override
        public String getSerializedName() { return this.name; }

        public Block getPlanks() { return this.planks; }

        public static final net.minecraft.util.StringRepresentable.EnumCodec<Type> CODEC =
                net.minecraft.util.StringRepresentable.fromEnum(Type::values);
        public static Type byName(String name)  { return CODEC.byName(name, AK); }
        public static Type byId(int id)         { return values()[id < 0 || id >= values().length ? 0 : id]; }
    }
}
