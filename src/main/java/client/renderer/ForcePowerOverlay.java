package client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.LightsaberFormProvider;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT)
public class ForcePowerOverlay {
    private static final ResourceLocation FORCE_HUD_ICONS =
            new ResourceLocation(galaxyunderchaos.MODID, "textures/gui/icons.png");

    private static final int HUD_LEFT = 24;
    private static final int HUD_BOTTOM_OFFSET = 70;
    private static final int RADIUS = 28;
    private static final int ARC_STEPS = 30;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }

        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap ->
                render(event.getGuiGraphics(), mc, player, cap));
    }

    private static void render(GuiGraphics graphics, Minecraft mc, Player player, ForceCapability cap) {
        int cx = HUD_LEFT + RADIUS;
        int cy = mc.getWindow().getGuiScaledHeight() - HUD_BOTTOM_OFFSET;

        ForcePower selectedPower = cap.getSelectedPower();
        boolean showForce = cap.getMaxForce() > 0 && (selectedPower != null || cap.getCurrentForce() > 0.0F);

        player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).ifPresent(formCap -> {
            boolean showGuard = formCap.isStaminaVisible();

            if (!showForce && !showGuard && selectedPower == null) {
                return;
            }

            if (showForce) {
                float forceRatio = Mth.clamp(cap.getCurrentForce() / (float) Math.max(1, cap.getMaxForce()), 0.0F, 1.0F);
                drawConnectedSemiRing(graphics, cx, cy, RADIUS, true, forceRatio, 0xFF36D9EA);
            }

            if (showGuard) {
                float guardRatio = Mth.clamp(formCap.getGuardStamina() / (float) Math.max(1, formCap.getMaxGuardStamina()), 0.0F, 1.0F);
                drawConnectedSemiRing(graphics, cx, cy, RADIUS, false, guardRatio, 0xFFE34B4B);
            }

            if (selectedPower != null) {
                drawCenterAbilityIcon(graphics, cx, cy, selectedPower);
            }
        });
    }

    private static void drawConnectedSemiRing(GuiGraphics graphics, int cx, int cy, int radius, boolean leftSide, float ratio, int activeColor) {
        ratio = Mth.clamp(ratio, 0.0F, 1.0F);

        float start = leftSide ? 112.0F : 68.0F;
        float end = leftSide ? 248.0F : -68.0F;

        // Neutral track first, then only the active amount. This makes depletion visible.
        drawSemiRingRange(graphics, cx, cy, radius, start, end, 0.0F, 1.0F, 0x660A0D12);

        if (ratio > 0.0F) {
            drawSemiRingRange(graphics, cx, cy, radius, start, end, 0.0F, ratio, activeColor);
        }
    }

    private static void drawSemiRingRange(GuiGraphics graphics, int cx, int cy, int radius, float startDeg, float endDeg, float fromRatio, float toRatio, int color) {
        fromRatio = Mth.clamp(fromRatio, 0.0F, 1.0F);
        toRatio = Mth.clamp(toRatio, 0.0F, 1.0F);
        if (toRatio <= fromRatio) {
            return;
        }

        int startStep = Mth.floor(ARC_STEPS * fromRatio);
        int endStep = Mth.ceil(ARC_STEPS * toRatio);

        for (int i = startStep; i <= endStep; i++) {
            float t = i / (float) ARC_STEPS;
            float deg = Mth.lerp(t, startDeg, endDeg);
            double rad = Math.toRadians(deg);

            int x = cx + Math.round((float) Math.cos(rad) * radius);
            int y = cy + Math.round((float) Math.sin(rad) * radius);

            graphics.fill(x - 2, y - 2, x + 3, y + 3, color);
        }
    }

    private static void drawCenterAbilityIcon(GuiGraphics graphics, int cx, int cy, ForcePower power) {
        int x = cx - 8;
        int y = cy - 8;

        graphics.fill(cx - 12, cy - 12, cx + 12, cy + 12, 0x62000000);
        graphics.fill(cx - 10, cy - 10, cx + 10, cy + 10, 0x99101420);
        graphics.fill(cx - 11, cy - 11, cx + 11, cy - 10, 0x6636D9EA);
        graphics.fill(cx - 11, cy + 10, cx + 11, cy + 11, 0x66E34B4B);

        graphics.blit(FORCE_HUD_ICONS, x, y, power.iconX() * 16, power.iconY() * 16, 16, 16, 256, 256);
    }
}
