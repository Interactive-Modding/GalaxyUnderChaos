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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import server.galaxyunderchaos.entity.ForceBeamEffectEntity;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Random;

public class ForceBeamEffectRenderer extends EntityRenderer<ForceBeamEffectEntity> {
    public ForceBeamEffectRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ForceBeamEffectEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Entity owner = entity.getOwnerEntity();
        Vec3 end = entity.getBeamEnd(partialTick);
        if (!(owner instanceof LivingEntity livingOwner) || end == null) {
            return;
        }

        Vec3 origin = entity.position();
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition().subtract(origin);
        VertexConsumer consumer = buffer.getBuffer(RenderType.lightning());
        Matrix4f pose = poseStack.last().pose();

        if (entity.getEffectKind() == ForceBeamEffectEntity.KIND_DRAIN) {
            Vec3 start = getHandPosition(livingOwner, true, partialTick).subtract(origin);
            drawJaggedBolt(consumer, pose, start, end.subtract(origin), cameraPos,
                    entity.getId() * 131L + entity.tickCount * 31L,
                    3, 0.050F, 0.016F,
                    190, 75, 235, 180,
                    255, 205, 255, 235);
        } else {
            int strandCount = 2; // Half the previous lightning density.
            for (int hand = 0; hand < 2; hand++) {
                Vec3 start = getHandPosition(livingOwner, hand == 0, partialTick).subtract(origin);
                for (int strand = 0; strand < strandCount; strand++) {
                    drawJaggedBolt(consumer, pose, start, end.subtract(origin), cameraPos,
                            entity.getId() * 100003L + entity.tickCount * 1009L + hand * 97L + strand * 17L,
                            4, 0.050F, 0.015F,
                            70, 155, 255, 165,
                            225, 245, 255, 235);
                }
            }
        }

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private static Vec3 getHandPosition(LivingEntity owner, boolean rightHand, float partialTick) {
        Vec3 base = owner.getPosition(partialTick);
        Vec3 forward = owner.getLookAngle().normalize();
        Vec3 side = new Vec3(0.0D, 1.0D, 0.0D).cross(forward);

        if (side.lengthSqr() < 1.0E-6D) {
            side = new Vec3(1.0D, 0.0D, 0.0D);
        } else {
            side = side.normalize();
        }

        double handSide = rightHand ? 1.0D : -1.0D;
        // Slightly higher origin: center the bolt between the raised palms instead of below them.
        double handHeight = owner.getEyeHeight() * 0.78D - 0.08D;
        return base
                .add(0.0D, handHeight, 0.0D)
                .add(forward.scale(0.48D))
                .add(side.scale(0.30D * handSide));
    }

    private static void drawJaggedBolt(VertexConsumer consumer, Matrix4f pose, Vec3 start, Vec3 end, Vec3 cameraPos,
                                       long seed, int segments, float outerWidth, float innerWidth,
                                       int outerR, int outerG, int outerB, int outerA,
                                       int innerR, int innerG, int innerB, int innerA) {
        Random random = new Random(seed);
        Vec3 previous = start;
        double length = Math.max(start.distanceTo(end), 0.001D);

        for (int i = 1; i <= segments; i++) {
            float t = i / (float) segments;
            Vec3 next = start.lerp(end, t);

            if (i < segments) {
                double falloff = Math.sin(Math.PI * t);
                double jitter = Math.min(0.45D, length * 0.12D) * falloff;
                next = next.add(
                        (random.nextDouble() - 0.5D) * jitter,
                        (random.nextDouble() - 0.5D) * jitter,
                        (random.nextDouble() - 0.5D) * jitter
                );
            }

            drawLine(consumer, pose, previous, next, cameraPos, outerWidth, outerR, outerG, outerB, outerA);
            drawLine(consumer, pose, previous, next, cameraPos, innerWidth, innerR, innerG, innerB, innerA);

            if (i < segments && random.nextFloat() < 0.25F) {
                Vec3 branch = next.add(
                        (random.nextDouble() - 0.5D) * Math.min(0.55D, length * 0.15D),
                        (random.nextDouble() - 0.5D) * Math.min(0.55D, length * 0.15D),
                        (random.nextDouble() - 0.5D) * Math.min(0.55D, length * 0.15D)
                );
                drawLine(consumer, pose, next, branch, cameraPos, outerWidth * 0.55F, outerR, outerG, outerB, outerA / 2);
                drawLine(consumer, pose, next, branch, cameraPos, innerWidth * 0.65F, innerR, innerG, innerB, innerA / 2);
            }

            previous = next;
        }
    }

    private static void drawLine(VertexConsumer consumer, Matrix4f pose, Vec3 start, Vec3 end, Vec3 cameraPos,
                                 float width, int r, int g, int b, int a) {
        Vec3 dir = end.subtract(start);
        if (dir.lengthSqr() < 1.0E-7D) {
            return;
        }

        dir = dir.normalize();
        Vec3 mid = start.add(end).scale(0.5D);
        Vec3 toCamera = cameraPos.subtract(mid);

        if (toCamera.lengthSqr() < 1.0E-7D) {
            toCamera = new Vec3(0.0D, 0.0D, 1.0D);
        } else {
            toCamera = toCamera.normalize();
        }

        Vec3 right = dir.cross(toCamera);
        if (right.lengthSqr() < 1.0E-7D) {
            right = new Vec3(0.0D, 1.0D, 0.0D).cross(dir);
        }
        if (right.lengthSqr() < 1.0E-7D) {
            right = new Vec3(1.0D, 0.0D, 0.0D);
        }

        right = right.normalize().scale(width);
        Vec3 a1 = start.add(right);
        Vec3 a2 = start.subtract(right);
        Vec3 b1 = end.add(right);
        Vec3 b2 = end.subtract(right);

        vertex(consumer, pose, a1, r, g, b, a);
        vertex(consumer, pose, a2, r, g, b, a);
        vertex(consumer, pose, b2, r, g, b, a);
        vertex(consumer, pose, b1, r, g, b, a);

        vertex(consumer, pose, b1, r, g, b, a);
        vertex(consumer, pose, b2, r, g, b, a);
        vertex(consumer, pose, a2, r, g, b, a);
        vertex(consumer, pose, a1, r, g, b, a);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Vec3 pos, int r, int g, int b, int a) {
        consumer.vertex(pose, (float) pos.x, (float) pos.y, (float) pos.z).color(r, g, b, a).endVertex();
    }

    @Override
    public boolean shouldRender(ForceBeamEffectEntity entity, net.minecraft.client.renderer.culling.Frustum frustum, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(ForceBeamEffectEntity entity) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/gui/icons.png");
    }
}
