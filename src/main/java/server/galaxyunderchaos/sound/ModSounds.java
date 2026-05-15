package server.galaxyunderchaos.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import server.galaxyunderchaos.galaxyunderchaos;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, galaxyunderchaos.MODID);
    public static final RegistryObject<SoundEvent> LIGHTSABER_HIT = registerSoundEvent("lightsaber_hit");
    public static final RegistryObject<SoundEvent> LIGHTSABER_SWING = registerSoundEvent("lightsaber_swing");
    public static final RegistryObject<SoundEvent> LIGHTSABER_IDLE = registerSoundEvent("lightsaber_idle");
    public static final RegistryObject<SoundEvent> LIGHTSABER_TURN_ON = registerSoundEvent("lightsaber_on");
    public static final RegistryObject<SoundEvent> LIGHTSABER_TURN_OFF = registerSoundEvent("lightsaber_off");
    public static final RegistryObject<SoundEvent> HYPERSPACE = registerSoundEvent("hyperspace");
    public static final RegistryObject<SoundEvent> LIGHTSABER_DEFLECT = registerSoundEvent("lightsaber_deflect");
    public static final RegistryObject<SoundEvent> TOMB_SOUND = registerSoundEvent("tomb_sound");
    public static final RegistryObject<SoundEvent> WINGMAW_LIVING = registerSoundEvent("wingmaw_living");
    public static final RegistryObject<SoundEvent> WINGMAW_HURT = registerSoundEvent("wingmaw_hurt");
    public static final RegistryObject<SoundEvent> WINGMAW_ATTACK = registerSoundEvent("wingmaw_attack");
    public static final RegistryObject<SoundEvent> ACID_SPIDER_LIVING = registerSoundEvent("acid_spider_living");
    public static final RegistryObject<SoundEvent> ACID_SPIDER_HURT = registerSoundEvent("acid_spider_hurt");
    public static final RegistryObject<SoundEvent> ACID_SPIDER_ATTACK = registerSoundEvent("acid_spider_attack");
    public static final RegistryObject<SoundEvent> FORCE_CAST = registerSoundEvent("force_cast");
    public static final RegistryObject<SoundEvent> FORCE_CAST_DARK = registerSoundEvent("force_cast_dark");
    public static final RegistryObject<SoundEvent> FORCE_CAST_FAIL = registerSoundEvent("force_cast_fail");
    public static final RegistryObject<SoundEvent> FORCE_HEAL = registerSoundEvent("force_heal");
    public static final RegistryObject<SoundEvent> FORCE_LIGHTNING_START = registerSoundEvent("force_lightning_start");
    public static final RegistryObject<SoundEvent> FORCE_LIGHTNING_LOOP = registerSoundEvent("force_lightning_loop");
    public static final RegistryObject<SoundEvent> FORCE_STEALTH_ON = registerSoundEvent("force_stealth_on");
    public static final RegistryObject<SoundEvent> FORCE_STEALTH_OFF = registerSoundEvent("force_stealth_off");
    public static final RegistryObject<SoundEvent> AMBIENT_FORCE_FORTIFY = registerSoundEvent("ambient_force_fortify");
    public static final RegistryObject<SoundEvent> AMBIENT_FORCE_STEALTH = registerSoundEvent("ambient_force_stealth");
    public static final RegistryObject<SoundEvent> AMBIENT_FORCE_STASIS = registerSoundEvent("ambient_force_stasis");
    public static final RegistryObject<SoundEvent> AMBIENT_FORCE_ENERGY_RESIST = registerSoundEvent("ambient_force_energy_resist");
    public static final RegistryObject<SoundEvent> HOLOCRON_OPEN = registerSoundEvent("holocron_open");
    public static final RegistryObject<SoundEvent> HOLOCRON_UNLOCK = registerSoundEvent("holocron_unlock");
    public static final RegistryObject<SoundEvent> HOLOCRON_INVEST = registerSoundEvent("holocron_invest");
    public static final RegistryObject<SoundEvent> SHIP_ENGINE_START = registerSoundEvent("ship_engine_start");
    public static final RegistryObject<SoundEvent> SHIP_ENGINE_LOOP = registerSoundEvent("ship_engine_loop");
    public static final RegistryObject<SoundEvent> SHIP_ENGINE_STOP = registerSoundEvent("ship_engine_stop");
    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(galaxyunderchaos.MODID, name)));
    }
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
