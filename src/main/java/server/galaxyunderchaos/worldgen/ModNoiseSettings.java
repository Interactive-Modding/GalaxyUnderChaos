package server.galaxyunderchaos.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.worldgen.dimension.ModDimensions;

import java.util.List;
import java.util.Map;

public class ModNoiseSettings {

    private record PlanetNoiseProfile(
            ResourceKey<NoiseGeneratorSettings> noiseSettingsKey,
            ResourceKey<DensityFunction> terrainDensity,
            Block topBlock,
            Block defaultBlock,
            Block defaultFluid,
            int seaLevel,
            int minY,
            int height,
            int sizeHorizontal,
            int sizeVertical
    ) {}

    private static final Map<String, PlanetNoiseProfile> PLANETS = Map.ofEntries(

            // Sea level lowered to 32 so plains/forests/mountains stay dry.
            // Tython has tall cliffs and high land — water only fills deep valleys.
            Map.entry("tython",
                    new PlanetNoiseProfile(
                            ModDimensions.TYTHON_NOISE,
                            ModDensityFunctions.TYTHON_TERRAIN,
                            Blocks.GRASS_BLOCK,
                            Blocks.STONE,
                            Blocks.WATER,
                            32, -64, 384, 1, 2
                    )),

            // Naboo intentionally has oceans/swamps but land biomes need to be above water.
            // Sea level lowered to 48 so plains and forests are safely above it.
            Map.entry("naboo",
                    new PlanetNoiseProfile(
                            ModDimensions.NABOO_NOISE,
                            ModDensityFunctions.NABOO_TERRAIN,
                            Blocks.GRASS_BLOCK,
                            Blocks.STONE,
                            Blocks.WATER,
                            48, -64, 384, 1, 2
                    )),

            Map.entry("ilum",
                    new PlanetNoiseProfile(
                            ModDimensions.ILUM_NOISE,
                            ModDensityFunctions.ILUM_TERRAIN,
                            Blocks.SNOW_BLOCK,
                            Blocks.STONE,
                            Blocks.ICE,
                            63, -64, 384, 1, 2
                    )),

            Map.entry("mustafar",
                    new PlanetNoiseProfile(
                            ModDimensions.MUSTAFAR_NOISE,
                            ModDensityFunctions.MUSTAFAR_TERRAIN,
                            Blocks.BASALT,
                            Blocks.SMOOTH_BASALT,
                            Blocks.LAVA,
                            32, -64, 384, 1, 2
                    )),

            Map.entry("ossus",
                    new PlanetNoiseProfile(
                            ModDimensions.OSSUS_NOISE,
                            ModDensityFunctions.OSSUS_TERRAIN,
                            Blocks.GRASS_BLOCK,
                            Blocks.STONE,
                            Blocks.WATER,
                            63, -64, 384, 1, 2
                    )),

            // Ashla is a gentle meadow moon — sea level dropped to 20 so
            // the low-amplitude terrain is never drowned by water.
            Map.entry("ashla",
                    new PlanetNoiseProfile(
                            ModDimensions.ASHLA_NOISE,
                            ModDensityFunctions.ASHLA_TERRAIN,
                            Blocks.MOSS_BLOCK,
                            Blocks.STONE,
                            Blocks.WATER,
                            20, -64, 384, 1, 2
                    )),

            // Bogan is a dark moon — sea level dropped to 32 so chaotic
            // terrain pits don't flood the surface biome.
            Map.entry("bogan",
                    new PlanetNoiseProfile(
                            ModDimensions.BOGAN_NOISE,
                            ModDensityFunctions.BOGAN_TERRAIN,
                            Blocks.MYCELIUM,
                            Blocks.DEEPSLATE,
                            Blocks.WATER,
                            32, -64, 384, 1, 2
                    )),

            Map.entry("malachor",
                    new PlanetNoiseProfile(
                            ModDimensions.MALACHOR_NOISE,
                            ModDensityFunctions.MALACHOR_TERRAIN,
                            galaxyunderchaos.MALACHITE_OBSIDIAN.get(),
                            Blocks.ANDESITE,
                            Blocks.AIR,
                            40, -64, 384, 1, 2
                    )),

            Map.entry("korriban",
                    new PlanetNoiseProfile(
                            ModDimensions.KORRIBAN_NOISE,
                            ModDensityFunctions.KORRIBAN_TERRAIN,
                            Blocks.RED_SAND,
                            Blocks.RED_SANDSTONE,
                            Blocks.AIR,
                            28, -64, 384, 1, 2
                    )),

            // Dantooine is peaceful plains — sea level dropped to 20 so
            // the gentle low-amplitude terrain is never underwater.
            Map.entry("dantooine",
                    new PlanetNoiseProfile(
                            ModDimensions.DANTOOINE_NOISE,
                            ModDensityFunctions.DANTOOINE_TERRAIN,
                            Blocks.GRASS_BLOCK,
                            Blocks.STONE,
                            Blocks.WATER,
                            20, -64, 384, 1, 2
                    ))
    );

