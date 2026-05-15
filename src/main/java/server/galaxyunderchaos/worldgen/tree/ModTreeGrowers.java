package server.galaxyunderchaos.worldgen.tree;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import server.galaxyunderchaos.worldgen.ModConfiguredFeatures;

public class ModTreeGrowers {
    public static final AbstractTreeGrower BLBA = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.BLBA_KEY;
        }
    };

    public static final AbstractTreeGrower AK_TREE = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.AK_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower DILLIA_TREE = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.DILLIA_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower CAMBYLICTUS_TREE = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.CAMBYLICTUS_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower PERLOTE_TREE = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.PERLOTE_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower RUTIGER_TREE = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.RUTIGER_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower POLAR_TREE = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.POLAR_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower NABOO_PINE_TREE = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.NABOO_PINE_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower HEART_BERRY_TREE = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.HEART_BERRY_KEY;
        }
    };
}
