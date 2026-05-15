package client.renderer;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.entity.CambylictusBoat;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(
        modid = galaxyunderchaos.MODID,
        value  = Dist.CLIENT,
        bus    = Mod.EventBusSubscriber.Bus.MOD)
public final class CambylictusBoatLayers {

    private CambylictusBoatLayers() {}

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        for (CambylictusBoat.Type type : CambylictusBoat.Type.values()) {
            e.registerLayerDefinition(CambylictusBoatRenderer.createBoatModelName(type), () -> BoatModel.createBodyModel());
            e.registerLayerDefinition(CambylictusBoatRenderer.createChestBoatModelName(type), () -> ChestBoatModel.createBodyModel());
        }
    }
}
