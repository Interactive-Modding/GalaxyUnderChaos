package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Simple block entity to render the custom Bleeding Table model. */
public class BleedingTableBlockEntity extends BlockEntity {

    public BleedingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLEEDING_TABLE_BE.get(), pos, state);
    }
}
