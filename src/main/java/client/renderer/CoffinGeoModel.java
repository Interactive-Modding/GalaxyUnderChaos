package client.renderer;

import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.entity.CoffinBlockEntity;
import server.galaxyunderchaos.galaxyunderchaos;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.model.GeoModel;

public class CoffinGeoModel extends GeoModel<CoffinBlockEntity> {
    private static final ResourceLocation JEDI_MODEL = new ResourceLocation(galaxyunderchaos.MODID, "geo/jedi_coffin.geo.json");
    private static final ResourceLocation SITH_MODEL = new ResourceLocation(galaxyunderchaos.MODID, "geo/sith_lord_coffin.geo.json");

    private static final ResourceLocation JEDI_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/block/jedi_coffin.png");
    private static final ResourceLocation SITH_TEXTURE = new ResourceLocation(galaxyunderchaos.MODID, "textures/block/sith_lord_coffin.png");

    private static final ResourceLocation JEDI_ANIMATIONS = new ResourceLocation(galaxyunderchaos.MODID, "animations/jedi_coffin.animation.json");
    private static final ResourceLocation SITH_ANIMATIONS = new ResourceLocation(galaxyunderchaos.MODID, "animations/sith_lord_coffin.animation.json");

    @Override
    public ResourceLocation getModelResource(CoffinBlockEntity animatable) {
        return animatable.isSithStyleCoffin() ? SITH_MODEL : JEDI_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(CoffinBlockEntity animatable) {
        return animatable.isSithStyleCoffin() ? SITH_TEXTURE : JEDI_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CoffinBlockEntity animatable) {
        return animatable.isSithStyleCoffin() ? SITH_ANIMATIONS : JEDI_ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(CoffinBlockEntity coffin, long instanceId, AnimationState<CoffinBlockEntity> animationState) {
        super.setCustomAnimations(coffin, instanceId, animationState);

        CoreGeoBone lid = this.getAnimationProcessor().getBone("bone2");
        if (lid == null) {
            return;
        }

        float progress = coffin.getOpenProgress(animationState.getPartialTick());
        // Smoothstep so the lid does not snap when the container opens.
        float eased = progress * progress * (3.0F - 2.0F * progress);

        if (coffin.isSithStyleCoffin()) {
            lid.setRotY((float)Math.toRadians(17.5F * eased));
            lid.setPosX(-2.0F * eased);
            lid.setPosZ(4.0F * eased);
        } else {
            lid.setRotY((float)Math.toRadians(-20.0F * eased));
            lid.setPosX(-1.5F * eased);
            lid.setPosZ(-3.25F * eased);
        }
    }
}
