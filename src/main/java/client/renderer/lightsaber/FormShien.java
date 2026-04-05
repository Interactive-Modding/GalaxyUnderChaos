package client.renderer.lightsaber;

import static client.renderer.lightsaber.LightsaberAnimData.*;

/**
 * Form V · Shien / Djem So — "Way of the Krayt Dragon"
 *
 * <p>The power form — raw strength channeled through dominant, overwhelming
 * strikes. Shien focuses on deflecting blaster bolts back at shooters, while
 * Djem So applies that same philosophy to lightsaber dueling: meet the
 * opponent's blade with superior force, then immediately counter-attack.
 * Two-handed grip for maximum impact.</p>
 *
 * <h3>Canonical Practitioners</h3>
 * <ul>
 *   <li>Anakin Skywalker / Darth Vader (primary form)</li>
 *   <li>Luke Skywalker (inherited from his father)</li>
 *   <li>Ahsoka Tano (later career)</li>
 * </ul>
 *
 * <h3>Idle Stance</h3>
 * <p>Two-handed overhead hold. Both hands above the head with the blade cocked
 * back, ready to crash down with crushing force. The Anakin signature stance —
 * intimidating, dominant, and telegraphing raw power. Wide base, knees bent,
 * ready to drive forward with the full body behind the strike.</p>
 *
 * <h3>Attack Pattern</h3>
 * <p>Massive two-handed overhead chop. Arms wind up high (negative mainScale
 * in wind-up), then crash downward with full body commitment. Shien's attacks
 * are about overwhelming the opponent's guard through sheer force.
 * Peak mainScale 0.90 for ~80° arm swing with full two-handed power.
 * Body lean at -0.18 — deep forward drive behind the strike.</p>
 *
 * <h3>Block Stance</h3>
 * <p>Blade angled upward in deflection stance, two-handed. Designed to redirect
 * incoming strikes upward and immediately transition into a counter-chop.</p>
 */
final class FormShien {

    private FormShien() {}

    // ═══════════════════════════════════════════════════════════════════
    //  IDLE STANCE — Two-handed overhead, blade angled back
    //  Anakin stance: both hands above head, blade cocked to crash down
    //  mainX -2.18 = arms well above horizontal (nearly straight up)
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile IDLE = new ArmProfile(
            -2.18F, 0.14F, 0.08F,          // main arm: HIGH overhead (well above horizontal)
            -2.24F, 0.46F, 0.04F,           // support arm: also overhead, crosses to grip hilt
            0.30F, -0.04F,                 // main positional: inward push, slight drop (arms up)
            0.38F, -0.06F,                 // support positional: strong inward for hilt grip
            0.05F, 0.18F,                  // idle sway: controlled — power, not flash
            -2.44F, 0.16F, 0.10F,           // attack vector main: downward power chop
            -2.50F, 0.44F, 0.02F,           // attack vector support: follows main
            true,                           // TWO-HANDED — always grips with both hands
            0.84F, 1.02F,                  // blend factors
            -0.08F                           // body lean: moderate forward lean, ready to charge
    );

    // ═══════════════════════════════════════════════════════════════════
    //  BLOCK STANCE — Blade angled upward, deflection stance
    //  Two-handed. Redirects strikes upward for immediate counter
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile BLOCK = new ArmProfile(
            -2.06F, 0.12F, 0.10F,
            -2.12F, 0.44F, 0.04F,           // support crosses to grip overhead for block
            0.30F, -0.04F,
            0.38F, -0.06F,
            0.022F, 0.16F,
            -2.30F, 0.14F, 0.12F,
            -2.36F, 0.42F, 0.02F,
            true,
            0.88F, 1.00F,
            -0.02F
    );

    // ═══════════════════════════════════════════════════════════════════
    //  ATTACK KEYFRAMES — Massive two-handed overhead power chop
    //
    //  Timeline:
    //    0.00-0.22  Wind-up: arms raise even higher (negative mainScale)
    //              Building potential energy at the apex
    //    0.22-0.38  Transition: arms at maximum height, begin descent
    //    0.38-0.52  POWER CHOP: crushing downward strike
    //              mainScale peaks at 0.90, support at 0.80
    //    0.52-0.66  Follow-through: blade drives past target
    //    0.66-1.00  Recovery: return to overhead ready position
    //
    //  Total arc: ~80° main arm, ~72° support arm
    //  Wind-up reaches mainScale -0.42: arms go UP before crashing down
    //  Body lean: -0.18 at peak — full-body drive behind the chop
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,      0,      0     ),
            // Wind-up: arms raise higher
            kf(0.10F,   10,    6,    4,   0.003F, 0.006F, 0.008F,-0.14F, -0.12F,  0,      0.03F,  0     ),
            // Wind-up peak: maximum height (mainScale -0.42)
            kf(0.22F,   28,   12,   12,   0.007F, 0.014F, 0.020F,-0.42F, -0.36F,  0.04F, -0.06F,  0.04F ),
            // Transition: begin descent
            kf(0.38F,  -12,    4,   -6,   0.006F, 0,     -0.006F, 0.28F,  0.24F, -0.10F,  0.04F, -0.04F ),
            // POWER CHOP: crushing downward strike — PEAK
            kf(0.52F,  -48,   -6,   -8,   0.003F,-0.016F,-0.030F, 0.90F,  0.80F, -0.18F, -0.06F,  0.04F ),
            // Follow-through: blade drives past
            kf(0.66F,  -30,   -3,   -4,   0,     -0.008F,-0.014F, 0.44F,  0.36F, -0.08F, -0.03F,  0.02F ),
            // Recovery
            kf(0.82F,  -10,   -1,   -1,   0,     -0.003F,-0.005F, 0.12F,  0.10F, -0.03F, -0.01F,  0.01F ),
            // Return to idle
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.02F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  FIRST-PERSON ITEM PROFILES
    // ═══════════════════════════════════════════════════════════════════

    static final ItemProfile ITEM = new ItemProfile(
            -16F, 12F, 12F,      // blade angled overhead, high ready position
            0.02F, 0.04F, 0.00F,
            0.97F
    );

    static final ItemProfile BLOCK_ITEM = new ItemProfile(
            14F, 6F, 10F,       // blade raised for upward deflection
            0.01F, 0.05F, 0.00F,
            0.97F
    );
}
