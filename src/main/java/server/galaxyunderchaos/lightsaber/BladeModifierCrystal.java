package server.galaxyunderchaos.lightsaber;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.item.BladeModifierCrystalItem;

import java.util.Locale;

public enum BladeModifierCrystal {
    COMPRESSED("compressed"),
    CRACKED("cracked"),
    INVERTING("inverting"),
    FINE_CUT("fine_cut"),
    PRISMATIC("prismatic");

    private final String serializedName;

    BladeModifierCrystal(String serializedName) {
        this.serializedName = serializedName;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public String getRegistryPath() {
        return "focusing_crystal_" + serializedName;
    }

    public String getTranslationKey() {
        return "item.galaxyunderchaos." + getRegistryPath();
    }

    @Nullable
    public static BladeModifierCrystal fromSerializedName(String name) {
        for (BladeModifierCrystal crystal : values()) {
            if (crystal.serializedName.equals(name)) {
                return crystal;
            }
        }
        return null;
    }

    @Nullable
    public static BladeModifierCrystal fromStack(ItemStack stack) {
        if (stack.getItem() instanceof BladeModifierCrystalItem item) {
            return item.getCrystal();
        }

        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (key == null) {
            return null;
        }

        String path = key.getPath().toLowerCase(Locale.ROOT);
        for (BladeModifierCrystal crystal : values()) {
            if (crystal.getRegistryPath().equals(path)) {
                return crystal;
            }
        }
        return null;
    }
}
