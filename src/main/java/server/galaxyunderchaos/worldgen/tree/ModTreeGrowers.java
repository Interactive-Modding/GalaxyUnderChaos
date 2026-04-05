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

    public static final AbstractTreeGrower HEART_BERRY_TREE = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return ModConfiguredFeatures.HEART_BERRY_KEY;
        }
    };
}
