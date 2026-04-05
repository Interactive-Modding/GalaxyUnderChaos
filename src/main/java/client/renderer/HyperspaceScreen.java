package client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HyperspaceScreen extends Screen {
    private static final ResourceLocation HYPERSPACE_TEXTURE = new ResourceLocation("galaxyunderchaos", "textures/gui/hyperspace.png");

    protected HyperspaceScreen() {
        super(Component.literal("Hyperspace Jump"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        RenderSystem.setShaderTexture(0, HYPERSPACE_TEXTURE);

        int centerX = (this.width - 256) / 2;
        int centerY = (this.height - 256) / 2;
        guiGraphics.blit(HYPERSPACE_TEXTURE, centerX, centerY, 0, 0, 256, 256);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
