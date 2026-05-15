package client.renderer;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.entity.BlbaBoat;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(
        modid = galaxyunderchaos.MODID,
        value  = Dist.CLIENT,
        bus    = Mod.EventBusSubscriber.Bus.MOD)
public final class BlbaBoatLayers {

    private BlbaBoatLayers() {}

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        for (BlbaBoat.Type type : BlbaBoat.Type.values()) {
            e.registerLayerDefinition(BlbaBoatRenderer.createBoatModelName(type), () -> BoatModel.createBodyModel());
            e.registerLayerDefinition(BlbaBoatRenderer.createChestBoatModelName(type), () -> ChestBoatModel.createBodyModel());
        }
    }
}
