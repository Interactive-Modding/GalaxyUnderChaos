package server.galaxyunderchaos.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.worldgen.ModBiomeModifiers;
import server.galaxyunderchaos.worldgen.ModCarvers;
import server.galaxyunderchaos.worldgen.ModConfiguredFeatures;
import server.galaxyunderchaos.worldgen.ModDensityFunctions;
import server.galaxyunderchaos.worldgen.ModNoiseSettings;
import server.galaxyunderchaos.worldgen.ModNoises;
import server.galaxyunderchaos.worldgen.ModPlacedFeatures;
import server.galaxyunderchaos.worldgen.biome.ModBiomes;
import server.galaxyunderchaos.worldgen.dimension.ModDimensions;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.NOISE, ModNoises::bootstrap)
            .add(Registries.DENSITY_FUNCTION, ModDensityFunctions::bootstrap)
            .add(Registries.NOISE_SETTINGS, ModNoiseSettings::bootstrap)
            .add(Registries.CONFIGURED_CARVER, ModCarvers::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem);
    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(galaxyunderchaos.MODID));
    }
}