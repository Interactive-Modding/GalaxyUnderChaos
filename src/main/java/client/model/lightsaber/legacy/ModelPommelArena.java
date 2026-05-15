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

import client.renderer.lightsaber.legacy.LegacyGL;
import client.renderer.lightsaber.legacy.LegacyModelBase;
import client.renderer.lightsaber.legacy.LegacyModelRenderer;
import net.minecraft.world.entity.Entity;

/**
 * Arena / Adi Gallia final hilt
 * Created using Tabula 7.1.0
 */
public class ModelPommelArena extends LegacyModelBase {
    public LegacyModelRenderer top1;
    public LegacyModelRenderer body27;
    public LegacyModelRenderer body1;
    public LegacyModelRenderer top2;
    public LegacyModelRenderer top3;
    public LegacyModelRenderer top4;
    public LegacyModelRenderer top5;
    public LegacyModelRenderer top6;
    public LegacyModelRenderer top7;
    public LegacyModelRenderer top8;
    public LegacyModelRenderer body28;
    public LegacyModelRenderer body29;
    public LegacyModelRenderer body30;
    public LegacyModelRenderer body31;
    public LegacyModelRenderer body32;
    public LegacyModelRenderer body33;
    public LegacyModelRenderer body34;
    public LegacyModelRenderer body35;
    public LegacyModelRenderer body2;
    public LegacyModelRenderer body3;
    public LegacyModelRenderer body4;
    public LegacyModelRenderer body5;
    public LegacyModelRenderer body6;
    public LegacyModelRenderer body7;
    public LegacyModelRenderer body9;

