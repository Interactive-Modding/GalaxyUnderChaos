//package client.renderer;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.entity.ItemRenderer;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.ItemDisplayContext;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import server.galaxyunderchaos.item.LightsaberItem;
//import server.galaxyunderchaos.lightsaber.BladeColorRegistry;
//import java.awt.Color;
//
//@OnlyIn(Dist.CLIENT)
//public class LightsaberRenderer implements ItemRenderer {
//
//    @Override
//    public void render(ItemStack stack, ItemTransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
//        LightsaberItem lightsaberItem = (LightsaberItem) stack.getItem();
//        if (lightsaberItem.isActive()) {
//            // Fetch the blade color from the lightsaber item
//            String bladeColor = lightsaberItem.getBladeColor();
//            String glowColor = BladeColorRegistry.getBladeColor(bladeColor);
//
//            // Apply the glow effect based on blade color
//            applyGlowEffect(poseStack, buffer, glowColor, light);
//        }
//
//        // Continue with normal rendering of the lightsaber item
//        super.render(stack, transformType, poseStack, buffer, light, overlay);
//    }
//
//    private void applyGlowEffect(PoseStack poseStack, MultiBufferSource buffer, String glowColor, int light) {
//        // Set up the glow color based on blade color
//        float[] color = getGlowColorFromRegistry(glowColor);
//        RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0f);
//
//        // Apply the glow effect using a special render layer
//        RenderLayer.glowingRender(poseStack, buffer, light);
//    }
//
//    private float[] getGlowColorFromRegistry(String glowColor) {
//        switch (glowColor) {
//            case "red":
//                return new float[]{1.0f, 0.0f, 0.0f};
//            case "blue":
//                return new float[]{0.0f, 0.0f, 1.0f};
//            case "green":
//                return new float[]{0.0f, 1.0f, 0.0f};
//            case "yellow":
//                return new float[]{1.0f, 1.0f, 0.0f};
//            // Add other cases for different colors
//            default:
//                return new float[]{1.0f, 1.0f, 1.0f}; // Default to white glow
//        }
//    }
//}
