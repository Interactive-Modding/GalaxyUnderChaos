/*
 * SPDX-License-Identifier: LGPL-3.0-only
 *
 * This file is part of Galaxy Under Chaos.
 * It contains code, data, model geometry, behavior, or compatibility logic
 * copied, translated, ported, adapted from, or created to support content
 * derived from Advanced Lightsabers 1.2 by FiskFille, credited to FiskFille
 * and Void Adept.
 *
 * Modifications for Galaxy Under Chaos / Minecraft Forge 1.20.1 by
 *  Vitiate and contributors.
 */

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
            float x0 = (x - delta) * scale;
            float y0 = (y - delta) * scale;
            float z0 = (z - delta) * scale;
            float x1 = (x + dx + delta) * scale;
            float y1 = (y + dy + delta) * scale;
            float z1 = (z + dz + delta) * scale;

            // Vanilla 1.7-1.12 ModelBox mirrors by swapping the X bounds before the
            // PositionTextureVertex/TexturedQuad objects are built, then reversing each
            // quad's face. Mechanical's emitter uses several mirrored boxes, so using a
            // simplified modern cube mapper makes its button/guard UVs look like the
            // wrong texture even though the PNG itself is correct.
            if (mirror) {
                float t = x0;
                x0 = x1;
                x1 = t;
            }

            Vertex v000 = new Vertex(x0, y0, z0);
            Vertex v100 = new Vertex(x1, y0, z0);
            Vertex v110 = new Vertex(x1, y1, z0);
            Vertex v010 = new Vertex(x0, y1, z0);
            Vertex v001 = new Vertex(x0, y0, z1);
            Vertex v101 = new Vertex(x1, y0, z1);
            Vertex v111 = new Vertex(x1, y1, z1);
            Vertex v011 = new Vertex(x0, y1, z1);

            float u0 = texU;
            float u1 = texU + dz;
            float u2 = texU + dz + dx;
            float u3 = texU + dz + dx + dx;
            float u4 = texU + dz + dx + dz;
            float u5 = texU + dz + dx + dz + dx;
            float v0 = texV;
            float v1 = texV + dz;
            float v2 = texV + dz + dy;

            PoseStack.Pose pose = poseStack.last();

            // Exact 1.12 ModelBox quad order and UV orientation:
            // x+, x-, y-, y+, z-, z+.  Do not rotate these into a generic cube strip;
            // AL's hilt sheets rely on the original TexturedQuad orientation.
            emitTexturedQuad(consumer, pose, light, overlay, mirror, texWidth, texHeight, u2, v1, u4, v2,
                    v101, v100, v110, v111);
            emitTexturedQuad(consumer, pose, light, overlay, mirror, texWidth, texHeight, u0, v1, u1, v2,
                    v000, v001, v011, v010);
            emitTexturedQuad(consumer, pose, light, overlay, mirror, texWidth, texHeight, u1, v0, u2, v1,
                    v101, v001, v000, v100);
            emitTexturedQuad(consumer, pose, light, overlay, mirror, texWidth, texHeight, u2, v0, u3, v1,
                    v110, v010, v011, v111);
            emitTexturedQuad(consumer, pose, light, overlay, mirror, texWidth, texHeight, u1, v1, u2, v2,
                    v100, v000, v010, v110);
            emitTexturedQuad(consumer, pose, light, overlay, mirror, texWidth, texHeight, u4, v1, u5, v2,
                    v001, v101, v111, v011);
        }

        private static void emitTexturedQuad(VertexConsumer consumer,
                                             PoseStack.Pose pose,
                                             int light,
                                             int overlay,
                                             boolean flipFace,
                                             float texWidth,
                                             float texHeight,
                                             float uMin,
                                             float vMin,
                                             float uMax,
                                             float vMax,
                                             Vertex a,
                                             Vertex b,
                                             Vertex c,
                                             Vertex d) {
            Vertex[] vertices = {a, b, c, d};
            float[] us = {
                    uMax / texWidth,
                    uMin / texWidth,
                    uMin / texWidth,
                    uMax / texWidth
            };
            float[] vs = {
                    vMin / texHeight,
                    vMin / texHeight,
                    vMax / texHeight,
                    vMax / texHeight
            };

            if (flipFace) {
                reverse(vertices);
                reverse(us);
                reverse(vs);
            }

            float[] normal = calculateNormal(vertices);
            LegacyRenderSession session = LegacyRenderSession.get();
            float red = session == null ? 1.0F : session.red();
            float green = session == null ? 1.0F : session.green();
            float blue = session == null ? 1.0F : session.blue();
            float alpha = session == null ? 1.0F : session.alpha();

            for (int i = 0; i < 4; i++) {
                Vertex vertex = vertices[i];
                consumer.vertex(pose.pose(), vertex.x, vertex.y, vertex.z)
                        .color(red, green, blue, alpha)
                        .uv(us[i], vs[i])
                        .overlayCoords(overlay)
                        .uv2(light)
                        .normal(pose.normal(), normal[0], normal[1], normal[2])
                        .endVertex();
            }
        }

        private static float[] calculateNormal(Vertex[] vertices) {
            float ax = vertices[1].x - vertices[0].x;
            float ay = vertices[1].y - vertices[0].y;
            float az = vertices[1].z - vertices[0].z;
            float bx = vertices[2].x - vertices[0].x;
            float by = vertices[2].y - vertices[0].y;
            float bz = vertices[2].z - vertices[0].z;

            float nx = ay * bz - az * by;
            float ny = az * bx - ax * bz;
            float nz = ax * by - ay * bx;
            float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
            if (len < 1.0E-6F) {
                return new float[]{0.0F, 1.0F, 0.0F};
            }
            return new float[]{nx / len, ny / len, nz / len};
        }

        private static void reverse(Vertex[] values) {
            for (int i = 0, j = values.length - 1; i < j; i++, j--) {
                Vertex t = values[i];
                values[i] = values[j];
                values[j] = t;
            }
        }

        private static void reverse(float[] values) {
            for (int i = 0, j = values.length - 1; i < j; i++, j--) {
                float t = values[i];
                values[i] = values[j];
                values[j] = t;
            }
        }
    }

    private record Vertex(float x, float y, float z) {
    }
}
