package client.renderer.lightsaber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.item.DoubleLightsaberItem;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.lightsaber.LightsaberForm;
import server.galaxyunderchaos.lightsaber.LightsaberFormProvider;

import java.util.Map;

import static client.renderer.lightsaber.LightsaberAnimData.*;

/**
 * Centralized pose, slash, and block controller for lightsabers.
 *
 * <p>This controller handles all third-person arm posing and first-person item
 * orientation for lightsaber combat. Animation data for each canonical form
 * is defined in its own dedicated file:</p>
 * <ul>
 *     <li>{@link FormShiiCho}  — Form I:   Wide horizontal sweeps, two-handed</li>
 *     <li>{@link FormMakashi}  — Form II:  One-handed fencing, precision thrusts</li>
 *     <li>{@link FormSoresu}   — Form III: Tight defensive parry, compact counter</li>
 *     <li>{@link FormAtaru}    — Form IV:  Acrobatic overhead arc, two-handed</li>
 *     <li>{@link FormShien}    — Form V:   Power overhead chop, two-handed</li>
 *     <li>{@link FormNiman}    — Form VI:  Balanced diagonal, draws from all</li>
 *     <li>{@link FormJuyo}     — Form VII: Explosive cross-body, minimal telegraph</li>
 * </ul>
 *
 * <p>Default/fallback profiles and wield-style animations (Staff, Dual) are in
 * {@link FormDefaults}. Shared data types are in {@link LightsaberAnimData}.</p>
 */
public final class LightsaberStanceController {

    // First-person dampening — vanilla also applies its own swing rotation,
    // so our contribution must be modest to avoid compounding into an extreme pose.
    private static final float FP_ATTACK_DAMPEN = 0.40F;

    // ═══════════════════════════════════════════════════════════════════
    //  FORM DATA REGISTRIES
    //  Maps lightsaber forms to their animation data from per-form files.
    // ═══════════════════════════════════════════════════════════════════

    private static final Map<LightsaberForm, Keyframe[]> FORM_ATTACK = Map.of(
            LightsaberForm.SHII_CHO, FormShiiCho.ATTACK,
            LightsaberForm.MAKASHI,  FormMakashi.ATTACK,
            LightsaberForm.SORESU,   FormSoresu.ATTACK,
            LightsaberForm.ATARU,    FormAtaru.ATTACK,
            LightsaberForm.SHIEN,    FormShien.ATTACK,
            LightsaberForm.NIMAN,    FormNiman.ATTACK,
            LightsaberForm.JUYO,     FormJuyo.ATTACK
    );

    private static final Map<LightsaberForm, ArmProfile> FORM_SINGLE = Map.of(
            LightsaberForm.SHII_CHO, FormShiiCho.IDLE,
            LightsaberForm.MAKASHI,  FormMakashi.IDLE,
            LightsaberForm.SORESU,   FormSoresu.IDLE,
            LightsaberForm.ATARU,    FormAtaru.IDLE,
            LightsaberForm.SHIEN,    FormShien.IDLE,
            LightsaberForm.NIMAN,    FormNiman.IDLE,
            LightsaberForm.JUYO,     FormJuyo.IDLE
    );

    private static final Map<LightsaberForm, ArmProfile> FORM_BLOCK_SINGLE = Map.of(
            LightsaberForm.SHII_CHO, FormShiiCho.BLOCK,
            LightsaberForm.MAKASHI,  FormMakashi.BLOCK,
            LightsaberForm.SORESU,   FormSoresu.BLOCK,
            LightsaberForm.ATARU,    FormAtaru.BLOCK,
            LightsaberForm.SHIEN,    FormShien.BLOCK,
            LightsaberForm.NIMAN,    FormNiman.BLOCK,
            LightsaberForm.JUYO,     FormJuyo.BLOCK
    );

    private static final Map<LightsaberForm, ItemProfile> FORM_ITEMS = Map.of(
            LightsaberForm.SHII_CHO, FormShiiCho.ITEM,
            LightsaberForm.MAKASHI,  FormMakashi.ITEM,
            LightsaberForm.SORESU,   FormSoresu.ITEM,
            LightsaberForm.ATARU,    FormAtaru.ITEM,
            LightsaberForm.SHIEN,    FormShien.ITEM,
            LightsaberForm.NIMAN,    FormNiman.ITEM,
            LightsaberForm.JUYO,     FormJuyo.ITEM
    );

