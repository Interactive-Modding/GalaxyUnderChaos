package server.galaxyunderchaos.worldgen.biome;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.worldgen.ModPlacedFeatures;

public class ModBiomes {
    public static final ResourceKey<Biome> NABOO_BIOME = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "naboo_biome"));
    public static final ResourceKey<Biome> ILUM_BIOME = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ilum_biome"));
    public static final ResourceKey<Biome> ILUM_BIOME_FOREST = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ilum_biome_forest"));
    public static final ResourceKey<Biome> MUSTAFAR_LAVA_FIELD = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "mustafar_lava_field"));
    public static final ResourceKey<Biome> MUSTAFAR_VOLCANIC_PLAINS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "mustafar_volcanic_plains"));
    public static final ResourceKey<Biome> MUSTAFAR_MAGMA_LAKE = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "mustafar_magma_lake"));
    public static final ResourceKey<Biome> OSSUS_FOREST = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ossus_forest"));
    public static final ResourceKey<Biome> OSSUS_PLAINS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ossus_plains"));
    public static final ResourceKey<Biome> OSSUS_MOUNTAINS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ossus_mountains"));
    public static final ResourceKey<Biome> OSSUS_OCEAN = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ossus_ocean"));
    public static final ResourceKey<Biome> OSSUS_DEEP_OCEAN = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ossus_deep_ocean"));
    public static final ResourceKey<Biome> MALACHOR_UPPER_LAYER = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "malachor_upper_layer"));
    public static final ResourceKey<Biome> MALACHOR_LOWER_SURFACE = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "malachor_lower_surface"));
    public static final ResourceKey<Biome> NABOO_SWAMP = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "naboo_swamp"));
    public static final ResourceKey<Biome> NABOO_PLAINS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "naboo_plains"));
    public static final ResourceKey<Biome> NABOO_OCEAN = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "naboo_ocean"));
    public static final ResourceKey<Biome> KORRIBAN_DRY_CANYON = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "korriban_dry_canyon"));
    public static final ResourceKey<Biome> KORRIBAN_SITH_TOMB = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "korriban_sith_tomb"));
    public static final ResourceKey<Biome> TYTHON_FOREST = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "tython_forest"));
    public static final ResourceKey<Biome> TYTHON_PLAINS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "tython_plains"));
    public static final ResourceKey<Biome> TYTHON_MOUNTAINS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "tython_mountains"));
    public static final ResourceKey<Biome> DANTOOINE_PLAINS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "dantooine_plains"));
    public static final ResourceKey<Biome> DANTOOINE_FOREST = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "dantooine_forest"));
    public static final ResourceKey<Biome> DANTOOINE_HILLS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "dantooine_hills"));
    public static final ResourceKey<Biome> ASHLA_BIOME = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "ashla_biome"));
    public static final ResourceKey<Biome> BOGAN_BIOME = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, "bogan_biome"));

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, "galaxyunderchaos");

    public static void bootstrap(BootstapContext<Biome> context) {
        context.register(NABOO_BIOME, createNabooBiome(context));
        context.register(ILUM_BIOME, createIlumBiome(context));
        context.register(ILUM_BIOME_FOREST, createIlumBiomeForest(context));
        context.register(MUSTAFAR_LAVA_FIELD, createMustafarLavaField(context));
        context.register(MUSTAFAR_VOLCANIC_PLAINS, createMustafarVolcanicPlains(context));
        context.register(MUSTAFAR_MAGMA_LAKE, createMustafarMagmaLake(context));
        context.register(OSSUS_FOREST, createOssusForest(context));
        context.register(OSSUS_PLAINS, createOssusPlains(context));
        context.register(OSSUS_MOUNTAINS, createOssusMountains(context));
        context.register(OSSUS_OCEAN, createOssusOcean(context));
        context.register(OSSUS_DEEP_OCEAN, createOssusDeepOcean(context));
        context.register(MALACHOR_UPPER_LAYER, createMalachorUpperLayer(context));
        context.register(MALACHOR_LOWER_SURFACE, createMalachorLowerSurface(context));
        context.register(NABOO_SWAMP, createNabooSwamp(context));
        context.register(NABOO_PLAINS, createNabooPlains(context));
        context.register(NABOO_OCEAN, createNabooOcean(context));
        context.register(KORRIBAN_DRY_CANYON, createKorribanDryCanyon(context));
        context.register(KORRIBAN_SITH_TOMB, createKorribanSithTomb(context));
        context.register(TYTHON_FOREST, createTythonForest(context));
        context.register(TYTHON_PLAINS, createTythonPlains(context));
        context.register(TYTHON_MOUNTAINS, createTythonMountains(context));
        context.register(DANTOOINE_PLAINS, createDantooinePlains(context));
        context.register(DANTOOINE_FOREST, createDantooineForest(context));
        context.register(DANTOOINE_HILLS, createDantooineHills(context));
        context.register(BOGAN_BIOME, createBoganBiome(context));
        context.register(ASHLA_BIOME, createAshlaBiome(context));


    }
    private static Biome createDantooinePlains(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        // CARVERS
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        // LAKES
        gen.addFeature(GenerationStep.Decoration.LAKES,
                placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        gen.addFeature(GenerationStep.Decoration.LAKES,
                placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        // GEODES
        gen.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS,
                placed.getOrThrow(CavePlacements.AMETHYST_GEODE));

        // DUNGEONS
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        // ORES
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIRT));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GRAVEL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_TUFF));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COAL_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_SMALL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GOLD_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_REDSTONE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_LAPIS));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(CavePlacements.UNDERWATER_MAGMA));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        // SPRINGS
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS,
                placed.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS,
                placed.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));

        // VEGETATION
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(CavePlacements.GLOW_LICHEN));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_TALL_GRASS_2));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.TREES_PLAINS));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.FLOWER_PLAINS));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_GRASS_PLAIN));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_NORMAL));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.RED_MUSHROOM_NORMAL));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));

        // FREEZE
        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                placed.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.8f)
                .downfall(0.5f)
                .mobSpawnSettings(spawns.build())
                .generationSettings(gen.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(8027785)
                        .waterColor(6579816)
                        .waterFogColor(6579896)
                        .fogColor(7842047)
                        .grassColorOverride(8352038)
                        .foliageColorOverride(8414762)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createAshlaBiome(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatureHolder = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carverHolder = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatureHolder, carverHolder);

        generation.addFeature(GenerationStep.Decoration.LAKES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));

        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placedFeatureHolder.getOrThrow(CavePlacements.AMETHYST_GEODE));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIRT));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRAVEL));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_TUFF));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COAL_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_SMALL));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_REDSTONE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_LAPIS));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(CavePlacements.UNDERWATER_MAGMA));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(CavePlacements.GLOW_LICHEN));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.FLOWER_FOREST_FLOWERS));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.TREES_FLOWER_FOREST));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.FLOWER_FLOWER_FOREST));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_GRASS_BADLANDS));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_NORMAL));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.RED_MUSHROOM_NORMAL));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));

        generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.9f)
                .downfall(0.4f)
                .generationSettings(generation.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(15132351)
                        .skyColor(11187199)
                        .waterColor(8947967)
                        .waterFogColor(8947967)
                        .grassColorOverride(7857322)
                        .foliageColorOverride(7857322)
                        .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP, 6000, 8, 2.0))
                        .build())
                .build();
    }
    private static Biome createBoganBiome(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatureHolder = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carverHolder = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatureHolder, carverHolder);

        generation.addFeature(GenerationStep.Decoration.LAKES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));

        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placedFeatureHolder.getOrThrow(CavePlacements.AMETHYST_GEODE));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIRT));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRAVEL));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_TUFF));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COAL_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_SMALL));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_REDSTONE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_LAPIS));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(CavePlacements.UNDERWATER_MAGMA));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(CavePlacements.GLOW_LICHEN));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_OLD_GROWTH));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.MUSHROOM_ISLAND_VEGETATION));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.RED_MUSHROOM_OLD_GROWTH));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.FLOWER_DEFAULT));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_GRASS_BADLANDS));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_NORMAL));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.RED_MUSHROOM_NORMAL));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));

        generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.4f)
                .downfall(0.0f)
                .generationSettings(generation.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(1312000)
                        .skyColor(65535)
                        .waterColor(3342591)
                        .waterFogColor(1973790)
                        .grassColorOverride(9127187)
                        .foliageColorOverride(9127187)
                        .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_NETHER_WASTES_LOOP, 6000, 8, 2.0))
                        .build())
                .build();
    }

    private static Biome createDantooineForest(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        // CARVERS
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        // LAKES
        gen.addFeature(GenerationStep.Decoration.LAKES,
                placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        gen.addFeature(GenerationStep.Decoration.LAKES,
                placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        // GEODES
        gen.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS,
                placed.getOrThrow(CavePlacements.AMETHYST_GEODE));

        // DUNGEONS
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        // ORES
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIRT));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GRAVEL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_TUFF));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COAL_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_SMALL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GOLD_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_REDSTONE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_LAPIS));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(CavePlacements.UNDERWATER_MAGMA));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        // SPRINGS
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS,
                placed.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS,
                placed.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));

        // VEGETATION
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(CavePlacements.GLOW_LICHEN));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.FLOWER_FOREST_FLOWERS));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.TREES_FLOWER_FOREST));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.FLOWER_FLOWER_FOREST));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_GRASS_BADLANDS));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_NORMAL));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.RED_MUSHROOM_NORMAL));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));

        // FREEZE
        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                placed.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.7f)
                .downfall(0.8f)
                .mobSpawnSettings(spawns.build())
                .generationSettings(gen.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(8027785)
                        .waterColor(6579816)
                        .waterFogColor(6579896)
                        .fogColor(7842047)
                        .grassColorOverride(8352038)
                        .foliageColorOverride(8414762)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(new Music(
                                SoundEvents.MUSIC_BIOME_FLOWER_FOREST,
                                12000,
                                24000,
                                false))
                        .build())
                .build();
    }

    private static Biome createDantooineHills(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        // CARVERS
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        // LAKES
        gen.addFeature(GenerationStep.Decoration.LAKES,
                placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        gen.addFeature(GenerationStep.Decoration.LAKES,
                placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        // GEODES
        gen.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS,
                placed.getOrThrow(CavePlacements.AMETHYST_GEODE));

        // DUNGEONS
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        // ORES
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIRT));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GRAVEL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_TUFF));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COAL_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_SMALL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GOLD_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_REDSTONE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_LAPIS));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(CavePlacements.UNDERWATER_MAGMA));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_EMERALD));

        // INFESTED (mountain stone)
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION,
                placed.getOrThrow(OrePlacements.ORE_INFESTED));

        // SPRINGS
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS,
                placed.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS,
                placed.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));

        // VEGETATION
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(CavePlacements.GLOW_LICHEN));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.TREES_WINDSWEPT_HILLS));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.FLOWER_DEFAULT));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_GRASS_BADLANDS));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_NORMAL));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.RED_MUSHROOM_NORMAL));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));

        // FREEZE
        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                placed.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.6f)
                .downfall(0.6f)
                .mobSpawnSettings(spawns.build())
                .generationSettings(gen.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(8027785)
                        .waterColor(6579816)
                        .waterFogColor(6579896)
                        .fogColor(7842047)
                        .grassColorOverride(8352038)
                        .foliageColorOverride(8414762)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createTythonForest(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(features, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        gen.addFeature(GenerationStep.Decoration.LAKES, features.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        gen.addFeature(GenerationStep.Decoration.LAKES, features.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, features.getOrThrow(CavePlacements.MONSTER_ROOM));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, features.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_DIRT));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GRAVEL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_TUFF));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_COAL_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_IRON_SMALL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GOLD_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_REDSTONE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_DIAMOND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_LAPIS));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_COPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.BLUE_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.ORANGE_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.GREEN_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.YELLOW_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.CYAN_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.WHITE_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.MAGENTA_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.PURPLE_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.PINK_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.LIME_GREEN_CRYSTAL_ORE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(ModPlacedFeatures.TURQUOISE_CRYSTAL_ORE_PLACED_KEY));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_INFESTED));

        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, features.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, features.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(ModPlacedFeatures.AK_PINE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(ModPlacedFeatures.HEART_BERRY_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(ModPlacedFeatures.PATCH_PINK_MUSHROOM_PLACED_KEY));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(CavePlacements.GLOW_LICHEN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_LARGE_FERN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.FLOWER_DEFAULT));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_GRASS_TAIGA_2));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_DEAD_BUSH));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_BERRY_COMMON));

        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, features.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        return new Biome.BiomeBuilder()
                .temperature(0.7F)
                .downfall(0.8F)
                .hasPrecipitation(true)
                .generationSettings(gen.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(46578)
                        .fogColor(12638463)
                        .waterColor(2069949)
                        .waterFogColor(329011)
                        .grassColorOverride(7505193)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_OLD_GROWTH_TAIGA))
                        .build())
                .build();
    }
    private static Biome createTythonPlains(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(features, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        gen.addFeature(GenerationStep.Decoration.LAKES, features.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        gen.addFeature(GenerationStep.Decoration.LAKES, features.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, features.getOrThrow(CavePlacements.MONSTER_ROOM));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, features.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GRAVEL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_DIAMOND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_EMERALD));

        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, features.getOrThrow(MiscOverworldPlacements.SPRING_WATER));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(ModPlacedFeatures.AK_PINE_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(ModPlacedFeatures.HEART_BERRY_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(ModPlacedFeatures.PATCH_PINK_MUSHROOM_PLACED_KEY));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(CavePlacements.GLOW_LICHEN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_LARGE_FERN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.FLOWER_DEFAULT));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_GRASS_TAIGA_2));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_DEAD_BUSH));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_BERRY_COMMON));

        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, features.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        return new Biome.BiomeBuilder()
                .temperature(0.5F)
                .downfall(0.4F)
                .hasPrecipitation(true)
                .generationSettings(gen.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(46578)
                        .fogColor(12638463)
                        .waterColor(2069949)
                        .waterFogColor(329011)
                        .grassColorOverride(7505193)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createTythonMountains(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(features, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        gen.addFeature(GenerationStep.Decoration.LAKES, features.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        gen.addFeature(GenerationStep.Decoration.LAKES, features.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, features.getOrThrow(CavePlacements.MONSTER_ROOM));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_DIRT));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GRAVEL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_COAL_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, features.getOrThrow(OrePlacements.ORE_GOLD_LOWER));

        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, features.getOrThrow(MiscOverworldPlacements.SPRING_WATER));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(ModPlacedFeatures.AK_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(ModPlacedFeatures.HEART_BERRY_PLACED_KEY));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(ModPlacedFeatures.PATCH_PINK_MUSHROOM_PLACED_KEY));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(CavePlacements.GLOW_LICHEN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_LARGE_FERN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.FLOWER_DEFAULT));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_GRASS_TAIGA_2));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_DEAD_BUSH));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.getOrThrow(VegetationPlacements.PATCH_BERRY_COMMON));

        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, features.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        return new Biome.BiomeBuilder()
                .temperature(0.8F)
                .downfall(0.6F)
                .hasPrecipitation(true)
                .generationSettings(gen.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(46578)
                        .fogColor(12638463)
                        .waterColor(2069949)
                        .waterFogColor(329011)
                        .grassColorOverride(7505193)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createKorribanDryCanyon(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placedFeatureHolder = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carverHolder = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder generation =
                new BiomeGenerationSettings.Builder(placedFeatureHolder, carverHolder);

        generation.addCarver(GenerationStep.Carving.AIR, carverHolder.getOrThrow(Carvers.CAVE));

        generation.addFeature(GenerationStep.Decoration.LAKES,
                placedFeatureHolder.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD));

        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS,
                placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_WATER));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        spawns.addSpawn(
                MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 10, 1, 4)
        );
