package client.renderer.ship;

import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.entity.NovadiveEntity;
import server.galaxyunderchaos.galaxyunderchaos;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.model.GeoModel;

public class NovadiveGeoModel extends GeoModel<NovadiveEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(galaxyunderchaos.MODID, "geo/novadive.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/novadive/novadive.png");
    public static final ResourceLocation BASE_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/novadive/novadive_base.png");
    public static final ResourceLocation PRIMARY_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/novadive/novadive_primary.png");
    public static final ResourceLocation SECONDARY_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/novadive/novadive_secondary.png");
    public static final ResourceLocation INTERIOR_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/novadive/novadive_inside.png");
    private static final ThreadLocal<ResourceLocation> TEXTURE_OVERRIDE = new ThreadLocal<>();

    public static void setTextureOverride(ResourceLocation texture) {
        TEXTURE_OVERRIDE.set(texture);
    }

    public static void clearTextureOverride() {
        TEXTURE_OVERRIDE.remove();
    }
    private static final ResourceLocation ANIMATIONS = new ResourceLocation(galaxyunderchaos.MODID, "animations/novadive.animation.json");

    @Override
    public ResourceLocation getModelResource(NovadiveEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(NovadiveEntity animatable) {
        ResourceLocation override = TEXTURE_OVERRIDE.get();
        return override != null ? override : TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(NovadiveEntity animatable) {
        return ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(NovadiveEntity ship, long instanceId, AnimationState<NovadiveEntity> animationState) {
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
