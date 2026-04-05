package client.renderer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.galaxyunderchaos;

public class ModModelLayers {
    public static final ModelLayerLocation AK_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(galaxyunderchaos.MODID, "boat/ak"), "main");
    public static final ModelLayerLocation AK_CHEST_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(galaxyunderchaos.MODID, "chest_boat/ak"), "main");
    public static final ModelLayerLocation HEART_BERRY_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(galaxyunderchaos.MODID, "boat/heart_berry"), "main");
    public static final ModelLayerLocation HEART_BERRY_CHEST_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(galaxyunderchaos.MODID, "chest_boat/heart_berry"), "main");

}