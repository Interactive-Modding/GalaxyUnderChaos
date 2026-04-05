package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/** Wooden button that can be activated by arrows (like oak). */
public class TreeButton extends ButtonBlock {

    public TreeButton(BlockSetType setType) {
        /* order in 1.21: (setType, pressTicks, props, arrows) */
        super(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(1.0F)
                        .noOcclusion(),
                setType,
                30,
                true
        );
    }
}
