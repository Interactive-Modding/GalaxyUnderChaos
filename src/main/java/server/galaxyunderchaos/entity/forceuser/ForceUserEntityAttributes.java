package server.galaxyunderchaos.entity.forceuser;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ForceUserEntityAttributes {
    private ForceUserEntityAttributes() {
    }

    @SubscribeEvent
    public static void register(EntityAttributeCreationEvent event) {
        event.put(galaxyunderchaos.JEDI_FORCE_USER.get(), ForceUserEntity.createAttributes().build());
        event.put(galaxyunderchaos.SITH_FORCE_USER.get(), ForceUserEntity.createAttributes().build());
        event.put(galaxyunderchaos.SITH_GHOST.get(), ForceUserEntity.createAttributes().build());
        event.put(galaxyunderchaos.SITH_LORD_GHOST.get(), ForceUserEntity.createBossAttributes().build());
        event.put(galaxyunderchaos.SITH_LORD.get(), ForceUserEntity.createBossAttributes().build());
        event.put(galaxyunderchaos.JEDI_MASTER.get(), ForceUserEntity.createBossAttributes().build());
        event.put(galaxyunderchaos.NEUTRAL_FORCE_USER.get(), ForceUserEntity.createAttributes().build());
        event.put(galaxyunderchaos.NEUTRAL_MASTER.get(), ForceUserEntity.createBossAttributes().build());
        event.put(galaxyunderchaos.SITH_APPRENTICE.get(), ForceUserEntity.createAttributes().build());
        event.put(galaxyunderchaos.JEDI_PADAWAN.get(), ForceUserEntity.createAttributes().build());
        event.put(galaxyunderchaos.NEUTRAL_PADAWAN.get(), ForceUserEntity.createAttributes().build());
        event.put(galaxyunderchaos.JEDI_TEMPLE_GUARD.get(), ForceUserEntity.createAttributes().build());
        event.put(galaxyunderchaos.SITH_GUARD.get(), ForceUserEntity.createAttributes().build());
    }
}
