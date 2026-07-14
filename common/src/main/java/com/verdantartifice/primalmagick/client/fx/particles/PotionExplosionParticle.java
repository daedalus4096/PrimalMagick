package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

/**
 * Meta particle shown when detonating an alchemical bomb.
 * 
 * @author Daedalus4096
 */
public class PotionExplosionParticle extends NoRenderParticle {
    protected int timeSinceStart;
    protected final int maximumTime = 8;
    protected final int color;
    protected final boolean isInstant;

    protected PotionExplosionParticle(ClientLevel world, double x, double y, double z, int color, boolean isInstant) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.color = color;
        this.isInstant = isInstant;
    }

    @Override
    public void tick() {
        for (int index = 0; index < 12; index++) {
            double x = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            double y = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            double z = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            double power = this.random.nextDouble() * 4.0D;
            SpellParticleOption options = SpellParticleOption.create(this.isInstant ? ParticleTypes.INSTANT_EFFECT : ParticleTypes.EFFECT, this.color, (float)power);
            this.level.addParticle(options, x, y, z, (double)this.timeSinceStart / (double)this.maximumTime, 0.0D, 0.0D);
        }
        
        if (++this.timeSinceStart >= this.maximumTime) {
            this.remove();
        }
    }
    
    public static class Provider implements ParticleProvider<PotionExplosionParticleData> {
        @Override
        public Particle createParticle(@NotNull PotionExplosionParticleData options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull RandomSource randomSource) {
            return new PotionExplosionParticle(level, x, y, z, options.getColor(), options.isInstant());
        }
    }
}
