package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import server.galaxyunderchaos.entity.ForceAbilityEffectEntity;
import server.galaxyunderchaos.galaxyunderchaos;

public class ForceAbilityEffectRenderer extends EntityRenderer<ForceAbilityEffectEntity> {
    public ForceAbilityEffectRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ForceAbilityEffectEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Entity anchorEntity = entity.getAnchorEntity();
        if (!(anchorEntity instanceof LivingEntity livingAnchor)) {
            return;
        }

        Vec3 anchor = entity.getRenderAnchor(partialTick);
        Vec3 origin = entity.position();
        Vec3 offset = anchor.subtract(origin);
        Matrix4f pose = poseStack.last().pose();
        VertexConsumer consumer = buffer.getBuffer(RenderType.lightning());
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition().subtract(origin);

        float progress = entity.getProgress(partialTick);
        float fade = 1.0F - progress;
        float radius = entity.getRadius();
        float height = Math.max(1.0F, livingAnchor.getBbHeight());

        switch (entity.getEffectKind()) {
            case ForceAbilityEffectEntity.KIND_HEAL -> renderHeal(consumer, pose, offset, radius, height, progress, fade);
            case ForceAbilityEffectEntity.KIND_FORTIFY -> renderFortify(consumer, pose, offset, radius, height, progress, fade);
            case ForceAbilityEffectEntity.KIND_STUN -> renderStun(consumer, pose, offset, radius, height, progress, fade);
            case ForceAbilityEffectEntity.KIND_WOUND -> renderWound(consumer, pose, offset, radius, height, progress, fade);
            case ForceAbilityEffectEntity.KIND_STEALTH -> renderStealth(consumer, pose, offset, radius, height, progress, fade);
            case ForceAbilityEffectEntity.KIND_SPEED -> renderSpeed(consumer, pose, offset, radius, height, progress, fade, livingAnchor, cameraPos);
            case ForceAbilityEffectEntity.KIND_MEDITATION -> renderMeditation(consumer, pose, offset, radius, height, progress, fade);
            case ForceAbilityEffectEntity.KIND_RESIST -> renderResist(consumer, pose, offset, radius, height, progress, fade);
            case ForceAbilityEffectEntity.KIND_REBOUND -> renderRebound(consumer, pose, offset, radius, height, progress, fade);
            case ForceAbilityEffectEntity.KIND_SIGHT, ForceAbilityEffectEntity.KIND_THROW -> {
                return;
            }
            default -> renderMeditation(consumer, pose, offset, radius, height, progress, fade);
        }

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private static void renderHeal(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, float progress, float fade) {
        int alpha = alpha(180, fade);
        drawRing(consumer, pose, c.add(0.0D, 0.18D + progress * height * 0.75D, 0.0D), radius * (0.35F + progress * 0.45F), 0.045F, 85, 255, 165, alpha);
        drawRing(consumer, pose, c.add(0.0D, 0.42D + progress * height * 0.55D, 0.0D), radius * (0.25F + progress * 0.35F), 0.032F, 230, 255, 245, alpha(145, fade));
        drawVerticalCross(consumer, pose, c.add(0.0D, height * 0.55D, 0.0D), radius * 0.35F, 0.04F, 150, 255, 210, alpha(155, fade));
    }

    private static void renderFortify(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, float progress, float fade) {
        int alpha = alpha(175, fade);
        Vec3 center = c.add(0.0D, height * 0.52D, 0.0D);
        float pulse = 1.0F + Mth.sin(progress * Mth.PI) * 0.045F;
        float bubbleRadius = Math.max(radius * 1.08F, height * 0.58F) * pulse;

        // Fortify is a protective Force barrier, visually distinct from Resist Energy's orange heat shield.
        drawBubbleShell(consumer, pose, center, bubbleRadius, 142, 64, 255, alpha);
        drawBubbleShell(consumer, pose, center, bubbleRadius * 0.86F, 230, 190, 255, alpha(95, fade));

        drawRing(consumer, pose, center.add(0.0D, -bubbleRadius * 0.62F, 0.0D), bubbleRadius * 0.72F, 0.030F, 190, 105, 255, alpha(130, fade));
        drawRing(consumer, pose, center.add(0.0D, bubbleRadius * 0.62F, 0.0D), bubbleRadius * 0.45F, 0.024F, 245, 220, 255, alpha(100, fade));
    }

