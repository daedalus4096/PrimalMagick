package com.verdantartifice.primalmagic.client.fx.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Particle type shown during spell trails and impacts.
 * 
 * @author Michael Bunting
 */
@OnlyIn(Dist.CLIENT)
public class SpellSparkleParticle extends SpriteTexturedParticle {
    protected final IAnimatedSprite spriteSet;

    public SpellSparkleParticle(World world, double x, double y, double z, IAnimatedSprite spriteSet) {
        this(world, x, y, z, 0.0D, 0.0D, 0.0D, spriteSet);
    }
    
    public SpellSparkleParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.motionX = xSpeed;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
        this.particleScale = 0.125F;
        this.particleGravity = 0.0F;
        this.maxAge = 20;
        this.spriteSet = spriteSet;
        this.selectSpriteWithAge(this.spriteSet);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public void tick() {
        super.tick();
        this.selectSpriteWithAge(this.spriteSet);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        protected final IAnimatedSprite spriteSet;
        
        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SpellSparkleParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
        
    }
}
