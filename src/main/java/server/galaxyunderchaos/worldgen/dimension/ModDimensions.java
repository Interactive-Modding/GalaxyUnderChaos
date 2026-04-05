package server.galaxyunderchaos.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.worldgen.biome.ModBiomes;

import java.util.List;
import java.util.OptionalLong;

public class ModDimensions {

    public static final ResourceKey<LevelStem> TYTHON_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "tython"));
    public static final ResourceKey<Level> TYTHON_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "tython"));
    public static final ResourceKey<DimensionType> TYTHON_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "tython_type"));

    public static final ResourceKey<LevelStem> NABOO_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "naboo"));
    public static final ResourceKey<Level> NABOO_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "naboo"));
    public static final ResourceKey<DimensionType> NABOO_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "naboo_type"));

    public static final ResourceKey<LevelStem> ILUM_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ilum"));
    public static final ResourceKey<Level> ILUM_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ilum"));
    public static final ResourceKey<DimensionType> ILUM_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ilum_type"));

    public static final ResourceKey<LevelStem> MUSTAFAR_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "mustafar"));
    public static final ResourceKey<Level> MUSTAFAR_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "mustafar"));
    public static final ResourceKey<DimensionType> MUSTAFAR_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "mustafar_type"));

    public static final ResourceKey<LevelStem> OSSUS_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ossus"));
    public static final ResourceKey<Level> OSSUS_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ossus"));
    public static final ResourceKey<DimensionType> OSSUS_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ossus_type"));

    public static final ResourceKey<LevelStem> ASHLA_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ashla"));
    public static final ResourceKey<Level> ASHLA_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ashla"));
    public static final ResourceKey<DimensionType> ASHLA_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ashla_type"));

    public static final ResourceKey<LevelStem> BOGAN_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "bogan"));
    public static final ResourceKey<Level> BOGAN_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "bogan"));
    public static final ResourceKey<DimensionType> BOGAN_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "bogan_type"));

    public static final ResourceKey<LevelStem> MALACHOR_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "malachor"));
    public static final ResourceKey<Level> MALACHOR_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "malachor"));
    public static final ResourceKey<DimensionType> MALACHOR_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "malachor_type"));

    public static final ResourceKey<LevelStem> KORRIBAN_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "korriban"));
    public static final ResourceKey<Level> KORRIBAN_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "korriban"));
    public static final ResourceKey<DimensionType> KORRIBAN_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "korriban_type"));

    public static final ResourceKey<LevelStem> DANTOOINE_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "dantooine"));
    public static final ResourceKey<Level> DANTOOINE_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "dantooine"));
    public static final ResourceKey<DimensionType> DANTOOINE_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "dantooine_type"));

    public static final ResourceKey<NoiseGeneratorSettings> TYTHON_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "tython_noise_settings"));
    public static final ResourceKey<NoiseGeneratorSettings> NABOO_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "naboo_noise_settings"));
    public static final ResourceKey<NoiseGeneratorSettings> ILUM_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ilum_noise_settings"));
    public static final ResourceKey<NoiseGeneratorSettings> MUSTAFAR_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "mustafar_noise_settings"));
    public static final ResourceKey<NoiseGeneratorSettings> OSSUS_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ossus_noise_settings"));
    public static final ResourceKey<NoiseGeneratorSettings> ASHLA_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ashla_noise_settings"));
    public static final ResourceKey<NoiseGeneratorSettings> BOGAN_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "bogan_noise_settings"));
    public static final ResourceKey<NoiseGeneratorSettings> MALACHOR_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "malachor_noise_settings"));
    public static final ResourceKey<NoiseGeneratorSettings> KORRIBAN_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "korriban_noise_settings"));
    public static final ResourceKey<NoiseGeneratorSettings> DANTOOINE_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "dantooine_noise_settings"));

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(ASHLA_DIM_TYPE, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, new ResourceLocation("minecraft", "overworld"), 0.8f, new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
        context.register(BOGAN_DIM_TYPE, new DimensionType(OptionalLong.of(18000), false, false, false, true, 1.0, true, true, -64, 384, 256, BlockTags.INFINIBURN_NETHER, new ResourceLocation("minecraft", "the_nether"), 0.05f, new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 7)));
        context.register(DANTOOINE_DIM_TYPE, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, new ResourceLocation("minecraft", "overworld"), 0.0f, new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 7)));
        context.register(ILUM_DIM_TYPE, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, new ResourceLocation("minecraft", "overworld"), 0.0f, new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
        context.register(KORRIBAN_DIM_TYPE, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, true, -64, 384, 256, BlockTags.INFINIBURN_NETHER, new ResourceLocation("minecraft", "overworld"), 0.1f, new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 7)));
        context.register(MALACHOR_DIM_TYPE, new DimensionType(OptionalLong.of(18000), true, false, false, true, 1.0, true, true, -64, 384, 256, BlockTags.INFINIBURN_NETHER, new ResourceLocation("minecraft", "the_nether"), 0.05f, new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)));
        context.register(MUSTAFAR_DIM_TYPE, new DimensionType(OptionalLong.of(18000), true, false, true, true, 1.0, true, true, -64, 384, 256, BlockTags.INFINIBURN_NETHER, new ResourceLocation("minecraft", "the_nether"), 0.1f, new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)));
        context.register(NABOO_DIM_TYPE, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, new ResourceLocation("minecraft", "overworld"), 0.0f, new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
        context.register(OSSUS_DIM_TYPE, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, new ResourceLocation("minecraft", "overworld"), 0.0f, new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 7)));
        context.register(TYTHON_DIM_TYPE, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, true, -64, 320, 256, BlockTags.INFINIBURN_OVERWORLD, new ResourceLocation(galaxyunderchaos.MODID, "tython"), 0.0f, new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 7)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator tythonChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(0.70F, 0.80F, 0.40F, 0.10F, 0.30F, 0.00F, 0.20F), biomeRegistry.getOrThrow(ModBiomes.TYTHON_FOREST)),
                                Pair.of(Climate.parameters(0.80F, 0.60F, 0.30F, 0.00F, 0.50F, 0.10F, 0.00F), biomeRegistry.getOrThrow(ModBiomes.TYTHON_PLAINS)),
                                Pair.of(Climate.parameters(0.50F, 0.30F, 0.20F, 0.20F, 0.70F, 0.10F, 0.10F), biomeRegistry.getOrThrow(ModBiomes.TYTHON_MOUNTAINS))
                        ))
                ),
                noiseGenSettings.getOrThrow(TYTHON_NOISE)
        );
        context.register(TYTHON_KEY, new LevelStem(dimTypes.getOrThrow(TYTHON_DIM_TYPE), tythonChunkGenerator));

        NoiseBasedChunkGenerator dantooineChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(0.8F, 0.6F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.DANTOOINE_PLAINS)),
                                Pair.of(Climate.parameters(0.7F, 0.7F, 0.4F, 0.3F, 0.1F, 0.1F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.DANTOOINE_FOREST)),
                                Pair.of(Climate.parameters(0.6F, 0.5F, 0.5F, 0.3F, 0.2F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.DANTOOINE_HILLS))
                        ))
                ),
                noiseGenSettings.getOrThrow(DANTOOINE_NOISE)
        );
        context.register(DANTOOINE_KEY, new LevelStem(dimTypes.getOrThrow(DANTOOINE_DIM_TYPE), dantooineChunkGenerator));

        NoiseBasedChunkGenerator nabooChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(0.7F, 0.7F, 0.3F, 0.2F, 0.125F, 0.3F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.NABOO_BIOME)),
                                Pair.of(Climate.parameters(0.9F, 0.9F, 0.2F, 0.4F, 0.05F, 0.2F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.NABOO_SWAMP)),
                                Pair.of(Climate.parameters(0.8F, 0.4F, 0.6F, 0.3F, 0.1F, 0.1F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.NABOO_PLAINS)),
                                Pair.of(Climate.parameters(0.3F, 0.7F, 0.6F, 0.2F, -0.2F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.NABOO_OCEAN))
                        ))
                ),
                noiseGenSettings.getOrThrow(NABOO_NOISE)
        );
        context.register(NABOO_KEY, new LevelStem(dimTypes.getOrThrow(NABOO_DIM_TYPE), nabooChunkGenerator));

        NoiseBasedChunkGenerator ilumChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(-1.0F, 0.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.ILUM_BIOME)),
                                Pair.of(Climate.parameters(-0.8F, 1.0F, 0.2F, 0.1F, 0.2F, 0.1F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.ILUM_BIOME_FOREST))
                        ))
                ),
                noiseGenSettings.getOrThrow(ILUM_NOISE)
        );
        context.register(ILUM_KEY, new LevelStem(dimTypes.getOrThrow(ILUM_DIM_TYPE), ilumChunkGenerator));

        NoiseBasedChunkGenerator mustafarChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(-0.5F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.MUSTAFAR_LAVA_FIELD)),
                                Pair.of(Climate.parameters(0.0F, 0.8F, 0.2F, 0.2F, 0.2F, 0.0F, 0.1F), biomeRegistry.getOrThrow(ModBiomes.MUSTAFAR_VOLCANIC_PLAINS)),
                                Pair.of(Climate.parameters(0.2F, 0.7F, 0.3F, 0.3F, 0.3F, 0.1F, 0.2F), biomeRegistry.getOrThrow(ModBiomes.MUSTAFAR_MAGMA_LAKE))
                        ))
                ),
                noiseGenSettings.getOrThrow(MUSTAFAR_NOISE)
        );
        context.register(MUSTAFAR_KEY, new LevelStem(dimTypes.getOrThrow(MUSTAFAR_DIM_TYPE), mustafarChunkGenerator));

        NoiseBasedChunkGenerator ossusChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(0.6F, 0.7F, 0.2F, 0.3F, 0.1F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.OSSUS_FOREST)),
                                Pair.of(Climate.parameters(0.8F, 0.4F, 0.3F, 0.2F, 0.0F, 0.1F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.OSSUS_PLAINS)),
                                Pair.of(Climate.parameters(0.4F, 0.5F, 0.4F, 0.3F, 0.2F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.OSSUS_MOUNTAINS)),
                                Pair.of(Climate.parameters(0.5F, 0.6F, -0.2F, 0.2F, -0.3F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.OSSUS_OCEAN)),
                                Pair.of(Climate.parameters(0.5F, 0.7F, -0.3F, 0.1F, -0.5F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.OSSUS_DEEP_OCEAN))
                        ))
                ),
                noiseGenSettings.getOrThrow(OSSUS_NOISE)
        );
        context.register(OSSUS_KEY, new LevelStem(dimTypes.getOrThrow(OSSUS_DIM_TYPE), ossusChunkGenerator));

        NoiseBasedChunkGenerator korribanChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(1.2F, 0.0F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.KORRIBAN_DRY_CANYON)),
                                Pair.of(Climate.parameters(1.0F, -0.2F, 0.7F, 0.4F, 0.1F, 0.0F, 0.1F), biomeRegistry.getOrThrow(ModBiomes.KORRIBAN_SITH_TOMB))
                        ))
                ),
                noiseGenSettings.getOrThrow(KORRIBAN_NOISE)
        );
        context.register(KORRIBAN_KEY, new LevelStem(dimTypes.getOrThrow(KORRIBAN_DIM_TYPE), korribanChunkGenerator));

        NoiseBasedChunkGenerator malachorChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(-0.8F, 0.4F, 0.0F, 0.0F, 0.5F, -0.3F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.MALACHOR_UPPER_LAYER)),
                                Pair.of(Climate.parameters(0.0F, 0.2F, 0.3F, 0.1F, 0.1F, 0.0F, 0.1F), biomeRegistry.getOrThrow(ModBiomes.MALACHOR_LOWER_SURFACE))
                        ))
                ),
                noiseGenSettings.getOrThrow(MALACHOR_NOISE)
        );
        context.register(MALACHOR_KEY, new LevelStem(dimTypes.getOrThrow(MALACHOR_DIM_TYPE), malachorChunkGenerator));

        NoiseBasedChunkGenerator ashlaChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(
                                        Climate.parameters(
                                                -1.0F, 1.0F,
                                                -1.0F, 1.0F,
                                                -1.0F, 1.0F,
                                                0.0F
                                        ),
                                        biomeRegistry.getOrThrow(ModBiomes.ASHLA_BIOME)
                                )
                        ))
                ),
                noiseGenSettings.getOrThrow(ASHLA_NOISE)
        );

        context.register(ASHLA_KEY, new LevelStem(dimTypes.getOrThrow(ASHLA_DIM_TYPE), ashlaChunkGenerator
                )
        );


        NoiseBasedChunkGenerator boganChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(
                                        Climate.parameters(
                                                -1.0F, 1.0F,
                                                -1.0F, 1.0F,
                                                -1.0F, 1.0F,
                                                0.0F
                                        ),
                                        biomeRegistry.getOrThrow(ModBiomes.BOGAN_BIOME)
                                )
                        ))
                ),
                noiseGenSettings.getOrThrow(BOGAN_NOISE)
        );

        context.register(BOGAN_KEY, new LevelStem(dimTypes.getOrThrow(BOGAN_DIM_TYPE), boganChunkGenerator
                )
        );
    }
}