    private static void renderStun(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, float progress, float fade) {
        int alpha = alpha(190, fade);
        float r = radius * (0.55F + Mth.sin(progress * Mth.PI) * 0.08F);
        drawVerticalCage(consumer, pose, c, r, height, 95, 210, 255, alpha);
        drawRing(consumer, pose, c.add(0.0D, height * 0.52D, 0.0D), r, 0.04F, 230, 250, 255, alpha(165, fade));
    }

    private static void renderWound(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, float progress, float fade) {
        int alpha = alpha(185, fade);
        float contraction = 1.0F - progress * 0.35F;
        for (int i = 0; i < 3; i++) {
            float y = height * (0.22F + i * 0.22F);
            drawRing(consumer, pose, c.add(0.0D, y, 0.0D), radius * (0.78F - i * 0.11F) * contraction, 0.052F, 255, 45, 45, alpha);
        }
        drawVerticalCage(consumer, pose, c, radius * 0.55F * contraction, height * 0.95F, 155, 0, 40, alpha(120, fade));
    }

    private static void renderStealth(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, float progress, float fade) {
        int alpha = alpha(115, fade);
        drawRing(consumer, pose, c.add(0.0D, height * 0.15D, 0.0D), radius * (0.80F + progress * 0.12F), 0.035F, 175, 210, 255, alpha);
        drawRing(consumer, pose, c.add(0.0D, height * 0.50D, 0.0D), radius * (0.70F + progress * 0.10F), 0.026F, 210, 235, 255, alpha(90, fade));
        drawRing(consumer, pose, c.add(0.0D, height * 0.85D, 0.0D), radius * (0.55F + progress * 0.08F), 0.020F, 235, 245, 255, alpha(70, fade));
    }

    private static void renderSpeed(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, float progress, float fade, LivingEntity anchor, Vec3 cameraPos) {
        int alpha = alpha(185, fade);
        Vec3 forward = anchor.getLookAngle().normalize();
        Vec3 side = new Vec3(0.0D, 1.0D, 0.0D).cross(forward);
        if (side.lengthSqr() < 1.0E-6D) side = new Vec3(1.0D, 0.0D, 0.0D); else side = side.normalize();
        for (int i = 0; i < 4; i++) {
            double back = 0.25D + i * 0.28D + progress * 0.25D;
            Vec3 center = c.add(forward.scale(-back)).add(0.0D, 0.22D + i * 0.035D, 0.0D);
            Vec3 a = center.add(side.scale(radius * 0.34F));
            Vec3 b = center.add(forward.scale(0.24D));
            Vec3 d = center.add(side.scale(-radius * 0.34F));
            drawLine(consumer, pose, a, b, cameraPos, 0.045F, 105, 230, 255, alpha);
            drawLine(consumer, pose, b, d, cameraPos, 0.045F, 105, 230, 255, alpha);
        }
    }

    private static void renderMeditation(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, float progress, float fade) {
        int alpha = alpha(150, fade);
        drawRing(consumer, pose, c.add(0.0D, 0.12D, 0.0D), radius * (0.95F + progress * 0.35F), 0.035F, 180, 170, 255, alpha);
        drawRing(consumer, pose, c.add(0.0D, height * 0.45D, 0.0D), radius * (0.55F + progress * 0.12F), 0.025F, 120, 220, 255, alpha(115, fade));
        drawRing(consumer, pose, c.add(0.0D, height * 0.78D, 0.0D), radius * (0.35F + progress * 0.08F), 0.020F, 240, 235, 255, alpha(100, fade));
    }

    private static void renderResist(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, float progress, float fade) {
        int alpha = alpha(160, fade);
        Vec3 center = c.add(0.0D, height * 0.52D, 0.0D);
        float bubbleRadius = Math.max(radius * 1.05F, height * 0.56F) * (1.0F + Mth.sin(progress * Mth.PI) * 0.035F);
        drawBubbleShell(consumer, pose, center, bubbleRadius, 255, 185, 70, alpha);
        drawBubbleShell(consumer, pose, center, bubbleRadius * 0.88F, 255, 238, 180, alpha(85, fade));
    }

    private static void renderRebound(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, float progress, float fade) {
        int alpha = alpha(210, fade);
        drawRing(consumer, pose, c.add(0.0D, height * 0.45D, 0.0D), radius * (0.25F + progress * 0.85F), 0.06F, 120, 225, 255, alpha);
        drawVerticalCross(consumer, pose, c.add(0.0D, height * 0.45D, 0.0D), radius * (0.35F + progress * 0.45F), 0.045F, 235, 250, 255, alpha(160, fade));
    }

