package client.renderer.lightsaber;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import server.galaxyunderchaos.lightsaber.LightsaberForm;

import java.util.WeakHashMap;

/**
 * Client-side tracker for extended lightsaber swing animations.
 * <p>
 * Vanilla swing animations last ~12 ticks (0.6s). This tracker extends
 * the visual animation to 16-26 ticks depending on form, giving smoother
 * follow-through and form-specific pacing without affecting gameplay timing.
 * <p>
 * The tracker is per-entity and automatically detects swing starts by
 * watching the vanilla attack animation progress.
 */
public final class LightsaberSwingTracker {

    private static final WeakHashMap<LivingEntity, SwingState> STATES = new WeakHashMap<>();

    private LightsaberSwingTracker() {}

    /**
     * Ensure the tracker is up-to-date for this entity. Safe to call
     * multiple times per render frame; state only advances once per game tick.
     */
    public static void ensureTicked(LivingEntity entity) {
        SwingState state = STATES.computeIfAbsent(entity, e -> new SwingState());
        if (state.lastProcessedTick == entity.tickCount) {
            return;
        }
        state.lastProcessedTick = entity.tickCount;

        float vanillaProgress = entity.getAttackAnim(0.0F);

        if (vanillaProgress > 0.02F && state.lastVanillaProgress < 0.02F) {
            state.swingStartTick = entity.tickCount;
            state.active = true;
        }

        state.lastVanillaProgress = vanillaProgress;

        if (state.active) {
            int elapsed = entity.tickCount - state.swingStartTick;
            if (elapsed >= state.durationTicks) {
                state.active = false;
            }
        }
    }

    /**
     * Get the extended animation progress (0.0 to 1.0) with partial tick interpolation.
     */
    public static float getProgress(LivingEntity entity, float partialTick) {
        SwingState state = STATES.get(entity);
        if (state == null || !state.active) {
            return 0.0F;
        }
        float elapsed = (entity.tickCount - state.swingStartTick) + partialTick;
        return Mth.clamp(elapsed / (float) state.durationTicks, 0.0F, 1.0F);
    }

    public static boolean isSwinging(LivingEntity entity) {
        SwingState state = STATES.get(entity);
        return state != null && state.active;
    }

    /**
     * Set the animation duration for the next swing. Call this whenever
     * the player's selected form changes.
     */
    public static void setFormDuration(LivingEntity entity, LightsaberForm form) {
        SwingState state = STATES.computeIfAbsent(entity, e -> new SwingState());
        state.durationTicks = getDuration(form);
    }

    private static int getDuration(LightsaberForm form) {
        if (form == null) return 18;
        return switch (form) {
            case SHII_CHO -> 22;
            case MAKASHI  -> 18;
            case SORESU   -> 24;
            case ATARU    -> 26;
            case SHIEN    -> 22;
            case NIMAN    -> 20;
            case JUYO     -> 16;
        };
    }

    private static final class SwingState {
        float lastVanillaProgress;
        int swingStartTick;
        boolean active;
        int durationTicks = 18;
        int lastProcessedTick = -1;
    }
}
