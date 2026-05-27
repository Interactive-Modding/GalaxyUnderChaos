package client.renderer.forceuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.entity.forceuser.ForceUserSide;
import server.galaxyunderchaos.entity.forceuser.ForceUserSpecies;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Map;

public class ForceUserRobeLayer extends RenderLayer<ForceUserEntity, ForceUserLayeredModel> {
    private static final ResourceLocation SITH_ROBES = robeTexture("sith_robes");
    private static final ResourceLocation SITH_ROBES_ALT = robeTexture("sith_robes_alt");
    private static final ResourceLocation JEDI_ROBES = robeTexture("jedi_robes");
    private static final ResourceLocation JEDI_ROBES_ALT = robeTexture("jedi_robes_alt");
    private static final ResourceLocation NEUTRAL_ROBES = robeTexture("neutral_robes");
    private static final ResourceLocation NEUTRAL_ROBES_ALT = robeTexture("neutral_robes_alt");

    private final Map<String, EntityModel<ForceUserEntity>> robes;

    public ForceUserRobeLayer(RenderLayerParent<ForceUserEntity, ForceUserLayeredModel> parent, net.minecraft.client.renderer.entity.EntityRendererProvider.Context context) {
        super(parent);
        this.robes = ForceUserModelLayers.bakeModels(context, ForceUserModelLayers.robeFactories());
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ForceUserEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.getType() == galaxyunderchaos.JEDI_TEMPLE_GUARD.get()
                || entity.getType() == galaxyunderchaos.SITH_GUARD.get()) {
            return;
        }
        ForceUserSpecies species = entity.getSpecies();
        ForceUserSide side = entity.getForceUserSide();

        boolean jediOriginalSpecies = species.usesEmbeddedJediOriginalRobe();

        /*
         * Robe rule is based on the species' ORIGINAL robe set, not the current side.
         *
         * Jedi-original species: Rodian, Zabrak, Twi'lek, Togruta.
         * - Jedi spawn: base texture already has the correct Jedi robe. Render no additive robe.
         * - Sith spawn: render ONLY sith_robes_alt over the base model.
         *
         * Sith-original species: Human, Sith, Mirialan, Miraluka, Chiss, Cerean.
         * - Sith spawn: base texture already has the correct Sith robe. Render no additive robe.
         * - Jedi spawn: render ONLY jedi_robes_alt over the base model.
         */
        if (side.isNeutral()) {
            if (jediOriginalSpecies) {
                renderRobe("jedi_robes", NEUTRAL_ROBES, poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, entity.isGhost() ? 0.48F : 1.0F);
            } else {
                renderRobe("sith_robes", NEUTRAL_ROBES_ALT, poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, entity.isGhost() ? 0.48F : 1.0F);
            }
            return;
        }

        if (side.isDark()) {
            if (jediOriginalSpecies) {
                renderRobe("sith_robes_alt", SITH_ROBES_ALT, poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, entity.isGhost() ? 0.48F : 1.0F);
            }
            return;
        }

        if (!jediOriginalSpecies) {
            renderRobe("jedi_robes_alt", JEDI_ROBES_ALT, poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, entity.isGhost() ? 0.48F : 1.0F);
        }
    }

    private void renderRobe(String modelId, ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ForceUserEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float alpha) {
        EntityModel<ForceUserEntity> model = robes.get(modelId);
        if (model == null) {
            return;
        }
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer consumer = buffer.getBuffer(entity.isGhost() ? RenderType.entityTranslucent(texture) : RenderType.entityCutoutNoCull(texture));
        int light = entity.isGhost() ? 15728640 : packedLight;
        float red = entity.isGhost() ? 0.44F : 1.0F;
        float green = entity.isGhost() ? 0.67F : 1.0F;
        float blue = entity.isGhost() ? 1.0F : 1.0F;
        model.renderToBuffer(poseStack, consumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), red, green, blue, alpha);
    }

    private static ResourceLocation robeTexture(String id) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/force_user/" + id + ".png");
    }
}
