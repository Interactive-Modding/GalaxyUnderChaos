package client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;

public final class ForceAlignmentOverlay {
    private static int statusTicks;
    private static int entityStatusTicks;
    private static String entityName = "";
    private static String entityRank = "";
    private static String entitySide = "";
    private static String entityLeaning = "";
    private static int entityLeanValue;
    private static boolean entityMentorEligible;
    private static boolean entityStudent;
    private static boolean entityGhost;
    private static boolean entityBound;

    private ForceAlignmentOverlay() {
    }

    /**
     * Compatibility hook used by KeyInputHandler when the player presses H while not looking at a Force-user.
     */
    public static void showStatus(int ticks) {
        statusTicks = Math.max(statusTicks, ticks);
        entityStatusTicks = 0;
    }

    /**
     * Compatibility hook used by KeyInputHandler when the player presses H while looking at a Force-user.
     */
    public static void showEntityStatus(ForceUserEntity entity, int ticks) {
        if (entity == null) {
            showStatus(ticks);
            return;
        }

        entityName = entity.getDisplayName().getString();
        entityRank = entity.getRankDisplayName();
        entitySide = entity.getForceUserSide().serializedName();
        entityLeaning = entity.getAlignmentLeaningLabel();
        entityLeanValue = entity.getAlignmentLeaning();
        entityMentorEligible = entity.canUnlockForceSensitivityForPlayer();
        entityStudent = entity.getForceUserRole().isStudent();
        entityGhost = entity.getForceUserRole().isGhost();
        entityBound = entity.hasMaster();

        entityStatusTicks = Math.max(entityStatusTicks, ticks);
        statusTicks = 0;
    }

    public static void render(GuiGraphics graphics, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) {
            return;
        }

        minecraft.player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            int ticks = cap.getAlignmentFlashTicks();
            ForceSide side = cap.getAlignmentFlashSide();
            if (ticks > 0 && side != ForceSide.UNIVERSAL) {
                renderAllegianceFlash(graphics, minecraft, partialTick, ticks, side);
            }

            if (entityStatusTicks > 0) {
                entityStatusTicks--;
                renderEntityStatus(graphics, minecraft);
                return;
            }

