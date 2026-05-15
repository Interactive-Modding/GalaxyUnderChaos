package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GroundSaberStandBlockEntity extends BlockEntity {

    private static final String TAG_HAS_SABER = "HasSaber";
    private static final String TAG_SABER = "Saber";

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
        if (stack.isEmpty()) {
            this.saber = ItemStack.EMPTY;
        } else {
            this.saber = stack.copy();
            this.saber.setCount(1);
        }

        setChanged();
        syncToClient();
    }

    public ItemStack removeItem() {
        if (this.saber.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack out = this.saber.copy();
        this.saber = ItemStack.EMPTY;

        setChanged();
        syncToClient();

        return out;
    }

    private void syncToClient() {
        Level level = this.level;

        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();

            level.sendBlockUpdated(
                    worldPosition,
                    state,
                    state,
                    Block.UPDATE_CLIENTS
            );
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putBoolean(TAG_HAS_SABER, !saber.isEmpty());

        if (!saber.isEmpty()) {
            tag.put(TAG_SABER, saber.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        boolean hasSaber = tag.getBoolean(TAG_HAS_SABER);

        if (hasSaber && tag.contains(TAG_SABER, Tag.TAG_COMPOUND)) {
            this.saber = ItemStack.of(tag.getCompound(TAG_SABER));
        } else {
            this.saber = ItemStack.EMPTY;
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();

        if (tag != null) {
            handleUpdateTag(tag);
        } else {
            // Important: a null/empty update should still clear stale client render data.
            this.saber = ItemStack.EMPTY;
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}