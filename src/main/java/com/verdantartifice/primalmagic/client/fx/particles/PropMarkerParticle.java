package com.verdantartifice.primalmagic.client.fx.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Particle type shown to highlight an open prop during a ritual.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class PropMarkerParticle extends SpriteTexturedParticle {
    protected final IAnimatedSprite spriteSet;
    protected final double initY;
    
    public PropMarkerParticle(ClientWorld world, double x, double y, double z, IAnimatedSprite spriteSet) {
        super(world, x, y, z);
        this.initY = y;
        this.particleScale = 0.33F;
        this.maxAge = 6000;
        this.spriteSet = spriteSet;
        this.selectSpriteWithAge(this.spriteSet);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.posY = this.initY + Math.abs(Math.cos(this.age * (Math.PI / 20.0D)));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        protected final IAnimatedSprite spriteSet;
        
        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PropMarkerParticle particle = new PropMarkerParticle(worldIn, x, y, z, this.spriteSet);
            return particle;
        }
    }
}
