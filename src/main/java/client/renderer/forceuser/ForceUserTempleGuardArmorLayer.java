package client.renderer.forceuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.galaxyunderchaos;

/**
 * Renders the existing Temple Guard armor items with a Force-user-sized armor model.
 * Vanilla HumanoidModel armor is intentionally not used because it is scaled and posed
 * for PlayerModel, while ForceUserEntity uses custom Blockbench parts.
 */
public class ForceUserTempleGuardArmorLayer extends RenderLayer<ForceUserEntity, ForceUserLayeredModel> {
    private static final ResourceLocation TEMPLE_LAYER_1 = new ResourceLocation(galaxyunderchaos.MODID, "textures/models/armor/temple_guard_layer_1.png");
    private static final ResourceLocation TEMPLE_LAYER_2 = new ResourceLocation(galaxyunderchaos.MODID, "textures/models/armor/temple_guard_layer_2.png");
    private static final ResourceLocation SITH_LAYER_1 = new ResourceLocation(galaxyunderchaos.MODID, "textures/models/armor/sith_guard_layer_1.png");
    private static final ResourceLocation SITH_LAYER_2 = new ResourceLocation(galaxyunderchaos.MODID, "textures/models/armor/sith_guard_layer_2.png");

    private final ForceUserTempleGuardArmorModel outerArmor;
    private final ForceUserTempleGuardArmorModel innerArmor;

    public ForceUserTempleGuardArmorLayer(RenderLayerParent<ForceUserEntity, ForceUserLayeredModel> parent,
                                          net.minecraft.client.renderer.entity.EntityRendererProvider.Context context) {
        super(parent);
        this.outerArmor = new ForceUserTempleGuardArmorModel(ForceUserTempleGuardArmorModel.createArmorLayer(0.62F).bakeRoot());
        this.innerArmor = new ForceUserTempleGuardArmorModel(ForceUserTempleGuardArmorModel.createArmorLayer(0.32F).bakeRoot());
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ForceUserEntity entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        if (entity.getType() != galaxyunderchaos.JEDI_TEMPLE_GUARD.get()
                && entity.getType() != galaxyunderchaos.SITH_GUARD.get()) {
            return;
        }
        renderSlot(poseStack, buffer, packedLight, entity, EquipmentSlot.HEAD, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        renderSlot(poseStack, buffer, packedLight, entity, EquipmentSlot.CHEST, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        renderSlot(poseStack, buffer, packedLight, entity, EquipmentSlot.LEGS, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        renderSlot(poseStack, buffer, packedLight, entity, EquipmentSlot.FEET, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void renderSlot(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ForceUserEntity entity,
                            EquipmentSlot slot, float limbSwing, float limbSwingAmount, float ageInTicks,
                            float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (stack.isEmpty()) {
            return;
        }

        ForceUserTempleGuardArmorModel model = slot == EquipmentSlot.LEGS ? innerArmor : outerArmor;
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.setVisibleForSlot(slot);

        ResourceLocation texture = resolveTexture(entity, slot);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        model.renderToBuffer(poseStack, consumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }
    private ResourceLocation resolveTexture(ForceUserEntity entity, EquipmentSlot slot) {
        boolean legs = slot == EquipmentSlot.LEGS;
        if (entity.getType() == galaxyunderchaos.SITH_GUARD.get()) {
            return legs ? SITH_LAYER_2 : SITH_LAYER_1;
        }
        return legs ? TEMPLE_LAYER_2 : TEMPLE_LAYER_1;
    }
}