    private static final Map<LightsaberForm, ItemProfile> FORM_BLOCK_ITEMS = Map.of(
            LightsaberForm.SHII_CHO, FormShiiCho.BLOCK_ITEM,
            LightsaberForm.MAKASHI,  FormMakashi.BLOCK_ITEM,
            LightsaberForm.SORESU,   FormSoresu.BLOCK_ITEM,
            LightsaberForm.ATARU,    FormAtaru.BLOCK_ITEM,
            LightsaberForm.SHIEN,    FormShien.BLOCK_ITEM,
            LightsaberForm.NIMAN,    FormNiman.BLOCK_ITEM,
            LightsaberForm.JUYO,     FormJuyo.BLOCK_ITEM
    );

    // ═══════════════════════════════════════════════════════════════════
    //  PRIVATE CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════════

    private LightsaberStanceController() {}

    // ═══════════════════════════════════════════════════════════════════
    //  SHOULD ANIMATE
    // ═══════════════════════════════════════════════════════════════════

    public static boolean shouldAnimate(LivingEntity entity, ItemStack stack) {
        if (!(stack.getItem() instanceof LightsaberItem saber)) {
            return false;
        }
        if (!saber.isActive(stack)) {
            return false;
        }
        if (entity.isFallFlying() || entity.isSwimming() || entity.isAutoSpinAttack() || entity.isPassenger()) {
            return false;
        }
        return !entity.isSleeping();
    }

    // ═══════════════════════════════════════════════════════════════════
    //  THIRD PERSON — HUMANOID POSE
    // ═══════════════════════════════════════════════════════════════════

