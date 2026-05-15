package client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import server.galaxyunderchaos.entity.NovadiveEntity;
import server.galaxyunderchaos.sound.ModSounds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NovadiveLoopSound extends AbstractTickableSoundInstance {
    private static final Map<Integer, NovadiveLoopSound> ACTIVE = new HashMap<>();

    private final NovadiveEntity ship;

    private NovadiveLoopSound(NovadiveEntity ship) {
        super(ModSounds.SHIP_ENGINE_LOOP.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.ship = ship;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
        this.pitch = 1.0F;
        this.x = ship.getX();
        this.y = ship.getY();
        this.z = ship.getZ();
    }

    public static void play(NovadiveEntity ship) {
        Minecraft minecraft = Minecraft.getInstance();
        Iterator<Map.Entry<Integer, NovadiveLoopSound>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, NovadiveLoopSound> entry = iterator.next();
            if (entry.getValue().isStopped()) {
                iterator.remove();
            }
        }

        NovadiveLoopSound existing = ACTIVE.get(ship.getId());
        if (existing == null || existing.isStopped()) {
            NovadiveLoopSound sound = new NovadiveLoopSound(ship);
            ACTIVE.put(ship.getId(), sound);
            minecraft.getSoundManager().play(sound);
        }
    }

    @Override
    public void tick() {
        if (this.ship.isRemoved() || this.ship.getEnginePower() <= 0.02F) {
            this.stop();
            ACTIVE.remove(this.ship.getId());
            return;
        }

        this.x = this.ship.getX();
        this.y = this.ship.getY();
        this.z = this.ship.getZ();
        float power = this.ship.getEnginePower();
        this.volume = 0.06F + power * 0.32F;
        this.pitch = 0.78F + power * 0.38F;
    }
}