    private static void drawBubbleShell(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, int r, int g, int b, int a) {
        drawRing(consumer, pose, c, radius, 0.028F, r, g, b, a);

        int latitudeBands = 3;
        for (int i = 1; i <= latitudeBands; i++) {
            float y = radius * (i - 2) / 2.0F;
            float bandRadius = Mth.sqrt(Math.max(0.0F, radius * radius - y * y));
            drawRing(consumer, pose, c.add(0.0D, y, 0.0D), bandRadius, 0.020F, r, g, b, a * 3 / 5);
        }

        drawVerticalBubbleRing(consumer, pose, c, radius, 0.0F, 0.026F, r, g, b, a * 4 / 5);
        drawVerticalBubbleRing(consumer, pose, c, radius, Mth.PI * 0.5F, 0.026F, r, g, b, a * 4 / 5);
        drawBubbleHexArcs(consumer, pose, c, radius, r, g, b, a / 2);
    }

    private static void drawVerticalBubbleRing(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float yaw, float width, int r, int g, int b, int a) {
        int segments = 64;
        Vec3 prevOuter = null;
        Vec3 prevInner = null;
        double cosYaw = Math.cos(yaw);
        double sinYaw = Math.sin(yaw);
        for (int i = 0; i <= segments; i++) {
            double angle = (Mth.PI * 2.0F) * i / segments;
            double radialOuter = Math.cos(angle) * radius;
            double yOuter = Math.sin(angle) * radius;
            double radialInner = Math.cos(angle) * Math.max(0.0F, radius - width);
            double yInner = Math.sin(angle) * Math.max(0.0F, radius - width);

            Vec3 outer = c.add(radialOuter * cosYaw, yOuter, radialOuter * sinYaw);
            Vec3 inner = c.add(radialInner * cosYaw, yInner, radialInner * sinYaw);
            if (prevOuter != null && prevInner != null) {
                quad(consumer, pose, prevOuter, outer, inner, prevInner, r, g, b, a);
            }
            prevOuter = outer;
            prevInner = inner;
        }
    }

    private static void drawBubbleHexArcs(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, int r, int g, int b, int a) {
        int arcs = 6;
        for (int i = 0; i < arcs; i++) {
            double angle = (Mth.PI * 2.0F) * i / arcs;
            Vec3 p1 = c.add(Math.cos(angle) * radius * 0.72F, radius * 0.52F, Math.sin(angle) * radius * 0.72F);
            Vec3 p2 = c.add(Math.cos(angle + Mth.PI / 6.0F) * radius * 0.95F, 0.0D, Math.sin(angle + Mth.PI / 6.0F) * radius * 0.95F);
            Vec3 p3 = c.add(Math.cos(angle) * radius * 0.72F, -radius * 0.52F, Math.sin(angle) * radius * 0.72F);
            drawLineNoCamera(consumer, pose, p1, p2, 0.016F, r, g, b, a);
            drawLineNoCamera(consumer, pose, p2, p3, 0.016F, r, g, b, a);
        }
    }

    private static void drawShieldShell(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, int r, int g, int b, int a) {
        drawRing(consumer, pose, c.add(0.0D, height * 0.18D, 0.0D), radius, 0.035F, r, g, b, a);
        drawRing(consumer, pose, c.add(0.0D, height * 0.50D, 0.0D), radius * 0.82F, 0.030F, r, g, b, a * 3 / 4);
        drawRing(consumer, pose, c.add(0.0D, height * 0.82D, 0.0D), radius * 0.45F, 0.025F, r, g, b, a * 2 / 3);
        drawVerticalCage(consumer, pose, c, radius * 0.72F, height, r, g, b, a / 2);
    }

    private static void drawVerticalCross(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float width, int r, int g, int b, int a) {
        drawLineNoCamera(consumer, pose, c.add(-radius, 0.0D, 0.0D), c.add(radius, 0.0D, 0.0D), width, r, g, b, a);
        drawLineNoCamera(consumer, pose, c.add(0.0D, -radius, 0.0D), c.add(0.0D, radius, 0.0D), width, r, g, b, a);
        drawLineNoCamera(consumer, pose, c.add(0.0D, 0.0D, -radius), c.add(0.0D, 0.0D, radius), width, r, g, b, a);
    }

