package server.galaxyunderchaos.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.function.Supplier;

import static server.galaxyunderchaos.galaxyunderchaos.*;

public class CreativeMenuTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, galaxyunderchaos.MODID);

    public static final java.util.function.Supplier<CreativeModeTab> GALAXY_UNDER_CHAOS_TAB = CREATIVE_MODE_TABS.register("galaxy_under_chaos",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(TEMPLE_STONE.get())) // Use ModBlocks.TEMPLE_STONE
                    .title(Component.translatable("creativetab.galaxyunderchaos.galaxy_under_chaos_blocks"))
                    .displayItems((parameters, output) -> {
                        output.accept(TEMPLE_STONE.get().asItem());
                        output.accept(TEMPLE_STONE_HOLOBOOK.get().asItem());
                        output.accept(TEMPLE_STONE_LIGHTS.get().asItem());
                        output.accept(TEMPLE_STONE_PILLAR.get().asItem());
                        output.accept(TEMPLE_STONE_STAIRS.get().asItem());
                        output.accept(TEMPLE_STONE_SLAB.get().asItem());
                        output.accept(TEMPLE_STONE_WALL.get().asItem());
                        output.accept(TYTHON_TEMPLE_STONE.get().asItem());
                        output.accept(CHISELED_TYTHON_TEMPLE_STONE.get().asItem());
                        output.accept(TYTHON_TEMPLE_STONE_LIGHTS.get().asItem());
                        output.accept(TYTHON_TEMPLE_STONE_PILLAR.get().asItem());
                        output.accept(TYTHON_TEMPLE_STONE_STAIRS.get().asItem());
                        output.accept(TYTHON_TEMPLE_STONE_SLAB.get().asItem());
                        output.accept(TYTHON_TEMPLE_STONE_WALL.get().asItem());
                        output.accept(ASHLA_TEMPLE_STONE.get().asItem());
                        output.accept(ASHLA_TEMPLE_STONE_PILLAR.get().asItem());
                        output.accept(ASHLA_TEMPLE_STONE_STAIRS.get().asItem());
                        output.accept(ASHLA_TEMPLE_STONE_SLAB.get().asItem());
                        output.accept(ASHLA_TEMPLE_STONE_WALL.get().asItem());
                        output.accept(BOGAN_TEMPLE_STONE.get().asItem());
                        output.accept(BOGAN_TEMPLE_STONE_PILLAR.get().asItem());
                        output.accept(BOGAN_TEMPLE_STONE_STAIRS.get().asItem());
                        output.accept(BOGAN_TEMPLE_STONE_SLAB.get().asItem());
                        output.accept(BOGAN_TEMPLE_STONE_WALL.get().asItem());
                        output.accept(DARK_TEMPLE_STONE.get().asItem());
                        output.accept(DARK_TEMPLE_STONE_HOLOBOOK.get().asItem());
                        output.accept(DARK_TEMPLE_STONE_LIGHTS.get().asItem());
                        output.accept(DARK_TEMPLE_STONE_PILLAR.get().asItem());
                        output.accept(DARK_TEMPLE_STONE_STAIRS.get().asItem());
                        output.accept(DARK_TEMPLE_STONE_SLAB.get().asItem());
                        output.accept(DARK_TEMPLE_STONE_WALL.get().asItem());
                        output.accept(ANCIENT_TEMPLE_STONE.get().asItem());
                        output.accept(ANCIENT_TEMPLE_STONE_HOLOBOOK.get().asItem());
                        output.accept(ANCIENT_TEMPLE_STONE_PILLAR.get().asItem());
                        output.accept(ANCIENT_TEMPLE_STONE_CRACKED.get().asItem());
                        output.accept(ANCIENT_TEMPLE_STONE_STAIRS.get().asItem());
                        output.accept(ANCIENT_TEMPLE_STONE_SLAB.get().asItem());
                        output.accept(ANCIENT_TEMPLE_STONE_WALL.get().asItem());
                        output.accept(PURPLE_CRYSTAL_ORE.get().asItem());
                        output.accept(MAGENTA_CRYSTAL_ORE.get().asItem());
                        output.accept(PINK_CRYSTAL_ORE.get().asItem());
                        output.accept(ORANGE_CRYSTAL_ORE.get().asItem());
                        output.accept(YELLOW_CRYSTAL_ORE.get().asItem());
                        output.accept(LIME_GREEN_CRYSTAL_ORE.get().asItem());
                        output.accept(GREEN_CRYSTAL_ORE.get().asItem());
                        output.accept(TURQUOISE_CRYSTAL_ORE.get().asItem());
                        output.accept(CYAN_CRYSTAL_ORE.get().asItem());
                        output.accept(BLUE_CRYSTAL_ORE.get().asItem());
                        output.accept(WHITE_CRYSTAL_ORE.get().asItem());
                        output.accept(AMBER_CRYSTAL_ORE.get().asItem());
                        output.accept(GOLD_CRYSTAL_ORE.get().asItem());
                        output.accept(LIGHT_BLUE_CRYSTAL_ORE.get().asItem());
                        output.accept(DARK_BLUE_CRYSTAL_ORE.get().asItem());
                        output.accept(DEEP_VIOLET_CRYSTAL_ORE.get().asItem());
                        output.accept(ARCTIC_BLUE_CRYSTAL_ORE.get().asItem());
                        output.accept(ROSE_PINK_CRYSTAL_ORE.get().asItem());
                        output.accept(MALACHITE_OBSIDIAN.get().asItem());
                        output.accept(CHROMIUM_ORE.get().asItem());
                        output.accept(CHROMIUM_DEEPSLATE_ORE.get().asItem());
                        output.accept(TITANIUM_ORE.get().asItem());
                        output.accept(TITANIUM_DEEPSLATE_ORE.get().asItem());
                        output.accept(BLEEDING_TABLE.get().asItem());
                        output.accept(LIGHTSABER_CRAFTING_TABLE.get().asItem());
                        output.accept(AK_LOG_ITEM.get());               // 1  log
                        output.accept(STRIPPED_AK_LOG_ITEM.get());      // 2  stripped log
                        output.accept(AK_WOOD_ITEM.get());              // 3  wood
                        output.accept(STRIPPED_AK_WOOD_ITEM.get());     // 4  stripped wood
                        output.accept(AK_PLANKS_ITEM.get());            // 5  planks
                        output.accept(AK_STAIRS_ITEM.get());            // 6  stairs
                        output.accept(AK_SLAB_ITEM.get());              // 7  slab
                        output.accept(AK_FENCE_ITEM.get());             // 8  fence
                        output.accept(AK_FENCE_GATE_ITEM.get());        // 9  fence gate
                        output.accept(AK_DOOR_ITEM.get());              // 10 door
                        output.accept(AK_TRAPDOOR_ITEM.get());          // 11 trapdoor
                        output.accept(AK_PRESSURE_PLATE_ITEM.get());    // 12 pressure plate
                        output.accept(AK_BUTTON_ITEM.get());            // 13 button
                        output.accept(AK_LEAVES_ITEM.get());            // 14 leaves   (moves down)
                        output.accept(AK_SAPLING_ITEM.get());           // 15 sapling  (moves down)
                        output.accept(AK_SIGN_ITEM.get());           // 15 sapling  (moves down)
                        output.accept(AK_HANGING_SIGN_ITEM.get());           // 15 sapling  (moves down)
                        output.accept(AK_BOAT.get());
                        output.accept(AK_CHEST_BOAT.get());
                        output.accept(HEART_BERRY_LOG_ITEM.get());               // 1  log
                        output.accept(STRIPPED_HEART_BERRY_LOG_ITEM.get());      // 2  stripped log
                        output.accept(HEART_BERRY_WOOD_ITEM.get());              // 3  wood
                        output.accept(STRIPPED_HEART_BERRY_WOOD_ITEM.get());     // 4  stripped wood
                        output.accept(HEART_BERRY_PLANKS_ITEM.get());            // 5  planks
                        output.accept(HEART_BERRY_STAIRS_ITEM.get());            // 6  stairs
                        output.accept(HEART_BERRY_SLAB_ITEM.get());              // 7  slab
                        output.accept(HEART_BERRY_FENCE_ITEM.get());             // 8  fence
                        output.accept(HEART_BERRY_FENCE_GATE_ITEM.get());        // 9  fence gate
                        output.accept(HEART_BERRY_DOOR_ITEM.get());              // 10 door
                        output.accept(HEART_BERRY_TRAPDOOR_ITEM.get());          // 11 trapdoor
                        output.accept(HEART_BERRY_PRESSURE_PLATE_ITEM.get());    // 12 pressure plate
                        output.accept(HEART_BERRY_BUTTON_ITEM.get());            // 13 button
                        output.accept(HEART_BERRY_LEAVES_ITEM.get());               // 14 Leaves
                        output.accept(HEART_BERRY_FRUIT_LEAVES_ITEM.get());            // 15 Fruit Leaves
                        output.accept(HEART_BERRY_SAPLING_ITEM.get());           // 16 sapling  (moves down)
                        output.accept(HEART_BERRY_SIGN_ITEM.get());           // 17 sapling  (moves down)
                        output.accept(HEART_BERRY_HANGING_SIGN_ITEM.get());           // 15 sapling  (moves down)
                        output.accept(HEART_BERRY_BOAT.get());
                        output.accept(HEART_BERRY_CHEST_BOAT.get());
                        output.accept(MUSHROOM_STEM_ITEM.get());
                        output.accept(PINK_MUSHROOM_BLOCK_ITEM.get());
                        output.accept(WHITE_MUSHROOM_BLOCK_ITEM.get());


                    }).build());

    public static final Supplier<CreativeModeTab> GALAXY_UNDER_CHAOS_ITEMS_TAB = CREATIVE_MODE_TABS.register("galaxy_under_chaos_items",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(SHUURA.get()))
                    .title(Component.translatable("creativetab.galaxyunderchaos.galaxy_under_chaos_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(CHROMIUM_INGOT.get());
                        output.accept(TITANIUM_INGOT.get());
                        output.accept(TITANIUM_CHROMIUM_INGOT.get());
//                        output.accept(REACTOR_ASSEMBLY.get());
                        output.accept(NAVIGATION_COMPUTER.get());
                        output.accept(PORTAL_ITEM.get());
                        output.accept(TYTHON_PORTAL_ITEM.get());
                        output.accept(ASHLA_PORTAL_ITEM.get());
                        output.accept(BOGAN_PORTAL_ITEM.get());
                        output.accept(NABOO_PORTAL_ITEM.get());
                        output.accept(ILUM_PORTAL_ITEM.get());
                        output.accept(MUSTAFAR_PORTAL_ITEM.get());
                        output.accept(OSSUS_PORTAL_ITEM.get());
                        output.accept(MALACHOR_PORTAL_ITEM.get());
                        output.accept(KORRIBAN_PORTAL_ITEM.get());
                        output.accept(DANTOOINE_PORTAL_ITEM.get());
                        output.accept(PURPLE_KYBER.get());
                        output.accept(MAGENTA_KYBER.get());
                        output.accept(PINK_KYBER.get());
                        output.accept(RED_KYBER.get());
                        output.accept(BLOOD_ORANGE_KYBER.get());
                        output.accept(ORANGE_KYBER.get());
                        output.accept(YELLOW_KYBER.get());
                        output.accept(LIME_GREEN_KYBER.get());
                        output.accept(GREEN_KYBER.get());
                        output.accept(TURQUOISE_KYBER.get());
                        output.accept(CYAN_KYBER.get());
                        output.accept(BLUE_KYBER.get());
                        output.accept(WHITE_KYBER.get());
                        output.accept(AMBER_KYBER.get());
                        output.accept(GOLD_KYBER.get());
                        output.accept(LIGHT_BLUE_KYBER.get());
                        output.accept(DARK_BLUE_KYBER.get());
                        output.accept(MAROON_KYBER.get());
                        output.accept(DEEP_VIOLET_KYBER.get());
                        output.accept(ARCTIC_BLUE_KYBER.get());
                        output.accept(ROSE_PINK_KYBER.get());
                        output.accept(FOCUSING_CRYSTAL_COMPRESSED.get());
                        output.accept(FOCUSING_CRYSTAL_CRACKED.get());
                        output.accept(FOCUSING_CRYSTAL_INVERTING.get());
                        output.accept(FOCUSING_CRYSTAL_FINE_CUT.get());
                        output.accept(FOCUSING_CRYSTAL_PRISMATIC.get());
                        output.accept(JEDI_HOLOBOOK.get());
                        output.accept(SHII_CHO_HOLOBOOK.get());
                        output.accept(MAKASHI_HOLBOOK.get());
                        output.accept(SORESU_HOLOBOOK.get());
                        output.accept(ATARU_HOLOBOOK.get());
                        output.accept(NIMAN_HOLOBOOK.get());
                        output.accept(JEDI_HOLOCRON.get());
                        output.accept(ANCIENT_HOLOBOOK.get());
                        output.accept(ANCIENT_HOLOCRON.get());
                        output.accept(SITH_HOLOBOOK.get());
                        output.accept(SHIEN_DJEM_SO_HOLOBOOK.get());
                        output.accept(JUYO_VAAPAD_HOLOBOOK.get());
                        output.accept(SITH_HOLOCRON.get());
                        output.accept(RAW_WINGMAW_MEAT.get());
                        output.accept(COOKED_WINGMAW_MEAT.get());
                        output.accept(WINGMAW_FANG.get());
                        output.accept(WINGMAW_BLADE.get());
                        output.accept(WINGMAW_FEATHER.get());
                        output.accept(WINGMAW_HIDE.get());
                        output.accept(ACIDIC_VENOM_SAC.get());
                        output.accept(SILK_THREAD.get());
                        output.accept(CHITIN_FRAGMENTS.get());
                        output.accept(ACID_FORGED_PLATE.get());
                        output.accept(TEMPLE_GUARD_FABRIC.get());
                        output.accept(SHUURA.get());
                        output.accept(HEART_BERRY.get());



                    }).build());

    public static final Supplier<CreativeModeTab> GALAXY_UNDER_CHAOS_ENTITIES_TAB = CREATIVE_MODE_TABS.register("galaxy_under_chaos_entities",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ACID_SPIDER_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.galaxyunderchaos.galaxy_under_chaos_entities"))
                    .displayItems((parameters, output) -> {
                        output.accept(ACID_SPIDER_SPAWN_EGG.get());
                        output.accept(WINGMAW_SPAWN_EGG.get());



                    }).build());
    public static final Supplier<CreativeModeTab> GALAXY_UNDER_CHAOS_DECORATIONS_TAB = CREATIVE_MODE_TABS.register("galaxy_under_chaos_decorations",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(JEDI_GUARD_STATUE_ITEM.get()))
                    .title(Component.translatable("creativetab.galaxyunderchaos.galaxy_under_chaos_decorations"))
                    .displayItems((parameters, output) -> {
                        output.accept(JEDI_GUARD_STATUE_ITEM.get());
                        output.accept(TYTHON_JEDI_IDLE_HEAD_STATUE.get());
                        output.accept(TYTHON_JEDI_CROSSED_TORSO_STATUE.get());
                        output.accept(TYTHON_JEDI_IDLE_TORSO_STATUE.get());
                        output.accept(TYTHON_JEDI_IDLE_LEG_1_STATUE.get());
                        output.accept(TYTHON_JEDI_IDLE_LEG_2_STATUE.get());
                        output.accept(SITH_GUARD_STATUE_ITEM.get());
                        output.accept(COUNCIL_CHAIR_1_ITEM.get());
                        output.accept(COUNCIL_CHAIR_2_ITEM.get());
                        output.accept(COUNCIL_CHAIR_3_ITEM.get());
                        output.accept(COUNCIL_CHAIR_4_ITEM.get());
                        output.accept(COUNCIL_CHAIR_5_ITEM.get());
                        output.accept(TYTHON_TEMPLE_CHAIR_1_ITEM.get());
                        output.accept(TYTHON_TEMPLE_CHAIR_2_ITEM.get());
                        output.accept(TYTHON_TEMPLE_CHAIR_3_ITEM.get());
                        output.accept(TYTHON_TEMPLE_CHAIR_4_ITEM.get());
                        output.accept(TYTHON_TEMPLE_CHAIR_5_ITEM.get());
                        output.accept(JEDI_COFFIN_ITEM.get());
                        output.accept(SITH_COFFIN_ITEM.get());
                        output.accept(GROUND_SABER_STAND.get());
                        output.accept(WHITE_GROUND_SABER_STAND.get());



                    }).build());


    public static final Supplier<CreativeModeTab> GALAXY_UNDER_CHAOS_WEAPONS_TAB = CREATIVE_MODE_TABS.register("galaxy_under_chaos_weapons",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(AEGIS_HILT.get()))
                    .title(Component.translatable("creativetab.galaxyunderchaos.galaxy_under_chaos_weapons"))
                    .displayItems((parameters, output) -> {
//                        for (String color : new String[]{"red", "blue", "green", "yellow", "cyan", "white", "magenta", "purple", "pink", "lime_green", "turquoise", "orange", "blood_orange"}) {
//                            for (String hilt : new String[]{"apprentice", "chosen", "emperor", "legacy", "padawan", "resolve", "talon", "valor", "wisdom", "lost", "aegis", "grace", "guard", "harmony", "skustell", "fallen"}) {
//                                output.accept(galaxyunderchaos.LIGHTSABERS.get(color + "_" + hilt + "_lightsaber").get());
//                            }
//                        }
                        output.accept(TEMPLE_GUARD_HELMET.get());
                        output.accept(TEMPLE_GUARD_CHESTPLATE.get());
                        output.accept(TEMPLE_GUARD_LEGGINGS.get());
                        output.accept(TEMPLE_GUARD_BOOTS.get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("green_lost_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("orange_aegis_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("pink_apprentice_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_chosen_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("red_emperor_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("red_fallen_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("red_grace_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("red_guard_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_harmony_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("green_legacy_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_padawan_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_resolve_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_skustell_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("orange_talon_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("purple_valor_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_wisdom_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_knightfall_lightsaber").get());                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_padawan_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_baroshe_lightsaber").get());
                        output.accept(galaxyunderchaos.LIGHTSABERS.get("blue_negotiator_lightsaber").get());
                        output.accept(LOST_HILT.get());
                        output.accept(AEGIS_HILT.get());
                        output.accept(APPRENTICE_HILT.get());
                        output.accept(CHOSEN_HILT.get());
                        output.accept(EMPEROR_HILT.get());
                        output.accept(FALLEN_HILT.get());
                        output.accept(GRACE_HILT.get());
                        output.accept(GUARD_HILT.get());
                        output.accept(HARMONY_HILT.get());
                        output.accept(LEGACY_HILT.get());
                        output.accept(PADAWAN_HILT.get());
                        output.accept(RESOLVE_HILT.get());
                        output.accept(SKUSTELL_HILT.get());
                        output.accept(TALON_HILT.get());
                        output.accept(VALOR_HILT.get());
                        output.accept(WISDOM_HILT.get());
                        output.accept(WISDOM_HILT.get());
                        output.accept(KNIGHTFALL_HILT.get());
                        output.accept(NEGOTIATOR_HILT.get());
                        output.accept(BAROSHE_HILT.get());
                        output.accept(WINGMAW_HILT.get());
                        output.accept(WINGMAW_DAGGER.get());
                        output.accept(galaxyunderchaos.CUSTOM_LIGHTSABER.get());
                        output.accept(galaxyunderchaos.DOUBLE_LIGHTSABER.get());
                        galaxyunderchaos.LEGACY_HILTS.values().forEach(reg -> output.accept(reg.get()));
                        galaxyunderchaos.LIGHTSABER_PARTS.values().forEach(reg -> output.accept(reg.get()));

                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
