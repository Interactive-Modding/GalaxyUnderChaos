package client.model.forceuser;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Applies vanilla-humanoid style rotations to the Blockbench species models.
 *
 * Some uploaded species/robe models are authored on a rotated Blockbench torso
 * (Torso.yRot ~= 90 degrees). On those models, vanilla x-axis limb walking makes
 * legs/arms swing across the body instead of forward/back. This helper detects
 * that baked pose and moves the animation onto the model-local z axis instead.
 */
public final class ForceUserModelAnimator {
    private static final Map<Class<?>, Parts> CACHE = new HashMap<>();

    private ForceUserModelAnimator() {
    }

    public static void animate(Object model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Parts parts = CACHE.computeIfAbsent(model.getClass(), ForceUserModelAnimator::inspect);
        resetModel(parts, model);

        ModelPart head = parts.get(model, Part.HEAD);
        ModelPart torso = parts.get(model, Part.TORSO);
        ModelPart rightArm = parts.get(model, Part.RIGHT_ARM);
        ModelPart leftArm = parts.get(model, Part.LEFT_ARM);
        ModelPart rightLeg = parts.get(model, Part.RIGHT_LEG);
        ModelPart leftLeg = parts.get(model, Part.LEFT_LEG);
        boolean rotatedTorso = usesSideAxis(model);

        if (head != null) {
            head.yRot += netHeadYaw * Mth.DEG_TO_RAD;
            head.xRot += headPitch * Mth.DEG_TO_RAD;
        }

        float walk = Math.min(limbSwingAmount, 1.0F);
        float legRight = Mth.cos(limbSwing * 0.6662F) * 0.95F * walk;
        float legLeft = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 0.95F * walk;
        float armRight = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 0.85F * walk;
        float armLeft = Mth.cos(limbSwing * 0.6662F) * 0.85F * walk;

        if (rotatedTorso) {
            // These models are built sideways in local space; xRot causes the crossed-leg/crossed-arm bug.
            applySideAxisWalk(rightLeg, leftLeg, rightArm, leftArm, legRight, legLeft, armRight, armLeft);
        } else {
            applyVanillaAxisWalk(rightLeg, leftLeg, rightArm, leftArm, legRight, legLeft, armRight, armLeft);
        }

        boolean saberDrawn = entity instanceof ForceUserEntity forceUser && forceUser.isSaberDrawn();
        boolean casting = entity instanceof ForceUserEntity forceUser && forceUser.getCastingPowerTicks() > 0;
        float attack = entity instanceof LivingEntity living ? living.getAttackAnim(0.0F) : 0.0F;

        if (saberDrawn) {
            applySaberPose(rightArm, leftArm, torso, ageInTicks, attack, entity.isCrouching(), rotatedTorso);
        } else if (casting) {
            applyForceCastPose(rightArm, leftArm, torso, ageInTicks, rotatedTorso);
        }
    }

    private static void applyVanillaAxisWalk(ModelPart rightLeg, ModelPart leftLeg, ModelPart rightArm, ModelPart leftArm,
                                             float legRight, float legLeft, float armRight, float armLeft) {
        if (rightLeg != null) {
            rightLeg.xRot += legRight;
            rightLeg.yRot += 0.0F;
            rightLeg.zRot += 0.0F;
        }
        if (leftLeg != null) {
            leftLeg.xRot += legLeft;
            leftLeg.yRot += 0.0F;
            leftLeg.zRot += 0.0F;
        }
        if (rightArm != null) {
            rightArm.xRot += armRight;
            rightArm.yRot += 0.0F;
            rightArm.zRot += 0.0F;
        }
        if (leftArm != null) {
            leftArm.xRot += armLeft;
            leftArm.yRot += 0.0F;
            leftArm.zRot += 0.0F;
        }
    }

