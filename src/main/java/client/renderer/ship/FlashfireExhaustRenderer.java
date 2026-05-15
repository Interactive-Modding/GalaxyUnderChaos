package client.renderer.ship;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import server.galaxyunderchaos.entity.FlashfireEntity;
import server.galaxyunderchaos.ship.FlashfireEngineLayout;

public final class FlashfireExhaustRenderer {
    private static final float GLOBAL_ENGINE_OFFSET_X = 0.0F;
    private static final float GLOBAL_ENGINE_OFFSET_Y = 0.0F;
    private static final float GLOBAL_ENGINE_OFFSET_Z = 0.0F;

    private static final float[][] ENGINE_OFFSETS = new float[][] {
            // engine 0 BIG RIGHT
            { 0.0F, 0.0F, -1.4F },

            // engine 1 BIG LEFT
            { 0.0F, 0.0F, -1.4F },

            // engine 2 SMALL RIGHT
            { 0.2F, -0.3F, -0.27F },

            // engine 3 SMALL LEFT
            { -0.2F, -0.3F, -0.27F }
    };

    /*
     * CHANGE ENGINE SIZES HERE.
     *
     * 1.0F = normal size
     * 2.0F = double size
     * 3.0F = triple size
     *
     * Each value matches the same engine index as ENGINE_OFFSETS.
     */
    private static final float[] ENGINE_SCALES = new float[] {
            3.0F, // engine 0
            3.0F, // engine 1
            1.0F, // engine 2
            1.0F  // engine 3
    };

    private FlashfireExhaustRenderer() {
    }

    public static void render(PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource buffer, FlashfireEntity ship, float partialTick) {
        float power = ship.getEnginePower();
        if (power <= 0.04F) {
            return;
        }

        Matrix4f matrix = new Matrix4f(poseStack.last().pose());
        float flicker = 0.9F + ((ship.tickCount + partialTick) % 6.0F) * 0.025F;

        int engineIndex = 0;
        for (FlashfireEngineLayout.EnginePoint engine : FlashfireEngineLayout.ENGINES) {
            float x = engine.renderX() + GLOBAL_ENGINE_OFFSET_X + getEngineOffset(engineIndex, 0);
            float y = engine.renderY() + GLOBAL_ENGINE_OFFSET_Y + getEngineOffset(engineIndex, 1);
            float z = engine.renderZ() + GLOBAL_ENGINE_OFFSET_Z + getEngineOffset(engineIndex, 2);
            float scale = getEngineScale(engineIndex);

            ShipEnginePrivateGlow.queueCone(matrix, x, y, z, 0.12F * scale, 1.05F * scale * power * flicker, 70, 145, 255, 92);
            ShipEnginePrivateGlow.queueCone(matrix, x, y, z, 0.055F * scale, 0.72F * scale * power * flicker, 180, 220, 255, 165);

            engineIndex++;
        }
    }

    private static float getEngineOffset(int engineIndex, int axis) {
        if (engineIndex < 0 || engineIndex >= ENGINE_OFFSETS.length) {
            return 0.0F;
        }
        float[] offset = ENGINE_OFFSETS[engineIndex];
        if (offset == null || axis < 0 || axis >= offset.length) {
            return 0.0F;
        }
        return offset[axis];
    }

    private static float getEngineScale(int engineIndex) {
        if (engineIndex < 0 || engineIndex >= ENGINE_SCALES.length) {
            return 1.0F;
        }
        return ENGINE_SCALES[engineIndex];
    }
}
