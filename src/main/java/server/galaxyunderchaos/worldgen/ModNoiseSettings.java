package server.galaxyunderchaos.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.worldgen.dimension.ModDimensions;
import server.galaxyunderchaos.worldgen.biome.ModBiomes;

import java.util.List;
import java.util.Map;

public class ModNoiseSettings {

    private record PlanetNoiseProfile(
            ResourceKey<NoiseGeneratorSettings> noiseSettingsKey,
            ResourceKey<DensityFunction> terrainDensity,
            Block topBlock,
            Block underBlock,
            Block defaultBlock,
            Block defaultFluid,
            int seaLevel,
            int minY,
            int height,
            int sizeHorizontal,
            int sizeVertical,
            boolean aquifersEnabled
    ) {}

    private static final Map<String, PlanetNoiseProfile> PLANETS = Map.ofEntries(

            // Sea level lowered to 32 so plains/forests/mountains stay dry.
            // Tython has tall cliffs and high land — water only fills deep valleys.
            Map.entry("tython",
                    new PlanetNoiseProfile(
                            ModDimensions.TYTHON_NOISE,
                            ModDensityFunctions.TYTHON_TERRAIN,
                            Blocks.GRASS_BLOCK,
                            Blocks.DIRT,
                            Blocks.STONE,
                            Blocks.WATER,
                            44, -64, 384, 1, 2, true
                    )),

            // Naboo keeps true warm oceans at a normal waterline.
            // Terrain shaping below raises plains/forests above this level while
            // negative continentalness carves actual ocean basins below it.
            Map.entry("naboo",
                    new PlanetNoiseProfile(
                            ModDimensions.NABOO_NOISE,
                            ModDensityFunctions.NABOO_TERRAIN,
                            Blocks.GRASS_BLOCK,
                            Blocks.DIRT,
                            Blocks.STONE,
                            Blocks.WATER,
                            63, -64, 384, 1, 2, false
                    )),

            Map.entry("ilum",
                    new PlanetNoiseProfile(
                            ModDimensions.ILUM_NOISE,
                            ModDensityFunctions.ILUM_TERRAIN,
                            Blocks.SNOW_BLOCK,
                            Blocks.ICE,
                            Blocks.STONE,
                            Blocks.ICE,
                            -50, -64, 384, 1, 2, true
                    )),
            Map.entry("hoth",
                    new PlanetNoiseProfile(
                            ModDimensions.HOTH_NOISE,
                            ModDensityFunctions.HOTH_TERRAIN,
                            Blocks.SNOW_BLOCK,
                            Blocks.SNOW_BLOCK,
                            Blocks.STONE,
                            Blocks.ICE,
                            63, -64, 384, 1, 2, true
                    )),

            Map.entry("mustafar",
                    new PlanetNoiseProfile(
                            ModDimensions.MUSTAFAR_NOISE,
                            ModDensityFunctions.MUSTAFAR_TERRAIN,
                            Blocks.BASALT,
                            Blocks.SMOOTH_BASALT,
                            Blocks.SMOOTH_BASALT,
                            Blocks.LAVA,
                            32, -64, 384, 1, 2, true
                    )),

            Map.entry("ossus",
                    new PlanetNoiseProfile(
                            ModDimensions.OSSUS_NOISE,
                            ModDensityFunctions.OSSUS_TERRAIN,
                            Blocks.GRASS_BLOCK,
                            Blocks.DIRT,
                            Blocks.STONE,
                            Blocks.WATER,
                            63, -64, 384, 1, 2, false
                    )),

            // Ashla is a gentle meadow moon — sea level dropped to 20 so
            // the low-amplitude terrain is never drowned by water.
            Map.entry("ashla",
                    new PlanetNoiseProfile(
                            ModDimensions.ASHLA_NOISE,
                            ModDensityFunctions.ASHLA_TERRAIN,
                            Blocks.GRASS_BLOCK,
                            Blocks.DIRT,
                            Blocks.CALCITE,
                            Blocks.AIR,
                            -64, -64, 384, 1, 2, false
                    )),

            // Bogan is a dark moon — sea level dropped to 32 so chaotic
            // terrain pits don't flood the surface biome.
            Map.entry("bogan",
                    new PlanetNoiseProfile(
                            ModDimensions.BOGAN_NOISE,
                            ModDensityFunctions.BOGAN_TERRAIN,
                            Blocks.MYCELIUM,
                            Blocks.MYCELIUM,
                            Blocks.DEEPSLATE,
                            Blocks.AIR,
                            -64, -64, 384, 1, 2, false
                    )),

            Map.entry("malachor",
                    new PlanetNoiseProfile(
                            ModDimensions.MALACHOR_NOISE,
                            ModDensityFunctions.MALACHOR_TERRAIN,
                            galaxyunderchaos.MALACHITE_OBSIDIAN.get(),
                            galaxyunderchaos.MALACHITE_OBSIDIAN.get(),
                            Blocks.ANDESITE,
                            Blocks.AIR,
                            40, -64, 384, 1, 2, false
                    )),

            Map.entry("korriban",
                    new PlanetNoiseProfile(
                            ModDimensions.KORRIBAN_NOISE,
                            ModDensityFunctions.KORRIBAN_TERRAIN,
                            Blocks.RED_SAND,
                            Blocks.RED_SAND,
                            Blocks.RED_SANDSTONE,
                            Blocks.AIR,
                            28, -64, 384, 1, 2, false
                    )),

            // Dantooine is dry rolling grassland. Disable the global fluid fill
            // entirely so the low-amplitude plains never spawn underwater.
            Map.entry("dantooine",
                    new PlanetNoiseProfile(
                            ModDimensions.DANTOOINE_NOISE,
                            ModDensityFunctions.DANTOOINE_TERRAIN,
                            Blocks.GRASS_BLOCK,
                            Blocks.DIRT,
                            Blocks.STONE,
                            Blocks.AIR,
                            -64, -64, 384, 1, 2, false
                    ))
    );

