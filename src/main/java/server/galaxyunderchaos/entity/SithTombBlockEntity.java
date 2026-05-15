package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Separate block entity type for the normal Sith tomb/coffin.
 *
 * It intentionally extends the same gameplay container logic but is registered
 * under its own BlockEntityType so the Geckolib sarcophagus renderer registered
 * for COFFIN_BE does not render over the older Sith tomb model.
 */
public class SithTombBlockEntity extends CoffinBlockEntity {
    public SithTombBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SITH_TOMB_BE.get(), pos, state);
    }
}
