package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlockEntityPerlote extends SignBlockEntity {
    public ModSignBlockEntityPerlote(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PERLOTE_SIGN_BE.get(), pos, state);
    }
}
