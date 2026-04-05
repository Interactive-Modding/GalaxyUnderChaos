package client.renderer.lightsaber;

import static client.renderer.lightsaber.LightsaberAnimData.*;

/**
 * Default and wield-style-specific animation data.
 *
 * <p>Contains fallback profiles used when no specific form is selected, plus
 * dedicated animation data for Staff (double-bladed) and Dual wield styles
 * that override per-form data.</p>
 *
 * <h3>Staff (Double-Bladed)</h3>
 * <p>ALWAYS two-handed. Both hands grip the long hilt with a spread bo-staff
 * grip — hands apart, not crossing. The staff rotates around the center grip
 * point. Both mainScale and supportScale track nearly identically to keep
 * the locked two-handed feel throughout all animations.</p>
 *
 * <h3>Dual Wield</h3>
 * <p>Two independent sabers with cinematic staggered strikes. Main hand swings
 * first with its full arc, then the off-hand follows ~0.15s later with its own
 * distinct slash. This creates fluid alternating motion rather than simultaneous
 * or tentative movement.</p>
 */
final class FormDefaults {

    private FormDefaults() {}

    // ═══════════════════════════════════════════════════════════════════
    //  DEFAULT IDLE PROFILES
    //  Used when no specific lightsaber form is selected
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile DEFAULT_SINGLE = new ArmProfile(
            -1.02F, -0.42F, -0.18F,
            -1.18F, 0.52F, 0.04F,
            0.48F, 0.02F,
            0.42F, 0.04F,
            0.06F, 0.18F,
            -1.25F, -0.55F, -0.22F,
            -0.95F, 0.35F, 0.06F,
            true,
            0.74F, 0.92F,
            -0.08F
    );

    static final ArmProfile DEFAULT_DUAL = new ArmProfile(
            -0.86F, -0.60F, -0.22F,
            -0.82F, 0.62F, 0.18F,
            0.22F, 0.00F,
            0.22F, 0.00F,
            0.05F, 0.16F,
            -1.18F, -0.72F, -0.24F,
            -1.05F, 0.74F, 0.20F,
            false,
            0.80F, 0.96F,
            -0.05F
    );

    // Staff (double-bladed): ALWAYS two-handed, hands SPREAD on long hilt
    // Like a bo-staff grip — both arms forward at chest, hands apart (not crossing)
    static final ArmProfile DEFAULT_STAFF = new ArmProfile(
            -1.34F, -0.04F, -0.10F,        // main arm: forward at chest, barely inward
            -1.38F, -0.02F, 0.10F,          // support arm: forward at chest, barely outward (spread grip)
            0.14F, 0.02F,
            0.14F, 0.02F,
            0.05F, 0.16F,
            -1.58F, -0.08F, -0.14F,
            -1.62F, -0.04F, 0.12F,
            true,
            0.84F, 0.98F,
            -0.06F
    );