    public static void bootstrap(BootstapContext<NoiseGeneratorSettings> context) {

        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);

        PLANETS.values().forEach(profile -> {

            Holder<DensityFunction> terrain = densityFunctions.getOrThrow(profile.terrainDensity);

            // Wrap the registered density function so it can be used inside the router
            DensityFunction terrainFunction = new DensityFunctions.HolderHolder(terrain);

            // NoiseRouter parameter order (Minecraft 1.20.x):
            //  0  barrierNoise
            //  1  fluidLevelFloodednessNoise
            //  2  fluidLevelSpreadNoise
            //  3  lavaNoise
            //  4  temperature
            //  5  vegetation
            //  6  continents
            //  7  erosion
            //  8  depth                          <- influences surface rule depth checks
            //  9  ridges
            // 10  initialDensityWithoutJaggedness <- used for aquifer placement
            // 11  finalDensity                    <- THIS shapes the actual terrain
            // 12  veinToggle
            // 13  veinRidged
            // 14  veinGap
            NoiseRouter router = new NoiseRouter(
                    DensityFunctions.zero(), // 0  barrierNoise
                    DensityFunctions.zero(), // 1  fluidLevelFloodednessNoise
                    DensityFunctions.zero(), // 2  fluidLevelSpreadNoise
                    DensityFunctions.zero(), // 3  lavaNoise
                    DensityFunctions.zero(), // 4  temperature
                    DensityFunctions.zero(), // 5  vegetation
                    DensityFunctions.zero(), // 6  continents
                    DensityFunctions.zero(), // 7  erosion
                    terrainFunction,         // 8  depth
                    DensityFunctions.zero(), // 9  ridges
                    terrainFunction,         // 10 initialDensityWithoutJaggedness
                    terrainFunction,         // 11 finalDensity  <- the one that actually matters
                    DensityFunctions.zero(), // 12 veinToggle
                    DensityFunctions.zero(), // 13 veinRidged
                    DensityFunctions.zero()  // 14 veinGap
            );

            SurfaceRules.RuleSource surface = SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                            SurfaceRules.ON_FLOOR,
                            SurfaceRules.state(profile.topBlock.defaultBlockState())
                    )
            );

            NoiseGeneratorSettings settings = new NoiseGeneratorSettings(
                    NoiseSettings.create(
                            profile.minY,
                            profile.height,
                            profile.sizeHorizontal,
                            profile.sizeVertical
                    ),
                    profile.defaultBlock.defaultBlockState(),
                    profile.defaultFluid.defaultBlockState(),
                    router,
                    surface,
                    List.of(),
                    profile.seaLevel,
                    false,  // disableMobGeneration
                    true,   // aquifersEnabled
                    true,   // oreVeinsEnabled
                    true    // useLegacyRandomSource
            );

            context.register(profile.noiseSettingsKey, settings);
        });
    }
}
