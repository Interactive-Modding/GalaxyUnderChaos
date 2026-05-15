package server.galaxyunderchaos.force;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

public class ForceCapability implements INBTSerializable<CompoundTag> {
    public static final int VISUAL_RIGHT_ARM = 1;
    public static final int VISUAL_LEFT_ARM = 2;
    public static final int VISUAL_LIGHTNING = 4;
    public static final int VISUAL_DRAIN = 8;
    public static final int VISUAL_PUSH = 16;

    private final LinkedHashSet<String> unlockedPowerIds = new LinkedHashSet<>();
    private String selectedPowerId = "";
    private float currentForce = 0.0F;
    private int useCooldownTicks = 0;
    private int reboundCooldownTicks = 0;

    private int lightDatacrons = 0;
    private int darkDatacrons = 0;
    private int ancientDatacrons = 0;
    private ForceSide committedSide = ForceSide.UNIVERSAL;
    private ForceSide alignmentFlashSide = ForceSide.UNIVERSAL;
    private int alignmentFlashTicks = 0;
    private boolean hasTrainedStudent = false;

    private String activeVisualPowerId = "";
    private int visualTicks = 0;
    private int visualFlags = 0;

    private boolean usingPower = false;
    private String usingPowerId = "";
    private int usingTicks = 0;
    private int noTargetUsingTicks = 0;

    private boolean dirty = true;

    public boolean hasPower(ForcePower power) {
        return power != null && unlockedPowerIds.contains(power.id());
    }

    public boolean unlockPower(ForcePower power) {
        if (power == null) {
            return false;
        }
        if (power.parent() != null) {
            unlockPower(power.parent());
        }
        boolean changed = unlockedPowerIds.add(power.id());
        if (changed && selectedPowerId.isBlank() && power.isSelectable()) {
            selectedPowerId = power.id();
        }
        if (currentForce <= 0.0F) {
            currentForce = getMaxForce();
        }
        dirty |= changed;
        return changed;
    }

    public void unlockStarterBranch(ForceSide side) {
        unlockPower(ForcePower.FORCE_SENSITIVITY);
        unlockPower(ForcePower.FORCE_LEVEL1);
        if (side == ForceSide.LIGHT || side == ForceSide.DARK || side == ForceSide.NEUTRAL) {
            committedSide = side;
        }
        switch (side) {
            case LIGHT -> {
                unlockPower(ForcePower.LIGHT_SIDE);
                unlockPower(ForcePower.HEAL1);
                unlockPower(ForcePower.FORTIFY1);
                unlockPower(ForcePower.STUN1);
            }
            case DARK -> {
                unlockPower(ForcePower.DARK_SIDE);
                unlockPower(ForcePower.DRAIN1);
                unlockPower(ForcePower.LIGHTNING1);
                unlockPower(ForcePower.WOUND1);
            }
            case NEUTRAL -> {
                unlockPower(ForcePower.NEUTRAL);
                unlockPower(ForcePower.PUSH1);
                unlockPower(ForcePower.SPEED);
                unlockPower(ForcePower.SIGHT1);
                unlockPower(ForcePower.MEDITATION1);
                unlockPower(ForcePower.THROW1);
                unlockPower(ForcePower.RESIST1);
                unlockPower(ForcePower.REBOUND);
            }
            default -> {
            }
        }
        currentForce = getMaxForce();
        dirty = true;
    }

    public void unlockAll() {
        for (ForcePower power : ForcePower.values()) {
            unlockPower(power);
        }
        currentForce = getMaxForce();
        dirty = true;
    }

    public void addDatacrons(ForceSide side, int amount) {
        int clamped = Math.max(0, amount);
        if (clamped <= 0) {
            return;
        }
        switch (side) {
            case LIGHT -> lightDatacrons += clamped;
            case DARK -> darkDatacrons += clamped;
            case NEUTRAL -> ancientDatacrons += clamped;
            default -> { return; }
        }
        dirty = true;
    }

    public int getDatacrons(ForceSide side) {
        return switch (side) {
            case LIGHT -> lightDatacrons;
            case DARK -> darkDatacrons;
            case NEUTRAL -> ancientDatacrons;
            default -> 0;
        };
    }

    public boolean consumeDatacrons(ForceSide side, int amount) {
        int clamped = Math.max(0, amount);
        if (clamped <= 0) {
            return true;
        }
        if (getDatacrons(side) < clamped) {
            return false;
        }
        switch (side) {
            case LIGHT -> lightDatacrons -= clamped;
            case DARK -> darkDatacrons -= clamped;
            case NEUTRAL -> ancientDatacrons -= clamped;
            default -> { return false; }
        }
        dirty = true;
        return true;
    }

