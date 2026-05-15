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
public class ModelSwitchSectionArena extends LegacyModelBase {
    public LegacyModelRenderer upperConnector1;
    public LegacyModelRenderer button1;
    public LegacyModelRenderer button1_1;
    public LegacyModelRenderer lowerConnector1;
    public LegacyModelRenderer lowerConnector1_1;
    public LegacyModelRenderer upperConnector2;
    public LegacyModelRenderer upperConnector2_1;
    public LegacyModelRenderer upperConnector3;
    public LegacyModelRenderer upperConnector4;
    public LegacyModelRenderer upperConnector5;
    public LegacyModelRenderer upperConnector6;
    public LegacyModelRenderer upperConnector7;
    public LegacyModelRenderer upperConnector8;
    public LegacyModelRenderer button2;
    public LegacyModelRenderer button3;
    public LegacyModelRenderer button4;
    public LegacyModelRenderer button5;
    public LegacyModelRenderer button6;
    public LegacyModelRenderer button7;
    public LegacyModelRenderer button8;
    public LegacyModelRenderer button2_1;
    public LegacyModelRenderer button3_1;
    public LegacyModelRenderer button4_1;
    public LegacyModelRenderer button5_1;
    public LegacyModelRenderer button6_1;
    public LegacyModelRenderer button7_1;
    public LegacyModelRenderer button8_1;
    public LegacyModelRenderer lowerConnector2;
    public LegacyModelRenderer lowerConnector3;
    public LegacyModelRenderer lowerConnector4;
    public LegacyModelRenderer lowerConnector5;
    public LegacyModelRenderer lowerConnector6;
    public LegacyModelRenderer lowerConnector7;
    public LegacyModelRenderer lowerConnector8;
    public LegacyModelRenderer lowerConnector2_1;
    public LegacyModelRenderer lowerConnector3_1;
    public LegacyModelRenderer lowerConnector4_1;
    public LegacyModelRenderer lowerConnector5_1;
    public LegacyModelRenderer lowerConnector6_1;
    public LegacyModelRenderer lowerConnector7_1;
    public LegacyModelRenderer lowerConnector8_1;