    public static void applyHumanoidPose(HumanoidModel<?> model, LivingEntity entity, float ageInTicks) {
        if (isLocalFirstPerson(entity)) {
            return;
        }

        SaberContext context = resolveContext(entity);
        if (context == null || !shouldAnimate(entity, context.primaryStack())) {
            return;
        }

        LightsaberForm form = getSelectedForm(entity);
        boolean blocking = isBlocking(entity, context);

        // Tick the swing tracker for this entity
        LightsaberSwingTracker.ensureTicked(entity);
        LightsaberSwingTracker.setFormDuration(entity, form);

        ArmProfile profile = resolveArmProfile(context, form, blocking);
        float idleWave = Mth.sin(ageInTicks * profile.idleFrequency()) * profile.idleAmplitude();
        float blend = entity.isCrouching() ? profile.crouchBlend() : profile.blend();

        // Apply idle arm pose
        applyArm(model, context.primaryArm(), profile.mainX() + idleWave, profile.mainY(), profile.mainZ(),
                profile.mainInward(), profile.mainLift(), blend);
        float supportBlend = profile.twoHanded() ? blend : blend * 0.90F;
        applySupportArm(model, opposite(context.primaryArm()), profile.supportX() - idleWave * 0.20F, profile.supportY(), profile.supportZ(),
                profile.supportInward(), profile.supportLift(), supportBlend, context.style(), profile.twoHanded());

        // Apply attack animation (keyframe-driven)
        float partialTick = ageInTicks - (float) Math.floor(ageInTicks);
        float attackProgress = blocking ? 0.0F : LightsaberSwingTracker.getProgress(entity, partialTick);

        if (attackProgress > 0.0F) {
            AttackSample sample = sampleAttack(attackProgress, context.style(), form);
            addAttack(model, context.primaryArm(),
                    profile.attackMainX() * sample.mainScale() + sample.mainX(),
                    profile.attackMainY() * sample.mainScale() + sample.mainY(),
                    profile.attackMainZ() * sample.mainScale() + sample.mainZ());
            addAttack(model, opposite(context.primaryArm()),
                    profile.attackSupportX() * sample.supportScale() + sample.supportX(),
                    profile.attackSupportY() * sample.supportScale() + sample.supportY(),
                    profile.attackSupportZ() * sample.supportScale() + sample.supportZ());

            float targetBodyY = Mth.clamp(mirror(context.primaryArm(), sample.bodyYaw()) * 0.22F, -0.08F, 0.08F);
            float targetBodyZ = Mth.clamp(sample.bodyTwist() * 0.12F, -0.05F, 0.05F);
            float targetBodyX = Mth.clamp(profile.bodyLean() + sample.bodyLean() * 0.22F, -0.09F, 0.05F);
            model.body.yRot = Mth.lerp(blend * 0.14F, model.body.yRot, targetBodyY);
            model.body.zRot = Mth.lerp(blend * 0.12F, model.body.zRot, targetBodyZ);
            model.body.xRot = Mth.lerp(blend * 0.16F, model.body.xRot, targetBodyX);
        } else {
            model.body.xRot = Mth.lerp(blend * 0.12F, model.body.xRot, Mth.clamp(profile.bodyLean(), -0.05F, 0.03F));
            model.body.yRot = Mth.lerp(blend * 0.10F, model.body.yRot, 0.0F);
            model.body.zRot = Mth.lerp(blend * 0.10F, model.body.zRot, 0.0F);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  FIRST PERSON — HAND / ITEM POSE
    // ═══════════════════════════════════════════════════════════════════

    public static void applyFirstPersonPose(PoseStack poseStack,
                                            LivingEntity entity,
                                            ItemStack stack,
                                            InteractionHand hand,
                                            float partialTick,
                                            float equipProgress) {
        if (!shouldAnimate(entity, stack)) {
            return;
        }

        SaberContext context = resolveContext(entity);
        WieldStyle style = resolveStyle(entity, stack);
        LightsaberForm form = getSelectedForm(entity);
        boolean blocking = context != null && isBlocking(entity, context);

        // Tick tracker and get extended progress
        LightsaberSwingTracker.ensureTicked(entity);
        LightsaberSwingTracker.setFormDuration(entity, form);

        ItemProfile profile = resolveItemProfile(style, form, blocking, hand == InteractionHand.OFF_HAND);
        boolean leftHand = hand == InteractionHand.OFF_HAND;
        float handSign = leftHand ? -1.0F : 1.0F;

        float equip = Mth.clamp(equipProgress, 0.0F, 1.0F);
        float equipOffset = Mth.sin(equip * Mth.HALF_PI);

        // Apply idle form stance
        poseStack.translate(handSign * profile.translateX(), profile.translateY() - equipOffset * 0.03F, profile.translateZ());
        poseStack.mulPose(Axis.YP.rotationDegrees(profile.rotateY() * handSign));
        poseStack.mulPose(Axis.ZP.rotationDegrees(profile.rotateZ() * handSign));
        poseStack.mulPose(Axis.XP.rotationDegrees(profile.rotateX()));

        // Apply attack or block animation
        if (blocking) {
            float breathe = Mth.sin((entity.tickCount + partialTick) * 0.08F) * 1.2F;
            poseStack.mulPose(Axis.XP.rotationDegrees(breathe));
        } else {
            float attackProgress = LightsaberSwingTracker.getProgress(entity, partialTick);
            if (attackProgress > 0.0F) {
                AttackSample sample = sampleAttack(attackProgress, style, form);
                float d = FP_ATTACK_DAMPEN;
                poseStack.mulPose(Axis.XP.rotationDegrees(sample.itemPitch() * d));
                poseStack.mulPose(Axis.YP.rotationDegrees(sample.itemYaw() * d * handSign));
                poseStack.mulPose(Axis.ZP.rotationDegrees(sample.itemRoll() * d * handSign));
                poseStack.translate(
                        handSign * sample.itemTranslateX() * d,
                        sample.itemTranslateY() * d,
                        sample.itemTranslateZ() * d
                );
            }
        }

        // Wield-style final adjustments
        if (style == WieldStyle.STAFF) {
            poseStack.mulPose(Axis.YP.rotationDegrees(8.0F * handSign * (1.0F - equip)));
            poseStack.mulPose(Axis.ZP.rotationDegrees(6.0F * handSign * (1.0F - equip)));
        } else if (style == WieldStyle.DUAL) {
            poseStack.mulPose(Axis.YP.rotationDegrees(5.0F * handSign));
        }

        poseStack.scale(profile.scale(), profile.scale(), profile.scale());
    }

    // ═══════════════════════════════════════════════════════════════════
    //  KEYFRAME INTERPOLATION
    // ═══════════════════════════════════════════════════════════════════

    private static AttackSample sampleAttack(float progress, WieldStyle style, LightsaberForm form) {
        Keyframe[] keyframes;
        if (style == WieldStyle.STAFF) {
            keyframes = FormDefaults.STAFF_ATTACK;
        } else if (style == WieldStyle.DUAL) {
            keyframes = FormDefaults.DUAL_ATTACK;
        } else {
            keyframes = form != null ? FORM_ATTACK.getOrDefault(form, FormDefaults.DEFAULT_ATTACK) : FormDefaults.DEFAULT_ATTACK;
        }
        float t = Mth.clamp(progress, 0.0F, 1.0F);

        // Find surrounding keyframes
        int idx = 0;
        for (int i = 0; i < keyframes.length - 1; i++) {
            if (t >= keyframes[i].time() && t <= keyframes[i + 1].time()) {
                idx = i;
                break;
            }
            if (i == keyframes.length - 2) {
                idx = i;
            }
        }

        Keyframe a = keyframes[idx];
        Keyframe b = keyframes[Math.min(idx + 1, keyframes.length - 1)];
        float segLen = b.time() - a.time();
        float localT = segLen > 0.001F ? (t - a.time()) / segLen : 0.0F;
        float s = smooth(localT);

        float mainScale = Mth.lerp(s, a.mainScale(), b.mainScale());
        float supportScale = Mth.lerp(s, a.supportScale(), b.supportScale());
        float itemPitch = Mth.lerp(s, a.itemPitch(), b.itemPitch());
        float itemYaw = Mth.lerp(s, a.itemYaw(), b.itemYaw());
        float itemRoll = Mth.lerp(s, a.itemRoll(), b.itemRoll());
        float itemTX = Mth.lerp(s, a.itemTX(), b.itemTX());
        float itemTY = Mth.lerp(s, a.itemTY(), b.itemTY());
        float itemTZ = Mth.lerp(s, a.itemTZ(), b.itemTZ());
        float bodyLean = Mth.lerp(s, a.bodyLean(), b.bodyLean());
        float bodyYaw = Mth.lerp(s, a.bodyYaw(), b.bodyYaw());
        float bodyTwist = Mth.lerp(s, a.bodyTwist(), b.bodyTwist());

        // Apply wield-style modifiers to arm offsets
        float mainX = 0.0F, mainY = 0.0F, mainZ = 0.0F;
        float supportX = 0.0F, supportY = 0.0F, supportZ = 0.0F;

        switch (style) {
            case STAFF -> {
                mainY += 0.18F * mainScale;
                mainZ += 0.20F * mainScale;
                supportY -= 0.28F * supportScale;
                supportZ -= 0.08F * supportScale;
                bodyYaw += 0.18F * mainScale;
                bodyTwist -= 0.12F * mainScale;
            }
            case DUAL -> {
                mainY -= 0.12F * mainScale;
                mainZ -= 0.05F * mainScale;
                supportY += 0.16F * supportScale;
                supportZ += 0.08F * supportScale;
                bodyYaw -= 0.06F * mainScale;
                bodyTwist += 0.08F * mainScale;
            }
            case SINGLE -> {
                mainY -= 0.08F * mainScale;
                supportY += 0.06F * supportScale;
                bodyYaw -= 0.05F * mainScale;
            }
        }

        // Apply form-specific arm/item modifiers
        if (form != null) {
            switch (form) {
                case MAKASHI -> {
                    itemYaw -= 10.0F * Math.abs(mainScale);
                    mainY -= 0.06F * mainScale;
                    supportScale *= 0.82F;
                }
                case SORESU -> {
                    itemPitch += 8.0F * Math.abs(mainScale);
                    bodyLean *= 0.65F;
                    supportScale *= 1.08F;
                }
                case ATARU -> {
                    itemPitch -= 12.0F * Math.abs(mainScale);
                    itemTZ -= 0.02F * Math.abs(mainScale);
                    mainX -= 0.16F * mainScale;
                    bodyLean -= 0.03F * Math.abs(mainScale);
                }
                case SHIEN -> {
                    itemYaw += 8.0F * Math.abs(mainScale);
                    supportY += 0.10F * supportScale;
                    bodyYaw += 0.06F * mainScale;
                }
                case NIMAN -> {
                    itemPitch += 3.0F * Math.abs(mainScale);
                    bodyLean *= 0.85F;
                }
                case JUYO -> {
                    itemPitch -= 8.0F * Math.abs(mainScale);
                    itemRoll -= 6.0F * Math.abs(mainScale);
                    mainZ -= 0.10F * mainScale;
                    bodyLean -= 0.03F * Math.abs(mainScale);
                }
                default -> {}
            }
        }

        return new AttackSample(mainScale, supportScale,
                mainX, mainY, mainZ,
                supportX, supportY, supportZ,
                bodyLean, bodyYaw, bodyTwist,
                itemPitch, itemYaw, itemRoll,
                itemTX, itemTY, itemTZ);
    }

    // ═══════════════════════════════════════════════════════════════════
    //  PROFILE RESOLUTION
    // ═══════════════════════════════════════════════════════════════════

    private static ArmProfile resolveArmProfile(SaberContext context, LightsaberForm form, boolean blocking) {
        if (blocking) {
            ArmProfile blockBase = form != null
                    ? FORM_BLOCK_SINGLE.getOrDefault(form, blockingProfile(FormDefaults.DEFAULT_SINGLE, context.style()))
                    : blockingProfile(FormDefaults.DEFAULT_SINGLE, context.style());
            return switch (context.style()) {
                case STAFF -> deriveStaffBlockProfile(blockBase);
                case DUAL -> deriveDualBlockProfile(blockBase);
                case SINGLE -> blockBase;
            };
        }
        ArmProfile base = form != null ? FORM_SINGLE.getOrDefault(form, FormDefaults.DEFAULT_SINGLE) : FormDefaults.DEFAULT_SINGLE;
        return switch (context.style()) {
            case STAFF -> deriveStaffProfile(base, form);
            case DUAL -> deriveDualProfile(base, form);
            case SINGLE -> base;
        };
    }

    private static ItemProfile resolveItemProfile(WieldStyle style, LightsaberForm form, boolean blocking, boolean offHand) {
        if (blocking) {
            ItemProfile blockBase = form != null
                    ? FORM_BLOCK_ITEMS.getOrDefault(form, new ItemProfile(-6F, -6F, -4F, 0.01F, 0.06F, -0.01F, 0.95F))
                    : new ItemProfile(-6F, -6F, -4F, 0.01F, 0.06F, -0.01F, 0.95F);
            return switch (style) {
                case STAFF -> deriveStaffBlockItemProfile(blockBase, offHand);
                case DUAL -> deriveDualBlockItemProfile(blockBase, offHand);
                case SINGLE -> blockBase;
            };
        }
        ItemProfile base = form != null ? FORM_ITEMS.getOrDefault(form, FormDefaults.DEFAULT_SINGLE_ITEM) : FormDefaults.DEFAULT_SINGLE_ITEM;
        return switch (style) {
            case STAFF -> deriveStaffItemProfile(base, form);
            case DUAL -> deriveDualItemProfile(base, form, offHand);
            case SINGLE -> base;
        };
    }

    // ═══════════════════════════════════════════════════════════════════
    //  DERIVE METHODS — STAFF / DUAL / BLOCK PROFILES
    // ═══════════════════════════════════════════════════════════════════

    private static ArmProfile deriveStaffProfile(ArmProfile base, LightsaberForm form) {
        return new ArmProfile(
                -1.34F, -0.04F, -0.10F,
                -1.38F, -0.02F, 0.10F,
                0.14F, 0.02F,
                0.14F, 0.02F,
                base.idleAmplitude(), base.idleFrequency(),
                -1.58F, -0.08F, -0.14F,
                -1.62F, -0.04F, 0.12F,
                true,
                Math.max(base.blend(), 0.84F),
                Math.max(base.crouchBlend(), 0.98F),
                base.bodyLean() - 0.01F
        );
    }

    private static ArmProfile deriveDualProfile(ArmProfile base, LightsaberForm form) {
        float makashiBias = form == LightsaberForm.MAKASHI ? 0.14F : 0.0F;
        return new ArmProfile(
                -0.88F + makashiBias, -0.62F, -0.20F,
                -0.92F + makashiBias, 0.64F, 0.20F,
                0.22F, 0.00F,
                0.22F, 0.00F,
                base.idleAmplitude(), base.idleFrequency(),
                -1.16F, -0.76F, -0.24F,
                -1.02F, 0.78F, 0.22F,
                false,
                Math.max(base.blend(), 0.80F),
                Math.max(base.crouchBlend(), 0.96F),
                -0.04F
        );
    }

    private static ArmProfile deriveStaffBlockProfile(ArmProfile base) {
        return new ArmProfile(
                -1.48F, -0.06F, -0.08F,
                -1.52F, -0.04F, 0.08F,
                0.16F, 0.02F,
                0.16F, 0.02F,
                base.idleAmplitude() * 0.35F, base.idleFrequency(),
                base.attackMainX(), base.attackMainY(), base.attackMainZ(),
                base.attackSupportX(), base.attackSupportY(), base.attackSupportZ(),
                true,
                Math.max(base.blend(), 0.92F),
                Math.max(base.crouchBlend(), 1.00F),
                -0.02F
        );
    }

    private static ArmProfile deriveDualBlockProfile(ArmProfile base) {
        return new ArmProfile(
                -1.20F, -0.32F, -0.18F,
                -1.20F, 0.32F, 0.18F,
                0.12F, 0.02F,
                0.12F, 0.02F,
                base.idleAmplitude() * 0.35F, base.idleFrequency(),
                base.attackMainX(), base.attackMainY(), base.attackMainZ(),
                base.attackSupportX(), base.attackSupportY(), base.attackSupportZ(),
                false,
                Math.max(base.blend(), 0.92F),
                Math.max(base.crouchBlend(), 1.00F),
                -0.02F
        );
    }

    private static ArmProfile blockingProfile(ArmProfile base, WieldStyle style) {
        return switch (style) {
            case SINGLE -> new ArmProfile(
                    -1.36F, -0.10F, -0.02F,
                    -1.22F, 0.28F, 0.02F,
                    0.24F, 0.02F,
                    0.18F, 0.06F,
                    base.idleAmplitude() * 0.45F, base.idleFrequency(),
                    base.attackMainX(), base.attackMainY(), base.attackMainZ(),
                    base.attackSupportX(), base.attackSupportY(), base.attackSupportZ(),
                    true,
                    Math.max(base.blend(), 0.90F),
                    Math.max(base.crouchBlend(), 1.00F),
                    -0.02F
            );
            case STAFF -> deriveStaffBlockProfile(base);
            case DUAL -> deriveDualBlockProfile(base);
        };
    }

    private static ItemProfile deriveStaffItemProfile(ItemProfile base, LightsaberForm form) {
        return new ItemProfile(-6F, 32F, -60F, 0.06F, 0.03F, 0.00F, 1.00F);
    }

    private static ItemProfile deriveDualItemProfile(ItemProfile base, LightsaberForm form, boolean offHand) {
        float handSign = offHand ? -1.0F : 1.0F;
        return new ItemProfile(
                base.rotateX() + (form == LightsaberForm.MAKASHI ? 8.0F : 2.0F),
                -28.0F + handSign * 3.0F,
                -6.0F * handSign,
                0.02F,
                0.04F,
                0.01F,
                0.93F
        );
    }

    private static ItemProfile deriveStaffBlockItemProfile(ItemProfile base, boolean offHand) {
        float handSign = offHand ? -1.0F : 1.0F;
        return new ItemProfile(2F, 10F, -22F * handSign, 0.04F, 0.06F, -0.03F, base.scale());
    }

    private static ItemProfile deriveDualBlockItemProfile(ItemProfile base, boolean offHand) {
        float handSign = offHand ? -1.0F : 1.0F;
        return new ItemProfile(-3F, -14F, -14F * handSign, 0.01F, 0.06F, -0.02F, base.scale());
    }

    // ═══════════════════════════════════════════════════════════════════
    //  HELPER METHODS
    // ═══════════════════════════════════════════════════════════════════

    private static boolean isLocalFirstPerson(LivingEntity entity) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.player != null
                && entity == minecraft.player
                && minecraft.options.getCameraType().isFirstPerson();
    }

    private static LightsaberForm getSelectedForm(LivingEntity entity) {
        return entity.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY)
                .resolve()
                .map(cap -> {
                    String selected = cap.getSelectedForm();
                    return selected == null || selected.isBlank() ? null : LightsaberForm.fromDisplayName(selected);
                })
                .orElse(null);
    }

