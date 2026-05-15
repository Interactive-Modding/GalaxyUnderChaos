package client.renderer;

import net.minecraft.world.phys.Vec3;

/**
 * Client-only compatibility wrapper for older calls that asked for a hyperspace animation directly.
 * It deliberately no longer moves the local player. Teleports must be server-authoritative; this
 * class only starts the visual sequence.
 */
public class HyperspaceAnimation {
    public static void startWarp(Vec3 destination) {
        HyperspaceOverlay.startWarpEffect();
    }
}
