package client.renderer;

import net.minecraft.util.FastColor;

/**
 * Client-side color helpers shared by the lightsaber renderers.
 *
 * IMPORTANT:
 * This class intentionally no longer places/removes vanilla LightBlock blocks.
 * The old render-tick light-block workaround could replace the block above the
 * player and then remove it later, which is what made active sabers delete
 * blocks. World illumination should now be handled by an installed dynamic
 * lighting mod such as AtomicStryker's Dynamic Lights instead of by writing
 * temporary blocks into the client world.
 */
public class ClientEventSubscriber {
    /**
     * Legacy compatibility only.
     * Do NOT use these as the saber blade render source anymore.
     * Saber renderers should resolve color directly from the active stack/bladeColor.
     */
    public static float glowR = 1f, glowG = 1f, glowB = 1f;

    public static int getGlowColor(String bladeColor) {
        return switch (bladeColor) {
            case "red"          -> 0xFFE20830;
            case "deep_blue"    -> 0xFF0000FF;
            case "medium_blue"  -> 0xFF006BFF;
            case "light_blue"   -> 0xFF2985D0;
            case "green"        -> 0xFF00FF00;
            case "yellow"       -> 0xFFFFE600;
            case "cyan"         -> 0xFF00FFFF;
            case "white"        -> 0xFFFFFFFF;
            case "magenta"      -> 0xFFD029D0;
            case "purple"       -> 0xFFAC2FC0;
            case "pink"         -> 0xFF7A1E3A;
            case "lime_green"   -> 0xFFADFF2F;
            case "turquoise"    -> 0xFF37B8AE;
            case "orange"       -> 0xFFE58416;
            case "blood_orange" -> 0xFFFF4008;
            case "amber"        -> 0xFFFFBF00;
            case "gold"         -> 0xFFFFC247;
            case "blue"         -> 0xFF293EFF;
            case "dark_blue"    -> 0xFF06244F;
            case "maroon"       -> 0xFF800000;
            case "deep_violet"  -> 0xFF460178;
            case "indigo"       -> 0xFF5D00FF;
            case "mint_green"   -> 0xFF00FF9B;
            case "arctic_blue"  -> 0xFFB0E1E8;
            case "rose_pink"    -> 0xFFFABBD7;
            default             -> 0xFFFFFFFF;
        };
    }

    public static void setLegacyGlowColor(String bladeColor) {
        int argb = getGlowColor(bladeColor);
        glowR = FastColor.ARGB32.red(argb) / 255f;
        glowG = FastColor.ARGB32.green(argb) / 255f;
        glowB = FastColor.ARGB32.blue(argb) / 255f;
    }

    public static void resetLegacyGlowColor() {
        glowR = glowG = glowB = 1f;
    }
}
