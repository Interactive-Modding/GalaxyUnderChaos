package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModHangingSignBlockEntityCambylictus extends HangingSignBlockEntity {
    public ModHangingSignBlockEntityCambylictus(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAMBYLICTUS_HANGING_SIGN_BE.get(), pos, state);
    }
}