    public ForceSide getCommittedSide() {
        return committedSide == null ? ForceSide.UNIVERSAL : committedSide;
    }

    public void setCommittedSide(ForceSide side) {
        ForceSide safeSide = side == null ? ForceSide.UNIVERSAL : side;
        if (this.committedSide != safeSide) {
            this.committedSide = safeSide;
            dirty = true;
        }
    }

    public void beginAlignmentFlash(ForceSide side, int ticks) {
        this.alignmentFlashSide = side == null ? ForceSide.UNIVERSAL : side;
        this.alignmentFlashTicks = Math.max(this.alignmentFlashTicks, ticks);
        dirty = true;
    }

    public ForceSide getAlignmentFlashSide() {
        return alignmentFlashSide == null ? ForceSide.UNIVERSAL : alignmentFlashSide;
    }

    public int getAlignmentFlashTicks() {
        return alignmentFlashTicks;
    }

    public void tickAlignmentFlash() {
        if (alignmentFlashTicks > 0) {
            alignmentFlashTicks--;
            if (alignmentFlashTicks == 0) {
                alignmentFlashSide = ForceSide.UNIVERSAL;
            }
            dirty = true;
        }
    }

    public boolean hasTrainedStudent() {
        return hasTrainedStudent;
    }

    public void setHasTrainedStudent(boolean hasTrainedStudent) {
        if (this.hasTrainedStudent != hasTrainedStudent) {
            this.hasTrainedStudent = hasTrainedStudent;
            dirty = true;
        }
    }

    public void renounceAndCommit(ForceSide newSide) {
        ForceSide safeSide = newSide == null ? ForceSide.UNIVERSAL : newSide;
        unlockPower(ForcePower.FORCE_SENSITIVITY);
        unlockPower(ForcePower.FORCE_LEVEL1);
        if (safeSide == ForceSide.LIGHT) {
            removeSidePowers(ForceSide.DARK);
            unlockPower(ForcePower.LIGHT_SIDE);
        } else if (safeSide == ForceSide.DARK) {
            removeSidePowers(ForceSide.LIGHT);
            unlockPower(ForcePower.DARK_SIDE);
        } else if (safeSide == ForceSide.NEUTRAL) {
            unlockPower(ForcePower.NEUTRAL);
        }
        committedSide = safeSide;
        if (selectedPowerId != null) {
            ForcePower selected = ForcePower.byId(selectedPowerId);
            if (selected != null && !hasPower(selected)) {
                selectedPowerId = "";
            }
        }
        currentForce = getMaxForce();
        dirty = true;
    }

    private void removeSidePowers(ForceSide side) {
        unlockedPowerIds.removeIf(id -> {
            ForcePower power = ForcePower.byId(id);
            return power != null && power.side() == side;
        });
    }


    public List<ForcePower> getUnlockedSelectablePowers() {
        List<ForcePower> result = new ArrayList<>();
        for (ForcePower power : ForcePower.selectablePowers()) {
            if (hasPower(power)) {
                result.add(power);
            }
        }
        return result;
    }

    public ForcePower getSelectedPower() {
        ForcePower selected = ForcePower.byId(selectedPowerId);
        if (selected != null && hasPower(selected) && selected.isSelectable()) {
            return selected;
        }
        List<ForcePower> unlocked = getUnlockedSelectablePowers();
        if (!unlocked.isEmpty()) {
            selectedPowerId = unlocked.get(0).id();
            return unlocked.get(0);
        }
        return null;
    }

    public void selectPower(ForcePower power) {
        if (power != null && hasPower(power) && power.isSelectable()) {
            selectedPowerId = power.id();
            dirty = true;
        }
    }

    public ForcePower cycleSelectedPower() {
        List<ForcePower> unlocked = getUnlockedSelectablePowers();
        if (unlocked.isEmpty()) {
            return null;
        }
        ForcePower current = getSelectedPower();
        int index = current == null ? -1 : unlocked.indexOf(current);
        ForcePower next = unlocked.get((index + 1 + unlocked.size()) % unlocked.size());
        selectedPowerId = next.id();
        dirty = true;
        return next;
    }

    public float getCurrentForce() {
        return currentForce;
    }

    public void setCurrentForce(float currentForce) {
        float clamped = Math.max(0.0F, Math.min(currentForce, getMaxForce()));
        if (Float.compare(this.currentForce, clamped) != 0) {
            this.currentForce = clamped;
            dirty = true;
        }
    }

    public void addForce(float delta) {
        setCurrentForce(currentForce + delta);
    }

    public boolean consumeForce(float amount) {
        if (currentForce + 1.0E-4F < amount) {
            return false;
        }
        setCurrentForce(currentForce - amount);
        return true;
    }

