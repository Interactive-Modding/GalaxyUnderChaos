package server.galaxyunderchaos.worldgen.dimension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Random;

/**
 * Per-dimension clock/weather state for Galaxy Under Chaos planets.
 *
 * Minecraft normally gives non-overworld ServerLevels a DerivedLevelData wrapper,
 * which makes their day time and weather read from the overworld. This saved data
 * keeps a real independent clock and weather cycle for each planet's own
 * DimensionDataStorage.
 */
public class PlanetTimeWeatherData extends SavedData {
    private static final String DATA_NAME = "galaxyunderchaos_planet_time_weather";

    private boolean initialized;
    private long gameTime;
    private long dayTime;

    private int clearWeatherTime;
    private int rainTime;
    private int thunderTime;
    private boolean raining;
    private boolean thundering;

    private boolean weatherChanged;

    public static PlanetTimeWeatherData get(ServerLevel level) {
        PlanetTimeWeatherData data = level.getDataStorage().computeIfAbsent(
                PlanetTimeWeatherData::load,
                PlanetTimeWeatherData::new,
                DATA_NAME
        );
        data.initialize(level);
        return data;
    }

    public static PlanetTimeWeatherData load(CompoundTag tag) {
        PlanetTimeWeatherData data = new PlanetTimeWeatherData();
        data.initialized = tag.getBoolean("Initialized");
        data.gameTime = tag.getLong("GameTime");
        data.dayTime = tag.getLong("DayTime");
        data.clearWeatherTime = tag.getInt("ClearWeatherTime");
        data.rainTime = tag.getInt("RainTime");
        data.thunderTime = tag.getInt("ThunderTime");
        data.raining = tag.getBoolean("Raining");
        data.thundering = tag.getBoolean("Thundering");
        return data;
    }

    private void initialize(ServerLevel level) {
        if (this.initialized) {
            return;
        }

        ResourceLocation dimensionId = level.dimension().location();
        long seed = level.getSeed() ^ ((long) dimensionId.hashCode() * 31L);
        Random random = new Random(seed);

        this.initialized = true;
        this.gameTime = 0L;
        this.dayTime = Math.floorMod(seed, 24000L);
        this.clearWeatherTime = 0;
        this.rainTime = 12000 + random.nextInt(12000);
        this.thunderTime = 12000 + random.nextInt(12000);
        this.raining = false;
        this.thundering = false;
        this.weatherChanged = true;
        this.setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("Initialized", this.initialized);
        tag.putLong("GameTime", this.gameTime);
        tag.putLong("DayTime", this.dayTime);
        tag.putInt("ClearWeatherTime", this.clearWeatherTime);
        tag.putInt("RainTime", this.rainTime);
        tag.putInt("ThunderTime", this.thunderTime);
        tag.putBoolean("Raining", this.raining);
        tag.putBoolean("Thundering", this.thundering);
        return tag;
    }

    public void tick(ServerLevel level) {
        this.gameTime++;

        if (level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DAYLIGHT)) {
            this.dayTime++;
        }

        if (level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_WEATHER_CYCLE)) {
            this.tickWeather(level);
        }

        if ((this.gameTime % 200L) == 0L) {
            this.setDirty();
        }
    }

    private void tickWeather(ServerLevel level) {
        boolean oldRaining = this.raining;
        boolean oldThundering = this.thundering;

        if (this.clearWeatherTime > 0) {
            this.clearWeatherTime--;
            this.raining = false;
            this.thundering = false;
        } else {
            RandomSource random = level.getRandom();
            if (this.thunderTime > 0) {
                this.thunderTime--;
            } else {
                this.thundering = !this.thundering;
                this.thunderTime = this.thundering
                        ? 3600 + random.nextInt(12000)
                        : 12000 + random.nextInt(168000);
            }

            if (this.rainTime > 0) {
                this.rainTime--;
            } else {
                this.raining = !this.raining;
                this.rainTime = this.raining
                        ? 12000 + random.nextInt(12000)
                        : 12000 + random.nextInt(168000);
            }
        }

        if (oldRaining != this.raining || oldThundering != this.thundering) {
            this.weatherChanged = true;
            this.setDirty();
        }
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
        this.setDirty();
    }

    public void setWeatherParameters(int clearWeatherTime, int weatherTime, boolean raining, boolean thundering) {
        this.clearWeatherTime = Math.max(0, clearWeatherTime);
        this.rainTime = Math.max(0, weatherTime);
        this.thunderTime = Math.max(0, weatherTime);
        this.raining = raining;
        this.thundering = thundering;
        this.weatherChanged = true;
        this.setDirty();
    }

    public void clearWeatherAfterSleep(ServerLevel level) {
        this.clearWeatherTime = 0;
        this.rainTime = 12000 + level.random.nextInt(168000);
        this.thunderTime = 12000 + level.random.nextInt(168000);
        this.raining = false;
        this.thundering = false;
        this.weatherChanged = true;
        this.setDirty();
    }

    public boolean consumeWeatherChanged() {
        boolean changed = this.weatherChanged;
        this.weatherChanged = false;
        return changed;
    }

    public long getGameTime() {
        return this.gameTime;
    }

    public long getDayTime() {
        return this.dayTime;
    }

    public boolean isRaining() {
        return this.raining;
    }

    public boolean isThundering() {
        return this.thundering;
    }

    public float getRainLevel() {
        return this.raining ? 1.0F : 0.0F;
    }

    public float getThunderLevel() {
        return this.thundering ? 1.0F : 0.0F;
    }
}
