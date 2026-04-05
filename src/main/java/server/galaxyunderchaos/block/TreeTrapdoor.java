package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class TreeTrapdoor extends TrapDoorBlock {

    public TreeTrapdoor(BlockSetType setType) {
        super(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(3.0F)
                        .requiresCorrectToolForDrops()
                        .noOcclusion(),
                setType
        );
    }
}
