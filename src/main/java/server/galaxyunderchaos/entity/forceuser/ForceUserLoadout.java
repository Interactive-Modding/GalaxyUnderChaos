package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.lightsaber.AdvancedLightsaberLegacyHilts;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;
import server.galaxyunderchaos.lightsaber.DoubleLightsaberData;
import server.galaxyunderchaos.lightsaber.ModularLightsaberData;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class ForceUserLoadout {
    private static final String[] JEDI_COMMON_COLORS = {
            "blue", "blue", "blue",
            "green", "green", "green",
            "yellow", "cyan",
            "purple", "white"
    };

    private static final String[] JEDI_RARE_COLORS = {
            "orange", "amber", "gold", "lime_green", "turquoise", "magenta", "pink",
            "light_blue", "dark_blue", "deep_violet", "arctic_blue", "rose_pink"
    };

    private static final String[] SITH_NON_RED_COLORS = {
            "blood_orange", "blood_orange",
            "maroon", "maroon",
            "purple"
    };

    private static final String[] NEUTRAL_COLORS = {
            "blue", "green", "yellow", "cyan", "white", "purple", "orange", "amber",
            "gold", "lime_green", "turquoise", "magenta", "pink", "light_blue", "dark_blue",
            "deep_violet", "arctic_blue", "rose_pink", "red", "blood_orange", "maroon"
    };

    private ForceUserLoadout() {
    }

    public static ItemStack randomLightsaber(RandomSource random, ForceUserSide side) {
        return randomLightsaber(random, side, 0.005F, false);
    }

    public static ItemStack randomLightsaber(RandomSource random, ForceUserSide side, float modifierChance, boolean lordQuality) {
        String bladeColor = side.isDark() ? chooseSithColor(random) : chooseJediColor(random);
        List<String> hilts = new ArrayList<>(AdvancedLightsaberLegacyHilts.HILTS.keySet());
        String emitter = randomFrom(hilts, random);
        String switchSection = random.nextFloat() < (lordQuality ? 0.55F : 0.35F) ? randomFrom(hilts, random) : emitter;
        String grip = random.nextFloat() < (lordQuality ? 0.55F : 0.35F) ? randomFrom(hilts, random) : emitter;
        String pommel = random.nextFloat() < (lordQuality ? 0.55F : 0.35F) ? randomFrom(hilts, random) : emitter;

        ItemStack stack = ModularLightsaberData.createCustomLightsaber(bladeColor, emitter, switchSection, grip, pommel);
        addModifiers(stack, random, modifierChance, lordQuality ? 0.18F : 0.0005F);

        if (lordQuality && random.nextFloat() < 0.22F) {
            ItemStack secondEnd = randomLightsaber(random, side, modifierChance * 0.5F, false);
            ItemStack doubleStack = DoubleLightsaberData.create(stack, secondEnd);
            setLightsaberActive(doubleStack, false);
            return doubleStack;
        }

        setLightsaberActive(stack, false);
        return stack;
    }

    public static ItemStack randomNeutralLightsaber(RandomSource random) {
        String bladeColor = NEUTRAL_COLORS[random.nextInt(NEUTRAL_COLORS.length)];
        List<String> hilts = new ArrayList<>(AdvancedLightsaberLegacyHilts.HILTS.keySet());
        String emitter = randomFrom(hilts, random);
        String switchSection = random.nextFloat() < 0.45F ? randomFrom(hilts, random) : emitter;
        String grip = random.nextFloat() < 0.45F ? randomFrom(hilts, random) : emitter;
        String pommel = random.nextFloat() < 0.45F ? randomFrom(hilts, random) : emitter;
        ItemStack stack = ModularLightsaberData.createCustomLightsaber(bladeColor, emitter, switchSection, grip, pommel);
        addModifiers(stack, random, 0.025F, 0.003F);
        setLightsaberActive(stack, false);
        return stack;
    }

    public static List<ForcePower> randomPowers(RandomSource random, ForceUserSide side) {
        return randomPowers(random, side, 2);
    }

    public static List<ForcePower> randomPowers(RandomSource random, ForceUserSide side, int maxTier) {
        ForcePower[] pool = side.isDark()
                ? new ForcePower[] {
                ForcePower.LIGHTNING1, ForcePower.LIGHTNING2, ForcePower.LIGHTNING3,
                ForcePower.DRAIN1, ForcePower.DRAIN2, ForcePower.DRAIN3,
                ForcePower.WOUND1, ForcePower.WOUND2, ForcePower.WOUND3,
                ForcePower.PUSH1, ForcePower.PUSH2, ForcePower.PUSH3, ForcePower.SPEED, ForcePower.THROW1, ForcePower.RESIST1, ForcePower.RESIST2, ForcePower.SIGHT1
        }
                : new ForcePower[] {
                ForcePower.HEAL1, ForcePower.HEAL2, ForcePower.HEAL3,
                ForcePower.FORTIFY1, ForcePower.FORTIFY2, ForcePower.FORTIFY3,
                ForcePower.STUN1, ForcePower.STUN2, ForcePower.STUN3,
                ForcePower.PUSH1, ForcePower.PUSH2, ForcePower.PUSH3, ForcePower.SPEED, ForcePower.SIGHT1, ForcePower.RESIST1, ForcePower.RESIST2, ForcePower.THROW1
        };

        int count = Math.max(1, maxTier) + random.nextInt(3);
        List<ForcePower> result = new ArrayList<>();
        int guard = 0;
        while (result.size() < count && guard++ < 40) {
            ForcePower power = pool[random.nextInt(pool.length)];
            if (power.tier() <= maxTier && isAllowedForSide(power, side) && !result.contains(power)) {
                result.add(power);
            }
        }
        return result;
    }

    public static boolean isAllowedForSide(ForcePower power, ForceUserSide side) {
        if (power == null) {
            return false;
        }
        ForceSide powerSide = power.side();
        if (powerSide == ForceSide.NEUTRAL || powerSide == ForceSide.UNIVERSAL) {
            return true;
        }
        return side.isDark() ? powerSide == ForceSide.DARK : powerSide == ForceSide.LIGHT;
    }

    public static ItemStack holobookFor(ForceUserSide side) {
        return side.isDark()
                ? new ItemStack(galaxyunderchaos.SITH_HOLOBOOK.get())
                : new ItemStack(galaxyunderchaos.JEDI_HOLOBOOK.get());
    }

    public static ItemStack randomHolobook(RandomSource random) {
        return random.nextBoolean()
                ? new ItemStack(galaxyunderchaos.JEDI_HOLOBOOK.get())
                : new ItemStack(galaxyunderchaos.SITH_HOLOBOOK.get());
    }

    public static ItemStack datacronFor(ForceUserSide side) {
        return side.isDark() ? new ItemStack(galaxyunderchaos.SITH_DATACRON.get()) : new ItemStack(galaxyunderchaos.JEDI_DATACRON.get());
    }

    public static void setLightsaberActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean("LightsaberActive", active);
    }

    private static String chooseJediColor(RandomSource random) {
        return random.nextFloat() < 0.90F
                ? JEDI_COMMON_COLORS[random.nextInt(JEDI_COMMON_COLORS.length)]
                : JEDI_RARE_COLORS[random.nextInt(JEDI_RARE_COLORS.length)];
    }

    private static String chooseSithColor(RandomSource random) {
        return random.nextFloat() < 0.90F
                ? "red"
                : SITH_NON_RED_COLORS[random.nextInt(SITH_NON_RED_COLORS.length)];
    }

    private static void addModifiers(ItemStack stack, RandomSource random, float firstChance, float secondChance) {
        EnumSet<BladeModifierCrystal> modifiers = EnumSet.noneOf(BladeModifierCrystal.class);

        if (random.nextFloat() < firstChance) {
            modifiers.add(randomModifier(random));
        }
        if (!modifiers.isEmpty() && random.nextFloat() < secondChance) {
            modifiers.add(randomModifier(random));
        }

        ModularLightsaberData.setBladeModifiers(stack, modifiers);
    }

    private static BladeModifierCrystal randomModifier(RandomSource random) {
        BladeModifierCrystal[] values = BladeModifierCrystal.values();
        return values[random.nextInt(values.length)];
    }

    private static String randomFrom(List<String> values, RandomSource random) {
        return values.get(random.nextInt(values.size()));
    }
}
