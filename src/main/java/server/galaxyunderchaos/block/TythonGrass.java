package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import static server.galaxyunderchaos.galaxyunderchaos.TYTHON_DIRT;

public class TythonGrass extends GrassBlock {
    public TythonGrass() {
        super(BlockBehaviour.Properties.of()
                .strength(0.6F)
                .randomTicks()
                .mapColor(MapColor.GRASS)
                .sound(SoundType.GRASS));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean isClientSide) {
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!world.isClientSide) {
            if (world.getBlockState(pos.above()).isAir()) {
                // Spread logic
                for (int i = 0; i < 4; ++i) {
                    BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
                    if (world.getBlockState(blockpos).getBlock() instanceof TythonDirt && world.getBlockState(blockpos.above()).isAir()) {
                        world.setBlock(blockpos, this.defaultBlockState(), 3);
                    }
                }
            }
            // Check if grass is stacked
            BlockPos belowPos = pos.below();
            if (world.getBlockState(belowPos).getBlock() instanceof TythonGrass) {
                world.setBlock(belowPos, TYTHON_DIRT.get().defaultBlockState(), 3);
            }
        }
    }
}
