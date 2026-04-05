package server.galaxyunderchaos.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.galaxyunderchaos;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        galaxyunderchaos.LIGHTSABERS.values().forEach(lightsaber -> registerActivePredicate(lightsaber.get()));
        registerActivePredicate(galaxyunderchaos.CUSTOM_LIGHTSABER.get());
        registerActivePredicate(galaxyunderchaos.DOUBLE_LIGHTSABER.get());
    }

    private static void registerActivePredicate(net.minecraft.world.item.Item item) {
        ItemProperties.register(item,
                new ResourceLocation(galaxyunderchaos.MODID, "active"),
                (itemStack, clientLevel, livingEntity, i) ->
                        itemStack.getItem() instanceof LightsaberItem lightsaber && lightsaber.isActive(itemStack) ? 1.0F : 0.0F
        );
    }
}
