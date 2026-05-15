package server.galaxyunderchaos.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaPineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.util.random.SimpleWeightedRandomList;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_TITANIUM_ORE_KEY = registerKey("titanium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_CHROMIUM_ORE_KEY = registerKey("chromium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLBA_KEY = registerKey("blba");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AK_TREE_KEY = registerKey("ak_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AK_TREE_PINE_KEY = registerKey("ak_tree_pine");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HEART_BERRY_KEY = registerKey("heart_berry_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DILLIA_TREE_KEY = registerKey("dillia_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BELLEW_FLOWER_KEY = registerKey("bellew_flower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CAMBYLICTUS_TREE_KEY = registerKey("cambylictus_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PERLOTE_TREE_KEY = registerKey("perlote_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RUTIGER_TREE_KEY = registerKey("rutiger_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> POLAR_TREE_KEY = registerKey("polar_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NABOO_PINE_TREE_KEY = registerKey("naboo_pine_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> QUEENS_HEART_FLOWER_KEY = registerKey("queens_heart_flower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_CRYSTAL_ORE_KEY = registerKey("blue_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_CRYSTAL_ORE_KEY = registerKey("orange_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GREEN_CRYSTAL_ORE_KEY = registerKey("green_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> YELLOW_CRYSTAL_ORE_KEY = registerKey("yellow_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CYAN_CRYSTAL_ORE_KEY = registerKey("cyan_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WHITE_CRYSTAL_ORE_KEY = registerKey("white_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAGENTA_CRYSTAL_ORE_KEY = registerKey("magenta_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PURPLE_CRYSTAL_ORE_KEY = registerKey("purple_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PINK_CRYSTAL_ORE_KEY = registerKey("pink_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIME_GREEN_CRYSTAL_ORE_KEY = registerKey("lime_green_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TURQUOISE_CRYSTAL_ORE_KEY = registerKey("turquoise_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PINK_MUSHROOM_KEY = registerKey("pink_mushroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMBER_CRYSTAL_ORE_KEY = registerKey("amber_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GOLD_CRYSTAL_ORE_KEY = registerKey("gold_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIGHT_BLUE_CRYSTAL_ORE_KEY = registerKey("light_blue_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_BLUE_CRYSTAL_ORE_KEY = registerKey("dark_blue_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DEEP_VIOLET_CRYSTAL_ORE_KEY = registerKey("deep_violet_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ARCTIC_BLUE_CRYSTAL_ORE_KEY = registerKey("arctic_blue_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROSE_PINK_CRYSTAL_ORE_KEY = registerKey("rose_pink_crystal_ore");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        register(context, PINK_MUSHROOM_KEY, Feature.HUGE_RED_MUSHROOM,
                new HugeMushroomFeatureConfiguration(
                        BlockStateProvider.simple(
                                galaxyunderchaos.PINK_MUSHROOM_BLOCK.get().defaultBlockState()
                        ),
                        BlockStateProvider.simple(
                                galaxyunderchaos.MUSHROOM_STEM.get().defaultBlockState()
                        ),
                        2
                ));
        List<OreConfiguration.TargetBlockState> overworldTitaniumOres = List.of(
                OreConfiguration.target(stoneReplaceables, galaxyunderchaos.TITANIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, galaxyunderchaos.TITANIUM_DEEPSLATE_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> overworldChromiumOres = List.of(
                OreConfiguration.target(stoneReplaceables, galaxyunderchaos.CHROMIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, galaxyunderchaos.CHROMIUM_DEEPSLATE_ORE.get().defaultBlockState()));

        register(context, OVERWORLD_TITANIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldTitaniumOres, 9));
        register(context, OVERWORLD_CHROMIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldChromiumOres, 9));
        register(context, AMBER_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.AMBER_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));

        register(context, GOLD_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.GOLD_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));

        register(context, LIGHT_BLUE_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.LIGHT_BLUE_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));

        register(context, DARK_BLUE_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.DARK_BLUE_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));

        register(context, DEEP_VIOLET_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.DEEP_VIOLET_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));

        register(context, ARCTIC_BLUE_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.ARCTIC_BLUE_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));

        register(context, ROSE_PINK_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.ROSE_PINK_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, BLUE_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.BLUE_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, ORANGE_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.ORANGE_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, GREEN_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.GREEN_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, YELLOW_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.YELLOW_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, CYAN_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.CYAN_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, WHITE_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.WHITE_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, MAGENTA_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.MAGENTA_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, PURPLE_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.PURPLE_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, PINK_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.PINK_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, LIME_GREEN_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.LIME_GREEN_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));
        register(context, TURQUOISE_CRYSTAL_ORE_KEY, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(stoneReplaceables, galaxyunderchaos.TURQUOISE_CRYSTAL_ORE.get().defaultBlockState())), 3, 0.0f));

        register(context, BLBA_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.BLBA_LOG.get()),
                new DarkOakTrunkPlacer(12, 2, 1),
                BlockStateProvider.simple(galaxyunderchaos.BLBA_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 0, 1, 2, 1, OptionalInt.empty())).build());

        register(context, BELLEW_FLOWER_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(
                        5,
                        3,
                        1,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(galaxyunderchaos.BELLEW_FLOWER.get())
                                )
                        )
                ));
        register(context, DILLIA_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.DILLIA_LOG.get()),
                new StraightTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(galaxyunderchaos.DILLIA_LEAVES.get()),
                new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 1), UniformInt.of(2, 3)),
                new TwoLayersFeatureSize(1, 0, 2))
                .ignoreVines()
                .build());

        register(context, QUEENS_HEART_FLOWER_KEY, Feature.FLOWER,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(galaxyunderchaos.QUEENS_HEART_FLOWER.get()))));

        // Naboo swamp/Gungan Sacred Place: giant Cambylictus with lifted, tangled roots and heavy vines.
        register(context, CAMBYLICTUS_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.CAMBYLICTUS_LOG.get()),
                new DarkOakTrunkPlacer(10, 5, 3),
                BlockStateProvider.simple(galaxyunderchaos.CAMBYLICTUS_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 0, 1, 2, 1, OptionalInt.empty()))
                .dirt(BlockStateProvider.simple(Blocks.MUD))
                .decorators(List.of(
                        TrunkVineDecorator.INSTANCE,
                        new LeaveVineDecorator(0.35F),
                        new AlterGroundDecorator(BlockStateProvider.simple(Blocks.MUD))))
                .build());

        // Naboo eastern swamps: Perlote trees form muddy groves where slug-beetles live among the roots.
        register(context, PERLOTE_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.PERLOTE_LOG.get()),
                new StraightTrunkPlacer(7, 2, 2),
                BlockStateProvider.simple(galaxyunderchaos.PERLOTE_LEAVES.get()),
                new SpruceFoliagePlacer(UniformInt.of(2, 4), UniformInt.of(0, 1), UniformInt.of(2, 4)),
                new TwoLayersFeatureSize(1, 0, 2))
                .dirt(BlockStateProvider.simple(Blocks.MUD))
                .decorators(List.of(
                        TrunkVineDecorator.INSTANCE,
                        new LeaveVineDecorator(0.35F),
                        new AlterGroundDecorator(BlockStateProvider.simple(Blocks.MUD))))
                .build());

        // Naboo swamp: Rutiger stays small and delicate because its roots have a limited depth range.
        register(context, RUTIGER_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.RUTIGER_LOG.get()),
                new StraightTrunkPlacer(4, 1, 1),
                BlockStateProvider.simple(galaxyunderchaos.RUTIGER_LEAVES.get()),
                new SpruceFoliagePlacer(UniformInt.of(1, 2), UniformInt.of(0, 1), UniformInt.of(1, 2)),
                new TwoLayersFeatureSize(1, 0, 1))
                .dirt(BlockStateProvider.simple(Blocks.MUD))
                .decorators(List.of(TrunkVineDecorator.INSTANCE))
                .build());

        // Naboo plains/Theed outskirts: pale formal Polar trees.
        register(context, POLAR_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.POLAR_LOG.get()),
                new StraightTrunkPlacer(7, 2, 1),
                BlockStateProvider.simple(galaxyunderchaos.POLAR_LEAVES.get()),
                new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 1), UniformInt.of(2, 3)),
                new TwoLayersFeatureSize(1, 0, 2))
                .ignoreVines()
                .build());

        // Naboo hills: taller pine-like tree variant for Legends pine references.
        register(context, NABOO_PINE_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.NABOO_PINE_LOG.get()),
                new GiantTrunkPlacer(11, 2, 8),
                BlockStateProvider.simple(galaxyunderchaos.NABOO_PINE_LEAVES.get()),
                new MegaPineFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), UniformInt.of(4, 7)),
                new TwoLayersFeatureSize(1, 1, 2))
                .dirt(BlockStateProvider.simple(Blocks.DIRT))
                .decorators(List.of(new AlterGroundDecorator(BlockStateProvider.simple(Blocks.PODZOL))))
                .build());

        register(context, HEART_BERRY_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.HEART_BERRY_LOG.get()),
                new StraightTrunkPlacer(5, 2, 1),
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(galaxyunderchaos.HEART_BERRY_LEAVES.get().defaultBlockState(), 3)
                        .add(galaxyunderchaos.HEART_BERRY_FRUIT_LEAVES.get().defaultBlockState(), 1)
                        .build()),
                new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(1, 2)),
                new TwoLayersFeatureSize(2, 0, 2))
                .dirt(BlockStateProvider.simple(Blocks.ROOTED_DIRT))
                .ignoreVines()
                .build());

        register(context, AK_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.AK_LOG.get()),
                new GiantTrunkPlacer(13, 2, 14),
                BlockStateProvider.simple(galaxyunderchaos.AK_LEAVES.get()),
                new MegaPineFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), UniformInt.of(13, 17)),
                new TwoLayersFeatureSize(1, 1, 2))
                .decorators(List.of(new AlterGroundDecorator(BlockStateProvider.simple(Blocks.PODZOL))))
                .dirt(BlockStateProvider.simple(Blocks.DIRT))
                .build());

        register(context, AK_TREE_PINE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(galaxyunderchaos.AK_LOG.get()),
                new GiantTrunkPlacer(13, 2, 14),
                BlockStateProvider.simple(galaxyunderchaos.AK_LEAVES.get()),
                new MegaPineFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), UniformInt.of(3, 7)),
                new TwoLayersFeatureSize(1, 1, 2))
                .decorators(List.of(new AlterGroundDecorator(BlockStateProvider.simple(Blocks.PODZOL))))
                .dirt(BlockStateProvider.simple(Blocks.DIRT))
                .build());
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(galaxyunderchaos.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
