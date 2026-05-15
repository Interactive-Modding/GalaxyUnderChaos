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

import client.mixin.RenderStateShardAccessor;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;

public final class LegacyRenderStates {
    private LegacyRenderStates() {
    }

    public static RenderStateShard.TransparencyStateShard lightningTransparency() {
        return RenderStateShardAccessor.guc$getLightningTransparency();
    }

    public static RenderStateShard.WriteMaskStateShard colorWrite() {
        return RenderStateShardAccessor.guc$getColorWrite();
    }

    public static RenderStateShard.TransparencyStateShard noTransparency() {
        return RenderStateShardAccessor.guc$getNoTransparency();
    }

    public static RenderStateShard.WriteMaskStateShard colorDepthWrite() {
        return RenderStateShardAccessor.guc$getColorDepthWrite();
    }

    public static RenderStateShard.OverlayStateShard noOverlay() {
        return RenderStateShardAccessor.guc$getNoOverlay();
    }

    public static RenderStateShard.LightmapStateShard noLightmap() {
        return RenderStateShardAccessor.guc$getNoLightmap();
    }

    public static RenderStateShard.CullStateShard noCull() {
        return RenderStateShardAccessor.guc$getNoCull();
    }

    public static RenderStateShard.ShaderStateShard positionColorShader() {
        return RenderStateShardAccessor.guc$getPositionColorShader();
    }

    // ============================================================
    // Shader-resistant states for the outer glow.
    // ============================================================

    /**
     * THIS IS THE CRITICAL ONE FOR IRIS/OCULUS COMPATIBILITY.
     *
     * Iris and Oculus classify render types by their shader to decide which
     * gbuffer program to route geometry through. POSITION_COLOR_SHADER falls
     * through to gbuffers_basic which does NOT run translucent compositing,
     * so additive-blended geometry gets silently dropped.
     *
     * RENDERTYPE_LIGHTNING_SHADER is what vanilla lightning bolts use, every
     * shader pack supports it, and Iris/Oculus has explicit translucent
     * handling for it. Its GLSL is functionally identical to position_color
     * (samples Position + Color, multiplies by ColorModulator), so it looks
     * the same in vanilla but actually shows up under shaders.
     *
     * GameRenderer::getRendertypeLightningShader is public static, so no
     * mixin accessor needed.
     */
    private static final RenderStateShard.ShaderStateShard RENDERTYPE_LIGHTNING_SHADER =
            new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeLightningShader);

    /**
     * Additive blend (SRC_ALPHA, ONE). Matches what the lightning shader
     * expects and what shader packs render with for additive translucent
     * geometry.
     */
    private static final RenderStateShard.TransparencyStateShard ADDITIVE_GLOW =
            new RenderStateShard.TransparencyStateShard(
                    "guc_additive_glow",
                    () -> {
                        RenderSystem.enableBlend();
                        RenderSystem.blendFunc(
                                GlStateManager.SourceFactor.SRC_ALPHA,
                                GlStateManager.DestFactor.ONE
                        );
                    },
                    () -> {
                        RenderSystem.disableBlend();
                        RenderSystem.defaultBlendFunc();
                    }
            );

    /**
     * Explicit GL_LEQUAL (515) depth test so behaviour is consistent across
     * vanilla and shader pipelines.
     */
    private static final RenderStateShard.DepthTestStateShard LEQUAL_DEPTH =
            new RenderStateShard.DepthTestStateShard("guc_lequal_depth", 515);

    /**
     * Translucent framebuffer target. Note: Minecraft.useShaderTransparency()
     * returns false when Iris/Oculus is active (that flag is for vanilla's
     * separate translucent buffer feature, not shader packs), so this is
     * mostly a no-op under shaders. The shader switch above is what actually
     * makes the difference. Kept here for vanilla shader-transparency users.
     */
    private static final RenderStateShard.OutputStateShard TRANSLUCENT_TARGET =
            new RenderStateShard.OutputStateShard(
                    "guc_translucent_target",
                    () -> {
                        if (Minecraft.useShaderTransparency()) {
                            Minecraft.getInstance().levelRenderer
                                    .getTranslucentTarget().bindWrite(false);
                        }
                    },
                    () -> {
                        if (Minecraft.useShaderTransparency()) {
                            Minecraft.getInstance()
                                    .getMainRenderTarget().bindWrite(false);
                        }
                    }
            );

    public static RenderStateShard.ShaderStateShard rendertypeLightningShader() {
        return RENDERTYPE_LIGHTNING_SHADER;
    }

    public static RenderStateShard.TransparencyStateShard additiveGlow() {
        return ADDITIVE_GLOW;
    }

    public static RenderStateShard.DepthTestStateShard lequalDepth() {
        return LEQUAL_DEPTH;
    }

    public static RenderStateShard.OutputStateShard translucentTarget() {
        return TRANSLUCENT_TARGET;
    }
}
