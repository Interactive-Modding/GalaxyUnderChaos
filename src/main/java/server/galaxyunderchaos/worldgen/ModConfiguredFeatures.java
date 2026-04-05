package server.galaxyunderchaos.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
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
                BlockStateProvider.simple(Blocks.STRIPPED_BIRCH_LOG),
                new DarkOakTrunkPlacer(12, 2, 1),
                BlockStateProvider.simple(Blocks.OAK_LEAVES),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 0, 1, 2, 1, OptionalInt.empty())).build());

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
