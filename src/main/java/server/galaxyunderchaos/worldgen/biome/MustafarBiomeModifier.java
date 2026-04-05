//package server.galaxyunderchaos.worldgen.biome;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.level.ChunkPos;
//import net.minecraft.world.level.biome.Biome;
//import net.minecraft.world.level.chunk.LevelChunk;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.event.level.ChunkEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import server.galaxyunderchaos.galaxyunderchaos;
//
//@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID)
//public class MustafarBiomeModifier {
//
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    @SubscribeEvent
//    public static void onChunkLoad(ChunkEvent.Load event) {
//        if (!(event.getChunk() instanceof LevelChunk chunk)) return;
//
//        if (!(chunk.getLevel() instanceof ServerLevel level)) return;
//
//        ChunkPos chunkPos = chunk.getPos();
//        boolean isModified = false;
//
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                BlockPos columnPos = chunkPos.getBlockAt(x, level.getMinBuildHeight(), z);
//                ResourceKey<Biome> biomeKey = level.getBiome(columnPos).unwrapKey().orElse(null);
//
//                if (biomeKey != null && isMustafarBiome(biomeKey)) {
//                    modifyChunk(chunk, level, chunkPos);
//                    isModified = true;
//                    break; // Modify the chunk only once if Mustafar biome detected
//                }
//            }
//            if (isModified) break;
//        }
//    }
//
//    private static boolean isMustafarBiome(ResourceKey<Biome> biomeKey) {
//        return biomeKey.equals(ModBiomes.MUSTAFAR_LAVA_FIELD) ||
//                biomeKey.equals(ModBiomes.MUSTAFAR_VOLCANIC_PLAINS) ||
//                biomeKey.equals(ModBiomes.MUSTAFAR_MAGMA_LAKE);
//    }
//
//    private static void modifyChunk(LevelChunk chunk, ServerLevel level, ChunkPos chunkPos) {
//        LOGGER.info("Modifying chunk at position: {}", chunkPos);
//
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
//                    BlockPos pos = chunkPos.getBlockAt(x, y, z);
//                    BlockState state = chunk.getBlockState(pos);
//
//                    if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.STONE) || state.is(Blocks.DEEPSLATE)) {
//                        chunk.setBlockState(pos, Blocks.BASALT.defaultBlockState(), false);
//                    } else if (state.is(Blocks.WATER)) {
//                        chunk.setBlockState(pos, Blocks.LAVA.defaultBlockState(), false);
//                    }
//                }
//            }
//        }
//
//        LOGGER.info("Chunk modification completed for position: {}", chunkPos);
//    }
//}
