package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;

public class EarthCrystalOre extends Block {
    public EarthCrystalOre() {
        super(Properties.of()
                .strength(4.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE)
                .pushReaction(PushReaction.NORMAL));
    }
}
