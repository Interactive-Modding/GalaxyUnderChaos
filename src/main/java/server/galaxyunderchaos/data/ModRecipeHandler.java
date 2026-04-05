package server.galaxyunderchaos.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipeHandler extends RecipeProvider implements IConditionBuilder {
    public ModRecipeHandler(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pRecipeOutput) {
        List<ItemLike> TITANIUM_SMELTABLES = List.of(galaxyunderchaos.TITANIUM_INGOT.get(),
                galaxyunderchaos.TITANIUM_ORE.get(), galaxyunderchaos.TITANIUM_DEEPSLATE_ORE.get());
        List<ItemLike> CHROMIUM_SMELTABLES = List.of(galaxyunderchaos.CHROMIUM_INGOT.get(),
                galaxyunderchaos.CHROMIUM_ORE.get(), galaxyunderchaos.CHROMIUM_DEEPSLATE_ORE.get());
        List<ItemLike> CHITIN_SMELTABLES = List.of(galaxyunderchaos.ACID_FORGED_PLATE.get(),
                galaxyunderchaos.CHITIN_FRAGMENTS.get());
        List<ItemLike> WINGMAW_SMELTABLES = List.of(galaxyunderchaos.COOKED_WINGMAW_MEAT.get(),
                galaxyunderchaos.RAW_WINGMAW_MEAT.get());

        oreSmelting(pRecipeOutput, CHROMIUM_SMELTABLES, RecipeCategory.MISC, galaxyunderchaos.CHROMIUM_INGOT.get(), 0.25f, 200, "chromium");
        oreSmelting(pRecipeOutput, TITANIUM_SMELTABLES, RecipeCategory.MISC, galaxyunderchaos.TITANIUM_INGOT.get(), 0.25f, 200, "titanium");
        oreBlasting(pRecipeOutput, TITANIUM_SMELTABLES, RecipeCategory.MISC, galaxyunderchaos.TITANIUM_INGOT.get(), 0.25f, 100, "titanium");
        oreBlasting(pRecipeOutput, CHROMIUM_SMELTABLES, RecipeCategory.MISC, galaxyunderchaos.CHROMIUM_INGOT.get(), 0.25f, 100, "chromium");
        oreBlasting(pRecipeOutput, CHITIN_SMELTABLES, RecipeCategory.MISC, galaxyunderchaos.CHITIN_FRAGMENTS.get(), 0.25f, 100, "chitin_fragments");
        oreSmelting(pRecipeOutput, CHITIN_SMELTABLES, RecipeCategory.MISC, galaxyunderchaos.CHITIN_FRAGMENTS.get(), 0.25f, 200, "chitin_fragments");
        oreBlasting(pRecipeOutput, WINGMAW_SMELTABLES, RecipeCategory.MISC, galaxyunderchaos.RAW_WINGMAW_MEAT.get(), 0.25f, 100, "raw_wingmaw_meat");
        oreSmelting(pRecipeOutput, WINGMAW_SMELTABLES, RecipeCategory.MISC, galaxyunderchaos.RAW_WINGMAW_MEAT.get(), 0.25f, 200, "raw_wingmaw_meat");
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_by_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_by_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> recipeOutput, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, galaxyunderchaos.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
