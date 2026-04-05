package server.galaxyunderchaos.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import server.galaxyunderchaos.galaxyunderchaos;

public class SeatEntity extends Entity {

    public SeatEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {

    }

    public SeatEntity(Level level, double x, double y, double z) {
        this(galaxyunderchaos.SEAT.get(), level);
        this.setPos(x, y, z);
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public void tick() {
        super.tick();
        if (this.getPassengers().isEmpty() && !this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
