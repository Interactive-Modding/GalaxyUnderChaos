package server.galaxyunderchaos.lightsaber;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class LightsaberFormEffects {

    private static final Map<String, FormEffect> FORM_EFFECTS = new HashMap<>();

    // Pairs a Holder<Attribute> with its corresponding AttributeModifier
    private static record FormEffect(Attribute attribute, AttributeModifier modifier) {}

    static {
        FORM_EFFECTS.put("Shii-Cho", new FormEffect(
                Attributes.MOVEMENT_SPEED,
                new AttributeModifier(
                        "shii_cho_speed",
                        0.15,  // +15% movement speed
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        ));
        FORM_EFFECTS.put("Makashi", new FormEffect(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        "makashi_damage",
                        4.0,   // +4 attack damage
                        AttributeModifier.Operation.ADDITION
                )
        ));
        FORM_EFFECTS.put("Soresu", new FormEffect(
                Attributes.ARMOR,
                new AttributeModifier(
                        "soresu_defense",
                        0.5,  // +50% base armor
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        ));
        FORM_EFFECTS.put("Ataru", new FormEffect(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        "ataru_attack_speed",
                        0.30,  // +30% attack speed
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        ));
        FORM_EFFECTS.put("Shien / Djem So", new FormEffect(
                Attributes.KNOCKBACK_RESISTANCE,
                new AttributeModifier(
                        "shien_counter",
                        0.25,  // +25% knockback resistance
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        ));
        FORM_EFFECTS.put("Niman", new FormEffect(
                Attributes.ATTACK_KNOCKBACK,
                new AttributeModifier(
                        "niman_balance",
                        0.15,  // +15% attack knockback
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                )
        ));
        FORM_EFFECTS.put("Juyo / Vaapad", new FormEffect(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        "juyo_power",
                        6.0,   // +6 attack damage
                        AttributeModifier.Operation.ADDITION
                )
        ));
    }

    /**
     * Clears existing form modifiers and applies the one corresponding to the selected form.
     */
    public static void applyEffects(Player player, String form) {
        removeAllEffects(player);
        FormEffect effect = FORM_EFFECTS.get(form);
        if (effect != null) {
            AttributeInstance inst = player.getAttribute(effect.attribute());
            if (inst != null && inst.getModifier(effect.modifier().getId()) == null) {
                inst.addTransientModifier(effect.modifier());
            }
        }
    }

    /**
     * Removes every form modifier from its associated attribute.
     */
    public static void removeAllEffects(Player player) {
        for (FormEffect effect : FORM_EFFECTS.values()) {
            AttributeInstance inst = player.getAttribute(effect.attribute());
            if (inst != null && inst.getModifier(effect.modifier().getId()) != null) {
                inst.removeModifier(effect.modifier().getId());
            }
        }
    }
}