    public int getMaxForce() {
        if (!hasPower(ForcePower.FORCE_SENSITIVITY)) {
            return 0;
        }
        int max = 100;
        if (hasPower(ForcePower.FORCE_LEVEL1)) max += 50;
        if (hasPower(ForcePower.FORCE_LEVEL2)) max += 50;
        if (hasPower(ForcePower.FORCE_LEVEL3)) max += 50;
        if (hasPower(ForcePower.FORCE_LEVEL4)) max += 75;
        if (hasPower(ForcePower.FORCE_LEVEL5)) max += 100;
        return max;
    }

    public float getRegenPerSecond() {
        if (!hasPower(ForcePower.FORCE_SENSITIVITY)) {
            return 0.0F;
        }
        float regen = 5.0F;
        if (hasPower(ForcePower.FORCE_LEVEL1)) regen += 1.0F;
        if (hasPower(ForcePower.FORCE_LEVEL2)) regen += 1.0F;
        if (hasPower(ForcePower.FORCE_LEVEL3)) regen += 1.0F;
        if (hasPower(ForcePower.FORCE_LEVEL4)) regen += 2.0F;
        if (hasPower(ForcePower.FORCE_LEVEL5)) regen += 2.0F;
        return regen;
    }

    public int getUseCooldownTicks() {
        return useCooldownTicks;
    }

    public void setUseCooldownTicks(int useCooldownTicks) {
        this.useCooldownTicks = Math.max(useCooldownTicks, 0);
        dirty = true;
    }

    public int getReboundCooldownTicks() {
        return reboundCooldownTicks;
    }

    public void setReboundCooldownTicks(int reboundCooldownTicks) {
        this.reboundCooldownTicks = Math.max(reboundCooldownTicks, 0);
        dirty = true;
    }

    public void tickCooldowns() {
        if (useCooldownTicks > 0) {
            useCooldownTicks--;
        }
        if (reboundCooldownTicks > 0) {
            reboundCooldownTicks--;
        }
    }

    public void beginVisual(ForcePower power, int ticks, int flags) {
        activeVisualPowerId = power == null ? "" : power.id();
        visualTicks = Math.max(visualTicks, ticks);
        visualFlags = flags;
        dirty = true;
    }

    public void clearVisual() {
        activeVisualPowerId = "";
        visualTicks = 0;
        visualFlags = 0;
        dirty = true;
    }

    public void tickVisual() {
        if (visualTicks > 0) {
            visualTicks--;
            if (visualTicks == 0 && !usingPower) {
                activeVisualPowerId = "";
                visualFlags = 0;
                dirty = true;
            }
        } else if (!usingPower && (!activeVisualPowerId.isEmpty() || visualFlags != 0)) {
            activeVisualPowerId = "";
            visualFlags = 0;
            dirty = true;
        }
    }

    public String getActiveVisualPowerId() {
        return activeVisualPowerId;
    }

    public int getVisualTicks() {
        return visualTicks;
    }

    public int getVisualFlags() {
        return visualFlags;
    }

    public boolean isUsingPower() {
        return usingPower;
    }

    public String getUsingPowerId() {
        return usingPowerId;
    }

    public int getUsingTicks() {
        return usingTicks;
    }

    public void startUsingPower(ForcePower power) {
        usingPower = power != null;
        usingPowerId = power == null ? "" : power.id();
        usingTicks = 0;
        noTargetUsingTicks = 0;
        dirty = true;
    }

    public void stopUsingPower() {
        if (usingPower || !usingPowerId.isEmpty()) {
            usingPower = false;
            usingPowerId = "";
            usingTicks = 0;
            noTargetUsingTicks = 0;
            if (visualTicks <= 0) {
                activeVisualPowerId = "";
                visualFlags = 0;
            }
            dirty = true;
        }
    }

    public void tickUsingPower() {
        if (usingPower) {
            usingTicks++;
        }
    }

    public int getNoTargetUsingTicks() {
        return noTargetUsingTicks;
    }

    public void resetNoTargetUsingTicks() {
        noTargetUsingTicks = 0;
        dirty = true;
    }

    public void incrementNoTargetUsingTicks() {
        noTargetUsingTicks++;
        dirty = true;
    }

    public boolean isVisualLightning() {
        return (visualFlags & VISUAL_LIGHTNING) != 0;
    }

