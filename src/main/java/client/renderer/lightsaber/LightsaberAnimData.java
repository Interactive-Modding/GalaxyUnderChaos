package client.renderer.lightsaber;

/**
 * Shared animation data types used by lightsaber form definitions and the stance controller.
 *
 * <p>These records define the structure of idle poses, attack keyframes, and
 * first-person item orientations. Each lightsaber form (Shii-Cho through Juyo)
 * provides its own instances via its dedicated Form class.</p>
 *
 * <h3>Coordinate System (Minecraft HumanoidModel)</h3>
 * <ul>
 *   <li><b>xRot</b> (pitch): negative = arm forward/up, positive = arm backward/down</li>
 *   <li><b>yRot</b> (yaw): positive = arm inward (toward body center), mirrored for left arm</li>
 *   <li><b>zRot</b> (roll): positive = arm lifted away from body, mirrored for left arm</li>
 * </ul>
 *
 * <h3>Keyframe Interpolation</h3>
 * <p>Attack animations use smoothstep interpolation: {@code t*t*(3-2*t)} between
 * consecutive keyframes. The {@code mainScale} and {@code supportScale} fields are
 * multiplied against the ArmProfile's attack vectors to produce the final arm rotation.</p>
 *
 * <p>Formula: {@code totalArmRotation = idleRotation + (attackProfile * keyframe.scale + keyframe.offset)}</p>
 */
final class LightsaberAnimData {

    private LightsaberAnimData() {}

    // ═══════════════════════════════════════════════════════════════════
    //  KEYFRAME — One point in an attack animation timeline
    // ═══════════════════════════════════════════════════════════════════

    /**
     * A single keyframe in an attack animation sequence.
     *
     * @param time          normalized time [0..1] within the attack duration
     * @param itemPitch     first-person blade pitch delta (degrees)
     * @param itemYaw       first-person blade yaw delta (degrees)
     * @param itemRoll      first-person blade roll delta (degrees)
     * @param itemTX        first-person X translation
     * @param itemTY        first-person Y translation
     * @param itemTZ        first-person Z translation
     * @param mainScale     third-person main arm rotation multiplier (0 = idle, 1 = full attack vector)
     * @param supportScale  third-person support arm rotation multiplier
     * @param bodyLean      torso forward lean (negative = forward)
     * @param bodyYaw       torso horizontal rotation
     * @param bodyTwist     torso twist/roll
     */
    record Keyframe(
            float time,
            float itemPitch, float itemYaw, float itemRoll,
            float itemTX, float itemTY, float itemTZ,
            float mainScale, float supportScale,
            float bodyLean, float bodyYaw, float bodyTwist
    ) {}

    // ═══════════════════════════════════════════════════════════════════
    //  ARM PROFILE — Third-person idle/block arm positioning
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Defines the third-person arm positioning for a lightsaber stance.
     *
     * <p>Contains both the idle arm rotations (mainX/Y/Z, supportX/Y/Z) and
     * the attack additive vectors (attackMainX/Y/Z, attackSupportX/Y/Z) that
     * get multiplied by keyframe mainScale/supportScale during attacks.</p>
     *
     * @param mainX            main arm pitch (negative = forward)
     * @param mainY            main arm yaw (negative = outward, positive = inward)
     * @param mainZ            main arm roll
     * @param supportX         support arm pitch
     * @param supportY         support arm yaw
     * @param supportZ         support arm roll
     * @param mainInward       positional X offset pushing main arm toward body center
     * @param mainLift         positional Y offset lifting main arm
     * @param supportInward    positional X offset for support arm
     * @param supportLift      positional Y offset for support arm
     * @param idleAmplitude    amplitude of idle breathing/sway animation
     * @param idleFrequency    frequency of idle breathing/sway animation
     * @param attackMainX      attack additive pitch for main arm
     * @param attackMainY      attack additive yaw for main arm
     * @param attackMainZ      attack additive roll for main arm
     * @param attackSupportX   attack additive pitch for support arm
     * @param attackSupportY   attack additive yaw for support arm
     * @param attackSupportZ   attack additive roll for support arm
     * @param twoHanded        true if support arm grips the same hilt as main arm
     * @param blend            lerp blend factor for standing pose
     * @param crouchBlend      lerp blend factor when crouching
     * @param bodyLean         resting torso forward lean
     */
    record ArmProfile(
            float mainX, float mainY, float mainZ,
            float supportX, float supportY, float supportZ,
            float mainInward, float mainLift,
            float supportInward, float supportLift,
            float idleAmplitude, float idleFrequency,
            float attackMainX, float attackMainY, float attackMainZ,
            float attackSupportX, float attackSupportY, float attackSupportZ,
            boolean twoHanded,
            float blend, float crouchBlend,
            float bodyLean
    ) {}

    // ═══════════════════════════════════════════════════════════════════
    //  ITEM PROFILE — First-person blade/item orientation
    // ═══════════════════════════════════════════════════════════════════

    /**
     * First-person item orientation for a lightsaber stance.
     *
     * @param rotateX     blade pitch rotation (degrees)
     * @param rotateY     blade yaw rotation (degrees)
     * @param rotateZ     blade roll rotation (degrees)
     * @param translateX  hand X offset
     * @param translateY  hand Y offset
     * @param translateZ  hand Z offset
     * @param scale       overall model scale factor
     */
    record ItemProfile(
            float rotateX, float rotateY, float rotateZ,
            float translateX, float translateY, float translateZ,
            float scale
    ) {}

    // ═══════════════════════════════════════════════════════════════════
    //  ATTACK SAMPLE — Interpolated result from keyframe sampling
    // ═══════════════════════════════════════════════════════════════════

    record AttackSample(
            float mainScale, float supportScale,
            float mainX, float mainY, float mainZ,
            float supportX, float supportY, float supportZ,
            float bodyLean, float bodyYaw, float bodyTwist,
            float itemPitch, float itemYaw, float itemRoll,
            float itemTranslateX, float itemTranslateY, float itemTranslateZ
    ) {}

    // ═══════════════════════════════════════════════════════════════════
    //  WIELD STYLE & CONTEXT
    // ═══════════════════════════════════════════════════════════════════

    enum WieldStyle {
        SINGLE, DUAL, STAFF
    }

    // ═══════════════════════════════════════════════════════════════════
    //  KEYFRAME FACTORY
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Convenience factory for creating Keyframe instances with positional args.
     * Keeps the keyframe arrays compact and readable.
     */
    static Keyframe kf(float t,
                       float ip, float iy, float ir,
                       float tx, float ty, float tz,
                       float ms, float ss,
                       float bl, float by, float bt) {
        return new Keyframe(t, ip, iy, ir, tx, ty, tz, ms, ss, bl, by, bt);
    }
}
