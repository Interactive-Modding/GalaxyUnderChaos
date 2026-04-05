package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.properties.WoodType;
import server.galaxyunderchaos.entity.ModBlockEntities;

/* Ceiling‑mounted hanging sign */
public class TreeHangingSign extends CeilingHangingSignBlock {

    public TreeHangingSign(WoodType woodType) {
        super(Properties.of()
                                .mapColor(MapColor.WOOD)
                                .strength(1.0F)
                                .noOcclusion(),
                woodType);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.AK_HANGING_SIGN_BE.get().create(pos, state);
    }
}