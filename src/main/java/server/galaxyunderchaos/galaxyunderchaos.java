package server.galaxyunderchaos;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import server.galaxyunderchaos.block.*;
import server.galaxyunderchaos.data.KeyBindings;
import server.galaxyunderchaos.effect.ModEffects;
import server.galaxyunderchaos.entity.*;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.event.LightsaberFormEventHandler;
import server.galaxyunderchaos.force.ForceNetworking;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.item.*;
import server.galaxyunderchaos.lightsaber.AdvancedLightsaberLegacyHilts;
import server.galaxyunderchaos.lightsaber.LightsaberFormNetworking;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;
import server.galaxyunderchaos.loot.ModLootModifiers;
import server.galaxyunderchaos.sound.ModSounds;
import server.galaxyunderchaos.ship.ShipNetworking;
import server.galaxyunderchaos.menu.ModMenuTypes;
import server.galaxyunderchaos.worldgen.biome.ModBiomes;
import server.galaxyunderchaos.worldgen.tree.ModTreeGrowers;
import server.galaxyunderchaos.worldgen.structure.ModStructureTypes;

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
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);

    public static final RegistryObject<SimpleParticleType> JEDI_COFFIN_PARTICLE = PARTICLE_TYPES.register("jedi_coffin_particle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SITH_COFFIN_PARTICLE = PARTICLE_TYPES.register("sith_coffin_particle", () -> new SimpleParticleType(false));

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
    public static final RegistryObject<Block> KORRIBAN_TEMPLE_STONE = BLOCKS.register("korriban_temple_stone", TempleStone::new);
    public static final RegistryObject<Item> KORRIBAN_TEMPLE_STONE_ITEM = ITEMS.register("korriban_temple_stone", () -> new BlockItem(KORRIBAN_TEMPLE_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Block> KORRIBAN_TEMPLE_STONE_HOLOBOOK = BLOCKS.register("korriban_temple_stone_holobook", KorribanTempleStoneHolobook::new);
    public static final RegistryObject<Item> KORRIBAN_TEMPLE_STONE_HOLOBOOK_ITEM = ITEMS.register("korriban_temple_stone_holobook", () -> new BlockItem(KORRIBAN_TEMPLE_STONE_HOLOBOOK.get(), new Item.Properties()));
    public static final RegistryObject<Block> KORRIBAN_TEMPLE_STONE_LIGHTS = BLOCKS.register("korriban_temple_stone_lights", TempleStoneHolobook::new);
    public static final RegistryObject<Item> KORRIBAN_TEMPLE_STONE_LIGHTS_ITEM = ITEMS.register("korriban_temple_stone_lights", () -> new BlockItem(KORRIBAN_TEMPLE_STONE_LIGHTS.get(), new Item.Properties()));
    public static final RegistryObject<Block> KORRIBAN_TEMPLE_STONE_PILLAR = BLOCKS.register("korriban_temple_stone_pillar", TempleStonePillar::new);
    public static final RegistryObject<Item> KORRIBAN_TEMPLE_STONE_PILLAR_ITEM = ITEMS.register("korriban_temple_stone_pillar", () -> new BlockItem(KORRIBAN_TEMPLE_STONE_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<Block> KORRIBAN_TEMPLE_STONE_STAIRS = BLOCKS.register("korriban_temple_stone_stairs", () -> new TempleStoneStairs(KORRIBAN_TEMPLE_STONE.get().defaultBlockState()));
    public static final RegistryObject<Item> KORRIBAN_TEMPLE_STONE_STAIRS_ITEM = ITEMS.register("korriban_temple_stone_stairs", () -> new BlockItem(KORRIBAN_TEMPLE_STONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> KORRIBAN_TEMPLE_STONE_SLAB = BLOCKS.register("korriban_temple_stone_slab", TempleStoneSlab::new);
    public static final RegistryObject<Item> KORRIBAN_TEMPLE_STONE_SLAB_ITEM = ITEMS.register("korriban_temple_stone_slab", () -> new BlockItem(KORRIBAN_TEMPLE_STONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<WallBlock> KORRIBAN_TEMPLE_STONE_WALL = BLOCKS.register("korriban_temple_stone_wall", TempleStoneWall::new);
    public static final RegistryObject<Item> KORRIBAN_TEMPLE_STONE_WALL_ITEM = ITEMS.register("korriban_temple_stone_wall", () -> new BlockItem(KORRIBAN_TEMPLE_STONE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHOR_TEMPLE_STONE = BLOCKS.register("malachor_temple_stone", TempleStone::new);
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_ITEM = ITEMS.register("malachor_temple_stone", () -> new BlockItem(MALACHOR_TEMPLE_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHOR_TEMPLE_STONE_CRYSTALLINE = BLOCKS.register("malachor_temple_stone_crystalline", () -> new Block(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).strength(3.0f, 10.0f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_CRYSTALLINE_ITEM = ITEMS.register("malachor_temple_stone_crystalline", () -> new BlockItem(MALACHOR_TEMPLE_STONE_CRYSTALLINE.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHOR_TEMPLE_STONE_GLASS = BLOCKS.register("malachor_temple_stone_glass", MalachorTempleStoneGlassBlock::new);
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_GLASS_ITEM = ITEMS.register("malachor_temple_stone_glass", () -> new BlockItem(MALACHOR_TEMPLE_STONE_GLASS.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHOR_TEMPLE_STONE_GLASS_2 = BLOCKS.register("malachor_temple_stone_glass_2", MalachorTempleStoneGlassBlock::new);
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_GLASS_2_ITEM = ITEMS.register("malachor_temple_stone_glass_2", () -> new BlockItem(MALACHOR_TEMPLE_STONE_GLASS_2.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHOR_TEMPLE_STONE_GLASS_3 = BLOCKS.register("malachor_temple_stone_glass_3", MalachorTempleStoneGlassBlock::new);
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_GLASS_3_ITEM = ITEMS.register("malachor_temple_stone_glass_3", () -> new BlockItem(MALACHOR_TEMPLE_STONE_GLASS_3.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHOR_TEMPLE_STONE_GLASS_4 = BLOCKS.register("malachor_temple_stone_glass_4", MalachorTempleStoneGlassBlock::new);
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_GLASS_4_ITEM = ITEMS.register("malachor_temple_stone_glass_4", () -> new BlockItem(MALACHOR_TEMPLE_STONE_GLASS_4.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHOR_TEMPLE_STONE_PILLAR = BLOCKS.register("malachor_temple_stone_pillar", TempleStonePillar::new);
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_PILLAR_ITEM = ITEMS.register("malachor_temple_stone_pillar", () -> new BlockItem(MALACHOR_TEMPLE_STONE_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHOR_TEMPLE_STONE_STAIRS = BLOCKS.register("malachor_temple_stone_stairs", () -> new TempleStoneStairs(MALACHOR_TEMPLE_STONE.get().defaultBlockState()));
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_STAIRS_ITEM = ITEMS.register("malachor_temple_stone_stairs", () -> new BlockItem(MALACHOR_TEMPLE_STONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> MALACHOR_TEMPLE_STONE_SLAB = BLOCKS.register("malachor_temple_stone_slab", TempleStoneSlab::new);
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_SLAB_ITEM = ITEMS.register("malachor_temple_stone_slab", () -> new BlockItem(MALACHOR_TEMPLE_STONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<WallBlock> MALACHOR_TEMPLE_STONE_WALL = BLOCKS.register("malachor_temple_stone_wall", TempleStoneWall::new);
    public static final RegistryObject<Item> MALACHOR_TEMPLE_STONE_WALL_ITEM = ITEMS.register("malachor_temple_stone_wall", () -> new BlockItem(MALACHOR_TEMPLE_STONE_WALL.get(), new Item.Properties()));
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

    public static final RegistryObject<Item> JEDI_DATACRON = ITEMS.register("jedi_datacron", () -> new DatacronItem(server.galaxyunderchaos.force.ForceSide.LIGHT, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SITH_DATACRON = ITEMS.register("sith_datacron", () -> new DatacronItem(server.galaxyunderchaos.force.ForceSide.DARK, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ANCIENT_DATACRON = ITEMS.register("ancient_datacron", () -> new DatacronItem(server.galaxyunderchaos.force.ForceSide.NEUTRAL, new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Block> JEDI_FORCE_HOLOCRON = BLOCKS.register("jedi_force_holocron", () -> new ForceHolocronBlock(server.galaxyunderchaos.force.ForceSide.LIGHT));
    public static final RegistryObject<Item> JEDI_FORCE_HOLOCRON_ITEM = ITEMS.register("jedi_force_holocron", () -> new BlockItem(JEDI_FORCE_HOLOCRON.get(), new Item.Properties()));
    public static final RegistryObject<Block> SITH_FORCE_HOLOCRON = BLOCKS.register("sith_force_holocron", () -> new ForceHolocronBlock(server.galaxyunderchaos.force.ForceSide.DARK));
    public static final RegistryObject<Item> SITH_FORCE_HOLOCRON_ITEM = ITEMS.register("sith_force_holocron", () -> new BlockItem(SITH_FORCE_HOLOCRON.get(), new Item.Properties()));
    public static final RegistryObject<Block> ANCIENT_FORCE_HOLOCRON = BLOCKS.register("ancient_force_holocron", () -> new ForceHolocronBlock(server.galaxyunderchaos.force.ForceSide.NEUTRAL));
    public static final RegistryObject<Item> ANCIENT_FORCE_HOLOCRON_ITEM = ITEMS.register("ancient_force_holocron", () -> new BlockItem(ANCIENT_FORCE_HOLOCRON.get(), new Item.Properties()));

    public static final RegistryObject<Block> JEDI_COFFIN = BLOCKS.register("jedi_coffin", () -> new JediCoffinBlock(BlockBehaviour.Properties.of().strength(2.5f)));
    public static final RegistryObject<Item> JEDI_COFFIN_ITEM = ITEMS.register("jedi_coffin", () -> new BlockItem(JEDI_COFFIN.get(), new Item.Properties()));
    public static final RegistryObject<Block> SITH_COFFIN = BLOCKS.register("sith_coffin", () -> new SithCoffinBlock(BlockBehaviour.Properties.of().strength(3.0f)));
    public static final RegistryObject<Item> SITH_COFFIN_ITEM = ITEMS.register("sith_coffin", () -> new BlockItem(SITH_COFFIN.get(), new Item.Properties()));
    public static final RegistryObject<Block> SITH_LORD_COFFIN = BLOCKS.register("sith_lord_coffin", () -> new JediCoffinBlock(BlockBehaviour.Properties.of().strength(3.0f).noOcclusion()));
    public static final RegistryObject<Item> SITH_LORD_COFFIN_ITEM = ITEMS.register("sith_lord_coffin", () -> new BlockItem(SITH_LORD_COFFIN.get(), new Item.Properties()));

    public static final RegistryObject<Block> JEDI_GUARD_STATUE = BLOCKS.register("jedi_guard_statue", JediGuard::new);
    public static final RegistryObject<Item> JEDI_GUARD_STATUE_ITEM = ITEMS.register("jedi_guard_statue", () -> new BlockItem(JEDI_GUARD_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> JEDI_GUARD_STATUE_STONE = BLOCKS.register("jedi_guard_statue_stone", JediGuard::new);
    public static final RegistryObject<Item> JEDI_GUARD_STATUE_STONE_ITEM = ITEMS.register("jedi_guard_statue_stone", () -> new BlockItem(JEDI_GUARD_STATUE_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Block> JEDI_GUARD_STATUE_STONE_2 = BLOCKS.register("jedi_guard_statue_stone_2", JediGuard::new);
    public static final RegistryObject<Item> JEDI_GUARD_STATUE_STONE_2_ITEM = ITEMS.register("jedi_guard_statue_stone_2", () -> new BlockItem(JEDI_GUARD_STATUE_STONE_2.get(), new Item.Properties()));
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


    public static final RegistryObject<Block> KORRIBAN_IDLE_HEAD_STATUE = BLOCKS.register("korriban_idle_head_statue", TythonJediStatueHEAD::new);
    public static final RegistryObject<Item> KORRIBAN_IDLE_HEAD_STATUE_ITEM = ITEMS.register("korriban_idle_head_statue", () -> new BlockItem(KORRIBAN_IDLE_HEAD_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> KORRIBAN_IDLE_TORSO_STATUE = BLOCKS.register("korriban_idle_torso_statue", TythonJediStatueTORSO::new);
    public static final RegistryObject<Item> KORRIBAN_IDLE_TORSO_STATUE_ITEM = ITEMS.register("korriban_idle_torso_statue", () -> new BlockItem(KORRIBAN_IDLE_TORSO_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> KORRIBAN_IDLE_LEG_1_STATUE = BLOCKS.register("korriban_idle_leg_1_statue", TythonJediStatueLEG::new);
    public static final RegistryObject<Item> KORRIBAN_IDLE_LEG_1_STATUE_ITEM = ITEMS.register("korriban_idle_leg_1_statue", () -> new BlockItem(KORRIBAN_IDLE_LEG_1_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> KORRIBAN_IDLE_LEG_2_STATUE = BLOCKS.register("korriban_idle_leg_2_statue", TythonJediStatueLEG::new);
    public static final RegistryObject<Item> KORRIBAN_IDLE_LEGS_2_STATUE_ITEM = ITEMS.register("korriban_idle_leg_2_statue", () -> new BlockItem(KORRIBAN_IDLE_LEG_2_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> KORRIBAN_CROSSED_TORSO_STATUE = BLOCKS.register("korriban_crossed_torso_statue", TythonJediStatueCTORSO::new);
    public static final RegistryObject<Item> KORRIBAN_CROSSED_TORSO_STATUE_ITEM = ITEMS.register("korriban_crossed_torso_statue", () -> new BlockItem(KORRIBAN_CROSSED_TORSO_STATUE.get(), new Item.Properties()));


    public static final RegistryObject<Block> SITH_GUARD_STATUE = BLOCKS.register("sith_guard_statue", SithGuard::new);
    public static final RegistryObject<Item> SITH_GUARD_STATUE_ITEM = ITEMS.register("sith_guard_statue", () -> new BlockItem(SITH_GUARD_STATUE.get(), new Item.Properties()));
    public static final RegistryObject<Block> LIGHTSABER_CRAFTING_TABLE = BLOCKS.register("lightsaber_crafting_table", () -> new LightsaberCraftingTableBlock());
    public static final RegistryObject<Item> LIGHTSABER_CRAFTING_TABLE_ITEM = ITEMS.register("lightsaber_crafting_table", () -> new BlockItem(LIGHTSABER_CRAFTING_TABLE.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHIP_CRAFTING_TABLE = BLOCKS.register("ship_crafting_table", () -> new ShipCraftingTableBlock());
    public static final RegistryObject<Item> SHIP_CRAFTING_TABLE_ITEM = ITEMS.register("ship_crafting_table", () -> new BlockItem(SHIP_CRAFTING_TABLE.get(), new Item.Properties()));

    public static final RegistryObject<Block> HOTH_STONE = BLOCKS.register("hoth_stone", AncientTempleStone::new);
    public static final RegistryObject<Item> HOTH_STONE_ITEM = ITEMS.register("hoth_stone", () -> new BlockItem(HOTH_STONE.get(), new Item.Properties()));

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
    public static final RegistryObject<Block> BELLEW_FLOWER = BLOCKS.register("bellew_flower",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.ALLIUM).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)){});
    public static final RegistryObject<Item> BELLEW_FLOWER_ITEM = ITEMS.register("bellew_flower",
            () -> new BlockItem(BELLEW_FLOWER.get(), new Item.Properties()));
    public static final RegistryObject<Block> QUEENS_HEART_FLOWER = BLOCKS.register("queens_heart_flower",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)){});
    public static final RegistryObject<Item> QUEENS_HEART_FLOWER_ITEM = ITEMS.register("queens_heart_flower",
            () -> new BlockItem(QUEENS_HEART_FLOWER.get(), new Item.Properties()));

    // #ITEMS

    public static final RegistryObject<Item> GALACTIC_GUIDE_BOOK = ITEMS.register("galactic_guide_book", () -> new GalacticGuideBookItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SHUURA = ITEMS.register("shuura", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(4).saturationMod(2f).build())));
    public static final RegistryObject<Item> HEART_BERRY = ITEMS.register("heart_berry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(8).saturationMod(2f).build())));
    public static final RegistryObject<Item> JEDI_HOLOBOOK = ITEMS.register("jedi_holobook", () -> new ForceLoreHolobookItem(ForceSide.LIGHT, new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_HOLOBOOK = ITEMS.register("ancient_holobook", () -> new ForceLoreHolobookItem(ForceSide.NEUTRAL, new Item.Properties()));
    public static final RegistryObject<Item> SITH_HOLOBOOK = ITEMS.register("sith_holobook", () -> new ForceLoreHolobookItem(ForceSide.DARK, new Item.Properties()));
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
    public static final RegistryObject<Item> FORCE_SHACKLES = ITEMS.register("force_shackles", () -> new ForceShacklesItem(new Item.Properties().stacksTo(1).durability(10)));
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
public static final RegistryObject<Item> FOCUSING_CRYSTAL_FORCE_WHIP = ITEMS.register("focusing_crystal_force_whip",
        () -> new BladeModifierCrystalItem(server.galaxyunderchaos.lightsaber.BladeModifierCrystal.FORCE_WHIP, new Item.Properties()));


    public static final RegistryObject<Item> TYTHON_PORTAL_ITEM = ITEMS.register("tython_portal",
            () -> new TythonPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MUSTAFAR_PORTAL_ITEM = ITEMS.register("mustafar_portal",
            () -> new MustafarPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NABOO_PORTAL_ITEM = ITEMS.register("naboo_portal",
            () -> new NabooPortalItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOTH_PORTAL_ITEM = ITEMS.register("hoth_portal",
            () -> new HothPortalItem(new Item.Properties().stacksTo(1)));
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
    public static final RegistryObject<Item> SITH_GUARD_HELMET = ITEMS.register("sith_guard_helmet",
            () -> new ArmorItem(CustomArmor.SITH_GUARD_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties()));
    public static final RegistryObject<Item> SITH_GUARD_CHESTPLATE = ITEMS.register("sith_guard_chestplate",
            () -> new ArmorItem(CustomArmor.SITH_GUARD_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties()));
    public static final RegistryObject<Item> SITH_GUARD_LEGGINGS = ITEMS.register("sith_guard_leggings",
            () -> new ArmorItem(CustomArmor.SITH_GUARD_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties()));
    public static final RegistryObject<Item> SITH_GUARD_BOOTS = ITEMS.register("sith_guard_boots",
            () -> new ArmorItem(CustomArmor.SITH_GUARD_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties()));
    public static final RegistryObject<Item> SITH_GUARD_FABRIC = ITEMS.register("sith_guard_fabric",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHITIN_FRAGMENTS = ITEMS.register("chitin_fragments",
            () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> LOST_HILT = ITEMS.register("lost_hilt",
//            () -> new HiltItem("green", new Item.Properties()));
//    public static final RegistryObject<Item> AEGIS_HILT = ITEMS.register("aegis_hilt",
//            () -> new HiltItem("orange", new Item.Properties()));
//    public static final RegistryObject<Item> APPRENTICE_HILT = ITEMS.register("apprentice_hilt",
//            () -> new HiltItem("pink", new Item.Properties()));
//    public static final RegistryObject<Item> CHOSEN_HILT = ITEMS.register("chosen_hilt",
//            () -> new HiltItem("blue", new Item.Properties()));
//    public static final RegistryObject<Item> EMPEROR_HILT = ITEMS.register("emperor_hilt",
//            () -> new HiltItem("red", new Item.Properties()));
//    public static final RegistryObject<Item> FALLEN_HILT = ITEMS.register("fallen_hilt",
//            () -> new HiltItem("red", new Item.Properties()));
//    public static final RegistryObject<Item> GRACE_HILT = ITEMS.register("grace_hilt",
//            () -> new HiltItem("red", new Item.Properties()));
//    public static final RegistryObject<Item> GUARD_HILT = ITEMS.register("guard_hilt",
//            () -> new HiltItem("red", new Item.Properties()));
//    public static final RegistryObject<Item> HARMONY_HILT = ITEMS.register("harmony_hilt",
//            () -> new HiltItem("blue", new Item.Properties()));
//    public static final RegistryObject<Item> LEGACY_HILT = ITEMS.register("legacy_hilt",
//            () -> new HiltItem("green", new Item.Properties()));
//    public static final RegistryObject<Item> PADAWAN_HILT = ITEMS.register("padawan_hilt",
//            () -> new HiltItem("blue", new Item.Properties()));
//    public static final RegistryObject<Item> RESOLVE_HILT = ITEMS.register("resolve_hilt",
//            () -> new HiltItem("blue", new Item.Properties()));
//    public static final RegistryObject<Item> SKUSTELL_HILT = ITEMS.register("skustell_hilt",
//            () -> new HiltItem("blue", new Item.Properties()));
//    public static final RegistryObject<Item> TALON_HILT = ITEMS.register("talon_hilt",
//            () -> new HiltItem("orange", new Item.Properties()));
//    public static final RegistryObject<Item> VALOR_HILT = ITEMS.register("valor_hilt",
//            () -> new HiltItem("purple", new Item.Properties()));
//    public static final RegistryObject<Item> WISDOM_HILT = ITEMS.register("wisdom_hilt",
//            () -> new HiltItem("blue", new Item.Properties()));
//    public static final RegistryObject<Item> NEGOTIATOR_HILT = ITEMS.register("negotiator_hilt",
//            () -> new HiltItem("blue", new Item.Properties()));
//    public static final RegistryObject<Item> KNIGHTFALL_HILT = ITEMS.register("knightfall_hilt",
//            () -> new HiltItem("blue", new Item.Properties()));
//    public static final RegistryObject<Item> BAROSHE_HILT = ITEMS.register("baroshe_hilt",
//            () -> new HiltItem("blue", new Item.Properties()));
    public static final RegistryObject<Item> ACID_SPIDER_SPAWN_EGG = ITEMS.register("acid_spider_spawn_egg",
            () -> new ForgeSpawnEggItem(galaxyunderchaos.ACID_SPIDER, 0x53524b, 0xdac741, new Item.Properties()));
    public static final RegistryObject<Item> WINGMAW_SPAWN_EGG = ITEMS.register("wingmaw_spawn_egg",
            () -> new ForgeSpawnEggItem(galaxyunderchaos.WINGMAW, 0x53524b, 0xdac741, new Item.Properties()));
    public static final RegistryObject<Item> VONSKR_SPAWN_EGG = ITEMS.register("vonskr_spawn_egg",
            () -> new ForgeSpawnEggItem(galaxyunderchaos.VONSKR, 0x2D241F, 0xB9875A, new Item.Properties()));
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

    public static final RegistryObject<Item> INTERNAL_LIGHTSABER_CIRCUITRY = ITEMS.register("internal_lightsaber_circuitry",
            () -> new Item(new Item.Properties().stacksTo(1)));

//    public static void registerLightsabers() {
//        String[] bladeColors = {
//                "red", "blue", "green", "yellow", "cyan",
//                "white", "magenta", "purple", "pink",
//                "lime_green", "turquoise", "orange", "blood_orange", "amber", "gold", "light_blue", "dark_blue", "maroon", "deep_violet", "arctic_blue", "rose_pink"
//        };
//
//        String[] hiltNames = {
//                "apprentice", "chosen", "emperor", "legacy", "padawan",
//                "resolve", "talon", "valor", "wisdom", "lost", "aegis", "grace", "guard", "harmony",
//                "skustell", "fallen", "negotiator", "baroshe", "knightfall"
//        };
//
//        for (String color : bladeColors) {
//            for (String hilt : hiltNames) {
//                String id = color + "_" + hilt + "_lightsaber";
//                LIGHTSABERS.put(id, ITEMS.register(
//                        id,
//                        () -> new LightsaberItem(color, new Item.Properties().stacksTo(1))
//                ));
//            }
//        }
//    }


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

    public static final RegistryObject<Block> BLBA_LOG = BLOCKS.register("blba_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_LOG)));
    public static final RegistryObject<Block> BLBA_WOOD = BLOCKS.register("blba_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_WOOD)));
    public static final RegistryObject<Block> STRIPPED_BLBA_LOG = BLOCKS.register("stripped_blba_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_LOG)));
    public static final RegistryObject<Block> STRIPPED_BLBA_WOOD = BLOCKS.register("stripped_blba_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_WOOD)));
    public static final RegistryObject<Block> BLBA_PLANKS = BLOCKS.register("blba_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)){});
    public static final RegistryObject<Block> BLBA_LEAVES = BLOCKS.register("blba_leaves",
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

    public static final RegistryObject<Block> DILLIA_LOG = BLOCKS.register("dillia_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_LOG)));
    public static final RegistryObject<Block> DILLIA_WOOD = BLOCKS.register("dillia_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_WOOD)));
    public static final RegistryObject<Block> STRIPPED_DILLIA_LOG = BLOCKS.register("stripped_dillia_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_LOG)));
    public static final RegistryObject<Block> STRIPPED_DILLIA_WOOD = BLOCKS.register("stripped_dillia_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_WOOD)));
    public static final RegistryObject<Block> DILLIA_PLANKS = BLOCKS.register("dillia_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)){});
    public static final RegistryObject<Block> DILLIA_LEAVES = BLOCKS.register("dillia_leaves",
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

    public static final RegistryObject<Block> CAMBYLICTUS_LOG = BLOCKS.register("cambylictus_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.JUNGLE_LOG)));
    public static final RegistryObject<Block> CAMBYLICTUS_WOOD = BLOCKS.register("cambylictus_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.JUNGLE_WOOD)));
    public static final RegistryObject<Block> STRIPPED_CAMBYLICTUS_LOG = BLOCKS.register("stripped_cambylictus_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_JUNGLE_LOG)));
    public static final RegistryObject<Block> STRIPPED_CAMBYLICTUS_WOOD = BLOCKS.register("stripped_cambylictus_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_JUNGLE_WOOD)));
    public static final RegistryObject<Block> CAMBYLICTUS_PLANKS = BLOCKS.register("cambylictus_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)){});
    public static final RegistryObject<Block> CAMBYLICTUS_LEAVES = BLOCKS.register("cambylictus_leaves",
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
    public static final RegistryObject<Block> PERLOTE_LOG = BLOCKS.register("perlote_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> PERLOTE_WOOD = BLOCKS.register("perlote_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_PERLOTE_LOG = BLOCKS.register("stripped_perlote_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_PERLOTE_WOOD = BLOCKS.register("stripped_perlote_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> PERLOTE_PLANKS = BLOCKS.register("perlote_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)){});
    public static final RegistryObject<Block> PERLOTE_LEAVES = BLOCKS.register("perlote_leaves",
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
    public static final RegistryObject<Block> RUTIGER_LOG = BLOCKS.register("rutiger_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> RUTIGER_WOOD = BLOCKS.register("rutiger_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_RUTIGER_LOG = BLOCKS.register("stripped_rutiger_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_RUTIGER_WOOD = BLOCKS.register("stripped_rutiger_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> RUTIGER_PLANKS = BLOCKS.register("rutiger_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)){});
    public static final RegistryObject<Block> RUTIGER_LEAVES = BLOCKS.register("rutiger_leaves",
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
    public static final RegistryObject<Block> POLAR_LOG = BLOCKS.register("polar_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_LOG)));
    public static final RegistryObject<Block> POLAR_WOOD = BLOCKS.register("polar_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_WOOD)));
    public static final RegistryObject<Block> STRIPPED_POLAR_LOG = BLOCKS.register("stripped_polar_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_LOG)));
    public static final RegistryObject<Block> STRIPPED_POLAR_WOOD = BLOCKS.register("stripped_polar_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_WOOD)));
    public static final RegistryObject<Block> POLAR_PLANKS = BLOCKS.register("polar_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)){});
    public static final RegistryObject<Block> POLAR_LEAVES = BLOCKS.register("polar_leaves",
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
    public static final RegistryObject<Block> NABOO_PINE_LOG = BLOCKS.register("naboo_pine_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_LOG)));
    public static final RegistryObject<Block> NABOO_PINE_WOOD = BLOCKS.register("naboo_pine_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_WOOD)));
    public static final RegistryObject<Block> STRIPPED_NABOO_PINE_LOG = BLOCKS.register("stripped_naboo_pine_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_SPRUCE_LOG)));
    public static final RegistryObject<Block> STRIPPED_NABOO_PINE_WOOD = BLOCKS.register("stripped_naboo_pine_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_SPRUCE_WOOD)));
    public static final RegistryObject<Block> NABOO_PINE_PLANKS = BLOCKS.register("naboo_pine_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)){});
    public static final RegistryObject<Block> NABOO_PINE_LEAVES = BLOCKS.register("naboo_pine_leaves",
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

    public static final BlockSetType BLBA_BLOCK_SET =
            BlockSetType.register(new BlockSetType("blba"));
    public static final WoodType BLBA_WOOD_TYPE =
            WoodType.register(new WoodType("blba", BLBA_BLOCK_SET));
    public static final BlockSetType CAMBYLICTUS_BLOCK_SET =
            BlockSetType.register(new BlockSetType("cambylictus"));
    public static final WoodType CAMBYLICTUS_WOOD_TYPE =
            WoodType.register(new WoodType("cambylictus", CAMBYLICTUS_BLOCK_SET));
    public static final BlockSetType PERLOTE_BLOCK_SET =
            BlockSetType.register(new BlockSetType("perlote"));
    public static final WoodType PERLOTE_WOOD_TYPE =
            WoodType.register(new WoodType("perlote", PERLOTE_BLOCK_SET));
    public static final BlockSetType RUTIGER_BLOCK_SET =
            BlockSetType.register(new BlockSetType("rutiger"));
    public static final WoodType RUTIGER_WOOD_TYPE =
            WoodType.register(new WoodType("rutiger", RUTIGER_BLOCK_SET));
    public static final BlockSetType POLAR_BLOCK_SET =
            BlockSetType.register(new BlockSetType("polar"));
    public static final WoodType POLAR_WOOD_TYPE =
            WoodType.register(new WoodType("polar", POLAR_BLOCK_SET));
    public static final BlockSetType DILLIA_BLOCK_SET =
            BlockSetType.register(new BlockSetType("dillia"));
    public static final WoodType DILLIA_WOOD_TYPE =
            WoodType.register(new WoodType("dillia", DILLIA_BLOCK_SET));
    public static final BlockSetType NABOO_PINE_BLOCK_SET =
            BlockSetType.register(new BlockSetType("naboo_pine"));
    public static final WoodType NABOO_PINE_WOOD_TYPE =
            WoodType.register(new WoodType("naboo_pine", NABOO_PINE_BLOCK_SET));
    public static final BlockSetType AK_BLOCK_SET =
            BlockSetType.register(new BlockSetType("ak"));
    public static final WoodType AK_WOOD_TYPE =
            WoodType.register(new WoodType("ak", AK_BLOCK_SET));
    public static final BlockSetType HEART_BERRY_BLOCK_SET =
            BlockSetType.register(new BlockSetType("heart_berry"));
    public static final WoodType HEART_BERRY_WOOD_TYPE =
            WoodType.register(new WoodType("heart_berry", HEART_BERRY_BLOCK_SET));


    public static final RegistryObject<Block> BLBA_SAPLING = BLOCKS.register("blba_sapling",
            () -> new SaplingBlock(ModTreeGrowers.BLBA, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> DILLIA_SAPLING = BLOCKS.register("dillia_sapling",
            () -> new SaplingBlock(ModTreeGrowers.DILLIA_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> CAMBYLICTUS_SAPLING = BLOCKS.register("cambylictus_sapling",
            () -> new SaplingBlock(ModTreeGrowers.CAMBYLICTUS_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> PERLOTE_SAPLING = BLOCKS.register("perlote_sapling",
            () -> new SaplingBlock(ModTreeGrowers.PERLOTE_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> RUTIGER_SAPLING = BLOCKS.register("rutiger_sapling",
            () -> new SaplingBlock(ModTreeGrowers.RUTIGER_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> POLAR_SAPLING = BLOCKS.register("polar_sapling",
            () -> new SaplingBlock(ModTreeGrowers.POLAR_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> NABOO_PINE_SAPLING = BLOCKS.register("naboo_pine_sapling",
            () -> new SaplingBlock(ModTreeGrowers.NABOO_PINE_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> HEART_BERRY_SAPLING = BLOCKS.register("heart_berry_sapling",
            () -> new SaplingBlock(ModTreeGrowers.HEART_BERRY_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> AK_SAPLING = BLOCKS.register("ak_sapling",
            () -> new SaplingBlock(ModTreeGrowers.AK_TREE, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Item> BLBA_LOG_ITEM = ITEMS.register("blba_log",
            () -> new BlockItem(BLBA_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLBA_WOOD_ITEM = ITEMS.register("blba_wood",
            () -> new BlockItem(BLBA_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_BLBA_LOG_ITEM = ITEMS.register("stripped_blba_log",
            () -> new BlockItem(STRIPPED_BLBA_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_BLBA_WOOD_ITEM = ITEMS.register("stripped_blba_wood",
            () -> new BlockItem(STRIPPED_BLBA_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLBA_PLANKS_ITEM = ITEMS.register("blba_planks",
            () -> new BlockItem(BLBA_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLBA_LEAVES_ITEM = ITEMS.register("blba_leaves",
            () -> new BlockItem(BLBA_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLBA_SAPLING_ITEM = ITEMS.register("blba_sapling",
            () -> new BlockItem(BLBA_SAPLING.get(), new Item.Properties()));

    public static final RegistryObject<Item> DILLIA_LOG_ITEM = ITEMS.register("dillia_log",
            () -> new BlockItem(DILLIA_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> DILLIA_WOOD_ITEM = ITEMS.register("dillia_wood",
            () -> new BlockItem(DILLIA_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_DILLIA_LOG_ITEM = ITEMS.register("stripped_dillia_log",
            () -> new BlockItem(STRIPPED_DILLIA_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_DILLIA_WOOD_ITEM = ITEMS.register("stripped_dillia_wood",
            () -> new BlockItem(STRIPPED_DILLIA_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> DILLIA_PLANKS_ITEM = ITEMS.register("dillia_planks",
            () -> new BlockItem(DILLIA_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> DILLIA_LEAVES_ITEM = ITEMS.register("dillia_leaves",
            () -> new BlockItem(DILLIA_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> DILLIA_SAPLING_ITEM = ITEMS.register("dillia_sapling",
            () -> new BlockItem(DILLIA_SAPLING.get(), new Item.Properties()));

    public static final RegistryObject<Item> CAMBYLICTUS_LOG_ITEM = ITEMS.register("cambylictus_log",
            () -> new BlockItem(CAMBYLICTUS_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> CAMBYLICTUS_WOOD_ITEM = ITEMS.register("cambylictus_wood",
            () -> new BlockItem(CAMBYLICTUS_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_CAMBYLICTUS_LOG_ITEM = ITEMS.register("stripped_cambylictus_log",
            () -> new BlockItem(STRIPPED_CAMBYLICTUS_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_CAMBYLICTUS_WOOD_ITEM = ITEMS.register("stripped_cambylictus_wood",
            () -> new BlockItem(STRIPPED_CAMBYLICTUS_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> CAMBYLICTUS_PLANKS_ITEM = ITEMS.register("cambylictus_planks",
            () -> new BlockItem(CAMBYLICTUS_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> CAMBYLICTUS_LEAVES_ITEM = ITEMS.register("cambylictus_leaves",
            () -> new BlockItem(CAMBYLICTUS_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> CAMBYLICTUS_SAPLING_ITEM = ITEMS.register("cambylictus_sapling",
            () -> new BlockItem(CAMBYLICTUS_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> PERLOTE_LOG_ITEM = ITEMS.register("perlote_log",
            () -> new BlockItem(PERLOTE_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> PERLOTE_WOOD_ITEM = ITEMS.register("perlote_wood",
            () -> new BlockItem(PERLOTE_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_PERLOTE_LOG_ITEM = ITEMS.register("stripped_perlote_log",
            () -> new BlockItem(STRIPPED_PERLOTE_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_PERLOTE_WOOD_ITEM = ITEMS.register("stripped_perlote_wood",
            () -> new BlockItem(STRIPPED_PERLOTE_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> PERLOTE_PLANKS_ITEM = ITEMS.register("perlote_planks",
            () -> new BlockItem(PERLOTE_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> PERLOTE_LEAVES_ITEM = ITEMS.register("perlote_leaves",
            () -> new BlockItem(PERLOTE_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> PERLOTE_SAPLING_ITEM = ITEMS.register("perlote_sapling",
            () -> new BlockItem(PERLOTE_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> RUTIGER_LOG_ITEM = ITEMS.register("rutiger_log",
            () -> new BlockItem(RUTIGER_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> RUTIGER_WOOD_ITEM = ITEMS.register("rutiger_wood",
            () -> new BlockItem(RUTIGER_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_RUTIGER_LOG_ITEM = ITEMS.register("stripped_rutiger_log",
            () -> new BlockItem(STRIPPED_RUTIGER_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_RUTIGER_WOOD_ITEM = ITEMS.register("stripped_rutiger_wood",
            () -> new BlockItem(STRIPPED_RUTIGER_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> RUTIGER_PLANKS_ITEM = ITEMS.register("rutiger_planks",
            () -> new BlockItem(RUTIGER_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> RUTIGER_LEAVES_ITEM = ITEMS.register("rutiger_leaves",
            () -> new BlockItem(RUTIGER_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> RUTIGER_SAPLING_ITEM = ITEMS.register("rutiger_sapling",
            () -> new BlockItem(RUTIGER_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> POLAR_LOG_ITEM = ITEMS.register("polar_log",
            () -> new BlockItem(POLAR_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> POLAR_WOOD_ITEM = ITEMS.register("polar_wood",
            () -> new BlockItem(POLAR_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_POLAR_LOG_ITEM = ITEMS.register("stripped_polar_log",
            () -> new BlockItem(STRIPPED_POLAR_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_POLAR_WOOD_ITEM = ITEMS.register("stripped_polar_wood",
            () -> new BlockItem(STRIPPED_POLAR_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> POLAR_PLANKS_ITEM = ITEMS.register("polar_planks",
            () -> new BlockItem(POLAR_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> POLAR_LEAVES_ITEM = ITEMS.register("polar_leaves",
            () -> new BlockItem(POLAR_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> POLAR_SAPLING_ITEM = ITEMS.register("polar_sapling",
            () -> new BlockItem(POLAR_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> NABOO_PINE_LOG_ITEM = ITEMS.register("naboo_pine_log",
            () -> new BlockItem(NABOO_PINE_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> NABOO_PINE_WOOD_ITEM = ITEMS.register("naboo_pine_wood",
            () -> new BlockItem(NABOO_PINE_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_NABOO_PINE_LOG_ITEM = ITEMS.register("stripped_naboo_pine_log",
            () -> new BlockItem(STRIPPED_NABOO_PINE_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_NABOO_PINE_WOOD_ITEM = ITEMS.register("stripped_naboo_pine_wood",
            () -> new BlockItem(STRIPPED_NABOO_PINE_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> NABOO_PINE_PLANKS_ITEM = ITEMS.register("naboo_pine_planks",
            () -> new BlockItem(NABOO_PINE_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> NABOO_PINE_LEAVES_ITEM = ITEMS.register("naboo_pine_leaves",
            () -> new BlockItem(NABOO_PINE_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> NABOO_PINE_SAPLING_ITEM = ITEMS.register("naboo_pine_sapling",
            () -> new BlockItem(NABOO_PINE_SAPLING.get(), new Item.Properties()));

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

    public static final RegistryObject<Block> DILLIA_STAIRS = BLOCKS.register(
            "dillia_stairs", () -> new StairBlock(() -> DILLIA_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(DILLIA_PLANKS.get())));
    public static final RegistryObject<Item> DILLIA_STAIRS_ITEM = ITEMS.register(
            "dillia_stairs", () -> new BlockItem(DILLIA_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> DILLIA_SLAB = BLOCKS.register(
            "dillia_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(DILLIA_PLANKS.get())));
    public static final RegistryObject<Item> DILLIA_SLAB_ITEM = ITEMS.register(
            "dillia_slab", () -> new BlockItem(DILLIA_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> DILLIA_BUTTON = BLOCKS.register(
            "dillia_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).noCollission().strength(0.5F), DILLIA_BLOCK_SET, 30, true));
    public static final RegistryObject<Item> DILLIA_BUTTON_ITEM = ITEMS.register(
            "dillia_button", () -> new BlockItem(DILLIA_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Block> DILLIA_PRESSURE_PLATE = BLOCKS.register(
            "dillia_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE).strength(0.5F), DILLIA_BLOCK_SET));
    public static final RegistryObject<Item> DILLIA_PRESSURE_PLATE_ITEM = ITEMS.register(
            "dillia_pressure_plate", () -> new BlockItem(DILLIA_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> DILLIA_FENCE_BLOCK = BLOCKS.register(
            "dillia_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(DILLIA_PLANKS.get())));
    public static final RegistryObject<Item> DILLIA_FENCE_ITEM = ITEMS.register(
            "dillia_fence", () -> new BlockItem(DILLIA_FENCE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> DILLIA_FENCE_GATE = BLOCKS.register(
            "dillia_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), DILLIA_WOOD_TYPE));
    public static final RegistryObject<Item> DILLIA_FENCE_GATE_ITEM = ITEMS.register(
            "dillia_fence_gate", () -> new BlockItem(DILLIA_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> DILLIA_DOOR_BLOCK = BLOCKS.register(
            "dillia_door", () -> new TreeDoor(DILLIA_BLOCK_SET));
    public static final RegistryObject<Item> DILLIA_DOOR_ITEM = ITEMS.register(
            "dillia_door", () -> new BlockItem(DILLIA_DOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> DILLIA_TRAPDOOR_BLOCK = BLOCKS.register(
            "dillia_trapdoor", () -> new TreeTrapdoor(DILLIA_BLOCK_SET));
    public static final RegistryObject<Item> DILLIA_TRAPDOOR_ITEM = ITEMS.register(
            "dillia_trapdoor", () -> new BlockItem(DILLIA_TRAPDOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> DILLIA_SIGN = BLOCKS.register(
            "dillia_sign", () -> new TreeStandingSignDillia(DILLIA_WOOD_TYPE));
    public static final RegistryObject<Block> DILLIA_WALL_SIGN = BLOCKS.register(
            "dillia_wall_sign", () -> new TreeWallSignDillia(DILLIA_WOOD_TYPE));
    public static final RegistryObject<Item> DILLIA_SIGN_ITEM = ITEMS.register(
            "dillia_sign", () -> new SignItem(new Item.Properties(), DILLIA_SIGN.get(), DILLIA_WALL_SIGN.get()));
    public static final RegistryObject<Block> DILLIA_HANGING_SIGN = BLOCKS.register(
            "dillia_hanging_sign", () -> new TreeHangingSignDillia(DILLIA_WOOD_TYPE));
    public static final RegistryObject<Block> DILLIA_WALL_HANGING_SIGN = BLOCKS.register(
            "dillia_wall_hanging_sign", () -> new TreeWallHangingSignDillia(DILLIA_WOOD_TYPE));
    public static final RegistryObject<Item> DILLIA_HANGING_SIGN_ITEM = ITEMS.register(
            "dillia_hanging_sign", () -> new HangingSignItem(DILLIA_HANGING_SIGN.get(), DILLIA_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> DILLIA_BOAT = ITEMS.register("dillia_boat",
            () -> new ModBoatItem(false, ModEntityTypes.DILLIA_BOAT::get, new Item.Properties()));
    public static final RegistryObject<Item> DILLIA_CHEST_BOAT = ITEMS.register("dillia_chest_boat",
            () -> new ModBoatItem(false, ModEntityTypes.DILLIA_CHEST_BOAT::get, new Item.Properties()));

    public static final RegistryObject<Block> NABOO_PINE_STAIRS = BLOCKS.register(
            "naboo_pine_stairs", () -> new StairBlock(() -> NABOO_PINE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(NABOO_PINE_PLANKS.get())));
    public static final RegistryObject<Item> NABOO_PINE_STAIRS_ITEM = ITEMS.register(
            "naboo_pine_stairs", () -> new BlockItem(NABOO_PINE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> NABOO_PINE_SLAB = BLOCKS.register(
            "naboo_pine_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(NABOO_PINE_PLANKS.get())));
    public static final RegistryObject<Item> NABOO_PINE_SLAB_ITEM = ITEMS.register(
            "naboo_pine_slab", () -> new BlockItem(NABOO_PINE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> NABOO_PINE_BUTTON = BLOCKS.register(
            "naboo_pine_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).noCollission().strength(0.5F), NABOO_PINE_BLOCK_SET, 30, true));
    public static final RegistryObject<Item> NABOO_PINE_BUTTON_ITEM = ITEMS.register(
            "naboo_pine_button", () -> new BlockItem(NABOO_PINE_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Block> NABOO_PINE_PRESSURE_PLATE = BLOCKS.register(
            "naboo_pine_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE).strength(0.5F), NABOO_PINE_BLOCK_SET));
    public static final RegistryObject<Item> NABOO_PINE_PRESSURE_PLATE_ITEM = ITEMS.register(
            "naboo_pine_pressure_plate", () -> new BlockItem(NABOO_PINE_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> NABOO_PINE_FENCE_BLOCK = BLOCKS.register(
            "naboo_pine_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(NABOO_PINE_PLANKS.get())));
    public static final RegistryObject<Item> NABOO_PINE_FENCE_ITEM = ITEMS.register(
            "naboo_pine_fence", () -> new BlockItem(NABOO_PINE_FENCE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> NABOO_PINE_FENCE_GATE = BLOCKS.register(
            "naboo_pine_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), NABOO_PINE_WOOD_TYPE));
    public static final RegistryObject<Item> NABOO_PINE_FENCE_GATE_ITEM = ITEMS.register(
            "naboo_pine_fence_gate", () -> new BlockItem(NABOO_PINE_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> NABOO_PINE_DOOR_BLOCK = BLOCKS.register(
            "naboo_pine_door", () -> new TreeDoor(NABOO_PINE_BLOCK_SET));
    public static final RegistryObject<Item> NABOO_PINE_DOOR_ITEM = ITEMS.register(
            "naboo_pine_door", () -> new BlockItem(NABOO_PINE_DOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> NABOO_PINE_TRAPDOOR_BLOCK = BLOCKS.register(
            "naboo_pine_trapdoor", () -> new TreeTrapdoor(NABOO_PINE_BLOCK_SET));
    public static final RegistryObject<Item> NABOO_PINE_TRAPDOOR_ITEM = ITEMS.register(
            "naboo_pine_trapdoor", () -> new BlockItem(NABOO_PINE_TRAPDOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> NABOO_PINE_SIGN = BLOCKS.register(
            "naboo_pine_sign", () -> new TreeStandingSignNabooPine(NABOO_PINE_WOOD_TYPE));
    public static final RegistryObject<Block> NABOO_PINE_WALL_SIGN = BLOCKS.register(
            "naboo_pine_wall_sign", () -> new TreeWallSignNabooPine(NABOO_PINE_WOOD_TYPE));
    public static final RegistryObject<Item> NABOO_PINE_SIGN_ITEM = ITEMS.register(
            "naboo_pine_sign", () -> new SignItem(new Item.Properties(), NABOO_PINE_SIGN.get(), NABOO_PINE_WALL_SIGN.get()));
    public static final RegistryObject<Block> NABOO_PINE_HANGING_SIGN = BLOCKS.register(
            "naboo_pine_hanging_sign", () -> new TreeHangingSignNabooPine(NABOO_PINE_WOOD_TYPE));
    public static final RegistryObject<Block> NABOO_PINE_WALL_HANGING_SIGN = BLOCKS.register(
            "naboo_pine_wall_hanging_sign", () -> new TreeWallHangingSignNabooPine(NABOO_PINE_WOOD_TYPE));
    public static final RegistryObject<Item> NABOO_PINE_HANGING_SIGN_ITEM = ITEMS.register(
            "naboo_pine_hanging_sign", () -> new HangingSignItem(NABOO_PINE_HANGING_SIGN.get(), NABOO_PINE_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> NABOO_PINE_BOAT = ITEMS.register("naboo_pine_boat",
            () -> new ModBoatItem(false, ModEntityTypes.NABOO_PINE_BOAT::get, new Item.Properties()));
    public static final RegistryObject<Item> NABOO_PINE_CHEST_BOAT = ITEMS.register("naboo_pine_chest_boat",
            () -> new ModBoatItem(false, ModEntityTypes.NABOO_PINE_CHEST_BOAT::get, new Item.Properties()));


    public static final RegistryObject<Block> BLBA_STAIRS = BLOCKS.register(
            "blba_stairs", () -> new TreeStairs(BLBA_PLANKS.get().defaultBlockState()));
    public static final RegistryObject<Item> BLBA_STAIRS_ITEM = ITEMS.register(
            "blba_stairs", () -> new BlockItem(BLBA_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLBA_SLAB = BLOCKS.register(
            "blba_slab", TreeSlab::new);
    public static final RegistryObject<Item> BLBA_SLAB_ITEM = ITEMS.register(
            "blba_slab", () -> new BlockItem(BLBA_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLBA_BUTTON = BLOCKS.register(
            "blba_button", () -> new TreeButton(BLBA_BLOCK_SET));
    public static final RegistryObject<Item> BLBA_BUTTON_ITEM = ITEMS.register(
            "blba_button", () -> new BlockItem(BLBA_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLBA_PRESSURE_PLATE = BLOCKS.register(
            "blba_pressure_plate", () -> new TreePressurePlate(BLBA_BLOCK_SET));
    public static final RegistryObject<Item> BLBA_PRESSURE_PLATE_ITEM = ITEMS.register(
            "blba_pressure_plate", () -> new BlockItem(BLBA_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLBA_FENCE_BLOCK = BLOCKS.register(
            "blba_fence", TreeFence::new);
    public static final RegistryObject<Item> BLBA_FENCE_ITEM = ITEMS.register(
            "blba_fence", () -> new BlockItem(BLBA_FENCE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLBA_FENCE_GATE = BLOCKS.register(
            "blba_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), BLBA_WOOD_TYPE));
    public static final RegistryObject<Item> BLBA_FENCE_GATE_ITEM = ITEMS.register(
            "blba_fence_gate", () -> new BlockItem(BLBA_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLBA_DOOR_BLOCK = BLOCKS.register(
            "blba_door", () -> new TreeDoor(BLBA_BLOCK_SET));
    public static final RegistryObject<Item> BLBA_DOOR_ITEM = ITEMS.register(
            "blba_door", () -> new BlockItem(BLBA_DOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLBA_TRAPDOOR_BLOCK = BLOCKS.register(
            "blba_trapdoor", () -> new TreeTrapdoor(BLBA_BLOCK_SET));
    public static final RegistryObject<Item> BLBA_TRAPDOOR_ITEM = ITEMS.register(
            "blba_trapdoor", () -> new BlockItem(BLBA_TRAPDOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> BLBA_SIGN = BLOCKS.register(
            "blba_sign", () -> new TreeStandingSignBlba(BLBA_WOOD_TYPE));
    public static final RegistryObject<Block> BLBA_WALL_SIGN = BLOCKS.register(
            "blba_wall_sign", () -> new TreeWallSignBlba(BLBA_WOOD_TYPE));
    public static final RegistryObject<Item> BLBA_SIGN_ITEM = ITEMS.register(
            "blba_sign", () -> new SignItem(new Item.Properties(), BLBA_SIGN.get(), BLBA_WALL_SIGN.get()));
    public static final RegistryObject<Block> BLBA_HANGING_SIGN = BLOCKS.register(
            "blba_hanging_sign", () -> new TreeHangingSignBlba(BLBA_WOOD_TYPE));
    public static final RegistryObject<Block> BLBA_WALL_HANGING_SIGN = BLOCKS.register(
            "blba_wall_hanging_sign", () -> new TreeWallHangingSignBlba(BLBA_WOOD_TYPE));
    public static final RegistryObject<Item> BLBA_HANGING_SIGN_ITEM = ITEMS.register(
            "blba_hanging_sign", () -> new HangingSignItem(BLBA_HANGING_SIGN.get(), BLBA_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLBA_BOAT = ITEMS.register("blba_boat",
            () -> new ModBoatItem(false, ModEntityTypes.BLBA_BOAT::get, new Item.Properties()));
    public static final RegistryObject<Item> BLBA_CHEST_BOAT = ITEMS.register("blba_chest_boat",
            () -> new ModBoatItem(false, ModEntityTypes.BLBA_CHEST_BOAT::get, new Item.Properties()));

    public static final RegistryObject<Block> CAMBYLICTUS_STAIRS = BLOCKS.register(
            "cambylictus_stairs", () -> new TreeStairs(CAMBYLICTUS_PLANKS.get().defaultBlockState()));
    public static final RegistryObject<Item> CAMBYLICTUS_STAIRS_ITEM = ITEMS.register(
            "cambylictus_stairs", () -> new BlockItem(CAMBYLICTUS_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> CAMBYLICTUS_SLAB = BLOCKS.register(
            "cambylictus_slab", TreeSlab::new);
    public static final RegistryObject<Item> CAMBYLICTUS_SLAB_ITEM = ITEMS.register(
            "cambylictus_slab", () -> new BlockItem(CAMBYLICTUS_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> CAMBYLICTUS_BUTTON = BLOCKS.register(
            "cambylictus_button", () -> new TreeButton(CAMBYLICTUS_BLOCK_SET));
    public static final RegistryObject<Item> CAMBYLICTUS_BUTTON_ITEM = ITEMS.register(
            "cambylictus_button", () -> new BlockItem(CAMBYLICTUS_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Block> CAMBYLICTUS_PRESSURE_PLATE = BLOCKS.register(
            "cambylictus_pressure_plate", () -> new TreePressurePlate(CAMBYLICTUS_BLOCK_SET));
    public static final RegistryObject<Item> CAMBYLICTUS_PRESSURE_PLATE_ITEM = ITEMS.register(
            "cambylictus_pressure_plate", () -> new BlockItem(CAMBYLICTUS_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> CAMBYLICTUS_FENCE_BLOCK = BLOCKS.register(
            "cambylictus_fence", TreeFence::new);
    public static final RegistryObject<Item> CAMBYLICTUS_FENCE_ITEM = ITEMS.register(
            "cambylictus_fence", () -> new BlockItem(CAMBYLICTUS_FENCE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> CAMBYLICTUS_FENCE_GATE = BLOCKS.register(
            "cambylictus_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), CAMBYLICTUS_WOOD_TYPE));
    public static final RegistryObject<Item> CAMBYLICTUS_FENCE_GATE_ITEM = ITEMS.register(
            "cambylictus_fence_gate", () -> new BlockItem(CAMBYLICTUS_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> CAMBYLICTUS_DOOR_BLOCK = BLOCKS.register(
            "cambylictus_door", () -> new TreeDoor(CAMBYLICTUS_BLOCK_SET));
    public static final RegistryObject<Item> CAMBYLICTUS_DOOR_ITEM = ITEMS.register(
            "cambylictus_door", () -> new BlockItem(CAMBYLICTUS_DOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> CAMBYLICTUS_TRAPDOOR_BLOCK = BLOCKS.register(
            "cambylictus_trapdoor", () -> new TreeTrapdoor(CAMBYLICTUS_BLOCK_SET));
    public static final RegistryObject<Item> CAMBYLICTUS_TRAPDOOR_ITEM = ITEMS.register(
            "cambylictus_trapdoor", () -> new BlockItem(CAMBYLICTUS_TRAPDOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> CAMBYLICTUS_SIGN = BLOCKS.register(
            "cambylictus_sign", () -> new TreeStandingSignCambylictus(CAMBYLICTUS_WOOD_TYPE));
    public static final RegistryObject<Block> CAMBYLICTUS_WALL_SIGN = BLOCKS.register(
            "cambylictus_wall_sign", () -> new TreeWallSignCambylictus(CAMBYLICTUS_WOOD_TYPE));
    public static final RegistryObject<Item> CAMBYLICTUS_SIGN_ITEM = ITEMS.register(
            "cambylictus_sign", () -> new SignItem(new Item.Properties(), CAMBYLICTUS_SIGN.get(), CAMBYLICTUS_WALL_SIGN.get()));
    public static final RegistryObject<Block> CAMBYLICTUS_HANGING_SIGN = BLOCKS.register(
            "cambylictus_hanging_sign", () -> new TreeHangingSignCambylictus(CAMBYLICTUS_WOOD_TYPE));
    public static final RegistryObject<Block> CAMBYLICTUS_WALL_HANGING_SIGN = BLOCKS.register(
            "cambylictus_wall_hanging_sign", () -> new TreeWallHangingSignCambylictus(CAMBYLICTUS_WOOD_TYPE));
    public static final RegistryObject<Item> CAMBYLICTUS_HANGING_SIGN_ITEM = ITEMS.register(
            "cambylictus_hanging_sign", () -> new HangingSignItem(CAMBYLICTUS_HANGING_SIGN.get(), CAMBYLICTUS_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> CAMBYLICTUS_BOAT = ITEMS.register("cambylictus_boat",
            () -> new ModBoatItem(false, ModEntityTypes.CAMBYLICTUS_BOAT::get, new Item.Properties()));
    public static final RegistryObject<Item> CAMBYLICTUS_CHEST_BOAT = ITEMS.register("cambylictus_chest_boat",
            () -> new ModBoatItem(false, ModEntityTypes.CAMBYLICTUS_CHEST_BOAT::get, new Item.Properties()));

    public static final RegistryObject<Block> PERLOTE_STAIRS = BLOCKS.register(
            "perlote_stairs", () -> new TreeStairs(PERLOTE_PLANKS.get().defaultBlockState()));
    public static final RegistryObject<Item> PERLOTE_STAIRS_ITEM = ITEMS.register(
            "perlote_stairs", () -> new BlockItem(PERLOTE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> PERLOTE_SLAB = BLOCKS.register(
            "perlote_slab", TreeSlab::new);
    public static final RegistryObject<Item> PERLOTE_SLAB_ITEM = ITEMS.register(
            "perlote_slab", () -> new BlockItem(PERLOTE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> PERLOTE_BUTTON = BLOCKS.register(
            "perlote_button", () -> new TreeButton(PERLOTE_BLOCK_SET));
    public static final RegistryObject<Item> PERLOTE_BUTTON_ITEM = ITEMS.register(
            "perlote_button", () -> new BlockItem(PERLOTE_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Block> PERLOTE_PRESSURE_PLATE = BLOCKS.register(
            "perlote_pressure_plate", () -> new TreePressurePlate(PERLOTE_BLOCK_SET));
    public static final RegistryObject<Item> PERLOTE_PRESSURE_PLATE_ITEM = ITEMS.register(
            "perlote_pressure_plate", () -> new BlockItem(PERLOTE_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> PERLOTE_FENCE_BLOCK = BLOCKS.register(
            "perlote_fence", TreeFence::new);
    public static final RegistryObject<Item> PERLOTE_FENCE_ITEM = ITEMS.register(
            "perlote_fence", () -> new BlockItem(PERLOTE_FENCE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> PERLOTE_FENCE_GATE = BLOCKS.register(
            "perlote_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), PERLOTE_WOOD_TYPE));
    public static final RegistryObject<Item> PERLOTE_FENCE_GATE_ITEM = ITEMS.register(
            "perlote_fence_gate", () -> new BlockItem(PERLOTE_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> PERLOTE_DOOR_BLOCK = BLOCKS.register(
            "perlote_door", () -> new TreeDoor(PERLOTE_BLOCK_SET));
    public static final RegistryObject<Item> PERLOTE_DOOR_ITEM = ITEMS.register(
            "perlote_door", () -> new BlockItem(PERLOTE_DOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> PERLOTE_TRAPDOOR_BLOCK = BLOCKS.register(
            "perlote_trapdoor", () -> new TreeTrapdoor(PERLOTE_BLOCK_SET));
    public static final RegistryObject<Item> PERLOTE_TRAPDOOR_ITEM = ITEMS.register(
            "perlote_trapdoor", () -> new BlockItem(PERLOTE_TRAPDOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> PERLOTE_SIGN = BLOCKS.register(
            "perlote_sign", () -> new TreeStandingSignPerlote(PERLOTE_WOOD_TYPE));
    public static final RegistryObject<Block> PERLOTE_WALL_SIGN = BLOCKS.register(
            "perlote_wall_sign", () -> new TreeWallSignPerlote(PERLOTE_WOOD_TYPE));
    public static final RegistryObject<Item> PERLOTE_SIGN_ITEM = ITEMS.register(
            "perlote_sign", () -> new SignItem(new Item.Properties(), PERLOTE_SIGN.get(), PERLOTE_WALL_SIGN.get()));
    public static final RegistryObject<Block> PERLOTE_HANGING_SIGN = BLOCKS.register(
            "perlote_hanging_sign", () -> new TreeHangingSignPerlote(PERLOTE_WOOD_TYPE));
    public static final RegistryObject<Block> PERLOTE_WALL_HANGING_SIGN = BLOCKS.register(
            "perlote_wall_hanging_sign", () -> new TreeWallHangingSignPerlote(PERLOTE_WOOD_TYPE));
    public static final RegistryObject<Item> PERLOTE_HANGING_SIGN_ITEM = ITEMS.register(
            "perlote_hanging_sign", () -> new HangingSignItem(PERLOTE_HANGING_SIGN.get(), PERLOTE_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> PERLOTE_BOAT = ITEMS.register("perlote_boat",
            () -> new ModBoatItem(false, ModEntityTypes.PERLOTE_BOAT::get, new Item.Properties()));
    public static final RegistryObject<Item> PERLOTE_CHEST_BOAT = ITEMS.register("perlote_chest_boat",
            () -> new ModBoatItem(false, ModEntityTypes.PERLOTE_CHEST_BOAT::get, new Item.Properties()));

    public static final RegistryObject<Block> RUTIGER_STAIRS = BLOCKS.register(
            "rutiger_stairs", () -> new TreeStairs(RUTIGER_PLANKS.get().defaultBlockState()));
    public static final RegistryObject<Item> RUTIGER_STAIRS_ITEM = ITEMS.register(
            "rutiger_stairs", () -> new BlockItem(RUTIGER_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> RUTIGER_SLAB = BLOCKS.register(
            "rutiger_slab", TreeSlab::new);
    public static final RegistryObject<Item> RUTIGER_SLAB_ITEM = ITEMS.register(
            "rutiger_slab", () -> new BlockItem(RUTIGER_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> RUTIGER_BUTTON = BLOCKS.register(
            "rutiger_button", () -> new TreeButton(RUTIGER_BLOCK_SET));
    public static final RegistryObject<Item> RUTIGER_BUTTON_ITEM = ITEMS.register(
            "rutiger_button", () -> new BlockItem(RUTIGER_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Block> RUTIGER_PRESSURE_PLATE = BLOCKS.register(
            "rutiger_pressure_plate", () -> new TreePressurePlate(RUTIGER_BLOCK_SET));
    public static final RegistryObject<Item> RUTIGER_PRESSURE_PLATE_ITEM = ITEMS.register(
            "rutiger_pressure_plate", () -> new BlockItem(RUTIGER_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> RUTIGER_FENCE_BLOCK = BLOCKS.register(
            "rutiger_fence", TreeFence::new);
    public static final RegistryObject<Item> RUTIGER_FENCE_ITEM = ITEMS.register(
            "rutiger_fence", () -> new BlockItem(RUTIGER_FENCE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> RUTIGER_FENCE_GATE = BLOCKS.register(
            "rutiger_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), RUTIGER_WOOD_TYPE));
    public static final RegistryObject<Item> RUTIGER_FENCE_GATE_ITEM = ITEMS.register(
            "rutiger_fence_gate", () -> new BlockItem(RUTIGER_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> RUTIGER_DOOR_BLOCK = BLOCKS.register(
            "rutiger_door", () -> new TreeDoor(RUTIGER_BLOCK_SET));
    public static final RegistryObject<Item> RUTIGER_DOOR_ITEM = ITEMS.register(
            "rutiger_door", () -> new BlockItem(RUTIGER_DOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> RUTIGER_TRAPDOOR_BLOCK = BLOCKS.register(
            "rutiger_trapdoor", () -> new TreeTrapdoor(RUTIGER_BLOCK_SET));
    public static final RegistryObject<Item> RUTIGER_TRAPDOOR_ITEM = ITEMS.register(
            "rutiger_trapdoor", () -> new BlockItem(RUTIGER_TRAPDOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> RUTIGER_SIGN = BLOCKS.register(
            "rutiger_sign", () -> new TreeStandingSignRutiger(RUTIGER_WOOD_TYPE));
    public static final RegistryObject<Block> RUTIGER_WALL_SIGN = BLOCKS.register(
            "rutiger_wall_sign", () -> new TreeWallSignRutiger(RUTIGER_WOOD_TYPE));
    public static final RegistryObject<Item> RUTIGER_SIGN_ITEM = ITEMS.register(
            "rutiger_sign", () -> new SignItem(new Item.Properties(), RUTIGER_SIGN.get(), RUTIGER_WALL_SIGN.get()));
    public static final RegistryObject<Block> RUTIGER_HANGING_SIGN = BLOCKS.register(
            "rutiger_hanging_sign", () -> new TreeHangingSignRutiger(RUTIGER_WOOD_TYPE));
    public static final RegistryObject<Block> RUTIGER_WALL_HANGING_SIGN = BLOCKS.register(
            "rutiger_wall_hanging_sign", () -> new TreeWallHangingSignRutiger(RUTIGER_WOOD_TYPE));
    public static final RegistryObject<Item> RUTIGER_HANGING_SIGN_ITEM = ITEMS.register(
            "rutiger_hanging_sign", () -> new HangingSignItem(RUTIGER_HANGING_SIGN.get(), RUTIGER_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> RUTIGER_BOAT = ITEMS.register("rutiger_boat",
            () -> new ModBoatItem(false, ModEntityTypes.RUTIGER_BOAT::get, new Item.Properties()));
    public static final RegistryObject<Item> RUTIGER_CHEST_BOAT = ITEMS.register("rutiger_chest_boat",
            () -> new ModBoatItem(false, ModEntityTypes.RUTIGER_CHEST_BOAT::get, new Item.Properties()));

    public static final RegistryObject<Block> POLAR_STAIRS = BLOCKS.register(
            "polar_stairs", () -> new TreeStairs(POLAR_PLANKS.get().defaultBlockState()));
    public static final RegistryObject<Item> POLAR_STAIRS_ITEM = ITEMS.register(
            "polar_stairs", () -> new BlockItem(POLAR_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Block> POLAR_SLAB = BLOCKS.register(
            "polar_slab", TreeSlab::new);
    public static final RegistryObject<Item> POLAR_SLAB_ITEM = ITEMS.register(
            "polar_slab", () -> new BlockItem(POLAR_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Block> POLAR_BUTTON = BLOCKS.register(
            "polar_button", () -> new TreeButton(POLAR_BLOCK_SET));
    public static final RegistryObject<Item> POLAR_BUTTON_ITEM = ITEMS.register(
            "polar_button", () -> new BlockItem(POLAR_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Block> POLAR_PRESSURE_PLATE = BLOCKS.register(
            "polar_pressure_plate", () -> new TreePressurePlate(POLAR_BLOCK_SET));
    public static final RegistryObject<Item> POLAR_PRESSURE_PLATE_ITEM = ITEMS.register(
            "polar_pressure_plate", () -> new BlockItem(POLAR_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> POLAR_FENCE_BLOCK = BLOCKS.register(
            "polar_fence", TreeFence::new);
    public static final RegistryObject<Item> POLAR_FENCE_ITEM = ITEMS.register(
            "polar_fence", () -> new BlockItem(POLAR_FENCE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> POLAR_FENCE_GATE = BLOCKS.register(
            "polar_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), POLAR_WOOD_TYPE));
    public static final RegistryObject<Item> POLAR_FENCE_GATE_ITEM = ITEMS.register(
            "polar_fence_gate", () -> new BlockItem(POLAR_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Block> POLAR_DOOR_BLOCK = BLOCKS.register(
            "polar_door", () -> new TreeDoor(POLAR_BLOCK_SET));
    public static final RegistryObject<Item> POLAR_DOOR_ITEM = ITEMS.register(
            "polar_door", () -> new BlockItem(POLAR_DOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> POLAR_TRAPDOOR_BLOCK = BLOCKS.register(
            "polar_trapdoor", () -> new TreeTrapdoor(POLAR_BLOCK_SET));
    public static final RegistryObject<Item> POLAR_TRAPDOOR_ITEM = ITEMS.register(
            "polar_trapdoor", () -> new BlockItem(POLAR_TRAPDOOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> POLAR_SIGN = BLOCKS.register(
            "polar_sign", () -> new TreeStandingSignPolar(POLAR_WOOD_TYPE));
    public static final RegistryObject<Block> POLAR_WALL_SIGN = BLOCKS.register(
            "polar_wall_sign", () -> new TreeWallSignPolar(POLAR_WOOD_TYPE));
    public static final RegistryObject<Item> POLAR_SIGN_ITEM = ITEMS.register(
            "polar_sign", () -> new SignItem(new Item.Properties(), POLAR_SIGN.get(), POLAR_WALL_SIGN.get()));
    public static final RegistryObject<Block> POLAR_HANGING_SIGN = BLOCKS.register(
            "polar_hanging_sign", () -> new TreeHangingSignPolar(POLAR_WOOD_TYPE));
    public static final RegistryObject<Block> POLAR_WALL_HANGING_SIGN = BLOCKS.register(
            "polar_wall_hanging_sign", () -> new TreeWallHangingSignPolar(POLAR_WOOD_TYPE));
    public static final RegistryObject<Item> POLAR_HANGING_SIGN_ITEM = ITEMS.register(
            "polar_hanging_sign", () -> new HangingSignItem(POLAR_HANGING_SIGN.get(), POLAR_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> POLAR_BOAT = ITEMS.register("polar_boat",
            () -> new ModBoatItem(false, ModEntityTypes.POLAR_BOAT::get, new Item.Properties()));
    public static final RegistryObject<Item> POLAR_CHEST_BOAT = ITEMS.register("polar_chest_boat",
            () -> new ModBoatItem(false, ModEntityTypes.POLAR_CHEST_BOAT::get, new Item.Properties()));

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
                    AK_WOOD_TYPE
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
                    HEART_BERRY_WOOD_TYPE
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
    public static final RegistryObject<Item> NOVADIVE_BLUEPRINT = ITEMS.register("novadive_blueprint",
            () -> new ShipBlueprintItem("novadive", "Novadive", new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> FLASHFIRE_BLUEPRINT = ITEMS.register("flashfire_blueprint",
            () -> new ShipBlueprintItem("flashfire", "Flashfire", new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> NOVADIVE = ITEMS.register("novadive",
            () -> new NovadiveItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FLASHFIRE = ITEMS.register("flashfire",
            () -> new FlashfireItem(new Item.Properties().stacksTo(1)));

    // #ENTITIES
    public static final RegistryObject<EntityType<AcidSpiderEntity>> ACID_SPIDER =
            ENTITY_TYPES.register("acid_spider", () -> EntityType.Builder.of(AcidSpiderEntity::new, MobCategory.MONSTER)
                    .sized(1.5f, 1.5f).build("acid_spider"));
    public static final RegistryObject<EntityType<WingmawEntity>> WINGMAW =
            ENTITY_TYPES.register("wingmaw", () -> EntityType.Builder.of(WingmawEntity::new, MobCategory.MONSTER)
                    .sized(1.0f, 1.0f).build("wingmaw"));
    public static final RegistryObject<EntityType<VonskrEntity>> VONSKR =
            ENTITY_TYPES.register("vonskr", () -> EntityType.Builder.of(VonskrEntity::new, MobCategory.MONSTER)
                    .sized(1.15F, 1.05F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("vonskr"));
    public static final RegistryObject<EntityType<ForceUserEntity>> JEDI_FORCE_USER =
            ENTITY_TYPES.register("jedi_force_user", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("jedi_force_user"));
    public static final RegistryObject<EntityType<ForceUserEntity>> SITH_FORCE_USER =
            ENTITY_TYPES.register("sith_force_user", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("sith_force_user"));
    public static final RegistryObject<EntityType<ForceUserEntity>> SITH_GHOST =
            ENTITY_TYPES.register("sith_ghost", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("sith_ghost"));
    public static final RegistryObject<EntityType<ForceUserEntity>> SITH_LORD_GHOST =
            ENTITY_TYPES.register("sith_lord_ghost", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(80)
                    .updateInterval(2)
                    .build("sith_lord_ghost"));
    public static final RegistryObject<EntityType<ForceUserEntity>> SITH_LORD =
            ENTITY_TYPES.register("sith_lord", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(80)
                    .updateInterval(2)
                    .build("sith_lord"));
    public static final RegistryObject<EntityType<ForceUserEntity>> JEDI_MASTER =
            ENTITY_TYPES.register("jedi_master", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(80)
                    .updateInterval(2)
                    .build("jedi_master"));
    public static final RegistryObject<EntityType<ForceUserEntity>> NEUTRAL_FORCE_USER =
            ENTITY_TYPES.register("neutral_force_user", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("neutral_force_user"));
    public static final RegistryObject<EntityType<ForceUserEntity>> NEUTRAL_MASTER =
            ENTITY_TYPES.register("neutral_master", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(80)
                    .updateInterval(2)
                    .build("neutral_master"));
    public static final RegistryObject<EntityType<ForceUserEntity>> SITH_APPRENTICE =
            ENTITY_TYPES.register("sith_apprentice", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.CREATURE)
                    .sized(0.48F, 1.52F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("sith_apprentice"));
    public static final RegistryObject<EntityType<ForceUserEntity>> JEDI_PADAWAN =
            ENTITY_TYPES.register("jedi_padawan", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.CREATURE)
                    .sized(0.48F, 1.52F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("jedi_padawan"));
    public static final RegistryObject<EntityType<ForceUserEntity>> NEUTRAL_PADAWAN =
            ENTITY_TYPES.register("neutral_padawan", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.CREATURE)
                    .sized(0.48F, 1.52F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("neutral_padawan"));
    public static final RegistryObject<EntityType<ForceUserEntity>> JEDI_TEMPLE_GUARD =
            ENTITY_TYPES.register("jedi_temple_guard", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("jedi_temple_guard"));
    public static final RegistryObject<EntityType<ForceUserEntity>> SITH_GUARD =
            ENTITY_TYPES.register("sith_guard", () -> EntityType.Builder.<ForceUserEntity>of(ForceUserEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("sith_guard"));

    public static final RegistryObject<Item> JEDI_FORCE_USER_SPAWN_EGG = ITEMS.register("jedi_force_user_spawn_egg",
            () -> new ForgeSpawnEggItem(JEDI_FORCE_USER, 0x4F7DDB, 0xD8C69A, new Item.Properties()));
    public static final RegistryObject<Item> SITH_FORCE_USER_SPAWN_EGG = ITEMS.register("sith_force_user_spawn_egg",
            () -> new ForgeSpawnEggItem(SITH_FORCE_USER, 0x7A0C0C, 0xF0B04F, new Item.Properties()));
    public static final RegistryObject<Item> SITH_GHOST_SPAWN_EGG = ITEMS.register("sith_ghost_spawn_egg",
            () -> new ForgeSpawnEggItem(SITH_GHOST, 0x5A0C0C, 0xA6C8FF, new Item.Properties()));
    public static final RegistryObject<Item> SITH_LORD_GHOST_SPAWN_EGG = ITEMS.register("sith_lord_ghost_spawn_egg",
            () -> new ForgeSpawnEggItem(SITH_LORD_GHOST, 0x230000, 0xD0E4FF, new Item.Properties()));
    public static final RegistryObject<Item> SITH_LORD_SPAWN_EGG = ITEMS.register("sith_lord_spawn_egg",
            () -> new ForgeSpawnEggItem(SITH_LORD, 0x240000, 0xFF2B1F, new Item.Properties()));
    public static final RegistryObject<Item> JEDI_MASTER_SPAWN_EGG = ITEMS.register("jedi_master_spawn_egg",
            () -> new ForgeSpawnEggItem(JEDI_MASTER, 0x2E5FDB, 0xF1E0B1, new Item.Properties()));
    public static final RegistryObject<Item> NEUTRAL_FORCE_USER_SPAWN_EGG = ITEMS.register("neutral_force_user_spawn_egg",
            () -> new ForgeSpawnEggItem(NEUTRAL_FORCE_USER, 0xF5F5F5, 0x8FA0B3, new Item.Properties()));
    public static final RegistryObject<Item> NEUTRAL_MASTER_SPAWN_EGG = ITEMS.register("neutral_master_spawn_egg",
            () -> new ForgeSpawnEggItem(NEUTRAL_MASTER, 0xFFFFFF, 0x66707C, new Item.Properties()));
    public static final RegistryObject<Item> SITH_APPRENTICE_SPAWN_EGG = ITEMS.register("sith_apprentice_spawn_egg",
            () -> new ForgeSpawnEggItem(SITH_APPRENTICE, 0x4A0000, 0xE06042, new Item.Properties()));
    public static final RegistryObject<Item> JEDI_PADAWAN_SPAWN_EGG = ITEMS.register("jedi_padawan_spawn_egg",
            () -> new ForgeSpawnEggItem(JEDI_PADAWAN, 0x4F7DDB, 0xFFF4C8, new Item.Properties()));
    public static final RegistryObject<Item> NEUTRAL_PADAWAN_SPAWN_EGG = ITEMS.register("neutral_padawan_spawn_egg",
            () -> new ForgeSpawnEggItem(NEUTRAL_PADAWAN, 0xF7F7F7, 0xC8D0D8, new Item.Properties()));
    public static final RegistryObject<Item> JEDI_TEMPLE_GUARD_SPAWN_EGG = ITEMS.register("jedi_temple_guard_spawn_egg",
            () -> new ForgeSpawnEggItem(JEDI_TEMPLE_GUARD, 0xE6C34A, 0xF7F0DA, new Item.Properties()));
    public static final RegistryObject<Item> SITH_GUARD_SPAWN_EGG = ITEMS.register("sith_guard_spawn_egg",
            () -> new ForgeSpawnEggItem(SITH_GUARD, 0x1A0A0A, 0xD21919, new Item.Properties()));
    public static final RegistryObject<EntityType<SeatEntity>> SEAT =
            ENTITY_TYPES.register("seat",
                    () -> EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                            .sized(0.0001F, 0.0001F)
                            .build("seat"));
    public static final RegistryObject<EntityType<ForceBeamEffectEntity>> FORCE_BEAM_EFFECT =
            ENTITY_TYPES.register("force_beam_effect", () -> EntityType.Builder.<ForceBeamEffectEntity>of(ForceBeamEffectEntity::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("force_beam_effect"));
    public static final RegistryObject<EntityType<ForcePushWaveEntity>> FORCE_PUSH_WAVE =
            ENTITY_TYPES.register("force_push_wave", () -> EntityType.Builder.<ForcePushWaveEntity>of(ForcePushWaveEntity::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("force_push_wave"));
    public static final RegistryObject<EntityType<ForceAbilityEffectEntity>> FORCE_ABILITY_EFFECT =
            ENTITY_TYPES.register("force_ability_effect", () -> EntityType.Builder.<ForceAbilityEffectEntity>of(ForceAbilityEffectEntity::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("force_ability_effect"));
    public static final RegistryObject<EntityType<ForceProjectionCloneEntity>> FORCE_PROJECTION_CLONE =
            ENTITY_TYPES.register("force_projection_clone", () -> EntityType.Builder.<ForceProjectionCloneEntity>of(ForceProjectionCloneEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(64)
                    .updateInterval(2)
                    .build("force_projection_clone"));
    public static final RegistryObject<EntityType<ThrownLightsaberEntity>> THROWN_LIGHTSABER =
            ENTITY_TYPES.register("thrown_lightsaber", () -> EntityType.Builder.<ThrownLightsaberEntity>of(ThrownLightsaberEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("thrown_lightsaber"));
    public static final RegistryObject<EntityType<NovadiveEntity>> NOVADIVE_ENTITY =
            ENTITY_TYPES.register("novadive", () -> EntityType.Builder.<NovadiveEntity>of(NovadiveEntity::new, MobCategory.MISC)
                    .sized(5.5F, 2.6F)
                    .clientTrackingRange(128)
                    .updateInterval(1)
                    .build("novadive"));
    public static final RegistryObject<EntityType<FlashfireEntity>> FLASHFIRE_ENTITY =
            ENTITY_TYPES.register("flashfire", () -> EntityType.Builder.<FlashfireEntity>of(FlashfireEntity::new, MobCategory.MISC)
                    .sized(5.5F, 2.6F)
                    .clientTrackingRange(128)
                    .updateInterval(1)
                    .build("flashfire"));

    public galaxyunderchaos() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        PARTICLE_TYPES.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModEffects.register(modEventBus);
        ModBiomes.BIOMES.register(modEventBus);
        ModStructureTypes.register(modEventBus);
        CreativeMenuTabs.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
//        registerLightsabers();
        registerAdvancedLightsaberContent();
        ModSounds.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModLootModifiers.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(LightsaberFormEventHandler.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LightsaberFormNetworking.registerPackets(event);
        ForceNetworking.registerPackets(event);
        ShipNetworking.registerPackets(event);
    }

//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//        LOGGER.info("HELLO from server starting");
//    }

}
