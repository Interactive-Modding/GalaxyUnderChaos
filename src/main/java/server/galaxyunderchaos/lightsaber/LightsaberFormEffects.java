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

    private static record FormEffect(Attribute attribute, AttributeModifier modifier) {}

    static {
        FORM_EFFECTS.put("Shii-Cho", new FormEffect(
                Attributes.MOVEMENT_SPEED,
                new AttributeModifier("shii_cho_speed", 0.10, AttributeModifier.Operation.MULTIPLY_TOTAL)
        ));
        FORM_EFFECTS.put("Makashi", new FormEffect(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier("makashi_damage", 2.0, AttributeModifier.Operation.ADDITION)
        ));
        FORM_EFFECTS.put("Soresu", new FormEffect(
                Attributes.KNOCKBACK_RESISTANCE,
                new AttributeModifier("soresu_control", 0.20, AttributeModifier.Operation.MULTIPLY_TOTAL)
        ));
        FORM_EFFECTS.put("Ataru", new FormEffect(
                Attributes.ATTACK_SPEED,
                new AttributeModifier("ataru_attack_speed", 0.30, AttributeModifier.Operation.MULTIPLY_TOTAL)
        ));
        FORM_EFFECTS.put("Shien / Djem So", new FormEffect(
                Attributes.ATTACK_KNOCKBACK,
                new AttributeModifier("shien_counter", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL)
        ));
        FORM_EFFECTS.put("Niman", new FormEffect(
                Attributes.MOVEMENT_SPEED,
                new AttributeModifier("niman_balance", 0.05, AttributeModifier.Operation.MULTIPLY_TOTAL)
        ));
        FORM_EFFECTS.put("Juyo / Vaapad", new FormEffect(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier("juyo_power", 4.0, AttributeModifier.Operation.ADDITION)
        ));
    }

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

    public static void removeAllEffects(Player player) {
        for (FormEffect effect : FORM_EFFECTS.values()) {
            AttributeInstance inst = player.getAttribute(effect.attribute());
            if (inst != null && inst.getModifier(effect.modifier().getId()) != null) {
                inst.removeModifier(effect.modifier().getId());
            }
        }
    }

    public static int getMaxStaminaForForm(String form) {
        return switch (form == null ? "" : form) {
            case "Makashi" -> 110;
            case "Soresu" -> 130;
            case "Ataru" -> 90;
            case "Shien / Djem So" -> 115;
            case "Niman" -> 105;
            case "Juyo / Vaapad" -> 85;
            default -> 100;
        };
    }

    public static float getBlockCostMultiplier(String form) {
        return switch (form == null ? "" : form) {
            case "Makashi" -> 0.90F;
            case "Soresu" -> 0.72F;
            case "Ataru" -> 1.12F;
            case "Shien / Djem So" -> 0.88F;
            case "Niman" -> 0.98F;
            case "Juyo / Vaapad" -> 1.20F;
            default -> 1.0F;
        };
    }

    public static float getMovementDrainMultiplier(String form) {
        return switch (form == null ? "" : form) {
            case "Makashi" -> 0.95F;
            case "Soresu" -> 0.85F;
            case "Ataru" -> 1.25F;
            case "Shien / Djem So" -> 1.05F;
            case "Niman" -> 1.0F;
            case "Juyo / Vaapad" -> 1.18F;
            default -> 1.0F;
        };
    }

    public static float getRegenMultiplier(String form) {
        return switch (form == null ? "" : form) {
            case "Makashi" -> 1.05F;
            case "Soresu" -> 1.20F;
            case "Ataru" -> 0.90F;
            case "Shien / Djem So" -> 1.0F;
            case "Niman" -> 1.05F;
            case "Juyo / Vaapad" -> 0.80F;
            default -> 1.0F;
        };
    }
}
