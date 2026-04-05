package client.renderer.lightsaber;

import static client.renderer.lightsaber.LightsaberAnimData.*;

/**
 * Form VII · Juyo / Vaapad — "Way of the Vornskr"
 *
 * <p>The ferocity form — raw aggression channeled through unpredictable,
 * explosive attacks. Juyo uses internal emotion as fuel, walking the edge
 * between the Light and Dark sides. Attacks are sudden and violent with
 * almost no telegraphing — the opponent never sees it coming. Vaapad is
 * Mace Windu's refined variant that channels the opponent's dark side
 * energy back against them.</p>
 *
 * <h3>Canonical Practitioners</h3>
 * <ul>
 *   <li>Mace Windu (Vaapad variant — only living master)</li>
 *   <li>Depa Billaba (trained in Vaapad by Windu)</li>
 *   <li>Darth Maul (classical Juyo)</li>
 * </ul>
 *
 * <h3>Idle Stance</h3>
 * <p>One-handed high guard — blade held horizontal above the head. The off-hand
 * is swept down and across the body, creating an asymmetric and unpredictable
 * ready position. This Mace Windu signature stance looks deceptively relaxed
 * but can explode into violence from any angle. The main arm is nearly
 * straight up (xRot -2.04).</p>
 *
 * <h3>Attack Pattern</h3>
 * <p>Explosive cross-body diagonal slash with almost NO wind-up. Juyo's attacks
 * are designed to come from nowhere — minimal telegraph, maximum violence.
 * The wind-up phase is only 0.11s (vs 0.18-0.22 for other forms).
 * Peak mainScale 0.88 for ~65° arm swing, but supportScale stays at 0.10
 * because the off-hand doesn't participate — it's all explosive one-arm power.
 * Aggressive body twist (-0.22 lean) — full commitment to the strike.</p>
 *
 * <h3>Block Stance</h3>
 * <p>Forward aggressive guard, two-handed for strength. Even Juyo's defense
 * is aggressive — the block is designed to create an immediate opening
 * for counter-attack.</p>
 */
final class FormJuyo {

    private FormJuyo() {}

    // ═══════════════════════════════════════════════════════════════════
    //  IDLE STANCE — One-handed high guard, off-hand swept down
    //  Mace Windu stance: blade horizontal above head
    //  Off-hand swept down and across body — unpredictable asymmetry
    //  mainX -2.04 = arm nearly vertical above head
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile IDLE = new ArmProfile(
            -2.04F, 0.08F, -0.18F,         // main arm: HIGH above head, blade horizontal
            -0.20F, 0.34F, 0.28F,           // off-hand: SWEPT DOWN and across body
            0.28F, -0.04F,                 // main positional: inward, slight drop
            0.12F, 0.04F,                  // support positional: minimal (free hand)
            0.10F, 0.24F,                  // idle sway: pronounced — aggressive energy at rest
            -2.36F, 0.12F, -0.22F,          // attack vector main: explosive diagonal
            -0.20F, 0.34F, 0.28F,           // attack vector support: stays swept down
            false,                          // ONE-HANDED — off-hand swept aside
            0.90F, 1.05F,                  // blend factors: high — aggressive override
            -0.14F                           // body lean: aggressive forward lean
    );

    // ═══════════════════════════════════════════════════════════════════
    //  BLOCK STANCE — Forward aggressive guard, two-handed for strength
    //  Even defense is aggressive — block creates counter-attack opening
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile BLOCK = new ArmProfile(
            -1.24F, 0.06F, -0.16F,
            -1.28F, 0.46F, 0.04F,           // support comes to hilt for power block
            0.26F, -0.02F,
            0.44F, 0.02F,
            0.028F, 0.22F,
            -1.50F, 0.08F, -0.20F,
            -1.46F, 0.44F, 0.02F,
            true,                           // TWO-HANDED when blocking
            0.90F, 1.00F,
            -0.06F
    );

    // ═══════════════════════════════════════════════════════════════════
    //  ATTACK KEYFRAMES — Explosive cross-body diagonal, minimal telegraph
    //
    //  Timeline:
    //    0.00-0.11  BARELY ANY WIND-UP: only 0.11s of setup
    //              This is by design — Juyo doesn't telegraph
    //    0.11-0.26  EXPLOSIVE STRIKE: sudden violent diagonal slash
    //              mainScale rockets from -0.14 to 0.88 in 0.15s
    //    0.26-0.42  Cross-body follow: blade crosses entire body diagonal
    //    0.42-1.00  Recovery: slow return from aggressive overextension
    //
    //  Total arc: ~65° main arm swing
    //  Off-hand stays down: supportScale maxes at 0.10
    //  Body twist: -0.22 lean at peak — violent commitment
    //  Fastest acceleration of any form: 0 to 0.88 mainScale in 0.26s
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,      0,      0     ),
            // Micro wind-up: barely perceptible — Juyo hides intention
            kf(0.05F,   -3,   -4,   -2,   0.002F, 0.002F, 0.004F,-0.04F, -0.02F,  0,      0.02F,  0     ),
            // Set-up: subtle body coil — still no telegraph
            kf(0.11F,   -6,   -8,   -3,   0.004F, 0.004F, 0.008F,-0.14F, -0.04F, -0.02F, -0.04F,  0.02F ),
            // EXPLOSIVE STRIKE: sudden diagonal slash — from nowhere
            kf(0.26F,  -44,   20,  -26,   0.008F,-0.010F,-0.024F, 0.88F,  0.10F, -0.18F,  0.14F, -0.10F ),
            // Cross-body follow: blade continues across entire body
            kf(0.42F,  -36,   36,  -22,   0.003F,-0.016F,-0.034F, 0.72F,  0.08F, -0.22F,  0.12F, -0.08F ),
            // Deceleration: recovering from violent overextension
            kf(0.60F,  -18,   18,   -8,   0,     -0.008F,-0.014F, 0.30F,  0.04F, -0.10F,  0.05F, -0.03F ),
            // Recovery
            kf(0.78F,   -8,    6,   -3,   0,     -0.003F,-0.006F, 0.10F,  0.02F, -0.04F,  0.02F, -0.01F ),
            // Settling
            kf(0.90F,   -3,    2,   -1,   0,     -0.001F,-0.002F, 0.04F,  0.01F, -0.01F,  0.01F,  0     ),
            // Return to idle
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.01F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  FIRST-PERSON ITEM PROFILES
    // ═══════════════════════════════════════════════════════════════════

    static final ItemProfile ITEM = new ItemProfile(
            -20F, 4F, -16F,      // blade high, angled for cross-body readiness
            0.02F, 0.05F, -0.01F,
            0.98F
    );

    static final ItemProfile BLOCK_ITEM = new ItemProfile(
            -14F, 3F, -10F,      // blade forward-aggressive for power block
            0.02F, 0.05F, -0.02F,
            0.98F
    );
}
