package client.renderer.ship;

import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.entity.FlashfireEntity;
import server.galaxyunderchaos.galaxyunderchaos;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.model.GeoModel;

public class FlashfireGeoModel extends GeoModel<FlashfireEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(galaxyunderchaos.MODID, "geo/flashfire.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/flashfire/flashfire.png");
    public static final ResourceLocation BASE_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/flashfire/flashfire_base.png");
    public static final ResourceLocation PRIMARY_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/flashfire/flashfire_primary.png");
    public static final ResourceLocation SECONDARY_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/flashfire/flashfire_secondary.png");
    public static final ResourceLocation INTERIOR_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/flashfire/flashfire_inside.png");
    private static final ThreadLocal<ResourceLocation> TEXTURE_OVERRIDE = new ThreadLocal<>();

    public static void setTextureOverride(ResourceLocation texture) {
        TEXTURE_OVERRIDE.set(texture);
    }

    public static void clearTextureOverride() {
        TEXTURE_OVERRIDE.remove();
    }
    private static final ResourceLocation ANIMATIONS = new ResourceLocation(galaxyunderchaos.MODID, "animations/flashfire.animation.json");

    @Override
    public ResourceLocation getModelResource(FlashfireEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(FlashfireEntity animatable) {
        ResourceLocation override = TEXTURE_OVERRIDE.get();
        return override != null ? override : TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FlashfireEntity animatable) {
        return ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(FlashfireEntity ship, long instanceId, AnimationState<FlashfireEntity> animationState) {
        super.setCustomAnimations(ship, instanceId, animationState);

        float partialTick = animationState.getPartialTick();
        float cockpit = ship.getCockpitProgress(partialTick);
        float gear = ship.getGearProgress(partialTick);

        rotateBone("Cockpit", 68.0F * cockpit, 0.0F, 0.0F);

        rotateBone("LandingGearFront", -82.0F * gear, 0.0F, 0.0F);
        rotateBone("FrontSupportThing", -72.0F * gear, 0.0F, 0.0F);
        rotateBone("BackSupportThing", -72.0F * gear, 0.0F, 0.0F);

        rotateBone("LeftLandingGear", -82.0F * gear, 0.0F, 0.0F);
        rotateBone("leftFrontSupportThing", -72.0F * gear, 0.0F, 0.0F);
        rotateBone("leftBackSupportThing", -72.0F * gear, 0.0F, 0.0F);

        rotateBone("RightLandingGear", -82.0F * gear, 0.0F, 0.0F);
        rotateBone("rightFrontSupportThing", -72.0F * gear, 0.0F, 0.0F);
        rotateBone("rightBackSupportThing", -72.0F * gear, 0.0F, 0.0F);
    }

    private void rotateBone(String boneName, float xDegrees, float yDegrees, float zDegrees) {
        CoreGeoBone bone = this.getAnimationProcessor().getBone(boneName);
        if (bone == null) {
            return;
        }
        bone.setRotX((float)Math.toRadians(xDegrees));
        bone.setRotY((float)Math.toRadians(yDegrees));
        bone.setRotZ((float)Math.toRadians(zDegrees));
    }
}
