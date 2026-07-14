package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

/**
 * Particle type shown when a Zephyr Engine is active.
 * 
 * @author Daedalus4096
 */
public class AirCurrentParticle extends SingleQuadParticle {
    private final SingleQuadParticle.Layer layer;
    private final SpriteSet spriteSet;

    public AirCurrentParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.first());
        this.spriteSet = spriteSet;
        this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize = 0.75F;
        this.gravity = 0.0F;
        this.lifetime = 24;
    }

    @Override
    @NotNull
    protected SingleQuadParticle.Layer getLayer() {
        return this.layer;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.spriteSet);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull RandomSource randomSource) {
            return new AirCurrentParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
        }
    }
}
