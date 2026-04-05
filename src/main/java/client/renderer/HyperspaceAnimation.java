package client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import client.renderer.HyperspaceOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HyperspaceAnimation {
    private static boolean isWarping = false;
    private static int warpTicks = 0;
    private static final int MAX_WARP_TICKS = 60;
    private static Vec3 startPos;
    private static Vec3 endPos;

    public static void startWarp(Vec3 destination) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        isWarping = true;
        warpTicks = 0;
        startPos = player.position();
        endPos = destination;

        HyperspaceOverlay.startWarpEffect(); // Start visual effect
        MinecraftForge.EVENT_BUS.register(new HyperspaceAnimation());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!isWarping) return;

        warpTicks++;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        float progress = (float) warpTicks / MAX_WARP_TICKS;
        float easeOut = (float) (1 - Math.pow(1 - progress, 3));

        Vec3 newPos = startPos.lerp(endPos, easeOut);
        player.setPos(newPos.x, newPos.y, newPos.z);

        if (warpTicks >= MAX_WARP_TICKS) {
            isWarping = false;
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
