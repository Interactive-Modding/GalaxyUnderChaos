package server.galaxyunderchaos.lightsaber;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum representing every canonical lightsaber form supported by the mod.
 *
 * Each constant carries its own:
 * <ul>
 *     <li>Display‑friendly name</li>
 *     <li>Primary {@link AttributeModifier} applied when the form is active</li>
 *     <li>Reference to a server‑side + client‑side animation file under resources/assets/<modid>/animations</li>
 * </ul>
 */
public enum LightsaberForm {

    SHII_CHO(
            "Shii-Cho",
            new AttributeModifier(
                    "shii_cho_speed",
                    0.10, AttributeModifier.Operation.MULTIPLY_TOTAL
            ),
            rl("lightsaber/stance/shii_cho.anim")
    ),

    MAKASHI(
            "Makashi",
            new AttributeModifier(
                    "makashi_damage",
                    2.0, AttributeModifier.Operation.ADDITION
            ),
            rl("lightsaber/stance/makashi.anim")
    ),

    SORESU(
            "Soresu",
            new AttributeModifier(
                    "soresu_defense",
                    0.20, AttributeModifier.Operation.MULTIPLY_TOTAL
            ),
            rl("lightsaber/stance/soresu.anim")
    ),

    ATARU(
            "Ataru",
            new AttributeModifier(
                    "ataru_attack_speed",
                    0.15, AttributeModifier.Operation.MULTIPLY_TOTAL
            ),
            rl("lightsaber/stance/ataru.anim")
    ),

    SHIEN(
            "Shien / DjemSo",
            new AttributeModifier(
                    "shien_counter",
                    1.50, AttributeModifier.Operation.ADDITION
            ),
            rl("lightsaber/stance/shien.anim")
    ),

    NIMAN(
            "Niman",
            new AttributeModifier(
                    "niman_balance",
                    0.05, AttributeModifier.Operation.MULTIPLY_TOTAL
            ),
            rl("lightsaber/stance/niman.anim")
    ),

    JUYO(
            "Juyo / Vaapad",
            new AttributeModifier(
                    "juyo_power",
                    3.00, AttributeModifier.Operation.ADDITION
            ),
            rl("lightsaber/stance/juyo.anim")
    );

    private final String displayName;
    private final AttributeModifier modifier;
    private final ResourceLocation animation;

    LightsaberForm(String displayName, AttributeModifier modifier, ResourceLocation animation) {
        this.displayName = displayName;
        this.modifier = modifier;
        this.animation = animation;
    }

    public String getDisplayName() { return displayName; }
    public AttributeModifier getModifier() { return modifier; }
    public ResourceLocation getAnimation() { return animation; }

    private static ResourceLocation rl(String path) {
        // Animations should be placed under resources/assets/<modid>/animations/
        return new ResourceLocation(galaxyunderchaos.MODID, "animations/" + path);
    }

    private static final Map<String, LightsaberForm> BY_ID =
            Stream.of(values()).collect(Collectors.toUnmodifiableMap(Enum::name, f -> f));

    private static final Map<String, LightsaberForm> BY_DISPLAY_NAME =
            Stream.of(values()).collect(Collectors.toUnmodifiableMap(
                    f -> normalize(f.displayName),
                    f -> f
            ));

    public static LightsaberForm fromId(String id) {
        return BY_ID.getOrDefault(id, SHII_CHO);
    }

    public static LightsaberForm fromDisplayName(String name) {
        return BY_DISPLAY_NAME.getOrDefault(normalize(name), SHII_CHO);
    }

    public LightsaberForm next() {
        LightsaberForm[] vals = values();
        return vals[(ordinal() + 1) % vals.length];
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
    }
}
