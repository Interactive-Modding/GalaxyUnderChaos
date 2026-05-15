package client.renderer.ship;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.ship.ShipColorSection;
import server.galaxyunderchaos.ship.ShipCustomization;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Builds one composed ship texture from the factory texture plus the four tint masks.
 *
 * This fixes the old multi-pass renderer problem where the whole GeckoLib model was
 * rendered once for every color layer. Rendering identical geometry several times can
 * make color appear on the wrong faces, z-fight, or disappear depending on depth order.
 *
 * The mask PNGs remain the authority for what each RGB selector controls:
 *   *_base.png      -> Base
 *   *_primary.png   -> Primary
 *   *_secondary.png -> Secondary
 *   *_inside.png    -> Interior
 */
public final class ShipTextureComposer {
    private static final Map<Key, ResourceLocation> CACHE = new HashMap<>();

    private static final TextureSet NOVADIVE = new TextureSet(
            "novadive",
            NovadiveGeoModel.BASE_TEXTURE,
            NovadiveGeoModel.PRIMARY_TEXTURE,
            NovadiveGeoModel.SECONDARY_TEXTURE,
            NovadiveGeoModel.INTERIOR_TEXTURE,
            new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/novadive/novadive.png")
    );

    private static final TextureSet FLASHFIRE = new TextureSet(
            "flashfire",
            FlashfireGeoModel.BASE_TEXTURE,
            FlashfireGeoModel.PRIMARY_TEXTURE,
            FlashfireGeoModel.SECONDARY_TEXTURE,
            FlashfireGeoModel.INTERIOR_TEXTURE,
            new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/flashfire/flashfire.png")
    );

    private ShipTextureComposer() {
    }

    public static ResourceLocation getNovadiveTexture(int base, int primary, int secondary, int interior) {
        return getTexture(NOVADIVE, base, primary, secondary, interior);
    }

    public static ResourceLocation getFlashfireTexture(int base, int primary, int secondary, int interior) {
        return getTexture(FLASHFIRE, base, primary, secondary, interior);
    }

    private static ResourceLocation getTexture(TextureSet textures, int base, int primary, int secondary, int interior) {
        int safeBase = ShipCustomization.clampColor(base);
        int safePrimary = ShipCustomization.clampColor(primary);
        int safeSecondary = ShipCustomization.clampColor(secondary);
        int safeInterior = ShipCustomization.clampColor(interior);
        Key key = new Key(textures.id, safeBase, safePrimary, safeSecondary, safeInterior);

        ResourceLocation cached = CACHE.get(key);
        if (cached != null) {
            return cached;
        }

        ResourceLocation composed = composeTexture(textures, safeBase, safePrimary, safeSecondary, safeInterior);
        CACHE.put(key, composed);
        return composed;
    }

    private static ResourceLocation composeTexture(TextureSet textures, int base, int primary, int secondary, int interior) {
        try (NativeImage image = read(textures.factoryTexture)) {
            applyMask(image, read(textures.baseMask), base);
            applyMask(image, read(textures.primaryMask), primary);
            applyMask(image, read(textures.secondaryMask), secondary);
            applyMask(image, read(textures.interiorMask), interior);

            NativeImage upload = new NativeImage(image.getWidth(), image.getHeight(), true);
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    upload.setPixelRGBA(x, y, image.getPixelRGBA(x, y));
                }
            }

