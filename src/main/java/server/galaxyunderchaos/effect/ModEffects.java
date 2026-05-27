package server.galaxyunderchaos.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.EnumMap;
import java.util.Map;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, galaxyunderchaos.MODID);

    private static final Map<ForcePower, RegistryObject<MobEffect>> FORCE_POWER_EFFECTS = new EnumMap<>(ForcePower.class);

    static {
        // Light side
        registerForcePower(ForcePower.HEAL1, MobEffectCategory.BENEFICIAL, 0x6EEA8A);
        registerForcePower(ForcePower.HEAL2, MobEffectCategory.BENEFICIAL, 0x55D978);
        registerForcePower(ForcePower.HEAL3, MobEffectCategory.BENEFICIAL, 0x3ECF66);
        registerForcePower(ForcePower.FORTIFY1, MobEffectCategory.BENEFICIAL, 0xD8C76E);
        registerForcePower(ForcePower.FORTIFY2, MobEffectCategory.BENEFICIAL, 0xE0CD5A);
        registerForcePower(ForcePower.FORTIFY3, MobEffectCategory.BENEFICIAL, 0xF0D94A);
        registerForcePower(ForcePower.STUN1, MobEffectCategory.HARMFUL, 0x83D8FF);
        registerForcePower(ForcePower.STUN2, MobEffectCategory.HARMFUL, 0x66C9FF);
        registerForcePower(ForcePower.STUN3, MobEffectCategory.HARMFUL, 0x47B8FF);
        
        registerForcePower(ForcePower.ELECTRIC_JUDGMENT1, MobEffectCategory.HARMFUL, 0xFFF08A);
        registerForcePower(ForcePower.ELECTRIC_JUDGMENT2, MobEffectCategory.HARMFUL, 0xFFE057);
        registerForcePower(ForcePower.ELECTRIC_JUDGMENT3, MobEffectCategory.HARMFUL, 0xFFD026);
        registerForcePower(ForcePower.TUTAMINIS, MobEffectCategory.BENEFICIAL, 0xFFE8A3);
        registerForcePower(ForcePower.WALL_OF_LIGHT, MobEffectCategory.HARMFUL, 0xFFF6C8);

        // Dark side
        registerForcePower(ForcePower.DRAIN1, MobEffectCategory.HARMFUL, 0x8E3DC8);
        registerForcePower(ForcePower.DRAIN2, MobEffectCategory.HARMFUL, 0x7D2AB8);
        registerForcePower(ForcePower.DRAIN3, MobEffectCategory.HARMFUL, 0x68199E);
        registerForcePower(ForcePower.LIGHTNING1, MobEffectCategory.HARMFUL, 0x805DFF);
        registerForcePower(ForcePower.LIGHTNING2, MobEffectCategory.HARMFUL, 0x6947EA);
        registerForcePower(ForcePower.LIGHTNING3, MobEffectCategory.HARMFUL, 0x5232D4);
        registerForcePower(ForcePower.WOUND1, MobEffectCategory.HARMFUL, 0xCB3434);
        registerForcePower(ForcePower.WOUND2, MobEffectCategory.HARMFUL, 0xB52525);
        registerForcePower(ForcePower.WOUND3, MobEffectCategory.HARMFUL, 0x9E1818);
        registerForcePower(ForcePower.FORCE_SCREAM1, MobEffectCategory.HARMFUL, 0x9B1144);
        registerForcePower(ForcePower.FORCE_SCREAM2, MobEffectCategory.HARMFUL, 0xB7134E);
        registerForcePower(ForcePower.FORCE_SCREAM3, MobEffectCategory.HARMFUL, 0xD21858);
        registerForcePower(ForcePower.FORCE_DESTRUCTION1, MobEffectCategory.HARMFUL, 0xFF4B1A);
        registerForcePower(ForcePower.FORCE_DESTRUCTION2, MobEffectCategory.HARMFUL, 0xFF351A);
        registerForcePower(ForcePower.FORCE_DESTRUCTION3, MobEffectCategory.HARMFUL, 0xE71616);
        registerForcePower(ForcePower.FORCE_SHACKLES, MobEffectCategory.HARMFUL, 0x6B0F95);

        // Neutral / utility
        registerForcePower(ForcePower.STEALTH, MobEffectCategory.BENEFICIAL, 0x6F6F86);
        registerForcePower(ForcePower.SPEED, MobEffectCategory.BENEFICIAL, 0x7FD7FF);
        registerForcePower(ForcePower.REBOUND, MobEffectCategory.BENEFICIAL, 0xCFA95A);
        registerForcePower(ForcePower.SIGHT1, MobEffectCategory.BENEFICIAL, 0xBDEBFF);
        registerForcePower(ForcePower.SIGHT2, MobEffectCategory.BENEFICIAL, 0xA5DFFF);
        registerForcePower(ForcePower.SIGHT3, MobEffectCategory.BENEFICIAL, 0x8BD4FF);
        registerForcePower(ForcePower.MEDITATION1, MobEffectCategory.BENEFICIAL, 0x76B7E8);
        registerForcePower(ForcePower.MEDITATION2, MobEffectCategory.BENEFICIAL, 0x5FA8DE);
        registerForcePower(ForcePower.MEDITATION3, MobEffectCategory.BENEFICIAL, 0x4998D4);
        registerForcePower(ForcePower.THROW1, MobEffectCategory.BENEFICIAL, 0xCACACA);
        registerForcePower(ForcePower.THROW2, MobEffectCategory.BENEFICIAL, 0xE0E0E0);
        registerForcePower(ForcePower.RESIST1, MobEffectCategory.BENEFICIAL, 0xFFB34A);
        registerForcePower(ForcePower.RESIST2, MobEffectCategory.BENEFICIAL, 0xFFA235);
        registerForcePower(ForcePower.RESIST3, MobEffectCategory.BENEFICIAL, 0xFF8F20);
        registerForcePower(ForcePower.PUSH1, MobEffectCategory.HARMFUL, 0xA7D6FF);
        registerForcePower(ForcePower.PUSH2, MobEffectCategory.HARMFUL, 0x8EC9FF);
        registerForcePower(ForcePower.PUSH3, MobEffectCategory.HARMFUL, 0x72B9FF);
        registerForcePower(ForcePower.PULL1, MobEffectCategory.HARMFUL, 0x91C8FF);
        registerForcePower(ForcePower.PULL2, MobEffectCategory.HARMFUL, 0x78B8FF);
        registerForcePower(ForcePower.PULL3, MobEffectCategory.HARMFUL, 0x5EA6FF);
        registerForcePower(ForcePower.FORCE_LEAP, MobEffectCategory.BENEFICIAL, 0xA8F0FF);
        registerForcePower(ForcePower.FORCE_PROJECTION1, MobEffectCategory.BENEFICIAL, 0xC8C7FF);
        registerForcePower(ForcePower.FORCE_PROJECTION2, MobEffectCategory.BENEFICIAL, 0xAFAEFF);
        registerForcePower(ForcePower.FORCE_PROJECTION3, MobEffectCategory.BENEFICIAL, 0x9694FF);
    }

    private ModEffects() {}

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

    private static RegistryObject<MobEffect> registerForcePower(ForcePower power, MobEffectCategory category, int color) {
        RegistryObject<MobEffect> effect = MOB_EFFECTS.register(power.id(), () -> new ForceMobEffect(category, color));
        FORCE_POWER_EFFECTS.put(power, effect);
        return effect;
    }

    public static MobEffect getForceEffect(ForcePower power) {
        RegistryObject<MobEffect> effect = FORCE_POWER_EFFECTS.get(power);
        return effect == null ? null : effect.get();
    }

    public static boolean hasAnyForceEffect(LivingEntity entity, ForcePower... powers) {
        for (ForcePower power : powers) {
            MobEffect effect = getForceEffect(power);
            if (effect != null && entity.hasEffect(effect)) {
                return true;
            }
        }
        return false;
    }
}