    public ModelSwitchSectionArena() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.upperConnector6 = new LegacyModelRenderer(this, 34, 7);
        this.upperConnector6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperConnector6.addBox(-1.5F, -1.0F, 2.62F, 3, 15, 1, 0.0F);
        this.setRotateAngle(upperConnector6, 0.0F, -2.356194490192345F, 0.0F);
        this.lowerConnector1 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector1.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.lowerConnector8 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector8.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector8.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector8, 0.0F, -0.7853981633974483F, 0.0F);
        this.button1_1 = new LegacyModelRenderer(this, 8, 4);
        this.button1_1.setRotationPoint(2.4F, -2.0F, 0.0F);
        this.button1_1.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button1_1, 0.0F, 0.0F, 1.5707963267948966F);
        this.lowerConnector7_1 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector7_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector7_1.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector7_1, 0.0F, -1.5707963267948966F, 0.0F);
        this.button5 = new LegacyModelRenderer(this, 8, 4);
        this.button5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button5.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button5, 0.0F, 3.141592653589793F, 0.0F);
        this.upperConnector7 = new LegacyModelRenderer(this, 34, 7);
        this.upperConnector7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperConnector7.addBox(-1.5F, -1.0F, 2.62F, 3, 15, 1, 0.0F);
        this.setRotateAngle(upperConnector7, 0.0F, -1.5707963267948966F, 0.0F);
        this.button2 = new LegacyModelRenderer(this, 8, 4);
        this.button2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button2.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button2, 0.0F, 0.7853981633974483F, 0.0F);
        this.lowerConnector4 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector4.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector4, 0.0F, 2.356194490192345F, 0.0F);
        this.upperConnector4 = new LegacyModelRenderer(this, 34, 7);
        this.upperConnector4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperConnector4.addBox(-1.5F, -1.0F, 2.62F, 3, 15, 1, 0.0F);
        this.setRotateAngle(upperConnector4, 0.0F, 2.356194490192345F, 0.0F);
        this.lowerConnector4_1 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector4_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector4_1.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector4_1, 0.0F, 2.356194490192345F, 0.0F);
        this.lowerConnector8_1 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector8_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector8_1.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector8_1, 0.0F, -0.7853981633974483F, 0.0F);
        this.button7 = new LegacyModelRenderer(this, 8, 4);
        this.button7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button7.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button7, 0.0F, -1.5707963267948966F, 0.0F);
        this.lowerConnector6_1 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector6_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector6_1.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector6_1, 0.0F, -2.356194490192345F, 0.0F);
        this.upperConnector5 = new LegacyModelRenderer(this, 34, 7);
        this.upperConnector5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperConnector5.addBox(-1.5F, -1.0F, 2.62F, 3, 15, 1, 0.0F);
        this.setRotateAngle(upperConnector5, 0.0F, 3.141592653589793F, 0.0F);
        this.button3 = new LegacyModelRenderer(this, 8, 4);
        this.button3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button3.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button3, 0.0F, 1.5707963267948966F, 0.0F);
        this.button5_1 = new LegacyModelRenderer(this, 8, 4);
        this.button5_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button5_1.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button5_1, 0.0F, 3.141592653589793F, 0.0F);
        this.upperConnector1 = new LegacyModelRenderer(this, 34, 7);
        this.upperConnector1.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.upperConnector1.addBox(-1.5F, -1.0F, 2.62F, 3, 15, 1, 0.0F);
        this.button4_1 = new LegacyModelRenderer(this, 8, 4);
        this.button4_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button4_1.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button4_1, 0.0F, 2.356194490192345F, 0.0F);
        this.lowerConnector3_1 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector3_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector3_1.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector3_1, 0.0F, 1.5707963267948966F, 0.0F);
        this.lowerConnector7 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector7.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector7, 0.0F, -1.5707963267948966F, 0.0F);
        this.button8_1 = new LegacyModelRenderer(this, 8, 4);
        this.button8_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button8_1.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button8_1, 0.0F, -0.7853981633974483F, 0.0F);
        this.button6 = new LegacyModelRenderer(this, 8, 4);
        this.button6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button6.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button6, 0.0F, -2.356194490192345F, 0.0F);
        this.lowerConnector2 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector2.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector2, 0.0F, 0.7853981633974483F, 0.0F);
        this.lowerConnector1_1 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector1_1.setRotationPoint(0.0F, -10.8F, 0.0F);
        this.lowerConnector1_1.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.button2_1 = new LegacyModelRenderer(this, 8, 4);
        this.button2_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button2_1.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button2_1, 0.0F, 0.7853981633974483F, 0.0F);
        this.button6_1 = new LegacyModelRenderer(this, 8, 4);
        this.button6_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button6_1.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button6_1, 0.0F, -2.356194490192345F, 0.0F);
        this.upperConnector2_1 = new LegacyModelRenderer(this, 34, 7);
        this.upperConnector2_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperConnector2_1.addBox(-1.5F, -1.0F, 2.62F, 3, 15, 1, 0.0F);
        this.setRotateAngle(upperConnector2_1, 0.0F, 0.7853981633974483F, 0.0F);
        this.lowerConnector2_1 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector2_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector2_1.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector2_1, 0.0F, 0.7853981633974483F, 0.0F);
        this.button4 = new LegacyModelRenderer(this, 8, 4);
        this.button4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button4.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button4, 0.0F, 2.356194490192345F, 0.0F);
        this.button3_1 = new LegacyModelRenderer(this, 8, 4);
        this.button3_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button3_1.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button3_1, 0.0F, 1.5707963267948966F, 0.0F);
        this.upperConnector2 = new LegacyModelRenderer(this, 24, 23);
        this.upperConnector2.setRotationPoint(1.4F, -6.7F, 3.6F);
        this.upperConnector2.addBox(0.0F, 0.0F, 0.0F, 3, 6, 1, 0.0F);
        this.setRotateAngle(upperConnector2, 0.0F, 0.7853981633974483F, 0.0F);
        this.upperConnector8 = new LegacyModelRenderer(this, 34, 7);
        this.upperConnector8.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperConnector8.addBox(-1.5F, -1.0F, 2.62F, 3, 15, 1, 0.0F);
        this.setRotateAngle(upperConnector8, 0.0F, -0.7853981633974483F, 0.0F);
        this.button8 = new LegacyModelRenderer(this, 8, 4);
        this.button8.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button8.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button8, 0.0F, -0.7853981633974483F, 0.0F);
        this.button7_1 = new LegacyModelRenderer(this, 8, 4);
        this.button7_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.button7_1.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button7_1, 0.0F, -1.5707963267948966F, 0.0F);
        this.lowerConnector6 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector6.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector6, 0.0F, -2.356194490192345F, 0.0F);
        this.lowerConnector5_1 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector5_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector5_1.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector5_1, 0.0F, 3.141592653589793F, 0.0F);
        this.lowerConnector5 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector5.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector5, 0.0F, 3.141592653589793F, 0.0F);
        this.button1 = new LegacyModelRenderer(this, 8, 4);
        this.button1.setRotationPoint(2.4F, -5.5F, 0.0F);
        this.button1.addBox(-1.5F, -5.0F, -0.38F, 3, 5, 4, 0.0F);
        this.setRotateAngle(button1, 0.0F, 0.0F, 1.5707963267948966F);
        this.upperConnector3 = new LegacyModelRenderer(this, 48, 8);
        this.upperConnector3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperConnector3.addBox(-1.5F, -1.0F, 2.62F, 3, 15, 1, 0.0F);
        this.setRotateAngle(upperConnector3, 0.0F, 1.5707963267948966F, 0.0F);
        this.lowerConnector3 = new LegacyModelRenderer(this, 54, 4);
        this.lowerConnector3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerConnector3.addBox(-1.5F, -1.0F, 2.62F, 3, 1, 1, 0.0F);
        this.setRotateAngle(lowerConnector3, 0.0F, 1.5707963267948966F, 0.0F);
        this.upperConnector1.addChild(this.upperConnector6);
        this.lowerConnector1.addChild(this.lowerConnector8);
        this.lowerConnector1_1.addChild(this.lowerConnector7_1);
        this.button1.addChild(this.button5);
        this.upperConnector1.addChild(this.upperConnector7);
        this.button1.addChild(this.button2);
        this.lowerConnector1.addChild(this.lowerConnector4);
        this.upperConnector1.addChild(this.upperConnector4);
        this.lowerConnector1_1.addChild(this.lowerConnector4_1);
        this.lowerConnector1_1.addChild(this.lowerConnector8_1);
        this.button1.addChild(this.button7);
        this.lowerConnector1_1.addChild(this.lowerConnector6_1);
        this.upperConnector1.addChild(this.upperConnector5);
        this.button1.addChild(this.button3);
        this.button1_1.addChild(this.button5_1);
        this.button1_1.addChild(this.button4_1);
        this.lowerConnector1_1.addChild(this.lowerConnector3_1);
        this.lowerConnector1.addChild(this.lowerConnector7);
        this.button1_1.addChild(this.button8_1);
        this.button1.addChild(this.button6);
        this.lowerConnector1.addChild(this.lowerConnector2);
        this.button1_1.addChild(this.button2_1);
        this.button1_1.addChild(this.button6_1);
        this.upperConnector1.addChild(this.upperConnector2_1);
        this.lowerConnector1_1.addChild(this.lowerConnector2_1);
        this.button1.addChild(this.button4);
        this.button1_1.addChild(this.button3_1);
        this.upperConnector1.addChild(this.upperConnector8);
        this.button1.addChild(this.button8);
        this.button1_1.addChild(this.button7_1);
        this.lowerConnector1.addChild(this.lowerConnector6);
        this.lowerConnector1_1.addChild(this.lowerConnector5_1);
        this.lowerConnector1.addChild(this.lowerConnector5);
        this.upperConnector1.addChild(this.upperConnector3);
        this.lowerConnector1.addChild(this.lowerConnector3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        LegacyGL.glPushMatrix();
        LegacyGL.glTranslatef(this.lowerConnector1.offsetX, this.lowerConnector1.offsetY, this.lowerConnector1.offsetZ);
        LegacyGL.glTranslatef(this.lowerConnector1.rotationPointX * f5, this.lowerConnector1.rotationPointY * f5, this.lowerConnector1.rotationPointZ * f5);
        LegacyGL.glScaled(1.2D, 0.3D, 1.2D);
        LegacyGL.glTranslatef(-this.lowerConnector1.offsetX, -this.lowerConnector1.offsetY, -this.lowerConnector1.offsetZ);
        LegacyGL.glTranslatef(-this.lowerConnector1.rotationPointX * f5, -this.lowerConnector1.rotationPointY * f5, -this.lowerConnector1.rotationPointZ * f5);
        this.lowerConnector1.render(f5);
        LegacyGL.glPopMatrix();
        LegacyGL.glPushMatrix();
        LegacyGL.glTranslatef(this.button1_1.offsetX, this.button1_1.offsetY, this.button1_1.offsetZ);
        LegacyGL.glTranslatef(this.button1_1.rotationPointX * f5, this.button1_1.rotationPointY * f5, this.button1_1.rotationPointZ * f5);
        LegacyGL.glScaled(0.4D, 0.4D, 0.4D);
        LegacyGL.glTranslatef(-this.button1_1.offsetX, -this.button1_1.offsetY, -this.button1_1.offsetZ);
        LegacyGL.glTranslatef(-this.button1_1.rotationPointX * f5, -this.button1_1.rotationPointY * f5, -this.button1_1.rotationPointZ * f5);
        this.button1_1.render(f5);
        LegacyGL.glPopMatrix();
        LegacyGL.glPushMatrix();
        LegacyGL.glTranslatef(this.upperConnector1.offsetX, this.upperConnector1.offsetY, this.upperConnector1.offsetZ);
        LegacyGL.glTranslatef(this.upperConnector1.rotationPointX * f5, this.upperConnector1.rotationPointY * f5, this.upperConnector1.rotationPointZ * f5);
        LegacyGL.glScaled(1.1D, 0.7D, 1.1D);
        LegacyGL.glTranslatef(-this.upperConnector1.offsetX, -this.upperConnector1.offsetY, -this.upperConnector1.offsetZ);
        LegacyGL.glTranslatef(-this.upperConnector1.rotationPointX * f5, -this.upperConnector1.rotationPointY * f5, -this.upperConnector1.rotationPointZ * f5);
        this.upperConnector1.render(f5);
        LegacyGL.glPopMatrix();
        LegacyGL.glPushMatrix();
        LegacyGL.glTranslatef(this.lowerConnector1_1.offsetX, this.lowerConnector1_1.offsetY, this.lowerConnector1_1.offsetZ);
        LegacyGL.glTranslatef(this.lowerConnector1_1.rotationPointX * f5, this.lowerConnector1_1.rotationPointY * f5, this.lowerConnector1_1.rotationPointZ * f5);
        LegacyGL.glScaled(1.2D, 0.3D, 1.2D);
        LegacyGL.glTranslatef(-this.lowerConnector1_1.offsetX, -this.lowerConnector1_1.offsetY, -this.lowerConnector1_1.offsetZ);
        LegacyGL.glTranslatef(-this.lowerConnector1_1.rotationPointX * f5, -this.lowerConnector1_1.rotationPointY * f5, -this.lowerConnector1_1.rotationPointZ * f5);
        this.lowerConnector1_1.render(f5);
        LegacyGL.glPopMatrix();
        this.upperConnector2.render(f5);
        LegacyGL.glPushMatrix();
        LegacyGL.glTranslatef(this.button1.offsetX, this.button1.offsetY, this.button1.offsetZ);
        LegacyGL.glTranslatef(this.button1.rotationPointX * f5, this.button1.rotationPointY * f5, this.button1.rotationPointZ * f5);
        LegacyGL.glScaled(0.4D, 0.4D, 0.4D);
        LegacyGL.glTranslatef(-this.button1.offsetX, -this.button1.offsetY, -this.button1.offsetZ);
        LegacyGL.glTranslatef(-this.button1.rotationPointX * f5, -this.button1.rotationPointY * f5, -this.button1.rotationPointZ * f5);
        this.button1.render(f5);
        LegacyGL.glPopMatrix();
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
