package server.galaxyunderchaos.lightsaber;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Portable metadata copied from AdvancedLightsabers-1.2's old Hilt classes.
 *
 * This is intentionally data-only. The original renderer stack depended on 1.7.10
 * IItemRenderer/GL11/Fisk registry code and cannot be dropped into Forge 1.20.1
 * unchanged, but these dimensions/defaults are still useful when rebuilding modern
 * item/entity/BEWLR saber rendering.
 */
public final class AdvancedLightsaberLegacyHilts {
    public static final Map<String, LegacyHiltSpec> HILTS = create();

    private AdvancedLightsaberLegacyHilts() {
    }

    private static Map<String, LegacyHiltSpec> create() {
        Map<String, LegacyHiltSpec> hilts = new LinkedHashMap<>();
        hilts.put("droideka", new LegacyHiltSpec("droideka", "amber", 5.0F, 9.6F, 29.4F, 6.5F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("fulcrum", new LegacyHiltSpec("fulcrum", "white", 19.0F, 10.0F, 30.0F, 7.0F, false, false, 0.0F, 0.0F, 0.0F, new float[] {11.7F, 2.0F, 9.8F, 3.0F, 9.0F}, new BladeModifierCrystal[] {BladeModifierCrystal.COMPRESSED}));
        hilts.put("fury", new LegacyHiltSpec("fury", "purple", 19.0F, 5.6F, 16.0F, 8.3F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("graflex", new LegacyHiltSpec("graflex", "deep_blue", 16.0F, 8.8F, 16.0F, 1.0F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("imperial", new LegacyHiltSpec("imperial", "red", 7.6F, 3.7F, 15.8F, 6.5F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("juggernaut", new LegacyHiltSpec("juggernaut", "red", 14.7F, 12.4F, 16.0F, 7.0F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("knighted", new LegacyHiltSpec("knighted", "red", 12.6F, 8.4F, 20.0F, 13.3F, false, true, 0.0F, 0.083F, 0.23F, new float[0], new BladeModifierCrystal[] {BladeModifierCrystal.CRACKED}));
        hilts.put("mandalorian", new LegacyHiltSpec("mandalorian", "white", 12.55F, 2.87F, 26.0F, 7.45F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[] {BladeModifierCrystal.INVERTING, BladeModifierCrystal.FINE_CUT}));
        hilts.put("mauler", new LegacyHiltSpec("mauler", "red", 18.0F, 12.0F, 21.6F, 0.25F, true, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("mechanical", new LegacyHiltSpec("mechanical", "red", 16.0F, 8.8F, 16.0F, 2.5F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("prodigal_son", new LegacyHiltSpec("prodigal_son", "green", 31.0F, 8.4F, 13.3F, 2.0F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("rebel", new LegacyHiltSpec("rebel", "medium_blue", 12.9F, 7.0F, 20.0F, 6.0F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[] {BladeModifierCrystal.COMPRESSED}));
        hilts.put("reborn", new LegacyHiltSpec("reborn", "purple", 14.86F, 10.0F, 19.0F, 6.0F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("redeemer", new LegacyHiltSpec("redeemer", "deep_blue", 30.0F, 8.0F, 12.3F, 1.0F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        hilts.put("vaid", new LegacyHiltSpec("vaid", "purple", 18.5F, 9.2F, 22.37F, 6.6F, false, false, 0.0F, 0.0F, 0.0F, new float[0], new BladeModifierCrystal[0]));
        return Map.copyOf(hilts);
    }

    public record LegacyHiltSpec(
            String id,
            String legacyDefaultBladeColor,
            float emitterHeight,
            float switchSectionHeight,
            float bodyHeight,
            float pommelHeight,
            boolean doubleBladed,
            boolean hasCrossguard,
            float crossguardX,
            float crossguardY,
            float crossguardZ,
            float[] pommelAlignmentOps,
            BladeModifierCrystal[] defaultBladeModifiers
    ) {
        public float[] pommelAlignmentOps() {
            return pommelAlignmentOps.clone();
        }

        public BladeModifierCrystal[] defaultBladeModifiers() {
            return defaultBladeModifiers.clone();
        }

        public float totalHeight() {
            return emitterHeight + switchSectionHeight + bodyHeight + pommelHeight;
        }
    }
}
