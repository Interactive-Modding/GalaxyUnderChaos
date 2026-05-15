package client.renderer.lightsaber;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "galaxyunderchaos", value = Dist.CLIENT)
public final class LegacyLightsaberPrivateGlow {
    private static final float BODY_HALF = 0.03125F;
    private static final float TIP_SIZE = 0.03125F;
    private static final float TIP_LENGTH = 0.125F;

    private static final int FORCE_WHIP_SEGMENTS = 36;
    private static final float FORCE_WHIP_CURVE_X = 0.340F;
    private static final float FORCE_WHIP_CURVE_Z = 0.220F;
    private static final float FORCE_WHIP_TRAIL = 0.330F;
    private static final float FORCE_WHIP_DROOP = 0.340F;
    private static final float FORCE_WHIP_SWING_LAG = 1.050F;
    private static final float FORCE_WHIP_BASE_ANCHOR = 0.175F;
    private static final float FORCE_WHIP_SWING_SNAP = 1.120F;

    private static final List<GlowBatch> BATCHES = new ArrayList<>();

    private LegacyLightsaberPrivateGlow() {
    }

    public static void queueOuterGlow(Matrix4f localPose,
                                      float bladeLength,
                                      float red,
                                      float green,
                                      float blue,
                                      float alphaBase,
                                      int smooth,
                                      float width,
                                      float xScale,
                                      float yScale,
                                      float zScale,
                                      boolean crossguard,
                                      boolean forceWhip,
                                      boolean fineCut,
                                      float whipTick,
                                      float whipAttackSwing) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null || minecraft.player == null) {
            return;
        }

        if (minecraft.screen != null) {
            return;
        }

        if (BATCHES.size() > 128) {
            BATCHES.clear();
        }

        BATCHES.add(new GlowBatch(
                new Matrix4f(RenderSystem.getProjectionMatrix()),
                new Matrix4f(RenderSystem.getModelViewStack().last().pose()),
                new Matrix4f(localPose),
                bladeLength,
                red,
                green,
                blue,
                alphaBase,
                smooth,
                width,
                xScale,
                yScale,
                zScale,
                crossguard,
                forceWhip,
                fineCut,
                whipTick,
                whipAttackSwing
        ));
    }

    @SubscribeEvent
    public static void renderPrivateGlow(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            return;
        }

        if (BATCHES.isEmpty()) {
            return;
        }

        Matrix4f oldProjection = new Matrix4f(RenderSystem.getProjectionMatrix());
        PoseStack modelViewStack = RenderSystem.getModelViewStack();

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(515);
        RenderSystem.depthMask(false);

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE
        );

        RenderSystem.setShader(GameRenderer::getRendertypeLightningShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        for (GlowBatch batch : BATCHES) {
            RenderSystem.setProjectionMatrix(batch.projection(), VertexSorting.DISTANCE_TO_ORIGIN);

            modelViewStack.pushPose();
            modelViewStack.last().pose().identity();
            modelViewStack.mulPoseMatrix(batch.modelView());
            RenderSystem.applyModelViewMatrix();

            drawBatch(batch);

            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
        }

        RenderSystem.setProjectionMatrix(oldProjection, VertexSorting.DISTANCE_TO_ORIGIN);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();

        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
        RenderSystem.enableDepthTest();

        BATCHES.clear();
    }

    private static void drawBatch(GlowBatch batch) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int layerCount = Math.max(1, 5 * batch.smooth());

        for (int i = 0; i < layerCount; ++i) {
            float scale = 1.0F + i * (batch.width() / batch.smooth());
            float f4 = (float) i / (float) layerCount * 50.0F;

            PoseStack replay = new PoseStack();
            replay.last().pose().set(batch.localPose());

            float alpha = batch.alphaBase() / batch.smooth();

            /*
             * Slightly softer than the normal world glow.
             * This restores shader-resistant color without making vanilla look doubled.
             */
            alpha *= 0.68F;

            if (batch.forceWhip()) {
                float whipWidth = scale * batch.xScale();

                emitCurvedBladeBody(
                        replay,
                        builder,
                        batch.bladeLength(),
                        batch.red(),
                        batch.green(),
                        batch.blue(),
                        alpha,
                        true,
                        whipWidth,
                        batch.whipTick(),
                        batch.whipAttackSwing()
                );

                emitCurvedBladeTip(
                        replay,
                        builder,
                        batch.bladeLength(),
                        TIP_SIZE * whipWidth,
                        TIP_LENGTH,
                        batch.red(),
                        batch.green(),
                        batch.blue(),
                        alpha,
                        batch.whipTick(),
                        batch.whipAttackSwing()
                );
            } else {
                replay.scale(
                        scale * batch.xScale(),
                        (batch.crossguard()
                                ? (1.0F - f4 * 0.05F + 2.0F)
                                : (1.0F - f4 * 0.005F + 0.2F)) * batch.yScale(),
                        scale * batch.zScale()
                );

                replay.translate(0.0F, -f4 / 400.0F + 0.06F, 0.0F);

                if (batch.fineCut()) {
                    replay.translate(0.0F, 0.0F, 0.005F + f4 * 0.00001F);
                }

                emitBladeBody(
                        replay.last().pose(),
                        builder,
                        batch.bladeLength(),
                        batch.red(),
                        batch.green(),
                        batch.blue(),
                        alpha
                );
            }
        }

        BufferUploader.drawWithShader(builder.end());
    }

    private static void emitBladeBody(Matrix4f pose,
                                      BufferBuilder builder,
                                      float bladeLength,
                                      float red,
                                      float green,
                                      float blue,
                                      float alpha) {
        float minX = -BODY_HALF;
        float maxX = BODY_HALF;
        float minY = -bladeLength;
        float maxY = 0.0F;
        float minZ = -BODY_HALF;
        float maxZ = BODY_HALF;

        emitQuad(builder, pose,
                minX, minY, maxZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                red, green, blue, alpha);

        emitQuad(builder, pose,
                maxX, minY, minZ,
                minX, minY, minZ,
                minX, maxY, minZ,
                maxX, maxY, minZ,
                red, green, blue, alpha);

        emitQuad(builder, pose,
                minX, minY, minZ,
                minX, minY, maxZ,
                minX, maxY, maxZ,
                minX, maxY, minZ,
                red, green, blue, alpha);

        emitQuad(builder, pose,
                maxX, minY, maxZ,
                maxX, minY, minZ,
                maxX, maxY, minZ,
                maxX, maxY, maxZ,
                red, green, blue, alpha);
    }

    private static void emitCurvedBladeBody(PoseStack poseStack,
                                            BufferBuilder builder,
                                            float bladeLength,
                                            float red,
                                            float green,
                                            float blue,
                                            float alpha,
                                            boolean outerGlow,
                                            float widthScale,
                                            float whipTick,
                                            float whipAttackSwing) {
        Matrix4f pose = poseStack.last().pose();
        float glowScale = (outerGlow ? 1.30F : 1.0F) * widthScale;

        for (int i = 0; i < FORCE_WHIP_SEGMENTS; ++i) {
            float t0 = (float) i / (float) FORCE_WHIP_SEGMENTS;
            float t1 = (float) (i + 1) / (float) FORCE_WHIP_SEGMENTS;

            Vec3 p0 = forceWhipPoint(bladeLength, t0, whipTick, whipAttackSwing);
            Vec3 p1 = forceWhipPoint(bladeLength, t1, whipTick, whipAttackSwing);

            float taper = 1.0F - t0 * 0.40F;
            float half0 = BODY_HALF * glowScale * taper;
            float half1 = BODY_HALF * glowScale * (1.0F - t1 * 0.40F);

            emitSegmentPrism(builder, pose, p0, p1, half0, half1, red, green, blue, alpha);
        }
    }

    private static void emitCurvedBladeTip(PoseStack poseStack,
                                           BufferBuilder builder,
                                           float bladeLength,
                                           float half,
                                           float tipLength,
                                           float red,
                                           float green,
                                           float blue,
                                           float alpha,
                                           float whipTick,
                                           float whipAttackSwing) {
        Matrix4f pose = poseStack.last().pose();

        Vec3 tipCenter = forceWhipPoint(bladeLength, 1.0F, whipTick, whipAttackSwing);
        Vec3 prevCenter = forceWhipPoint(bladeLength, 0.94F, whipTick, whipAttackSwing);
        Vec3 direction = tipCenter.subtract(prevCenter).normalize();

        if (direction.lengthSqr() < 1.0E-6D) {
            direction = new Vec3(0.0D, -1.0D, 0.0D);
        }

        float tipHalf = half * 0.70F;

        float x1 = (float) tipCenter.x - tipHalf;
        float z1 = (float) tipCenter.z - tipHalf;
        float x2 = (float) tipCenter.x + tipHalf;
        float z2 = (float) tipCenter.z - tipHalf;
        float x3 = (float) tipCenter.x + tipHalf;
        float z3 = (float) tipCenter.z + tipHalf;
        float x4 = (float) tipCenter.x - tipHalf;
        float z4 = (float) tipCenter.z + tipHalf;
        float y = (float) tipCenter.y;

        float ax = (float) (tipCenter.x + direction.x * tipLength);
        float ay = (float) (tipCenter.y + direction.y * tipLength);
        float az = (float) (tipCenter.z + direction.z * tipLength);

        emitQuad(builder, pose,
                x4, y, z4,
                x3, y, z3,
                ax, ay, az,
                ax, ay, az,
                red, green, blue, alpha);

        emitQuad(builder, pose,
                x3, y, z3,
                x2, y, z2,
                ax, ay, az,
                ax, ay, az,
                red, green, blue, alpha);

        emitQuad(builder, pose,
                x2, y, z2,
                x1, y, z1,
                ax, ay, az,
                ax, ay, az,
                red, green, blue, alpha);

        emitQuad(builder, pose,
                x1, y, z1,
                x4, y, z4,
                ax, ay, az,
                ax, ay, az,
                red, green, blue, alpha);
    }

    private static Vec3 forceWhipPoint(float bladeLength, float t, float tick, float attackSwing) {

        float flexT = Mth.clamp((t - FORCE_WHIP_BASE_ANCHOR) / (1.0F - FORCE_WHIP_BASE_ANCHOR), 0.0F, 1.0F);
        float baseBlend = Mth.clamp(t / FORCE_WHIP_BASE_ANCHOR, 0.0F, 1.0F);
        baseBlend = baseBlend * baseBlend * (3.0F - 2.0F * baseBlend);

        float rootFlex = (float) Math.pow(flexT, 0.38D);
        float midFlex = Mth.sin(flexT * Mth.PI);
        float tailFlex = (float) Math.pow(flexT, 0.72D);
        float tipFlex = (float) Math.pow(flexT, 1.55D);

        float swingEnvelope = Mth.sin(attackSwing * Mth.PI);
        float swingDir = attackSwing < 0.50F ? 1.0F : -1.0F;

        float forwardWave = Mth.clamp(attackSwing * 1.95F - flexT * 1.03F + 0.24F, 0.0F, 1.0F);
        float snapWave = Mth.sin(forwardWave * Mth.PI);

        float returnWave = Mth.clamp(attackSwing * 1.72F - flexT * 0.92F - 0.20F, 0.0F, 1.0F);
        float recoilWave = Mth.sin(returnWave * Mth.PI);

        float crackWave = Mth.sin(Mth.clamp((attackSwing - 0.34F) / 0.42F, 0.0F, 1.0F) * Mth.PI);
        float travelSnap = FORCE_WHIP_SWING_SNAP * swingEnvelope * tipFlex;

        float idleWaveA = Mth.sin(flexT * 5.65F + tick * 0.20F);
        float idleWaveB = Mth.sin(flexT * 12.40F - tick * 0.32F);
        float idleWaveC = Mth.cos(flexT * 7.20F + tick * 0.16F);

        float lateral = FORCE_WHIP_CURVE_X * tailFlex;
        lateral += idleWaveA * FORCE_WHIP_TRAIL * 0.42F * rootFlex;
        lateral += idleWaveB * FORCE_WHIP_TRAIL * 0.20F * rootFlex;
        lateral += swingDir * travelSnap * snapWave * (0.30F + 0.70F * tipFlex);
        lateral -= swingDir * travelSnap * 0.70F * recoilWave * (0.08F + 0.92F * tipFlex);
        lateral += swingDir * FORCE_WHIP_SWING_LAG * swingEnvelope * midFlex * rootFlex * 0.30F;
        lateral += swingDir * FORCE_WHIP_SWING_LAG * crackWave * tipFlex * 0.42F;
        lateral *= baseBlend;

        float depth = FORCE_WHIP_CURVE_Z * tailFlex;
        depth += FORCE_WHIP_DROOP * 0.70F * rootFlex;
        depth += idleWaveC * FORCE_WHIP_CURVE_Z * 0.34F * rootFlex;
        depth += Mth.sin(flexT * 10.35F - tick * 0.24F) * FORCE_WHIP_CURVE_Z * 0.20F * rootFlex;
        depth += travelSnap * 0.85F * snapWave * (0.18F + 0.82F * tipFlex);
        depth -= travelSnap * 0.52F * recoilWave * tipFlex;
        depth += FORCE_WHIP_SWING_LAG * crackWave * tipFlex * 0.24F;
        depth *= baseBlend;

        float sagCompression = FORCE_WHIP_DROOP * 0.42F * rootFlex * (0.16F + 0.84F * flexT);
        float swingBow = FORCE_WHIP_DROOP * 0.62F * bladeLength * swingEnvelope * midFlex * rootFlex;
        float tipExtension = FORCE_WHIP_DROOP * 0.52F * bladeLength * snapWave * tipFlex;
        float crackExtension = FORCE_WHIP_DROOP * 0.24F * bladeLength * crackWave * tipFlex;

        float extension = t - sagCompression;
        float y = -bladeLength * extension;

        y += Mth.sin(flexT * 3.85F + tick * 0.10F) * FORCE_WHIP_DROOP * 0.13F * bladeLength * rootFlex * baseBlend;
        y += swingBow * baseBlend;
        y -= tipExtension * baseBlend;
        y -= crackExtension * baseBlend;

        return new Vec3(lateral, y, depth);
    }

    private static void emitSegmentPrism(BufferBuilder builder,
                                         Matrix4f pose,
                                         Vec3 start,
                                         Vec3 end,
                                         float startHalf,
                                         float endHalf,
                                         float red,
                                         float green,
                                         float blue,
                                         float alpha) {
        float sxMin = (float) start.x - startHalf;
        float sxMax = (float) start.x + startHalf;
        float szMin = (float) start.z - startHalf;
        float szMax = (float) start.z + startHalf;
        float sy = (float) start.y;

        float exMin = (float) end.x - endHalf;
        float exMax = (float) end.x + endHalf;
        float ezMin = (float) end.z - endHalf;
        float ezMax = (float) end.z + endHalf;
        float ey = (float) end.y;

        emitQuad(builder, pose,
                sxMin, sy, szMax,
                sxMax, sy, szMax,
                exMax, ey, ezMax,
                exMin, ey, ezMax,
                red, green, blue, alpha);

        emitQuad(builder, pose,
                sxMax, sy, szMin,
                sxMin, sy, szMin,
                exMin, ey, ezMin,
                exMax, ey, ezMin,
                red, green, blue, alpha);

        emitQuad(builder, pose,
                sxMin, sy, szMin,
                sxMin, sy, szMax,
                exMin, ey, ezMax,
                exMin, ey, ezMin,
                red, green, blue, alpha);

        emitQuad(builder, pose,
                sxMax, sy, szMax,
                sxMax, sy, szMin,
                exMax, ey, ezMin,
                exMax, ey, ezMax,
                red, green, blue, alpha);
    }

    private static void emitQuad(BufferBuilder builder,
                                 Matrix4f pose,
                                 float x1,
                                 float y1,
                                 float z1,
                                 float x2,
                                 float y2,
                                 float z2,
                                 float x3,
                                 float y3,
                                 float z3,
                                 float x4,
                                 float y4,
                                 float z4,
                                 float red,
                                 float green,
                                 float blue,
                                 float alpha) {
        builder.vertex(pose, x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.vertex(pose, x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.vertex(pose, x3, y3, z3).color(red, green, blue, alpha).endVertex();
        builder.vertex(pose, x4, y4, z4).color(red, green, blue, alpha).endVertex();
    }

    private record GlowBatch(Matrix4f projection,
                             Matrix4f modelView,
                             Matrix4f localPose,
                             float bladeLength,
                             float red,
                             float green,
                             float blue,
                             float alphaBase,
                             int smooth,
                             float width,
                             float xScale,
                             float yScale,
                             float zScale,
                             boolean crossguard,
                             boolean forceWhip,
                             boolean fineCut,
                             float whipTick,
                             float whipAttackSwing) {
    }
}