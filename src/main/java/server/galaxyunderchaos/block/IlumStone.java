package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;

public class IlumStone extends Block {
    public IlumStone() {
        super(Properties.of()
                .strength(3.0f, 10.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.GLASS)
                .pushReaction(PushReaction.NORMAL));
    }
}