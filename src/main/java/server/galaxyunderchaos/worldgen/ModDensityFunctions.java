package server.galaxyunderchaos.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Map;

public class ModDensityFunctions {

    public static final ResourceKey<DensityFunction> TYTHON_TERRAIN    = densityKey("tython_terrain");
    public static final ResourceKey<DensityFunction> NABOO_TERRAIN      = densityKey("naboo_terrain");
    public static final ResourceKey<DensityFunction> ILUM_TERRAIN       = densityKey("ilum_terrain");
    public static final ResourceKey<DensityFunction> MUSTAFAR_TERRAIN   = densityKey("mustafar_terrain");
    public static final ResourceKey<DensityFunction> OSSUS_TERRAIN      = densityKey("ossus_terrain");
    public static final ResourceKey<DensityFunction> ASHLA_TERRAIN      = densityKey("ashla_terrain");
    public static final ResourceKey<DensityFunction> BOGAN_TERRAIN      = densityKey("bogan_terrain");
    public static final ResourceKey<DensityFunction> MALACHOR_TERRAIN   = densityKey("malachor_terrain");
    public static final ResourceKey<DensityFunction> KORRIBAN_TERRAIN   = densityKey("korriban_terrain");
    public static final ResourceKey<DensityFunction> DANTOOINE_TERRAIN  = densityKey("dantooine_terrain");

    /**
     * Per-planet terrain profile.
     *
     * @param noise           The noise parameters to sample from
     * @param xzScale         Horizontal frequency of the base noise (higher = more frequent hills)
     * @param yScale          Vertical frequency of the base noise (higher = thinner layers)
     * @param noiseAmplitude  How strongly the noise modifies terrain (larger = wilder)
     * @param hillXZScale     Horizontal frequency of the secondary "hills" noise pass
     * @param hillAmplitude   Amplitude of the secondary hills pass
     * @param gradientMinY    Bottom of the Y gradient (solid rock below this)
     * @param gradientMaxY    Top of the Y gradient (open air above this)
     * @param gradientStrength How steep the gradient is (higher = sharper surface cutoff)
     */
    private record PlanetDensity(
            ResourceKey<NormalNoise.NoiseParameters> noise,
            double xzScale,
            double yScale,
            double noiseAmplitude,
            double hillXZScale,
            double hillAmplitude,
            int    gradientMinY,
            int    gradientMaxY,
            double gradientStrength
    ) {}

