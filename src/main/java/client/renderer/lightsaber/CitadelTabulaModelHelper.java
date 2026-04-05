package client.renderer.lightsaber;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.TabulaModelHandler;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CitadelTabulaModelHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<ResourceLocation, TabulaModelContainer> MODEL_CACHE = new ConcurrentHashMap<>();

    private CitadelTabulaModelHelper() {
    }

    public static boolean render(ResourceLocation modelLocation,
                                 ResourceLocation texture,
                                 PoseStack poseStack,
                                 MultiBufferSource buffer,
                                 int packedLight,
                                 int overlay) {
        TabulaModelContainer container = getOrLoadContainer(modelLocation);
        if (container == null) {
            LOGGER.warn("Failed to load Tabula model {}", modelLocation);
            return false;
        }

        if (Minecraft.getInstance().getResourceManager().getResource(texture).isEmpty()) {
            LOGGER.warn("Missing lightsaber texture {}", texture);
            return false;
        }

        TabulaModel model = new TabulaModel(container);
        model.resetToDefaultPose();

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        model.renderToBuffer(poseStack, consumer, packedLight, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    public static TabulaModelContainer getOrLoadContainer(ResourceLocation modelLocation) {
        return MODEL_CACHE.computeIfAbsent(modelLocation, CitadelTabulaModelHelper::loadContainerInternal);
    }

    private static TabulaModelContainer loadContainerInternal(ResourceLocation modelLocation) {
        String rawPath = modelLocation.getPath();
        String assetPath = "assets/" + modelLocation.getNamespace() + "/" + rawPath;
        String namespacedPath = modelLocation.getNamespace() + ":" + rawPath;

        TabulaModelContainer container = tryLoad(assetPath);
        if (container != null) {
            LOGGER.debug("Loaded Tabula model via asset path {}", assetPath);
            return container;
        }

        container = tryLoad(namespacedPath);
        if (container != null) {
            LOGGER.debug("Loaded Tabula model via namespaced path {}", namespacedPath);
            return container;
        }

        LOGGER.error("Could not load Tabula model {}. Tried '{}' and '{}'.", modelLocation, assetPath, namespacedPath);
        return null;
    }

    private static TabulaModelContainer tryLoad(String path) {
        try {
            return TabulaModelHandler.INSTANCE.loadTabulaModel(path);
        } catch (Exception ex) {
            LOGGER.debug("Tabula load failed for {}", path, ex);
            return null;
        }
    }

    public static void clearCache() {
        MODEL_CACHE.clear();
    }
}