    private static void applySideAxisWalk(ModelPart rightLeg, ModelPart leftLeg, ModelPart rightArm, ModelPart leftArm,
                                          float legRight, float legLeft, float armRight, float armLeft) {
        if (rightLeg != null) {
            rightLeg.xRot += 0.0F;
            rightLeg.yRot += 0.0F;
            rightLeg.zRot += -legRight;
        }
        if (leftLeg != null) {
            leftLeg.xRot += 0.0F;
            leftLeg.yRot += 0.0F;
            leftLeg.zRot += -legLeft;
        }
        if (rightArm != null) {
            rightArm.xRot += 0.0F;
            rightArm.yRot += 0.0F;
            rightArm.zRot += -armRight;
        }
        if (leftArm != null) {
            leftArm.xRot += 0.0F;
            leftArm.yRot += 0.0F;
            leftArm.zRot += -armLeft;
        }
    }

    public static void translateToRightHand(Object model, PoseStack poseStack) {
        Parts parts = CACHE.computeIfAbsent(model.getClass(), ForceUserModelAnimator::inspect);
        ModelPart root = parts.get(model, Part.ROOT);
        ModelPart torso = parts.get(model, Part.TORSO);
        ModelPart rightArm = parts.get(model, Part.RIGHT_ARM);

        if (root != null) {
            root.translateAndRotate(poseStack);
        }
        if (torso != null) {
            torso.translateAndRotate(poseStack);
        }
        if (rightArm != null) {
            rightArm.translateAndRotate(poseStack);
        }
    }

    public static void translateToBelt(Object model, PoseStack poseStack) {
        Parts parts = CACHE.computeIfAbsent(model.getClass(), ForceUserModelAnimator::inspect);
        ModelPart root = parts.get(model, Part.ROOT);
        ModelPart torso = parts.get(model, Part.TORSO);

        if (root != null) {
            root.translateAndRotate(poseStack);
        }
        if (torso != null) {
            torso.translateAndRotate(poseStack);
        }
    }

    public static boolean usesSideAxis(Object model) {
        Parts parts = CACHE.computeIfAbsent(model.getClass(), ForceUserModelAnimator::inspect);
        ModelPart torso = parts.get(model, Part.TORSO);
        return torso != null && Math.abs(Mth.wrapDegrees(torso.yRot * Mth.RAD_TO_DEG)) > 45.0F;
    }

    /**
     * Hides only the base body legs on the currently active Force-user model.
     * This does not touch armor models/layers, so armor legs can still render cleanly
     * without the human skin/robe leg cubes z-fighting through the leggings.
     */
    public static void setLegVisibility(Object model, boolean visible) {
        Parts parts = CACHE.computeIfAbsent(model.getClass(), ForceUserModelAnimator::inspect);
        ModelPart rightLeg = parts.get(model, Part.RIGHT_LEG);
        ModelPart leftLeg = parts.get(model, Part.LEFT_LEG);
        if (rightLeg != null) {
            rightLeg.visible = visible;
        }
        if (leftLeg != null) {
            leftLeg.visible = visible;
        }
    }

    private static void resetModel(Parts parts, Object model) {
        ModelPart root = parts.get(model, Part.ROOT);
        if (root != null) {
            root.getAllParts().forEach(ModelPart::resetPose);
            return;
        }

        for (Part part : Part.values()) {
            ModelPart modelPart = parts.get(model, part);
            if (modelPart != null) {
                modelPart.resetPose();
            }
        }
    }

    private static void applySaberPose(ModelPart rightArm, ModelPart leftArm, ModelPart torso, float ageInTicks, float attack, boolean crouching, boolean rotatedTorso) {
        float idle = Mth.sin(ageInTicks * 0.22F) * 0.035F;
        float swing = attack > 0.0F ? Mth.sin(Mth.sqrt(attack) * Mth.PI) : 0.0F;

        if (torso != null) {
            torso.xRot += crouching ? 0.10F : 0.02F;
            torso.yRot += 0.055F * swing;
        }

        if (rotatedTorso) {
            if (rightArm != null) {
                rightArm.zRot += 1.18F - idle + 0.42F * swing;
                rightArm.yRot += -0.10F + 0.18F * swing;
                rightArm.xRot += -0.04F;
            }
            if (leftArm != null) {
                leftArm.zRot += 0.92F + idle + 0.20F * swing;
                leftArm.yRot += 0.10F - 0.10F * swing;
                leftArm.xRot += 0.04F;
            }
            return;
        }

        if (rightArm != null) {
            rightArm.xRot += -1.30F + idle - 0.55F * swing;
            rightArm.yRot += -0.18F + 0.25F * swing;
            rightArm.zRot += 0.08F - 0.15F * swing;
        }
        if (leftArm != null) {
            rightTwoHandSupport(leftArm, idle, swing);
        }
    }