    private static final Map<ResourceKey<DensityFunction>, PlanetDensity> PLANETS = Map.ofEntries(

            // TYTHON — lush temperate world, high land with dramatic cliff faces
            // Raised amplitude and ceiling so land sits well above sea level.
            // Softer gradient allows cliff overhangs and steep drops.
            Map.entry(TYTHON_TERRAIN, new PlanetDensity(
                    ModNoises.TYTHON_TERRAIN,
                    0.65, 0.40,  // slightly higher y-scale for cliff definition
                    0.55,        // much higher amplitude = tall land above sea level
                    0.25, 0.35,  // stronger secondary pass for cliff faces
                    -64, 130,    // higher ceiling pushes land up well above water
                    1.6          // softer gradient = allows cliffs and overhangs
            )),

            // NABOO — flat plains and shallow seas, very gentle terrain
            Map.entry(NABOO_TERRAIN, new PlanetDensity(
                    ModNoises.NABOO_TERRAIN,
                    0.4, 0.2,   // low frequency = wide flat plains
                    0.20,       // low amplitude = flat
                    0.10, 0.10, // very subtle hills
                    -64, 80,    // low surface ceiling keeps terrain flat
                    3.0         // steeper gradient = flatter plains
            )),

            // ILUM — icy jagged mountains, dramatic soaring peaks
            // Much higher ceiling and softer gradient so mountains can truly tower.
            Map.entry(ILUM_TERRAIN, new PlanetDensity(
                    ModNoises.ILUM_TERRAIN,
                    0.9, 0.6,   // higher xz frequency = sharper jagged peaks
                    0.75,       // higher amplitude = much taller mountains
                    0.40, 0.50, // stronger secondary layer for dramatic ridges
                    -64, 220,   // much higher ceiling for soaring peaks
                    1.2         // very soft gradient = mountains can truly soar
            )),

            // MUSTAFAR — volcanic, sharp chaotic terrain, tall jagged spires
            Map.entry(MUSTAFAR_TERRAIN, new PlanetDensity(
                    ModNoises.MUSTAFAR_TERRAIN,
                    1.0, 0.6,   // very high frequency = chaotic spires
                    0.65,       // very high amplitude = violent terrain
                    0.40, 0.40, // strong secondary layer for lava channels
                    -64, 120,   // medium ceiling, terrain is dense not tall
                    1.4         // soft gradient allows overhangs/spires
            )),

            // OSSUS — ancient overgrown world, jungle highlands, deep valleys
            Map.entry(OSSUS_TERRAIN, new PlanetDensity(
                    ModNoises.OSSUS_TERRAIN,
                    0.5, 0.35,
                    0.40,
                    0.20, 0.25,
                    -64, 110,
                    1.8
            )),

            // ASHLA — light side moon, rolling flower meadows, soft bright terrain
            Map.entry(ASHLA_TERRAIN, new PlanetDensity(
                    ModNoises.ASHLA_TERRAIN,
                    0.35, 0.20, // very low freq = wide gentle meadows
                    0.22,
                    0.12, 0.12,
                    -64, 85,
                    2.8         // steep gradient = stays flat and pastoral
            )),

            // BOGAN — dark side moon, twisted uneven terrain, deep pits
            Map.entry(BOGAN_TERRAIN, new PlanetDensity(
                    ModNoises.BOGAN_TERRAIN,
                    0.75, 0.50, // high freq = chaotic pitted surface
                    0.50,
                    0.35, 0.30,
                    -64, 95,
                    1.6         // softer gradient = deep chasms possible
            )),

            // MALACHOR — shattered planet, extreme terrain with massive craters
            Map.entry(MALACHOR_TERRAIN, new PlanetDensity(
                    ModNoises.MALACHOR_TERRAIN,
                    0.90, 0.55,
                    0.60,
                    0.45, 0.35,
                    -64, 130,
                    1.3         // very soft gradient = dramatic overhangs
            )),

            // KORRIBAN — arid desert canyons with tall mountain ridges
            // Higher amplitude and ceiling for imposing canyon walls and peaks.
            Map.entry(KORRIBAN_TERRAIN, new PlanetDensity(
                    ModNoises.KORRIBAN_TERRAIN,
                    0.65, 0.35,  // slightly higher xz for sharper canyon ridges
                    0.65,        // much higher amplitude = tall canyon walls
                    0.35, 0.30,  // stronger secondary for layered ridge detail
                    -64, 140,    // higher ceiling = room for mountain peaks
                    1.6          // softer gradient = canyon walls can be steep
            )),

            // DANTOOINE — peaceful grassy plains with gentle hills
            Map.entry(DANTOOINE_TERRAIN, new PlanetDensity(
                    ModNoises.DANTOOINE_TERRAIN,
                    0.40, 0.22,
                    0.22,
                    0.12, 0.12,
                    -64, 85,
                    2.6
            ))
    );

    public static void bootstrap(BootstapContext<DensityFunction> context) {

        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        PLANETS.forEach((key, planet) -> {

            Holder<NormalNoise.NoiseParameters> noise = noises.getOrThrow(planet.noise);

            // Primary noise layer — broad terrain shape
            DensityFunction baseNoise = DensityFunctions.noise(noise, planet.xzScale, planet.yScale);
            DensityFunction baseTerrain = DensityFunctions.mul(
                    baseNoise,
                    DensityFunctions.constant(planet.noiseAmplitude)
            );

            // Secondary noise layer — hills and detail on top of base
            DensityFunction hillNoise = DensityFunctions.noise(noise, planet.hillXZScale, planet.hillXZScale * 0.5);
            DensityFunction hills = DensityFunctions.mul(
                    hillNoise,
                    DensityFunctions.constant(planet.hillAmplitude)
            );

            // Y gradient: positive (solid) deep underground, negative (air) high up.
            // This is what actually gives the world a surface.
            DensityFunction yGradient = DensityFunctions.yClampedGradient(
                    planet.gradientMinY,
                    planet.gradientMaxY,
                    planet.gradientStrength,   // value at gradientMinY  (strongly solid)
                    -planet.gradientStrength   // value at gradientMaxY  (strongly air)
            );

            // Combine: gradient provides the solid/air split, noise carves detail into it
            DensityFunction terrain = DensityFunctions.add(
                    yGradient,
                    DensityFunctions.add(baseTerrain, hills)
            );

            context.register(key, terrain);
        });
    }

    private static ResourceKey<DensityFunction> densityKey(String path) {
        return ResourceKey.create(
                Registries.DENSITY_FUNCTION,
                ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, path)
        );
    }
}