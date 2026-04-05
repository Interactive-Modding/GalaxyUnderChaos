//package client.renderer;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.DefaultVertexFormat;
//import com.mojang.blaze3d.vertex.VertexFormat;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.RenderStateShard;
//
//public class CustomRenderTypes {
//
//    public static final RenderType PLASMA_BLADE = createPlasmaBlade();
//
//    private static RenderType createPlasmaBlade() {
//        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
//                .setShaderState(new RenderStateShard.ShaderStateShard(() -> RenderType.entityTranslucent())) // Public shader for translucent entities
//                .setTransparencyState(createTranslucentTransparency()) // Custom transparency
//                .setCullState(createNoCullState()) // Custom no culling
//                .setLightmapState(RenderStateShard.LIGHTMAP) // Use the predefined lightmap state
//                .setOverlayState(RenderStateShard.OVERLAY) // Use the predefined overlay state
//                .createCompositeState(true);
//
//        return RenderType.create(
//                "plasma_blade",
//                DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
//                VertexFormat.Mode.QUADS,
//                256,
//                true,
//                true,
//                compositeState
//        );
//    }
//
//    // Define a custom transparency state for translucency
//    private static RenderStateShard.TransparencyStateShard createTranslucentTransparency() {
//        return new RenderStateShard.TransparencyStateShard(
//                "translucent_transparency",
//                () -> {
//                    RenderSystem.enableBlend();
//                    RenderSystem.defaultBlendFunc();
//                },
//                RenderSystem::disableBlend
//        );
//    }
//
//    // Define a custom no-cull state to ensure two-sided rendering
//    private static RenderStateShard.CullStateShard createNoCullState() {
//        return new RenderStateShard.CullStateShard(false); // Disable culling
//    }
//}
