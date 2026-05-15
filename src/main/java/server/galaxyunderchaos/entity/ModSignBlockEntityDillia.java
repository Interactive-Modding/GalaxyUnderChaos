package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlockEntityDillia extends SignBlockEntity {
    public ModSignBlockEntityDillia(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DILLIA_SIGN_BE.get(), pos, state);
    }
}