    public ModelPommelArena() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.body7 = new LegacyModelRenderer(this, 12, 16);
        this.body7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body7.addBox(-1.5F, -8.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(body7, 0.0F, -1.5707963267948966F, 0.0F);
        this.top6 = new LegacyModelRenderer(this, 12, 2);
        this.top6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.top6.addBox(-1.5F, -1.0F, -0.38F, 3, 1, 4, 0.0F);
        this.setRotateAngle(top6, 0.0F, -2.356194490192345F, 0.0F);
        this.body28 = new LegacyModelRenderer(this, 12, 8);
        this.body28.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body28.addBox(-1.5F, -4.0F, 2.62F, 3, 4, 1, 0.0F);
        this.setRotateAngle(body28, -0.2792526803190927F, 0.0F, 0.0F);
        this.body1 = new LegacyModelRenderer(this, 12, 16);
        this.body1.setRotationPoint(0.0F, 12.6F, 0.0F);
        this.body1.addBox(-1.5F, -8.0F, 2.62F, 3, 1, 1, 0.0F);
        this.body30 = new LegacyModelRenderer(this, 12, 8);
        this.body30.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body30.addBox(-1.5F, -4.0F, 2.62F, 3, 4, 1, 0.0F);
        this.setRotateAngle(body30, -0.2792526803190927F, 1.5707963267948966F, 0.0F);
        this.top5 = new LegacyModelRenderer(this, 12, 2);
        this.top5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.top5.addBox(-1.5F, -1.0F, -0.38F, 3, 1, 4, 0.0F);
        this.setRotateAngle(top5, 0.0F, 3.141592653589793F, 0.0F);
        this.top1 = new LegacyModelRenderer(this, 12, 2);
        this.top1.setRotationPoint(0.0F, 1.3F, 0.0F);
        this.top1.addBox(-1.5F, -1.0F, -0.38F, 3, 1, 4, 0.0F);
        this.body3 = new LegacyModelRenderer(this, 12, 16);
        this.body3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body3.addBox(-1.5F, -8.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(body3, 0.0F, 1.5707963267948966F, 0.0F);
        this.body6 = new LegacyModelRenderer(this, 12, 16);
        this.body6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body6.addBox(-1.5F, -8.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(body6, 0.0F, -2.356194490192345F, 0.0F);
        this.top3 = new LegacyModelRenderer(this, 12, 2);
        this.top3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.top3.addBox(-1.5F, -1.0F, -0.38F, 3, 1, 4, 0.0F);
        this.setRotateAngle(top3, 0.0F, 1.5707963267948966F, 0.0F);
        this.body33 = new LegacyModelRenderer(this, 12, 8);
        this.body33.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body33.addBox(-1.5F, -4.0F, 2.62F, 3, 4, 1, 0.0F);
        this.setRotateAngle(body33, -0.2792526803190927F, -2.356194490192345F, 0.0F);
        this.body27 = new LegacyModelRenderer(this, 0, 0);
        this.body27.setRotationPoint(0.0F, 4.1F, 0.0F);
        this.body27.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.top4 = new LegacyModelRenderer(this, 12, 2);
        this.top4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.top4.addBox(-1.5F, -1.0F, -0.38F, 3, 1, 4, 0.0F);
        this.setRotateAngle(top4, 0.0F, 2.356194490192345F, 0.0F);
        this.body34 = new LegacyModelRenderer(this, 12, 8);
        this.body34.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body34.addBox(-1.5F, -4.0F, 2.62F, 3, 4, 1, 0.0F);
        this.setRotateAngle(body34, -0.2792526803190927F, -1.5707963267948966F, 0.0F);
        this.body9 = new LegacyModelRenderer(this, 12, 16);
        this.body9.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body9.addBox(-1.5F, -8.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(body9, 0.0F, -0.7853981633974483F, 0.0F);
        this.top7 = new LegacyModelRenderer(this, 12, 2);
        this.top7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.top7.addBox(-1.5F, -1.0F, -0.38F, 3, 1, 4, 0.0F);
        this.setRotateAngle(top7, 0.0F, -1.5707963267948966F, 0.0F);
        this.body2 = new LegacyModelRenderer(this, 12, 16);
        this.body2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body2.addBox(-1.5F, -8.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(body2, 0.0F, 0.7853981633974483F, 0.0F);
        this.body5 = new LegacyModelRenderer(this, 12, 16);
        this.body5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body5.addBox(-1.5F, -8.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(body5, 0.0F, 3.141592653589793F, 0.0F);
        this.body29 = new LegacyModelRenderer(this, 12, 8);
        this.body29.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body29.addBox(-1.5F, -4.0F, 2.62F, 3, 4, 1, 0.0F);
        this.setRotateAngle(body29, -0.2792526803190927F, 0.7853981633974483F, 0.0F);
        this.body35 = new LegacyModelRenderer(this, 12, 8);
        this.body35.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body35.addBox(-1.5F, -4.0F, 2.62F, 3, 4, 1, 0.0F);
        this.setRotateAngle(body35, -0.2792526803190927F, -0.7853981633974483F, 0.0F);
        this.body31 = new LegacyModelRenderer(this, 12, 8);
        this.body31.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body31.addBox(-1.5F, -4.0F, 2.62F, 3, 4, 1, 0.0F);
        this.setRotateAngle(body31, -0.2792526803190927F, 2.356194490192345F, 0.0F);
        this.top8 = new LegacyModelRenderer(this, 12, 2);
        this.top8.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.top8.addBox(-1.5F, -1.0F, 1.62F, 3, 1, 2, 0.0F);
        this.setRotateAngle(top8, 0.0F, -0.7853981633974483F, 0.0F);
        this.body4 = new LegacyModelRenderer(this, 12, 16);
        this.body4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body4.addBox(-1.5F, -8.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(body4, 0.0F, 2.356194490192345F, 0.0F);
        this.top2 = new LegacyModelRenderer(this, 12, 2);
        this.top2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.top2.addBox(-1.5F, -1.0F, -0.38F, 3, 1, 4, 0.0F);
        this.setRotateAngle(top2, 0.0F, 0.7853981633974483F, 0.0F);
        this.body32 = new LegacyModelRenderer(this, 12, 8);
        this.body32.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body32.addBox(-1.5F, -4.0F, 2.62F, 3, 4, 1, 0.0F);
        this.setRotateAngle(body32, -0.2792526803190927F, 3.141592653589793F, 0.0F);
        this.body1.addChild(this.body7);
        this.top1.addChild(this.top6);
        this.body27.addChild(this.body28);
        this.body27.addChild(this.body30);
        this.top1.addChild(this.top5);
        this.body1.addChild(this.body3);
        this.body1.addChild(this.body6);
        this.top1.addChild(this.top3);
        this.body27.addChild(this.body33);
        this.top1.addChild(this.top4);
        this.body27.addChild(this.body34);
        this.body1.addChild(this.body9);
        this.top1.addChild(this.top7);
        this.body1.addChild(this.body2);
        this.body1.addChild(this.body5);
        this.body27.addChild(this.body29);
        this.body27.addChild(this.body35);
        this.body27.addChild(this.body31);
        this.top1.addChild(this.top8);
        this.body1.addChild(this.body4);
        this.top1.addChild(this.top2);
        this.body27.addChild(this.body32);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body1.render(f5);
        LegacyGL.glPushMatrix();
        LegacyGL.glTranslatef(this.top1.offsetX, this.top1.offsetY, this.top1.offsetZ);
        LegacyGL.glTranslatef(this.top1.rotationPointX * f5, this.top1.rotationPointY * f5, this.top1.rotationPointZ * f5);
        LegacyGL.glScaled(1.3D, 1.3D, 1.3D);
        LegacyGL.glTranslatef(-this.top1.offsetX, -this.top1.offsetY, -this.top1.offsetZ);
        LegacyGL.glTranslatef(-this.top1.rotationPointX * f5, -this.top1.rotationPointY * f5, -this.top1.rotationPointZ * f5);
        this.top1.render(f5);
        LegacyGL.glPopMatrix();
        this.body27.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(LegacyModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
