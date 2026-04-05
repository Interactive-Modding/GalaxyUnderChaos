package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlockEntityHB extends SignBlockEntity {

    public ModSignBlockEntityHB(BlockPos pos, BlockState state) {
        /* 👇 tell the parent which type we really are */
        super(ModBlockEntities.HEART_BERRY_SIGN_BE.get(), pos, state);

    }
}