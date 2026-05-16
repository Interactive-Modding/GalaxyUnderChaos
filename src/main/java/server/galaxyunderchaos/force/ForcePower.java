package server.galaxyunderchaos.force;

import java.util.*;

public enum ForcePower {
    FORCE_SENSITIVITY("forceSensitivity", "Force Sensitivity", ForceSide.UNIVERSAL, ForcePowerType.META, 0, null, 14, 0),
    LIGHT_SIDE("lightSide", "Light Side", ForceSide.LIGHT, ForcePowerType.META, 0, FORCE_SENSITIVITY, 13, 1),
    DARK_SIDE("darkSide", "Dark Side", ForceSide.DARK, ForcePowerType.META, 0, FORCE_SENSITIVITY, 13, 2),
    NEUTRAL("neutral", "Neutral", ForceSide.NEUTRAL, ForcePowerType.META, 0, FORCE_SENSITIVITY, 14, 1),
    FORCE_LEVEL1("level1", "Force Level I", ForceSide.UNIVERSAL, ForcePowerType.META, 0, FORCE_SENSITIVITY, 11, 1),
    FORCE_LEVEL2("level2", "Force Level II", ForceSide.UNIVERSAL, ForcePowerType.META, 0, FORCE_LEVEL1, 12, 1),
    FORCE_LEVEL3("level3", "Force Level III", ForceSide.UNIVERSAL, ForcePowerType.META, 0, FORCE_LEVEL2, 10, 2),
    FORCE_LEVEL4("level4", "Force Level IV", ForceSide.UNIVERSAL, ForcePowerType.META, 0, FORCE_LEVEL3, 11, 2),
    FORCE_LEVEL5("level5", "Force Level V", ForceSide.UNIVERSAL, ForcePowerType.META, 0, FORCE_LEVEL4, 12, 2),

    HEAL1("heal1", "Force Heal I", ForceSide.LIGHT, ForcePowerType.PER_USE, 50, LIGHT_SIDE, 0, 0),
    HEAL2("heal2", "Force Heal II", ForceSide.LIGHT, ForcePowerType.PER_USE, 125, HEAL1, 0, 1),
    HEAL3("heal3", "Force Heal III", ForceSide.LIGHT, ForcePowerType.PER_USE, 175, HEAL2, 0, 2),
    FORTIFY1("fortify1", "Force Fortify I", ForceSide.LIGHT, ForcePowerType.BUFF, 40, LIGHT_SIDE, 1, 0),
    FORTIFY2("fortify2", "Force Fortify II", ForceSide.LIGHT, ForcePowerType.BUFF, 55, FORTIFY1, 1, 1),
    FORTIFY3("fortify3", "Force Fortify III", ForceSide.LIGHT, ForcePowerType.BUFF, 70, FORTIFY2, 1, 2),
    STUN1("stun1", "Force Stun I", ForceSide.LIGHT, ForcePowerType.PER_USE, 50, LIGHT_SIDE, 2, 0),
    STUN2("stun2", "Force Stun II", ForceSide.LIGHT, ForcePowerType.PER_USE, 100, STUN1, 2, 1),
    STUN3("stun3", "Force Stun III", ForceSide.LIGHT, ForcePowerType.PER_USE, 150, STUN2, 2, 2),

    DRAIN1("drain1", "Force Drain I", ForceSide.DARK, ForcePowerType.PER_USE, 100, DARK_SIDE, 3, 0),
    DRAIN2("drain2", "Force Drain II", ForceSide.DARK, ForcePowerType.PER_USE, 150, DRAIN1, 3, 1),
    DRAIN3("drain3", "Force Drain III", ForceSide.DARK, ForcePowerType.PER_USE, 200, DRAIN2, 3, 2),
    LIGHTNING1("lightning1", "Force Lightning I", ForceSide.DARK, ForcePowerType.PER_USE, 50, DARK_SIDE, 4, 0),
    LIGHTNING2("lightning2", "Force Lightning II", ForceSide.DARK, ForcePowerType.PER_USE, 85, LIGHTNING1, 4, 1),
    LIGHTNING3("lightning3", "Force Lightning III", ForceSide.DARK, ForcePowerType.PER_USE, 120, LIGHTNING2, 4, 2),
    WOUND1("wound1", "Force Wound I", ForceSide.DARK, ForcePowerType.PER_USE, 50, DARK_SIDE, 5, 0),
    WOUND2("wound2", "Force Wound II", ForceSide.DARK, ForcePowerType.PER_USE, 115, WOUND1, 5, 1),
    WOUND3("wound3", "Force Wound III", ForceSide.DARK, ForcePowerType.PER_USE, 170, WOUND2, 5, 2),

