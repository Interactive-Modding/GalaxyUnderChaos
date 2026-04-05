package client.renderer.lightsaber;

import static client.renderer.lightsaber.LightsaberAnimData.*;

/**
 * Form IV · Ataru — "Way of the Hawk-Bat"
 *
 * <p>The acrobatic aggression form. Ataru is characterized by explosive,
 * kinetic movements — leaps, spins, and powerful overhead strikes. It uses
 * the Force to augment physical ability, turning the wielder into a
 * whirlwind of blade energy. Two-handed for maximum power on the big swings.</p>
 *
 * <h3>Canonical Practitioners</h3>
 * <ul>
 *   <li>Yoda (master of Ataru despite his size)</li>
 *   <li>Qui-Gon Jinn</li>
 *   <li>Ahsoka Tano (early training)</li>
 * </ul>
 *
 * <h3>Idle Stance</h3>
 * <p>Two-handed grip held to one side at mid-torso height. Both hands grip the
 * hilt at chest level with the blade pointing straight up — coiled and ready
 * to spring into an explosive overhead arc. Athletic stance, weight slightly
 * forward, knees bent for explosive movement.</p>
 *
 * <h3>Attack Pattern</h3>
 * <p>Big acrobatic overhead arc — the signature Ataru power move. Arms wind
 * up high with a backward coil (negative mainScale in wind-up phase), then
 * crash down in a massive downward arc. Both arms commit fully.
 * Peak mainScale 0.92 — the LARGEST attack of any form, producing ~106°
 * arm swing. Support at 0.82 shows full two-handed commitment.</p>
 *
 * <h3>Block Stance</h3>
 * <p>High overhead guard, two-handed. Blade raised above the head to intercept
 * downward strikes and immediately counter with Ataru's overhead power.</p>
 */
final class FormAtaru {

    private FormAtaru() {}

    // ═══════════════════════════════════════════════════════════════════
    //  IDLE STANCE — Two-handed, blade vertical held to one side
    //  Yoda stance: both hands grip hilt at mid-torso side
    //  Blade pointing straight up, coiled for explosive movement
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile IDLE = new ArmProfile(
            -1.38F, -0.08F, -0.14F,        // main arm: raised, held to the side
            -1.44F, 0.50F, 0.06F,           // support arm: crosses to grip same hilt position
            0.28F, -0.02F,                 // main positional: inward push
            0.46F, 0.02F,                  // support positional: strong inward (hands on shared hilt)
            0.10F, 0.24F,                  // idle sway: pronounced — Ataru is kinetic even at rest
            -1.86F, -0.12F, -0.24F,         // attack vector main: massive overhead arc
            -1.82F, 0.48F, 0.04F,           // attack vector support: follows main closely
            true,                           // TWO-HANDED — both hands always on hilt
            0.86F, 1.03F,                  // blend factors: extra crouch blend for athletic stance
            -0.12F                           // body lean: noticeable forward lean, aggressive ready
    );

    // ═══════════════════════════════════════════════════════════════════
    //  BLOCK STANCE — High overhead guard, two-handed
    //  Blade raised above head to intercept and counter
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile BLOCK = new ArmProfile(
            -1.72F, -0.08F, -0.12F,
            -1.78F, 0.48F, 0.04F,           // support crosses to grip overhead
            0.30F, -0.02F,
            0.46F, 0.02F,
            0.030F, 0.20F,
            -1.86F, -0.12F, -0.24F,
            -1.82F, 0.46F, 0.02F,
            true,
            0.88F, 1.00F,
            -0.02F
    );

    // ═══════════════════════════════════════════════════════════════════
    //  ATTACK KEYFRAMES — Big acrobatic overhead arc, two-handed
    //
    //  Timeline:
    //    0.00-0.18  Wind-up: arms swing UPWARD and back (negative mainScale)
    //              This is the coiling phase — gathering power
    //    0.18-0.32  Transition: arms reach apex, begin reversing
    //    0.32-0.46  POWER STRIKE: massive downward arc
    //              mainScale peaks at 0.92 (!), support at 0.82
    //              This is THE biggest swing in any form
    //    0.46-0.62  Follow-through: full commitment past target
    //    0.62-1.00  Recovery: slow return — Ataru overextends by design
    //
    //  Total arc: ~106° main arm, ~85° support arm
    //  Note the wind-up reaching mainScale -0.48: arms go BACKWARD first
    //  Body lean: -0.20 at peak — deep forward lunge
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,       0,      0     ),
            // Wind-up start: arms begin upward swing
            kf(0.08F,   12,   -8,   -5,   0.004F, 0.006F, 0.008F,-0.16F, -0.14F, -0.02F,  0.04F,  0     ),
            // Wind-up peak: arms HIGH and back — maximum coil (mainScale -0.48)
            kf(0.18F,   30,  -20,  -14,   0.008F, 0.014F, 0.020F,-0.48F, -0.42F,  0.04F, -0.08F,  0.06F ),
            // Transition: apex — about to reverse into downward strike
            kf(0.32F,  -10,   -6,  -24,   0.006F, 0,     -0.008F, 0.38F,  0.32F, -0.12F,  0.10F, -0.08F ),
            // POWER STRIKE: massive downward overhead arc — PEAK
            kf(0.46F,  -52,   24,  -36,   0.003F,-0.018F,-0.036F, 0.92F,  0.82F, -0.20F, -0.14F,  0.10F ),
            // Follow-through: full commitment past target
            kf(0.62F,  -38,   16,  -22,  -0.003F,-0.014F,-0.022F, 0.56F,  0.48F, -0.12F, -0.08F,  0.04F ),
            // Deceleration: slow recovery (Ataru overextends)
            kf(0.78F,  -16,    6,   -8,   0,     -0.006F,-0.010F, 0.18F,  0.14F, -0.05F, -0.03F,  0.02F ),
            // Recovery
            kf(0.90F,   -5,    2,   -2,   0,     -0.002F,-0.004F, 0.06F,  0.04F, -0.02F, -0.01F,  0.01F ),
            // Return to idle
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.02F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  FIRST-PERSON ITEM PROFILES
    // ═══════════════════════════════════════════════════════════════════

    static final ItemProfile ITEM = new ItemProfile(
            -18F, -12F, -14F,    // blade angled for overhead readiness
            0.02F, 0.05F, -0.01F,
            0.97F
    );

    static final ItemProfile BLOCK_ITEM = new ItemProfile(
            22F, -2F, -3F,      // blade raised overhead for high block
            0.01F, 0.05F, -0.01F,
            0.97F
    );
}
