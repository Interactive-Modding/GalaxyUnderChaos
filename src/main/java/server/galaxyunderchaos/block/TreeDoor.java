package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/** Simple wooden door that uses the supplied BlockSetType for sounds. */
public class TreeDoor extends DoorBlock {

    public TreeDoor(BlockSetType setType) {
        super(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(2.0F, 2.0F)
                        .requiresCorrectToolForDrops()
                        .noOcclusion(),
                setType
        );
    }
}
