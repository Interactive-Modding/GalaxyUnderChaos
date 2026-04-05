package client.renderer.lightsaber;

import static client.renderer.lightsaber.LightsaberAnimData.*;

/**
 * Form VI · Niman — "Way of the Rancor"
 *
 * <p>The diplomat's form — balanced, moderate, and adaptable. Niman draws
 * elements from all previous forms to create a well-rounded but unexceptional
 * fighting style. It is the preferred form of Jedi who split their training
 * between combat and other Force disciplines. The off-hand is kept free
 * for Force techniques during combat.</p>
 *
 * <h3>Canonical Practitioners</h3>
 * <ul>
 *   <li>Exar Kun (combined with Jar'Kai dual-wielding)</li>
 *   <li>Many Jedi Council members who prioritized diplomacy</li>
 *   <li>General Grievous (adapted — uses all forms)</li>
 * </ul>
 *
 * <h3>Idle Stance</h3>
 * <p>One-handed grip with blade extended moderately forward. The off-hand is
 * drawn across the chest — ready for Force push, pull, or other techniques.
 * Stance is balanced and centered, neither aggressive nor purely defensive.
 * The "jack of all trades" position.</p>
 *
 * <h3>Attack Pattern</h3>
 * <p>Balanced diagonal slash — not as wide as Shii-Cho, not as precise as
 * Makashi, but competent and efficient. The body rotates moderately.
 * Peak mainScale 0.78 — solidly in the middle range. Support arm at 0.46
 * shows partial commitment (may assist or may stay free for Force use).</p>
 *
 * <h3>Block Stance</h3>
 * <p>Balanced guard, two-handed for defense. When blocking, Niman drops
 * the Force-ready off-hand to brace the hilt for stability.</p>
 */
final class FormNiman {

    private FormNiman() {}

    // ═══════════════════════════════════════════════════════════════════
    //  IDLE STANCE — One-handed, off-hand drawn across chest
    //  Diplomat form: blade extended outward, moderate guard
    //  Free hand across chest, ready for Force techniques
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile IDLE = new ArmProfile(
            -0.80F, -0.28F, -0.04F,        // main arm: forward, moderate extension
            -0.86F, 0.56F, 0.24F,           // off-hand: ACROSS CHEST (forward, strongly inward)
            0.22F, 0.00F,                  // main positional: moderate inward
            0.30F, 0.02F,                  // support positional: moderate inward (hand at chest)
            0.04F, 0.18F,                  // idle sway: moderate
            -1.26F, -0.32F, -0.06F,         // attack vector main: balanced diagonal
            -0.86F, 0.56F, 0.24F,           // attack vector support: stays at chest
            false,                          // ONE-HANDED — off-hand free for Force use
            0.80F, 0.98F,
            -0.03F                           // body lean: slight forward lean
    );

    // ═══════════════════════════════════════════════════════════════════
    //  BLOCK STANCE — Balanced guard, two-handed for defense
    //  Off-hand drops from Force-ready position to brace hilt
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile BLOCK = new ArmProfile(
            -1.16F, -0.14F, -0.04F,
            -1.20F, 0.44F, 0.08F,           // support comes to hilt for defensive brace
            0.24F, 0.02F,
            0.42F, 0.04F,
            0.020F, 0.16F,
            -1.26F, -0.18F, -0.06F,
            -1.22F, 0.42F, 0.06F,
            true,                           // TWO-HANDED when blocking
            0.90F, 1.00F,
            -0.01F
    );

    // ═══════════════════════════════════════════════════════════════════
    //  ATTACK KEYFRAMES — Balanced diagonal slash
    //
    //  Timeline:
    //    0.00-0.20  Wind-up: controlled preparation, no excess
    //    0.20-0.38  STRIKE: balanced diagonal cut
    //              mainScale peaks at 0.78, support at 0.46
    //    0.38-0.52  Follow-through: blade continues diagonal arc
    //    0.52-1.00  Recovery: efficient return to guard
    //
    //  Total arc: ~55° main arm — middle of the pack
    //  Support arm at 0.46: partial commitment — Niman is adaptable,
    //    support may assist the strike or stay free for Force techniques
    //  Body rotation moderate (0.12 yaw) — balanced approach
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,      0,      0     ),
            // Wind-up: controlled preparation
            kf(0.09F,   -5,  -10,   -3,   0.004F, 0.004F, 0.008F,-0.10F, -0.06F,  0,      0.03F,  0     ),
            // Wind-up peak: ready to release
            kf(0.20F,  -10,  -24,   -6,   0.008F, 0.008F, 0.018F,-0.34F, -0.20F, -0.04F, -0.06F,  0.04F ),
            // STRIKE: balanced diagonal cut — peak power
            kf(0.38F,  -38,   14,  -18,   0.008F,-0.008F,-0.018F, 0.78F,  0.46F, -0.10F,  0.12F, -0.06F ),
            // Follow-through: diagonal continues
            kf(0.52F,  -30,   32,  -14,  -0.003F,-0.014F,-0.028F, 0.62F,  0.36F, -0.08F,  0.08F, -0.04F ),
            // Deceleration
            kf(0.68F,  -14,   16,   -6,  -0.001F,-0.006F,-0.010F, 0.24F,  0.14F, -0.04F,  0.03F, -0.02F ),
            // Recovery
            kf(0.84F,   -4,    6,   -2,   0,     -0.002F,-0.004F, 0.08F,  0.04F, -0.01F,  0.01F,  0     ),
            // Return to idle
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.01F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  FIRST-PERSON ITEM PROFILES
    // ═══════════════════════════════════════════════════════════════════

    static final ItemProfile ITEM = new ItemProfile(
            -10F, -14F, -8F,     // blade forward, balanced angle
            0.02F, 0.04F, 0.00F,
            0.95F
    );

    static final ItemProfile BLOCK_ITEM = new ItemProfile(
            -4F, -10F, -4F,      // blade slightly raised for balanced guard
            0.01F, 0.06F, -0.01F,
            0.95F
    );
}
