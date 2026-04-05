package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import server.galaxyunderchaos.lightsaber.LightsaberForm;

import java.util.Map;

final class LightsaberFormAnimations {
    private static final Profile DEFAULT = new Profile(
            -5f, 10f, 0f,
            5f, 0.25f,
            -25f, 30f, 10f,
            -0.1f, 0.4f, -0.2f
    );

    private static final Map<LightsaberForm, Profile> PROFILES = Map.of(
            LightsaberForm.SHII_CHO, new Profile(
                    -5f, 15f, 0f,
                    6f, 0.25f,
                    -35f, 35f, 20f,
                    -0.10f, 0.40f, -0.20f
            ),
            LightsaberForm.MAKASHI, new Profile(
                    0f, -70f, 5f,
                    3f, 0.35f,
                    -10f, -55f, -10f,
                    -0.05f, 0.42f, -0.25f
            ),
            LightsaberForm.SORESU, new Profile(
                    30f, 8f, 0f,
                    4f, 0.20f,
                    -12f, 20f, -5f,
                    -0.10f, 0.45f, -0.18f
            ),
            LightsaberForm.ATARU, new Profile(
                    55f, 0f, 10f,
                    10f, 0.45f,
                    -70f, 35f, 25f,
                    -0.12f, 0.38f, -0.22f
            ),
            LightsaberForm.SHIEN, new Profile(
                    15f, 25f, 160f,
                    5f, 0.30f,
                    20f, 50f, -15f,
                    -0.10f, 0.40f, -0.18f
            ),
            LightsaberForm.NIMAN, new Profile(
                    -8f, -20f, 0f,
                    5f, 0.22f,
                    -20f, 25f, 10f,
                    -0.10f, 0.40f, -0.20f
            ),
            LightsaberForm.JUYO, new Profile(
                    -75f, 12f, 5f,
                    12f, 0.40f,
                    -60f, 30f, 30f,
                    -0.14f, 0.36f, -0.22f
            )
    );

    private LightsaberFormAnimations() {}

    static void apply(PoseStack poseStack, AbstractClientPlayer player, float partialTicks, LightsaberForm form) {
        Profile profile = PROFILES.getOrDefault(form, DEFAULT);

        float time = player.tickCount + partialTicks;
        float idle = Mth.sin(time * profile.idleSpeed) * profile.idleAmplitude;

        poseStack.translate(profile.offsetX, profile.offsetY, profile.offsetZ);

        poseStack.mulPose(Axis.XP.rotationDegrees(profile.baseX + idle));
        poseStack.mulPose(Axis.YP.rotationDegrees(profile.baseY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(profile.baseZ));

        float swing = player.getAttackAnim(partialTicks);
        if (swing > 0f) {
            float swingEase = Mth.sin(Mth.sqrt(swing) * Mth.PI);
            poseStack.mulPose(Axis.XP.rotationDegrees(profile.attackX * swingEase));
            poseStack.mulPose(Axis.YP.rotationDegrees(profile.attackY * swingEase));
            poseStack.mulPose(Axis.ZP.rotationDegrees(profile.attackZ * swingEase));
            poseStack.translate(0.0f, -0.02f * swingEase, -0.05f * swingEase);
        }
    }

    private record Profile(
            float baseX, float baseY, float baseZ,
            float idleAmplitude, float idleSpeed,
            float attackX, float attackY, float attackZ,
            float offsetX, float offsetY, float offsetZ
    ) {
    }
}
