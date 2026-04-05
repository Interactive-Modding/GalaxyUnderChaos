package client.renderer.lightsaber.legacy;

import client.model.lightsaber.legacy.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class LegacyJavaLightsaberModels {
    private static final Map<String, EnumMap<LightsaberPartType, LegacyModelBase>> MODELS = createModels();

    private LegacyJavaLightsaberModels() {
    }

    public static boolean hasModel(String familyId, LightsaberPartType type) {
        EnumMap<LightsaberPartType, LegacyModelBase> map = MODELS.get(familyId);
        return map != null && map.containsKey(type);
    }

    public static boolean renderPart(String familyId,
                                     LightsaberPartType type,
                                     ResourceLocation texture,
                                     PoseStack poseStack,
                                     MultiBufferSource buffer,
                                     int packedLight,
                                     int packedOverlay) {
        LegacyModelBase model = getModel(familyId, type);
        if (model == null) {
            return false;
        }
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        LegacyRenderSession.begin(poseStack, consumer, packedLight, packedOverlay);
        try {
            model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        } finally {
            LegacyRenderSession.end();
        }
        return true;
    }

    private static LegacyModelBase getModel(String familyId, LightsaberPartType type) {
        EnumMap<LightsaberPartType, LegacyModelBase> map = MODELS.get(familyId);
        return map != null ? map.get(type) : null;
    }

    private static Map<String, EnumMap<LightsaberPartType, LegacyModelBase>> createModels() {
        Map<String, EnumMap<LightsaberPartType, LegacyModelBase>> map = new HashMap<>();
        put(map, "droideka", new ModelEmitterDroideka(), new ModelSwitchSectionDroideka(), new ModelBodyDroideka(), new ModelPommelDroideka());
        put(map, "fulcrum", new ModelEmitterFulcrum(), new ModelSwitchSectionFulcrum(), new ModelBodyFulcrum(), new ModelPommelFulcrum());
        put(map, "fury", new ModelEmitterFury(), new ModelSwitchSectionFury(), new ModelBodyFury(), new ModelPommelFury());
        put(map, "graflex", new ModelEmitterGraflex(), new ModelSwitchSectionGraflex(), new ModelBodyGraflex(), new ModelPommelGraflex());
        put(map, "imperial", new ModelEmitterImperial(), new ModelSwitchSectionImperial(), new ModelBodyImperial(), new ModelPommelImperial());
        put(map, "juggernaut", new ModelEmitterJuggernaut(), new ModelSwitchSectionJuggernaut(), new ModelBodyJuggernaut(), new ModelPommelJuggernaut());
        put(map, "knighted", new ModelEmitterKnighted(), new ModelSwitchSectionKnighted(), new ModelBodyKnighted(), new ModelPommelKnighted());
        put(map, "mandalorian", new ModelEmitterMandalorian(), new ModelSwitchSectionMandalorian(), new ModelBodyMandalorian(), new ModelPommelMandalorian());
        put(map, "mauler", new ModelEmitterMauler(), new ModelSwitchSectionMauler(), new ModelBodyMauler(), new ModelPommelMauler());
        put(map, "mechanical", new ModelEmitterMechanical(), new ModelSwitchSectionMechanical(), new ModelBodyMechanical(), new ModelPommelMechanical());
        put(map, "prodigal_son", new ModelEmitterProdigalSon(), new ModelSwitchSectionProdigalSon(), new ModelBodyProdigalSon(), new ModelPommelProdigalSon());
        put(map, "rebel", new ModelEmitterRebel(), new ModelSwitchSectionRebel(), new ModelBodyRebel(), new ModelPommelRebel());
        put(map, "reborn", new ModelEmitterReborn(), new ModelSwitchSectionReborn(), new ModelBodyReborn(), new ModelPommelReborn());
        put(map, "redeemer", new ModelEmitterRedeemer(), new ModelSwitchSectionRedeemer(), new ModelBodyRedeemer(), new ModelPommelRedeemer());
        put(map, "vaid", new ModelEmitterVaid(), new ModelSwitchSectionVaid(), new ModelBodyVaid(), new ModelPommelVaid());
        return map;
    }

    private static void put(Map<String, EnumMap<LightsaberPartType, LegacyModelBase>> map,
                            String familyId,
                            LegacyModelBase emitter,
                            LegacyModelBase switchSection,
                            LegacyModelBase grip,
                            LegacyModelBase pommel) {
        EnumMap<LightsaberPartType, LegacyModelBase> parts = new EnumMap<>(LightsaberPartType.class);
        parts.put(LightsaberPartType.EMITTER, emitter);
        parts.put(LightsaberPartType.SWITCH_SECTION, switchSection);
        parts.put(LightsaberPartType.GRIP, grip);
        parts.put(LightsaberPartType.POMMEL, pommel);
        map.put(familyId, parts);
    }
}
