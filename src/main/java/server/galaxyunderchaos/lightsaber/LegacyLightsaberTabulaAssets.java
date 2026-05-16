/*
 * SPDX-License-Identifier: LGPL-3.0-only
 *
 * This file is part of Galaxy Under Chaos.
 * It contains code, data, model geometry, behavior, or compatibility logic
 * copied, translated, ported, adapted from, or created to support content
 * derived from Advanced Lightsabers 1.2 by FiskFille, credited to FiskFille
 * and Void Adept.
 *
 * Modifications for Galaxy Under Chaos / Minecraft Forge 1.20.1 by
 *  Vitiate and contributors.
 */

package server.galaxyunderchaos.lightsaber;

import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class LegacyLightsaberTabulaAssets {
    private static final Map<String, Map<LightsaberPartType, ResourceLocation>> PARTS = createParts();
    private static final Map<String, ResourceLocation> FULL_HILTS = createFullHilts();

    private LegacyLightsaberTabulaAssets() {
    }

    public static boolean hasPartModel(String familyId, LightsaberPartType partType) {
        Map<LightsaberPartType, ResourceLocation> map = PARTS.get(familyId);
        return map != null && map.containsKey(partType);
    }

    public static ResourceLocation getPartModel(String familyId, LightsaberPartType partType) {
        Map<LightsaberPartType, ResourceLocation> map = PARTS.get(familyId);
        return map != null ? map.get(partType) : null;
    }

    public static boolean hasFullHiltModel(String familyId) {
        return FULL_HILTS.containsKey(familyId);
    }

    public static ResourceLocation getFullHiltModel(String familyId) {
        return FULL_HILTS.get(familyId);
    }

    private static Map<String, Map<LightsaberPartType, ResourceLocation>> createParts() {
        Map<String, Map<LightsaberPartType, ResourceLocation>> result = new HashMap<>();
        addFamily(result, "droideka", "emitter_droideka", "switch_section_droideka", "body_droideka", "pommel_droideka");
        addFamily(result, "fulcrum", "emitter_fulcrum", "switch_section_fulcrum", "body_fulcrum", "pommel_fulcrum");
        addFamily(result, "fury", "emitter_fury", "switch_section_fury", "body_fury", "pommel_fury");
        addFamily(result, "graflex", "emitter_graflex", "switch_section_graflex", "body_graflex", "pommel_graflex");
        addFamily(result, "juggernaut", "emitter_juggernaut", "switch_section_juggernaut", "body_juggernaut", "pommel_juggernaut");
        addFamily(result, "knighted", "emitter_knighted", "switch_section_knighted", "body_knighted", "pommel_knighted");
        addFamily(result, "mandalorian", "emitter_mandalorian", "switch_section_mandalorian", "body_mandalorian", "pommel_mandalorian");
        addFamily(result, "mauler", "emitter_mauler", "switch_section_mauler", "body_mauler", "pommel_mauler");
        addFamily(result, "mechanical", "emitter_mechanical", "switch_section_mechanical", "body_mechanical", "pommel_mechanical");
        addFamily(result, "prodigal_son", "emitter_prodigal_son", "switch_section_prodigal_son", "body_prodigal_son", "pommel_prodigal_son");
        addFamily(result, "redeemer", "emitter_redeemer", "switch_section_redeemer", "body_redeemer", "pommel_redeemer");
        addFamily(result, "vaid", "emitter_vaid_modern", "switch_section_vaid_modern", "body_vaid_modern", "pommel_vaid_modern");
        addFamily(result, "vaid_modern", "emitter_vaid_modern", "switch_section_vaid_modern", "body_vaid_modern", "pommel_vaid_modern");
        addFamily(result, "vaid_ancient", "emitter_vaid_ancient", "switch_section_vaid_ancient", "body_vaid_ancient", "pommel_vaid_ancient");
        return Map.copyOf(result);
    }

    private static Map<String, ResourceLocation> createFullHilts() {
        Map<String, ResourceLocation> result = new HashMap<>();
        result.put("fulcrum", rl("fulcrum"));
        result.put("mandalorian", rl("mandalorian"));
        result.put("vaid", rl("vaid"));
        result.put("vaid_modern", rl("vaid"));
        result.put("vaid_ancient", rl("vaid"));
        result.put("generic_fisk", rl("fisks_lightsaber"));
        return Map.copyOf(result);
    }

    private static void addFamily(Map<String, Map<LightsaberPartType, ResourceLocation>> result,
                                  String familyId,
                                  String emitter,
                                  String switchSection,
                                  String body,
                                  String pommel) {
        Map<LightsaberPartType, ResourceLocation> map = new EnumMap<>(LightsaberPartType.class);
        map.put(LightsaberPartType.EMITTER, rl(emitter));
        map.put(LightsaberPartType.SWITCH_SECTION, rl(switchSection));
        map.put(LightsaberPartType.GRIP, rl(body));
        map.put(LightsaberPartType.POMMEL, rl(pommel));
        result.put(familyId, Map.copyOf(map));
    }

    private static ResourceLocation rl(String path) {
        return new ResourceLocation("galaxyunderchaos", "tabula/lightsabers/" + path + ".tbl");
    }
}