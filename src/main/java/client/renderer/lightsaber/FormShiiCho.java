package client.renderer.lightsaber;

import static client.renderer.lightsaber.LightsaberAnimData.*;

/**
 * Form I · Shii-Cho — "Way of the Sarlacc"
 *
 * <p>The most ancient and fundamental lightsaber form. Shii-Cho teaches wide,
 * sweeping strikes designed for blade-to-blade combat. It emphasizes raw power
 * and broad coverage over precision or finesse.</p>
 *
 * <h3>Canonical Practitioners</h3>
 * <ul>
 *   <li>Kit Fisto (primary form)</li>
 *   <li>Most Jedi younglings learn this first</li>
 * </ul>
 *
 * <h3>Idle Stance</h3>
 * <p>Two-handed grip at navel height. Both hands grip the hilt centered in front
 * of the stomach. Blade angles upward ~45°. Wide, stable stance with weight
 * evenly distributed. The classic "ready" position — arms extended forward at
 * chest level, blade horizontal or slightly angled up.</p>
 *
 * <h3>Attack Pattern</h3>
 * <p>Wide deliberate horizontal sweep across the body. Full body rotation drives
 * both arms together. Maximum arc coverage for zone-based attacks.
 * Peak mainScale 0.88 produces ~65° arm swing; support arm at 0.76 is fully
 * committed for two-handed power.</p>
 *
 * <h3>Block Stance</h3>
 * <p>Vertical guard in front of face, two-handed. Blade held upright as a
 * shield between the wielder and the attacker.</p>
 */
final class FormShiiCho {

    private FormShiiCho() {}

    // ═══════════════════════════════════════════════════════════════════
    //  IDLE STANCE — Two-handed grip at navel, blade angled up ~45°
    //  Both hands on hilt, centered in front of stomach
    //  Wide stance for stability, weight centered
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile IDLE = new ArmProfile(
            -0.90F, -0.32F, -0.08F,       // main arm: forward to navel, moderately inward
            -0.94F, 0.50F, 0.10F,          // support arm: same angle, crosses inward to grip hilt
            0.38F, 0.02F,                 // main positional: inward push, slight lift
            0.44F, 0.04F,                 // support positional: more inward push (hand on shared hilt)
            0.06F, 0.18F,                 // idle sway: moderate amplitude, relaxed frequency
            -1.30F, -0.36F, -0.12F,        // attack vector main: forward-down diagonal
            -1.26F, 0.48F, 0.08F,          // attack vector support: follows main closely
            true,                          // TWO-HANDED — both hands always on hilt
            0.78F, 0.96F,                 // blend factors: standing, crouching
            -0.05F                          // body lean: slight forward lean
    );

    // ═══════════════════════════════════════════════════════════════════
    //  BLOCK STANCE — Vertical guard in front of face, two-handed
    //  Blade held upright between wielder and attacker
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile BLOCK = new ArmProfile(
            -1.38F, -0.10F, -0.02F,        // main arm: raised to face level
            -1.42F, 0.46F, 0.08F,           // support arm: crosses to grip hilt for block
            0.28F, 0.02F,
            0.44F, 0.04F,
            0.025F, 0.15F,                 // reduced idle sway during block
            -1.38F, -0.14F, -0.14F,
            -1.34F, 0.44F, 0.06F,
            true,
            0.90F, 1.00F,
            -0.01F
    );

    // ═══════════════════════════════════════════════════════════════════
    //  ATTACK KEYFRAMES — Wide deliberate two-handed horizontal sweep
    //
    //  Timeline:
    //    0.00-0.18  Wind-up: arms draw back slightly, body coils
    //    0.18-0.35  POWER PHASE: full horizontal sweep across body
    //              mainScale peaks at 0.88, support at 0.76
    //    0.35-0.50  Follow-through: blade continues arc past target
    //    0.50-1.00  Recovery: arms return to idle position
    //
    //  Total arc: ~65° main arm, ~55° support arm
    //  Body rotation: 0.18 rad yaw at peak — full torso commitment
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,      0,      0     ),
            // Wind-up: slight backstep, arms coil
            kf(0.08F,   -4,  -12,   -3,   0.006F, 0.006F, 0.012F,-0.12F, -0.10F, -0.02F,  0.06F,  0     ),
            // Wind-up peak: maximum coil before release
            kf(0.18F,  -14,  -30,   -8,   0.016F, 0.016F, 0.032F,-0.40F, -0.34F, -0.06F, -0.10F,  0.06F ),
            // STRIKE: full horizontal sweep — peak power
            kf(0.35F,  -40,   16,  -22,   0.010F,-0.010F,-0.020F, 0.88F,  0.76F, -0.16F,  0.18F, -0.10F ),
            // Follow-through: blade continues past target
            kf(0.50F,  -34,   44,  -18,  -0.006F,-0.016F,-0.038F, 0.74F,  0.62F, -0.12F,  0.14F, -0.08F ),
            // Deceleration
            kf(0.68F,  -18,   26,   -8,  -0.002F,-0.008F,-0.014F, 0.32F,  0.26F, -0.06F,  0.06F, -0.03F ),
            // Recovery
            kf(0.84F,   -6,   10,   -2,   0,     -0.003F,-0.006F, 0.10F,  0.08F, -0.02F,  0.02F, -0.01F ),
            // Return to idle
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.02F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  FIRST-PERSON ITEM PROFILES
    // ═══════════════════════════════════════════════════════════════════

    static final ItemProfile ITEM = new ItemProfile(
            -14F, -18F, -10F,   // blade angled slightly down, turned inward
            0.02F, 0.04F, 0.00F,
            0.95F
    );

    static final ItemProfile BLOCK_ITEM = new ItemProfile(
            -10F, -4F, -6F,     // blade more vertical for blocking
            0.01F, 0.06F, -0.01F,
            0.95F
    );
}