    private static void drawVerticalCage(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float height, int r, int g, int b, int a) {
        int posts = 8;
        for (int i = 0; i < posts; i++) {
            double angle = (Mth.PI * 2.0F) * i / posts;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            drawLineNoCamera(consumer, pose, c.add(x, 0.12D, z), c.add(x * 0.55D, height * 0.92D, z * 0.55D), 0.022F, r, g, b, a);
        }
    }

    private static void drawRing(VertexConsumer consumer, Matrix4f pose, Vec3 c, float radius, float width, int r, int g, int b, int a) {
        int segments = 48;
        float inner = Math.max(0.0F, radius - width);
        for (int i = 0; i < segments; i++) {
            double a0 = (Mth.PI * 2.0F) * i / segments;
            double a1 = (Mth.PI * 2.0F) * (i + 1) / segments;
            float x0 = (float) Math.cos(a0);
            float z0 = (float) Math.sin(a0);
            float x1 = (float) Math.cos(a1);
            float z1 = (float) Math.sin(a1);
            quad(consumer, pose,
                    c.add(x0 * radius, 0.0D, z0 * radius),
                    c.add(x1 * radius, 0.0D, z1 * radius),
                    c.add(x1 * inner, 0.0D, z1 * inner),
                    c.add(x0 * inner, 0.0D, z0 * inner),
                    r, g, b, a);
        }
    }

    private static void drawLineNoCamera(VertexConsumer consumer, Matrix4f pose, Vec3 start, Vec3 end, float width, int r, int g, int b, int a) {
        Vec3 dir = end.subtract(start);
        if (dir.lengthSqr() < 1.0E-7D) return;
        dir = dir.normalize();
        Vec3 right = new Vec3(0.0D, 1.0D, 0.0D).cross(dir);
        if (right.lengthSqr() < 1.0E-7D) right = new Vec3(1.0D, 0.0D, 0.0D).cross(dir);
        if (right.lengthSqr() < 1.0E-7D) right = new Vec3(1.0D, 0.0D, 0.0D);
        right = right.normalize().scale(width);
        quad(consumer, pose, start.add(right), end.add(right), end.subtract(right), start.subtract(right), r, g, b, a);
    }

    private static void drawLine(VertexConsumer consumer, Matrix4f pose, Vec3 start, Vec3 end, Vec3 cameraPos, float width, int r, int g, int b, int a) {
        Vec3 dir = end.subtract(start);
        if (dir.lengthSqr() < 1.0E-7D) return;
        dir = dir.normalize();
        Vec3 mid = start.add(end).scale(0.5D);
        Vec3 toCamera = cameraPos.subtract(mid);
        if (toCamera.lengthSqr() < 1.0E-7D) toCamera = new Vec3(0.0D, 0.0D, 1.0D); else toCamera = toCamera.normalize();
        Vec3 right = dir.cross(toCamera);
        if (right.lengthSqr() < 1.0E-7D) right = new Vec3(0.0D, 1.0D, 0.0D).cross(dir);
        if (right.lengthSqr() < 1.0E-7D) right = new Vec3(1.0D, 0.0D, 0.0D);
        right = right.normalize().scale(width);
        quad(consumer, pose, start.add(right), end.add(right), end.subtract(right), start.subtract(right), r, g, b, a);
    }

    private static void quad(VertexConsumer consumer, Matrix4f pose, Vec3 a, Vec3 b, Vec3 c, Vec3 d, int r, int g, int bl, int alpha) {
        vertex(consumer, pose, a, r, g, bl, alpha);
        vertex(consumer, pose, b, r, g, bl, alpha);
        vertex(consumer, pose, c, r, g, bl, alpha);
        vertex(consumer, pose, d, r, g, bl, alpha);
        vertex(consumer, pose, d, r, g, bl, alpha);
        vertex(consumer, pose, c, r, g, bl, alpha);
        vertex(consumer, pose, b, r, g, bl, alpha);
        vertex(consumer, pose, a, r, g, bl, alpha);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Vec3 pos, int r, int g, int b, int a) {
        consumer.vertex(pose, (float) pos.x, (float) pos.y, (float) pos.z).color(r, g, b, Mth.clamp(a, 0, 255)).endVertex();
    }

    private static int alpha(int max, float fade) {
        return Mth.clamp((int) (max * Mth.clamp(fade, 0.0F, 1.0F)), 0, 255);
    }

    @Override
    public boolean shouldRender(ForceAbilityEffectEntity entity, net.minecraft.client.renderer.culling.Frustum frustum, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(ForceAbilityEffectEntity entity) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/gui/icons.png");
    }
}
