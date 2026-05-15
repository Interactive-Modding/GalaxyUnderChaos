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

package client.model.lightsaber.legacy;

import client.renderer.lightsaber.legacy.LegacyModelBase;
import client.renderer.lightsaber.legacy.LegacyModelRenderer;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared helper for the Arena hilt family.
 *
 * Identity note: Arena is the Adi Gallia-inspired hilt.  The model is kept as
 * four modular Advanced Lightsabers-style sections, but the proportions and
 * texture islands below are authored around her smoother silver/brass hilt
 * rather than the harsher Graflex/Knighted/Juggernaut silhouettes.
 *
 * UV contract:
 * - textureWidth/textureHeight stay 64x32 like the other legacy Java hilt parts.
 * - octTube uses four reusable face strips at U 0, 12, 24, and 36.
 * - small detail boxes live in the isolated U 48+ and V 22+ islands.
 *
 * Do not widen the ring radii back into the 3.8+ range except for a deliberate
 * emitter lip.  The previous Arena pass used oversized alternating rings, which
 * made the sections look disconnected and caused the ribs to visually fight each
 * other when assembled.
 */
abstract class ModelArenaPartBase extends LegacyModelBase {
    private static final float OCT_STEP = 0.7853981633974483F;

    protected final List<LegacyModelRenderer> parts = new ArrayList<>();

    protected ModelArenaPartBase() {
        this.textureWidth = 64;
        this.textureHeight = 32;
    }

    protected LegacyModelRenderer box(int texU, int texV, float x, float y, float z, int dx, int dy, int dz) {
        return box(texU, texV, x, y, z, dx, dy, dz, 0.0F, 0.0F, 0.0F);
    }

    protected LegacyModelRenderer box(int texU, int texV, float x, float y, float z, int dx, int dy, int dz,
                                      float rotX, float rotY, float rotZ) {
        LegacyModelRenderer model = new LegacyModelRenderer(this, texU, texV);
        model.setRotationPoint(0.0F, 0.0F, 0.0F);
        model.addBox(x, y, z, dx, dy, dz, 0.0F);
        model.rotateAngleX = rotX;
        model.rotateAngleY = rotY;
        model.rotateAngleZ = rotZ;
        this.parts.add(model);
        return model;
    }

    protected void octTube(float y, int height, float radius, int width, int texU, int texV) {
        float x = -(width / 2.0F);
        for (int i = 0; i < 8; ++i) {
            box(texU + ((i & 3) * 12), texV, x, y, radius, width, height, 1, 0.0F, i * OCT_STEP, 0.0F);
        }
    }

    protected void sleeve(float y, int height, float radius, int texU, int texV) {
        octTube(y, height, radius, 4, texU, texV);
    }

    /** Smooth low collar used for section seams. */
    protected void collar(float y, float radius, int texU, int texV) {
        octTube(y, 1, radius, 4, texU, texV);
    }

    /** Slightly raised decorative band; still kept close to sleeve radius. */
    protected void band(float y, float radius, int texU, int texV) {
        octTube(y, 1, radius, 4, texU, texV);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                       float netHeadYaw, float headPitch, float scale) {
        for (LegacyModelRenderer part : this.parts) {
            part.render(scale);
        }
    }
}
