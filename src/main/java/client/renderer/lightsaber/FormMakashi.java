package client.renderer.lightsaber;

import static client.renderer.lightsaber.LightsaberAnimData.*;

/**
 * Form II · Makashi — "Way of the Ysalamiri"
 *
 * <p>The dueling form — elegant, precise, and economical. Makashi is designed
 * specifically for lightsaber-to-lightsaber combat. It emphasizes blade-work,
 * parries, and ripostes with minimal body movement. One-handed technique
 * allows for greater reach and fencing-style thrusts.</p>
 *
 * <h3>Canonical Practitioners</h3>
 * <ul>
 *   <li>Count Dooku (quintessential Makashi master)</li>
 *   <li>Asajj Ventress (adapted dual-Makashi)</li>
 * </ul>
 *
 * <h3>Idle Stance</h3>
 * <p>Classic fencing pose: one-handed grip with the blade extended in a low guard
 * to the side. The off-hand is tucked behind the back in a Dooku-style flourish —
 * a sign of confidence and precision. Sideways body profile presents a smaller
 * target. Weight on the back foot, ready to lunge.</p>
 *
 * <h3>Attack Pattern</h3>
 * <p>Quick precise thrust forward, then a lateral cut. Minimal wind-up to avoid
 * telegraphing. Off-hand stays back throughout — no two-handed commitment.
 * Peak mainScale 0.72 for controlled precision; supportScale only 0.08 since
 * the off-hand doesn't participate in the strike.</p>
 *
 * <h3>Block Stance</h3>
 * <p>Precision parry with one hand. Off-hand stays behind the back even while
 * blocking — Makashi's defense relies on blade angles, not brute force.</p>
 */
final class FormMakashi {

    private FormMakashi() {}

    // ═══════════════════════════════════════════════════════════════════
    //  IDLE STANCE — One-handed fencer, off-hand behind back
    //  Dooku stance: sideways profile, blade in low guard
    //  Free hand tucked behind back (arm backward, positive xRot)
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile IDLE = new ArmProfile(
            -0.54F, -0.80F, 0.08F,         // main arm: extended to side, low guard fencing pose
            0.22F, 0.34F, 0.42F,           // off-hand: BEHIND BACK (positive xRot = arm backward, tucked)
            0.12F, -0.02F,                 // main positional: slight inward, slight drop
            0.08F, -0.02F,                 // support positional: minimal (not on hilt)
            0.03F, 0.22F,                  // idle sway: subtle — Makashi is controlled
            -0.90F, -0.58F, 0.08F,          // attack vector main: forward thrust
            0.22F, 0.34F, 0.42F,           // attack vector support: stays behind back
            false,                          // ONE-HANDED — off-hand behind back
            0.86F, 1.00F,
            -0.02F                           // body lean: minimal — upright fencer posture
    );

    // ═══════════════════════════════════════════════════════════════════
    //  BLOCK STANCE — Precision parry, off-hand stays behind back
    //  Blade angled to deflect, NOT a brute-force block
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile BLOCK = new ArmProfile(
            -0.64F, -0.62F, 0.06F,
            0.18F, 0.30F, 0.38F,           // off-hand: behind back even while blocking
            0.14F, -0.02F,
            0.08F, -0.02F,
            0.015F, 0.18F,
            -0.90F, -0.58F, 0.08F,
            0.18F, 0.30F, 0.38F,
            false,
            0.88F, 1.00F,
            -0.01F
    );

    // ═══════════════════════════════════════════════════════════════════
    //  ATTACK KEYFRAMES — Elegant thrust then lateral cut
    //
    //  Timeline:
    //    0.00-0.16  Set-up: subtle forward lean, blade alignment
    //    0.16-0.30  THRUST: quick precise lunge forward
    //              mainScale peaks at 0.72, support stays at 0.08
    //    0.30-0.44  Lateral cut: blade sweeps sideways after thrust
    //    0.44-1.00  Recovery: controlled return, no wasted motion
    //
    //  Total arc: ~41° main arm — precise, not wide
    //  Off-hand stays back: supportScale maxes at 0.08
    //  Minimal body rotation — Makashi is all wrist and blade
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,      0,      0     ),
            // Set-up: subtle lean forward
            kf(0.07F,    4,   -6,   -2,   0.003F, 0.003F, 0.006F,-0.06F, -0.02F,  0,      0.02F,  0     ),
            // Blade alignment: preparing thrust line
            kf(0.16F,    8,  -14,   -3,   0.006F, 0.006F, 0.014F,-0.18F, -0.04F, -0.02F, -0.04F,  0.02F ),
            // THRUST: precise forward lunge — peak power
            kf(0.30F,  -26,   -8,   -6,   0.014F, 0,     -0.014F, 0.72F,  0.08F, -0.04F,  0.08F, -0.04F ),
            // Lateral cut: blade sweeps sideways after thrust
            kf(0.44F,  -22,  -38,  -12,   0.008F,-0.008F,-0.024F, 0.60F,  0.06F, -0.06F, -0.06F,  0.04F ),
            // Controlled recovery
            kf(0.62F,  -10,  -18,   -5,   0.003F,-0.003F,-0.008F, 0.22F,  0.03F, -0.02F, -0.03F,  0.02F ),
            // Return
            kf(0.82F,   -3,   -6,   -2,   0,     -0.001F,-0.003F, 0.06F,  0.01F, -0.01F, -0.01F,  0     ),
            // Idle
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.01F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  FIRST-PERSON ITEM PROFILES
    // ═══════════════════════════════════════════════════════════════════

    static final ItemProfile ITEM = new ItemProfile(
            4F, -30F, -4F,      // blade extended outward, fencing angle
            0.04F, 0.03F, 0.01F,
            0.96F
    );

    static final ItemProfile BLOCK_ITEM = new ItemProfile(
            4F, -24F, 10F,      // blade angled for deflection parry
            0.03F, 0.04F, 0.01F,
            0.96F
    );
}
