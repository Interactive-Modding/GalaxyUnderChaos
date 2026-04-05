package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.entity.CoffinBlockEntity;
import server.galaxyunderchaos.sound.ModSounds;

public abstract class AbstractCoffinBlock extends BaseEntityBlock {
    protected AbstractCoffinBlock(Properties properties) {
        super(properties);
    }

    protected InteractionResult tryOpenContainer(Level level, BlockPos pos, Player player) {
        BlockPos mainPos = getMainBlockPos(level.getBlockState(pos), pos);
        BlockEntity blockEntity = level.getBlockEntity(mainPos);
        if (blockEntity instanceof CoffinBlockEntity coffin) {
            if (!level.isClientSide) {
                player.openMenu((MenuProvider) coffin);
            }
            level.playSound(null, mainPos, ModSounds.TOMB_SOUND.get(), net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    protected void dropMainContainer(Level level, BlockPos pos, BlockState state) {
        BlockPos mainPos = getMainBlockPos(state, pos);
        BlockEntity blockEntity = level.getBlockEntity(mainPos);
        if (blockEntity instanceof CoffinBlockEntity coffin) {
            Containers.dropContents(level, mainPos, coffin);
            level.updateNeighbourForOutputSignal(mainPos, state.getBlock());
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            dropMainContainer(level, pos, state);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    protected abstract BlockPos getMainBlockPos(BlockState state, BlockPos pos);

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);
}