    private static boolean isBlocking(LivingEntity entity, SaberContext context) {
        if (!entity.isUsingItem()) {
            return false;
        }
        ItemStack using = entity.getUseItem();
        if (!(using.getItem() instanceof LightsaberItem saber) || !saber.isActive(using)) {
            return false;
        }
        return using == context.primaryStack()
                || (context.secondaryStack() != null && using == context.secondaryStack());
    }

    private static WieldStyle resolveStyle(LivingEntity entity, ItemStack stack) {
        if (stack.getItem() instanceof DoubleLightsaberItem) {
            return WieldStyle.STAFF;
        }
        ItemStack main = entity.getMainHandItem();
        ItemStack off = entity.getOffhandItem();
        if (!(stack.getItem() instanceof DoubleLightsaberItem)
                && main.getItem() instanceof LightsaberItem && !(main.getItem() instanceof DoubleLightsaberItem)
                && off.getItem() instanceof LightsaberItem && !(off.getItem() instanceof DoubleLightsaberItem)) {
            return WieldStyle.DUAL;
        }
        return WieldStyle.SINGLE;
    }

    private static SaberContext resolveContext(LivingEntity entity) {
        ItemStack main = entity.getMainHandItem();
        ItemStack off = entity.getOffhandItem();
        if (main.getItem() instanceof DoubleLightsaberItem) {
            return new SaberContext(main, null, entity.getMainArm(), WieldStyle.STAFF);
        }
        if (off.getItem() instanceof DoubleLightsaberItem) {
            return new SaberContext(off, null, opposite(entity.getMainArm()), WieldStyle.STAFF);
        }
        if (main.getItem() instanceof LightsaberItem && off.getItem() instanceof LightsaberItem) {
            return new SaberContext(main, off, entity.getMainArm(), WieldStyle.DUAL);
        }
        if (main.getItem() instanceof LightsaberItem) {
            return new SaberContext(main, null, entity.getMainArm(), WieldStyle.SINGLE);
        }
        if (off.getItem() instanceof LightsaberItem) {
            return new SaberContext(off, null, opposite(entity.getMainArm()), WieldStyle.SINGLE);
        }
        return null;
    }