    STEALTH("stealth", "Force Stealth", ForceSide.NEUTRAL, ForcePowerType.BUFF, 40, NEUTRAL, 11, 0),
    SPEED("speed", "Force Speed", ForceSide.NEUTRAL, ForcePowerType.BUFF, 30, NEUTRAL, 12, 0),
    REBOUND("rebound", "Force Rebound", ForceSide.NEUTRAL, ForcePowerType.PASSIVE, 0, NEUTRAL, 13, 0),
    SIGHT1("sight1", "Force Sight I", ForceSide.NEUTRAL, ForcePowerType.BUFF, 25, NEUTRAL, 6, 0),
    SIGHT2("sight2", "Force Sight II", ForceSide.NEUTRAL, ForcePowerType.BUFF, 35, SIGHT1, 6, 1),
    SIGHT3("sight3", "Force Sight III", ForceSide.NEUTRAL, ForcePowerType.BUFF, 45, SIGHT2, 6, 2),
    MEDITATION1("meditation1", "Meditation I", ForceSide.NEUTRAL, ForcePowerType.BUFF, 50, NEUTRAL, 7, 0),
    MEDITATION2("meditation2", "Meditation II", ForceSide.NEUTRAL, ForcePowerType.BUFF, 75, MEDITATION1, 7, 1),
    MEDITATION3("meditation3", "Meditation III", ForceSide.NEUTRAL, ForcePowerType.BUFF, 100, MEDITATION2, 7, 2),
    THROW1("throw1", "Blade Throw I", ForceSide.NEUTRAL, ForcePowerType.PER_USE, 50, NEUTRAL, 10, 0),
    THROW2("throw2", "Blade Throw II", ForceSide.NEUTRAL, ForcePowerType.PER_USE, 75, THROW1, 10, 1),
    RESIST1("resist1", "Resist Energy I", ForceSide.NEUTRAL, ForcePowerType.BUFF, 50, NEUTRAL, 8, 0),
    RESIST2("resist2", "Resist Energy II", ForceSide.NEUTRAL, ForcePowerType.BUFF, 60, RESIST1, 8, 1),
    RESIST3("resist3", "Resist Energy III", ForceSide.NEUTRAL, ForcePowerType.BUFF, 70, RESIST2, 8, 2),
    PUSH1("push1", "Force Push I", ForceSide.NEUTRAL, ForcePowerType.PER_USE, 50, NEUTRAL, 9, 0),
    PUSH2("push2", "Force Push II", ForceSide.NEUTRAL, ForcePowerType.PER_USE, 85, PUSH1, 9, 1),
    PUSH3("push3", "Force Push III", ForceSide.NEUTRAL, ForcePowerType.PER_USE, 120, PUSH2, 9, 2);

    private static final Map<String, ForcePower> BY_ID = new LinkedHashMap<>();
    private static final List<ForcePower> SELECTABLE_ORDER = new ArrayList<>();

    static {
        for (ForcePower power : values()) {
            BY_ID.put(power.id, power);
            if (power.isSelectable()) {
                SELECTABLE_ORDER.add(power);
            }
        }
    }

    private final String id;
    private final String displayName;
    private final ForceSide side;
    private final ForcePowerType type;
    private final float useCost;
    private final ForcePower parent;
    private final int iconX;
    private final int iconY;

