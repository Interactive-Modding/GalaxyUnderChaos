package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class TempleStone extends Block {
    public TempleStone() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE)
                .mapColor(MapColor.STONE)
                .strength(3.0f, 10.0f)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.NORMAL));
    }
}