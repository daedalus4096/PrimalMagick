package com.verdantartifice.primalmagic.client.fx.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.MetaParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Meta particle shown when playing a celestial harp.  Shows note particles over time.
 * 
 * @author Daedalus4096
 */
public class NoteEmitterParticle extends MetaParticle {
    protected int timeSinceStart;
    protected final int maximumTime;
    protected final double hue;
    
    protected NoteEmitterParticle(ClientWorld world, double x, double y, double z, double hue, int duration) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.hue = hue;
        this.maximumTime = duration;
    }

    @Override
    public void tick() {
        if (this.timeSinceStart % 5 == 0) {
            double x = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
            double y = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
            double z = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
            this.world.addParticle(ParticleTypes.NOTE, x, y, z, this.hue, 0.0D, 0.0D);
        }
        
        if (++this.timeSinceStart >= this.maximumTime) {
            this.setExpired();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<NoteEmitterParticleData> {
        @Override
        public Particle makeParticle(NoteEmitterParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new NoteEmitterParticle(worldIn, x, y, z, typeIn.getHue(), typeIn.getDuration());
        }
    }
}
