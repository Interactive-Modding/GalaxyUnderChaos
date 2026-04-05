package server.galaxyunderchaos.lightsaber;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.item.HiltItem;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.item.LightsaberPartItem;

import java.util.EnumSet;
import java.util.List;


public class LightsaberCrafting {
    public static ItemStack applyModifierCrystals(ItemStack lightsaber, List<ItemStack> modifierCrystals) {
        if (!(lightsaber.getItem() instanceof LightsaberItem) || lightsaber.getItem() instanceof server.galaxyunderchaos.item.DoubleLightsaberItem) {
            return ItemStack.EMPTY;
        }

        EnumSet<BladeModifierCrystal> modifiers = ModularLightsaberData.getBladeModifiers(lightsaber);
        boolean changed = false;

        for (ItemStack modifierStack : modifierCrystals) {
            BladeModifierCrystal crystal = BladeModifierCrystal.fromStack(modifierStack);
            if (crystal == null || modifiers.contains(crystal)) {
                continue;
            }
            if (modifiers.size() >= ModularLightsaberData.MAX_BLADE_MODIFIERS) {
                break;
            }
            modifiers.add(crystal);
            changed = true;
        }

        if (!changed) {
            return ItemStack.EMPTY;
        }

        ItemStack result = lightsaber.copy();
        ModularLightsaberData.setBladeModifiers(result, modifiers);
        return result;
    }

    public static ItemStack craftDoubleLightsaber(ItemStack upper, ItemStack lower) {
        if (!DoubleLightsaberData.canCreateFrom(upper) || !DoubleLightsaberData.canCreateFrom(lower)) {
            return ItemStack.EMPTY;
        }
        return DoubleLightsaberData.create(upper, lower);
    }

    public static ItemStack craftLightsaber(ItemStack hilt, ItemStack kyberCrystal) {
        if (hilt.isEmpty() || kyberCrystal.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ResourceLocation kyberRegistryName = BuiltInRegistries.ITEM.getKey(kyberCrystal.getItem());
        if (kyberRegistryName == null) {
            return ItemStack.EMPTY;
        }

        String bladeColor = BladeColorRegistry.getBladeColor(kyberRegistryName.getPath());
        if ("unknown".equals(bladeColor)) {
            return ItemStack.EMPTY;
        }

        if (hilt.getItem() instanceof HiltItem hiltItem) {
            String hiltId = hiltItem.getHiltId();
            if (ModularLightsaberData.isLegacyFamily(hiltId)) {
                return ModularLightsaberData.createCustomLightsaberFromPreset(bladeColor, hiltId);
            }
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack craftLightsaber(ItemStack emitter, ItemStack switchSection, ItemStack grip, ItemStack pommel, ItemStack kyberCrystal) {
        if (!(emitter.getItem() instanceof LightsaberPartItem emitterItem)
                || !(switchSection.getItem() instanceof LightsaberPartItem switchItem)
                || !(grip.getItem() instanceof LightsaberPartItem gripItem)
                || !(pommel.getItem() instanceof LightsaberPartItem pommelItem)) {
            return ItemStack.EMPTY;
        }

        if (emitterItem.getPartType() != LightsaberPartType.EMITTER
                || switchItem.getPartType() != LightsaberPartType.SWITCH_SECTION
                || gripItem.getPartType() != LightsaberPartType.GRIP
                || pommelItem.getPartType() != LightsaberPartType.POMMEL) {
            return ItemStack.EMPTY;
        }

        ResourceLocation kyberRegistryName = BuiltInRegistries.ITEM.getKey(kyberCrystal.getItem());
        if (kyberRegistryName == null) {
            return ItemStack.EMPTY;
        }

        String bladeColor = BladeColorRegistry.getBladeColor(kyberRegistryName.getPath());
        if ("unknown".equals(bladeColor)) {
            return ItemStack.EMPTY;
        }

        return ModularLightsaberData.createCustomLightsaber(
                bladeColor,
                emitterItem.getFamilyId(),
                switchItem.getFamilyId(),
                gripItem.getFamilyId(),
                pommelItem.getFamilyId()
        );
    }
}
