package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModHangingSignBlockEntityNabooPine extends HangingSignBlockEntity {
    public ModHangingSignBlockEntityNabooPine(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NABOO_PINE_HANGING_SIGN_BE.get(), pos, state);
    }
}
