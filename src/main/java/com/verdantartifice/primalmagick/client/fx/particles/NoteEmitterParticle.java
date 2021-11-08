package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;

/**
 * Meta particle shown when playing a celestial harp.  Shows note particles over time.
 * 
 * @author Daedalus4096
 */
public class NoteEmitterParticle extends NoRenderParticle {
    protected int timeSinceStart;
    protected final int maximumTime;
    protected final double hue;
    
    protected NoteEmitterParticle(ClientLevel world, double x, double y, double z, double hue, int duration) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.hue = hue;
        this.maximumTime = duration;
    }

    @Override
    public void tick() {
        if (this.timeSinceStart % 5 == 0) {
            double x = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
            double y = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
            double z = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
            this.level.addParticle(ParticleTypes.NOTE, x, y, z, this.hue, 0.0D, 0.0D);
        }
        
        if (++this.timeSinceStart >= this.maximumTime) {
            this.remove();
        }
    }
    
    public static class Factory implements ParticleProvider<NoteEmitterParticleData> {
        @Override
        public Particle createParticle(NoteEmitterParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new NoteEmitterParticle(worldIn, x, y, z, typeIn.getHue(), typeIn.getDuration());
        }
    }
}
