package client.screen;

import client.renderer.forceuser.PlayerForceIdentityClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import server.galaxyunderchaos.force.ForceNetworking;
import server.galaxyunderchaos.force.ForceUserIdentityUpdatePacket;
import server.galaxyunderchaos.force.ForceUserInteractionActionPacket;
import server.galaxyunderchaos.force.ForceUserInteractionScreenPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/** Button-driven Force-user companion/mentor interaction screen. */
public class ForceUserInteractionScreen extends Screen {
    private static final int IDENTITY_PREVIEW_WIDTH = 158;
    private static final int IDENTITY_PREVIEW_MARGIN = 18;

    private static final SpeciesChoice[] SPECIES_CHOICES = {
            new SpeciesChoice("Miraluka M", "miraluka_male"),
            new SpeciesChoice("Miraluka F", "miraluka_female"),
            new SpeciesChoice("Mirialan M", "mirialan_male"),
            new SpeciesChoice("Mirialan F", "mirialan_female"),
            new SpeciesChoice("Twi'lek M", "twilek_male"),
            new SpeciesChoice("Twi'lek F", "twilek_female"),
            new SpeciesChoice("Togruta M", "togruta_male"),
            new SpeciesChoice("Togruta F", "togruta_female"),
            new SpeciesChoice("Zabrak M", "zabrak_male"),
            new SpeciesChoice("Zabrak F", "zabrak_female"),
            new SpeciesChoice("Rodian M", "rodian_male"),
            new SpeciesChoice("Rodian F", "rodian_female"),
            new SpeciesChoice("Chiss M", "chiss_male"),
            new SpeciesChoice("Chiss F", "chiss_female"),
            new SpeciesChoice("Cerean M", "cerean_male"),
            new SpeciesChoice("Cerean F", "cerean_female"),
            new SpeciesChoice("Sith M", "sith_male"),
            new SpeciesChoice("Sith F", "sith_female")
    };

    private static final RobeChoice[] PLAYER_ROBE_CHOICES = {
            new RobeChoice("Jedi", "jedi_robes"),
            new RobeChoice("Jedi Alt", "jedi_robes_alt"),
            new RobeChoice("Sith", "sith_robes"),
            new RobeChoice("Sith Alt", "sith_robes_alt"),
            new RobeChoice("Neutral", "neutral_robes"),
            new RobeChoice("Neutral Alt", "neutral_robes_alt")
    };

    private static final RobeChoice[] ALIEN_LOOK_CHOICES = {
            new RobeChoice("Jedi", "jedi_robes"),
            new RobeChoice("Sith", "sith_robes"),
            new RobeChoice("Neutral", "neutral_robes")
    };

    private final ForceUserInteractionScreenPacket data;
    private final Side lockedRobeSide;
    private final boolean identityLocked;
    private final List<Component> wrappedLines = new ArrayList<>();
    private EditBox identityNameBox;
    private String selectedSpeciesId;
    private String selectedRobeId;
    private String selectedTextureId;
    private String selectedEyeId;
    private int skinRed;
    private int skinGreen;
    private int skinBlue;
    private int identityPage;

    public ForceUserInteractionScreen(ForceUserInteractionScreenPacket data) {
        super(Component.literal(data.title()));
        this.data = data;
        this.lockedRobeSide = parseForceSide(extractLineValue("Current force side:", data.subtitle()));
        this.identityLocked = Boolean.parseBoolean(extractLineValue("Identity locked:", "false"));
        this.selectedSpeciesId = normalizeSpecies(extractLineValue("Current species id:", extractLineValue("Current species:", "mirialan_male")));
        this.selectedRobeId = normalizeRobeForSelection(this.selectedSpeciesId, lockRobeForAllegiance(extractLineValue("Current robe id:", extractLineValue("Current robe:", defaultRobeForAllegiance(this.lockedRobeSide))), this.lockedRobeSide));
        this.selectedTextureId = normalizeTextureForSpecies(this.selectedSpeciesId, extractLineValue("Current texture id:", defaultTextureForSpecies(this.selectedSpeciesId)));
        this.selectedEyeId = normalizeEyeForSpecies(this.selectedSpeciesId, extractLineValue("Current eye id:", "blue"));
        int skin = parseSkinColor(extractLineValue("Current skin hex:", "C68642"));
        this.skinRed = (skin >> 16) & 0xFF;
        this.skinGreen = (skin >> 8) & 0xFF;
        this.skinBlue = skin & 0xFF;
    }

    public static void open(ForceUserInteractionScreenPacket data) {
        Minecraft.getInstance().setScreen(new ForceUserInteractionScreen(data));
    }