            if (statusTicks > 0) {
                statusTicks--;
                renderPlayerStatus(graphics, minecraft,
                        cap.getLightSidePoints(),
                        cap.getDarkSidePoints(),
                        cap.getNeutralKnowledgePoints(),
                        cap.getAlignmentBalance());
            }
        });
    }

    private static void renderAllegianceFlash(GuiGraphics graphics, Minecraft minecraft, float partialTick, int ticks, ForceSide side) {
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        float life = Math.min(1.0F, (ticks + partialTick) / 120.0F);
        float pulse = 0.65F + (float)Math.sin((minecraft.player.tickCount + partialTick) * 0.35F) * 0.18F;
        int alpha = Math.max(35, Math.min(145, (int)(life * pulse * 150.0F)));
        int fill = overlayFill(side, alpha);
        int border = borderColor(side);
        int textColor = textColor(side);

        graphics.fill(0, 0, width, height, fill);
        graphics.fill(0, 0, width, 4, border);
        graphics.fill(0, height - 4, width, height, border);
        graphics.fill(0, 0, 4, height, border);
        graphics.fill(width - 4, 0, width, height, border);
        graphics.drawCenteredString(minecraft.font, Component.literal(message(side)), width / 2, height / 2 - 5, textColor);
    }

    private static void renderPlayerStatus(GuiGraphics graphics, Minecraft minecraft, int light, int dark, int neutral, int alignmentBalance) {
        int width = minecraft.getWindow().getGuiScaledWidth();
        int x = width / 2 - 92;
        int y = 28;
        int barW = 184;
        int barH = 12;

        int balance = Mth.clamp(alignmentBalance, -100, 100);
        int marker = x + barW / 2 - Math.round(balance / 100.0F * (barW / 2.0F));

        graphics.fill(x - 6, y - 20, x + barW + 6, y + 42, 0xCC05070B);
        graphics.drawCenteredString(minecraft.font, Component.literal("Force Allegiance"), x + barW / 2, y - 15, 0xFFFFFFFF);
        graphics.fill(x, y, x + barW, y + barH, 0xFF251010);
        graphics.fill(x, y, x + barW / 2, y + barH, 0xFF315D91);
        graphics.fill(x + barW / 2, y, x + barW, y + barH, 0xFF8E2424);
        graphics.fill(marker - 2, y - 3, marker + 2, y + barH + 3, 0xFFFFFFFF);
        graphics.drawString(minecraft.font, "Light " + light, x, y + 18, 0xFF9FD8FF, false);
        graphics.drawString(minecraft.font, "Dark " + dark, x + barW - minecraft.font.width("Dark " + dark), y + 18, 0xFFFF8D8D, false);
        graphics.drawCenteredString(minecraft.font, Component.literal("Neutral knowledge " + neutral), x + barW / 2, y + 30, 0xFFECECEC);
    }

    private static void renderEntityStatus(GuiGraphics graphics, Minecraft minecraft) {
        int width = minecraft.getWindow().getGuiScaledWidth();
        int x = width / 2 - 116;
        int y = 28;
        int barW = 232;
        int barH = 12;

        int lean = Mth.clamp(entityLeanValue, -100, 100);
        int marker = x + barW / 2 - Math.round(lean / 100.0F * (barW / 2.0F));

        graphics.fill(x - 8, y - 20, x + barW + 8, y + 64, 0xCC05070B);
        graphics.drawCenteredString(minecraft.font, Component.literal(entityName), x + barW / 2, y - 15, 0xFFFFFFFF);
        graphics.drawCenteredString(minecraft.font, Component.literal(entityRank + " | " + entitySide), x + barW / 2, y - 4, 0xFFECECEC);
        graphics.fill(x, y + 12, x + barW, y + 12 + barH, 0xFF251010);
        graphics.fill(x, y + 12, x + barW / 2, y + 12 + barH, 0xFF315D91);
        graphics.fill(x + barW / 2, y + 12, x + barW, y + 12 + barH, 0xFF8E2424);
        graphics.fill(marker - 2, y + 9, marker + 2, y + 12 + barH + 3, 0xFFFFFFFF);
        graphics.drawCenteredString(minecraft.font, Component.literal("Leaning: " + entityLeaning + " (" + entityLeanValue + ")"), x + barW / 2, y + 30, 0xFFFFFFFF);

        String persistence = entityGhost ? "ghost persists" : (entityBound ? "bonded/persistent" : "unbonded natural spawn");
        String flags = (entityMentorEligible ? "mentor eligible" : "not a mentor")
                + " | " + (entityStudent ? "student" : "not student")
                + " | " + persistence;
        graphics.drawCenteredString(minecraft.font, Component.literal(flags), x + barW / 2, y + 43, 0xFFECECEC);
    }

    private static int overlayFill(ForceSide side, int alpha) {
        int rgb = switch (side) {
            case DARK -> 0x8F0000;
            case LIGHT -> 0x0648A8;
            case NEUTRAL -> 0xE8ECF4;
            default -> 0x000000;
        };
        return (alpha << 24) | rgb;
    }

    private static int borderColor(ForceSide side) {
        return switch (side) {
            case DARK -> 0xAAFF0000;
            case LIGHT -> 0xAA4EA3FF;
            case NEUTRAL -> 0xAAFFFFFF;
            default -> 0xAAFFFFFF;
        };
    }

    private static int textColor(ForceSide side) {
        return switch (side) {
            case DARK -> 0xFFFF3030;
            case LIGHT -> 0xFF7DBBFF;
            case NEUTRAL -> 0xFFFFFFFF;
            default -> 0xFFFFFFFF;
        };
    }

    private static String message(ForceSide side) {
        return switch (side) {
            case DARK -> "Embracing the dark side...";
            case LIGHT -> "Embracing the light...";
            case NEUTRAL -> "Embracing the balance...";
            default -> "The Force shifts...";
        };
    }
}
