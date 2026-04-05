package client.renderer;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.entity.HeartBerryBoat;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(
        modid = galaxyunderchaos.MODID,
        value  = Dist.CLIENT,
        bus    = Mod.EventBusSubscriber.Bus.MOD)
public final class HBBoatLayers {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {

        for (HeartBerryBoat.Type type : HeartBerryBoat.Type.values()) {

            // normal boat
            e.registerLayerDefinition(
                    HBBoatRenderer.createBoatModelName(type),
                    () -> BoatModel.createBodyModel()
            );

            // chest‑boat
            e.registerLayerDefinition(
                    HBBoatRenderer.createChestBoatModelName(type),
                    () -> ChestBoatModel.createBodyModel()
            );
        }
    }
}
