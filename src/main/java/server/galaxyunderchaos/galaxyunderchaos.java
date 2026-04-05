package server.galaxyunderchaos;

import client.renderer.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import server.galaxyunderchaos.block.*;
import server.galaxyunderchaos.data.KeyBindings;
import server.galaxyunderchaos.entity.*;
import server.galaxyunderchaos.event.LightsaberFormEventHandler;
import server.galaxyunderchaos.item.*;
import server.galaxyunderchaos.lightsaber.AdvancedLightsaberLegacyHilts;
import server.galaxyunderchaos.lightsaber.LightsaberFormNetworking;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;
import server.galaxyunderchaos.loot.ModLootModifiers;
import server.galaxyunderchaos.sound.ModSounds;
import server.galaxyunderchaos.worldgen.biome.ModBiomes;
import server.galaxyunderchaos.worldgen.tree.ModTreeGrowers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod(galaxyunderchaos.MODID)public class galaxyunderchaos {
    public static final String MODID = "galaxyunderchaos";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    // #BLOCKS
    public static final RegistryObject<Block> CHROMIUM_ORE = BLOCKS.register("chromium_ore", EarthCrystalOre::new);
    public static final RegistryObject<Block> CHROMIUM_DEEPSLATE_ORE = BLOCKS.register("chromium_deepslate_ore", EarthCrystalOre::new);
    public static final RegistryObject<Block> TITANIUM_ORE = BLOCKS.register("titanium_ore", EarthCrystalOre::new);
    public static final RegistryObject<Block> TITANIUM_DEEPSLATE_ORE = BLOCKS.register("titanium_deepslate_ore", EarthCrystalOre::new);
    public static final RegistryObject<Block> BLUE_CRYSTAL_ORE = BLOCKS.register("blue_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> ORANGE_CRYSTAL_ORE = BLOCKS.register("orange_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> GREEN_CRYSTAL_ORE = BLOCKS.register("green_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> YELLOW_CRYSTAL_ORE = BLOCKS.register("yellow_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> CYAN_CRYSTAL_ORE = BLOCKS.register("cyan_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> WHITE_CRYSTAL_ORE = BLOCKS.register("white_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> MAGENTA_CRYSTAL_ORE = BLOCKS.register("magenta_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> PURPLE_CRYSTAL_ORE = BLOCKS.register("purple_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> PINK_CRYSTAL_ORE = BLOCKS.register("pink_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> LIME_GREEN_CRYSTAL_ORE = BLOCKS.register("lime_green_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> TURQUOISE_CRYSTAL_ORE = BLOCKS.register("turquoise_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> AMBER_CRYSTAL_ORE = BLOCKS.register("amber_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> GOLD_CRYSTAL_ORE = BLOCKS.register("gold_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> LIGHT_BLUE_CRYSTAL_ORE = BLOCKS.register("light_blue_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> DARK_BLUE_CRYSTAL_ORE = BLOCKS.register("dark_blue_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> DEEP_VIOLET_CRYSTAL_ORE = BLOCKS.register("deep_violet_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> ARCTIC_BLUE_CRYSTAL_ORE = BLOCKS.register("arctic_blue_crystal_ore", CrystalOre::new);
    public static final RegistryObject<Block> ROSE_PINK_CRYSTAL_ORE = BLOCKS.register("rose_pink_crystal_ore", CrystalOre::new);

    public static final RegistryObject<Item> CHROMIUM_ORE_ITEM = ITEMS.register("chromium_ore", () -> new BlockItem(CHROMIUM_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHROMIUM_DEEPSLATE_ORE_ITEM = ITEMS.register("chromium_deepslate_ore", () -> new BlockItem(CHROMIUM_DEEPSLATE_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TIATANIUM_ORE_ITEM = ITEMS.register("titanium_ore", () -> new BlockItem(TITANIUM_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TITANIUM_DEEPSLATE_ORE_ITEM = ITEMS.register("titanium_deepslate_ore", () -> new BlockItem(TITANIUM_DEEPSLATE_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLUE_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("blue_crystal_ore", () -> new BlockItem(BLUE_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> ORANGE_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("orange_crystal_ore", () -> new BlockItem(ORANGE_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> GREEN_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("green_crystal_ore", () -> new BlockItem(GREEN_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> YELLOW_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("yellow_crystal_ore", () -> new BlockItem(YELLOW_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> CYAN_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("cyan_crystal_ore", () -> new BlockItem(CYAN_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> WHITE_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("white_crystal_ore", () -> new BlockItem(WHITE_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> MAGENTA_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("magenta_crystal_ore", () -> new BlockItem(MAGENTA_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PURPLE_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("purple_crystal_ore", () -> new BlockItem(PURPLE_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PINK_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("pink_crystal_ore", () -> new BlockItem(PINK_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> LIME_GREEN_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("lime_green_crystal_ore", () -> new BlockItem(LIME_GREEN_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TURQUOISE_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("turquoise_crystal_ore", () -> new BlockItem(TURQUOISE_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> AMBER_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("amber_crystal_ore", () -> new BlockItem(AMBER_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> GOLD_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("gold_crystal_ore", () -> new BlockItem(GOLD_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_BLUE_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("light_blue_crystal_ore", () -> new BlockItem(LIGHT_BLUE_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> DARK_BLUE_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("dark_blue_crystal_ore", () -> new BlockItem(DARK_BLUE_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> DEEP_VIOLET_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("deep_violet_crystal_ore", () -> new BlockItem(DEEP_VIOLET_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> ARCTIC_BLUE_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("arctic_blue_crystal_ore", () -> new BlockItem(ARCTIC_BLUE_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROSE_PINK_KYBER_CRYSTAL_ORE_ITEM = ITEMS.register("rose_pink_crystal_ore", () -> new BlockItem(ROSE_PINK_CRYSTAL_ORE.get(), new Item.Properties()));

    public static final RegistryObject<Block> TEMPLE_STONE = BLOCKS.register("temple_stone", TempleStone::new);
    public static final RegistryObject<Item> TEMPLE_STONE_ITEM = ITEMS.register("temple_stone", () -> new BlockItem(TEMPLE_STONE.get(), new Item.Properties()));

    // MUSHROOM STEM
    public static final RegistryObject<Block> MUSHROOM_STEM = BLOCKS.register("mushroom_stem", () -> new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.MUSHROOM_STEM)));
    public static final RegistryObject<Item> MUSHROOM_STEM_ITEM = ITEMS.register("mushroom_stem", () -> new BlockItem(MUSHROOM_STEM.get(), new Item.Properties()));
    public static final RegistryObject<Block> PINK_MUSHROOM_BLOCK = BLOCKS.register("pink_mushroom_block", () -> new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK)));
    public static final RegistryObject<Item> PINK_MUSHROOM_BLOCK_ITEM = ITEMS.register("pink_mushroom_block", () -> new BlockItem(PINK_MUSHROOM_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> WHITE_MUSHROOM_BLOCK = BLOCKS.register("white_mushroom_block", () -> new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM_BLOCK)));
    public static final RegistryObject<Item> WHITE_MUSHROOM_BLOCK_ITEM = ITEMS.register("white_mushroom_block", () -> new BlockItem(WHITE_MUSHROOM_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> MUSHROOM_BLOCK_INSIDE = BLOCKS.register("mushroom_block_inside", () -> new HugeMushroomBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM_BLOCK)));
    public static final RegistryObject<Item> MUSHROOM_BLOCK_INSIDE_ITEM = ITEMS.register("mushroom_block_inside", () -> new BlockItem(MUSHROOM_BLOCK_INSIDE.get(), new Item.Properties()));
    public static final RegistryObject<Block> TEMPLE_STONE_PILLAR = BLOCKS.register("temple_stone_pillar", TempleStonePillar::new);
    public static final RegistryObject<Item> TEMPLE_STONE_PILLAR_ITEM = ITEMS.register("temple_stone_pillar", () -> new BlockItem(TEMPLE_STONE_PILLAR.get(), new Item.Properties()));

    public static final RegistryObject<Block> TEMPLE_STONE_HOLOBOOK = BLOCKS.register("temple_stone_holobook", TempleStoneHolobook::new);
    public static final RegistryObject<Item> TEMPLE_STONE_HOLOBOOK_ITEM = ITEMS.register("temple_stone_holobook", () -> new BlockItem(TEMPLE_STONE_HOLOBOOK.get(), new Item.Properties()));

    public static final RegistryObject<Block> TEMPLE_STONE_LIGHTS = BLOCKS.register("temple_stone_lights", TempleStoneHolobook::new);
    public static final RegistryObject<Item> TEMPLE_STONE_LIGHTS_ITEM = ITEMS.register("temple_stone_lights", () -> new BlockItem(TEMPLE_STONE_LIGHTS.get(), new Item.Properties()));


    public static final RegistryObject<Block> TEMPLE_STONE_STAIRS = BLOCKS.register("temple_stone_stairs", () -> new TempleStoneStairs(TEMPLE_STONE.get().defaultBlockState()));
    public static final RegistryObject<Item> TEMPLE_STONE_STAIRS_ITEM = ITEMS.register("temple_stone_stairs", () -> new BlockItem(TEMPLE_STONE_STAIRS.get(), new Item.Properties()));

    public static final RegistryObject<Block> TEMPLE_STONE_SLAB = BLOCKS.register("temple_stone_slab", TempleStoneSlab::new);
    public static final RegistryObject<Item> TEMPLE_STONE_SLAB_ITEM = ITEMS.register("temple_stone_slab", () -> new BlockItem(TEMPLE_STONE_SLAB.get(), new Item.Properties()));
   
    public static final RegistryObject<Block> ASHLA_TEMPLE_STONE = BLOCKS.register("ashla_temple_stone", TempleStone::new);
    public static final RegistryObject<Item> ASHLA_TEMPLE_STONE_ITEM = ITEMS.register("ashla_temple_stone", () -> new BlockItem(ASHLA_TEMPLE_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Block> ASHLA_TEMPLE_STONE_PILLAR = BLOCKS.register("ashla_temple_stone_pillar", TempleStonePillar::new);
    public static final RegistryObject<Item> ASHLA_TEMPLE_STONE_PILLAR_ITEM = ITEMS.register("ashla_temple_stone_pillar", () -> new BlockItem(ASHLA_TEMPLE_STONE_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<Block> ASHLA_TEMPLE_STONE_STAIRS = BLOCKS.register("ashla_temple_stone_stairs", () -> new TempleStoneStairs(ASHLA_TEMPLE_STONE.get().defaultBlockState()));
    public static final RegistryObject<Item> ASHLA_TEMPLE_STONE_STAIRS_ITEM = ITEMS.register("ashla_temple_stone_stairs", () -> new BlockItem(ASHLA_TEMPLE_STONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> ASHLA_TEMPLE_STONE_SLAB = BLOCKS.register("ashla_temple_stone_slab", TempleStoneSlab::new);
    public static final RegistryObject<Item> ASHLA_TEMPLE_STONE_SLAB_ITEM = ITEMS.register("ashla_temple_stone_slab", () -> new BlockItem(ASHLA_TEMPLE_STONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<WallBlock> ASHLA_TEMPLE_STONE_WALL = BLOCKS.register("ashla_temple_stone_wall", TempleStoneWall::new);
    public static final RegistryObject<Item> ASHLA_TEMPLE_STONE_WALL_ITEM = ITEMS.register("ashla_temple_stone_wall", () -> new BlockItem(ASHLA_TEMPLE_STONE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Block> BOGAN_TEMPLE_STONE = BLOCKS.register("bogan_temple_stone", TempleStone::new);
    public static final RegistryObject<Item> BOGANN_TEMPLE_STONE_ITEM = ITEMS.register("bogan_temple_stone", () -> new BlockItem(BOGAN_TEMPLE_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Block> BOGAN_TEMPLE_STONE_PILLAR = BLOCKS.register("bogan_temple_stone_pillar", TempleStonePillar::new);
    public static final RegistryObject<Item> BOGANN_TEMPLE_STONE_PILLAR_ITEM = ITEMS.register("bogan_temple_stone_pillar", () -> new BlockItem(BOGAN_TEMPLE_STONE_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<Block> BOGAN_TEMPLE_STONE_STAIRS = BLOCKS.register("bogan_temple_stone_stairs", () -> new TempleStoneStairs(BOGAN_TEMPLE_STONE.get().defaultBlockState()));
    public static final RegistryObject<Item> BOGANN_TEMPLE_STONE_STAIRS_ITEM = ITEMS.register("bogan_temple_stone_stairs", () -> new BlockItem(BOGAN_TEMPLE_STONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> BOGAN_TEMPLE_STONE_SLAB = BLOCKS.register("bogan_temple_stone_slab", TempleStoneSlab::new);
    public static final RegistryObject<Item> BOGANN_TEMPLE_STONE_SLAB_ITEM = ITEMS.register("bogan_temple_stone_slab", () -> new BlockItem(BOGAN_TEMPLE_STONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<WallBlock> BOGAN_TEMPLE_STONE_WALL = BLOCKS.register("bogan_temple_stone_wall", TempleStoneWall::new);
    public static final RegistryObject<Item> BOGANN_TEMPLE_STONE_WALL_ITEM = ITEMS.register("bogan_temple_stone_wall", () -> new BlockItem(BOGAN_TEMPLE_STONE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_TEMPLE_STONE = BLOCKS.register("tython_temple_stone", TempleStone::new);
    public static final RegistryObject<Item> TYTHON_TEMPLE_STONE_ITEM = ITEMS.register("tython_temple_stone", () -> new BlockItem(TYTHON_TEMPLE_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Block> CHISELED_TYTHON_TEMPLE_STONE = BLOCKS.register("chiseled_tython_temple_stone", ChiseledTempleStoneBlock::new);
    public static final RegistryObject<Item> CHISELED_TYTHON_TEMPLE_STONE_ITEM = ITEMS.register("chiseled_tython_temple_stone", () -> new BlockItem(CHISELED_TYTHON_TEMPLE_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_TEMPLE_STONE_PILLAR = BLOCKS.register("tython_temple_stone_pillar", TempleStonePillar::new);
    public static final RegistryObject<Item> TYTHON_TEMPLE_STONE_PILLAR_ITEM = ITEMS.register("tython_temple_stone_pillar", () -> new BlockItem(TYTHON_TEMPLE_STONE_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_TEMPLE_STONE_LIGHTS = BLOCKS.register("tython_temple_stone_lights", TempleStoneHolobook::new);
    public static final RegistryObject<Item> TYTHON_TEMPLE_STONE_LIGHTS_ITEM = ITEMS.register("tython_temple_stone_lights", () -> new BlockItem(TYTHON_TEMPLE_STONE_LIGHTS.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_TEMPLE_STONE_STAIRS = BLOCKS.register("tython_temple_stone_stairs", () -> new TempleStoneStairs(TYTHON_TEMPLE_STONE.get().defaultBlockState()));
    public static final RegistryObject<Item> TYTHON_TEMPLE_STONE_STAIRS_ITEM = ITEMS.register("tython_temple_stone_stairs", () -> new BlockItem(TYTHON_TEMPLE_STONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_TEMPLE_STONE_SLAB = BLOCKS.register("tython_temple_stone_slab", TempleStoneSlab::new);
    public static final RegistryObject<Item> TYTHON_TEMPLE_STONE_SLAB_ITEM = ITEMS.register("tython_temple_stone_slab", () -> new BlockItem(TYTHON_TEMPLE_STONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<WallBlock> TYTHON_TEMPLE_STONE_WALL = BLOCKS.register("tython_temple_stone_wall", TempleStoneWall::new);
    public static final RegistryObject<Item> TYTHONN_TEMPLE_STONE_WALL_ITEM = ITEMS.register("tython_temple_stone_wall", () -> new BlockItem(TYTHON_TEMPLE_STONE_WALL.get(), new Item.Properties()));

    public static final RegistryObject<Block> DARK_TEMPLE_STONE = BLOCKS.register("dark_temple_stone", DarkTempleStone::new);
    public static final RegistryObject<Item> DARK_TEMPLE_STONE_ITEM = ITEMS.register("dark_temple_stone", () -> new BlockItem(DARK_TEMPLE_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHITE_OBSIDIAN = BLOCKS.register("malachite_obsidian", DarkTempleStone::new);
    public static final RegistryObject<Item> MALACHITE_OBSIDIAN_ITEM = ITEMS.register("malachite_obsidian", () -> new BlockItem(MALACHITE_OBSIDIAN.get(), new Item.Properties()));
    public static final RegistryObject<Block> DARK_TEMPLE_STONE_LIGHTS = BLOCKS.register("dark_temple_stone_lights", TempleStoneHolobook::new);
    public static final RegistryObject<Item> DARK_TEMPLE_STONE_LIGHTS_ITEM = ITEMS.register("dark_temple_stone_lights", () -> new BlockItem(DARK_TEMPLE_STONE_LIGHTS.get(), new Item.Properties()));

    public static final RegistryObject<Block> DARK_TEMPLE_STONE_PILLAR = BLOCKS.register("dark_temple_stone_pillar", DarkTempleStonePillar::new);
    public static final RegistryObject<Item> DARK_TEMPLE_STONE_PILLAR_ITEM = ITEMS.register("dark_temple_stone_pillar", () -> new BlockItem(DARK_TEMPLE_STONE_PILLAR.get(), new Item.Properties()));

    public static final RegistryObject<Block> DARK_TEMPLE_STONE_HOLOBOOK = BLOCKS.register("dark_temple_stone_holobook", DarkTempleStoneHolobook::new);
    public static final RegistryObject<Item> DARK_TEMPLE_STONE_HOLOBOOK_ITEM = ITEMS.register("dark_temple_stone_holobook", () -> new BlockItem(DARK_TEMPLE_STONE_HOLOBOOK.get(), new Item.Properties()));

    public static final RegistryObject<Block> DARK_TEMPLE_STONE_STAIRS = BLOCKS.register("dark_temple_stone_stairs", () -> new DarkTempleStoneStairs(DARK_TEMPLE_STONE.get().defaultBlockState()));
    public static final RegistryObject<Item> DARK_TEMPLE_STONE_STAIRS_ITEM = ITEMS.register("dark_temple_stone_stairs", () -> new BlockItem(DARK_TEMPLE_STONE_STAIRS.get(), new Item.Properties()));

    public static final RegistryObject<Block> DARK_TEMPLE_STONE_SLAB = BLOCKS.register("dark_temple_stone_slab", DarkTempleStoneSlab::new);
    public static final RegistryObject<Item> DARK_TEMPLE_STONE_SLAB_ITEM = ITEMS.register("dark_temple_stone_slab", () -> new BlockItem(DARK_TEMPLE_STONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLEEDING_TABLE = BLOCKS.register("bleeding_table", BleedingTable::new);
    public static final RegistryObject<Item> BLEEDING_TABLE_ITEM = ITEMS.register("bleeding_table", () -> new BlockItem(BLEEDING_TABLE.get(), new Item.Properties()));

    public static final RegistryObject<Item> SHII_CHO_HOLOBOOK = ITEMS.register("shii_cho_holobook",
            () -> new SaberFormHolobookItem("Shii-Cho", new Item.Properties()));

    public static final RegistryObject<Item> MAKASHI_HOLBOOK = ITEMS.register("makashi_holobook",
            () -> new SaberFormHolobookItem("Makashi", new Item.Properties()));

    public static final RegistryObject<Item> SORESU_HOLOBOOK = ITEMS.register("soresu_holobook",
            () -> new SaberFormHolobookItem("Soresu", new Item.Properties()));

    public static final RegistryObject<Item> ATARU_HOLOBOOK = ITEMS.register("ataru_holobook",
            () -> new SaberFormHolobookItem("Ataru", new Item.Properties()));

    public static final RegistryObject<Item> SHIEN_DJEM_SO_HOLOBOOK = ITEMS.register("shien_djem_so_holobook",
            () -> new SaberFormHolobookItem("Shien / Djem So", new Item.Properties()));

    public static final RegistryObject<Item> NIMAN_HOLOBOOK = ITEMS.register("niman_holobook",
            () -> new SaberFormHolobookItem("Niman", new Item.Properties()));

    public static final RegistryObject<Item> JUYO_VAAPAD_HOLOBOOK = ITEMS.register("juyo_vaapad_holobook",
            () -> new SaberFormHolobookItem("Juyo / Vaapad", new Item.Properties()));
    public static final RegistryObject<Block> JEDI_HOLOCRON = BLOCKS.register("jedi_holocron", Holocron::new);
    public static final RegistryObject<Item> JEDI_HOLOCRON_ITEM = ITEMS.register("jedi_holocron", () -> new BlockItem(JEDI_HOLOCRON.get(), new Item.Properties()));
    public static final RegistryObject<Block> ANCIENT_HOLOCRON = BLOCKS.register("ancient_holocron", Holocron::new);
    public static final RegistryObject<Item> ANCIENT_HOLOCRON_ITEM = ITEMS.register("ancient_holocron", () -> new BlockItem(ANCIENT_HOLOCRON.get(), new Item.Properties()));

    public static final RegistryObject<Block> SITH_HOLOCRON = BLOCKS.register("sith_holocron", Holocron::new);
    public static final RegistryObject<Item> SITH_HOLOCRON_ITEM = ITEMS.register("sith_holocron", () -> new BlockItem(SITH_HOLOCRON.get(), new Item.Properties()));

    public static final RegistryObject<Block> JEDI_COFFIN = BLOCKS.register("jedi_coffin", () -> new JediCoffinBlock(BlockBehaviour.Properties.of().strength(2.5f)));
    public static final RegistryObject<Item> JEDI_COFFIN_ITEM = ITEMS.register("jedi_coffin", () -> new BlockItem(JEDI_COFFIN.get(), new Item.Properties()));
    public static final RegistryObject<Block> SITH_COFFIN = BLOCKS.register("sith_coffin", () -> new SithCoffinBlock(BlockBehaviour.Properties.of().strength(3.0f)));
    public static final RegistryObject<Item> SITH_COFFIN_ITEM = ITEMS.register("sith_coffin", () -> new BlockItem(SITH_COFFIN.get(), new Item.Properties()));

    public static final RegistryObject<Block> JEDI_GUARD_STATUE = BLOCKS.register("jedi_guard_statue", JediGuard::new);
    public static final RegistryObject<Item> JEDI_GUARD_STATUE_ITEM = ITEMS.register("jedi_guard_statue", () -> new BlockItem(JEDI_GUARD_STATUE.get(), new Item.Properties()));
// server.galaxyunderchaos.registry.ModBlockEntities

    public static final RegistryObject<Block> GROUND_SABER_STAND = BLOCKS.register("ground_lightsaber_stand", () -> new GroundSaberStandBlock(BlockBehaviour.Properties.of().strength(2.0f).noOcclusion()));
    public static final RegistryObject<Item> GROUND_SABER_STAND_ITEM = ITEMS.register("ground_lightsaber_stand", () -> new BlockItem(GROUND_SABER_STAND.get(), new Item.Properties()));

    public static final RegistryObject<Block> WHITE_GROUND_SABER_STAND = BLOCKS.register("white_ground_lightsaber_stand", () -> new GroundSaberStandBlock(BlockBehaviour.Properties.of().strength(2.0f).noOcclusion()));
    public static final RegistryObject<Item> WHITE_GROUND_SABER_STAND_ITEM = ITEMS.register("white_ground_lightsaber_stand", () -> new BlockItem(WHITE_GROUND_SABER_STAND.get(), new Item.Properties()));

    public static final RegistryObject<Block> TYTHON_JEDI_IDLE_HEAD_STATUE = BLOCKS.register("tython_jedi_idle_head_statue", TythonJediStatueHEAD::new);
    public static final RegistryObject<Item> TYTHON_JEDI_IDLE_HEAD_STATUE_ITEM = ITEMS.register("tython_jedi_idle_head_statue", () -> new BlockItem(TYTHON_JEDI_IDLE_HEAD_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_JEDI_IDLE_TORSO_STATUE = BLOCKS.register("tython_jedi_idle_torso_statue", TythonJediStatueTORSO::new);
    public static final RegistryObject<Item> TYTHON_JEDI_IDLE_TORSO_STATUE_ITEM = ITEMS.register("tython_jedi_idle_torso_statue", () -> new BlockItem(TYTHON_JEDI_IDLE_TORSO_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_JEDI_IDLE_LEG_1_STATUE = BLOCKS.register("tython_jedi_idle_leg_1_statue", TythonJediStatueLEG::new);
    public static final RegistryObject<Item> TYTHON_JEDI_IDLE_LEG_1_STATUE_ITEM = ITEMS.register("tython_jedi_idle_leg_1_statue", () -> new BlockItem(TYTHON_JEDI_IDLE_LEG_1_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_JEDI_IDLE_LEG_2_STATUE = BLOCKS.register("tython_jedi_idle_leg_2_statue", TythonJediStatueLEG::new);
    public static final RegistryObject<Item> TYTHON_JEDI_IDLE_LEGS_2_STATUE_ITEM = ITEMS.register("tython_jedi_idle_leg_2_statue", () -> new BlockItem(TYTHON_JEDI_IDLE_LEG_2_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_JEDI_CROSSED_TORSO_STATUE = BLOCKS.register("tython_jedi_crossed_torso_statue", TythonJediStatueCTORSO::new);
    public static final RegistryObject<Item> TYTHON_JEDI_CROSSED_TORSO_STATUE_ITEM = ITEMS.register("tython_jedi_crossed_torso_statue", () -> new BlockItem(TYTHON_JEDI_CROSSED_TORSO_STATUE.get(), new Item.Properties()));


    public static final RegistryObject<Block> SITH_GUARD_STATUE = BLOCKS.register("sith_guard_statue", SithGuard::new);
    public static final RegistryObject<Item> SITH_GUARD_STATUE_ITEM = ITEMS.register("sith_guard_statue", () -> new BlockItem(SITH_GUARD_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> LIGHTSABER_CRAFTING_TABLE = BLOCKS.register("lightsaber_crafting_table", () -> new LightsaberCraftingTableBlock());
    public static final RegistryObject<Item> LIGHTSABER_CRAFTING_TABLE_ITEM = ITEMS.register("lightsaber_crafting_table", () -> new BlockItem(LIGHTSABER_CRAFTING_TABLE.get(), new Item.Properties()));

    public static final RegistryObject<Block> ILUM_STONE = BLOCKS.register("ilum_stone", AncientTempleStone::new);
    public static final RegistryObject<Item> ILUM_STONE_ITEM = ITEMS.register("ilum_stone", () -> new BlockItem(ILUM_STONE.get(), new Item.Properties()));

    public static final RegistryObject<Block> ANCIENT_TEMPLE_STONE = BLOCKS.register("ancient_temple_stone", AncientTempleStone::new);
    public static final RegistryObject<Item> ANCIENT_TEMPLE_STONE_ITEM = ITEMS.register("ancient_temple_stone", () -> new BlockItem(ANCIENT_TEMPLE_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Block> ANCIENT_TEMPLE_STONE_CRACKED = BLOCKS.register("ancient_temple_stone_cracked", AncientTempleStoneCracked::new);
    public static final RegistryObject<Item> ANCIENT_TEMPLE_STONE_CRACKED_ITEM = ITEMS.register("ancient_temple_stone_cracked", () -> new BlockItem(ANCIENT_TEMPLE_STONE_CRACKED.get(), new Item.Properties()));
    public static final RegistryObject<Block> ANCIENT_TEMPLE_STONE_PILLAR = BLOCKS.register("ancient_temple_stone_pillar", AncientTempleStonePillar::new);
    public static final RegistryObject<Item> ANCIENT_TEMPLE_STONE_PILLAR_ITEM = ITEMS.register("ancient_temple_stone_pillar", () -> new BlockItem(ANCIENT_TEMPLE_STONE_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<Block> ANCIENT_TEMPLE_STONE_HOLOBOOK = BLOCKS.register("ancient_temple_stone_holobook", AncientTempleStoneHolobook::new);
    public static final RegistryObject<Item> ANCIENT_TEMPLE_STONE_HOLOBOOK_ITEM = ITEMS.register("ancient_temple_stone_holobook", () -> new BlockItem(ANCIENT_TEMPLE_STONE_HOLOBOOK.get(), new Item.Properties()));
    public static final RegistryObject<Block> ANCIENT_TEMPLE_STONE_STAIRS = BLOCKS.register("ancient_temple_stone_stairs", () -> new AncientTempleStoneStairs(ANCIENT_TEMPLE_STONE.get().defaultBlockState()));
    public static final RegistryObject<Item> ANCIENT_TEMPLE_STONE_STAIRS_ITEM = ITEMS.register("ancient_temple_stone_stairs", () -> new BlockItem(ANCIENT_TEMPLE_STONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> ANCIENT_TEMPLE_STONE_SLAB = BLOCKS.register("ancient_temple_stone_slab", AncientTempleStoneSlab::new);
    public static final RegistryObject<Item> ANCIENT_TEMPLE_STONE_SLAB_ITEM = ITEMS.register("ancient_temple_stone_slab", () -> new BlockItem(ANCIENT_TEMPLE_STONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_GRASS = BLOCKS.register("tython_grass", TythonGrass::new);
    public static final RegistryObject<Item> TYTHON_GRASS_ITEM = ITEMS.register("tython_grass", () -> new BlockItem(TYTHON_GRASS.get(), new Item.Properties()));
    public static final RegistryObject<Block> TYTHON_DIRT = BLOCKS.register("tython_dirt", TythonDirt::new);
    public static final RegistryObject<Item> TYTHON_DIRT_ITEM = ITEMS.register("tython_dirt", () -> new BlockItem(TYTHON_DIRT.get(), new Item.Properties()));
    public static final RegistryObject<WallBlock> ANCIENT_TEMPLE_STONE_WALL = BLOCKS.register("ancient_temple_stone_wall", AncientTempleStoneWall::new);
    public static final RegistryObject<Item> ANCIENT_TEMPLE_STONE_WALL_ITEM = ITEMS.register("ancient_temple_stone_wall", () -> new BlockItem(ANCIENT_TEMPLE_STONE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<WallBlock> TEMPLE_STONE_WALL = BLOCKS.register("temple_stone_wall", TempleStoneWall::new);
    public static final RegistryObject<Item> TEMPLE_STONE_WALL_ITEM = ITEMS.register("temple_stone_wall", () -> new BlockItem(TEMPLE_STONE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<WallBlock> DARK_TEMPLE_STONE_WALL = BLOCKS.register("dark_temple_stone_wall", DarkTempleStoneWall::new);
    public static final RegistryObject<Item> DARK_TEMPLE_STONE_WALL_ITEM = ITEMS.register("dark_temple_stone_wall", () -> new BlockItem(DARK_TEMPLE_STONE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLBA_SAPLING = BLOCKS.register("blba_sapling", () -> new SaplingBlock(ModTreeGrowers.BLBA, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Item> BLBA_SAPLING_ITEM = ITEMS.register("blba_sapling", () -> new BlockItem(BLBA_SAPLING.get(), new Item.Properties()));
    // #ITEMS

    public static final RegistryObject<Item> SHUURA = ITEMS.register("shuura", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(4).saturationMod(2f).build())));
    public static final RegistryObject<Item> HEART_BERRY = ITEMS.register("heart_berry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(8).saturationMod(2f).build())));
    public static final RegistryObject<Item> JEDI_HOLOBOOK = ITEMS.register("jedi_holobook", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_HOLOBOOK = ITEMS.register("ancient_holobook", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SITH_HOLOBOOK = ITEMS.register("sith_holobook", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_KYBER = ITEMS.register("red_kyber", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BLOOD_ORANGE_KYBER = ITEMS.register("blood_orange_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AMBER_KYBER = ITEMS.register("amber_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_KYBER = ITEMS.register("gold_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_BLUE_KYBER = ITEMS.register("light_blue_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DARK_BLUE_KYBER = ITEMS.register("dark_blue_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAROON_KYBER = ITEMS.register("maroon_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DEEP_VIOLET_KYBER = ITEMS.register("deep_violet_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ARCTIC_BLUE_KYBER = ITEMS.register("arctic_blue_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROSE_PINK_KYBER = ITEMS.register("rose_pink_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_KYBER = ITEMS.register("blue_kyber", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ORANGE_KYBER = ITEMS.register("orange_kyber", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GREEN_KYBER = ITEMS.register("green_kyber", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> YELLOW_KYBER = ITEMS.register("yellow_kyber", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHROMIUM_INGOT = ITEMS.register("chromium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORTAL_ITEM = ITEMS.register("portal_item", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NAVIGATION_COMPUTER = ITEMS.register("navigation_computer", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> REACTOR_ASSEMBLY = ITEMS.register("reactor_assembly", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TITANIUM_CHROMIUM_INGOT = ITEMS.register("titanium_chromium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CYAN_KYBER = ITEMS.register("cyan_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WHITE_KYBER = ITEMS.register("white_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGENTA_KYBER = ITEMS.register("magenta_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PURPLE_KYBER = ITEMS.register("purple_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PINK_KYBER = ITEMS.register("pink_kyber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIME_GREEN_KYBER = ITEMS.register("lime_green_kyber", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TURQUOISE_KYBER = ITEMS.register("turquoise_kyber", () -> new Item(new Item.Properties()));


public static final RegistryObject<Item> FOCUSING_CRYSTAL_COMPRESSED = ITEMS.register("focusing_crystal_compressed",
        () -> new BladeModifierCrystalItem(server.galaxyunderchaos.lightsaber.BladeModifierCrystal.COMPRESSED, new Item.Properties()));
public static final RegistryObject<Item> FOCUSING_CRYSTAL_CRACKED = ITEMS.register("focusing_crystal_cracked",
        () -> new BladeModifierCrystalItem(server.galaxyunderchaos.lightsaber.BladeModifierCrystal.CRACKED, new Item.Properties()));
public static final RegistryObject<Item> FOCUSING_CRYSTAL_INVERTING = ITEMS.register("focusing_crystal_inverting",
        () -> new BladeModifierCrystalItem(server.galaxyunderchaos.lightsaber.BladeModifierCrystal.INVERTING, new Item.Properties()));
public static final RegistryObject<Item> FOCUSING_CRYSTAL_FINE_CUT = ITEMS.register("focusing_crystal_fine_cut",
        () -> new BladeModifierCrystalItem(server.galaxyunderchaos.lightsaber.BladeModifierCrystal.FINE_CUT, new Item.Properties()));
public static final RegistryObject<Item> FOCUSING_CRYSTAL_PRISMATIC = ITEMS.register("focusing_crystal_prismatic",
        () -> new BladeModifierCrystalItem(server.galaxyunderchaos.lightsaber.BladeModifierCrystal.PRISMATIC, new Item.Properties()));


    public static final RegistryObject<Item> TYTHON_PORTAL_ITEM = ITEMS.register("tython_portal",
            () -> new TythonPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MUSTAFAR_PORTAL_ITEM = ITEMS.register("mustafar_portal",
            () -> new MustafarPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NABOO_PORTAL_ITEM = ITEMS.register("naboo_portal",
            () -> new NabooPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ILUM_PORTAL_ITEM = ITEMS.register("ilum_portal",
            () -> new IlumPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> OSSUS_PORTAL_ITEM = ITEMS.register("ossus_portal",
            () -> new OssusPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MALACHOR_PORTAL_ITEM = ITEMS.register("malachor_portal",
            () -> new MalachorPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KORRIBAN_PORTAL_ITEM = ITEMS.register("korriban_portal",
            () -> new KorribanPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BOGAN_PORTAL_ITEM = ITEMS.register("bogan_portal",
            () -> new BoganPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ASHLA_PORTAL_ITEM = ITEMS.register("ashla_portal",
            () -> new AshlaPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DANTOOINE_PORTAL_ITEM = ITEMS.register("dantooine_portal",
            () -> new DantooinePortalItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ACID_FORGED_PLATE = ITEMS.register("acid_forged_plate",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ACIDIC_VENOM_SAC = ITEMS.register("acidic_venom_sac",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILK_THREAD = ITEMS.register("silk_thread",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TEMPLE_GUARD_FABRIC = ITEMS.register("temple_guard_fabric",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TEMPLE_GUARD_HELMET = ITEMS.register("temple_guard_helmet",
            () -> new ArmorItem(CustomArmor.TEMPLE_GUARD_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties()));
    public static final RegistryObject<Item> TEMPLE_GUARD_CHESTPLATE = ITEMS.register("temple_guard_chestplate",
            () -> new ArmorItem(CustomArmor.TEMPLE_GUARD_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties()));
    public static final RegistryObject<Item> TEMPLE_GUARD_LEGGINGS = ITEMS.register("temple_guard_leggings",
            () -> new ArmorItem(CustomArmor.TEMPLE_GUARD_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties()));
    public static final RegistryObject<Item> TEMPLE_GUARD_BOOTS = ITEMS.register("temple_guard_boots",
            () -> new ArmorItem(CustomArmor.TEMPLE_GUARD_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties()));
    public static final RegistryObject<Item> CHITIN_FRAGMENTS = ITEMS.register("chitin_fragments",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LOST_HILT = ITEMS.register("lost_hilt",
            () -> new HiltItem("green", new Item.Properties()));
    public static final RegistryObject<Item> AEGIS_HILT = ITEMS.register("aegis_hilt",
            () -> new HiltItem("orange", new Item.Properties()));
    public static final RegistryObject<Item> APPRENTICE_HILT = ITEMS.register("apprentice_hilt",
            () -> new HiltItem("pink", new Item.Properties()));
    public static final RegistryObject<Item> CHOSEN_HILT = ITEMS.register("chosen_hilt",
            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> EMPEROR_HILT = ITEMS.register("emperor_hilt",
            () -> new HiltItem("red", new Item.Properties()));
    public static final RegistryObject<Item> FALLEN_HILT = ITEMS.register("fallen_hilt",
            () -> new HiltItem("red", new Item.Properties()));
    public static final RegistryObject<Item> GRACE_HILT = ITEMS.register("grace_hilt",
            () -> new HiltItem("red", new Item.Properties()));
    public static final RegistryObject<Item> GUARD_HILT = ITEMS.register("guard_hilt",
            () -> new HiltItem("red", new Item.Properties()));
    public static final RegistryObject<Item> HARMONY_HILT = ITEMS.register("harmony_hilt",
            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> LEGACY_HILT = ITEMS.register("legacy_hilt",
            () -> new HiltItem("green", new Item.Properties()));
    public static final RegistryObject<Item> PADAWAN_HILT = ITEMS.register("padawan_hilt",
            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> RESOLVE_HILT = ITEMS.register("resolve_hilt",
            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> SKUSTELL_HILT = ITEMS.register("skustell_hilt",
            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> TALON_HILT = ITEMS.register("talon_hilt",
            () -> new HiltItem("orange", new Item.Properties()));
    public static final RegistryObject<Item> VALOR_HILT = ITEMS.register("valor_hilt",
            () -> new HiltItem("purple", new Item.Properties()));
    public static final RegistryObject<Item> WISDOM_HILT = ITEMS.register("wisdom_hilt",
            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> NEGOTIATOR_HILT = ITEMS.register("negotiator_hilt",
            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> KNIGHTFALL_HILT = ITEMS.register("knightfall_hilt",
            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> BAROSHE_HILT = ITEMS.register("baroshe_hilt",
            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> ACID_SPIDER_SPAWN_EGG = ITEMS.register("acid_spider_spawn_egg",
            () -> new ForgeSpawnEggItem(galaxyunderchaos.ACID_SPIDER, 0x53524b, 0xdac741, new Item.Properties()));
    public static final RegistryObject<Item> WINGMAW_SPAWN_EGG = ITEMS.register("wingmaw_spawn_egg",
            () -> new ForgeSpawnEggItem(galaxyunderchaos.WINGMAW, 0x53524b, 0xdac741, new Item.Properties()));
    public static final RegistryObject<Block> COUNCIL_CHAIR_1 = BLOCKS.register("council_chair_1", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Block> COUNCIL_CHAIR_2 = BLOCKS.register("council_chair_2", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Block> COUNCIL_CHAIR_3 = BLOCKS.register("council_chair_3", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Block> COUNCIL_CHAIR_4 = BLOCKS.register("council_chair_4", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Block> COUNCIL_CHAIR_5 = BLOCKS.register("council_chair_5", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Item> COUNCIL_CHAIR_1_ITEM = ITEMS.register("council_chair_1", () -> new BlockItem(COUNCIL_CHAIR_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> COUNCIL_CHAIR_2_ITEM = ITEMS.register("council_chair_2", () -> new BlockItem(COUNCIL_CHAIR_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> COUNCIL_CHAIR_3_ITEM = ITEMS.register("council_chair_3", () -> new BlockItem(COUNCIL_CHAIR_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> COUNCIL_CHAIR_4_ITEM = ITEMS.register("council_chair_4", () -> new BlockItem(COUNCIL_CHAIR_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> COUNCIL_CHAIR_5_ITEM = ITEMS.register("council_chair_5", () -> new BlockItem(COUNCIL_CHAIR_5.get(), new Item.Properties()));

    public static final RegistryObject<Block> TYTHON_TEMPLE_CHAIR_1 = BLOCKS.register("tython_temple_chair_1", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Block> TYTHON_TEMPLE_CHAIR_2 = BLOCKS.register("tython_temple_chair_2", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Block> TYTHON_TEMPLE_CHAIR_3 = BLOCKS.register("tython_temple_chair_3", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Block> TYTHON_TEMPLE_CHAIR_4 = BLOCKS.register("tython_temple_chair_4", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Block> TYTHON_TEMPLE_CHAIR_5 = BLOCKS.register("tython_temple_chair_5", () -> new RotatableSittableChairBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));
    public static final RegistryObject<Item> TYTHON_TEMPLE_CHAIR_1_ITEM = ITEMS.register("tython_temple_chair_1", () -> new BlockItem(TYTHON_TEMPLE_CHAIR_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> TYTHON_TEMPLE_CHAIR_2_ITEM = ITEMS.register("tython_temple_chair_2", () -> new BlockItem(TYTHON_TEMPLE_CHAIR_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> TYTHON_TEMPLE_CHAIR_3_ITEM = ITEMS.register("tython_temple_chair_3", () -> new BlockItem(TYTHON_TEMPLE_CHAIR_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> TYTHON_TEMPLE_CHAIR_4_ITEM = ITEMS.register("tython_temple_chair_4", () -> new BlockItem(TYTHON_TEMPLE_CHAIR_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> TYTHON_TEMPLE_CHAIR_5_ITEM = ITEMS.register("tython_temple_chair_5", () -> new BlockItem(TYTHON_TEMPLE_CHAIR_5.get(), new Item.Properties()));


    public static final Map<String, RegistryObject<Item>> LIGHTSABERS = new HashMap<>();
    public static final Map<String, RegistryObject<Item>> LEGACY_HILTS = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Item>> LIGHTSABER_PARTS = new LinkedHashMap<>();
    public static final RegistryObject<Item> CUSTOM_LIGHTSABER = ITEMS.register("custom_lightsaber",
            () -> new LightsaberItem("white", "graflex", new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DOUBLE_LIGHTSABER = ITEMS.register("double_lightsaber",
            () -> new server.galaxyunderchaos.item.DoubleLightsaberItem("white", "mauler", new Item.Properties().stacksTo(1)));

    public static void registerLightsabers() {
        String[] bladeColors = {
                "red", "blue", "green", "yellow", "cyan",
                "white", "magenta", "purple", "pink",
                "lime_green", "turquoise", "orange", "blood_orange", "amber", "gold", "light_blue", "dark_blue", "maroon", "deep_violet", "arctic_blue", "rose_pink"
        };

        String[] hiltNames = {
                "apprentice", "chosen", "emperor", "legacy", "padawan",
                "resolve", "talon", "valor", "wisdom", "lost", "aegis", "grace", "guard", "harmony",
                "skustell", "fallen", "negotiator", "baroshe", "knightfall"
        };

        for (String color : bladeColors) {
            for (String hilt : hiltNames) {
                String id = color + "_" + hilt + "_lightsaber";
                LIGHTSABERS.put(id, ITEMS.register(
                        id,
                        () -> new LightsaberItem(color, new Item.Properties().stacksTo(1))
                ));
            }
        }
    }


    public static void registerAdvancedLightsaberContent() {
        AdvancedLightsaberLegacyHilts.HILTS.values().forEach(spec -> {
            String hiltId = spec.id() + "_hilt";
            LEGACY_HILTS.put(spec.id(), ITEMS.register(hiltId,
                    () -> new HiltItem(spec.id(), spec.legacyDefaultBladeColor(), new Item.Properties().stacksTo(1))));

            for (LightsaberPartType partType : LightsaberPartType.values()) {
                String id = spec.id() + "_" + partType.getSerializedName();
                LIGHTSABER_PARTS.put(id, ITEMS.register(id,
                        () -> new LightsaberPartItem(spec.id(), partType, new Item.Properties().stacksTo(1))));
            }
        });
    }

    public static final RegistryObject<RotatedPillarBlock> HEART_BERRY_LOG = BLOCKS.register("heart_berry_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> HEART_BERRY_WOOD = BLOCKS.register("heart_berry_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_HEART_BERRY_LOG = BLOCKS.register("stripped_heart_berry_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_HEART_BERRY_WOOD = BLOCKS.register("stripped_heart_berry_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> AK_LOG = BLOCKS.register("ak_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> AK_WOOD = BLOCKS.register("ak_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_AK_LOG = BLOCKS.register("stripped_ak_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_AK_WOOD = BLOCKS.register("stripped_ak_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Item> AK_LOG_ITEM               = ITEMS.register("ak_log",
            () -> new BlockItem(AK_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> AK_WOOD_ITEM              = ITEMS.register("ak_wood",
            () -> new BlockItem(AK_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_AK_LOG_ITEM      = ITEMS.register("stripped_ak_log",
            () -> new BlockItem(STRIPPED_AK_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_AK_WOOD_ITEM     = ITEMS.register("stripped_ak_wood",
            () -> new BlockItem(STRIPPED_AK_WOOD.get(), new Item.Properties()));

    public static final RegistryObject<Item> HEART_BERRY_LOG_ITEM           = ITEMS.register("heart_berry_log",
            () -> new BlockItem(HEART_BERRY_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> HEART_BERRY_WOOD_ITEM          = ITEMS.register("heart_berry_wood",
            () -> new BlockItem(HEART_BERRY_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_HEART_BERRY_LOG_ITEM  = ITEMS.register("stripped_heart_berry_log",
            () -> new BlockItem(STRIPPED_HEART_BERRY_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_HEART_BERRY_WOOD_ITEM = ITEMS.register("stripped_heart_berry_wood",
            () -> new BlockItem(STRIPPED_HEART_BERRY_WOOD.get(), new Item.Properties()));

    public static final RegistryObject<Item> RAW_WINGMAW_MEAT = ITEMS.register("raw_wingmaw_meat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.6F)
                            .build())));

    public static final RegistryObject<Item> COOKED_WINGMAW_MEAT = ITEMS.register("cooked_wingmaw_meat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(10)
                            .saturationMod(0.8F)
                            .build())));
    public static final RegistryObject<Item> WINGMAW_HIDE = ITEMS.register("wingmaw_hide",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WINGMAW_FANG = ITEMS.register("wingmaw_fang",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WINGMAW_BLADE = ITEMS.register("wingmaw_blade",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<DaggerItem> WINGMAW_DAGGER = (RegistryObject<DaggerItem>) ITEMS.register("wingmaw_dagger",
            () -> new DaggerItem(new Item.Properties()));
    public static final RegistryObject<Item> WINGMAW_FEATHER = ITEMS.register("wingmaw_feather",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<HiltItem> WINGMAW_HILT = (RegistryObject<HiltItem>) ITEMS.register("wingmaw_hilt",
            () -> new HiltItem("green", new Item.Properties()));

    public static final RegistryObject<Block> AK_PLANKS = BLOCKS.register("ak_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });
    public static final RegistryObject<Block> HEART_BERRY_PLANKS = BLOCKS.register("heart_berry_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final RegistryObject<Block> HEART_BERRY_LEAVES = BLOCKS.register("heart_berry_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });
    public static final RegistryObject<Block> HEART_BERRY_FRUIT_LEAVES = BLOCKS.register("heart_berry_fruit_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });
    public static final RegistryObject<Block> AK_LEAVES = BLOCKS.register("ak_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    public static final BlockSetType AK_BLOCK_SET =
            BlockSetType.register(new BlockSetType("ak"));
    public static final WoodType AK_WOOD_TYPE =
            WoodType.register(new WoodType("ak", AK_BLOCK_SET));
    public static final BlockSetType HEART_BERRY_BLOCK_SET =
            BlockSetType.register(new BlockSetType("heart_berry"));
    public static final WoodType HEART_BERRY_WOOD_TYPE =
            WoodType.register(new WoodType("heart_berry", HEART_BERRY_BLOCK_SET));


    public static final RegistryObject<Block> HEART_BERRY_SAPLING = BLOCKS.register("heart_berry_sapling",
            () -> new SaplingBlock(ModTreeGrowers.HEART_BERRY_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> AK_SAPLING = BLOCKS.register("ak_sapling",
            () -> new SaplingBlock(ModTreeGrowers.AK_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Item> HEART_BERRY_PLANKS_ITEM        = ITEMS.register("heart_berry_planks",
            () -> new BlockItem(HEART_BERRY_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> HEART_BERRY_LEAVES_ITEM        = ITEMS.register("heart_berry_leaves",
            () -> new BlockItem(HEART_BERRY_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> HEART_BERRY_FRUIT_LEAVES_ITEM  = ITEMS.register("heart_berry_fruit_leaves",
            () -> new BlockItem(HEART_BERRY_FRUIT_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> HEART_BERRY_SAPLING_ITEM       = ITEMS.register("heart_berry_sapling",
            () -> new BlockItem(HEART_BERRY_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> AK_PLANKS_ITEM            = ITEMS.register("ak_planks",
            () -> new BlockItem(AK_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> AK_LEAVES_ITEM            = ITEMS.register("ak_leaves",
            () -> new BlockItem(AK_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> AK_SAPLING_ITEM           = ITEMS.register("ak_sapling",
            () -> new BlockItem(AK_SAPLING.get(), new Item.Properties()));

    public static final RegistryObject<Block> AK_DOOR_BLOCK = BLOCKS.register(
            "ak_door", () -> new TreeDoor(AK_BLOCK_SET));
    public static final RegistryObject<Item> AK_DOOR_ITEM = ITEMS.register(
            "ak_door", () -> new BlockItem(AK_DOOR_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Block> AK_TRAPDOOR_BLOCK = BLOCKS.register(
            "ak_trapdoor", () -> new TreeTrapdoor(AK_BLOCK_SET));
    public static final RegistryObject<Item> AK_TRAPDOOR_ITEM = ITEMS.register(
            "ak_trapdoor", () -> new BlockItem(AK_TRAPDOOR_BLOCK.get(), new Item.Properties()));

    // correct registration
    public static final RegistryObject<FenceGateBlock> AK_FENCE_GATE = BLOCKS.register(
            "ak_fence_gate",
            () -> new FenceGateBlock(
                    BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),
                    WoodType.ACACIA
            ));

    public static final RegistryObject<Item> AK_FENCE_GATE_ITEM = ITEMS.register(
            "ak_fence_gate",
            () -> new BlockItem(AK_FENCE_GATE.get(), new Item.Properties()));

    public static final RegistryObject<Block> AK_FENCE_BLOCK = BLOCKS.register(
            "ak_fence", TreeFence::new);
    public static final RegistryObject<Item> AK_FENCE_ITEM = ITEMS.register(
            "ak_fence", () -> new BlockItem(AK_FENCE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> AK_STAIRS = BLOCKS.register("ak_stairs", () -> new TreeStairs(AK_PLANKS.get().defaultBlockState()));
    public static final RegistryObject<Item> AK_STAIRS_ITEM = ITEMS.register("ak_stairs", () -> new BlockItem(AK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> AK_SLAB = BLOCKS.register("ak_slab", TreeSlab::new);
    public static final RegistryObject<Item> AK_SLAB_ITEM = ITEMS.register("ak_slab", () -> new BlockItem(AK_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<Block> AK_PRESSURE_PLATE = BLOCKS.register(
            "ak_pressure_plate", () -> new TreePressurePlate(AK_BLOCK_SET));
    public static final RegistryObject<Item>  AK_PRESSURE_PLATE_ITEM = ITEMS.register(
            "ak_pressure_plate", () -> new BlockItem(AK_PRESSURE_PLATE.get(), new Item.Properties()));

    public static final RegistryObject<Block> AK_BUTTON = BLOCKS.register(
            "ak_button", () -> new TreeButton(AK_BLOCK_SET));
    public static final RegistryObject<Item>  AK_BUTTON_ITEM = ITEMS.register(
            "ak_button", () -> new BlockItem(AK_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Block> AK_SIGN = BLOCKS.register(
            "ak_sign", () -> new TreeStandingSign(AK_WOOD_TYPE));
    public static final RegistryObject<Block> AK_WALL_SIGN = BLOCKS.register(
            "ak_wall_sign", () -> new TreeWallSign(AK_WOOD_TYPE));
    public static final RegistryObject<Item> AK_SIGN_ITEM = ITEMS.register(
            "ak_sign", () -> new SignItem(new Item.Properties(), AK_SIGN.get(), AK_WALL_SIGN.get()));

    // hanging signs
    public static final RegistryObject<Block> AK_HANGING_SIGN = BLOCKS.register(
            "ak_hanging_sign", () -> new TreeHangingSign(AK_WOOD_TYPE));
    public static final RegistryObject<Block> AK_WALL_HANGING_SIGN = BLOCKS.register(
            "ak_wall_hanging_sign", () -> new TreeWallHangingSign(AK_WOOD_TYPE));
    public static final RegistryObject<Item> AK_HANGING_SIGN_ITEM = ITEMS.register(
            "ak_hanging_sign", () -> new HangingSignItem(AK_HANGING_SIGN.get(), AK_WALL_HANGING_SIGN.get(), new Item.Properties()));

    public static final RegistryObject<Item> AK_BOAT = ITEMS.register("ak_boat",
            () -> new ModBoatItem(false, ModEntityTypes.AK_BOAT::get, new Item.Properties()));
    public static final RegistryObject<Item> AK_CHEST_BOAT = ITEMS.register("ak_chest_boat",
            () -> new ModBoatItem(false, ModEntityTypes.AK_CHEST_BOAT::get, new Item.Properties()));


    public static final RegistryObject<Block> HEART_BERRY_DOOR_BLOCK = BLOCKS.register(
            "heart_berry_door", () -> new TreeDoor(HEART_BERRY_BLOCK_SET));
    public static final RegistryObject<Item> HEART_BERRY_DOOR_ITEM = ITEMS.register(
            "heart_berry_door", () -> new BlockItem(HEART_BERRY_DOOR_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Block> HEART_BERRY_TRAPDOOR_BLOCK = BLOCKS.register(
            "heart_berry_trapdoor", () -> new TreeTrapdoor(HEART_BERRY_BLOCK_SET));
    public static final RegistryObject<Item> HEART_BERRY_TRAPDOOR_ITEM = ITEMS.register(
            "heart_berry_trapdoor", () -> new BlockItem(HEART_BERRY_TRAPDOOR_BLOCK.get(), new Item.Properties()));

    // correct registration
    public static final RegistryObject<FenceGateBlock> HEART_BERRY_FENCE_GATE = BLOCKS.register(
            "heart_berry_fence_gate",
            () -> new FenceGateBlock(
                    BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),
                    WoodType.ACACIA
            ));

    public static final RegistryObject<Item> HEART_BERRY_FENCE_GATE_ITEM = ITEMS.register(
            "heart_berry_fence_gate",
            () -> new BlockItem(HEART_BERRY_FENCE_GATE.get(), new Item.Properties()));

    public static final RegistryObject<Block> HEART_BERRY_FENCE_BLOCK = BLOCKS.register(
            "heart_berry_fence", TreeFence::new);
    public static final RegistryObject<Item> HEART_BERRY_FENCE_ITEM = ITEMS.register(
            "heart_berry_fence", () -> new BlockItem(HEART_BERRY_FENCE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> HEART_BERRY_STAIRS = BLOCKS.register("heart_berry_stairs", () -> new TreeStairs(HEART_BERRY_PLANKS.get().defaultBlockState()));
    public static final RegistryObject<Item> HEART_BERRY_STAIRS_ITEM = ITEMS.register("heart_berry_stairs", () -> new BlockItem(HEART_BERRY_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> HEART_BERRY_SLAB = BLOCKS.register("heart_berry_slab", TreeSlab::new);
    public static final RegistryObject<Item> HEART_BERRY_SLAB_ITEM = ITEMS.register("heart_berry_slab", () -> new BlockItem(HEART_BERRY_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<Block> HEART_BERRY_PRESSURE_PLATE = BLOCKS.register(
            "heart_berry_pressure_plate", () -> new TreePressurePlate(HEART_BERRY_BLOCK_SET));
    public static final RegistryObject<Item>  HEART_BERRY_PRESSURE_PLATE_ITEM = ITEMS.register(
            "heart_berry_pressure_plate", () -> new BlockItem(HEART_BERRY_PRESSURE_PLATE.get(), new Item.Properties()));

    public static final RegistryObject<Block> HEART_BERRY_BUTTON = BLOCKS.register(
            "heart_berry_button", () -> new TreeButton(HEART_BERRY_BLOCK_SET));
    public static final RegistryObject<Item>  HEART_BERRY_BUTTON_ITEM = ITEMS.register(
            "heart_berry_button", () -> new BlockItem(HEART_BERRY_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Block> HEART_BERRY_SIGN = BLOCKS.register(
            "heart_berry_sign", () -> new TreeStandingSignHB(HEART_BERRY_WOOD_TYPE));
    public static final RegistryObject<Block> HEART_BERRY_WALL_SIGN = BLOCKS.register(
            "heart_berry_wall_sign", () -> new TreeWallSignHB(HEART_BERRY_WOOD_TYPE));
    public static final RegistryObject<Item> HEART_BERRY_SIGN_ITEM = ITEMS.register(
            "heart_berry_sign", () -> new SignItem(new Item.Properties(), HEART_BERRY_SIGN.get(), HEART_BERRY_WALL_SIGN.get()));

    public static final RegistryObject<Block> HEART_BERRY_HANGING_SIGN = BLOCKS.register(
            "heart_berry_hanging_sign", () -> new TreeHangingSignHB(HEART_BERRY_WOOD_TYPE));
    public static final RegistryObject<Block> HEART_BERRY_WALL_HANGING_SIGN = BLOCKS.register(
            "heart_berry_wall_hanging_sign", () -> new TreeWallHangingSignHB(HEART_BERRY_WOOD_TYPE));
    public static final RegistryObject<Item> HEART_BERRY_HANGING_SIGN_ITEM = ITEMS.register(
            "heart_berry_hanging_sign", () -> new HangingSignItem(HEART_BERRY_HANGING_SIGN.get(), HEART_BERRY_WALL_HANGING_SIGN.get(), new Item.Properties()));

    public static final RegistryObject<Item> HEART_BERRY_BOAT = ITEMS.register("heart_berry_boat",
            () -> new ModBoatItem(false, ModEntityTypes.HEART_BERRY_BOAT::get, new Item.Properties()));
    public static final RegistryObject<Item> HEART_BERRY_CHEST_BOAT = ITEMS.register("heart_berry_chest_boat",
            () -> new ModBoatItem(false, ModEntityTypes.HEART_BERRY_CHEST_BOAT::get, new Item.Properties()));

    // #ENTITIES
    public static final RegistryObject<EntityType<AcidSpiderEntity>> ACID_SPIDER =
            ENTITY_TYPES.register("acid_spider", () -> EntityType.Builder.of(AcidSpiderEntity::new, MobCategory.MONSTER)
                    .sized(1.5f, 1.5f).build("acid_spider"));
    public static final RegistryObject<EntityType<WingmawEntity>> WINGMAW =
            ENTITY_TYPES.register("wingmaw", () -> EntityType.Builder.of(WingmawEntity::new, MobCategory.MONSTER)
                    .sized(1.0f, 1.0f).build("wingmaw"));
    public static final RegistryObject<EntityType<SeatEntity>> SEAT =
            ENTITY_TYPES.register("seat",
                    () -> EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                            .sized(0.0001F, 0.0001F)
                            .build("seat"));

    public galaxyunderchaos() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ModBiomes.BIOMES.register(modEventBus);
        CreativeMenuTabs.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        registerLightsabers();
        registerAdvancedLightsaberContent();
        ModSounds.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(LightsaberBeltRenderer.class);
//        MinecraftForge.EVENT_BUS.register(ForcePowerOverlay.class);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(HyperspaceOverlayRenderer.class);
        ModLootModifiers.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(LightsaberFormEventHandler.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LightsaberFormNetworking.registerPackets(event);
    }
    @Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID,
            bus = Mod.EventBusSubscriber.Bus.MOD,
            value = Dist.CLIENT)
    public class ModClient {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                EntityRenderers.register(ModEntityTypes.AK_BOAT.get(), pContext -> new AkBoatRenderer(pContext, false));
                EntityRenderers.register(ModEntityTypes.AK_CHEST_BOAT.get(), pContext -> new AkBoatRenderer(pContext, true));
                EntityRenderers.register(ModEntityTypes.HEART_BERRY_BOAT.get(), pContext -> new HBBoatRenderer(pContext, false));
                EntityRenderers.register(ModEntityTypes.HEART_BERRY_CHEST_BOAT.get(), pContext -> new HBBoatRenderer(pContext, true));
                Sheets.addWoodType(galaxyunderchaos.AK_WOOD_TYPE);
                Sheets.addWoodType(galaxyunderchaos.HEART_BERRY_WOOD_TYPE);

                BlockEntityRenderers.register(ModBlockEntities.AK_SIGN_BE.get(),    SignRenderer::new);
                BlockEntityRenderers.register(ModBlockEntities.AK_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
                BlockEntityRenderers.register(ModBlockEntities.HEART_BERRY_SIGN_BE.get(),    SignRenderer::new);
                BlockEntityRenderers.register(ModBlockEntities.HEART_BERRY_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
                BlockEntityRenderers.register(ModBlockEntities.SABER_STAND_BE.get(), GroundSaberStandRenderer::new);
                BlockEntityRenderers.register(ModBlockEntities.BLEEDING_TABLE_BE.get(), BleedingTableRenderer::new);

            });
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        EntityRenderers.register(galaxyunderchaos.ACID_SPIDER.get(), AcidSpiderRenderer::new);
        EntityRenderers.register(galaxyunderchaos.WINGMAW.get(), WingmawRenderer::new);
        EntityRenderers.register(galaxyunderchaos.SEAT.get(), SeatRenderer::new);
        event.enqueueWork(() -> {

            ModItemProperties.addCustomItemProperties();
            Minecraft mc = Minecraft.getInstance();

            mc.getEntityRenderDispatcher()
                    .getSkinMap()
                    .values()
                    .forEach(renderer -> {
                        if (renderer instanceof PlayerRenderer pr) {
                            pr.addLayer(new LightsaberFirstPersonLayer(pr));
                        }
                    });
            ItemBlockRenderTypes.setRenderLayer(galaxyunderchaos.GROUND_SABER_STAND.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(galaxyunderchaos.WHITE_GROUND_SABER_STAND.get(), RenderType.cutout());

            galaxyunderchaos.LIGHTSABERS.values()
                    .forEach(reg -> ItemBlockRenderTypes.setRenderLayer(
                            Block.byItem(reg.get()),
                            RenderType.translucent()
                    ));
        });
        MinecraftForge.EVENT_BUS.addListener(ClientEventSubscriber::onRenderTick);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

}