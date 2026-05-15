package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlockEntityBlba extends SignBlockEntity {
    public ModSignBlockEntityBlba(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLBA_SIGN_BE.get(), pos, state);
    }
}
