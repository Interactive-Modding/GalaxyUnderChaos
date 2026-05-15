
package client.screen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;

import java.util.ArrayList;
import java.util.List;

public final class ScreenGhostStacks {
    private static final String[] KYBER_IDS = {
            "red_kyber", "blue_kyber", "green_kyber", "yellow_kyber", "cyan_kyber",
            "white_kyber", "magenta_kyber", "purple_kyber", "pink_kyber", "lime_green_kyber",
            "turquoise_kyber", "orange_kyber", "blood_orange_kyber", "amber_kyber", "gold_kyber",
            "light_blue_kyber", "dark_blue_kyber", "maroon_kyber", "deep_violet_kyber", "arctic_blue_kyber", "rose_pink_kyber"
    };

    private ScreenGhostStacks() {
    }

    public static ItemStack circuitryStack() {
        return new ItemStack(galaxyunderchaos.INTERNAL_LIGHTSABER_CIRCUITRY.get());
    }

    public static List<ItemStack> kyberStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (String id : KYBER_IDS) {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(galaxyunderchaos.MODID, id));
            if (item != null && item != net.minecraft.world.item.Items.AIR) {
                stacks.add(new ItemStack(item));
            }
        }
        return stacks;
    }

    public static List<ItemStack> modifierStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (BladeModifierCrystal crystal : BladeModifierCrystal.values()) {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(galaxyunderchaos.MODID, crystal.getRegistryPath()));
            if (item != null && item != net.minecraft.world.item.Items.AIR) {
                stacks.add(new ItemStack(item));
            }
        }
        return stacks;
    }
}