    public Set<String> getUnlockedPowerIds() {
        return Collections.unmodifiableSet(unlockedPowerIds);
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clearDirty() {
        dirty = false;
    }


    public void applyRemoteVisualState(String activeVisualPowerId,
                                       int visualTicks,
                                       int visualFlags,
                                       boolean usingPower,
                                       String usingPowerId,
                                       int usingTicks) {
        this.activeVisualPowerId = activeVisualPowerId == null ? "" : activeVisualPowerId;
        this.visualTicks = Math.max(visualTicks, 0);
        this.visualFlags = Math.max(visualFlags, 0);
        this.usingPower = usingPower;
        this.usingPowerId = usingPowerId == null ? "" : usingPowerId;
        this.usingTicks = Math.max(usingTicks, 0);
        this.dirty = false;
    }

    public void copyFrom(ForceCapability other) {
        unlockedPowerIds.clear();
        unlockedPowerIds.addAll(other.unlockedPowerIds);
        selectedPowerId = other.selectedPowerId;
        currentForce = other.currentForce;
        useCooldownTicks = other.useCooldownTicks;
        reboundCooldownTicks = other.reboundCooldownTicks;
        lightDatacrons = other.lightDatacrons;
        darkDatacrons = other.darkDatacrons;
        ancientDatacrons = other.ancientDatacrons;
        hasTrainedStudent = other.hasTrainedStudent;
        committedSide = other.committedSide;
        alignmentFlashSide = other.alignmentFlashSide;
        alignmentFlashTicks = other.alignmentFlashTicks;
        activeVisualPowerId = other.activeVisualPowerId;
        visualTicks = other.visualTicks;
        visualFlags = other.visualFlags;
        usingPower = other.usingPower;
        usingPowerId = other.usingPowerId;
        usingTicks = other.usingTicks;
        noTargetUsingTicks = other.noTargetUsingTicks;
        dirty = true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag unlocked = new ListTag();
        for (String id : unlockedPowerIds) {
            unlocked.add(StringTag.valueOf(id));
        }
        tag.put("UnlockedPowers", unlocked);
        tag.putString("SelectedPower", selectedPowerId);
        tag.putFloat("CurrentForce", currentForce);
        tag.putInt("UseCooldownTicks", useCooldownTicks);
        tag.putInt("ReboundCooldownTicks", reboundCooldownTicks);
        tag.putInt("LightDatacrons", lightDatacrons);
        tag.putInt("DarkDatacrons", darkDatacrons);
        tag.putInt("AncientDatacrons", ancientDatacrons);
        tag.putString("CommittedSide", getCommittedSide().name());
        tag.putBoolean("HasTrainedStudent", hasTrainedStudent);
        tag.putString("AlignmentFlashSide", getAlignmentFlashSide().name());
        tag.putInt("AlignmentFlashTicks", alignmentFlashTicks);
        tag.putString("ActiveVisualPower", activeVisualPowerId);
        tag.putInt("VisualTicks", visualTicks);
        tag.putInt("VisualFlags", visualFlags);
        tag.putBoolean("UsingPower", usingPower);
        tag.putString("UsingPowerId", usingPowerId);
        tag.putInt("UsingTicks", usingTicks);
        tag.putInt("NoTargetUsingTicks", noTargetUsingTicks);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        unlockedPowerIds.clear();
        ListTag unlocked = tag.getList("UnlockedPowers", 8);
        for (int i = 0; i < unlocked.size(); i++) {
            unlockedPowerIds.add(unlocked.getString(i));
        }
        selectedPowerId = tag.getString("SelectedPower");
        currentForce = tag.getFloat("CurrentForce");
        useCooldownTicks = tag.getInt("UseCooldownTicks");
        reboundCooldownTicks = tag.getInt("ReboundCooldownTicks");
        lightDatacrons = tag.getInt("LightDatacrons");
        darkDatacrons = tag.getInt("DarkDatacrons");
        ancientDatacrons = tag.getInt("AncientDatacrons");
        committedSide = parseSide(tag.getString("CommittedSide"), ForceSide.UNIVERSAL);
        hasTrainedStudent = tag.getBoolean("HasTrainedStudent");
        alignmentFlashSide = parseSide(tag.getString("AlignmentFlashSide"), ForceSide.UNIVERSAL);
        alignmentFlashTicks = tag.getInt("AlignmentFlashTicks");
        activeVisualPowerId = tag.getString("ActiveVisualPower");
        visualTicks = tag.getInt("VisualTicks");
        visualFlags = tag.getInt("VisualFlags");
        usingPower = tag.getBoolean("UsingPower");
        usingPowerId = tag.getString("UsingPowerId");
        usingTicks = tag.getInt("UsingTicks");
        noTargetUsingTicks = tag.getInt("NoTargetUsingTicks");
        if (currentForce > getMaxForce()) {
            currentForce = getMaxForce();
        }
        dirty = true;
    }

    private static ForceSide parseSide(String name, ForceSide fallback) {
        if (name == null || name.isBlank()) {
            return fallback;
        }
        try {
            return ForceSide.valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return fallback;
        }
    }
}
