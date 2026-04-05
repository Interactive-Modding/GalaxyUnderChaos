package server.galaxyunderchaos.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.List;

public class ModCarvers {

    public static final ResourceKey<ConfiguredWorldCarver<?>> TYTHON_CAVES = carverKey("tython_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> NABOO_CAVES = carverKey("naboo_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> ILUM_CAVES = carverKey("ilum_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> MUSTAFAR_CAVES = carverKey("mustafar_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> OSSUS_CAVES = carverKey("ossus_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> ASHLA_CAVES = carverKey("ashla_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> BOGAN_CAVES = carverKey("bogan_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> MALACHOR_CAVES = carverKey("malachor_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> KORRIBAN_CAVES = carverKey("korriban_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> DANTOOINE_CAVES = carverKey("dantooine_caves");

    public static void bootstrap(BootstapContext<ConfiguredWorldCarver<?>> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

        ConfiguredWorldCarver<?> cave = WorldCarver.CAVE.configured(
                new CaveCarverConfiguration(
                        0.15F,
                        UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)),
                        UniformFloat.of(0.1F, 0.9F),
                        VerticalAnchor.aboveBottom(8),
                        CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
                        blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
                        UniformFloat.of(0.7F, 1.4F),
                        UniformFloat.of(0.8F, 1.3F),
                        UniformFloat.of(-1.0F, -0.4F)
                )
        );

        List.of(
                TYTHON_CAVES,
                NABOO_CAVES,
                ILUM_CAVES,
                MUSTAFAR_CAVES,
                OSSUS_CAVES,
                ASHLA_CAVES,
                BOGAN_CAVES,
                MALACHOR_CAVES,
                KORRIBAN_CAVES,
                DANTOOINE_CAVES
        ).forEach(key -> context.register(key, cave));
    }

    private static ResourceKey<ConfiguredWorldCarver<?>> carverKey(String path) {
        return ResourceKey.create(
                Registries.CONFIGURED_CARVER,
                ResourceLocation.fromNamespaceAndPath(galaxyunderchaos.MODID, path)
        );
    }
}