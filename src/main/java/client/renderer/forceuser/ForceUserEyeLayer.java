package client.renderer.forceuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.galaxyunderchaos;

public class ForceUserEyeLayer extends RenderLayer<ForceUserEntity, ForceUserLayeredModel> {
    public ForceUserEyeLayer(RenderLayerParent<ForceUserEntity, ForceUserLayeredModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ForceUserEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        EyeOverlay overlay = resolveEyeTexture(entity);
        if (overlay == null) {
            return;
        }

        this.getParentModel().setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        RenderType renderType = overlay.glowing ? RenderType.eyes(overlay.texture) : RenderType.entityCutoutNoCull(overlay.texture);
        VertexConsumer consumer = buffer.getBuffer(renderType);
        int light = overlay.glowing ? 15728640 : packedLight;
        this.getParentModel().renderToBuffer(poseStack, consumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private EyeOverlay resolveEyeTexture(ForceUserEntity entity) {
        boolean jediOriginalSpecies = entity.getSpecies().usesEmbeddedJediOriginalRobe();
        String originSuffix = jediOriginalSpecies ? "jedi_og" : "sith_og";

        // Sith always gets the fixed Sith-eye sheet for its species origin group:
        // Rodian/Zabrak/Twi'lek/Togruta -> sith_eyes_jedi_og
        // Every other species             -> sith_eyes_sith_og
        if (entity.getForceUserSide().isDark()) {
            return firstExisting(true,
                    "sith_eyes_" + originSuffix,
                    // Compatibility for the older misspelled files that were exported as "sith_org".
                    jediOriginalSpecies ? "sith_eyes_jedi_org" : "sith_eyes_sith_org");
        }

        // Jedi/neutral eye color uses the same color set, with the correct origin suffix.
        // Example: blue_eyes_jedi_og for Rodian/Zabrak/Twi'lek/Togruta originals,
        //          blue_eyes_sith_og for Sith-original species spawning as Jedi.
        String color = entity.getEyeColor();
        EyeOverlay colored = firstExisting(false,
                color + "_eyes_" + originSuffix,
                jediOriginalSpecies ? color + "_eyes_jedi_org" : color + "_eyes_sith_org");
        if (colored != null) {
            return colored;
        }

        // Safe fallbacks if a selected color file is missing.
        return firstExisting(false,
                "blue_eyes_" + originSuffix,
                "green_eyes_" + originSuffix,
                "brown_eyes_" + originSuffix,
                jediOriginalSpecies ? "blue_eyes_jedi_org" : "blue_eyes_sith_org");
    }

    private static EyeOverlay firstExisting(boolean glowing, String... ids) {
        for (String id : ids) {
            ResourceLocation texture = forceUserTexture(id);
            if (exists(texture)) {
                return new EyeOverlay(texture, glowing);
            }
        }
        return null;
    }

    private static ResourceLocation forceUserTexture(String id) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/force_user/" + id + ".png");
    }

    private static boolean exists(ResourceLocation location) {
        return Minecraft.getInstance().getResourceManager().getResource(location).isPresent();
    }

    private record EyeOverlay(ResourceLocation texture, boolean glowing) {
    }
}
