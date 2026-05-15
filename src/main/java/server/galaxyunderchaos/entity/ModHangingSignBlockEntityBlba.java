package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModHangingSignBlockEntityBlba extends HangingSignBlockEntity {
    public ModHangingSignBlockEntityBlba(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLBA_HANGING_SIGN_BE.get(), pos, state);
    }
}
