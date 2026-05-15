package server.galaxyunderchaos.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Marker effect for Force powers.
 *
 * These effects intentionally do not apply vanilla potion logic by themselves.
 * ForcePowerHandler owns the gameplay behavior, while these effects provide
 * Galaxy Under Chaos names/icons in the effect HUD and inventory screen.
 */
public class ForceMobEffect extends MobEffect {
    public ForceMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}