    ForcePower(String id, String displayName, ForceSide side, ForcePowerType type, float useCost, ForcePower parent, int iconX, int iconY) {
        this.id = id;
        this.displayName = displayName;
        this.side = side;
        this.type = type;
        this.useCost = useCost;
        this.parent = parent;
        this.iconX = iconX;
        this.iconY = iconY;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public ForceSide side() { return side; }
    public ForcePowerType type() { return type; }
    public float useCost() { return useCost; }
    public ForcePower parent() { return parent; }
    public int iconX() { return iconX; }
    public int iconY() { return iconY; }

    public boolean isSelectable() {
        return type != ForcePowerType.META && this != REBOUND;
    }

    public int tier() {
        int tier = 1;
        ForcePower cursor = this;
        while (cursor.parent != null && cursor.parent.type != ForcePowerType.META) {
            tier++;
            cursor = cursor.parent;
        }
        return tier;
    }

    public String description() {
        return switch (this) {
            case FORCE_SENSITIVITY -> "Awakens access to Force energy, holocrons, datacrons, and trained abilities.";
            case LIGHT_SIDE -> "Commits the student toward light-side teachings and unlocks light techniques.";
            case DARK_SIDE -> "Commits the student toward dark-side teachings and unlocks aggressive techniques.";
            case NEUTRAL -> "Unlocks neutral utility powers and ancient cross-discipline progression.";
            case FORCE_LEVEL1 -> "Raises the user's Force training baseline to Level I.";
            case FORCE_LEVEL2 -> "Raises maximum Force reserves and prepares stronger tiered abilities.";
            case FORCE_LEVEL3 -> "Advanced Force training; requires completing Padawan/Apprentice training first.";
            case FORCE_LEVEL4 -> "Expert Force training; requires completing Padawan/Apprentice training first.";
            case FORCE_LEVEL5 -> "Master-level Force training for the end of the holocron progression.";
            case HEAL1 -> "Restores a small amount of health and briefly regenerates wounds.";
            case HEAL2 -> "Restores more health and applies stronger regeneration.";
            case HEAL3 -> "Major heal with regeneration and a short absorption buffer.";
            case FORTIFY1 -> "Temporarily increases resistance and melee strength.";
            case FORTIFY2 -> "Longer fortification with stronger resistance.";
            case FORTIFY3 -> "Maximum fortification duration and protection.";
            case STUN1 -> "Briefly locks down a target with slowness, weakness, and mining fatigue.";
            case STUN2 -> "Stuns a target longer and applies stronger control.";
            case STUN3 -> "Strongest stun tier with the longest control window.";
            case DRAIN1 -> "Damages a target, heals the caster, and restores Force energy.";
            case DRAIN2 -> "Drains more health and returns more Force energy.";
            case DRAIN3 -> "Maximum drain damage, healing, and Force return.";
            case LIGHTNING1 -> "Hold to channel damaging lightning at the aimed target.";
            case LIGHTNING2 -> "Longer-range lightning with higher sustained damage.";
            case LIGHTNING3 -> "Highest-damage lightning channel with the strongest target lock.";
            case WOUND1 -> "Crushes a target with damage, weakness, slowing, and a short lift.";
            case WOUND2 -> "Deals heavier dark-side wound damage and longer debuffs.";
            case WOUND3 -> "Maximum wound damage and control pressure.";
            case STEALTH -> "Turns the user invisible and increases movement speed temporarily.";
            case SPEED -> "Boosts movement and jumping for rapid repositioning.";
            case REBOUND -> "Passive technique that can reflect pressure back when triggered by combat logic.";
            case SIGHT1 -> "Reveals nearby living entities and grants night vision.";
            case SIGHT2 -> "Reveals entities across a wider area for longer.";
            case SIGHT3 -> "Largest Force Sight radius and longest reveal duration.";
            case MEDITATION1 -> "Restores Force energy and adds regeneration/resistance while focused.";
            case MEDITATION2 -> "Restores more Force energy and improves the meditation effects.";
            case MEDITATION3 -> "Strongest meditation recovery and longest focus duration.";
            case THROW1 -> "Throws an active lightsaber forward, then returns it to the user.";
            case THROW2 -> "Throws the saber farther and longer before it returns.";
            case RESIST1 -> "Creates an energy-resistance buffer against fire and incoming damage.";
            case RESIST2 -> "Longer and stronger energy resistance.";
            case RESIST3 -> "Maximum duration energy-resistance protection.";
            case PUSH1 -> "Knocks targets backward in front of the user.";
            case PUSH2 -> "Pushes farther with more force and damage.";
            case PUSH3 -> "Maximum push range, knockback, and impact damage.";
        };
    }

    public static ForcePower byId(String id) {
        return id == null ? null : BY_ID.get(id);
    }

    public static List<ForcePower> selectablePowers() {
        return Collections.unmodifiableList(SELECTABLE_ORDER);
    }
}
