package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

/**
 * Particle type shown when draining a mana font with a wand.
 * 
 * @author Daedalus4096
 */
public class ManaSparkleParticle extends TextureSheetParticle {
    protected final SpriteSet spriteSet;
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

    protected ManaSparkleParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.initX = x;
        this.initY = y;
        this.initZ = z;
        this.xd = this.initXSpeed = xSpeed;
        this.yd = this.initYSpeed = ySpeed;
        this.zd = this.initZSpeed = zSpeed;
        this.quadSize = 0.125F;
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(this.spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public void setLifetime(int particleLifeTime) {
        super.setLifetime(particleLifeTime);
        
        // Now that we have the max age back in hand, solve for the destination of the particle vector
        double x2 = (this.initXSpeed * (double)this.lifetime) + this.initX;
        double y2 = (this.initYSpeed * (double)this.lifetime) + this.initY;
        double z2 = (this.initZSpeed * (double)this.lifetime) + this.initZ;
        
        // Compute the distance the particle travels
        Vec3 start = new Vec3(this.initX, this.initY, this.initZ);
        Vec3 end = new Vec3(x2, y2, z2);
        this.dist = end.subtract(start).length();

        // Solve for pitch and yaw of the particle vector
        Vec3 unitPath = end.subtract(start).normalize();
        double pitch = Math.asin(unitPath.y);
        double yaw = (Math.PI / 2.0D) - Math.atan2(unitPath.x, unitPath.z);
        this.sinYaw = Math.sin(yaw);
        this.sinPitch = Math.sin(pitch);
        this.cosYaw = Math.cos(yaw);
        this.cosPitch = Math.cos(pitch);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age >= this.lifetime) {
            this.remove();
        } else {
            double t = (double)this.age / (double)this.lifetime;
            double tpl = 2 * Math.PI * this.loops;
            double theta = tpl * t;
            double radius = 0.5D * Math.sin(Math.PI * t);
            this.age++;
            
            // Compute position along spiral path 
            this.x = this.initX + (this.dist * this.cosYaw * this.cosPitch * t) + (radius * this.cosYaw * this.sinPitch * Math.sin(theta)) + (radius * this.sinYaw * this.cosPitch * Math.sin(theta));
            this.y = this.initY + (radius * this.cosPitch * Math.cos(theta)) + (this.dist * this.sinPitch * t);
            this.z = this.initZ - (radius * this.cosYaw * this.cosPitch * Math.sin(theta)) - (radius * this.cosYaw * this.sinPitch * Math.cos(theta)) + (this.dist * this.sinYaw * this.cosPitch * t);
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet spriteSet;
        
        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ManaSparkleParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
