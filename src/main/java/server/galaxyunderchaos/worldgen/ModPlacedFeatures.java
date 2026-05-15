package server.galaxyunderchaos.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> CHROMIUM_ORE_PLACED_KEY = registerKey("chromium_ore_placed");
    public static final ResourceKey<PlacedFeature> TITANIUM_ORE_PLACED_KEY = registerKey("titanium_ore_placed");
    public static final ResourceKey<PlacedFeature> BLBA_PLACED_KEY = registerKey("blba_placed");
    public static final ResourceKey<PlacedFeature> AK_PLACED_KEY = registerKey("ak_placed");
    public static final ResourceKey<PlacedFeature> AK_PINE_PLACED_KEY = registerKey("ak_pine_placed");
    public static final ResourceKey<PlacedFeature> HEART_BERRY_PLACED_KEY = registerKey("heart_berry_placed");
    public static final ResourceKey<PlacedFeature> DILLIA_PLACED_KEY = registerKey("dillia_placed");
    public static final ResourceKey<PlacedFeature> BELLEW_FLOWER_PLACED_KEY = registerKey("bellew_flower_placed");
    public static final ResourceKey<PlacedFeature> CAMBYLICTUS_PLACED_KEY = registerKey("cambylictus_placed");
    public static final ResourceKey<PlacedFeature> PERLOTE_PLACED_KEY = registerKey("perlote_placed");
    public static final ResourceKey<PlacedFeature> RUTIGER_PLACED_KEY = registerKey("rutiger_placed");
    public static final ResourceKey<PlacedFeature> POLAR_PLACED_KEY = registerKey("polar_placed");
    public static final ResourceKey<PlacedFeature> NABOO_PINE_PLACED_KEY = registerKey("naboo_pine_placed");
    public static final ResourceKey<PlacedFeature> QUEENS_HEART_FLOWER_PLACED_KEY = registerKey("queens_heart_flower_placed");
    public static final ResourceKey<PlacedFeature> PATCH_PINK_MUSHROOM_PLACED_KEY = registerKey("patch_pink_mushroom");
    public static final ResourceKey<PlacedFeature> BLUE_CRYSTAL_ORE_PLACED_KEY = registerKey("blue_crystal_ore");
    public static final ResourceKey<PlacedFeature> ORANGE_CRYSTAL_ORE_PLACED_KEY = registerKey("orange_crystal_ore");
    public static final ResourceKey<PlacedFeature> GREEN_CRYSTAL_ORE_PLACED_KEY = registerKey("green_crystal_ore");
    public static final ResourceKey<PlacedFeature> YELLOW_CRYSTAL_ORE_PLACED_KEY = registerKey("yellow_crystal_ore");
    public static final ResourceKey<PlacedFeature> CYAN_CRYSTAL_ORE_PLACED_KEY = registerKey("cyan_crystal_ore");
    public static final ResourceKey<PlacedFeature> WHITE_CRYSTAL_ORE_PLACED_KEY = registerKey("white_crystal_ore");
    public static final ResourceKey<PlacedFeature> MAGENTA_CRYSTAL_ORE_PLACED_KEY = registerKey("magenta_crystal_ore");
    public static final ResourceKey<PlacedFeature> PURPLE_CRYSTAL_ORE_PLACED_KEY = registerKey("purple_crystal_ore");
    public static final ResourceKey<PlacedFeature> PINK_CRYSTAL_ORE_PLACED_KEY = registerKey("pink_crystal_ore");
    public static final ResourceKey<PlacedFeature> LIME_GREEN_CRYSTAL_ORE_PLACED_KEY = registerKey("lime_green_crystal_ore");
    public static final ResourceKey<PlacedFeature> TURQUOISE_CRYSTAL_ORE_PLACED_KEY = registerKey("turquoise_crystal_ore");
    public static final ResourceKey<PlacedFeature> DEEP_VIOLET_CRYSTAL_ORE_PLACED_KEY = registerKey("deep_violet_crystal_ore");
    public static final ResourceKey<PlacedFeature> ARCTIC_BLUE_CRYSTAL_ORE_PLACED_KEY = registerKey("arctic_blue_crystal_ore");
    public static final ResourceKey<PlacedFeature> DARK_BLUE_CRYSTAL_ORE_PLACED_KEY = registerKey("dark_blue_crystal_ore");
    public static final ResourceKey<PlacedFeature> LIGHT_BLUE_CRYSTAL_ORE_PLACED_KEY = registerKey("light_blue_crystal_ore");
    public static final ResourceKey<PlacedFeature> AMBER_CRYSTAL_ORE_PLACED_KEY = registerKey("amber_crystal_ore");
    public static final ResourceKey<PlacedFeature> GOLD_CRYSTAL_ORE_PLACED_KEY = registerKey("gold_crystal_ore");
    public static final ResourceKey<PlacedFeature> ROSE_PINK_CRYSTAL_ORE_PLACED_KEY = registerKey("rose_pink_crystal_ore");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, CHROMIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_CHROMIUM_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, PATCH_PINK_MUSHROOM_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.PINK_MUSHROOM_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(512),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                ));

        register(context, TITANIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_TITANIUM_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, BLBA_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BLBA_KEY),
                List.of(
                        CountPlacement.of(3),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.BLBA_SAPLING.get().defaultBlockState(), BlockPos.ZERO))
                ));

        register(context, AK_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.AK_TREE_KEY),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome()));

        register(context, AK_PINE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.AK_TREE_PINE_KEY),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome()));

        register(context, HEART_BERRY_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.HEART_BERRY_KEY),
                List.of(
                        CountPlacement.of(UniformInt.of(0, 1)),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.HEART_BERRY_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                ));

        register(context, DILLIA_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DILLIA_TREE_KEY),
                List.of(
                        CountPlacement.of(UniformInt.of(2, 3)),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.DILLIA_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                ));

        register(context, BELLEW_FLOWER_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BELLEW_FLOWER_KEY),
                List.of(CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.BELLEW_FLOWER.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome()));

        register(context, CAMBYLICTUS_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CAMBYLICTUS_TREE_KEY),
                List.of(
                        CountPlacement.of(UniformInt.of(1, 2)),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(2),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.CAMBYLICTUS_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                ));

        register(context, PERLOTE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.PERLOTE_TREE_KEY),
                List.of(
                        CountPlacement.of(UniformInt.of(2, 3)),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(2),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.PERLOTE_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                ));

        register(context, RUTIGER_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.RUTIGER_TREE_KEY),
                List.of(
                        CountPlacement.of(1),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(1),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.RUTIGER_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                ));

        register(context, POLAR_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.POLAR_TREE_KEY),
                List.of(
                        CountPlacement.of(UniformInt.of(2, 3)),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.POLAR_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                ));

        register(context, NABOO_PINE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.NABOO_PINE_TREE_KEY),
                List.of(
                        CountPlacement.of(1),
                        InSquarePlacement.spread(),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.NABOO_PINE_SAPLING.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                ));

        register(context, QUEENS_HEART_FLOWER_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.QUEENS_HEART_FLOWER_KEY),
                List.of(
                        CountPlacement.of(7),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.QUEENS_HEART_FLOWER.get().defaultBlockState(), BlockPos.ZERO)),
                        BiomeFilter.biome()
                ));

        register(context, BLUE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BLUE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, ORANGE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ORANGE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, GREEN_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GREEN_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, YELLOW_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.YELLOW_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, CYAN_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CYAN_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, WHITE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.WHITE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, MAGENTA_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MAGENTA_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, PURPLE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.PURPLE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, PINK_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.PINK_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, LIME_GREEN_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.LIME_GREEN_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, TURQUOISE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TURQUOISE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, AMBER_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.AMBER_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, GOLD_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GOLD_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, LIGHT_BLUE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.LIGHT_BLUE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, DARK_BLUE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DARK_BLUE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, DEEP_VIOLET_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DEEP_VIOLET_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, ARCTIC_BLUE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ARCTIC_BLUE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, ROSE_PINK_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ROSE_PINK_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(galaxyunderchaos.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}