    // ═══════════════════════════════════════════════════════════════════
    //  DEFAULT ATTACK KEYFRAMES
    //  Generic balanced attack when no specific form is selected
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] DEFAULT_ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,      0,      0     ),
            kf(0.12F,   -8,  -14,   -4,   0.006F, 0.006F, 0.012F,-0.16F, -0.10F, -0.03F,  0.04F,  0     ),
            kf(0.24F,  -16,  -28,   -8,   0.014F, 0.014F, 0.028F,-0.38F, -0.28F, -0.06F, -0.08F,  0.04F ),
            kf(0.42F,  -40,   16,  -20,   0.008F,-0.008F,-0.020F, 0.82F,  0.64F, -0.14F,  0.12F, -0.06F ),
            kf(0.58F,  -30,   36,  -16,  -0.004F,-0.014F,-0.032F, 0.64F,  0.48F, -0.10F,  0.08F, -0.04F ),
            kf(0.74F,  -12,   16,   -6,   0,     -0.006F,-0.010F, 0.22F,  0.16F, -0.04F,  0.03F, -0.01F ),
            kf(0.88F,   -4,    6,   -2,   0,     -0.002F,-0.004F, 0.06F,  0.04F, -0.01F,  0.01F,  0     ),
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.02F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  STAFF (DOUBLE-BLADED) ATTACK KEYFRAMES
    //
    //  ALWAYS two-handed — both arms move in lockstep.
    //  Horizontal spinning sweep: staff rotates around center grip.
    //  Wide body rotation, supportScale tracks mainScale closely.
    //
    //  Timeline:
    //    0.00-0.18  Wind-up: staff winds back, body coils (heavy itemRoll)
    //    0.18-0.32  Transition: staff at maximum coil
    //    0.32-0.46  SPINNING SWEEP: staff rotates around center grip
    //              mainScale 0.86, supportScale 0.84 — nearly identical
    //    0.46-0.60  Follow-through: staff continues rotation
    //    0.60-1.00  Recovery: return to spread grip ready position
    //
    //  Note the heavy itemRoll values (-52° peak): this creates the
    //  visual impression of the staff spinning around the center pivot
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] STAFF_ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,      0,      0     ),
            kf(0.08F,   -4,   -8,  -12,   0.004F, 0.004F, 0.007F,-0.14F, -0.14F, -0.02F,  0.08F,  0.04F ),
            kf(0.18F,  -10,  -22,  -30,   0.010F, 0.008F, 0.016F,-0.42F, -0.40F, -0.06F,  0.18F,  0.10F ),
            kf(0.32F,  -20,    8,  -52,   0.006F,-0.003F,-0.008F, 0.36F,  0.34F, -0.12F, -0.14F,  0.10F ),
            kf(0.46F,  -28,   28,  -42,  -0.003F,-0.010F,-0.022F, 0.86F,  0.84F, -0.16F, -0.22F,  0.14F ),
            kf(0.60F,  -20,   22,  -28,   0,     -0.008F,-0.014F, 0.52F,  0.50F, -0.10F, -0.14F,  0.08F ),
            kf(0.74F,  -10,   12,  -14,   0,     -0.004F,-0.007F, 0.22F,  0.20F, -0.05F, -0.06F,  0.03F ),
            kf(0.88F,   -4,    4,   -5,   0,     -0.001F,-0.003F, 0.08F,  0.07F, -0.02F, -0.02F,  0.01F ),
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.02F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  DUAL WIELD ATTACK KEYFRAMES
    //
    //  Cinematic staggered strikes: main hand swings first with full arc,
    //  off-hand follows ~0.15s later with its own distinct slash direction.
    //  Both arms commit fully — fluid alternating motion, not tentative.
    //
    //  Timeline:
    //    0.00-0.16  Main wind-up: primary saber draws back
    //    0.16-0.30  MAIN STRIKE: primary saber full arc (mainScale 0.82)
    //              Off-hand is still in wind-up (supportScale -0.28)
    //    0.30-0.42  Crossover: main decelerates, off-hand STRIKES (0.72)
    //    0.42-0.56  Off-hand follow-through
    //    0.56-1.00  Both arms recover in sequence
    //
    //  The stagger creates the cinematic dual-wield alternation:
    //    main peaks at t=0.30 (0.82), support peaks at t=0.42 (0.72)
    //  During support's negative phase, it swings OPPOSITE to main
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] DUAL_ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,      0,      0     ),
            kf(0.07F,   -4,   -8,   -3,   0.003F, 0.003F, 0.007F,-0.12F,  0.04F,  0,      0.03F,  0     ),
            kf(0.16F,  -12,  -22,   -7,   0.008F, 0.006F, 0.016F,-0.42F,  0.08F, -0.04F, -0.06F,  0.04F ),
            kf(0.30F,  -34,    8,  -18,   0.006F,-0.006F,-0.014F, 0.82F, -0.28F, -0.10F,  0.10F, -0.06F ),
            kf(0.42F,  -26,   22,  -14,   0,     -0.008F,-0.018F, 0.58F,  0.72F, -0.08F,  0.06F, -0.04F ),
            kf(0.56F,  -16,   16,   -8,  -0.003F,-0.007F,-0.013F, 0.26F,  0.56F, -0.06F,  0.04F, -0.02F ),
            kf(0.70F,   -8,    8,   -4,   0,     -0.004F,-0.007F, 0.10F,  0.28F, -0.03F,  0.02F, -0.01F ),
            kf(0.85F,   -3,    3,   -1,   0,     -0.001F,-0.003F, 0.04F,  0.10F, -0.01F,  0.01F,  0     ),
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.02F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  DEFAULT FIRST-PERSON ITEM PROFILES
    // ═══════════════════════════════════════════════════════════════════

    static final ItemProfile DEFAULT_SINGLE_ITEM = new ItemProfile(
            -14F, -18F, -10F, 0.02F, 0.04F, 0.00F, 0.95F);

    static final ItemProfile DEFAULT_DUAL_ITEM = new ItemProfile(
            -10F, -28F, -8F, 0.02F, 0.04F, 0.01F, 0.93F);

    static final ItemProfile DEFAULT_STAFF_ITEM = new ItemProfile(
            -6F, 32F, -60F, 0.06F, 0.03F, 0.00F, 1.00F);
}
