package server.galaxyunderchaos.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        for (RegistryObject<Block> entry : galaxyunderchaos.BLOCKS.getEntries()) {
            Block block = entry.get();

            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
            if (id == null || !id.getNamespace().equals(galaxyunderchaos.MODID)) continue;

            String path = id.getPath();

            switch (path) {
                case "ak_slab",
                     "ancient_temple_stone_slab",
                     "ashla_temple_stone_slab",
                     "bogan_temple_stone_slab",
                     "dark_temple_stone_slab",
                     "heart_berry_slab",
                     "temple_stone_slab",
                     "tython_temple_stone_slab" ->
                        this.add(block, createSlabItemTable(block));
                case "ancient_temple_stone" -> {
                    Block cracked = getBlock("ancient_temple_stone_cracked");
                    this.add(block, createSilkTouchDispatchTable(
                            block,
                            applyExplosionCondition(block, LootItem.lootTableItem(cracked))
                    ));
                }
                case "ancient_temple_stone_holobook" -> {
                    Item drop = getItem("ancient_holobook");
                    this.add(block,
                            createSilkTouchDispatchTable(
                                    block,
                                    applyExplosionDecay(block,
                                            LootItem.lootTableItem(drop)
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)))
                                    )
                            )
                    );
                }

                case "dark_temple_stone_holobook" -> {
                    Item drop = getItem("sith_holobook");
                    this.add(block,
                            createSilkTouchDispatchTable(
                                    block,
                                    applyExplosionDecay(block,
                                            LootItem.lootTableItem(drop)
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)))
                                    )
                            )
                    );
                }

                case "temple_stone_holobook" -> {
                    Item drop = getItem("jedi_holobook");
                    this.add(block,
                            createSilkTouchDispatchTable(
                                    block,
                                    applyExplosionDecay(block,
                                            LootItem.lootTableItem(drop)
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)))
                                    )
                            )
                    );
                }
                case "blue_crystal_ore"      -> this.add(block, createOreDrop(block, getItem("blue_kyber")));
                case "cyan_crystal_ore"      -> this.add(block, createOreDrop(block, getItem("cyan_kyber")));
                case "green_crystal_ore"     -> this.add(block, createOreDrop(block, getItem("green_kyber")));
                case "lime_green_crystal_ore"-> this.add(block, createOreDrop(block, getItem("lime_green_kyber")));
                case "magenta_crystal_ore"   -> this.add(block, createOreDrop(block, getItem("magenta_kyber")));
                case "orange_crystal_ore"    -> this.add(block, createOreDrop(block, getItem("orange_kyber")));
                case "pink_crystal_ore"      -> this.add(block, createOreDrop(block, getItem("pink_kyber")));
                case "purple_crystal_ore"    -> this.add(block, createOreDrop(block, getItem("purple_kyber")));
                case "turquoise_crystal_ore" -> this.add(block, createOreDrop(block, getItem("turquoise_kyber")));
                case "white_crystal_ore"     -> this.add(block, createOreDrop(block, getItem("white_kyber")));
                case "yellow_crystal_ore"    -> this.add(block, createOreDrop(block, getItem("yellow_kyber")));
                case "amber_crystal_ore"       -> this.add(block, createOreDrop(block, getItem("amber_kyber")));
                case "gold_crystal_ore"        -> this.add(block, createOreDrop(block, getItem("gold_kyber")));
                case "light_blue_crystal_ore"  -> this.add(block, createOreDrop(block, getItem("light_blue_kyber")));
                case "dark_blue_crystal_ore"   -> this.add(block, createOreDrop(block, getItem("dark_blue_kyber")));
                case "deep_violet_crystal_ore"      -> this.add(block, createOreDrop(block, getItem("deep_violet_kyber")));
                case "arctic_blue_crystal_ore" -> this.add(block, createOreDrop(block, getItem("arctic_blue_kyber")));
                case "rose_pink_crystal_ore"   -> this.add(block, createOreDrop(block, getItem("rose_pink_kyber")));
                case "chromium_ore",
                     "chromium_deepslate_ore",
                     "titanium_ore",
                     "titanium_deepslate_ore" ->
                        this.add(block, createOreDrop(block, getItem(path)));
                case "tython_grass" -> {
                    Block dirt = getBlock("tython_dirt");
                    this.add(block, createSilkTouchDispatchTable(
                            block,
                            applyExplosionCondition(block, LootItem.lootTableItem(dirt))
                    ));
                }
                case "heart_berry_fruit_leaves" -> {
                    Block sapling = getBlock("heart_berry_sapling");
                    this.add(block, createLeavesDrops(block, sapling, NORMAL_LEAVES_SAPLING_CHANCES));
                }
                default -> this.dropSelf(block);
            }
        }
    }
    private static Item getItem(String path) {
        ResourceLocation id = new ResourceLocation(galaxyunderchaos.MODID, path);
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null) throw new IllegalStateException("Missing item: " + id);
        return item;
    }

    private static Block getBlock(String path) {
        ResourceLocation id = new ResourceLocation(galaxyunderchaos.MODID, path);
        Block block = ForgeRegistries.BLOCKS.getValue(id);
        if (block == null) throw new IllegalStateException("Missing block: " + id);
        return block;
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return galaxyunderchaos.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
    }
}
