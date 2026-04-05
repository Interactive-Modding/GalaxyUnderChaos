package client.renderer.lightsaber;

import client.renderer.ClientEventSubscriber;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.item.LightsaberItem;

public final class LightsaberColorResolver {
    private LightsaberColorResolver() {
    }

    public record BladeTint(int argb, float red, float green, float blue) {
    }

    public static BladeTint resolve(ItemStack stack) {
        return resolve(LightsaberItem.getBladeColor(stack));
    }

    public static BladeTint resolve(String bladeColor) {
        int argb = ClientEventSubscriber.getGlowColor(bladeColor);
        return new BladeTint(
                argb,
                FastColor.ARGB32.red(argb) / 255.0F,
                FastColor.ARGB32.green(argb) / 255.0F,
                FastColor.ARGB32.blue(argb) / 255.0F
        );
    }
}