package server.galaxyunderchaos.worldgen.dimension;

import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Set;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlanetTimeWeatherHandler {
    private static final Set<ResourceKey<Level>> MANAGED_PLANETS = Set.of(
            ModDimensions.TYTHON_LEVEL_KEY,
            ModDimensions.DANTOOINE_LEVEL_KEY,
            ModDimensions.OSSUS_LEVEL_KEY,
            ModDimensions.ILUM_LEVEL_KEY,
            ModDimensions.HOTH_LEVEL_KEY,
            ModDimensions.NABOO_LEVEL_KEY,
            ModDimensions.MUSTAFAR_LEVEL_KEY,
            ModDimensions.KORRIBAN_LEVEL_KEY,
            ModDimensions.MALACHOR_LEVEL_KEY,
            ModDimensions.ASHLA_LEVEL_KEY,
            ModDimensions.BOGAN_LEVEL_KEY
    );

    private PlanetTimeWeatherHandler() {
    }

    public static boolean isManagedPlanet(Level level) {
        return level instanceof ServerLevel && MANAGED_PLANETS.contains(level.dimension());
    }

    public static Long getPlanetGameTimeForMixin(ServerLevel level) {
        if (!isManagedPlanet(level)) {
            return null;
        }
        try {
            return PlanetTimeWeatherData.get(level).getGameTime();
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    public static Long getPlanetDayTimeForMixin(ServerLevel level) {
        if (!isManagedPlanet(level)) {
            return null;
        }
        try {
            return PlanetTimeWeatherData.get(level).getDayTime();
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    public static Boolean getPlanetRainingForMixin(ServerLevel level) {
        if (!isManagedPlanet(level)) {
            return null;
        }
        try {
            return PlanetTimeWeatherData.get(level).isRaining();
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    public static void setPlanetDayTime(ServerLevel level, long dayTime) {
        if (!isManagedPlanet(level)) {
            return;
        }
        PlanetTimeWeatherData data = PlanetTimeWeatherData.get(level);
        data.setDayTime(dayTime);
        syncTime(level, data);
    }

    public static void setPlanetWeather(ServerLevel level, int clearWeatherTime, int weatherTime, boolean raining, boolean thundering) {
        if (!isManagedPlanet(level)) {
            return;
        }
        PlanetTimeWeatherData data = PlanetTimeWeatherData.get(level);
        data.setWeatherParameters(clearWeatherTime, weatherTime, raining, thundering);
        applyWeatherVisuals(level, data);
        syncWeather(level, data);
    }

    public static void resetPlanetWeather(ServerLevel level) {
        if (!isManagedPlanet(level)) {
            return;
        }
        PlanetTimeWeatherData data = PlanetTimeWeatherData.get(level);
        data.clearWeatherAfterSleep(level);
        applyWeatherVisuals(level, data);
        syncWeather(level, data);
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!(event.level instanceof ServerLevel level) || !isManagedPlanet(level)) {
            return;
        }

        PlanetTimeWeatherData data = PlanetTimeWeatherData.get(level);
        data.tick(level);

        applyWeatherVisuals(level, data);

        if ((data.getGameTime() % 20L) == 0L) {
            syncTime(level, data);
        }

        if (data.consumeWeatherChanged() || (data.getGameTime() % 20L) == 0L) {
            syncWeather(level, data);
        }
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || !isManagedPlanet(level)) {
            return;
        }

        PlanetTimeWeatherData data = PlanetTimeWeatherData.get(level);
        long current = data.getDayTime();
        long nextMorning = current + 24000L;
        nextMorning -= nextMorning % 24000L;

        if (event.setTimeAddition(nextMorning)) {
            data.setDayTime(nextMorning);
            data.clearWeatherAfterSleep(level);
            applyWeatherVisuals(level, data);
            syncTime(level, data);
            syncWeather(level, data);
        }
    }

    private static void applyWeatherVisuals(ServerLevel level, PlanetTimeWeatherData data) {
        level.setRainLevel(data.getRainLevel());
        level.setThunderLevel(data.getThunderLevel());
    }

    private static void syncTime(ServerLevel level, PlanetTimeWeatherData data) {
        boolean doDaylightCycle = level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DAYLIGHT);
        ClientboundSetTimePacket packet = new ClientboundSetTimePacket(data.getGameTime(), data.getDayTime(), doDaylightCycle);
        for (ServerPlayer player : level.players()) {
            player.connection.send(packet);
        }
    }

    private static void syncWeather(ServerLevel level, PlanetTimeWeatherData data) {
        ClientboundGameEventPacket rainState = new ClientboundGameEventPacket(
                data.isRaining() ? ClientboundGameEventPacket.START_RAINING : ClientboundGameEventPacket.STOP_RAINING,
                0.0F
        );
        ClientboundGameEventPacket rainLevel = new ClientboundGameEventPacket(
                ClientboundGameEventPacket.RAIN_LEVEL_CHANGE,
                data.getRainLevel()
        );
        ClientboundGameEventPacket thunderLevel = new ClientboundGameEventPacket(
                ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE,
                data.getThunderLevel()
        );

        for (ServerPlayer player : level.players()) {
            player.connection.send(rainState);
            player.connection.send(rainLevel);
            player.connection.send(thunderLevel);
        }
    }
}
