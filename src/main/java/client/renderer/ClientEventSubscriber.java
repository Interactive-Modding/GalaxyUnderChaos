package client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.item.LightsaberItem;

@Mod.EventBusSubscriber(modid = "galaxyunderchaos", value = Dist.CLIENT)
public class ClientEventSubscriber {
    private static BlockPos lastPos;

    /**
     * Legacy compatibility only.
     * Do NOT use these as the saber blade render source anymore.
     * Saber renderers should resolve color directly from the active stack/bladeColor.
     */
    public static float glowR = 1f, glowG = 1f, glowB = 1f;

    public static int getGlowColor(String bladeColor) {
        return switch (bladeColor) {
            case "red"          -> 0xFFE20830;
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
            case "arctic_blue"  -> 0xFFB0E1E8;
            case "rose_pink"    -> 0xFFFABBD7;
            default             -> 0xFFFFFFFF;
        };
    }

    @SubscribeEvent
    public static void onRenderTick(RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) return;

        ItemStack mainStack = player.getMainHandItem();
        ItemStack offStack = player.getOffhandItem();

        boolean mainActive = mainStack.getItem() instanceof LightsaberItem lsMain && lsMain.isActive(mainStack);
        boolean offActive = offStack.getItem() instanceof LightsaberItem lsOff && lsOff.isActive(offStack);

        ItemStack activeStack;
        if (mainActive) {
            activeStack = mainStack;
        } else if (offActive) {
            activeStack = offStack;
        } else {
            if (lastPos != null) {
                minecraft.level.removeBlock(lastPos, false);
                lastPos = null;
            }
            glowR = glowG = glowB = 1f;
            return;
        }

        BlockPos lightPos = player.blockPosition().above(1);

        BlockState lightState = Blocks.LIGHT
                .defaultBlockState()
                .setValue(LightBlock.LEVEL, LightsaberItem.getLightLevel(activeStack));
        minecraft.level.setBlock(lightPos, lightState, 2);

        if (lastPos != null && !lastPos.equals(lightPos)) {
            minecraft.level.removeBlock(lastPos, false);
        }
        lastPos = lightPos;

        // Legacy compatibility only. Blade renderers should not read these anymore.
        int argb = getGlowColor(LightsaberItem.getBladeColor(activeStack));
        glowR = FastColor.ARGB32.red(argb) / 255f;
        glowG = FastColor.ARGB32.green(argb) / 255f;
        glowB = FastColor.ARGB32.blue(argb) / 255f;
    }
}