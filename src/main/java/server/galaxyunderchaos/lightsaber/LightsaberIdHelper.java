package server.galaxyunderchaos.lightsaber;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;

public final class LightsaberIdHelper {
    private static final String[] BLADE_COLORS = {
            "red", "blue", "green", "yellow", "cyan",
            "white", "magenta", "purple", "pink",
            "lime_green", "turquoise", "orange", "blood_orange" , "amber", "gold", "light_blue", "dark_blue", "maroon", "deep_violet", "arctic_blue", "rose_pink"
    };

    private LightsaberIdHelper() {
    }

    @Nullable
    public static String getItemPath(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        return id == null ? null : id.getPath();
    }

    public static String resolveHiltId(Item item) {
        String path = getItemPath(item);
        if (path == null || path.isBlank()) {
            return "unknown";
        }

        if (path.endsWith("_hilt")) {
            return path.substring(0, path.length() - "_hilt".length());
        }

        if (path.endsWith("_lightsaber")) {
            String trimmed = path.substring(0, path.length() - "_lightsaber".length());
            String color = resolveBladeColorFromRegistryPath(trimmed);
            if (color != null) {
                return trimmed.substring(color.length() + 1);
            }
        }

        return path;
    }

    @Nullable
    public static String resolveBladeColor(Item item) {
        String path = getItemPath(item);
        return path == null ? null : resolveBladeColorFromRegistryPath(path);
    }

    @Nullable
    public static String resolveBladeColorFromRegistryPath(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }

        if (path.endsWith("_kyber")) {
            return path.substring(0, path.length() - "_kyber".length());
        }

        String normalizedPath = path;
        if (normalizedPath.endsWith("_lightsaber")) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - "_lightsaber".length());
        }

        final String finalPath = normalizedPath;
        return Arrays.stream(BLADE_COLORS)
                .sorted(Comparator.comparingInt(String::length).reversed())
                .filter(color -> finalPath.equals(color) || finalPath.startsWith(color + "_"))
                .findFirst()
                .orElse(null);
    }

    public static String resolveLightsaberRegistryId(String bladeColor, String hiltId) {
        return bladeColor + "_" + hiltId + "_lightsaber";
    }
}
