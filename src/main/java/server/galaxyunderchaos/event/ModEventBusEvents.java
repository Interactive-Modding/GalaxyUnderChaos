package server.galaxyunderchaos.event;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.entity.AcidSpiderEntity;
import server.galaxyunderchaos.entity.WingmawEntity;
import server.galaxyunderchaos.entity.VonskrEntity;
import server.galaxyunderchaos.entity.ForceProjectionCloneEntity;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(galaxyunderchaos.ACID_SPIDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(galaxyunderchaos.VONSKR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                VonskrEntity::checkVonskrSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(galaxyunderchaos.ACID_SPIDER.get(), AcidSpiderEntity.createAttributes().build());
        event.put(galaxyunderchaos.WINGMAW.get(), WingmawEntity.createAttributes().build());
        event.put(galaxyunderchaos.VONSKR.get(), VonskrEntity.createAttributes().build());
        event.put(galaxyunderchaos.FORCE_PROJECTION_CLONE.get(), ForceProjectionCloneEntity.createAttributes().build());
    }
}