    public static void bootstrap(BootstapContext<NoiseGeneratorSettings> context) {

        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        PLANETS.values().forEach(profile -> {

            Holder<DensityFunction> terrain = densityFunctions.getOrThrow(profile.terrainDensity);

            // Wrap the registered density function so it can be used inside the router.
            DensityFunction terrainFunction = new DensityFunctions.HolderHolder(terrain);

            boolean isNaboo = profile.noiseSettingsKey.equals(ModDimensions.NABOO_NOISE);
            DensityFunction zero = DensityFunctions.zero();

            // Naboo used to feed 0.0 into every climate router channel. Multi-noise biome
            // placement then had almost nothing to sample, so ocean/swamp/plains/forest
            // points could collapse into the wrong places. Give Naboo actual broad climate
            // fields so low continentalness becomes ocean/coast, wet lowland becomes swamp,
            // and drier positive continentalness becomes plains/forest.
            DensityFunction temperature = zero;
            DensityFunction vegetation = zero;
            DensityFunction continents = zero;
            DensityFunction erosion = zero;
            DensityFunction ridges = zero;
            DensityFunction climateDepth = terrainFunction;
            DensityFunction finalTerrain = terrainFunction;

            if (isNaboo) {
                Holder<NormalNoise.NoiseParameters> nabooNoise = noises.getOrThrow(ModNoises.NABOO_TERRAIN);

                DensityFunction broadClimate = DensityFunctions.noise(nabooNoise, 0.18, 0.0);
                DensityFunction wetnessDetail = DensityFunctions.noise(nabooNoise, 0.42, 0.0);

                temperature = DensityFunctions.add(
                        DensityFunctions.constant(0.72D),
                        DensityFunctions.mul(broadClimate, DensityFunctions.constant(0.18D))
                );
                vegetation = DensityFunctions.add(
                        DensityFunctions.constant(0.65D),
                        DensityFunctions.mul(wetnessDetail, DensityFunctions.constant(0.35D))
                );
                continents = DensityFunctions.mul(broadClimate, DensityFunctions.constant(0.85D));
                erosion = DensityFunctions.mul(wetnessDetail, DensityFunctions.constant(0.55D));
                ridges = DensityFunctions.mul(broadClimate, DensityFunctions.constant(0.35D));

                // Keep climate depth stable for biome picking; finalDensity below still shapes terrain.
                climateDepth = zero;

                // Tie terrain height to the same broad continentalness field used for biome selection.
                // Important: Naboo must keep a real waterline for ocean biomes. The base terrain
                // now sits above sea level, then negative continentalness cuts ocean basins back
                // below sea level while positive continentalness keeps plains/forests dry.
                finalTerrain = DensityFunctions.add(
                        terrainFunction,
                        DensityFunctions.mul(continents, DensityFunctions.constant(0.85D))
                );
            }

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
                    zero,            // 0  barrierNoise
                    zero,            // 1  fluidLevelFloodednessNoise
                    zero,            // 2  fluidLevelSpreadNoise
                    zero,            // 3  lavaNoise
                    temperature,     // 4  temperature
                    vegetation,      // 5  vegetation / humidity
                    continents,      // 6  continents / land-ocean split
                    erosion,         // 7  erosion
                    climateDepth,    // 8  depth
                    ridges,          // 9  ridges / weirdness
                    finalTerrain,    // 10 initialDensityWithoutJaggedness
                    finalTerrain,    // 11 finalDensity  <- the one that actually matters
                    zero,            // 12 veinToggle
                    zero,            // 13 veinRidged
                    zero             // 14 veinGap
            );

            SurfaceRules.RuleSource surface;
            if (profile.noiseSettingsKey.equals(ModDimensions.TYTHON_NOISE)) {
                surface = SurfaceRules.sequence(
                        SurfaceRules.ifTrue(
                                SurfaceRules.ON_FLOOR,
                                SurfaceRules.ifTrue(
                                        SurfaceRules.waterBlockCheck(-1, 0),
                                        SurfaceRules.state(profile.topBlock.defaultBlockState())
                                )
                        ),
                        SurfaceRules.ifTrue(
                                SurfaceRules.ON_FLOOR,
                                SurfaceRules.state(Blocks.SAND.defaultBlockState())
                        ),
                        SurfaceRules.ifTrue(
                                SurfaceRules.UNDER_FLOOR,
                                SurfaceRules.state(profile.underBlock.defaultBlockState())
                        )
                );
            } else if (isNaboo) {
                surface = SurfaceRules.sequence(
                        // Ocean biome needs an actual ocean floor, not grass under water.
                        SurfaceRules.ifTrue(
                                SurfaceRules.isBiome(ModBiomes.NABOO_OCEAN),
                                SurfaceRules.ifTrue(
                                        SurfaceRules.ON_FLOOR,
                                        SurfaceRules.state(Blocks.SAND.defaultBlockState())
                                )
                        ),
                        // Swamps should stay soft/muddy without turning the whole planet into water.
                        SurfaceRules.ifTrue(
                                SurfaceRules.isBiome(ModBiomes.NABOO_SWAMP),
                                SurfaceRules.ifTrue(
                                        SurfaceRules.ON_FLOOR,
                                        SurfaceRules.state(Blocks.MUD.defaultBlockState())
                                )
                        ),
                        SurfaceRules.ifTrue(
                                SurfaceRules.ON_FLOOR,
                                SurfaceRules.state(profile.topBlock.defaultBlockState())
                        ),
                        SurfaceRules.ifTrue(
                                SurfaceRules.UNDER_FLOOR,
                                SurfaceRules.state(profile.underBlock.defaultBlockState())
                        )
                );
            } else {
                surface = SurfaceRules.sequence(
                        SurfaceRules.ifTrue(
                                SurfaceRules.ON_FLOOR,
                                SurfaceRules.state(profile.topBlock.defaultBlockState())
                        ),
                        SurfaceRules.ifTrue(
                                SurfaceRules.UNDER_FLOOR,
                                SurfaceRules.state(profile.underBlock.defaultBlockState())
                        )
                );
            }

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
                    profile.aquifersEnabled, // aquifersEnabled
                    true,   // oreVeinsEnabled
                    true    // useLegacyRandomSource
            );

            context.register(profile.noiseSettingsKey, settings);
        });
    }
}