            String name = "guc_ship_" + textures.id + "_" + Integer.toHexString(base) + "_"
                    + Integer.toHexString(primary) + "_" + Integer.toHexString(secondary) + "_"
                    + Integer.toHexString(interior);
            return Minecraft.getInstance().getTextureManager().register(name, new DynamicTexture(upload));
        } catch (Exception exception) {
            return textures.factoryTexture;
        }
    }

    private static NativeImage read(ResourceLocation texture) throws Exception {
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(texture);
        if (resource.isEmpty()) {
            throw new IllegalStateException("Missing ship texture: " + texture);
        }

        try (InputStream stream = resource.get().open()) {
            return NativeImage.read(stream);
        }
    }

    private static void applyMask(NativeImage target, NativeImage mask, int color) {
        try (mask) {
            int width = Math.min(target.getWidth(), mask.getWidth());
            int height = Math.min(target.getHeight(), mask.getHeight());
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (isProtectedGlassPixel(x, y)) {
                        continue;
                    }

                    int maskPixel = mask.getPixelRGBA(x, y);
                    int alpha = alpha(maskPixel);
                    if (alpha <= 0) {
                        continue;
                    }

                    int shade = Math.max(red(maskPixel), Math.max(green(maskPixel), blue(maskPixel)));
                    int srcRed = (((color >> 16) & 255) * shade) / 255;
                    int srcGreen = (((color >> 8) & 255) * shade) / 255;
                    int srcBlue = ((color & 255) * shade) / 255;

                    int dstPixel = target.getPixelRGBA(x, y);
                    target.setPixelRGBA(x, y, blend(dstPixel, srcRed, srcGreen, srcBlue, alpha));
                }
            }
        }
    }

    /**
     * The cockpit/window UVs are intentionally protected so color masks never tint
     * the glass. This keeps the cockpit from becoming fully colored or transparent.
     */
    private static boolean isProtectedGlassPixel(int x, int y) {
        return in(x, y, 145, 109, 152, 110)
                || in(x, y, 23, 165, 39, 166)
                || in(x, y, 217, 115, 224, 116)
                || in(x, y, 183, 85, 199, 86)
                || in(x, y, 37, 182, 51, 198)
                || in(x, y, 237, 149, 244, 150)
                || in(x, y, 205, 39, 221, 40)
                || in(x, y, 240, 122, 247, 123)
                || in(x, y, 214, 126, 230, 127)
                || in(x, y, 146, 187, 174, 203)
                || in(x, y, 121, 155, 130, 156)
                || in(x, y, 152, 133, 170, 134)
                || in(x, y, 215, 95, 224, 96)
                || in(x, y, 177, 116, 195, 117)
                || in(x, y, 167, 168, 176, 186)
                || in(x, y, 0, 171, 9, 207);
    }

    private static boolean in(int x, int y, int minX, int minY, int maxX, int maxY) {
        return x >= minX && x < maxX && y >= minY && y < maxY;
    }

    private static int blend(int dstPixel, int srcRed, int srcGreen, int srcBlue, int srcAlpha) {
        int dstAlpha = alpha(dstPixel);
        int dstRed = red(dstPixel);
        int dstGreen = green(dstPixel);
        int dstBlue = blue(dstPixel);

        int inverseAlpha = 255 - srcAlpha;
        int outAlpha = srcAlpha + ((dstAlpha * inverseAlpha + 127) / 255);
        if (outAlpha <= 0) {
            return 0;
        }

        int outRed = ((srcRed * srcAlpha) + (dstRed * dstAlpha * inverseAlpha / 255)) / outAlpha;
        int outGreen = ((srcGreen * srcAlpha) + (dstGreen * dstAlpha * inverseAlpha / 255)) / outAlpha;
        int outBlue = ((srcBlue * srcAlpha) + (dstBlue * dstAlpha * inverseAlpha / 255)) / outAlpha;
        return rgba(outRed, outGreen, outBlue, outAlpha);
    }

    private static int alpha(int pixel) {
        return (pixel >>> 24) & 255;
    }

    private static int blue(int pixel) {
        return (pixel >>> 16) & 255;
    }

    private static int green(int pixel) {
        return (pixel >>> 8) & 255;
    }

    private static int red(int pixel) {
        return pixel & 255;
    }

    private static int rgba(int red, int green, int blue, int alpha) {
        return ((alpha & 255) << 24) | ((blue & 255) << 16) | ((green & 255) << 8) | (red & 255);
    }

    private record TextureSet(String id,
                              ResourceLocation baseMask,
                              ResourceLocation primaryMask,
                              ResourceLocation secondaryMask,
                              ResourceLocation interiorMask,
                              ResourceLocation factoryTexture) {
    }

    private record Key(String shipId, int base, int primary, int secondary, int interior) {
    }
}
