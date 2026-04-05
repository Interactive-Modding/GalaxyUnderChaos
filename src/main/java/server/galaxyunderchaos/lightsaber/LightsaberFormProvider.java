package server.galaxyunderchaos.lightsaber;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LightsaberFormProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<LightsaberFormCapability> LIGHTSABER_FORM_CAPABILITY =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final LightsaberFormCapability instance = new LightsaberFormCapability();
    private final LazyOptional<LightsaberFormCapability> lazyInstance = LazyOptional.of(() -> instance);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return LIGHTSABER_FORM_CAPABILITY.orEmpty(capability, lazyInstance);
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        instance.deserializeNBT(compoundTag);
    }
}