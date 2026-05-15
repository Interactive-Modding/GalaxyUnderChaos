package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.galaxyunderchaos;

public class ModFlammableRotatedPillarBlock extends RotatedPillarBlock {
    public ModFlammableRotatedPillarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if(context.getItemInHand().getItem() instanceof AxeItem) {
            if(state.is(galaxyunderchaos.AK_LOG.get())) {
                return galaxyunderchaos.STRIPPED_AK_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.AK_WOOD.get())) {
                return galaxyunderchaos.STRIPPED_AK_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.BLBA_LOG.get())) {
                return galaxyunderchaos.STRIPPED_BLBA_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.BLBA_WOOD.get())) {
                return galaxyunderchaos.STRIPPED_BLBA_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.DILLIA_LOG.get())) {
                return galaxyunderchaos.STRIPPED_DILLIA_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.DILLIA_WOOD.get())) {
                return galaxyunderchaos.STRIPPED_DILLIA_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.CAMBYLICTUS_LOG.get())) {
                return galaxyunderchaos.STRIPPED_CAMBYLICTUS_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.CAMBYLICTUS_WOOD.get())) {
                return galaxyunderchaos.STRIPPED_CAMBYLICTUS_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.PERLOTE_LOG.get())) {
                return galaxyunderchaos.STRIPPED_PERLOTE_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.PERLOTE_WOOD.get())) {
                return galaxyunderchaos.STRIPPED_PERLOTE_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.RUTIGER_LOG.get())) {
                return galaxyunderchaos.STRIPPED_RUTIGER_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.RUTIGER_WOOD.get())) {
                return galaxyunderchaos.STRIPPED_RUTIGER_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.POLAR_LOG.get())) {
                return galaxyunderchaos.STRIPPED_POLAR_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.POLAR_WOOD.get())) {
                return galaxyunderchaos.STRIPPED_POLAR_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.NABOO_PINE_LOG.get())) {
                return galaxyunderchaos.STRIPPED_NABOO_PINE_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.NABOO_PINE_WOOD.get())) {
                return galaxyunderchaos.STRIPPED_NABOO_PINE_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.HEART_BERRY_LOG.get())) {
                return galaxyunderchaos.STRIPPED_HEART_BERRY_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
            if(state.is(galaxyunderchaos.HEART_BERRY_WOOD.get())) {
                return galaxyunderchaos.STRIPPED_HEART_BERRY_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
        }

        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}