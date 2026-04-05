package client.renderer.lightsaber;

import static client.renderer.lightsaber.LightsaberAnimData.*;

/**
 * Form III · Soresu — "Way of the Mynock"
 *
 * <p>The ultimate defensive form. Soresu prioritizes tight, economical movements
 * that create an impenetrable guard. The blade stays close to the body, covering
 * vital areas. Attacks are compact counter-strikes — wait for the opponent to
 * overextend, then exploit the opening.</p>
 *
 * <h3>Canonical Practitioners</h3>
 * <ul>
 *   <li>Obi-Wan Kenobi (Soresu master, perfected after Qui-Gon's death)</li>
 *   <li>Kanan Jarrus</li>
 * </ul>
 *
 * <h3>Idle Stance</h3>
 * <p>One-handed defensive grip with blade close to the body, nearly vertical
 * near the face. The free hand is raised with palm forward in the iconic
 * Obi-Wan "come at me" beckoning gesture — inviting the opponent to attack
 * first. Compact posture, minimal exposure.</p>
 *
 * <h3>Attack Pattern</h3>
 * <p>Tight circular parry motion followed by a compact counter-strike. The blade
 * traces a small defensive circle before snapping forward. Off-hand stays raised
 * throughout — Soresu never abandons its guard.
 * Peak mainScale 0.64 — the smallest attack arc of any form, reflecting
 * Soresu's philosophy of minimum necessary movement.</p>
 *
 * <h3>Block Stance</h3>
 * <p>Tight center guard, two-handed brace. Both hands grip the hilt for maximum
 * deflection stability — Soresu's block is its strongest move.</p>
 */
final class FormSoresu {

    private FormSoresu() {}

    // ═══════════════════════════════════════════════════════════════════
    //  IDLE STANCE — One-handed defensive, off-hand raised beckoning
    //  Obi-Wan stance: blade close to body, nearly vertical
    //  Free hand raised palm-forward, inviting the opponent to attack
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile IDLE = new ArmProfile(
            -0.88F, -0.16F, -0.02F,        // main arm: forward, blade defensive and close
            -1.76F, -0.24F, -0.16F,         // off-hand: RAISED UP (-1.76 = well above horizontal) and outward
            0.22F, 0.04F,                  // main positional: moderate inward push
            -0.06F, -0.06F,                 // support positional: outward (not toward hilt)
            0.03F, 0.18F,                  // idle sway: subtle — Soresu is patient and still
            -1.18F, -0.20F, -0.04F,         // attack vector main: tight forward counter
            -1.76F, -0.24F, -0.16F,         // attack vector support: stays raised
            false,                          // ONE-HANDED — off-hand beckoning, not on hilt
            0.88F, 1.00F,
            -0.01F                           // body lean: nearly upright, centered balance
    );

    // ═══════════════════════════════════════════════════════════════════
    //  BLOCK STANCE — Tight center guard, two-handed brace
    //  Both hands grip hilt for maximum deflection stability
    //  Soresu's block IS its primary technique
    // ═══════════════════════════════════════════════════════════════════

    static final ArmProfile BLOCK = new ArmProfile(
            -1.12F, -0.08F, -0.02F,
            -1.16F, 0.44F, 0.06F,           // support arm braces the hilt for tight defense
            0.22F, 0.04F,
            0.42F, 0.04F,
            0.018F, 0.14F,
            -1.18F, -0.12F, -0.04F,
            -1.14F, 0.42F, 0.04F,
            true,                           // TWO-HANDED when blocking
            0.92F, 1.00F,
            0.00F
    );

    // ═══════════════════════════════════════════════════════════════════
    //  ATTACK KEYFRAMES — Circular parry into compact counter-strike
    //
    //  Timeline:
    //    0.00-0.22  Parry circle: blade traces defensive arc
    //    0.22-0.36  Transition: parry redirects into counter-attack
    //    0.36-0.50  COUNTER-STRIKE: quick precise snap forward
    //              mainScale peaks at 0.64, support stays at 0.08
    //    0.50-1.00  Recovery: immediate return to guard
    //
    //  Total arc: ~37° main arm — tightest of all forms
    //  Note the parry phase (0.10-0.36) with itemRoll +16° to -8°:
    //    this creates the circular deflection motion before the counter
    //  Off-hand stays raised: supportScale maxes at 0.08
    // ═══════════════════════════════════════════════════════════════════

    static final Keyframe[] ATTACK = {
            kf(0.00F,    0,    0,    0,   0,      0,      0,      0,      0,      0,      0,      0     ),
            // Parry start: blade begins circular motion
            kf(0.10F,   -5,   -6,    8,   0.003F, 0.004F, 0.008F,-0.08F, -0.04F,  0,      0.02F,  0     ),
            // Parry peak: blade at maximum deflection angle (itemRoll +16°)
            kf(0.22F,  -10,  -14,   16,   0.006F, 0.008F, 0.016F,-0.28F, -0.06F, -0.03F, -0.04F,  0.02F ),
            // Transition: parry redirects into counter-attack
            kf(0.36F,   -8,   -6,   -8,   0.004F, 0.003F, 0.006F,-0.12F, -0.04F, -0.03F,  0.02F,  0     ),
            // COUNTER-STRIKE: quick snap forward — exploit the opening
            kf(0.50F,  -36,   12,  -14,   0.008F,-0.008F,-0.018F, 0.64F,  0.08F, -0.06F,  0.08F, -0.04F ),
            // Immediate return to guard
            kf(0.66F,  -20,    6,   -6,   0.003F,-0.003F,-0.008F, 0.28F,  0.04F, -0.03F,  0.03F, -0.02F ),
            // Recovery
            kf(0.84F,   -6,    2,   -2,   0,     -0.001F,-0.003F, 0.08F,  0.02F, -0.01F,  0.01F,  0     ),
            // Idle
            kf(1.00F,    0,    0,    0,   0,      0,      0,      0.02F,  0.01F,  0,       0,      0     ),
    };

    // ═══════════════════════════════════════════════════════════════════
    //  FIRST-PERSON ITEM PROFILES
    // ═══════════════════════════════════════════════════════════════════

    static final ItemProfile ITEM = new ItemProfile(
            -4F, -10F, -6F,      // blade close to center, defensive angle
            0.01F, 0.05F, 0.01F,
            0.94F
    );

    static final ItemProfile BLOCK_ITEM = new ItemProfile(
            -2F, -4F, -3F,       // blade nearly vertical, tight guard
            0.00F, 0.06F, -0.01F,
            0.94F
    );
}
