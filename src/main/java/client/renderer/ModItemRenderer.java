package client.renderer;

import client.renderer.lightsaber.LegacyLightsaberBladeRenderer;
import client.renderer.lightsaber.legacy.LegacyJavaLightsaberModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.DoubleLightsaberItem;
import server.galaxyunderchaos.item.HiltItem;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.item.LightsaberPartItem;
import server.galaxyunderchaos.lightsaber.AdvancedLightsaberLegacyHilts;
import server.galaxyunderchaos.lightsaber.DoubleLightsaberData;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;
import server.galaxyunderchaos.lightsaber.ModularLightsaberData;

import java.util.function.Consumer;

public class ModItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final float PIXEL = 1.0F / 16.0F;
    private static final float BLADE_LENGTH = 1.55F;
    private static final float DOUBLE_BLADE_LENGTH = 1.45F;
    private static final float CROSSGUARD_BLADE_LENGTH = 0.38F;

    public ModItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack,
                             ItemDisplayContext displayContext,
                             PoseStack poseStack,
                             MultiBufferSource buffer,
                             int light,
                             int overlay) {
        if (stack.getItem() instanceof LightsaberPartItem partItem) {
            renderPartItem(partItem, displayContext, poseStack, buffer, light, overlay);
            return;
        }

        if (stack.getItem() instanceof HiltItem hiltItem) {
            renderFullHilt(stack, hiltItem.getHiltId(), false, hiltItem.getDefaultBladeColor(),
                    displayContext, poseStack, buffer, light, overlay);
            return;
        }

        if (stack.getItem() instanceof DoubleLightsaberItem doubleItem) {
            renderDoubleLightsaber(stack, doubleItem, displayContext, poseStack, buffer, light, overlay);
            return;
        }

        if (stack.getItem() instanceof LightsaberItem lightsaberItem) {
            renderFullHilt(stack, lightsaberItem.getHiltId(stack), lightsaberItem.isActive(stack),
                    LightsaberItem.getBladeColor(stack), displayContext, poseStack, buffer, light, overlay);
        }
    }

    private ResourceLocation resolveLightsaberTexture(String familyId, LightsaberPartType type) {
        Minecraft mc = Minecraft.getInstance();
        ResourceLocation preferred = ModularLightsaberData.getPreferredPartTexture(familyId, type);
        if (mc.getResourceManager().getResource(preferred).isPresent()) {
            return preferred;
        }

        ResourceLocation family = ModularLightsaberData.getFamilyTexture(familyId);
        if (mc.getResourceManager().getResource(family).isPresent()) {
            return family;
        }

        return preferred;
    }

    private void renderPartItem(LightsaberPartItem partItem,
                                ItemDisplayContext displayContext,
                                PoseStack poseStack,
                                MultiBufferSource buffer,
                                int light,
                                int overlay) {
        float heightPixels = ModularLightsaberData.getLegacyHeight(partItem.getFamilyId(), partItem.getPartType());
        float displayScale = 0.32F;

        poseStack.pushPose();
        switch (displayContext) {
            case FIRST_PERSON_RIGHT_HAND -> {
                poseStack.translate(1.0F, 0.2F, 0.0F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            }
            case FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.0F, 0.1F, 0.0F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            }
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5F, 0.5F, 0.55F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                displayScale = 0.15F;
            }
            case GROUND -> {
                poseStack.translate(0.2F, 0.35F, 0.2F);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                displayScale = 0.20F;
            }
            case GUI -> {
                poseStack.translate(0.45F, 0.45F, 0.30F);
                poseStack.mulPose(Axis.YP.rotationDegrees(25.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(25.0F));
                displayScale = (partItem.getPartType() == LightsaberPartType.POMMEL && heightPixels <= 4.0F) ? 0.42F : 0.32F;
                if (heightPixels * displayScale > 15.5F) {
                    displayScale = 15.5F / heightPixels;
                }
            }
            case FIXED -> {
                poseStack.mulPose(Axis.ZN.rotationDegrees(-45.0F));
                poseStack.translate(0.7F, 0.0F, 0.5F);
            }
            default -> displayScale = 0.15F;
        }

        if (displayScale != 1.0F) {
            poseStack.scale(displayScale, displayScale, displayScale);
        }

        float offset = heightPixels * (isLowerPart(partItem.getPartType()) ? -1.0F : 1.0F) * 0.5F * PIXEL;
        if (displayContext == ItemDisplayContext.GUI) {
            offset *= 0.75F;
        }
        poseStack.translate(0.0F, offset, 0.0F);

        boolean rendered = LegacyJavaLightsaberModels.renderPart(
                partItem.getFamilyId(),
                partItem.getPartType(),
                resolveLightsaberTexture(partItem.getFamilyId(), partItem.getPartType()),
                poseStack,
                buffer,
                light,
                overlay
        );

        if (!rendered) {
            renderSegmentBox(
                    poseStack,
                    buffer,
                    resolveLightsaberTexture(partItem.getFamilyId(), partItem.getPartType()),
                    0.0F,
                    heightPixels * PIXEL,
                    getHalfWidth(partItem.getPartType()),
                    getHalfDepth(partItem.getPartType()),
                    light,
                    overlay
            );
        }
        poseStack.popPose();
    }

    private boolean isLowerPart(LightsaberPartType partType) {
        return partType == LightsaberPartType.GRIP || partType == LightsaberPartType.POMMEL;
    }

    private void renderDoubleLightsaber(ItemStack stack,
                                        DoubleLightsaberItem doubleItem,
                                        ItemDisplayContext displayContext,
                                        PoseStack poseStack,
                                        MultiBufferSource buffer,
                                        int light,
                                        int overlay) {
        poseStack.pushPose();
        applyAssembledSaberDisplayTransform(displayContext, poseStack);

        boolean active = doubleItem.isActive(stack);
        renderDoubleEnd(stack, true, active, poseStack, buffer, light, overlay);
        renderDoubleEnd(stack, false, active, poseStack, buffer, light, overlay);

        poseStack.popPose();
    }

    private void renderDoubleEnd(ItemStack stack,
                                 boolean upper,
                                 boolean active,
                                 PoseStack poseStack,
                                 MultiBufferSource buffer,
                                 int light,
                                 int overlay) {
        ItemStack endStack = DoubleLightsaberData.createRenderStack(stack, upper, active);
        String fallbackHiltId = DoubleLightsaberData.getPrimaryHiltId(stack, upper, "graflex");
        String bladeColor = DoubleLightsaberData.getBladeColor(stack, upper, "white");
        float totalHeightPx = DoubleLightsaberData.getTotalHeight(stack, upper, fallbackHiltId);

        poseStack.pushPose();
        if (!upper) {
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
        poseStack.translate(0.0F, -(totalHeightPx * PIXEL) / 2.0F, 0.0F);
        renderAssembledSaberCore(endStack, fallbackHiltId, active, bladeColor, poseStack, buffer, light, overlay, false, DOUBLE_BLADE_LENGTH);
        poseStack.popPose();
    }

    private void renderFullHilt(ItemStack stack,
                                String fallbackHiltId,
                                boolean active,
                                String bladeColor,
                                ItemDisplayContext displayContext,
                                PoseStack poseStack,
                                MultiBufferSource buffer,
                                int light,
                                int overlay) {
        poseStack.pushPose();
        applyAssembledSaberDisplayTransform(displayContext, poseStack);
        renderAssembledSaberCore(stack, fallbackHiltId, active, bladeColor, poseStack, buffer, light, overlay, true, BLADE_LENGTH);
        poseStack.popPose();
    }

    private void applyAssembledSaberDisplayTransform(ItemDisplayContext displayContext, PoseStack poseStack) {
        float displayScale = 0.32F;

        switch (displayContext) {
            case FIRST_PERSON_RIGHT_HAND -> {
                poseStack.translate(1.0F, 0.0F, 0.0F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            }
            case FIRST_PERSON_LEFT_HAND -> poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.5F, 0.4F, 0.55F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                displayScale = 0.17F;
            }
            case GROUND -> {
                poseStack.translate(0.2F, 0.35F, 0.2F);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                displayScale = 0.17F;
            }
            case GUI -> {
                poseStack.translate(0.45F, 0.45F, 0.30F);
                poseStack.mulPose(Axis.YP.rotationDegrees(25.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(25.0F));
            }
            case FIXED -> {
                poseStack.mulPose(Axis.ZN.rotationDegrees(-45.0F));
                poseStack.translate(0.7F, 0.0F, 0.5F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            }
            default -> displayScale = 0.15F;
        }

        if (displayScale != 1.0F) {
            poseStack.scale(displayScale, displayScale, displayScale);
        }
    }

    private void renderAssembledSaberCore(ItemStack stack,
                                          String fallbackHiltId,
                                          boolean active,
                                          String bladeColor,
                                          PoseStack poseStack,
                                          MultiBufferSource buffer,
                                          int light,
                                          int overlay,
                                          boolean allowIntrinsicSecondBlade,
                                          float bladeLength) {
        String emitterFamily = ModularLightsaberData.getPartFamily(stack, LightsaberPartType.EMITTER, fallbackHiltId);
        String switchFamily = ModularLightsaberData.getPartFamily(stack, LightsaberPartType.SWITCH_SECTION, fallbackHiltId);
        String gripFamily = ModularLightsaberData.getPartFamily(stack, LightsaberPartType.GRIP, fallbackHiltId);
        String pommelFamily = ModularLightsaberData.getPartFamily(stack, LightsaberPartType.POMMEL, fallbackHiltId);

        float emitterHeightPx = ModularLightsaberData.getLegacyHeight(emitterFamily, LightsaberPartType.EMITTER);
        float switchHeightPx = ModularLightsaberData.getLegacyHeight(switchFamily, LightsaberPartType.SWITCH_SECTION);
        float gripHeightPx = ModularLightsaberData.getLegacyHeight(gripFamily, LightsaberPartType.GRIP);
        float pommelHeightPx = ModularLightsaberData.getLegacyHeight(pommelFamily, LightsaberPartType.POMMEL);
        float totalHeightPx = emitterHeightPx + switchHeightPx + gripHeightPx + pommelHeightPx;

        poseStack.translate(0.0F, -((gripHeightPx + pommelHeightPx) - (totalHeightPx / 2.0F)) * PIXEL, 0.0F);

        boolean renderedAnything = false;

        poseStack.pushPose();
        poseStack.translate(0.0F, -(switchHeightPx * PIXEL), 0.0F);
        renderedAnything |= LegacyJavaLightsaberModels.renderPart(
                emitterFamily,
                LightsaberPartType.EMITTER,
                resolveLightsaberTexture(emitterFamily, LightsaberPartType.EMITTER),
                poseStack,
                buffer,
                light,
                overlay
        );
        poseStack.popPose();

        renderedAnything |= LegacyJavaLightsaberModels.renderPart(
                switchFamily,
                LightsaberPartType.SWITCH_SECTION,
                resolveLightsaberTexture(switchFamily, LightsaberPartType.SWITCH_SECTION),
                poseStack,
                buffer,
                light,
                overlay
        );

        renderedAnything |= LegacyJavaLightsaberModels.renderPart(
                gripFamily,
                LightsaberPartType.GRIP,
                resolveLightsaberTexture(gripFamily, LightsaberPartType.GRIP),
                poseStack,
                buffer,
                light,
                overlay
        );

        poseStack.pushPose();
        float[] pommelOps = ModularLightsaberData.getPommelAlignmentOps(gripFamily);
        if (pommelOps.length > 0) {
            for (int i = 0; i < pommelOps.length; ++i) {
                float value = pommelOps[i];
                if ((i & 1) == 0) {
                    poseStack.translate(0.0F, value * PIXEL, 0.0F);
                } else {
                    poseStack.mulPose(Axis.XP.rotationDegrees(value));
                }
            }
        } else {
            poseStack.translate(0.0F, gripHeightPx * PIXEL, 0.0F);
        }

        renderedAnything |= LegacyJavaLightsaberModels.renderPart(
                pommelFamily,
                LightsaberPartType.POMMEL,
                resolveLightsaberTexture(pommelFamily, LightsaberPartType.POMMEL),
                poseStack,
                buffer,
                light,
                overlay
        );
        poseStack.popPose();

        if (!renderedAnything) {
            renderFallbackAssembledHilt(
                    emitterFamily,
                    switchFamily,
                    gripFamily,
                    pommelFamily,
                    emitterHeightPx * PIXEL,
                    switchHeightPx * PIXEL,
                    gripHeightPx * PIXEL,
                    pommelHeightPx * PIXEL,
                    poseStack,
                    buffer,
                    light,
                    overlay
            );
        }

        if (active) {
            LegacyLightsaberBladeRenderer.renderMainBlade(
                    poseStack,
                    buffer,
                    stack,
                    bladeColor,
                    switchHeightPx,
                    emitterHeightPx,
                    bladeLength,
                    false,
                    overlay
            );

            if (ModularLightsaberData.shouldRenderCrossguard(stack, fallbackHiltId)) {
                renderLegacyCrossguardBlades(
                        stack,
                        poseStack,
                        buffer,
                        bladeColor,
                        emitterFamily,
                        switchHeightPx,
                        emitterHeightPx,
                        overlay
                );
            }

            if (allowIntrinsicSecondBlade && ModularLightsaberData.shouldRenderSecondBlade(stack, fallbackHiltId)) {
                LegacyLightsaberBladeRenderer.renderMainBlade(
                        poseStack,
                        buffer,
                        stack,
                        bladeColor,
                        switchHeightPx,
                        emitterHeightPx,
                        bladeLength,
                        true,
                        overlay
                );
            }
        }
    }

    private void renderFallbackAssembledHilt(String emitterFamily,
                                             String switchFamily,
                                             String gripFamily,
                                             String pommelFamily,
                                             float emitterHeight,
                                             float switchHeight,
                                             float gripHeight,
                                             float pommelHeight,
                                             PoseStack poseStack,
                                             MultiBufferSource buffer,
                                             int light,
                                             int overlay) {
        float totalHeight = emitterHeight + switchHeight + gripHeight + pommelHeight;
        float cursor = -totalHeight / 2.0F;
        cursor = renderSegmentAtCursor(poseStack, buffer, emitterFamily, LightsaberPartType.EMITTER, cursor, emitterHeight, light, overlay);
        cursor = renderSegmentAtCursor(poseStack, buffer, switchFamily, LightsaberPartType.SWITCH_SECTION, cursor, switchHeight, light, overlay);
        cursor = renderSegmentAtCursor(poseStack, buffer, gripFamily, LightsaberPartType.GRIP, cursor, gripHeight, light, overlay);
        renderSegmentAtCursor(poseStack, buffer, pommelFamily, LightsaberPartType.POMMEL, cursor, pommelHeight, light, overlay);
    }

    private float renderSegmentAtCursor(PoseStack poseStack,
                                        MultiBufferSource buffer,
                                        String familyId,
                                        LightsaberPartType type,
                                        float cursor,
                                        float height,
                                        int light,
                                        int overlay) {
        float center = cursor + (height / 2.0F);
        renderSegmentBox(poseStack, buffer, resolveLightsaberTexture(familyId, type), center, height,
                getHalfWidth(type), getHalfDepth(type), light, overlay);
        return cursor + height;
    }

    private float getHalfWidth(LightsaberPartType type) {
        return switch (type) {
            case EMITTER -> 0.095F;
            case SWITCH_SECTION -> 0.11F;
            case GRIP -> 0.12F;
            case POMMEL -> 0.105F;
        };
    }

    private float getHalfDepth(LightsaberPartType type) {
        return getHalfWidth(type);
    }

    private void renderSegmentBox(PoseStack poseStack,
                                  MultiBufferSource buffer,
                                  ResourceLocation texture,
                                  float centerY,
                                  float height,
                                  float halfWidth,
                                  float halfDepth,
                                  int light,
                                  int overlay) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        emitBox(poseStack, consumer, centerY, height, halfWidth, halfDepth,
                1.0F, 1.0F, 1.0F, 1.0F, light, overlay);
    }

    private void renderLegacyCrossguardBlades(ItemStack stack,
                                              PoseStack poseStack,
                                              MultiBufferSource buffer,
                                              String bladeColor,
                                              String emitterFamily,
                                              float switchHeightPx,
                                              float emitterHeightPx,
                                              int overlay) {
        AdvancedLightsaberLegacyHilts.LegacyHiltSpec spec = ModularLightsaberData.getLegacySpec(emitterFamily);
        if (spec == null || !spec.hasCrossguard()) {
            return;
        }

        for (int i = -1; i <= 1; i += 2) {
            poseStack.pushPose();
            LegacyLightsaberBladeRenderer.applyBladeRootTransform(poseStack, switchHeightPx, emitterHeightPx);
            poseStack.translate(spec.crossguardX(), spec.crossguardY(), spec.crossguardZ() * -i);
            poseStack.mulPose(Axis.XP.rotationDegrees(i * 90.0F));

            if (i == 1) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            }

            LegacyLightsaberBladeRenderer.renderCrossguardGeometry(
                    poseStack,
                    buffer,
                    stack,
                    bladeColor,
                    CROSSGUARD_BLADE_LENGTH,
                    overlay
            );
            poseStack.popPose();
        }
    }
    private void emitBox(PoseStack poseStack,
                         VertexConsumer consumer,
                         float centerY,
                         float height,
                         float halfWidth,
                         float halfDepth,
                         float red,
                         float green,
                         float blue,
                         float alpha,
                         int light,
                         int overlay) {
        float minX = -halfWidth;
        float maxX = halfWidth;
        float minY = centerY - (height / 2.0F);
        float maxY = centerY + (height / 2.0F);
        float minZ = -halfDepth;
        float maxZ = halfDepth;
        PoseStack.Pose pose = poseStack.last();

        emitQuad(consumer, pose, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, 0, 0, 1, red, green, blue, alpha, light, overlay);
        emitQuad(consumer, pose, maxX, minY, minZ, minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, 0, 0, -1, red, green, blue, alpha, light, overlay);
        emitQuad(consumer, pose, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, -1, 0, 0, red, green, blue, alpha, light, overlay);
        emitQuad(consumer, pose, maxX, minY, maxZ, maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, 1, 0, 0, red, green, blue, alpha, light, overlay);
        emitQuad(consumer, pose, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ, 0, 1, 0, red, green, blue, alpha, light, overlay);
        emitQuad(consumer, pose, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, 0, -1, 0, red, green, blue, alpha, light, overlay);
    }

    private void emitQuad(VertexConsumer consumer,
                          PoseStack.Pose pose,
                          float x1, float y1, float z1,
                          float x2, float y2, float z2,
                          float x3, float y3, float z3,
                          float x4, float y4, float z4,
                          float normalX, float normalY, float normalZ,
                          float red, float green, float blue, float alpha,
                          int light, int overlay) {
        consumer.vertex(pose.pose(), x1, y1, z1).color(red, green, blue, alpha)
                .uv(0.0F, 1.0F).overlayCoords(overlay).uv2(light)
                .normal(pose.normal(), normalX, normalY, normalZ).endVertex();
        consumer.vertex(pose.pose(), x2, y2, z2).color(red, green, blue, alpha)
                .uv(1.0F, 1.0F).overlayCoords(overlay).uv2(light)
                .normal(pose.normal(), normalX, normalY, normalZ).endVertex();
        consumer.vertex(pose.pose(), x3, y3, z3).color(red, green, blue, alpha)
                .uv(1.0F, 0.0F).overlayCoords(overlay).uv2(light)
                .normal(pose.normal(), normalX, normalY, normalZ).endVertex();
        consumer.vertex(pose.pose(), x4, y4, z4).color(red, green, blue, alpha)
                .uv(0.0F, 0.0F).overlayCoords(overlay).uv2(light)
                .normal(pose.normal(), normalX, normalY, normalZ).endVertex();
    }

    public static void registerItemRenderer(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer customRenderer = new ModItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return customRenderer;
            }
        });
    }
}