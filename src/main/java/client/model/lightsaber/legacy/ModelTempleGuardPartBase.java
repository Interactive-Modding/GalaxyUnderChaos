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

abstract class ModelTempleGuardPartBase extends LegacyModelBase {
    private static final float OCT_STEP = 0.7853981633974483F;

    protected final List<LegacyModelRenderer> parts = new ArrayList<>();

    protected ModelTempleGuardPartBase() {
        this.textureWidth = 64;
        this.textureHeight = 32;
    }

    protected LegacyModelRenderer box(int texU, int texV, float x, float y, float z, int dx, int dy, int dz) {
        return box(texU, texV, x, y, z, dx, dy, dz, 0.0F, 0.0F, 0.0F);
    }

    protected LegacyModelRenderer box(int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float rotX, float rotY, float rotZ) {
        LegacyModelRenderer model = new LegacyModelRenderer(this, texU, texV);
        model.setRotationPoint(0.0F, 0.0F, 0.0F);
        model.addBox(x, y, z, dx, dy, dz, 0.0F);
        model.rotateAngleX = rotX;
        model.rotateAngleY = rotY;
        model.rotateAngleZ = rotZ;
        this.parts.add(model);
        return model;
    }

    /**
     * 3 x height x 1 wall strips use an 8-pixel-wide vanilla cube UV island.
     * The four islands are placed side-by-side without touching the gold ring islands.
     */
    protected void octagonalSleeve(float y, int height, float radius, int texU, int texV) {
        for (int i = 0; i < 8; ++i) {
            box(texU + ((i & 3) * 8), texV, -1.5F, y, radius, 3, height, 1, 0.0F, i * OCT_STEP, 0.0F);
        }
    }

    /**
     * 4 x height x 1 ring panels need 10 pixels of UV width. The old 8-pixel stride caused
     * neighboring ring islands and sleeve islands to overlap, which made the Temple Guard texture smear.
     */
    protected void octagonalRing(float y, int height, float radius, int texU, int texV) {
        for (int i = 0; i < 8; ++i) {
            box(texU + ((i & 3) * 10), texV, -2.0F, y, radius, 4, height, 1, 0.0F, i * OCT_STEP, 0.0F);
        }
    }

    protected void raisedRib(float y, int height, float radius, int texU, int texV, int index) {
        box(texU, texV, -0.5F, y, radius, 1, height, 1, 0.0F, index * 1.5707963267948966F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
        for (LegacyModelRenderer part : this.parts) {
            part.render(scale);
        }
    }
}