    private static void applyArm(HumanoidModel<?> model,
                                 HumanoidArm arm,
                                 float targetX, float targetY, float targetZ,
                                 float inward, float lift,
                                 float blend) {
        ModelPart part = getArm(model, arm);
        part.xRot = Mth.lerp(blend, part.xRot, targetX);
        part.yRot = Mth.lerp(blend, part.yRot, mirror(arm, targetY));
        part.zRot = Mth.lerp(blend, part.zRot, mirror(arm, targetZ));
        part.x += arm == HumanoidArm.RIGHT ? inward : -inward;
        part.y += lift;
    }

    private static void addAttack(HumanoidModel<?> model, HumanoidArm arm, float x, float y, float z) {
        ModelPart part = getArm(model, arm);
        part.xRot += x;
        part.xRot = Mth.clamp(part.xRot, -2.80F, 0.10F);
        part.yRot += mirror(arm, y);
        part.zRot += mirror(arm, z);
    }

    private static void applySupportArm(HumanoidModel<?> model,
                                        HumanoidArm arm,
                                        float targetX, float targetY, float targetZ,
                                        float inward, float lift,
                                        float blend,
                                        WieldStyle style,
                                        boolean twoHanded) {
        ModelPart part = getArm(model, arm);
        part.xRot = Mth.lerp(blend, part.xRot, targetX);
        part.yRot = Mth.lerp(blend, part.yRot, mirror(arm, targetY));
        part.zRot = Mth.lerp(blend, part.zRot, mirror(arm, targetZ));
        part.x += arm == HumanoidArm.RIGHT ? inward : -inward;
        part.y += lift;

        // Only push support hand toward hilt for two-handed single-saber grips.
        // One-handed forms (Makashi, Soresu, Niman, Juyo) keep off-hand free.
        // Staff keeps hands spread — do NOT push inward.
        if (style == WieldStyle.SINGLE && twoHanded) {
            part.x += arm == HumanoidArm.RIGHT ? 0.20F : -0.20F;
            part.z -= 0.38F;
        }
    }

    private static float mirror(HumanoidArm arm, float value) {
        return arm == HumanoidArm.RIGHT ? value : -value;
    }

    private static ModelPart getArm(HumanoidModel<?> model, HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
    }

    private static HumanoidArm opposite(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }

    private static float smooth(float value) {
        float t = Mth.clamp(value, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }

    // ═══════════════════════════════════════════════════════════════════
    //  PRIVATE RECORD — Controller-internal context
    // ═══════════════════════════════════════════════════════════════════

    private record SaberContext(ItemStack primaryStack, ItemStack secondaryStack, HumanoidArm primaryArm, WieldStyle style) {}
}
