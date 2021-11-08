package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;

/**
 * Meta particle shown when detonating an alchemical bomb.
 * 
 * @author Daedalus4096
 */
public class PotionExplosionParticle extends NoRenderParticle {
    protected int timeSinceStart;
    protected final int maximumTime = 8;
    protected final boolean isInstant;

    protected PotionExplosionParticle(ClientLevel world, double x, double y, double z, boolean isInstant) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.isInstant = isInstant;
    }

    @Override
    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        for (int index = 0; index < 12; index++) {
            double x = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            double y = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            double z = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            Particle p = mc.particleEngine.createParticle(this.isInstant ? ParticleTypes.INSTANT_EFFECT : ParticleTypes.EFFECT, x, y, z, (double)this.timeSinceStart / (double)this.maximumTime, 0.0D, 0.0D);
            if (p != null) {
                p.setColor(this.rCol, this.gCol, this.bCol);
            }
        }
        
        if (++this.timeSinceStart >= this.maximumTime) {
            this.remove();
        }
    }
    
    public static class Factory implements ParticleProvider<PotionExplosionParticleData> {
        @Override
        public Particle createParticle(PotionExplosionParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PotionExplosionParticle(worldIn, x, y, z, typeIn.isInstant());
        }
    }
}
