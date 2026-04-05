package server.galaxyunderchaos.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
                List.of(PlacementUtils.countExtra(3, 0.1F, 2), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.BLBA_SAPLING.get().defaultBlockState(), BlockPos.ZERO))));

        register(context, AK_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.AK_TREE_KEY),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome()));

        register(context, AK_PINE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.AK_TREE_PINE_KEY),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome()));

        register(context, HEART_BERRY_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.HEART_BERRY_KEY),
                List.of(PlacementUtils.countExtra(0, 0.05F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(galaxyunderchaos.HEART_BERRY_SAPLING.get().defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome()));

        register(context, BLUE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BLUE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, ORANGE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ORANGE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, GREEN_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GREEN_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, YELLOW_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.YELLOW_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, CYAN_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CYAN_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, WHITE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.WHITE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, MAGENTA_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MAGENTA_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, PURPLE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.PURPLE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, PINK_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.PINK_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, LIME_GREEN_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.LIME_GREEN_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
        register(context, TURQUOISE_CRYSTAL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TURQUOISE_CRYSTAL_ORE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(galaxyunderchaos.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
