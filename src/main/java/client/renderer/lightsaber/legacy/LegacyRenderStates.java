package client.renderer.lightsaber.legacy;

import client.mixin.RenderStateShardAccessor;
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
}