package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GroundSaberStandBlockEntity extends BlockEntity {

    private ItemStack saber = ItemStack.EMPTY;

    public GroundSaberStandBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SABER_STAND_BE.get(), pos, state);
    }

    public ItemStack getItem() {
        return saber;
    }

    public boolean isEmpty() {
        return saber.isEmpty();
    }

    public void setItem(ItemStack stack) {
        this.saber = stack;
        setChanged();
        syncToClient();
    }

    public ItemStack removeItem() {
        ItemStack out = saber.copy();
        this.saber = ItemStack.EMPTY;
        setChanged();
        syncToClient();
        return out;
    }

    private void syncToClient() {
        Level level = this.level;
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!saber.isEmpty()) {
            tag.put("Saber", saber.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        saber = tag.contains("Saber") ? ItemStack.of(tag.getCompound("Saber")) : ItemStack.EMPTY;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}
