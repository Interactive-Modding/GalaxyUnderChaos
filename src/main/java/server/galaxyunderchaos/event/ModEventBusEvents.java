package server.galaxyunderchaos.event;
import client.model.AcidSpiderModel;
import client.model.WingmawModel;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.entity.AcidSpiderEntity;
import server.galaxyunderchaos.entity.WingmawEntity;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AcidSpiderModel.LAYER_LOCATION, AcidSpiderModel::createBodyLayer);
        event.registerLayerDefinition(WingmawModel.LAYER_LOCATION, WingmawModel::createBodyLayer);
    }
    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(galaxyunderchaos.ACID_SPIDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(galaxyunderchaos.ACID_SPIDER.get(), AcidSpiderEntity.createAttributes().build());
        event.put(galaxyunderchaos.WINGMAW.get(), WingmawEntity.createAttributes().build());
    }
}