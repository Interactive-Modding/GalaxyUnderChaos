package client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import server.galaxyunderchaos.force.ForceCapability;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.sound.ModSounds;

public class MovingSoundForceLightning extends AbstractTickableSoundInstance {
    private final Player caster;
    private boolean stopped;

    public MovingSoundForceLightning(Player caster) {
        super(ModSounds.FORCE_LIGHTNING_LOOP.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.caster = caster;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.9F;
        this.pitch = 0.92F;
    }

    @Override
    public void tick() {
        if (caster == null || caster.isRemoved()) {
            stopSelf();
            return;
        }
        var capOpt = caster.getCapability(ForceProvider.FORCE_CAPABILITY);
        if (!capOpt.isPresent()) {
            stopSelf();
            return;
        }

        ForceCapability cap = capOpt.orElseThrow(IllegalStateException::new);
        if (!cap.isVisualLightning()) {
            stopSelf();
            return;
        }

        this.x = caster.getX();
        this.y = caster.getY();
        this.z = caster.getZ();
        this.volume = 0.9F;
        this.pitch = 0.90F + caster.getRandom().nextFloat() * 0.03F;
    }

    public boolean isStoppedCustom() {
        return stopped;
    }

    private void stopSelf() {
        this.stopped = true;
        this.stop();
    }
}
