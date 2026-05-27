package server.galaxyunderchaos.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.worldgen.dimension.PlanetTimeWeatherHandler;

/**
 * Adds a hard bedrock floor to every Galaxy Under Chaos planet dimension.
 * This is intentionally chunk-load based so already-generated planet chunks are
 * repaired too, not only new chunks.
 */
@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlanetBedrockEventHandler {
    private PlanetBedrockEventHandler() {}

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        if (!PlanetTimeWeatherHandler.isManagedPlanet(level)) {
            return;
        }

        ChunkAccess chunk = event.getChunk();
        ChunkPos chunkPos = chunk.getPos();
        int minY = level.getMinBuildHeight();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        boolean changed = false;
        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                mutable.set(chunkPos.getMinBlockX() + localX, minY, chunkPos.getMinBlockZ() + localZ);
                if (!chunk.getBlockState(mutable).is(Blocks.BEDROCK)) {
                    chunk.setBlockState(mutable, bedrock, false);
                    changed = true;
                }
            }
        }

        if (changed) {
            chunk.setUnsaved(true);
        }
    }
}
