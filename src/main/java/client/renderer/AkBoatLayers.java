package client.renderer;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.entity.AkBoat;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(
        modid = galaxyunderchaos.MODID,
        value  = Dist.CLIENT,
        bus    = Mod.EventBusSubscriber.Bus.MOD)
public final class AkBoatLayers {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {

        for (AkBoat.Type type : AkBoat.Type.values()) {

            // normal boat
            e.registerLayerDefinition(
                    AkBoatRenderer.createBoatModelName(type),
                    () -> BoatModel.createBodyModel()
            );

            // chest‑boat
            e.registerLayerDefinition(
                    AkBoatRenderer.createChestBoatModelName(type),
                    () -> ChestBoatModel.createBodyModel()
            );
        }
    }
}
