package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class TreeFence extends FenceBlock {

    public TreeFence() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F, 2.0F)
                .requiresCorrectToolForDrops());
    }
}
