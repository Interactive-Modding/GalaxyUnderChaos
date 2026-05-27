package client.mixin;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import server.galaxyunderchaos.worldgen.dimension.PlanetTimeWeatherHandler;

@Mixin(ServerLevel.class)
public abstract class ServerLevelPlanetTimeMixin {
    @Inject(method = "setDayTime", at = @At("HEAD"), cancellable = true)
    private void galaxyunderchaos$setPlanetDayTime(long dayTime, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        if (PlanetTimeWeatherHandler.isManagedPlanet(level)) {
            PlanetTimeWeatherHandler.setPlanetDayTime(level, dayTime);
            ci.cancel();
        }
    }

    @Inject(method = "setWeatherParameters", at = @At("HEAD"), cancellable = true)
    private void galaxyunderchaos$setPlanetWeather(int clearWeatherTime, int weatherTime, boolean raining, boolean thundering, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        if (PlanetTimeWeatherHandler.isManagedPlanet(level)) {
            PlanetTimeWeatherHandler.setPlanetWeather(level, clearWeatherTime, weatherTime, raining, thundering);
            ci.cancel();
        }
    }

    @Inject(method = "advanceWeatherCycle", at = @At("HEAD"), cancellable = true)
    private void galaxyunderchaos$skipVanillaPlanetWeatherCycle(CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        if (PlanetTimeWeatherHandler.isManagedPlanet(level)) {
            ci.cancel();
        }
    }

    @Inject(method = "resetWeatherCycle", at = @At("HEAD"), cancellable = true)
    private void galaxyunderchaos$resetPlanetWeatherCycle(CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        if (PlanetTimeWeatherHandler.isManagedPlanet(level)) {
            PlanetTimeWeatherHandler.resetPlanetWeather(level);
            ci.cancel();
        }
    }
}
