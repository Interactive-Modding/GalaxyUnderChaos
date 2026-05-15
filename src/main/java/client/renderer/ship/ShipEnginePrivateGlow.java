package client.renderer.ship;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.ArrayList;
import java.util.List;

/**
 * Shader-resistant private engine exhaust pass.
 *
 * The ship engines previously used a normal translucent RenderType. That works in vanilla, but Iris/Oculus
 * and some optimization stacks can route custom translucent RenderTypes through shader programs that strip
 * or recolor additive POSITION_COLOR geometry. This mirrors the private saber outer-glow path: queue the
 * already-transformed engine geometry during the entity render, then replay it in a controlled additive pass
 * after the level has rendered.
 */
@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT)
public final class ShipEnginePrivateGlow {
    private static final int MAX_BATCHES = 256;
    private static final List<EngineConeBatch> BATCHES = new ArrayList<>();

    private ShipEnginePrivateGlow() {
    }

    public static void queueCone(Matrix4f localPose,
                                 float x,
                                 float y,
                                 float z,
                                 float radius,
                                 float length,
                                 int red,
                                 int green,
                                 int blue,
                                 int alpha) {
        if (localPose == null || length <= 0.0F || radius <= 0.0F || alpha <= 0) {
            return;
        }

        if (BATCHES.size() > MAX_BATCHES) {
            BATCHES.clear();
        }

        BATCHES.add(new EngineConeBatch(
                new Matrix4f(RenderSystem.getProjectionMatrix()),
                new Matrix4f(RenderSystem.getModelViewStack().last().pose()),
                new Matrix4f(localPose),
                x,
                y,
                z,
                radius,
                length,
                red,
                green,
                blue,
                alpha
        ));
    }

    @SubscribeEvent
    public static void renderPrivateEngineGlow(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL || BATCHES.isEmpty()) {
            return;
        }

        Matrix4f oldProjection = new Matrix4f(RenderSystem.getProjectionMatrix());
        PoseStack modelViewStack = RenderSystem.getModelViewStack();

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(515);
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        for (EngineConeBatch batch : BATCHES) {
            RenderSystem.setProjectionMatrix(batch.projection(), VertexSorting.DISTANCE_TO_ORIGIN);

            modelViewStack.pushPose();
            modelViewStack.last().pose().identity();
            modelViewStack.mulPoseMatrix(batch.modelView());
            RenderSystem.applyModelViewMatrix();

            drawCone(batch);

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
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BATCHES.clear();
    }

    private static void drawCone(EngineConeBatch batch) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        Matrix4f pose = batch.localPose();
        int segments = 16;
        float tipZ = batch.z() - batch.length();

        for (int i = 0; i < segments; i++) {
            double a0 = (Math.PI * 2.0D * i) / segments;
            double a1 = (Math.PI * 2.0D * (i + 1)) / segments;

            float x0 = batch.x() + (float)Math.cos(a0) * batch.radius();
            float y0 = batch.y() + (float)Math.sin(a0) * batch.radius();
            float x1 = batch.x() + (float)Math.cos(a1) * batch.radius();
            float y1 = batch.y() + (float)Math.sin(a1) * batch.radius();

            vertex(builder, pose, x0, y0, batch.z(), batch.red(), batch.green(), batch.blue(), batch.alpha());
            vertex(builder, pose, x1, y1, batch.z(), batch.red(), batch.green(), batch.blue(), batch.alpha());
            vertex(builder, pose, batch.x(), batch.y(), tipZ, batch.red(), batch.green(), batch.blue(), 0);
        }

        BufferUploader.drawWithShader(builder.end());
    }

    private static void vertex(BufferBuilder builder,
                               Matrix4f pose,
                               float x,
                               float y,
                               float z,
                               int red,
                               int green,
                               int blue,
                               int alpha) {
        builder.vertex(pose, x, y, z).color(red, green, blue, alpha).endVertex();
    }

    private record EngineConeBatch(Matrix4f projection,
                                   Matrix4f modelView,
                                   Matrix4f localPose,
                                   float x,
                                   float y,
                                   float z,
                                   float radius,
                                   float length,
                                   int red,
                                   int green,
                                   int blue,
                                   int alpha) {
    }
}
