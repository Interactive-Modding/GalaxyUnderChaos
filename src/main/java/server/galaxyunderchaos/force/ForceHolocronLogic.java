package server.galaxyunderchaos.force;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class ForceHolocronLogic {
    public static final int NORMAL_DATACRON_COST = 3;
    public static final int ANCIENT_DATACRON_COST = 6;
    public static final int FORCE_LEVEL_COST_MULTIPLIER = 3;

    private ForceHolocronLogic() {}

    public static boolean isAncientHolocron(ForceSide side) {
        return side == ForceSide.NEUTRAL;
    }

    public static boolean isAllowed(ForceSide side, ForcePower power) {
        if (power == null || power == ForcePower.FORCE_SHACKLES) {
            return false;
        }
        return switch (side) {
            case LIGHT -> power.side() == ForceSide.LIGHT || power.side() == ForceSide.NEUTRAL || power.side() == ForceSide.UNIVERSAL;
            case DARK -> power.side() == ForceSide.DARK || power.side() == ForceSide.NEUTRAL || power.side() == ForceSide.UNIVERSAL;
            case NEUTRAL -> power.side() == ForceSide.LIGHT || power.side() == ForceSide.DARK || power.side() == ForceSide.NEUTRAL || power.side() == ForceSide.UNIVERSAL;
            default -> false;
        };
    }

    public static ForceSide getDatacronBank(ForceSide holocronSide) {
        return isAncientHolocron(holocronSide) ? ForceSide.NEUTRAL : holocronSide;
    }

    public static int getDatacronCost(ForceSide holocronSide, ForcePower power) {
        if (power == null) {
            return 0;
        }

        int baseCost = isAncientHolocron(holocronSide) ? ANCIENT_DATACRON_COST : NORMAL_DATACRON_COST;

        /*
         * Force level upgrades are progression upgrades, not active powers, but they should
         * still consume datacrons. They cost 3x the normal cost:
         * - Normal Jedi/Sith holocrons: 9 datacrons
         * - Ancient holocron: 18 ancient datacrons
         */
        if (isForceLevelUpgrade(power)) {
            return baseCost * FORCE_LEVEL_COST_MULTIPLIER;
        }

        if (!power.isSelectable()) {
            return 0;
        }

        return baseCost;
    }

    public static boolean isForceLevelUpgrade(ForcePower power) {
        return power == ForcePower.FORCE_LEVEL1
                || power == ForcePower.FORCE_LEVEL2
                || power == ForcePower.FORCE_LEVEL3
                || power == ForcePower.FORCE_LEVEL4
                || power == ForcePower.FORCE_LEVEL5;
    }

    public static boolean hasDatacronsForUnlock(ForceSide holocronSide, ForceCapability cap, ForcePower power) {
        int cost = getDatacronCost(holocronSide, power);
        return cost <= 0 || cap.getDatacrons(getDatacronBank(holocronSide)) >= cost;
    }

    public static boolean canUnlock(ForceSide side, ForceCapability cap, ForcePower power) {
        if (!hasPrerequisites(side, cap, power)) {
            return false;
        }
        return hasDatacronsForUnlock(side, cap, power) && hasAlignmentForUnlock(side, cap, power);
    }

    public static int getAlignmentPointCost(ForceSide holocronSide, ForcePower power) {
        if (power == null || power == ForcePower.FORCE_SENSITIVITY || power == ForcePower.FORCE_LEVEL1) {
            return 0;
        }
        if (isForceLevelUpgrade(power)) {
            return Math.max(10, power.tier() * 12);
        }
        if (!power.isSelectable()) {
            return 0;
        }
        return Math.max(5, power.tier() * 8);
    }

    public static ForceSide getAlignmentBank(ForceSide holocronSide, ForcePower power) {
        if (power != null && power.side() != ForceSide.UNIVERSAL && power.side() != ForceSide.NEUTRAL) {
            return power.side();
        }
        return holocronSide == ForceSide.UNIVERSAL ? ForceSide.NEUTRAL : holocronSide;
    }

    public static boolean hasAlignmentForUnlock(ForceSide holocronSide, ForceCapability cap, ForcePower power) {
        int cost = getAlignmentPointCost(holocronSide, power);
        return cost <= 0 || cap.hasAlignmentPointsFor(getAlignmentBank(holocronSide, power), cost);
    }

    public static boolean hasPrerequisites(ForceSide side, ForceCapability cap, ForcePower power) {
        if (!isAllowed(side, power) || cap.hasPower(power)) {
            return false;
        }
        ForcePower parent = power.parent();
        if (parent != null && !cap.hasPower(parent)) {
            return false;
        }
        if (requiresForceMentor(power) && !cap.hasForceMentor()) {
            return false;
        }
        return !requiresCompletedStudentTraining(power) || cap.hasTrainedStudent();
    }

    public static boolean requiresForceMentor(ForcePower power) {
        return power == ForcePower.FORCE_SENSITIVITY;
    }

    public static boolean requiresCompletedStudentTraining(ForcePower power) {
        return power == ForcePower.FORCE_LEVEL3 || power == ForcePower.FORCE_LEVEL4 || power == ForcePower.FORCE_LEVEL5;
    }

    public static boolean isCompleteForSide(ForceSide side, ForceCapability cap) {
        for (ForcePower power : ForcePower.values()) {
            if (power.isSelectable() && isAllowed(side, power) && !cap.hasPower(power)) {
                return false;
            }
        }
        return true;
    }

    public static List<ForcePower> getDisplayPowers(ForceSide side) {
        return java.util.Arrays.stream(ForcePower.values())
                .filter(power -> isAllowed(side, power))
                .sorted(Comparator.comparingInt(ForcePower::iconY).thenComparingInt(ForcePower::iconX))
                .collect(Collectors.toList());
    }
}