    @Override
    protected void init() {
        String currentName = this.identityNameBox == null ? extractLineValue("Current name:", "") : this.identityNameBox.getValue();
        super.init();
        this.wrappedLines.clear();
        for (String line : data.lines()) {
            if (!isHiddenMetaLine(line)) {
                this.wrappedLines.add(Component.literal(line));
            }
        }

        int panelW = panelWidth();
        int x = (this.width - panelW) / 2;
        int panelH = panelHeight();
        int y = Math.max(20, (this.height - panelH) / 2);
        int buttonY = y + panelH - 64;
        int bw = 108;
        int gap = 8;

        switch (data.mode()) {
            case "main" -> {
                addRenderableWidget(Button.builder(Component.literal("Interact"), b -> send("interact", 0)).bounds(x + 18, buttonY, 112, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Order"), b -> send("order", 0)).bounds(x + panelW / 2 - 56, buttonY, 112, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Identity"), b -> send("identity", 0)).bounds(x + panelW - 130, buttonY, 112, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Close"), b -> this.onClose()).bounds(x + panelW / 2 - 45, buttonY + 28, 90, 20).build());
            }
            case "interact" -> {
                addRenderableWidget(Button.builder(Component.literal("Conversation"), b -> send("conversation", 0)).bounds(x + 16, buttonY, bw, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Quests"), b -> send("quests", 0)).bounds(x + 16 + bw + gap, buttonY, bw, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Identity"), b -> send("identity", 0)).bounds(x + 16 + (bw + gap) * 2, buttonY, bw, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Back"), b -> send("main", 0)).bounds(x + panelW / 2 - 45, buttonY + 28, 90, 20).build());
            }
            case "order" -> {
                addRenderableWidget(Button.builder(Component.literal("Stay"), b -> send("stay", 0)).bounds(x + 16, buttonY, bw, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Wander"), b -> send("wander", 0)).bounds(x + 16 + bw + gap, buttonY, bw, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Follow & Defend"), b -> send("follow", 0)).bounds(x + 16 + (bw + gap) * 2, buttonY, bw + 24, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Back"), b -> send("main", 0)).bounds(x + panelW / 2 - 45, buttonY + 28, 90, 20).build());
            }
            case "conversation" -> {
                int cy = buttonY - 8;
                for (int i = 0; i < Math.min(3, data.choiceTexts().size()); i++) {
                    String label = (i + 1) + ". " + trim(data.choiceTexts().get(i), 62);
                    int choice = i + 1;
                    addRenderableWidget(Button.builder(Component.literal(label), b -> send("choice", choice)).bounds(x + 16, cy, panelW - 32, 20).build());
                    cy += 23;
                }
                addRenderableWidget(Button.builder(Component.literal("Back"), b -> send("interact", 0)).bounds(x + panelW / 2 - 45, cy + 3, 90, 20).build());
            }
            case "quests" -> {
                addRenderableWidget(Button.builder(Component.literal("Refresh"), b -> send("quests", 0)).bounds(x + 42, buttonY + 28, 110, 20).build());
                addRenderableWidget(Button.builder(Component.literal("Back"), b -> send("interact", 0)).bounds(x + panelW - 142, buttonY + 28, 100, 20).build());
            }
            case "identity" -> initIdentityWidgets(x, y, panelW, buttonY, currentName);
            default -> addRenderableWidget(Button.builder(Component.literal("Close"), b -> this.onClose()).bounds(x + panelW / 2 - 45, buttonY, 90, 20).build());
        }
    }

    private void initIdentityWidgets(int x, int y, int panelW, int buttonY, String currentName) {
        if (this.identityLocked && this.identityPage != 1) {
            this.identityPage = 1;
        }
        int controlW = identityControlWidth(panelW);
        this.identityNameBox = new EditBox(this.font, x + 24, y + 82, controlW - 48, 20, Component.literal("Force name"));
        this.identityNameBox.setMaxLength(24);
        this.identityNameBox.setValue(currentName);
        addRenderableWidget(this.identityNameBox);
        setInitialFocus(this.identityNameBox);

        int pageY = y + 108;
        addPageButton(x + 24, pageY, "Species", 0);
        addPageButton(x + 126, pageY, "Robes", 1);
        addPageButton(x + 228, pageY, "Details", 2);

        switch (this.identityPage) {
            case 1 -> initRobePage(x, y, controlW);
            case 2 -> initDetailsPage(x, y, controlW);
            default -> initSpeciesPage(x, y);
        }

        addRenderableWidget(Button.builder(Component.literal(this.identityLocked ? "Save Name/Robes" : "Save Identity"), b -> saveIdentity()).bounds(x + 48, buttonY + 28, 130, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Back"), b -> send("main", 0)).bounds(x + Math.min(controlW - 148, panelW - 148), buttonY + 28, 100, 20).build());
    }

    private void addPageButton(int x, int y, String label, int page) {
        Button button = Button.builder(Component.literal(label), b -> rebuildIdentityPage(page)).bounds(x, y, 92, 18).build();
        button.active = this.identityPage != page && (!this.identityLocked || page == 1);
        addRenderableWidget(button);
    }

    private void rebuildIdentityPage(int page) {
        this.identityPage = page;
        this.clearWidgets();
        this.init();
    }

    private void initSpeciesPage(int x, int y) {
        if (this.identityLocked) {
            return;
        }
        int sx = x + 24;
        int sy = y + 140;
        int sw = 78;
        int sh = 18;
        int gap = 6;
        for (int i = 0; i < SPECIES_CHOICES.length; i++) {
            SpeciesChoice choice = SPECIES_CHOICES[i];
            int col = i % 5;
            int row = i / 5;
            addRenderableWidget(Button.builder(Component.literal(choice.label()), b -> {
                        this.selectedSpeciesId = choice.speciesId();
                        this.selectedRobeId = normalizeRobeForSelection(this.selectedSpeciesId, lockRobeForAllegiance(this.selectedRobeId, this.lockedRobeSide));
                        this.selectedTextureId = normalizeTextureForSpecies(this.selectedSpeciesId, this.selectedTextureId);
                        this.selectedEyeId = normalizeEyeForSpecies(this.selectedSpeciesId, this.selectedEyeId);
                    })
                    .bounds(sx + col * (sw + gap), sy + row * 23, sw, sh).build());
        }
        addRenderableWidget(Button.builder(Component.literal("Human Body"), b -> {
                    this.selectedSpeciesId = "player";
                    this.selectedRobeId = lockRobeForAllegiance(this.selectedRobeId, this.lockedRobeSide);
                    this.selectedTextureId = "";
                })
                .bounds(sx, sy + 4 * 23, 110, sh).build());
    }

    private void initRobePage(int x, int y, int panelW) {
        int sx = x + 24;
        int sy = y + 148;
        int sh = 20;
        int gap = 8;

        String lockedRobe = defaultRobeForAllegiance(this.lockedRobeSide);
        this.selectedRobeId = normalizeRobeForSelection(this.selectedSpeciesId, lockRobeForAllegiance(this.selectedRobeId, this.lockedRobeSide));
        Button lockedAlien = Button.builder(Component.literal("Species Robes: " + sideLabel(this.lockedRobeSide)), b ->
                        this.selectedRobeId = normalizeRobeForSelection(this.selectedSpeciesId, lockedRobe))
                .bounds(sx, sy, Math.min(panelW - 48, 220), sh).build();
        lockedAlien.active = !"player".equals(normalizeSpecies(this.selectedSpeciesId));
        addRenderableWidget(lockedAlien);

        if (this.identityLocked && !"player".equals(normalizeSpecies(this.selectedSpeciesId))) {
            return;
        }

        int playerY = sy + 42;
        int robeW = 132;
        RobeChoice[] playerChoices = playerRobeChoicesForSide(this.lockedRobeSide);
        for (int i = 0; i < playerChoices.length; i++) {
            RobeChoice choice = playerChoices[i];
            addRenderableWidget(Button.builder(Component.literal(choice.label()), b -> {
                        this.selectedSpeciesId = "player";
                        this.selectedTextureId = "";
                        this.selectedRobeId = choice.robeId();
                    })
                    .bounds(sx + i * (robeW + gap), playerY, robeW, sh).build());
        }
    }

    private void initDetailsPage(int x, int y, int panelW) {
        int sx = x + 24;
        int sy = y + 148;

        String[] variants = textureVariants(this.selectedSpeciesId);
        OptionSlider variantSlider = new OptionSlider(sx, sy, panelW - 48, 20, "Variant", variants,
                normalizeTextureForSpecies(this.selectedSpeciesId, this.selectedTextureId), true, value -> this.selectedTextureId = value);
        variantSlider.active = variants.length > 1;
        addRenderableWidget(variantSlider);

        int eyeY = sy + 34;
        String[] eyes = eyeOptions(this.selectedSpeciesId);
        OptionSlider eyeSlider = new OptionSlider(sx, eyeY, panelW - 48, 20, "Eyes", eyes,
                normalizeEyeForSpecies(this.selectedSpeciesId, this.selectedEyeId), false, value -> this.selectedEyeId = value);
        eyeSlider.active = eyes.length > 1;
        addRenderableWidget(eyeSlider);

        int sliderY = eyeY + 54;
        addRenderableWidget(new ColorSlider(sx, sliderY, panelW - 48, 18, "R", this.skinRed, value -> this.skinRed = value));
        addRenderableWidget(new ColorSlider(sx, sliderY + 24, panelW - 48, 18, "G", this.skinGreen, value -> this.skinGreen = value));
        addRenderableWidget(new ColorSlider(sx, sliderY + 48, panelW - 48, 18, "B", this.skinBlue, value -> this.skinBlue = value));
    }

    private void cycleTexture(int delta) {
        String[] variants = textureVariants(this.selectedSpeciesId);
        if (variants.length == 0) {
            this.selectedTextureId = "";
            return;
        }
        int index = indexOf(variants, normalizeTextureForSpecies(this.selectedSpeciesId, this.selectedTextureId));
        index = Math.floorMod(index + delta, variants.length);
        this.selectedTextureId = variants[index];
    }

    private void cycleEye(int delta) {
        String[] eyes = eyeOptions(this.selectedSpeciesId);
        if (eyes.length == 0) {
            this.selectedEyeId = "blue";
            return;
        }
        int index = indexOf(eyes, normalizeEye(this.selectedEyeId));
        index = Math.floorMod(index + delta, eyes.length);
        this.selectedEyeId = eyes[index];
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int panelW = panelWidth();
        int panelH = panelHeight();
        int x = (this.width - panelW) / 2;
        int y = Math.max(20, (this.height - panelH) / 2);

        graphics.fill(x, y, x + panelW, y + panelH, 0xF0101320);
        graphics.fill(x + 2, y + 2, x + panelW - 2, y + panelH - 2, 0xF0182030);
        graphics.fill(x + 6, y + 6, x + panelW - 6, y + 34, 0xCC050812);

        int accent = accentColor(data.subtitle());
        graphics.fill(x, y, x + panelW, y + 2, accent);
        graphics.fill(x, y + panelH - 2, x + panelW, y + panelH, accent);
        graphics.drawCenteredString(this.font, data.title(), x + panelW / 2, y + 11, 0xFFEDEEFF);
        graphics.drawCenteredString(this.font, data.subtitle(), x + panelW / 2, y + 24, 0xFFC9CED8);

        if ("quests".equals(data.mode())) {
            graphics.fill(x + 14, y + 42, x + panelW - 14, y + 178, 0xAA050812);
            drawQuestLines(graphics, x + 24, y + 50, panelW - 48, 78);
            drawQuestProgressBar(graphics, x + 24, y + 138, panelW - 48, 16);
        } else if ("identity".equals(data.mode())) {
            int controlW = identityControlWidth(panelW);
            graphics.fill(x + 14, y + 42, x + panelW - 14, y + panelH - 76, 0xAA050812);
            drawIdentityPreview(graphics, x, y, panelW, panelH, mouseX, mouseY);
            drawLines(graphics, x + 24, y + 48, controlW - 48, 22, 2);
            graphics.drawString(this.font, "Name", x + 24, y + 72, 0xFFD8DDE8, false);
            graphics.drawString(this.font, trim("Selected: " + speciesLabel(this.selectedSpeciesId) + " / " + lookLabel(this.selectedSpeciesId, this.selectedRobeId), controlW / 6), x + 24, y + 132, 0xFFD8DDE8, false);
            if (this.identityPage == 2) {
                graphics.drawString(this.font, "Skin RGB: #" + skinHex(), x + 24, y + 220, 0xFFE7E8EF, false);
            }
        } else {
            graphics.fill(x + 10, y + 40, x + panelW - 10, y + 150, 0xAA050812);
            drawLines(graphics, x + 16, y + 46, panelW - 32, 100, Integer.MAX_VALUE);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawQuestLines(GuiGraphics graphics, int x, int y, int width, int maxHeight) {
        int yy = y;
        for (Component line : wrappedLines) {
            String raw = line.getString();
            if (!(raw.startsWith("Quest:") || raw.startsWith("What to do:") || raw.startsWith("Where to go:") || raw.startsWith("Status:") || raw.startsWith("Training:"))) {
                continue;
            }
            graphics.drawWordWrap(this.font, line, x, yy, width, 0xFFE7E8EF);
            yy += 12 * Math.max(1, this.font.split(line, width).size());
            if (yy > y + maxHeight) {
                graphics.drawString(this.font, "...", x, y + maxHeight - 8, 0xFFE7E8EF, false);
                return;
            }
        }
    }

    private void drawQuestProgressBar(GuiGraphics graphics, int x, int y, int width, int height) {
        int value = extractIntLine("ProgressValue:", 0);
        int max = Math.max(1, extractIntLine("ProgressMax:", 1));
        int fill = Math.max(0, Math.min(width - 2, Math.round((width - 2) * (value / (float) max))));
        graphics.drawString(this.font, "Progress", x, y - 11, 0xFFD8DDE8, false);
        graphics.fill(x, y, x + width, y + height, 0xFF111827);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF202838);
        graphics.fill(x + 1, y + 1, x + 1 + fill, y + height - 1, accentColor(data.subtitle()));
        String label = extractLineValue("ProgressLabel:", value + "/" + max);
        graphics.drawCenteredString(this.font, label, x + width / 2, y + 4, 0xFFFFFFFF);
    }

    private void drawLines(GuiGraphics graphics, int x, int y, int width, int maxHeight, int maxLines) {
        int yy = y;
        int drawn = 0;
        for (Component line : wrappedLines) {
            if (drawn >= maxLines) {
                return;
            }
            int before = yy;
            graphics.drawWordWrap(this.font, line, x, yy, width, 0xFFE7E8EF);
            yy += 12 * Math.max(1, this.font.split(line, width).size());
            drawn++;
            if (yy == before) {
                yy += 12;
            }
            if (yy > y + maxHeight) {
                graphics.drawString(this.font, "...", x, y + maxHeight - 8, 0xFFE7E8EF, false);
                return;
            }
        }
    }

    private void send(String action, int value) {
        ForceNetworking.sendToServer(new ForceUserInteractionActionPacket(data.entityId(), action, value));
    }

    private void saveIdentity() {
        String name = this.identityNameBox == null ? extractLineValue("Current name:", "") : this.identityNameBox.getValue();
        ForceNetworking.sendToServer(new ForceUserIdentityUpdatePacket(data.entityId(), name, previewIdentityId()));
    }

    private String previewIdentityId() {
        String species = normalizeSpecies(this.selectedSpeciesId);
        String robe = normalizeRobeForSelection(species, lockRobeForAllegiance(this.selectedRobeId, this.lockedRobeSide));
        String texture = normalizeTextureForSpecies(species, this.selectedTextureId);
        String eye = normalizeEyeForSpecies(species, this.selectedEyeId);
        return species + "|" + robe + "|" + texture + "|" + eye + "|" + skinHex();
    }

    private void drawIdentityPreview(GuiGraphics graphics, int x, int y, int panelW, int panelH, int mouseX, int mouseY) {
        if (!hasIdentityPreview(panelW) || this.minecraft == null || this.minecraft.player == null) {
            return;
        }

        int previewX = x + panelW - IDENTITY_PREVIEW_WIDTH - IDENTITY_PREVIEW_MARGIN;
        int previewY = y + 52;
        int previewRight = x + panelW - IDENTITY_PREVIEW_MARGIN;
        int previewBottom = y + panelH - 88;

        graphics.fill(previewX - 8, y + 46, previewX - 7, y + panelH - 82, 0x66D8DDE8);
        graphics.fill(previewX, previewY, previewRight, previewBottom, 0x77111824);
        graphics.drawCenteredString(this.font, "Preview", previewX + IDENTITY_PREVIEW_WIDTH / 2, previewY + 8, 0xFFD8DDE8);

        AbstractClientPlayer player = this.minecraft.player;
        PlayerForceIdentityClientState.Entry previous = PlayerForceIdentityClientState.putTemporaryPreview(
                player.getUUID(),
                new PlayerForceIdentityClientState.Entry(
                        this.identityNameBox == null ? extractLineValue("Current name:", "") : this.identityNameBox.getValue(),
                        "",
                        PlayerForceIdentityClientState.normalizeIdentity(previewIdentityId()),
                        "",
                        false
                )
        );
        try {
            int entityX = previewX + IDENTITY_PREVIEW_WIDTH / 2;
            int entityY = previewBottom - 12;
            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, entityX, entityY, 46, entityX - mouseX, entityY - 92 - mouseY, player);
        } finally {
            PlayerForceIdentityClientState.restoreTemporaryPreview(player.getUUID(), previous);
        }
    }

    private int panelWidth() {
        return Math.min("identity".equals(data.mode()) ? 660 : 380, this.width - 32);
    }

    private int identityControlWidth(int panelW) {
        return hasIdentityPreview(panelW) ? panelW - IDENTITY_PREVIEW_WIDTH - (IDENTITY_PREVIEW_MARGIN * 2) : panelW;
    }

    private boolean hasIdentityPreview(int panelW) {
        return "identity".equals(data.mode()) && panelW >= 600;
    }

    private int panelHeight() {
        return switch (data.mode()) {
            case "conversation" -> 258;
            case "identity" -> 392;
            default -> 238;
        };
    }

    private boolean isHiddenMetaLine(String line) {
        return line.startsWith("ProgressValue:") || line.startsWith("ProgressMax:") || line.startsWith("ProgressLabel:")
                || line.startsWith("Current species id:") || line.startsWith("Current robe id:")
                || line.startsWith("Current texture id:") || line.startsWith("Current eye id:") || line.startsWith("Current skin hex:")
                || line.startsWith("Current force side:") || line.startsWith("Identity locked:");
    }

    private String extractLineValue(String prefix, String fallback) {
        for (String line : data.lines()) {
            if (line.startsWith(prefix)) {
                return line.substring(prefix.length()).trim();
            }
        }
        return fallback;
    }

    private int extractIntLine(String prefix, int fallback) {
        String value = extractLineValue(prefix, Integer.toString(fallback));
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static int accentColor(String subtitle) {
        String s = subtitle.toLowerCase(Locale.ROOT);
        if (s.contains("sith") || s.contains("dark")) {
            return 0xFF7A1C24;
        }
        if (s.contains("neutral") || s.contains("white")) {
            return 0xFFB8BEC8;
        }
        return 0xFF386CA8;
    }

    private static String trim(String value, int length) {
        if (value.length() <= length) {
            return value;
        }
        return value.substring(0, Math.max(0, length - 3)) + "...";
    }

    private static String normalizeSpecies(String speciesId) {
        if (speciesId == null || speciesId.isBlank()) {
            return "mirialan_male";
        }
        String s = speciesId.toLowerCase(Locale.ROOT).trim();
        if (s.contains("|")) {
            s = s.split("\\|", 2)[0];
        }
        if (s.contains(":")) {
            s = s.split(":", 2)[0];
        }
        return switch (s) {
            case "player" -> "player";
            case "cerean_male", "cerean_female", "chiss_male", "chiss_female",
                 "miraluka_male", "miraluka_female", "mirialan_male", "mirialan_female",
                 "rodian_male", "rodian_female", "sith_male", "sith_female",
                 "togruta_male", "togruta_female", "twilek_male", "twilek_female",
                 "zabrak_male", "zabrak_female" -> s;
            case "human_male", "human_female", "human_old_male", "human_old_female" -> "player";
            default -> "mirialan_male";
        };
    }

    private static String normalizeRobe(String robeId) {
        if (robeId == null || robeId.isBlank()) {
            return "jedi_robes";
        }
        String s = robeId.toLowerCase(Locale.ROOT).trim();
        if (s.contains("|")) {
            String[] parts = s.split("\\|", 2);
            s = parts.length > 1 && !parts[1].isBlank() ? parts[1] : "jedi_robes";
        }
        if (s.contains(":")) {
            String[] parts = s.split(":", 2);
            s = parts.length > 1 && !parts[1].isBlank() ? parts[1] : "jedi_robes";
        }
        return switch (s) {
            case "jedi_robes", "jedi_robes_alt", "sith_robes", "sith_robes_alt", "neutral_robes", "neutral_robes_alt" -> s;
            default -> "jedi_robes";
        };
    }

    private static RobeChoice[] playerRobeChoicesForSide(Side side) {
        return switch (side) {
            case DARK -> new RobeChoice[] { new RobeChoice("Sith", "sith_robes"), new RobeChoice("Sith Alt", "sith_robes_alt") };
            case NEUTRAL -> new RobeChoice[] { new RobeChoice("Neutral", "neutral_robes"), new RobeChoice("Neutral Alt", "neutral_robes_alt") };
            default -> new RobeChoice[] { new RobeChoice("Jedi", "jedi_robes"), new RobeChoice("Jedi Alt", "jedi_robes_alt") };
        };
    }

    private static String defaultRobeForAllegiance(Side side) {
        return switch (side) {
            case DARK -> "sith_robes";
            case NEUTRAL -> "neutral_robes";
            default -> "jedi_robes";
        };
    }

    private static String lockRobeForAllegiance(String robeId, Side side) {
        String robe = normalizeRobe(robeId);
        return switch (side) {
            case DARK -> robe.startsWith("sith_") ? robe : "sith_robes";
            case NEUTRAL -> robe.startsWith("neutral_") ? robe : "neutral_robes";
            default -> robe.startsWith("jedi_") ? robe : "jedi_robes";
        };
    }

    private static Side parseForceSide(String value) {
        if (value == null) {
            return Side.LIGHT;
        }
        String lower = value.toLowerCase(Locale.ROOT);
        if (lower.contains("dark") || lower.contains("sith")) {
            return Side.DARK;
        }
        if (lower.contains("neutral") || lower.contains("force_user") || lower.contains("force-user")) {
            return Side.NEUTRAL;
        }
        return Side.LIGHT;
    }

    private static String sideLabel(Side side) {
        return switch (side) {
            case DARK -> "Sith";
            case NEUTRAL -> "Neutral";
            default -> "Jedi";
        };
    }

    private static String normalizeRobeForSelection(String speciesId, String robeId) {
        String species = normalizeSpecies(speciesId);
        String robe = normalizeRobe(robeId);
        if ("player".equals(species)) {
            return robe;
        }
        Side wanted = robeSide(robe);
        if (isLightOriginalSpecies(species)) {
            return switch (wanted) {
                case DARK -> "sith_robes_alt";
                case NEUTRAL -> "neutral_robes";
                default -> "jedi_robes";
            };
        }
        return switch (wanted) {
            case LIGHT -> "jedi_robes_alt";
            case NEUTRAL -> "neutral_robes_alt";
            default -> "sith_robes";
        };
    }

    private static boolean isLightOriginalSpecies(String speciesId) {
        return switch (normalizeSpecies(speciesId)) {
            case "rodian_male", "rodian_female", "zabrak_male", "zabrak_female",
                 "twilek_male", "twilek_female", "togruta_male", "togruta_female" -> true;
            default -> false;
        };
    }

    private static String lookLabel(String speciesId, String robeId) {
        String species = normalizeSpecies(speciesId);
        String robe = normalizeRobeForSelection(species, robeId);
        if ("player".equals(species)) {
            return robeLabel(robe);
        }
        return switch (robeSide(robe)) {
            case DARK -> "Sith";
            case NEUTRAL -> "Neutral";
            default -> "Jedi";
        };
    }

    private static String speciesLabel(String speciesId) {
        return switch (normalizeSpecies(speciesId)) {
            case "player" -> "Human Body";
            case "cerean_male" -> "Cerean Male";
            case "cerean_female" -> "Cerean Female";
            case "chiss_male" -> "Chiss Male";
            case "chiss_female" -> "Chiss Female";
            case "miraluka_male" -> "Miraluka Male";
            case "miraluka_female" -> "Miraluka Female";
            case "mirialan_male" -> "Mirialan Male";
            case "mirialan_female" -> "Mirialan Female";
            case "rodian_male" -> "Rodian Male";
            case "rodian_female" -> "Rodian Female";
            case "sith_male" -> "Sith Male";
            case "sith_female" -> "Sith Female";
            case "togruta_male" -> "Togruta Male";
            case "togruta_female" -> "Togruta Female";
            case "twilek_male" -> "Twi'lek Male";
            case "twilek_female" -> "Twi'lek Female";
            case "zabrak_male" -> "Zabrak Male";
            case "zabrak_female" -> "Zabrak Female";
            default -> "Mirialan Male";
        };
    }

    private static String robeLabel(String robeId) {
        return switch (normalizeRobe(robeId)) {
            case "jedi_robes" -> "Jedi Robes";
            case "jedi_robes_alt" -> "Jedi Robes Alt";
            case "sith_robes" -> "Sith Robes";
            case "sith_robes_alt" -> "Sith Robes Alt";
            case "neutral_robes" -> "Neutral Robes";
            case "neutral_robes_alt" -> "Neutral Robes Alt";
            default -> "Jedi Robes";
        };
    }

    private static String normalizeTextureForSpecies(String speciesId, String textureId) {
        String species = normalizeSpecies(speciesId);
        if ("player".equals(species)) {
            return "";
        }
        String texture = textureId == null ? "" : textureId.toLowerCase(Locale.ROOT).trim();
        for (String option : textureVariants(species)) {
            if (option.equals(texture)) {
                return texture;
            }
        }
        return defaultTextureForSpecies(species);
    }

    private static String[] textureVariants(String speciesId) {
        return switch (normalizeSpecies(speciesId)) {
            case "mirialan_male" -> new String[] { "mirialan_male", "mirialan_male_2", "mirialan_male_3" };
            case "mirialan_female" -> new String[] { "mirialan_female", "mirialan_female_2", "mirialan_female_3" };
            case "rodian_male" -> new String[] { "rodian_male", "rodian_male_2", "rodian_male_3" };
            case "rodian_female" -> new String[] { "rodian_female", "rodian_female_2", "rodian_female_3" };
            case "zabrak_male" -> new String[] { "zabrack_male", "zabrack_male_2", "zabrack_male_3" };
            case "zabrak_female" -> new String[] { "zabrack_female", "zabrack_female_2", "zabrack_female_3" };
            case "twilek_male" -> new String[] { "twilek_male", "twilek_male_2", "twilek_male_3" };
            case "twilek_female" -> new String[] { "twilek_female", "twilek_female_2", "twilek_female_3" };
            case "togruta_male" -> new String[] { "togruta_male", "togruta_male_2", "togruta_male_3", "togruta_male_4" };
            case "togruta_female" -> new String[] { "togruta_female", "togruta_female_2", "togruta_female_3", "togruta_female_4" };
            case "sith_male" -> new String[] { "sith_male", "sith_younger_male" };
            case "player" -> new String[0];
            default -> new String[] { defaultTextureForSpecies(speciesId) };
        };
    }

    private static String defaultTextureForSpecies(String speciesId) {
        return switch (normalizeSpecies(speciesId)) {
            case "zabrak_male" -> "zabrack_male";
            case "zabrak_female" -> "zabrack_female";
            case "sith_female" -> "sith_younger_female";
            case "player" -> "";
            default -> normalizeSpecies(speciesId);
        };
    }

    private static String normalizeEyeForSpecies(String speciesId, String eyeId) {
        return hasEyeOptions(speciesId) ? normalizeEye(eyeId) : "blue";
    }

    private static boolean hasEyeOptions(String speciesId) {
        String species = normalizeSpecies(speciesId);
        return !"player".equals(species) && !species.startsWith("chiss_") && !species.startsWith("miraluka_");
    }

    private static String[] eyeOptions(String speciesId) {
        return hasEyeOptions(speciesId)
                ? new String[] { "blue", "light_blue", "grey_blue", "green", "hazel", "brown", "dark_brown" }
                : new String[0];
    }

    private static String normalizeEye(String eyeId) {
        if (eyeId == null || eyeId.isBlank()) {
            return "blue";
        }
        return switch (eyeId.toLowerCase(Locale.ROOT).trim()) {
            case "blue", "light_blue", "grey_blue", "green", "hazel", "brown", "dark_brown" -> eyeId.toLowerCase(Locale.ROOT).trim();
            default -> "blue";
        };
    }

    private static String eyeLabel(String eyeId) {
        return switch (normalizeEye(eyeId)) {
            case "light_blue" -> "Light Blue";
            case "grey_blue" -> "Grey Blue";
            case "green" -> "Green";
            case "hazel" -> "Hazel";
            case "brown" -> "Brown";
            case "dark_brown" -> "Dark Brown";
            default -> "Blue";
        };
    }

    private static String textureLabel(String textureId) {
        if (textureId == null || textureId.isBlank()) {
            return "Default";
        }
        String cleaned = textureId.replace('_', ' ');
        String[] parts = cleaned.split(" ");
        StringBuilder out = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) continue;
            if (out.length() > 0) out.append(' ');
            out.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return out.length() == 0 ? "Default" : out.toString();
    }

    private static int parseSkinColor(String value) {
        String hex = normalizeSkinHex(value);
        try {
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException ignored) {
            return 0xC68642;
        }
    }

    private static String normalizeSkinHex(String value) {
        String hex = value == null ? "" : value.trim().toUpperCase(Locale.ROOT).replace("#", "");
        if (!hex.matches("[0-9A-F]{6}")) {
            return "C68642";
        }
        return hex;
    }

    private String skinHex() {
        return String.format(Locale.ROOT, "%02X%02X%02X", clampColor(this.skinRed), clampColor(this.skinGreen), clampColor(this.skinBlue));
    }

    private static int clampColor(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private static int indexOf(String[] values, String value) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private enum Side {
        LIGHT,
        DARK,
        NEUTRAL
    }

    private static Side robeSide(String robeId) {
        String robe = normalizeRobe(robeId);
        if (robe.startsWith("sith_")) {
            return Side.DARK;
        }
        if (robe.startsWith("neutral_")) {
            return Side.NEUTRAL;
        }
        return Side.LIGHT;
    }

    private static final class OptionSlider extends AbstractSliderButton {
        private final String label;
        private final String[] options;
        private final boolean textureLabels;
        private final Consumer<String> setter;

        private OptionSlider(int x, int y, int width, int height, String label, String[] rawOptions, String selectedValue,
                             boolean textureLabels, Consumer<String> setter) {
            super(x, y, width, height, Component.empty(), startValue(rawOptions, selectedValue));
            this.label = label;
            this.options = rawOptions == null || rawOptions.length == 0 ? new String[] { "" } : rawOptions;
            this.textureLabels = textureLabels;
            this.setter = setter;
            updateMessage();
            applyValue();
        }

        @Override
        protected void updateMessage() {
            String value = currentOption();
            String shown = this.textureLabels ? textureLabel(value) : eyeLabel(value);
            if (value.isBlank() && this.textureLabels) {
                shown = "Default";
            }
            if (this.options.length <= 1 && !this.textureLabels) {
                shown = "Species Default";
            }
            setMessage(Component.literal(this.label + ": " + shown));
        }

        @Override
        protected void applyValue() {
            this.setter.accept(currentOption());
        }

        private String currentOption() {
            int index = this.options.length <= 1 ? 0 : Math.max(0, Math.min(this.options.length - 1, (int) Math.round(this.value * (this.options.length - 1))));
            return this.options[index];
        }

        private static double startValue(String[] rawOptions, String selectedValue) {
            if (rawOptions == null || rawOptions.length <= 1) {
                return 0.0D;
            }
            int index = indexOf(rawOptions, selectedValue);
            return index / (double) (rawOptions.length - 1);
        }
    }

    private static final class ColorSlider extends AbstractSliderButton {
        private final String label;
        private final IntConsumer setter;

        private ColorSlider(int x, int y, int width, int height, String label, int startValue, IntConsumer setter) {
            super(x, y, width, height, Component.empty(), clampColor(startValue) / 255.0D);
            this.label = label;
            this.setter = setter;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal(this.label + ": " + currentValue()));
        }

        @Override
        protected void applyValue() {
            this.setter.accept(currentValue());
        }

        private int currentValue() {
            return clampColor((int) Math.round(this.value * 255.0D));
        }
    }

    private record SpeciesChoice(String label, String speciesId) {}
    private record RobeChoice(String label, String robeId) {}
}
