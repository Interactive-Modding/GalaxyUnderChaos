package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import server.galaxyunderchaos.entity.ModBlockEntities;

public class TreeWallSignPerlote extends WallSignBlock {
    public TreeWallSignPerlote(WoodType woodType) {
        super(Properties.of().mapColor(MapColor.WOOD).strength(1.0F).noOcclusion(), woodType);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.PERLOTE_SIGN_BE.get().create(pos, state);
    }
}
