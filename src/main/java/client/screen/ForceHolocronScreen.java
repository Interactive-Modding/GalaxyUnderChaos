package client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.force.ForceHolocronLogic;
import server.galaxyunderchaos.force.ForceNetworking;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.force.HolocronPowerActionPacket;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.menu.ForceHolocronMenu;

import java.util.ArrayList;
import java.util.List;

public class ForceHolocronScreen extends AbstractContainerScreen<ForceHolocronMenu> {
    private static final ResourceLocation ICONS = new ResourceLocation(galaxyunderchaos.MODID, "textures/gui/icons.png");
    private static final ResourceLocation ALT_FONT = new ResourceLocation("minecraft", "alt");

    private static final int NODE_SIZE = 18;
    private static final int ICON_SIZE = 16;

    private static final int MAP_BASE_X = 44;
    private static final int MAP_BASE_Y = 34;
    private static final int MAP_STEP_X = 54;
    private static final int MAP_STEP_Y = 40;

    private static final int VIEW_X = 14;
    private static final int VIEW_Y = 42;
    private static final int VIEW_W = 300;
    private static final int VIEW_H = 152;

    private static final int FOOTER_Y = 203;

    private List<Component> tooltip;

    private int mapMinX;
    private int mapMinY;
    private int mapMaxX;
    private int mapMaxY;
    private int maxScrollX;
    private int maxScrollY;
    private int scrollX;
    private int scrollY;

    private boolean panning;
    private double lastPanMouseX;
    private double lastPanMouseY;

    public ForceHolocronScreen(ForceHolocronMenu menu, net.minecraft.world.entity.player.Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 328;
        this.imageHeight = 244;
        this.inventoryLabelY = 10000;
        this.titleLabelX = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void init() {
        super.init();
        recalcMapBounds();
        centerMap();
    }

    private void recalcMapBounds() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (ForcePower power : ForceHolocronLogic.getDisplayPowers(menu.getSide())) {
            int x = nodeMapX(power);
            int y = nodeMapY(power);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x + NODE_SIZE);
            maxY = Math.max(maxY, y + NODE_SIZE);
        }

        if (minX == Integer.MAX_VALUE) {
            minX = 0;
            minY = 0;
            maxX = VIEW_W;
            maxY = VIEW_H;
        }

