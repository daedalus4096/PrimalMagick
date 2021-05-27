package com.verdantartifice.primalmagic.client.fx.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.MetaParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Meta particle shown when detonating an alchemical bomb.
 * 
 * @author Daedalus4096
 */
public class PotionExplosionParticle extends MetaParticle {
    protected int timeSinceStart;
    protected final int maximumTime = 8;

    protected PotionExplosionParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        for (int index = 0; index < 12; index++) {
            double x = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
            double y = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
            double z = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
            // TODO Determine between Effect or InstantEffect particle type
            Particle p = mc.particles.addParticle(ParticleTypes.EFFECT, x, y, z, (double)this.timeSinceStart / (double)this.maximumTime, 0.0D, 0.0D);
            if (p != null) {
                p.setColor(this.particleRed, this.particleGreen, this.particleBlue);
            }
        }
        
        if (++this.timeSinceStart >= this.maximumTime) {
            this.setExpired();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PotionExplosionParticle(worldIn, x, y, z);
        }
    }
}
