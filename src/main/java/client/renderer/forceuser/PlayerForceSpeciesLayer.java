package client.renderer.forceuser;

import client.model.forceuser.cerean_female;
import client.model.forceuser.cerean_male;
import client.model.forceuser.chiss_female;
import client.model.forceuser.chiss_male;
import client.model.forceuser.jedi_robes;
import client.model.forceuser.jedi_robes_alt;
import client.model.forceuser.miraluka_female;
import client.model.forceuser.miraluka_male;
import client.model.forceuser.mirialan_female;
import client.model.forceuser.mirialan_male;
import client.model.forceuser.rodian_female;
import client.model.forceuser.rodian_male;
import client.model.forceuser.sith_female;
import client.model.forceuser.sith_male;
import client.model.forceuser.sith_robes;
import client.model.forceuser.sith_robes_alt;
import client.model.forceuser.togruta_female;
import client.model.forceuser.togruta_male;
import client.model.forceuser.twilek_female;
import client.model.forceuser.twilek_male;
import client.model.forceuser.zabrak_female;
import client.model.forceuser.zabrak_male;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import server.galaxyunderchaos.galaxyunderchaos;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerForceSpeciesLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final float CROUCH_UPPER_BODY_BACK_OFFSET = 4.0F;

    private static final Map<Class<?>, Parts> PART_CACHE = new HashMap<>();

    private final Map<String, EntityModel<AbstractClientPlayer>> speciesModels = new LinkedHashMap<>();
    private final Map<String, EntityModel<AbstractClientPlayer>> robeModels = new LinkedHashMap<>();

    public PlayerForceSpeciesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, EntityModelSet modelSet) {
        super(parent);

        speciesModels.put("cerean_female", new cerean_female<>(modelSet.bakeLayer(ForceUserModelLayers.layer("cerean_female"))));
        speciesModels.put("cerean_male", new cerean_male<>(modelSet.bakeLayer(ForceUserModelLayers.layer("cerean_male"))));
        speciesModels.put("chiss_female", new chiss_female<>(modelSet.bakeLayer(ForceUserModelLayers.layer("chiss_female"))));
        speciesModels.put("chiss_male", new chiss_male<>(modelSet.bakeLayer(ForceUserModelLayers.layer("chiss_male"))));
        speciesModels.put("miraluka_female", new miraluka_female<>(modelSet.bakeLayer(ForceUserModelLayers.layer("miraluka_female"))));
        speciesModels.put("miraluka_male", new miraluka_male<>(modelSet.bakeLayer(ForceUserModelLayers.layer("miraluka_male"))));
        speciesModels.put("mirialan_female", new mirialan_female<>(modelSet.bakeLayer(ForceUserModelLayers.layer("mirialan_female"))));
        speciesModels.put("mirialan_male", new mirialan_male<>(modelSet.bakeLayer(ForceUserModelLayers.layer("mirialan_male"))));
        speciesModels.put("rodian_female", new rodian_female<>(modelSet.bakeLayer(ForceUserModelLayers.layer("rodian_female"))));
        speciesModels.put("rodian_male", new rodian_male<>(modelSet.bakeLayer(ForceUserModelLayers.layer("rodian_male"))));
        speciesModels.put("sith_female", new sith_female<>(modelSet.bakeLayer(ForceUserModelLayers.layer("sith_female"))));
        speciesModels.put("sith_male", new sith_male<>(modelSet.bakeLayer(ForceUserModelLayers.layer("sith_male"))));
        speciesModels.put("togruta_female", new togruta_female<>(modelSet.bakeLayer(ForceUserModelLayers.layer("togruta_female"))));
        speciesModels.put("togruta_male", new togruta_male<>(modelSet.bakeLayer(ForceUserModelLayers.layer("togruta_male"))));
        speciesModels.put("twilek_female", new twilek_female<>(modelSet.bakeLayer(ForceUserModelLayers.layer("twilek_female"))));
        speciesModels.put("twilek_male", new twilek_male<>(modelSet.bakeLayer(ForceUserModelLayers.layer("twilek_male"))));
        speciesModels.put("zabrak_female", new zabrak_female<>(modelSet.bakeLayer(ForceUserModelLayers.layer("zabrak_female"))));
        speciesModels.put("zabrak_male", new zabrak_male<>(modelSet.bakeLayer(ForceUserModelLayers.layer("zabrak_male"))));

        robeModels.put("jedi_robes", new jedi_robes<>(modelSet.bakeLayer(ForceUserModelLayers.layer("jedi_robes"))));
        robeModels.put("jedi_robes_alt", new jedi_robes_alt<>(modelSet.bakeLayer(ForceUserModelLayers.layer("jedi_robes_alt"))));
        robeModels.put("sith_robes", new sith_robes<>(modelSet.bakeLayer(ForceUserModelLayers.layer("sith_robes"))));
        robeModels.put("sith_robes_alt", new sith_robes_alt<>(modelSet.bakeLayer(ForceUserModelLayers.layer("sith_robes_alt"))));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        PlayerForceIdentityClientState.Entry identity = PlayerForceIdentityClientState.get(player.getUUID());
        if (identity == null || player.isInvisible()) {
            return;
        }

        String identityId = identity.speciesId();

        poseStack.pushPose();

        if (PlayerForceIdentityClientState.hasAlienSpecies(identityId)) {
            renderAlienSpecies(identityId, identity.darkEyes(), poseStack, buffer, packedLight, player);
            renderCompatibleAlienRobe(identityId, poseStack, buffer, packedLight, player);
        } else {
            renderPlayerRobeBody(identityId, poseStack, buffer, packedLight, player);
        }

        poseStack.popPose();
    }

    private void renderAlienSpecies(String identityId, boolean darkEyes, PoseStack poseStack, MultiBufferSource buffer,
                                    int packedLight, AbstractClientPlayer player) {
        String modelId = PlayerForceIdentityClientState.modelId(identityId);
        EntityModel<AbstractClientPlayer> speciesModel = speciesModels.getOrDefault(modelId, speciesModels.get("mirialan_male"));
        if (speciesModel == null) {
            return;
        }

        applyExactVanillaPlayerPose(speciesModel);
        setAllVisible(speciesModel, true);

        ResourceLocation speciesTexture = texture(PlayerForceIdentityClientState.textureId(identityId));
        renderModel(speciesModel, poseStack, buffer, packedLight, player, speciesTexture, RenderType.entityCutoutNoCull(speciesTexture));

        if (!PlayerForceIdentityClientState.isChiss(identityId) && PlayerForceIdentityClientState.hasEyeColorOptions(identityId)) {
            String suffix = PlayerForceIdentityClientState.isJediOriginSpecies(identityId) ? "jedi_og" : "sith_og";
            if (darkEyes) {
                ResourceLocation eyes = firstExistingTexture(
                        "sith_eyes_" + suffix,
                        PlayerForceIdentityClientState.isJediOriginSpecies(identityId) ? "sith_eyes_jedi_org" : "sith_eyes_sith_org"
                );
                if (eyes != null) {
                    renderModel(speciesModel, poseStack, buffer, 15728640, player, eyes, RenderType.eyes(eyes));
                }
            } else {
                String color = PlayerForceIdentityClientState.eyeColorId(identityId);
                ResourceLocation eyes = firstExistingTexture(
                        color + "_eyes_" + suffix,
                        PlayerForceIdentityClientState.isJediOriginSpecies(identityId) ? color + "_eyes_jedi_org" : color + "_eyes_sith_org"
                );
                if (eyes != null) {
                    renderModel(speciesModel, poseStack, buffer, packedLight, player, eyes, RenderType.entityCutoutNoCull(eyes));
                }
            }
        }
    }

    private void renderCompatibleAlienRobe(String identityId, PoseStack poseStack, MultiBufferSource buffer,
                                           int packedLight, AbstractClientPlayer player) {
        String robeId = PlayerForceIdentityClientState.robeModelId(identityId);
        if (robeId == null || robeId.isBlank()) {
            return;
        }

        EntityModel<AbstractClientPlayer> robeModel = robeModels.get(robeId);
        if (robeModel == null) {
            return;
        }

        applyExactVanillaPlayerPose(robeModel);
        setAllVisible(robeModel, true);

        ResourceLocation robeTexture = texture(PlayerForceIdentityClientState.robeTextureId(identityId));
        renderModel(robeModel, poseStack, buffer, packedLight, player, robeTexture, RenderType.entityCutoutNoCull(robeTexture));
    }

    private void renderPlayerRobeBody(String identityId, PoseStack poseStack, MultiBufferSource buffer,
                                      int packedLight, AbstractClientPlayer player) {
        String robeId = PlayerForceIdentityClientState.robeModelId(identityId);
        if (robeId == null || robeId.isBlank()) {
            return;
        }

        EntityModel<AbstractClientPlayer> robeModel = robeModels.getOrDefault(robeId, robeModels.get("jedi_robes"));
        if (robeModel == null) {
            return;
        }

        applyExactVanillaPlayerPose(robeModel);
        setAllVisible(robeModel, true);

        ResourceLocation robeTexture = texture(PlayerForceIdentityClientState.robeTextureId(identityId));
        renderModel(robeModel, poseStack, buffer, packedLight, player, robeTexture, RenderType.entityCutoutNoCull(robeTexture));

        ResourceLocation skinOverlay = texture(PlayerForceIdentityClientState.skinOverlayTextureId(identityId));
        renderModel(
                robeModel,
                poseStack,
                buffer,
                packedLight,
                player,
                skinOverlay,
                RenderType.entityCutoutNoCull(skinOverlay),
                PlayerForceIdentityClientState.skinRed(identityId),
                PlayerForceIdentityClientState.skinGreen(identityId),
                PlayerForceIdentityClientState.skinBlue(identityId),
                1.0F
        );
    }

    private static void renderModel(EntityModel<AbstractClientPlayer> model, PoseStack poseStack, MultiBufferSource buffer,
                                    int packedLight, AbstractClientPlayer player, ResourceLocation texture, RenderType renderType) {
        renderModel(model, poseStack, buffer, packedLight, player, texture, renderType, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void renderModel(EntityModel<AbstractClientPlayer> model, PoseStack poseStack, MultiBufferSource buffer,
                                    int packedLight, AbstractClientPlayer player, ResourceLocation texture, RenderType renderType,
                                    float red, float green, float blue, float alpha) {
        VertexConsumer consumer = buffer.getBuffer(renderType);
        model.renderToBuffer(
                poseStack,
                consumer,
                packedLight,
                LivingEntityRenderer.getOverlayCoords(player, 0.0F),
                red,
                green,
                blue,
                alpha
        );
    }

    private void applyExactVanillaPlayerPose(EntityModel<AbstractClientPlayer> model) {
        Parts parts = PART_CACHE.computeIfAbsent(model.getClass(), PlayerForceSpeciesLayer::inspect);
        resetModel(parts, model);

        PlayerModel<AbstractClientPlayer> vanilla = getParentModel();
        ModelPart targetTorso = parts.get(model, Part.TORSO);

        boolean sideAxis = usesSideAxis(targetTorso);
        // Capture the torso's authored yaw BEFORE copyBodyPose folds in the live body yaw.
        // Side-axis children are re-expressed in this yawed frame via a true rotation.
        float bakedTorsoYaw = (targetTorso != null) ? targetTorso.yRot : 0.0F;

        copyBodyPose(vanilla.body, targetTorso, sideAxis);
        copyChildPose(vanilla.head, vanilla.body, parts.get(model, Part.HEAD), sideAxis, Part.HEAD, bakedTorsoYaw);
        copyChildPose(vanilla.rightArm, vanilla.body, parts.get(model, Part.RIGHT_ARM), sideAxis, Part.RIGHT_ARM, bakedTorsoYaw);
        copyChildPose(vanilla.leftArm, vanilla.body, parts.get(model, Part.LEFT_ARM), sideAxis, Part.LEFT_ARM, bakedTorsoYaw);
        copyChildPose(vanilla.rightLeg, vanilla.body, parts.get(model, Part.RIGHT_LEG), sideAxis, Part.RIGHT_LEG, bakedTorsoYaw);
        copyChildPose(vanilla.leftLeg, vanilla.body, parts.get(model, Part.LEFT_LEG), sideAxis, Part.LEFT_LEG, bakedTorsoYaw);

        if (vanilla.crouching) {
            applyCrouch(parts, model, sideAxis, vanilla.body.xRot);
        }
    }


    private static void applyCrouch(Parts parts, Object model, boolean sideAxis, float crouchPitch) {
        ModelPart torso = parts.get(model, Part.TORSO);
        if (torso == null) {
            return;
        }

        if (sideAxis) {
            /*
             * Side-axis (Alex-like) rigs bake the torso yawed ~90 degrees. Adding the
             * vanilla crouch lean (body.xRot) as a raw torso.xRot rolls the whole model
             * sideways, so the lean used to be dropped entirely and the upper body stayed
             * bolt upright while sneaking. Apply the lean as a real world-space forward
             * pitch composed with the baked yaw instead, then keep the limbs/head at their
             * vanilla orientation so only the torso bends (matching vanilla's flat rig).
             */
            applySideAxisCrouchLean(parts, model, crouchPitch);
            return;
        }

        // Straight rigs already bend via the copied body.xRot. Nudge the upper body back
        // slightly so the crouch silhouette matches vanilla, compensating the legs.
        torso.z += CROUCH_UPPER_BODY_BACK_OFFSET;
        compensateStraightAxisLegForCrouchBackOffset(parts.get(model, Part.RIGHT_LEG));
        compensateStraightAxisLegForCrouchBackOffset(parts.get(model, Part.LEFT_LEG));
    }

    private static void compensateStraightAxisLegForCrouchBackOffset(ModelPart leg) {
        if (leg != null) {
            leg.z -= CROUCH_UPPER_BODY_BACK_OFFSET;
        }
    }

    private static void applySideAxisCrouchLean(Parts parts, Object model, float crouchPitch) {
        ModelPart torso = parts.get(model, Part.TORSO);
        ModelPart legs = parts.get(model, Part.LEFT_LEG);
        ModelPart legs2 = parts.get(model, Part.RIGHT_LEG);
        if (torso == null || Math.abs(crouchPitch) < 1.0E-4F) {
            return;
        }

        // Compose the crouch lean as a forward pitch around the world X axis, applied
        // OUTSIDE the torso's baked yaw: torsoNew = Rx(pitch) * torsoOld. This bends the
        // upper body forward instead of rolling it sideways, for any yaw the rig was
        // authored with.
        float[] torsoOld = rotationMatrix(torso.zRot, torso.yRot, torso.xRot);
        float[] torsoNew = multiply(rotationX(crouchPitch), torsoOld);
        setRotationFromMatrix(torso, torsoNew);
        torso.z += CROUCH_UPPER_BODY_BACK_OFFSET;
        legs.x += CROUCH_UPPER_BODY_BACK_OFFSET;
        legs2.x += CROUCH_UPPER_BODY_BACK_OFFSET;
        // Head/arms/legs are children of the torso, so they would otherwise inherit the
        // new lean and over-rotate (vanilla keeps them upright during crouch). Counter
        // the inherited lean in torso-local space so each child keeps its vanilla world
        // orientation while still following the lean's pivot shift.
        float[] counter = multiply(transpose(torsoNew), torsoOld);
        counterRotateChild(parts.get(model, Part.HEAD), counter);
        counterRotateChild(parts.get(model, Part.RIGHT_ARM), counter);
        counterRotateChild(parts.get(model, Part.LEFT_ARM), counter);
        counterRotateChild(parts.get(model, Part.RIGHT_LEG), counter);
        counterRotateChild(parts.get(model, Part.LEFT_LEG), counter);
    }

    private static void counterRotateChild(ModelPart child, float[] counter) {
        if (child == null) {
            return;
        }
        float[] current = rotationMatrix(child.zRot, child.yRot, child.xRot);
        setRotationFromMatrix(child, multiply(counter, current));
    }

    // ---- minimal 3x3 rotation helpers (row-major), mirroring ModelPart's rotationZYX ----

    private static float[] rotationX(float angle) {
        float c = Mth.cos(angle);
        float s = Mth.sin(angle);
        return new float[]{
                1.0F, 0.0F, 0.0F,
                0.0F, c, -s,
                0.0F, s, c
        };
    }

    private static float[] rotationY(float angle) {
        float c = Mth.cos(angle);
        float s = Mth.sin(angle);
        return new float[]{
                c, 0.0F, s,
                0.0F, 0.80F, 0.0F,
                -s, 0.0F, c
        };
    }

    /** R = Rz(z) * Ry(y) * Rx(x), matching {@code new Quaternionf().rotationZYX(z, y, x)}. */
    private static float[] rotationMatrix(float z, float y, float x) {
        float cz = Mth.cos(z);
        float sz = Mth.sin(z);
        float cy = Mth.cos(y);
        float sy = Mth.sin(y);
        float cx = Mth.cos(x);
        float sx = Mth.sin(x);
        return new float[]{
                cy * cz, sx * sy * cz - cx * sz, cx * sy * cz + sx * sz,
                cy * sz, sx * sy * sz + cx * cz, cx * sy * sz - sx * cz,
                -sy, sx * cy, cx * cy
        };
    }

    private static float[] multiply(float[] a, float[] b) {
        float[] r = new float[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                r[row * 3 + col] = a[row * 3] * b[col]
                        + a[row * 3 + 1] * b[3 + col]
                        + a[row * 3 + 2] * b[6 + col];
            }
        }
        return r;
    }

    private static float[] transpose(float[] m) {
        return new float[]{
                m[0], m[3], m[6],
                m[1], m[4], m[7],
                m[2], m[5], m[8]
        };
    }

    /** Writes the ZYX Euler angles that reproduce {@code m} back onto the part. */
    private static void setRotationFromMatrix(ModelPart part, float[] m) {
        float sy = Mth.clamp(-m[6], -1.0F, 1.0F);
        float y = (float) Math.asin(sy);
        float x;
        float z;
        if (Math.abs(Mth.cos(y)) > 1.0E-6F) {
            x = (float) Math.atan2(m[7], m[8]);
            z = (float) Math.atan2(m[3], m[0]);
        } else {
            // Gimbal lock: fold the roll into the pitch axis.
            x = (float) Math.atan2(-m[5], m[4]);
            z = 0.0F;
        }
        part.xRot = x;
        part.yRot = y;
        part.zRot = z;
    }

    private static boolean usesSideAxis(ModelPart torso) {
        return torso != null && Math.abs(Mth.wrapDegrees(torso.yRot * Mth.RAD_TO_DEG)) > 45.0F;
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

    private static void copyBodyPose(ModelPart sourceBody, ModelPart targetBody, boolean sideAxis) {
        if (targetBody == null) {
            return;
        }

        /*
         * Straight rigs, like the working Sith models, can copy Minecraft's body
         * rotations directly. The sideways robe/species rigs are different: their
         * torso is already baked with Torso.yRot ~= 90°. If Minecraft's crouch
         * body.xRot is added to that yawed torso, the whole model visually rolls
         * sideways. So side-axis torsos only inherit horizontal twist, while crouch
         * is handled by position offsets and child limb/head rotations below.
         */
        if (sideAxis) {
            targetBody.yRot += sourceBody.yRot;
        } else {
            targetBody.xRot += sourceBody.xRot;
            targetBody.yRot += sourceBody.yRot;
            targetBody.zRot += sourceBody.zRot;
        }

        targetBody.x += sourceBody.x;
        targetBody.y += sourceBody.y;
        targetBody.z += sourceBody.z;
    }

    private static void copyChildPose(ModelPart source, ModelPart sourceBody, ModelPart target,
                                      boolean sideAxis, Part part, float bakedTorsoYaw) {
        if (target == null) {
            return;
        }

        float relativeXRot = source.xRot - sourceBody.xRot;
        float relativeYRot = source.yRot - sourceBody.yRot;
        float relativeZRot = source.zRot - sourceBody.zRot;

        if (sideAxis) {
            /*
             * Side-axis children live under a torso baked with a ~90 degree yaw, so a
             * vanilla limb rotation has to be re-expressed in that yawed frame. The old
             * code did a per-axis swap (xRot = -source.zRot, zRot = source.xRot), which is
             * only correct when the vanilla limb rotates about a SINGLE axis. Item/saber
             * holds and attack swings combine pitch, yaw and roll at once, and three
             * independent axis swaps do not equal the correctly composed rotation, so the
             * hand drifted to the wrong place.
             *
             * Re-express the rotation properly with a change of basis (conjugation) by the
             * torso's baked yaw: A_local = Ry(-yaw) * A * Ry(yaw). This is exactly the old
             * swap for single-axis cases (so idle/walk are unchanged) but composes
             * correctly for any combined pose. sourceBody.xRot is intentionally NOT
             * subtracted: vanilla arms/legs are siblings of the body and never inherit the
             * crouch body lean, and the torso lean is now handled separately in applyCrouch.
             */
            float[] vanillaRel = rotationMatrix(source.zRot, relativeYRot, source.xRot);
            float[] toYawedFrame = rotationY(bakedTorsoYaw);
            float[] localRotation = multiply(multiply(transpose(toYawedFrame), vanillaRel), toYawedFrame);

            // Compose onto the part's baked default (identity for these rigs) and write back.
            float[] baked = rotationMatrix(target.zRot, target.yRot, target.xRot);
            setRotationFromMatrix(target, multiply(baked, localRotation));

            applySideAxisPositionDelta(source, sourceBody, target, part);
            return;
        }

        target.xRot += relativeXRot;
        target.yRot += relativeYRot;
        target.zRot += relativeZRot;

        applyStraightAxisPositionDelta(source, sourceBody, target, part);
    }

    private static void applyStraightAxisPositionDelta(ModelPart source, ModelPart sourceBody, ModelPart target,
                                                       Part part) {
        float relativeX = (source.x - vanillaDefaultX(part)) - sourceBody.x;
        float relativeY = (source.y - vanillaDefaultY(part)) - sourceBody.y;
        float relativeZ = source.z - sourceBody.z;

        target.x += relativeX;
        target.y += relativeY;
        target.z += relativeZ;
    }

    private static void applySideAxisPositionDelta(ModelPart source, ModelPart sourceBody, ModelPart target,
                                                   Part part) {
        float relativeX = (source.x - vanillaDefaultX(part)) - sourceBody.x;
        float relativeY = (source.y - vanillaDefaultY(part)) - sourceBody.y;
        float relativeZ = source.z - sourceBody.z;

        target.x += -relativeZ;
        target.y += relativeY;
        target.z += relativeX;
    }

    private static float vanillaDefaultX(Part part) {
        return switch (part) {
            case RIGHT_ARM -> -5.0F;
            case LEFT_ARM -> 5.0F;
            case RIGHT_LEG -> -1.9F;
            case LEFT_LEG -> 1.9F;
            default -> 0.0F;
        };
    }

    private static float vanillaDefaultY(Part part) {
        return switch (part) {
            // Minecraft's slim/Alex model only changes the baked arm mesh/pivot.
            // During setupAnim, HumanoidModel still animates arms from y = 2.0F.
            case RIGHT_ARM, LEFT_ARM -> 2.0F;
            case RIGHT_LEG, LEFT_LEG -> 12.0F;
            default -> 0.0F;
        };
    }

    private static void setAllVisible(EntityModel<AbstractClientPlayer> model, boolean visible) {
        Parts parts = PART_CACHE.computeIfAbsent(model.getClass(), PlayerForceSpeciesLayer::inspect);
        ModelPart root = parts.get(model, Part.ROOT);
        if (root != null) {
            root.getAllParts().forEach(part -> part.visible = visible);
            return;
        }

        for (Part part : Part.values()) {
            ModelPart modelPart = parts.get(model, part);
            if (modelPart != null) {
                modelPart.visible = visible;
            }
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

    private static ResourceLocation texture(String id) {
        return new ResourceLocation(galaxyunderchaos.MODID, "textures/entity/force_user/" + id + ".png");
    }

    private static ResourceLocation firstExistingTexture(String... ids) {
        for (String id : ids) {
            ResourceLocation texture = texture(id);
            if (Minecraft.getInstance().getResourceManager().getResource(texture).isPresent()) {
                return texture;
            }
        }
        return null;
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