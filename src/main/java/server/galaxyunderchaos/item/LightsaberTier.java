package server.galaxyunderchaos.item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

public class LightsaberTier implements Tier {

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public float getSpeed() {
        return 15.0F; // Mining speed
    }

    @Override
    public float getAttackDamageBonus() {
        return 20.0F; // Additional attack damage
    }

    @Override
    public int getEnchantmentValue() {
        return 25; // Enchantment value
    }

    @Override
    public Ingredient getRepairIngredient() {
        // Use a futuristic material for repairs; obsidian as a placeholder
        return Ingredient.of(Blocks.NETHERITE_BLOCK);
    }

    @Override
    public int getLevel() {
        return 4;
    }
}
