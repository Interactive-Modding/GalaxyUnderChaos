package client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.entity.CoffinBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

/**
 * Thin vanilla BlockEntityRenderer wrapper around Geckolib's GeoBlockRenderer.
 *
 * Do not extend GeoBlockRenderer directly here. In this Forge/Geckolib target,
 * GeoBlockRenderer already contributes a raw BlockEntity render method. Extending it
 * and adding render(CoffinBlockEntity, ...) creates a same-erasure method clash.
 */
public class CoffinGeoRenderer implements BlockEntityRenderer<CoffinBlockEntity> {
    private final GeoBlockRenderer<CoffinBlockEntity> geoRenderer;

    public CoffinGeoRenderer(BlockEntityRendererProvider.Context context) {
        this.geoRenderer = new GeoBlockRenderer<CoffinBlockEntity>(new CoffinGeoModel()) {
            @Override
            public RenderType getRenderType(CoffinBlockEntity animatable,
                                            ResourceLocation texture,
                                            MultiBufferSource bufferSource,
                                            float partialTick) {
                return RenderType.entityCutoutNoCull(texture);
            }
        };
    }

    @Override
    public void render(CoffinBlockEntity coffin,
                       float partialTick,
                       PoseStack poseStack,
                       MultiBufferSource bufferSource,
                       int packedLight,
                       int packedOverlay) {
        poseStack.pushPose();
        Direction facing = coffin.getFacing();

        // Keep blockstate rotations at zero for these Geckolib coffins and do the
        // visual correction here only.  This keeps the hitbox/block half logic
        // stable while letting the exported .geo model line up with placement.
        poseStack.translate(modelOffsetX(facing), modelOffsetY(facing), modelOffsetZ(facing));
        poseStack.translate(0.5D, 0.0D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(modelRotation(facing)));
        poseStack.translate(-0.5D, 0.0D, -0.5D);

        this.geoRenderer.render(coffin, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }


    private static double modelOffsetY(Direction facing) {
        return 0.0D;
    }

    private static double modelOffsetZ(Direction facing) {
        return switch (facing) {
            // User-reported correction: north render was one block too far forward.
            case SOUTH -> 1.0D;
            // User-reported correction: south render was one block too low/up-screen.
            // In world-space this is the opposite horizontal offset of north.
            case NORTH -> -1.0D;
            default -> 0.0D;
        };
    }
    private static double modelOffsetX(Direction facing) {
        return switch (facing) {
            // User-reported correction: north render was one block too far forward.
            case EAST -> 1.0D;
            // User-reported correction: south render was one block too low/up-screen.
            // In world-space this is the opposite horizontal offset of north.
            case WEST -> -1.0D;
            default -> 0.0D;
        };
    }

    private static float modelRotation(Direction facing) {
        return switch (facing) {
            case NORTH -> 180.0F;
            case EAST -> -180.0F;
            case SOUTH -> 180.0F;
            case WEST -> 180.0F;
            default -> 0.0F;
        };
    }
}
