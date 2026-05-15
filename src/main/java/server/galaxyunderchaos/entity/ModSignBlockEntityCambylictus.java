package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlockEntityCambylictus extends SignBlockEntity {
    public ModSignBlockEntityCambylictus(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CAMBYLICTUS_SIGN_BE.get(), pos, state);
    }
}
