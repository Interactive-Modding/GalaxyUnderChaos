package client.renderer.ship;

import com.mojang.blaze3d.vertex.PoseStack;
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

        float flicker = 0.9F + ((ship.tickCount + partialTick) % 6.0F) * 0.025F;

        int engineIndex = 0;
        for (FlashfireEngineLayout.EnginePoint engine : FlashfireEngineLayout.ENGINES) {
            float x = engine.renderX() + GLOBAL_ENGINE_OFFSET_X + getEngineOffset(engineIndex, 0);
            float y = engine.renderY() + GLOBAL_ENGINE_OFFSET_Y + getEngineOffset(engineIndex, 1);
            float z = engine.renderZ() + GLOBAL_ENGINE_OFFSET_Z + getEngineOffset(engineIndex, 2);
            float scale = getEngineScale(engineIndex);

            // Keep this in the normal entity buffer path. Do not restore the old late/private queue;
            // that is what caused Oculus to double / misplace the engine cones.
            // These values intentionally compensate for the loss of the old stacked private pass
            // so the exhaust keeps the same bright saber-like punch without rendering twice.
            float outerRadius = 0.14F * scale;
            float outerLength = 1.10F * scale * power * flicker;

            // Draw small -> large because the engine RenderType writes depth.
            // This keeps the bright core visible while the final outer cone leaves
            // a real depth value for water / translucent entities behind the jet.
            ShipEnginePrivateGlow.renderCone(poseStack, buffer, x, y, z, 0.030F * scale, 0.56F * scale * power * flicker, 235, 250, 255, 255);
            ShipEnginePrivateGlow.renderCone(poseStack, buffer, x, y, z, 0.070F * scale, 0.78F * scale * power * flicker, 165, 215, 255, 245);
            ShipEnginePrivateGlow.renderCone(poseStack, buffer, x, y, z, outerRadius, outerLength, 70, 145, 255, 175);

            engineIndex++;
        }

        ShipEnginePrivateGlow.flush(buffer);
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
