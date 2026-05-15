package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModHangingSignBlockEntityPolar extends HangingSignBlockEntity {
    public ModHangingSignBlockEntityPolar(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POLAR_HANGING_SIGN_BE.get(), pos, state);
    }
}
