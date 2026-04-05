package server.galaxyunderchaos.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Map;

public class ModNoises {

    public static final ResourceKey<NormalNoise.NoiseParameters> TYTHON_TERRAIN = noiseKey("tython_terrain");
    public static final ResourceKey<NormalNoise.NoiseParameters> NABOO_TERRAIN = noiseKey("naboo_terrain");
    public static final ResourceKey<NormalNoise.NoiseParameters> ILUM_TERRAIN = noiseKey("ilum_terrain");
    public static final ResourceKey<NormalNoise.NoiseParameters> MUSTAFAR_TERRAIN = noiseKey("mustafar_terrain");
    public static final ResourceKey<NormalNoise.NoiseParameters> OSSUS_TERRAIN = noiseKey("ossus_terrain");
    public static final ResourceKey<NormalNoise.NoiseParameters> ASHLA_TERRAIN = noiseKey("ashla_terrain");
    public static final ResourceKey<NormalNoise.NoiseParameters> BOGAN_TERRAIN = noiseKey("bogan_terrain");
    public static final ResourceKey<NormalNoise.NoiseParameters> MALACHOR_TERRAIN = noiseKey("malachor_terrain");
    public static final ResourceKey<NormalNoise.NoiseParameters> KORRIBAN_TERRAIN = noiseKey("korriban_terrain");
    public static final ResourceKey<NormalNoise.NoiseParameters> DANTOOINE_TERRAIN = noiseKey("dantooine_terrain");

    private static final Map<ResourceKey<NormalNoise.NoiseParameters>, NormalNoise.NoiseParameters> PLANET_NOISES = Map.ofEntries(
            Map.entry(TYTHON_TERRAIN, new NormalNoise.NoiseParameters(-7, 1.0, 1.0, 0.6, 0.3)),
            Map.entry(NABOO_TERRAIN, new NormalNoise.NoiseParameters(-6, 1.0, 0.8, 0.4, 0.2)),
            Map.entry(ILUM_TERRAIN, new NormalNoise.NoiseParameters(-9, 1.2, 1.0, 0.7, 0.3)),
            Map.entry(MUSTAFAR_TERRAIN, new NormalNoise.NoiseParameters(-5, 1.4, 1.1, 0.9, 0.5)),
            Map.entry(OSSUS_TERRAIN, new NormalNoise.NoiseParameters(-7, 1.0, 0.9, 0.5, 0.2)),
            Map.entry(ASHLA_TERRAIN, new NormalNoise.NoiseParameters(-8, 0.8, 0.7, 0.4, 0.2)),
            Map.entry(BOGAN_TERRAIN, new NormalNoise.NoiseParameters(-8, 1.1, 0.9, 0.8, 0.4)),
            Map.entry(MALACHOR_TERRAIN, new NormalNoise.NoiseParameters(-6, 1.3, 1.1, 0.8, 0.4)),
            Map.entry(KORRIBAN_TERRAIN, new NormalNoise.NoiseParameters(-6, 1.2, 1.0, 0.7, 0.3)),
            Map.entry(DANTOOINE_TERRAIN, new NormalNoise.NoiseParameters(-7, 0.9, 0.8, 0.4, 0.2))
    );

    public static void bootstrap(BootstapContext<NormalNoise.NoiseParameters> context) {
        PLANET_NOISES.forEach(context::register);
    }

    private static ResourceKey<NormalNoise.NoiseParameters> noiseKey(String path) {
        return ResourceKey.create(Registries.NOISE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, path));
    }
}
