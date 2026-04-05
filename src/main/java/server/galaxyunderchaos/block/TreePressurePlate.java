/* ─── Pressure Plate ─────────────────────────────────────────── */
package server.galaxyunderchaos.block;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TreePressurePlate extends PressurePlateBlock {

    public TreePressurePlate(BlockSetType setType) {
        super(Sensitivity.EVERYTHING,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(0.5F)
                        .noOcclusion(),
                setType);
    }
}
