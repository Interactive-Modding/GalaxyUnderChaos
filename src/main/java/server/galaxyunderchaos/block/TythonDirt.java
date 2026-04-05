package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TythonDirt extends Block {
    private static final Logger LOGGER = LogManager.getLogger();

    public TythonDirt() {
        super(BlockBehaviour.Properties.of()
                .strength(0.5f)
                .sound(SoundType.GRAVEL)
                .noOcclusion()); // Added noOcclusion for better interaction handling
    }
}