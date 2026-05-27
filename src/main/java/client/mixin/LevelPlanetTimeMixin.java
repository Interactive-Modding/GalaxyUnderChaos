package client.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import server.galaxyunderchaos.worldgen.dimension.PlanetTimeWeatherHandler;

@Mixin(Level.class)
public abstract class LevelPlanetTimeMixin {
    @Inject(method = "getGameTime", at = @At("HEAD"), cancellable = true)
    private void galaxyunderchaos$getPlanetGameTime(CallbackInfoReturnable<Long> cir) {
        if ((Object) this instanceof ServerLevel level) {
            Long gameTime = PlanetTimeWeatherHandler.getPlanetGameTimeForMixin(level);
            if (gameTime != null) {
                cir.setReturnValue(gameTime);
            }
        }
    }

    @Inject(method = "getDayTime", at = @At("HEAD"), cancellable = true)
    private void galaxyunderchaos$getPlanetDayTime(CallbackInfoReturnable<Long> cir) {
        if ((Object) this instanceof ServerLevel level) {
            Long dayTime = PlanetTimeWeatherHandler.getPlanetDayTimeForMixin(level);
            if (dayTime != null) {
                cir.setReturnValue(dayTime);
            }
        }
    }

    @Inject(method = "isRaining", at = @At("HEAD"), cancellable = true)
    private void galaxyunderchaos$isPlanetRaining(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerLevel level) {
            Boolean raining = PlanetTimeWeatherHandler.getPlanetRainingForMixin(level);
            if (raining != null) {
                cir.setReturnValue(raining);
            }
        }
    }
}
