package client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class CoffinRuneParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected CoffinRuneParticle(ClientLevel level, double x, double y, double z,
                                 double xd, double yd, double zd, SpriteSet sprites,
                                 float red, float green, float blue) {
        super(level, x, y, z, xd, yd, zd);
        this.sprites = sprites;
        this.lifetime = 18 + this.random.nextInt(10);
        this.gravity = 0.015F;
        this.friction = 0.86F;
        this.quadSize = 0.11F + this.random.nextFloat() * 0.08F;
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.alpha = 0.88F;
        this.xd = xd;
        this.yd = yd + 0.025D;
        this.zd = zd;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        float fade = 1.0F - (this.age / (float) this.lifetime);
        this.alpha = Math.max(0.0F, fade) * 0.88F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class JediProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public JediProvider(SpriteSet sprites) { this.sprites = sprites; }
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new CoffinRuneParticle(level, x, y, z, xd, yd, zd, sprites, 0.72F, 0.92F, 1.0F);
        }
    }

    public static class SithProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public SithProvider(SpriteSet sprites) { this.sprites = sprites; }
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new CoffinRuneParticle(level, x, y, z, xd, yd, zd, sprites, 1.0F, 0.24F, 0.12F);
        }
    }
}
