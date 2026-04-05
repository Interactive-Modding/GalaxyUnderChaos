package client.renderer.lightsaber.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import java.util.ArrayList;
import java.util.List;

public class LegacyModelRenderer {
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public boolean mirror;

    private final LegacyModelBase base;
    private final int texOffX;
    private final int texOffY;
    private final List<LegacyBox> cubes = new ArrayList<>();
    private final List<LegacyModelRenderer> children = new ArrayList<>();

    public LegacyModelRenderer(LegacyModelBase base, int texOffX, int texOffY) {
        this.base = base;
        this.texOffX = texOffX;
        this.texOffY = texOffY;
    }

    public void setRotationPoint(float x, float y, float z) {
        this.rotationPointX = x;
        this.rotationPointY = y;
        this.rotationPointZ = z;
    }

    public void addBox(float x, float y, float z, int dx, int dy, int dz, float delta) {
        this.cubes.add(new LegacyBox(texOffX, texOffY, x, y, z, dx, dy, dz, delta, mirror, base.textureWidth, base.textureHeight));
    }

    public void addChild(LegacyModelRenderer child) {
        this.children.add(child);
    }

    public void render(float scale) {
        LegacyRenderSession session = LegacyRenderSession.get();
        if (session == null) {
            return;
        }

        PoseStack poseStack = session.poseStack();
        VertexConsumer consumer = session.consumer();

        poseStack.pushPose();
        poseStack.translate(offsetX, offsetY, offsetZ);
        poseStack.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

        if (rotateAngleZ != 0.0F) {
            poseStack.mulPose(Axis.ZP.rotation(rotateAngleZ));
        }
        if (rotateAngleY != 0.0F) {
            poseStack.mulPose(Axis.YP.rotation(rotateAngleY));
        }
        if (rotateAngleX != 0.0F) {
            poseStack.mulPose(Axis.XP.rotation(rotateAngleX));
        }

        for (LegacyBox cube : cubes) {
            cube.render(poseStack, consumer, session.packedLight(), session.packedOverlay(), scale);
        }
        for (LegacyModelRenderer child : children) {
            child.render(scale);
        }
        poseStack.popPose();
    }

    private record LegacyBox(int texU, int texV, float x, float y, float z, int dx, int dy, int dz,
                             float delta, boolean mirror, float texWidth, float texHeight) {
        void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float scale) {
            float minX = (x - delta) * scale;
            float minY = (y - delta) * scale;
            float minZ = (z - delta) * scale;
            float maxX = (x + dx + delta) * scale;
            float maxY = (y + dy + delta) * scale;
            float maxZ = (z + dz + delta) * scale;

            if (mirror) {
                float t = minX;
                minX = maxX;
                maxX = t;
            }

            float u0 = texU / texWidth;
            float u1 = (texU + dz) / texWidth;
            float u2 = (texU + dz + dx) / texWidth;
            float u3 = (texU + dz + dx + dz) / texWidth;
            float u4 = (texU + dz + dx + dz + dx) / texWidth;
            float v0 = texV / texHeight;
            float v1 = (texV + dz) / texHeight;
            float v2 = (texV + dz + dy) / texHeight;

            PoseStack.Pose pose = poseStack.last();

            emitQuad(consumer, pose, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, minY, maxZ, u1, v1, u2, v2, 0, 0, 1, light, overlay);
            emitQuad(consumer, pose, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ, minX, minY, minZ, u3, v1, u4, v2, 0, 0, -1, light, overlay);
            emitQuad(consumer, pose, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ, minX, minY, maxZ, u0, v1, u1, v2, -1, 0, 0, light, overlay);
            emitQuad(consumer, pose, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, maxX, minY, minZ, u2, v1, u3, v2, 1, 0, 0, light, overlay);
            emitQuad(consumer, pose, minX, minY, minZ, minX, minY, maxZ, maxX, minY, maxZ, maxX, minY, minZ, u1, v0, u2, v1, 0, -1, 0, light, overlay);
            emitQuad(consumer, pose, minX, maxY, maxZ, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, u2, v0, u3, v1, 0, 1, 0, light, overlay);
        }

        private static void emitQuad(VertexConsumer consumer,
                                     PoseStack.Pose pose,
                                     float x1, float y1, float z1,
                                     float x2, float y2, float z2,
                                     float x3, float y3, float z3,
                                     float x4, float y4, float z4,
                                     float uMin, float vMin, float uMax, float vMax,
                                     float nx, float ny, float nz,
                                     int light, int overlay) {
            consumer.vertex(pose.pose(), x1, y1, z1).color(1.0F, 1.0F, 1.0F, 1.0F).uv(uMin, vMax).overlayCoords(overlay).uv2(light).normal(pose.normal(), nx, ny, nz).endVertex();
            consumer.vertex(pose.pose(), x2, y2, z2).color(1.0F, 1.0F, 1.0F, 1.0F).uv(uMin, vMin).overlayCoords(overlay).uv2(light).normal(pose.normal(), nx, ny, nz).endVertex();
            consumer.vertex(pose.pose(), x3, y3, z3).color(1.0F, 1.0F, 1.0F, 1.0F).uv(uMax, vMin).overlayCoords(overlay).uv2(light).normal(pose.normal(), nx, ny, nz).endVertex();
            consumer.vertex(pose.pose(), x4, y4, z4).color(1.0F, 1.0F, 1.0F, 1.0F).uv(uMax, vMax).overlayCoords(overlay).uv2(light).normal(pose.normal(), nx, ny, nz).endVertex();
        }
    }
}