        this.mapMinX = minX - 32;
        this.mapMinY = minY - 28;
        this.mapMaxX = maxX + 32;
        this.mapMaxY = maxY + 32;
        this.maxScrollX = Math.max(0, (mapMaxX - mapMinX) - VIEW_W);
        this.maxScrollY = Math.max(0, (mapMaxY - mapMinY) - VIEW_H);
        this.scrollX = clamp(this.scrollX, 0, this.maxScrollX);
        this.scrollY = clamp(this.scrollY, 0, this.maxScrollY);
    }

    private void centerMap() {
        this.scrollX = this.maxScrollX / 2;
        this.scrollY = this.maxScrollY / 2;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int aura = sideColor(menu.getSide());
        int accent = accentColor(menu.getSide());
        drawFrame(graphics, aura, accent, partialTick);
        drawHeader(graphics, aura, accent);
        drawMapWindow(graphics, mouseX, mouseY, aura, accent, partialTick);
        drawFooter(graphics, aura, accent);
    }

    private void drawFrame(GuiGraphics graphics, int aura, int accent, float partialTick) {
        int x = this.leftPos;
        int y = this.topPos;

        graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xF0070810);
        graphics.fill(x + 2, y + 2, x + this.imageWidth - 2, y + this.imageHeight - 2, 0xF0121724);
        graphics.fill(x + 6, y + 6, x + this.imageWidth - 6, y + this.imageHeight - 6, panelColor(menu.getSide()));

        int glow = 0x77000000 | (aura & 0x00FFFFFF);
        int accentGlow = 0x55000000 | (accent & 0x00FFFFFF);
        graphics.fill(x, y, x + this.imageWidth, y + 3, glow);
        graphics.fill(x, y + this.imageHeight - 3, x + this.imageWidth, y + this.imageHeight, glow);
        graphics.fill(x, y, x + 3, y + this.imageHeight, glow);
        graphics.fill(x + this.imageWidth - 3, y, x + this.imageWidth, y + this.imageHeight, glow);

        graphics.fill(x + 10, y + 10, x + this.imageWidth - 10, y + 34, 0x8C05070E);
        graphics.fill(x + 12, y + 38, x + this.imageWidth - 12, y + 198, 0xAA060911);
        graphics.fill(x + 10, y + FOOTER_Y - 4, x + this.imageWidth - 10, y + this.imageHeight - 10, 0x9005070E);

        int midX = x + this.imageWidth / 2;
        int tick = this.minecraft != null && this.minecraft.player != null ? this.minecraft.player.tickCount : 0;
        int pulse = 10 + (int)(Math.sin((tick + partialTick) * 0.09F) * 6.0F);
        graphics.fill(midX - 110, y + 30, midX + 110, y + 31, accentGlow);
        graphics.fill(midX - pulse, y + 28, midX + pulse, y + 33, 0x33000000 | (accent & 0x00FFFFFF));

        for (int i = 0; i < 4; i++) {
            int barX = x + 18 + i * 6;
            graphics.fill(barX, y + 20, barX + 2, y + 29, accentGlow);
            int rightBarX = x + this.imageWidth - 20 - i * 6;
            graphics.fill(rightBarX, y + 20, rightBarX + 2, y + 29, accentGlow);
        }
    }

    private void drawHeader(GuiGraphics graphics, int aura, int accent) {
        int centerX = this.leftPos + this.imageWidth / 2;
        Component titleText = Component.literal(switch (menu.getSide()) {
            case DARK -> "SITH HOLOCRON";
            case LIGHT -> "JEDI HOLOCRON";
            case NEUTRAL -> "ANCIENT HOLOCRON";
            default -> "FORCE HOLOCRON";
        }).withStyle(style -> style.withFont(ALT_FONT).withBold(true));
        graphics.drawCenteredString(this.font, titleText, centerX, this.topPos + 14, aura);

        Component subtitle = Component.literal(menu.getSide() == ForceSide.NEUTRAL
                ? "Ancient path: unlock Jedi, Sith, and Neutral powers | 6 datacrons each"
                : "Focused path: spread branches by dragging the archive | 3 datacrons each")
                .withStyle(style -> style.withColor(0xD7D7DF));
        graphics.drawCenteredString(this.font, subtitle, centerX, this.topPos + 28, 0xD7D7DF);

        graphics.drawString(this.font,
                Component.literal("Drag to move    Scroll to pan    Shift + Scroll for sideways")
                        .withStyle(style -> style.withColor(accent)),
                this.leftPos + 18,
                this.topPos + 204,
                accent,
                false);
    }

    private void drawMapWindow(GuiGraphics graphics, int mouseX, int mouseY, int aura, int accent, float partialTick) {
        int x1 = this.leftPos + VIEW_X;
        int y1 = this.topPos + VIEW_Y;
        int x2 = x1 + VIEW_W;
        int y2 = y1 + VIEW_H;

        graphics.fill(x1 - 2, y1 - 2, x2 + 2, y2 + 2, 0xFF1D2432);
        graphics.fill(x1 - 1, y1 - 1, x2 + 1, y2 + 1, 0xBB000000 | (accent & 0x00FFFFFF));
        graphics.fill(x1, y1, x2, y2, windowBackColor(menu.getSide()));

        for (int gx = x1; gx < x2; gx += 24) {
            graphics.fill(gx, y1, gx + 1, y2, 0x1AFFFFFF);
        }
        for (int gy = y1; gy < y2; gy += 24) {
            graphics.fill(x1, gy, x2, gy + 1, 0x18000000 | (accent & 0x00FFFFFF));
        }

        graphics.enableScissor(x1, y1, x2, y2);
        drawConnections(graphics, aura);
        drawPowerNodes(graphics, mouseX, mouseY, aura, accent, partialTick);
        graphics.disableScissor();

        drawScrollBars(graphics, x1, y1, x2, y2, accent);
        drawMinimapHint(graphics, x2 - 74, y1 + 8, accent);
    }

    private void drawConnections(GuiGraphics graphics, int aura) {
        int line = 0x66000000 | (aura & 0x00FFFFFF);
        int nodeGlow = 0x34000000 | (aura & 0x00FFFFFF);

        for (ForcePower power : ForceHolocronLogic.getDisplayPowers(menu.getSide())) {
            if (power.parent() == null || !ForceHolocronLogic.isAllowed(menu.getSide(), power.parent())) {
                continue;
            }
            int x1 = nodeX(power.parent()) + NODE_SIZE / 2;
            int y1 = nodeY(power.parent()) + NODE_SIZE / 2;
            int x2 = nodeX(power) + NODE_SIZE / 2;
            int y2 = nodeY(power) + NODE_SIZE / 2;

            graphics.fill(Math.min(x1, x2), y1 - 1, Math.max(x1, x2) + 1, y1 + 1, nodeGlow);
            graphics.fill(x2 - 1, Math.min(y1, y2), x2 + 1, Math.max(y1, y2) + 1, nodeGlow);
            graphics.fill(Math.min(x1, x2), y1, Math.max(x1, x2) + 1, y1 + 1, line);
            graphics.fill(x2, Math.min(y1, y2), x2 + 1, Math.max(y1, y2) + 1, line);
        }
    }

    private void drawPowerNodes(GuiGraphics graphics, int mouseX, int mouseY, int aura, int accent, float partialTick) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }

        this.minecraft.player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            for (ForcePower power : ForceHolocronLogic.getDisplayPowers(menu.getSide())) {
                int x = nodeX(power);
                int y = nodeY(power);
                boolean unlocked = cap.hasPower(power);
                boolean prereq = ForceHolocronLogic.hasPrerequisites(menu.getSide(), cap, power);
                boolean affordable = ForceHolocronLogic.hasDatacronsForUnlock(menu.getSide(), cap, power);
                boolean available = prereq && affordable;
                boolean hovered = isInside(mouseX, mouseY, x, y, NODE_SIZE, NODE_SIZE);
                boolean selected = cap.getSelectedPower() == power;

                int bg = unlocked ? (0xD0000000 | (aura & 0x00555555)) : available ? 0xD0513C18 : prereq ? 0xD02A2020 : 0xC9141920;
                int border = unlocked ? aura : available ? accent : prereq ? 0xFF8F5A5A : 0xFF4E5763;
                int innerBorder = unlocked ? 0xFFEFEFF7 : 0xFF05070E;
                if (hovered) {
                    border = 0xFFFFFFFF;
                }

                graphics.fill(x - 4, y - 4, x + NODE_SIZE + 4, y + NODE_SIZE + 4, 0x80000000);
                graphics.fill(x - 3, y - 3, x + NODE_SIZE + 3, y + NODE_SIZE + 3, border);
                graphics.fill(x - 2, y - 2, x + NODE_SIZE + 2, y + NODE_SIZE + 2, innerBorder);
                graphics.fill(x - 1, y - 1, x + NODE_SIZE + 1, y + NODE_SIZE + 1, 0xFF070A12);
                graphics.fill(x, y, x + NODE_SIZE, y + NODE_SIZE, bg);

                if (selected) {
                    int pulse = 155 + (int)(Math.sin((minecraft.player.tickCount + partialTick) * 0.18F) * 60.0F);
                    int selectedColor = (255 << 24) | (pulse << 16) | (220 << 8) | 80;
                    graphics.fill(x - 6, y - 6, x + NODE_SIZE + 6, y - 5, selectedColor);
                    graphics.fill(x - 6, y + NODE_SIZE + 5, x + NODE_SIZE + 6, y + NODE_SIZE + 6, selectedColor);
                    graphics.fill(x - 6, y - 5, x - 5, y + NODE_SIZE + 5, selectedColor);
                    graphics.fill(x + NODE_SIZE + 5, y - 5, x + NODE_SIZE + 6, y + NODE_SIZE + 5, selectedColor);
                }

                RenderSystem.enableBlend();
                float alpha = unlocked || available ? 1.0F : prereq ? 0.60F : 0.28F;
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
                graphics.blit(ICONS, x + 1, y + 1, power.iconX() * 16, power.iconY() * 16, ICON_SIZE, ICON_SIZE);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();

                if (hovered) {
                    graphics.drawCenteredString(this.font,
                            Component.literal(shortLabel(power)).withStyle(style -> style.withColor(0xFFF1F2FF)),
                            x + NODE_SIZE / 2,
                            y - 12,
                            0xFFF1F2FF);
                }
            }
        });
    }

    private void drawScrollBars(GuiGraphics graphics, int x1, int y1, int x2, int y2, int accent) {
        if (this.maxScrollX > 0) {
            graphics.fill(x1 + 10, y2 - 6, x2 - 10, y2 - 4, 0x66000000);
            int trackWidth = (x2 - 10) - (x1 + 10);
            int knobWidth = Math.max(24, (int) (trackWidth * (VIEW_W / (float) ((mapMaxX - mapMinX)))));
            int knobRange = trackWidth - knobWidth;
            int knobX = x1 + 10 + (this.maxScrollX == 0 ? 0 : (int) (knobRange * (this.scrollX / (float) this.maxScrollX)));
            graphics.fill(knobX, y2 - 7, knobX + knobWidth, y2 - 3, 0xCC000000 | (accent & 0x00FFFFFF));
        }

        if (this.maxScrollY > 0) {
            graphics.fill(x2 - 6, y1 + 10, x2 - 4, y2 - 10, 0x66000000);
            int trackHeight = (y2 - 10) - (y1 + 10);
            int knobHeight = Math.max(24, (int) (trackHeight * (VIEW_H / (float) ((mapMaxY - mapMinY)))));
            int knobRange = trackHeight - knobHeight;
            int knobY = y1 + 10 + (this.maxScrollY == 0 ? 0 : (int) (knobRange * (this.scrollY / (float) this.maxScrollY)));
            graphics.fill(x2 - 7, knobY, x2 - 3, knobY + knobHeight, 0xCC000000 | (accent & 0x00FFFFFF));
        }
    }

    private void drawMinimapHint(GuiGraphics graphics, int x, int y, int accent) {
        graphics.fill(x, y, x + 62, y + 28, 0x8A05070E);
        graphics.fill(x + 1, y + 1, x + 61, y + 27, 0x40000000 | (accent & 0x00FFFFFF));
        graphics.drawString(this.font, Component.literal("ARCHIVE"), x + 6, y + 4, accent, false);
        graphics.drawString(this.font, Component.literal((scrollX + VIEW_W / 2) + " , " + (scrollY + VIEW_H / 2)), x + 6, y + 15, 0xFFD7D7E0, false);
    }

    private void drawFooter(GuiGraphics graphics, int aura, int accent) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }

        this.minecraft.player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            ForceSide bank = ForceHolocronLogic.getDatacronBank(menu.getSide());
            int stored = cap.getDatacrons(bank);
            int unlocked = 0;
            int total = 0;
            for (ForcePower power : ForceHolocronLogic.getDisplayPowers(menu.getSide())) {
                if (power.isSelectable()) {
                    total++;
                    if (cap.hasPower(power)) {
                        unlocked++;
                    }
                }
            }

            Component topLine = Component.literal(bankLabel(bank) + " datacrons stored: " + stored)
                    .withStyle(style -> style.withFont(ALT_FONT).withBold(true));
            Component bottomLine = Component.literal("Unlocked powers: " + unlocked + " / " + total + "    |    Click unlocked active powers again to select them.");
            graphics.drawString(this.font, topLine, this.leftPos + 16, this.topPos + 219, aura, false);
            graphics.drawString(this.font, bottomLine, this.leftPos + 16, this.topPos + 231, 0xD8D8E3, false);

            int pillLeft = this.leftPos + this.imageWidth - 110;
            int pillTop = this.topPos + 216;
            graphics.fill(pillLeft, pillTop, pillLeft + 94, pillTop + 16, 0x7A05070E);
            graphics.fill(pillLeft + 1, pillTop + 1, pillLeft + 93, pillTop + 15, 0x66000000 | (accent & 0x00FFFFFF));
            graphics.drawCenteredString(this.font,
                    Component.literal(panning ? "MOVING ARCHIVE" : "READY")
                            .withStyle(style -> style.withColor(0xFFF2F2FA)),
                    pillLeft + 47,
                    pillTop + 4,
                    0xFFF2F2FA);
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isInsideMapWindow(mouseX, mouseY)) {
            ForcePower clicked = getHoveredPower((int) mouseX, (int) mouseY);
            if (clicked != null && this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                    boolean unlocked = cap.hasPower(clicked);
                    boolean selectOnly = unlocked && clicked.isSelectable();
                    if (!unlocked || clicked.isSelectable()) {
                        ForceNetworking.sendToServer(new HolocronPowerActionPacket(clicked.id(), selectOnly));
                    }
                });
                return true;
            }

            this.panning = true;
            this.lastPanMouseX = mouseX;
            this.lastPanMouseY = mouseY;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 && this.panning) {
            int dx = (int) Math.round(mouseX - this.lastPanMouseX);
            int dy = (int) Math.round(mouseY - this.lastPanMouseY);
            this.scrollX = clamp(this.scrollX - dx, 0, this.maxScrollX);
            this.scrollY = clamp(this.scrollY - dy, 0, this.maxScrollY);
            this.lastPanMouseX = mouseX;
            this.lastPanMouseY = mouseY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && this.panning) {
            this.panning = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (isInsideMapWindow(mouseX, mouseY)) {
            if (Screen.hasShiftDown()) {
                this.scrollX = clamp(this.scrollX + (int) (-delta * 24.0D), 0, this.maxScrollX);
            } else {
                this.scrollY = clamp(this.scrollY + (int) (-delta * 24.0D), 0, this.maxScrollY);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }

        this.minecraft.player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            ForcePower hovered = getHoveredPower(mouseX, mouseY);
            if (hovered != null) {
                List<Component> lines = new ArrayList<>();
                lines.add(Component.literal(hovered.displayName()).withStyle(style -> style.withFont(ALT_FONT).withBold(true)));
                lines.add(Component.literal(sideLine(hovered.side()) + " • " + typeLine(hovered)).withStyle(style -> style.withColor(0xFFD9D9E6)));

                if (cap.hasPower(hovered)) {
                    lines.add(Component.literal(hovered.isSelectable() ? "Unlocked — click again to select." : "Unlocked branch knowledge."));
                } else if (!ForceHolocronLogic.hasPrerequisites(menu.getSide(), cap, hovered)) {
                    lines.add(Component.literal(ForceHolocronLogic.requiresCompletedStudentTraining(hovered)
                            ? "Requires fully training a Padawan or apprentice first."
                            : "Requires earlier knowledge from its branch."));
                } else {
                    int cost = ForceHolocronLogic.getDatacronCost(menu.getSide(), hovered);
                    if (cost <= 0) {
                        lines.add(Component.literal("Click to unlock branch knowledge."));
                    } else {
                        ForceSide bank = ForceHolocronLogic.getDatacronBank(menu.getSide());
                        lines.add(Component.literal("Cost: " + cost + " " + bankLabel(bank) + " datacrons"));
                        lines.add(Component.literal("Stored: " + cap.getDatacrons(bank)));
                        lines.add(Component.literal(cap.getDatacrons(bank) >= cost ? "Ready to unlock." : "More datacrons required."));
                    }
                }

                if (hovered.useCost() > 0) {
                    lines.add(Component.literal("Force cost: " + (int) hovered.useCost()));
                }
                if (menu.getSide() == ForceSide.NEUTRAL) {
                    lines.add(Component.literal("Ancient holocrons can master Light and Dark traditions."));
                }
                this.tooltip = lines;
            }
        });
    }

    private ForcePower getHoveredPower(int mouseX, int mouseY) {
        if (!isInsideMapWindow(mouseX, mouseY)) {
            return null;
        }
        for (ForcePower power : ForceHolocronLogic.getDisplayPowers(menu.getSide())) {
            int x = nodeX(power);
            int y = nodeY(power);
            if (isInside(mouseX, mouseY, x, y, NODE_SIZE, NODE_SIZE)) {
                return power;
            }
        }
        return null;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        tooltip = null;
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        if (tooltip != null) {
            graphics.renderTooltip(this.font, tooltip, ItemStack.EMPTY.getTooltipImage(), mouseX, mouseY);
        }
    }

    private int nodeX(ForcePower power) {
        return this.leftPos + VIEW_X + (nodeMapX(power) - mapMinX) - scrollX;
    }

    private int nodeY(ForcePower power) {
        return this.topPos + VIEW_Y + (nodeMapY(power) - mapMinY) - scrollY;
    }

    private static int nodeMapX(ForcePower power) {
        return MAP_BASE_X + treeDepth(power) * MAP_STEP_X;
    }

    private static int nodeMapY(ForcePower power) {
        return MAP_BASE_Y + treeRow(power) * MAP_STEP_Y;
    }

    private static int treeDepth(ForcePower power) {
        int depth = 0;
        ForcePower cursor = power;
        while (cursor != null && cursor.parent() != null && depth < 12) {
            depth++;
            cursor = cursor.parent();
        }
        return depth;
    }

    private static int treeRow(ForcePower power) {
        return switch (power) {
            case FORCE_SENSITIVITY, FORCE_LEVEL1, FORCE_LEVEL2, FORCE_LEVEL3, FORCE_LEVEL4, FORCE_LEVEL5 -> 0;
            case LIGHT_SIDE -> 1;
            case HEAL1, HEAL2, HEAL3 -> 2;
            case FORTIFY1, FORTIFY2, FORTIFY3 -> 3;
            case STUN1, STUN2, STUN3 -> 4;
            case DARK_SIDE -> 5;
            case DRAIN1, DRAIN2, DRAIN3 -> 6;
            case LIGHTNING1, LIGHTNING2, LIGHTNING3 -> 7;
            case WOUND1, WOUND2, WOUND3 -> 8;
            case NEUTRAL -> 9;
            case STEALTH -> 10;
            case SPEED -> 11;
            case REBOUND -> 12;
            case SIGHT1, SIGHT2, SIGHT3 -> 13;
            case MEDITATION1, MEDITATION2, MEDITATION3 -> 14;
            case THROW1, THROW2 -> 15;
            case RESIST1, RESIST2, RESIST3 -> 16;
            case PUSH1, PUSH2, PUSH3 -> 17;
        };
    }

    private boolean isInsideMapWindow(double mouseX, double mouseY) {
        return isInside(mouseX, mouseY, this.leftPos + VIEW_X, this.topPos + VIEW_Y, VIEW_W, VIEW_H);
    }

    private static boolean isInside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static String shortLabel(ForcePower power) {
        String name = power.displayName();
        if (name.length() <= 14) {
            return name;
        }
        return name.replace("Force ", "");
    }

    private static String bankLabel(ForceSide bank) {
        return switch (bank) {
            case LIGHT -> "Jedi";
            case DARK -> "Sith";
            case NEUTRAL -> "Ancient";
            default -> "Force";
        };
    }

    private static String sideLine(ForceSide side) {
        return switch (side) {
            case LIGHT -> "Light Side";
            case DARK -> "Dark Side";
            case NEUTRAL -> "Neutral";
            case UNIVERSAL -> "Universal";
            default -> "Force";
        };
    }

    private static String typeLine(ForcePower power) {
        return switch (power.type()) {
            case BUFF -> "Sustained";
            case PASSIVE -> "Passive";
            case PER_USE -> "Active";
            case META -> "Knowledge";
            default -> "Force";
        };
    }

    private static int sideColor(ForceSide side) {
        return switch (side) {
            case DARK -> 0xFFE74343;
            case LIGHT -> 0xFF63D6FF;
            case NEUTRAL -> 0xFFD4A6FF;
            default -> 0xFFFFFFFF;
        };
    }

    private static int accentColor(ForceSide side) {
        return switch (side) {
            case DARK -> 0xFFFFB14A;
            case LIGHT -> 0xFFFFE181;
            case NEUTRAL -> 0xFFF5C8FF;
            default -> 0xFFE0E0E0;
        };
    }

    private static int panelColor(ForceSide side) {
        return switch (side) {
            case DARK -> 0xE1140C12;
            case LIGHT -> 0xE10A1320;
            case NEUTRAL -> 0xE1121020;
            default -> 0xE1101010;
        };
    }

    private static int windowBackColor(ForceSide side) {
        return switch (side) {
            case DARK -> 0xD6181014;
            case LIGHT -> 0xD6101620;
            case NEUTRAL -> 0xD6151220;
            default -> 0xD6101010;
        };
    }
}
