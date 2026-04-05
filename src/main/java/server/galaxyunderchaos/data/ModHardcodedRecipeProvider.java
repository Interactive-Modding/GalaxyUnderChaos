package server.galaxyunderchaos.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.FinishedRecipe;
import java.util.function.Consumer;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.concurrent.CompletableFuture;

public class ModHardcodedRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final TagKey<Item> AK_LOGS = ItemTags.create(new ResourceLocation(galaxyunderchaos.MODID, "ak_logs"));
    private static final TagKey<Item> HEART_BERRY_LOGS = ItemTags.create(new ResourceLocation(galaxyunderchaos.MODID, "heart_berry_logs"));

    public ModHardcodedRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput);
    }


    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeOutput) {
        addSmeltingAndBlasting(recipeOutput, galaxyunderchaos.CHITIN_FRAGMENTS.get(), galaxyunderchaos.ACID_FORGED_PLATE.get(), "chitin_fragments", 0.25f, 200, 100);

        addWoodSet(recipeOutput, "ak", galaxyunderchaos.AK_PLANKS.get(), AK_LOGS, galaxyunderchaos.AK_BOAT.get(), galaxyunderchaos.AK_CHEST_BOAT.get(), galaxyunderchaos.AK_BUTTON.get(), galaxyunderchaos.AK_DOOR_BLOCK.get(), galaxyunderchaos.AK_FENCE_BLOCK.get(), galaxyunderchaos.AK_FENCE_GATE.get(), galaxyunderchaos.AK_HANGING_SIGN.get(), galaxyunderchaos.AK_PRESSURE_PLATE.get(), galaxyunderchaos.AK_SIGN.get(), galaxyunderchaos.AK_SLAB.get(), galaxyunderchaos.AK_STAIRS.get(), galaxyunderchaos.AK_TRAPDOOR_BLOCK.get(), galaxyunderchaos.STRIPPED_AK_LOG.get());
        addWoodSet(recipeOutput, "heart_berry", galaxyunderchaos.HEART_BERRY_PLANKS.get(), HEART_BERRY_LOGS, galaxyunderchaos.HEART_BERRY_BOAT.get(), galaxyunderchaos.HEART_BERRY_CHEST_BOAT.get(), galaxyunderchaos.HEART_BERRY_BUTTON.get(), galaxyunderchaos.HEART_BERRY_DOOR_BLOCK.get(), galaxyunderchaos.HEART_BERRY_FENCE_BLOCK.get(), galaxyunderchaos.HEART_BERRY_FENCE_GATE.get(), galaxyunderchaos.HEART_BERRY_HANGING_SIGN.get(), galaxyunderchaos.HEART_BERRY_PRESSURE_PLATE.get(), galaxyunderchaos.HEART_BERRY_SIGN.get(), galaxyunderchaos.HEART_BERRY_SLAB.get(), galaxyunderchaos.HEART_BERRY_STAIRS.get(), galaxyunderchaos.HEART_BERRY_DOOR_BLOCK.get(), galaxyunderchaos.STRIPPED_HEART_BERRY_LOG.get());

        addStoneBuildingRecipes(recipeOutput, "ancient_temple_stone", galaxyunderchaos.ANCIENT_TEMPLE_STONE.get(), galaxyunderchaos.ANCIENT_TEMPLE_STONE_PILLAR.get(), galaxyunderchaos.ANCIENT_TEMPLE_STONE_STAIRS.get(), galaxyunderchaos.ANCIENT_TEMPLE_STONE_SLAB.get(), galaxyunderchaos.ANCIENT_TEMPLE_STONE_WALL.get());
        addStoneBuildingRecipes(recipeOutput, "ashla_temple_stone", galaxyunderchaos.ASHLA_TEMPLE_STONE.get(), galaxyunderchaos.ASHLA_TEMPLE_STONE_PILLAR.get(), galaxyunderchaos.ASHLA_TEMPLE_STONE_STAIRS.get(), galaxyunderchaos.ASHLA_TEMPLE_STONE_SLAB.get(), galaxyunderchaos.ASHLA_TEMPLE_STONE_WALL.get());
        addStoneBuildingRecipes(recipeOutput, "bogan_temple_stone", galaxyunderchaos.BOGAN_TEMPLE_STONE.get(), galaxyunderchaos.BOGAN_TEMPLE_STONE_PILLAR.get(), galaxyunderchaos.BOGAN_TEMPLE_STONE_STAIRS.get(), galaxyunderchaos.BOGAN_TEMPLE_STONE_SLAB.get(), galaxyunderchaos.BOGAN_TEMPLE_STONE_WALL.get());
        addStoneBuildingRecipes(recipeOutput, "dark_temple_stone", galaxyunderchaos.DARK_TEMPLE_STONE.get(), galaxyunderchaos.DARK_TEMPLE_STONE_PILLAR.get(), galaxyunderchaos.DARK_TEMPLE_STONE_STAIRS.get(), galaxyunderchaos.DARK_TEMPLE_STONE_SLAB.get(), galaxyunderchaos.DARK_TEMPLE_STONE_WALL.get());
        addStoneBuildingRecipes(recipeOutput, "temple_stone", galaxyunderchaos.TEMPLE_STONE.get(), galaxyunderchaos.TEMPLE_STONE_PILLAR.get(), galaxyunderchaos.TEMPLE_STONE_STAIRS.get(), galaxyunderchaos.TEMPLE_STONE_SLAB.get(), galaxyunderchaos.TEMPLE_STONE_WALL.get());
        addStoneBuildingRecipes(recipeOutput, "tython_temple_stone", galaxyunderchaos.TYTHON_TEMPLE_STONE.get(), galaxyunderchaos.TYTHON_TEMPLE_STONE_PILLAR.get(), galaxyunderchaos.TYTHON_TEMPLE_STONE_STAIRS.get(), galaxyunderchaos.TYTHON_TEMPLE_STONE_SLAB.get(), galaxyunderchaos.TYTHON_TEMPLE_STONE_WALL.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.ANCIENT_TEMPLE_STONE_HOLOBOOK.get())
                .pattern("###")
                .pattern("GGG")
                .pattern("###")
                .define('#', galaxyunderchaos.ANCIENT_TEMPLE_STONE.get())
                .define('G', galaxyunderchaos.ANCIENT_HOLOBOOK.get())
                .unlockedBy(getHasName(galaxyunderchaos.ANCIENT_HOLOBOOK.get()), has(galaxyunderchaos.ANCIENT_HOLOBOOK.get()))
                .save(recipeOutput, modLoc("ancient_temple_stone_holobook"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.TEMPLE_STONE_HOLOBOOK.get())
                .pattern("###")
                .pattern("GGG")
                .pattern("###")
                .define('#', galaxyunderchaos.TEMPLE_STONE.get())
                .define('G', galaxyunderchaos.JEDI_HOLOBOOK.get())
                .unlockedBy(getHasName(galaxyunderchaos.JEDI_HOLOBOOK.get()), has(galaxyunderchaos.JEDI_HOLOBOOK.get()))
                .save(recipeOutput, modLoc("temple_stone_holobook"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(galaxyunderchaos.TYTHON_TEMPLE_STONE.get()), RecipeCategory.BUILDING_BLOCKS, galaxyunderchaos.CHISELED_TYTHON_TEMPLE_STONE.get())
                .group("misc")
                .unlockedBy(getHasName(galaxyunderchaos.TYTHON_TEMPLE_STONE.get()), has(galaxyunderchaos.TYTHON_TEMPLE_STONE.get()))
                .save(recipeOutput, modLoc("chiseled_tython_temple_stone_stonecutter"));

        addPortal(recipeOutput, "ashla_portal", galaxyunderchaos.ASHLA_PORTAL_ITEM.get(), Items.CALCITE);
        addPortal(recipeOutput, "bogan_portal", galaxyunderchaos.BOGAN_PORTAL_ITEM.get(), Items.BLACKSTONE);
        addPortal(recipeOutput, "dantooine_portal", galaxyunderchaos.DANTOOINE_PORTAL_ITEM.get(), Items.STRIPPED_BIRCH_LOG);
        addPortal(recipeOutput, "ilum_portal", galaxyunderchaos.ILUM_PORTAL_ITEM.get(), Items.SNOWBALL);
        addPortal(recipeOutput, "korriban_portal", galaxyunderchaos.KORRIBAN_PORTAL_ITEM.get(), Items.RED_SAND);
        addPortal(recipeOutput, "malachor_portal", galaxyunderchaos.MALACHOR_PORTAL_ITEM.get(), Items.DEAD_BUSH);
        addPortal(recipeOutput, "mustafar_portal", galaxyunderchaos.MUSTAFAR_PORTAL_ITEM.get(), Items.BASALT);
        addPortal(recipeOutput, "naboo_portal", galaxyunderchaos.NABOO_PORTAL_ITEM.get(), galaxyunderchaos.SHUURA.get());
        addPortal(recipeOutput, "ossus_portal", galaxyunderchaos.OSSUS_PORTAL_ITEM.get(), Items.BAMBOO);
        addPortal(recipeOutput, "tython_portal", galaxyunderchaos.TYTHON_PORTAL_ITEM.get(), Items.MOSSY_COBBLESTONE);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.BLEEDING_TABLE.get())
                .pattern("CCC")
                .pattern("#X#")
                .pattern("#X#")
                .define('#', Items.GRAY_CONCRETE)
                .define('X', galaxyunderchaos.SITH_HOLOBOOK.get())
                .define('C', Items.BLACK_CONCRETE)
                .group("lightsaber")
                .unlockedBy(getHasName(galaxyunderchaos.SITH_HOLOBOOK.get()), has(galaxyunderchaos.SITH_HOLOBOOK.get()))
                .save(recipeOutput, modLoc("bleeding_table"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.LIGHTSABER_CRAFTING_TABLE.get())
                .pattern("CCC")
                .pattern("#X#")
                .pattern("#X#")
                .define('#', Items.GRAY_CONCRETE)
                .define('X', galaxyunderchaos.ANCIENT_HOLOBOOK.get())
                .define('C', Items.LIGHT_GRAY_CONCRETE)
                .group("lightsaber")
                .unlockedBy(getHasName(galaxyunderchaos.ANCIENT_HOLOBOOK.get()), has(galaxyunderchaos.ANCIENT_HOLOBOOK.get()))
                .save(recipeOutput, modLoc("lightsaber_crafting_table"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.NAVIGATION_COMPUTER.get())
                .pattern("CCC")
                .pattern("#X#")
                .pattern("#R#")
                .define('#', Items.REDSTONE)
                .define('R', Items.GLASS)
                .define('C', galaxyunderchaos.TITANIUM_INGOT.get())
                .define('X', Items.COMPASS)
                .group("hyperdrive")
                .unlockedBy(getHasName(Items.COMPASS), has(Items.COMPASS))
                .save(recipeOutput, modLoc("navigation_computer"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.PORTAL_ITEM.get())
                .pattern("TTT")
                .pattern("#X#")
                .pattern("#R#")
                .define('#', galaxyunderchaos.TITANIUM_CHROMIUM_INGOT.get())
                .define('R', Items.NETHERITE_INGOT)
                .define('T', Items.DIAMOND)
                .define('X', galaxyunderchaos.NAVIGATION_COMPUTER.get())
                .unlockedBy(getHasName(galaxyunderchaos.NAVIGATION_COMPUTER.get()), has(galaxyunderchaos.NAVIGATION_COMPUTER.get()))
                .save(recipeOutput, modLoc("portal_item"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.TEMPLE_GUARD_HELMET.get())
                .pattern("SSS")
                .pattern("P P")
                .define('S', galaxyunderchaos.TEMPLE_GUARD_FABRIC.get())
                .define('P', Items.GOLD_INGOT)
                .unlockedBy(getHasName(galaxyunderchaos.TEMPLE_GUARD_FABRIC.get()), has(galaxyunderchaos.TEMPLE_GUARD_FABRIC.get()))
                .save(recipeOutput, modLoc("temple_guard_helmet"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.TEMPLE_GUARD_CHESTPLATE.get())
                .pattern("P P")
                .pattern("SPS")
                .pattern("SSS")
                .define('S', galaxyunderchaos.TEMPLE_GUARD_FABRIC.get())
                .define('P', Items.GOLD_INGOT)
                .unlockedBy(getHasName(galaxyunderchaos.TEMPLE_GUARD_FABRIC.get()), has(galaxyunderchaos.TEMPLE_GUARD_FABRIC.get()))
                .save(recipeOutput, modLoc("temple_guard_chestplate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.TEMPLE_GUARD_LEGGINGS.get())
                .pattern("SSS")
                .pattern("S S")
                .pattern("P P")
                .define('S', galaxyunderchaos.TEMPLE_GUARD_FABRIC.get())
                .define('P', Items.GOLD_INGOT)
                .unlockedBy(getHasName(galaxyunderchaos.TEMPLE_GUARD_FABRIC.get()), has(galaxyunderchaos.TEMPLE_GUARD_FABRIC.get()))
                .save(recipeOutput, modLoc("temple_guard_leggings"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.TEMPLE_GUARD_BOOTS.get())
                .pattern("S S")
                .pattern("P P")
                .define('S', galaxyunderchaos.TEMPLE_GUARD_FABRIC.get())
                .define('P', Items.GOLD_INGOT)
                .unlockedBy(getHasName(galaxyunderchaos.TEMPLE_GUARD_FABRIC.get()), has(galaxyunderchaos.TEMPLE_GUARD_FABRIC.get()))
                .save(recipeOutput, modLoc("temple_guard_boots"));

        addSmeltingAndBlasting(recipeOutput, galaxyunderchaos.CHROMIUM_DEEPSLATE_ORE.get(), galaxyunderchaos.CHROMIUM_INGOT.get(), "chromium", 0.25f, 200, 100);
        addSmeltingAndBlasting(recipeOutput, galaxyunderchaos.CHROMIUM_ORE.get(), galaxyunderchaos.CHROMIUM_INGOT.get(), "chromium", 0.25f, 200, 100);
        addSmeltingAndBlasting(recipeOutput, galaxyunderchaos.TITANIUM_DEEPSLATE_ORE.get(), galaxyunderchaos.TITANIUM_INGOT.get(), "titanium", 0.25f, 200, 100);
        addSmeltingAndBlasting(recipeOutput, galaxyunderchaos.TITANIUM_ORE.get(), galaxyunderchaos.TITANIUM_INGOT.get(), "titanium", 0.25f, 200, 100);
        addSmeltingAndBlasting(recipeOutput, galaxyunderchaos.RAW_WINGMAW_MEAT.get(), galaxyunderchaos.COOKED_WINGMAW_MEAT.get(), "raw_wingmaw_meat", 0.25f, 200, 100);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, galaxyunderchaos.TITANIUM_CHROMIUM_INGOT.get())
                .pattern("X")
                .pattern("#")
                .define('#', galaxyunderchaos.CHROMIUM_INGOT.get())
                .define('X', galaxyunderchaos.TITANIUM_INGOT.get())
                .group("hyperdrive")
                .unlockedBy(getHasName(galaxyunderchaos.CHROMIUM_INGOT.get()), has(galaxyunderchaos.CHROMIUM_INGOT.get()))
                .save(recipeOutput, modLoc("titanium_chromium_ingot"));

        addWeaponPart(recipeOutput, galaxyunderchaos.WINGMAW_BLADE.get(), galaxyunderchaos.WINGMAW_FANG.get(), "wingmaw_blade");
        addWeaponPart(recipeOutput, galaxyunderchaos.WINGMAW_HILT.get(), galaxyunderchaos.WINGMAW_FEATHER.get(), galaxyunderchaos.WINGMAW_HIDE.get(), Items.STICK, "wingmaw_hilt");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, galaxyunderchaos.WINGMAW_DAGGER.get())
                .pattern("   ")
                .pattern("a# ")
                .pattern("xa ")
                .define('#', galaxyunderchaos.WINGMAW_BLADE.get())
                .define('x', galaxyunderchaos.WINGMAW_HILT.get())
                .define('a', galaxyunderchaos.WINGMAW_FANG.get())
                .group("sticks")
                .unlockedBy(getHasName(galaxyunderchaos.WINGMAW_BLADE.get()), has(galaxyunderchaos.WINGMAW_BLADE.get()))
                .save(recipeOutput, modLoc("wingmaw_dagger"));
    }

    private void addSmeltingAndBlasting(Consumer<FinishedRecipe> recipeOutput, ItemLike input, ItemLike result, String group, float experience, int smeltTime, int blastTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, result, experience, smeltTime)
                .group(group)
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, modLoc(getItemName(result) + "_by_smelting_" + getItemName(input)));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC, result, experience, blastTime)
                .group(group)
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, modLoc(getItemName(result) + "_by_blasting_" + getItemName(input)));
    }

    private void addWoodSet(Consumer<FinishedRecipe> recipeOutput, String namePrefix, ItemLike planks, TagKey<Item> logsTag, ItemLike boat, ItemLike chestBoat, ItemLike button, ItemLike door, ItemLike fence, ItemLike fenceGate, ItemLike hangingSign, ItemLike pressurePlate, ItemLike sign, ItemLike slab, ItemLike stairs, ItemLike trapdoor, ItemLike strippedLog) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                .requires(logsTag)
                .group("planks")
                .unlockedBy(getHasName(planks), has(logsTag))
                .save(recipeOutput, modLoc(namePrefix + "_planks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, boat)
                .pattern("# #")
                .pattern("###")
                .define('#', planks)
                .group("boat")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_boat"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, chestBoat)
                .requires(Items.CHEST)
                .requires(boat)
                .group("chest_boat")
                .unlockedBy(getHasName(boat), has(boat))
                .save(recipeOutput, modLoc(namePrefix + "_chest_boat"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button)
                .requires(planks)
                .group("wooden_button")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_button"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', planks)
                .group("wooden_door")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_door"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, fence, 3)
                .pattern("W#W")
                .pattern("W#W")
                .define('#', Items.STICK)
                .define('W', planks)
                .group("wooden_fence")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_fence"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, fenceGate)
                .pattern("#W#")
                .pattern("#W#")
                .define('#', Items.STICK)
                .define('W', planks)
                .group("wooden_fence_gate")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_fence_gate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, hangingSign, 6)
                .pattern("X X")
                .pattern("###")
                .pattern("###")
                .define('#', strippedLog)
                .define('X', Items.CHAIN)
                .group("hanging_sign")
                .unlockedBy(getHasName(strippedLog), has(strippedLog))
                .save(recipeOutput, modLoc(namePrefix + "_hanging_sign"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pressurePlate)
                .pattern("##")
                .define('#', planks)
                .group("wooden_pressure_plate")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_pressure_plate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, sign, 3)
                .pattern("###")
                .pattern("###")
                .pattern(" X ")
                .define('#', planks)
                .define('X', Items.STICK)
                .group("wooden_sign")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_sign"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                .pattern("###")
                .define('#', planks)
                .group("wooden_slab")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', planks)
                .group("wooden_stairs")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, trapdoor, 2)
                .pattern("###")
                .pattern("###")
                .define('#', planks)
                .group("wooden_trapdoor")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeOutput, modLoc(namePrefix + "_trapdoor"));
    }

    private void addStoneBuildingRecipes(Consumer<FinishedRecipe> recipeOutput, String namePrefix, ItemLike base, ItemLike pillar, ItemLike stairs, ItemLike slab, ItemLike wall) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pillar, 2)
                .pattern("#")
                .pattern("#")
                .define('#', base)
                .group("misc")
                .unlockedBy(getHasName(base), has(base))
                .save(recipeOutput, modLoc(namePrefix + "_pillar"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                .pattern("###")
                .define('#', base)
                .group("misc")
                .unlockedBy(getHasName(base), has(base))
                .save(recipeOutput, modLoc(namePrefix + "_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                .pattern("  #")
                .pattern(" ##")
                .pattern("###")
                .define('#', base)
                .group("misc")
                .unlockedBy(getHasName(base), has(base))
                .save(recipeOutput, modLoc(namePrefix + "_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, wall, 6)
                .pattern("###")
                .pattern("###")
                .define('#', base)
                .unlockedBy(getHasName(base), has(base))
                .save(recipeOutput, modLoc(namePrefix + "_wall"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, pillar)
                .group("misc")
                .unlockedBy(getHasName(base), has(base))
                .save(recipeOutput, modLoc(namePrefix + "_pillar_stonecutter"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, slab, 2)
                .group("misc")
                .unlockedBy(getHasName(base), has(base))
                .save(recipeOutput, modLoc(namePrefix + "_slab_stonecutter"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, stairs)
                .group("misc")
                .unlockedBy(getHasName(base), has(base))
                .save(recipeOutput, modLoc(namePrefix + "_stairs_stonecutter"));
    }

    private void addPortal(Consumer<FinishedRecipe> recipeOutput, String name, ItemLike result, ItemLike frameItem) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, result)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .define('#', frameItem)
                .define('X', galaxyunderchaos.PORTAL_ITEM.get())
                .group("hyperdrive")
                .unlockedBy(getHasName(galaxyunderchaos.PORTAL_ITEM.get()), has(galaxyunderchaos.PORTAL_ITEM.get()))
                .save(recipeOutput, modLoc(name));
    }

    private void addWeaponPart(Consumer<FinishedRecipe> recipeOutput, ItemLike blade, ItemLike fang, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, blade)
                .pattern("  #")
                .pattern(" # ")
                .pattern("#  ")
                .define('#', fang)
                .group("sticks")
                .unlockedBy(getHasName(fang), has(fang))
                .save(recipeOutput, modLoc(name));
    }

    private void addWeaponPart(Consumer<FinishedRecipe> recipeOutput, ItemLike hilt, ItemLike feather, ItemLike hide, ItemLike stick, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, hilt)
                .pattern(" ax")
                .pattern("a#a")
                .pattern("xa ")
                .define('#', feather)
                .define('x', hide)
                .define('a', stick)
                .group("sticks")
                .unlockedBy(getHasName(feather), has(feather))
                .save(recipeOutput, modLoc(name));
    }

    private ResourceLocation modLoc(String path) {
        return new ResourceLocation(galaxyunderchaos.MODID, path);
    }
}
