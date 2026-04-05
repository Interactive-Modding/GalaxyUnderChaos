package client.renderer;

import net.minecraftforge.eventbus.api.Event;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

public class CustomRenderGuiOverlayEvent extends Event {
    private final GuiGraphics guiGraphics;
    private final float partialTick;
    private final int screenWidth;
    private final int screenHeight;

    public CustomRenderGuiOverlayEvent(GuiGraphics guiGraphics, float partialTick) {
        this.guiGraphics = guiGraphics;
        this.partialTick = partialTick;
        this.screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        this.screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
    }

    public GuiGraphics getGuiGraphics() {
        return guiGraphics;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
