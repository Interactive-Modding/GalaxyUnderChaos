package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModHangingSignBlockEntityRutiger extends HangingSignBlockEntity {
    public ModHangingSignBlockEntityRutiger(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RUTIGER_HANGING_SIGN_BE.get(), pos, state);
    }
}