//                        .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.KORRIBAN_GHOST, 10, 1, 4))
        spawns.addMobCharge(EntityType.SPIDER, 1.0, 1.0);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(1.2f)
                .downfall(0.0f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(8947848)
                        .waterColor(16711680)
                        .waterFogColor(329011)
                        .fogColor(16744448)
                        .grassColorOverride(10234659)
                        .foliageColorOverride(11274854)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }

    private static Biome createKorribanSithTomb(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placedFeatureHolder = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carverHolder = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder generation =
                new BiomeGenerationSettings.Builder(placedFeatureHolder, carverHolder);

        generation.addCarver(GenerationStep.Carving.AIR, carverHolder.getOrThrow(Carvers.CAVE));

        generation.addFeature(GenerationStep.Decoration.LAKES,
                placedFeatureHolder.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD));

        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS,
                placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_WATER));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        spawns.addSpawn(
                MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 10, 1, 2)
        );

        spawns.addMobCharge(EntityType.CAVE_SPIDER, 1.0, 1.0);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(1.0f)
                .downfall(0.0f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(16744448)
                        .waterColor(11141120)
                        .waterFogColor(5570560)
                        .fogColor(6684672)
                        .grassColorOverride(7340032)
                        .foliageColorOverride(8396800)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createNabooBiome(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        BiomeDefaultFeatures.addDefaultCarversAndLakes(gen);
        BiomeDefaultFeatures.addDefaultCrystalFormations(gen);
        BiomeDefaultFeatures.addDefaultMonsterRoom(gen);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(gen);
        BiomeDefaultFeatures.addDefaultSprings(gen);
        BiomeDefaultFeatures.addSurfaceFreezing(gen);

        BiomeDefaultFeatures.addDefaultOres(gen);
        BiomeDefaultFeatures.addDefaultSoftDisks(gen);

        BiomeDefaultFeatures.addPlainGrass(gen);
        BiomeDefaultFeatures.addDefaultFlowers(gen);
        BiomeDefaultFeatures.addForestFlowers(gen);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        BiomeDefaultFeatures.farmAnimals(spawns);
        BiomeDefaultFeatures.commonSpawns(spawns);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.8F)
                .downfall(0.6F)
                .generationSettings(gen.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(12638463)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(7907327)
                        .grassColorOverride(5676610)
                        .foliageColorOverride(8367671)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }

    private static Biome createNabooPlains(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        BiomeDefaultFeatures.addDefaultCarversAndLakes(gen);
        BiomeDefaultFeatures.addDefaultCrystalFormations(gen);
        BiomeDefaultFeatures.addDefaultMonsterRoom(gen);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(gen);
        BiomeDefaultFeatures.addDefaultSprings(gen);
        BiomeDefaultFeatures.addSurfaceFreezing(gen);

        BiomeDefaultFeatures.addDefaultOres(gen);
        BiomeDefaultFeatures.addDefaultSoftDisks(gen);

        BiomeDefaultFeatures.addPlainGrass(gen);
        BiomeDefaultFeatures.addDefaultFlowers(gen);

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(VegetationPlacements.TREES_PLAINS));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        BiomeDefaultFeatures.farmAnimals(spawns);
        BiomeDefaultFeatures.commonSpawns(spawns);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.8F)
                .downfall(0.4F)
                .generationSettings(gen.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(12638463)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(7907327)
                        .grassColorOverride(5676610)
                        .foliageColorOverride(8367671)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createNabooOcean(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        gen.addFeature(GenerationStep.Decoration.LAKES,
                placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));

        gen.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS,
                placed.getOrThrow(CavePlacements.AMETHYST_GEODE));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COAL_UPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_IRON_UPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_GOLD));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION,
                placed.getOrThrow(CavePlacements.GLOW_LICHEN));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(AquaticPlacements.SEAGRASS_SIMPLE));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(AquaticPlacements.SEAGRASS_NORMAL));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(AquaticPlacements.KELP_COLD));

        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                placed.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        spawns.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE,
                new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 10, 4, 6));

        spawns.addSpawn(MobCategory.WATER_AMBIENT,
                new MobSpawnSettings.SpawnerData(EntityType.COD, 10, 3, 6));

        spawns.addSpawn(MobCategory.WATER_CREATURE,
                new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 1, 4));

        spawns.addSpawn(MobCategory.WATER_CREATURE,
                new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 1, 1, 2));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.3f)
                .downfall(0.4f)
                .generationSettings(gen.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(14083839)
                        .grassColorOverride(5676610)
                        .foliageColorOverride(8367671)
                        .waterColor(2995897)
                        .waterFogColor(3983048)
                        .skyColor(5358054)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createNabooSwamp(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        BiomeDefaultFeatures.addDefaultCarversAndLakes(gen);
        BiomeDefaultFeatures.addDefaultCrystalFormations(gen);
        BiomeDefaultFeatures.addDefaultMonsterRoom(gen);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(gen);
        BiomeDefaultFeatures.addDefaultSprings(gen);
        BiomeDefaultFeatures.addSurfaceFreezing(gen);

        BiomeDefaultFeatures.addDefaultOres(gen);
        BiomeDefaultFeatures.addDefaultSoftDisks(gen);

        BiomeDefaultFeatures.addSwampVegetation(gen);
        BiomeDefaultFeatures.addDefaultFlowers(gen);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        BiomeDefaultFeatures.commonSpawns(spawns);

        spawns.addSpawn(MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(EntityType.SLIME, 1, 1, 1));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.8F)
                .downfall(0.9F)
                .generationSettings(gen.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(12638463)
                        .waterColor(6388580)
                        .waterFogColor(2302743)
                        .skyColor(7907327)
                        .grassColorOverride(5011004)
                        .foliageColorOverride(6975545)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createMalachorUpperLayer(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        gen.addFeature(GenerationStep.Decoration.LAKES,
                placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_TUFF));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.0f)
                .downfall(0.0f)
                .generationSettings(gen.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(1842204)
                        .fogColor(4013373)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(new Music(SoundEvents.MUSIC_BIOME_NETHER_WASTES, 12000, 24000, false))
                        .build())
                .build();
    }

    private static Biome createMalachorLowerSurface(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        gen.addFeature(GenerationStep.Decoration.LAKES,
                placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                placed.getOrThrow(CavePlacements.MONSTER_ROOM));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COAL_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                placed.getOrThrow(OrePlacements.ORE_COPPER));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(-0.8f)
                .downfall(0.0f)
                .generationSettings(gen.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(2829099)
                        .fogColor(1776418)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(new Music(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP, 12000, 24000, false))
                        .build())
                .build();
    }
    private static Biome createOssusOcean(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));

        BiomeDefaultFeatures.addDefaultOres(gen);
        BiomeDefaultFeatures.addDefaultSoftDisks(gen);
        BiomeDefaultFeatures.addDefaultSprings(gen);

        gen.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(AquaticPlacements.SEAGRASS_NORMAL)
        );

        gen.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(AquaticPlacements.KELP_COLD)
        );

        gen.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                placed.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER)
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.5f)
                .downfall(0.8f)
                .generationSettings(gen.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x5C5C5D)
                        .waterFogColor(0x5C5C5D)
                        .skyColor(0x92949A)
                        .grassColorOverride(0x535745)
                        .foliageColorOverride(0x575E4E)
                        .fogColor(0xD6E7FF)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createOssusDeepOcean(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));

        BiomeDefaultFeatures.addDefaultOres(gen);
        BiomeDefaultFeatures.addDefaultSoftDisks(gen);
        BiomeDefaultFeatures.addDefaultSprings(gen);

        gen.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                placed.getOrThrow(AquaticPlacements.SEAGRASS_DEEP_COLD)
        );

        gen.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                placed.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER)
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.5f)
                .downfall(0.8f)
                .generationSettings(gen.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x5C5C5D)
                        .waterFogColor(0x5C5C5D)
                        .skyColor(0x92949A)
                        .grassColorOverride(0x535745)
                        .foliageColorOverride(0x575E4E)
                        .fogColor(0xD6E7FF)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .build();
    }
    private static Biome createOssusForest(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        // CARVERS
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        // LAKES
        gen.addFeature(GenerationStep.Decoration.LAKES, placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        gen.addFeature(GenerationStep.Decoration.LAKES, placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        // GEODES
        gen.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placed.getOrThrow(CavePlacements.AMETHYST_GEODE));

        // DUNGEONS
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placed.getOrThrow(CavePlacements.MONSTER_ROOM));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placed.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        // ORES
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIRT));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRAVEL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_TUFF));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COAL_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_SMALL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GOLD_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_REDSTONE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_LAPIS));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(CavePlacements.UNDERWATER_MAGMA));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        // SPRINGS
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placed.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placed.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));

        // VEGETATION
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(CavePlacements.GLOW_LICHEN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.BAMBOO));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.BAMBOO_VEGETATION));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.FLOWER_WARM));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_GRASS_JUNGLE));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_NORMAL));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.RED_MUSHROOM_NORMAL));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.VINES));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_MELON));

        // FREEZE
        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, placed.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 12, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.FROG, 10, 1, 5));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 2, 1, 1));
        spawns.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE,
                new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 10, 4, 6));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.95f)
                .downfall(0.9f)
                .mobSpawnSettings(spawns.build())
                .generationSettings(gen.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(9613498)
                        .waterColor(6050877)
                        .waterFogColor(6050877)
                        .fogColor(14083839)
                        .grassColorOverride(5458976)
                        .foliageColorOverride(5722926)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(new Music(SoundEvents.MUSIC_BIOME_BAMBOO_JUNGLE, 12000, 24000, false))
                        .build())
                .build();
    }
    private static Biome createOssusPlains(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        // CARVERS
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        // STEP 1 — LAKES
        gen.addFeature(GenerationStep.Decoration.LAKES, placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        gen.addFeature(GenerationStep.Decoration.LAKES, placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        // STEP 2 — LOCAL MODIFICATIONS
        gen.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placed.getOrThrow(CavePlacements.AMETHYST_GEODE));

        // STEP 3 — UNDERGROUND STRUCTURES
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placed.getOrThrow(CavePlacements.MONSTER_ROOM));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placed.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        // STEP 6 — UNDERGROUND ORES
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIRT));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRAVEL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_TUFF));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COAL_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_SMALL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GOLD_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_REDSTONE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_LAPIS));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(CavePlacements.UNDERWATER_MAGMA));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        // STEP 8 — SPRINGS
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placed.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placed.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));

        // STEP 9 — VEGETATION
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(CavePlacements.GLOW_LICHEN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.BAMBOO));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.BAMBOO_VEGETATION));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.FLOWER_WARM));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_GRASS_JUNGLE));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_NORMAL));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.RED_MUSHROOM_NORMAL));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.VINES));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_MELON));

        // STEP 10 — FREEZE
        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, placed.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 12, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.FROG, 10, 1, 5));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 2, 1, 1));
        spawns.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE,
                new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 10, 4, 6));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.95f)
                .downfall(0.9f)
                .mobSpawnSettings(spawns.build())
                .generationSettings(gen.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(9613498)
                        .waterColor(6050877)
                        .waterFogColor(6050877)
                        .fogColor(14083839)
                        .grassColorOverride(5458976)
                        .foliageColorOverride(5722926)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(new Music(SoundEvents.MUSIC_BIOME_BAMBOO_JUNGLE, 12000, 24000, false))
                        .build())
                .build();
    }
    private static Biome createOssusMountains(BootstapContext<Biome> context) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));

        gen.addFeature(GenerationStep.Decoration.LAKES, placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        gen.addFeature(GenerationStep.Decoration.LAKES, placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        gen.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placed.getOrThrow(CavePlacements.AMETHYST_GEODE));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placed.getOrThrow(CavePlacements.MONSTER_ROOM));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placed.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIRT));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRAVEL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_TUFF));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COAL_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_SMALL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GOLD_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_REDSTONE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_LAPIS));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(CavePlacements.UNDERWATER_MAGMA));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placed.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        gen.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placed.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));

        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(CavePlacements.GLOW_LICHEN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.BAMBOO));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.BAMBOO_VEGETATION));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.FLOWER_WARM));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_GRASS_JUNGLE));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_NORMAL));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.RED_MUSHROOM_NORMAL));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.VINES));
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed.getOrThrow(VegetationPlacements.PATCH_MELON));

        gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, placed.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 12, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.FROG, 10, 1, 5));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 2, 1, 1));
        spawns.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 10, 4, 6));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.4f)
                .downfall(0.5f)
                .mobSpawnSettings(spawns.build())
                .generationSettings(gen.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(9613498)
                        .waterColor(6050877)
                        .waterFogColor(6050877)
                        .fogColor(14083839)
                        .grassColorOverride(5458976)
                        .foliageColorOverride(5722926)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(new Music(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST, 12000, 24000, false))
                        .build())
                .build();
    }
    private static Biome createIlumBiome(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatureHolder = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carverHolder = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatureHolder, carverHolder);

        generation.addCarver(GenerationStep.Carving.AIR, carverHolder.getOrThrow(Carvers.CAVE));
        generation.addCarver(GenerationStep.Carving.AIR, carverHolder.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        generation.addCarver(GenerationStep.Carving.AIR, carverHolder.getOrThrow(Carvers.CANYON));

        generation.addFeature(GenerationStep.Decoration.LAKES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        generation.addFeature(GenerationStep.Decoration.LAKES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placedFeatureHolder.getOrThrow(CavePlacements.AMETHYST_GEODE));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIRT));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRAVEL));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_TUFF));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COAL_LOWER));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_SMALL));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD_LOWER));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_REDSTONE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_LAPIS));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COPPER));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(CavePlacements.UNDERWATER_MAGMA));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_EMERALD));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.BLUE_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.ORANGE_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.GREEN_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.YELLOW_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.CYAN_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.WHITE_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.MAGENTA_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.PURPLE_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.PINK_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.LIME_GREEN_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.TURQUOISE_CRYSTAL_ORE_PLACED_KEY));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, placedFeatureHolder.getOrThrow(OrePlacements.ORE_INFESTED));

        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));
        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_LAVA_FROZEN));

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(CavePlacements.GLOW_LICHEN));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));

        generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(-1.0f)
                .downfall(2.0f)
                .generationSettings(generation.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(15462399)
                        .fogColor(12638463)
                        .waterColor(13622258)
                        .waterFogColor(11250623)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(new Music(SoundEvents.MUSIC_BIOME_SNOWY_SLOPES, 12000, 24000, false))
                        .build())
                .build();
    }
    private static Biome createIlumBiomeForest(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatureHolder = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carverHolder = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatureHolder, carverHolder);

        generation.addCarver(GenerationStep.Carving.AIR, carverHolder.getOrThrow(Carvers.CAVE));
        generation.addCarver(GenerationStep.Carving.AIR, carverHolder.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        generation.addCarver(GenerationStep.Carving.AIR, carverHolder.getOrThrow(Carvers.CANYON));

        generation.addFeature(GenerationStep.Decoration.LAKES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND));
        generation.addFeature(GenerationStep.Decoration.LAKES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placedFeatureHolder.getOrThrow(CavePlacements.AMETHYST_GEODE));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placedFeatureHolder.getOrThrow(CavePlacements.MONSTER_ROOM_DEEP));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIRT));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRAVEL));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_TUFF));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COAL_LOWER));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_IRON_SMALL));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_GOLD_LOWER));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_REDSTONE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_LAPIS));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_COPPER));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(CavePlacements.UNDERWATER_MAGMA));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(OrePlacements.ORE_EMERALD));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.BLUE_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.ORANGE_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.GREEN_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.YELLOW_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.CYAN_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.WHITE_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.MAGENTA_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.PURPLE_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.PINK_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.LIME_GREEN_CRYSTAL_ORE_PLACED_KEY));
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.getOrThrow(ModPlacedFeatures.TURQUOISE_CRYSTAL_ORE_PLACED_KEY));

        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, placedFeatureHolder.getOrThrow(OrePlacements.ORE_INFESTED));

        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_WATER));
        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_LAVA));
        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.SPRING_LAVA_FROZEN));

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(CavePlacements.GLOW_LICHEN));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_LARGE_FERN));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.TREES_TAIGA));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.FLOWER_DEFAULT));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_GRASS_TAIGA_2));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.BROWN_MUSHROOM_TAIGA));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.RED_MUSHROOM_TAIGA));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_SUGAR_CANE));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_PUMPKIN));
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder.getOrThrow(VegetationPlacements.PATCH_BERRY_RARE));

        generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, placedFeatureHolder.getOrThrow(MiscOverworldPlacements.FREEZE_TOP_LAYER));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(-1.0f)
                .downfall(2.0f)
                .generationSettings(generation.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(15462399)
                        .fogColor(12638463)
                        .waterColor(13622258)
                        .waterFogColor(11250623)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(new Music(SoundEvents.MUSIC_BIOME_SNOWY_SLOPES, 12000, 24000, false))
                        .build())
                .build();
    }
    private static Biome createMustafarBiome(BootstapContext<Biome> context, float temperature) {

        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placed, carvers);

        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND));
        gen.addCarver(GenerationStep.Carving.AIR, carvers.getOrThrow(Carvers.CANYON));

        gen.addFeature(GenerationStep.Decoration.LAKES, placed.getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));

        gen.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, placed.getOrThrow(CavePlacements.AMETHYST_GEODE));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, placed.getOrThrow(CavePlacements.MONSTER_ROOM));

        // STEP 6 — UNDERGROUND ORES
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRAVEL));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRANITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GRANITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIORITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIORITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_TUFF));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COAL_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COAL_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_UPPER));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_MIDDLE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_IRON_SMALL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GOLD));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_GOLD_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_REDSTONE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_REDSTONE_LOWER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND_LARGE));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_DIAMOND_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_LAPIS));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_LAPIS_BURIED));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_COPPER));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_SAND));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_CLAY));
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(MiscOverworldPlacements.DISK_GRAVEL));

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placed.getOrThrow(OrePlacements.ORE_EMERALD));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(temperature)
                .downfall(0.0f)
                .generationSettings(gen.build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(16733184)
                        .fogColor(8388608)
                        .waterColor(16740352)
                        .waterFogColor(11674146)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(new Music(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST, 12000, 24000, false))
                        .build())
                .build();
    }
    private static Biome createMustafarVolcanicPlains(BootstapContext<Biome> context) {
        return createMustafarBiome(context, 1.5f);
    }

    private static Biome createMustafarLavaField(BootstapContext<Biome> context) {
        return createMustafarBiome(context, 2.0f);
    }

    private static Biome createMustafarMagmaLake(BootstapContext<Biome> context) {
        return createMustafarBiome(context, 2.5f);
    }
}