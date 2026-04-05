//package client.renderer;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import server.galaxyunderchaos.data.ModDataComponentTypes;
//import server.galaxyunderchaos.galaxyunderchaos;
//
//public class ForcePowerOverlay {
//    private static final ResourceLocation FORCE_HUD_ICONS = new ResourceLocation(galaxyunderchaos.MODID, "textures/gui/force_icons.png");
//
//    public static void register() {
//        MinecraftForge.EVENT_BUS.register(new ForcePowerOverlay());
//    }
//
//    @SubscribeEvent
//    public void onCustomGuiRender(CustomRenderGuiOverlayEvent event) {
//        Minecraft mc = Minecraft.getInstance();
//        Player player = mc.player;
//        if (player == null || mc.options.hideGui) return;
//
//        player.getCapability(ModCapabilities.FORCE_CAPABILITY).ifPresent(force -> {
//                String power = force.getSelectedPower();
//                if (power == null || power.isEmpty()) return;
//
//                GuiGraphics graphics = event.getGuiGraphics();
//                PoseStack pose = graphics.pose();
//                int x = 10, y = 10;
//
//                pose.pushPose();
//                RenderSystem.enableBlend();
//
//                // draw the name
//                graphics.drawString(mc.font, "Power: " + power, x + 20, y + 4, 0xFFFFFF, true);
//
//                // pick an icon index (0–7) from your sprite sheet (128px wide, 16px icons)
//                int iconsPerRow = 128 / 16;
//                int idx = Math.abs(power.hashCode()) % iconsPerRow;
//                int iconX = idx * 16;
//
//                // blit the 16×16 icon
//                graphics.blit(FORCE_HUD_ICONS, x, y, iconX, 0, 16, 16, 128, 16);
//
//                RenderSystem.disableBlend();
//                pose.popPose();
//            });
//        }
//}