    private static void rightTwoHandSupport(ModelPart leftArm, float idle, float swing) {
        leftArm.xRot += -1.02F - idle - 0.28F * swing;
        leftArm.yRot += 0.22F - 0.12F * swing;
        leftArm.zRot += -0.07F + 0.10F * swing;
    }

    private static void applyForceCastPose(ModelPart rightArm, ModelPart leftArm, ModelPart torso, float ageInTicks, boolean rotatedTorso) {
        float pulse = Mth.sin(ageInTicks * 0.35F) * 0.06F;
        if (torso != null) {
            torso.xRot += -0.02F;
        }

        if (rotatedTorso) {
            if (rightArm != null) {
                rightArm.zRot += 1.25F - pulse;
                rightArm.yRot += -0.12F;
            }
            if (leftArm != null) {
                leftArm.zRot += 1.10F + pulse;
                leftArm.yRot += 0.12F;
            }
            return;
        }

        if (rightArm != null) {
            rightArm.xRot += -1.35F + pulse;
            rightArm.yRot += -0.16F;
        }
        if (leftArm != null) {
            leftArm.xRot += -1.20F - pulse;
            leftArm.yRot += 0.16F;
        }
    }

    private static Parts inspect(Class<?> modelClass) {
        Parts parts = new Parts();
        Class<?> cursor = modelClass;
        while (cursor != null && cursor != Object.class) {
            for (Field field : cursor.getDeclaredFields()) {
                if (!ModelPart.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                field.setAccessible(true);
                String name = field.getName();
                if (equalsAny(name, "Root", "root")) {
                    parts.put(Part.ROOT, field);
                } else if (equalsAny(name, "Head", "head")) {
                    parts.put(Part.HEAD, field);
                } else if (equalsAny(name, "Torso", "torso", "body")) {
                    parts.put(Part.TORSO, field);
                } else if (equalsAny(name, "RightHand", "rightHand", "rightArm")) {
                    parts.putIfAbsent(Part.RIGHT_ARM, field);
                } else if (equalsAny(name, "LeftHand", "leftHand", "leftArm")) {
                    parts.putIfAbsent(Part.LEFT_ARM, field);
                } else if (equalsAny(name, "RightLeg", "rightLeg")) {
                    parts.putIfAbsent(Part.RIGHT_LEG, field);
                } else if (equalsAny(name, "LeftLeg", "leftLeg")) {
                    parts.putIfAbsent(Part.LEFT_LEG, field);
                }
            }
            cursor = cursor.getSuperclass();
        }
        return parts;
    }

    private static boolean equalsAny(String value, String... options) {
        for (String option : options) {
            if (option.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private enum Part {
        ROOT,
        HEAD,
        TORSO,
        RIGHT_ARM,
        LEFT_ARM,
        RIGHT_LEG,
        LEFT_LEG
    }

    private static final class Parts {
        private final Map<Part, Field> fields = new HashMap<>();

        void put(Part part, Field field) {
            fields.put(part, field);
        }

        void putIfAbsent(Part part, Field field) {
            fields.putIfAbsent(part, field);
        }

        ModelPart get(Object model, Part part) {
            Field field = fields.get(part);
            if (field == null) {
                return null;
            }
            try {
                return (ModelPart) field.get(model);
            } catch (IllegalAccessException ignored) {
                return null;
            }
        }
    }
}
