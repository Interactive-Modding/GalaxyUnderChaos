package server.galaxyunderchaos.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.EnumMap;
import java.util.function.Supplier;

public class CustomArmor {
    public static final ArmorMaterial TEMPLE_GUARD_ARMOR_MATERIAL = register(
            "temple_guard",
            15,
            4f,
            0.1f,
            () -> galaxyunderchaos.TEMPLE_GUARD_FABRIC.get(),
            new int[]{3, 6, 8, 3}
    );

    private static ArmorMaterial register(String name,
                                          int enchantability,
                                          float toughness,
                                          float knockbackResistance,
                                          Supplier<Item> ingredientItem,
                                          int[] typeProtection) {
        EnumMap<ArmorItem.Type, Integer> protection = new EnumMap<>(ArmorItem.Type.class);
        protection.put(ArmorItem.Type.BOOTS, typeProtection[0]);
        protection.put(ArmorItem.Type.CHESTPLATE, typeProtection[1]);
        protection.put(ArmorItem.Type.LEGGINGS, typeProtection[2]);
        protection.put(ArmorItem.Type.HELMET, typeProtection[3]);

        int[] durabilityPerType = new int[]{13, 15, 16, 11};

        return new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return durabilityPerType[type.getSlot().getIndex()] * 37;
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return protection.getOrDefault(type, 0);
            }

            @Override
            public int getEnchantmentValue() {
                return enchantability;
            }

            @Override
            public SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_NETHERITE;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.of(ingredientItem.get());
            }

            @Override
            public String getName() {
                return galaxyunderchaos.MODID + ":" + name;
            }

            @Override
            public float getToughness() {
                return toughness;
            }

            @Override
            public float getKnockbackResistance() {
                return knockbackResistance;
            }
        };
    }
}
