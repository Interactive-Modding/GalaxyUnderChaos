package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;

public class DeepslateCrystalOre extends Block {
    public DeepslateCrystalOre() {
        super(Properties.of()
                .strength(4.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.DEEPSLATE)
                .pushReaction(PushReaction.NORMAL));
    }
}
