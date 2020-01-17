package com.verdantartifice.primalmagic.client.fx.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Particle type shown when draining a mana font with a wand.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ManaSparkleParticle extends SpriteTexturedParticle {
    protected final IAnimatedSprite spriteSet;
    protected final double initX;
    protected final double initY;
    protected final double initZ;
    protected final double initXSpeed;
    protected final double initYSpeed;
    protected final double initZSpeed;
    protected double sinYaw;
    protected double sinPitch;
    protected double cosYaw;
    protected double cosPitch;
    protected final double loops = 2.0D;
    protected double dist = 1.0D;

    protected ManaSparkleParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.initX = x;
        this.initY = y;
        this.initZ = z;
        this.motionX = this.initXSpeed = xSpeed;
        this.motionY = this.initYSpeed = ySpeed;
        this.motionZ = this.initZSpeed = zSpeed;
        this.particleScale = 0.125F;
        this.spriteSet = spriteSet;
        this.selectSpriteWithAge(this.spriteSet);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public void setMaxAge(int particleLifeTime) {
        super.setMaxAge(particleLifeTime);
        
        // Now that we have the max age back in hand, solve for the destination of the particle vector
        double x2 = (this.initXSpeed * (double)this.maxAge) + this.initX;
        double y2 = (this.initYSpeed * (double)this.maxAge) + this.initY;
        double z2 = (this.initZSpeed * (double)this.maxAge) + this.initZ;
        
        // Compute the distance the particle travels
        Vec3d start = new Vec3d(this.initX, this.initY, this.initZ);
        Vec3d end = new Vec3d(x2, y2, z2);
        this.dist = end.subtract(start).length();

        // Solve for pitch and yaw of the particle vector
        Vec3d unitPath = end.subtract(start).normalize();
        double pitch = Math.asin(unitPath.y);
        double yaw = (Math.PI / 2.0D) - Math.atan2(unitPath.x, unitPath.z);
        this.sinYaw = Math.sin(yaw);
        this.sinPitch = Math.sin(pitch);
        this.cosYaw = Math.cos(yaw);
        this.cosPitch = Math.cos(pitch);
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age >= this.maxAge) {
            this.setExpired();
        } else {
            double t = (double)this.age / (double)this.maxAge;
            double tpl = 2 * Math.PI * this.loops;
            double theta = tpl * t;
            double radius = 0.5D * Math.sin(Math.PI * t);
            this.age++;
            
            // Compute position along spiral path 
            this.posX = this.initX + (this.dist * this.cosYaw * this.cosPitch * t) + (radius * this.cosYaw * this.sinPitch * Math.sin(theta)) + (radius * this.sinYaw * this.cosPitch * Math.sin(theta));
            this.posY = this.initY + (radius * this.cosPitch * Math.cos(theta)) + (this.dist * this.sinPitch * t);
            this.posZ = this.initZ - (radius * this.cosYaw * this.cosPitch * Math.sin(theta)) - (radius * this.cosYaw * this.sinPitch * Math.cos(theta)) + (this.dist * this.sinYaw * this.cosPitch * t);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        protected final IAnimatedSprite spriteSet;
        
        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ManaSparkleParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
