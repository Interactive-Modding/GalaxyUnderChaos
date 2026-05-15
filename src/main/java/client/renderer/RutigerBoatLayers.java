package client.renderer;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.entity.RutigerBoat;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(
        modid = galaxyunderchaos.MODID,
        value  = Dist.CLIENT,
        bus    = Mod.EventBusSubscriber.Bus.MOD)
public final class RutigerBoatLayers {

    private RutigerBoatLayers() {}

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        for (RutigerBoat.Type type : RutigerBoat.Type.values()) {
            e.registerLayerDefinition(RutigerBoatRenderer.createBoatModelName(type), () -> BoatModel.createBodyModel());
            e.registerLayerDefinition(RutigerBoatRenderer.createChestBoatModelName(type), () -> ChestBoatModel.createBodyModel());
        }
    }
}
