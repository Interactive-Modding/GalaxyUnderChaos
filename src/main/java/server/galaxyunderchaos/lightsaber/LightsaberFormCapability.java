package server.galaxyunderchaos.lightsaber;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class LightsaberFormCapability implements INBTSerializable<CompoundTag> {
    private final Set<String> unlockedForms = new HashSet<>();
    private String selectedForm = "";

    private int guardStamina = 100;
    private int maxGuardStamina = 100;
    private int lastGuardContactTicks = 200;
    private int staminaVisibleTicks = 0;
    private boolean dirty = true;

    public void unlockForm(String form) {
        if (form == null || form.isBlank()) {
            return;
        }
        unlockedForms.add(form);
        if (selectedForm == null || selectedForm.isBlank()) {
            selectedForm = form;
        }
        dirty = true;
    }

    public boolean hasForm(String form) {
        return unlockedForms.contains(form);
    }

    public void setSelectedForm(String form) {
        if (form == null || form.isBlank()) {
            this.selectedForm = "";
            dirty = true;
            return;
        }
        if (unlockedForms.isEmpty() || unlockedForms.contains(form)) {
            this.selectedForm = form;
            dirty = true;
        }
    }

    public String getSelectedForm() {
        return selectedForm == null ? "" : selectedForm;
    }

    public Set<String> getUnlockedForms() {
        return unlockedForms;
    }

    public void clearFormsOnDeath() {
        unlockedForms.clear();
        selectedForm = "";
        guardStamina = maxGuardStamina;
        lastGuardContactTicks = 200;
        staminaVisibleTicks = 0;
        dirty = true;
    }

    public int getGuardStamina() {
        return guardStamina;
    }

    public void setGuardStamina(int value) {
        int clamped = Math.max(0, Math.min(value, maxGuardStamina));
        if (guardStamina != clamped) {
            guardStamina = clamped;
            dirty = true;
        }
    }

    public int getMaxGuardStamina() {
        return maxGuardStamina;
    }

    public void setMaxGuardStamina(int value) {
        int clamped = Math.max(1, value);
        if (maxGuardStamina != clamped) {
            maxGuardStamina = clamped;
            if (guardStamina > maxGuardStamina) {
                guardStamina = maxGuardStamina;
            }
            dirty = true;
        }
    }

    public int getLastGuardContactTicks() {
        return lastGuardContactTicks;
    }

    public void tickLastGuardContact() {
        lastGuardContactTicks++;
        if (staminaVisibleTicks > 0) {
            staminaVisibleTicks--;
        }
    }

    public void markGuardContact() {
        lastGuardContactTicks = 0;
        staminaVisibleTicks = Math.max(staminaVisibleTicks, 120);
        dirty = true;
    }

    public int getStaminaVisibleTicks() {
        return staminaVisibleTicks;
    }

    public void setStaminaVisibleTicks(int value) {
        int clamped = Math.max(0, value);
        if (staminaVisibleTicks != clamped) {
            staminaVisibleTicks = clamped;
            dirty = true;
        }
    }

    public boolean isStaminaVisible() {
        return staminaVisibleTicks > 0;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clearDirty() {
        dirty = false;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag formList = new ListTag();
        for (String form : unlockedForms) {
            CompoundTag formTag = new CompoundTag();
            formTag.putString("form", form);
            formList.add(formTag);
        }
        tag.put("UnlockedForms", formList);
        tag.putString("SelectedForm", selectedForm);
        tag.putInt("GuardStamina", guardStamina);
        tag.putInt("MaxGuardStamina", maxGuardStamina);
        tag.putInt("LastGuardContactTicks", lastGuardContactTicks);
        tag.putInt("StaminaVisibleTicks", staminaVisibleTicks);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        unlockedForms.clear();
        if (compoundTag.contains("UnlockedForms")) {
            ListTag formList = compoundTag.getList("UnlockedForms", 10);
            for (int i = 0; i < formList.size(); i++) {
                CompoundTag formTag = formList.getCompound(i);
                unlockedForms.add(formTag.getString("form"));
            }
        }
        selectedForm = compoundTag.contains("SelectedForm") ? compoundTag.getString("SelectedForm") : "";
        if (selectedForm == null) {
            selectedForm = "";
        }
        guardStamina = compoundTag.contains("GuardStamina") ? compoundTag.getInt("GuardStamina") : 100;
        maxGuardStamina = compoundTag.contains("MaxGuardStamina") ? Math.max(1, compoundTag.getInt("MaxGuardStamina")) : 100;
        if (guardStamina > maxGuardStamina) {
            guardStamina = maxGuardStamina;
        }
        lastGuardContactTicks = compoundTag.contains("LastGuardContactTicks") ? compoundTag.getInt("LastGuardContactTicks") : 200;
        staminaVisibleTicks = compoundTag.contains("StaminaVisibleTicks") ? compoundTag.getInt("StaminaVisibleTicks") : 0;
        dirty = true;
    }
}
