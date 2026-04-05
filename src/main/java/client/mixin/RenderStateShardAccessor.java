package client.mixin;

import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderStateShard.class)
public interface RenderStateShardAccessor {

    @Accessor("LIGHTNING_TRANSPARENCY")
    static RenderStateShard.TransparencyStateShard guc$getLightningTransparency() {
        throw new AssertionError();
    }

    @Accessor("COLOR_WRITE")
    static RenderStateShard.WriteMaskStateShard guc$getColorWrite() {
        throw new AssertionError();
    }

    @Accessor("NO_TRANSPARENCY")
    static RenderStateShard.TransparencyStateShard guc$getNoTransparency() {
        throw new AssertionError();
    }

    @Accessor("COLOR_DEPTH_WRITE")
    static RenderStateShard.WriteMaskStateShard guc$getColorDepthWrite() {
        throw new AssertionError();
    }

    @Accessor("NO_OVERLAY")
    static RenderStateShard.OverlayStateShard guc$getNoOverlay() {
        throw new AssertionError();
    }

    @Accessor("NO_LIGHTMAP")
    static RenderStateShard.LightmapStateShard guc$getNoLightmap() {
        throw new AssertionError();
    }

    @Accessor("NO_CULL")
    static RenderStateShard.CullStateShard guc$getNoCull() {
        throw new AssertionError();
    }

    @Accessor("POSITION_COLOR_SHADER")
    static RenderStateShard.ShaderStateShard guc$getPositionColorShader() {
        throw new AssertionError();
    }
}