package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlockEntityNabooPine extends SignBlockEntity {
    public ModSignBlockEntityNabooPine(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NABOO_PINE_SIGN_BE.get(), pos, state);
    }
}
