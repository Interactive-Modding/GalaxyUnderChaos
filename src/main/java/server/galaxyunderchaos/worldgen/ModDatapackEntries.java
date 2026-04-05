//package server.galaxyunderchaos.worldgen;
//
//import net.minecraft.core.HolderLookup;
//import net.minecraft.core.RegistrySetBuilder;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.data.PackOutput;
//import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
//import net.minecraftforge.registries.ForgeRegistries;
//import server.galaxyunderchaos.galaxyunderchaos;
//import server.galaxyunderchaos.worldgen.biome.ModBiomes;
//import server.galaxyunderchaos.worldgen.dimension.ModDimensions;
//
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//
//public class ModDatapackEntries extends DatapackBuiltinEntriesProvider {
//
//    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
//            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
//            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
//            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
//            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
//            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem)
//            .add(Registries.NOISE_SETTINGS, ModNoiseSettings::bootstrap)
//            .add(Registries.BIOME, ModBiomes::bootstrap);
//
//
//    public ModDatapackEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
//        super(output, registries, BUILDER, Set.of(galaxyunderchaos.MODID));
//    }
//}