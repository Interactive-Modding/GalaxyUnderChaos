package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModHangingSignBlockEntityDillia extends HangingSignBlockEntity {
    public ModHangingSignBlockEntityDillia(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DILLIA_HANGING_SIGN_BE.get(), pos, state);
    }
}
