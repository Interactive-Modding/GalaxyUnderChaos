package server.galaxyunderchaos.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import server.galaxyunderchaos.datagen.ModTags;
import server.galaxyunderchaos.galaxyunderchaos;

public class ModToolTiers {
    public static final Tier LIGHTSABER = new ForgeTier(4, 2500, 10f, 20,
            20, ModTags.Blocks.NEEDS_LIGHTSABER_TOOL,
            () -> Ingredient.of(galaxyunderchaos.AEGIS_HILT.get()));
    public static final Tier DAGGER = new ForgeTier(2, 340, 3f, 20,
            20, ModTags.Blocks.NEEDS_DAGGER_TOOL,
            () -> Ingredient.of(galaxyunderchaos.WINGMAW_HILT.get()));

}
