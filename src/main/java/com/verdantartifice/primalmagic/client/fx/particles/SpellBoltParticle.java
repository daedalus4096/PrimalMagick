package com.verdantartifice.primalmagic.client.fx.particles;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpellBoltParticle extends Particle {
    protected final Vec3d source;
    protected final Vec3d target;
    
    protected SpellBoltParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Vec3d target) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.source = new Vec3d(x, y, z);
        this.target = target;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.maxAge = 3;
    }
    
    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        // TODO Auto-generated method stub
        PrimalMagic.LOGGER.info("Rendering spell bolt particle from {} to {}", this.source, this.target);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    public static class Factory implements IParticleFactory<SpellBoltParticleData> {
        public Factory(IAnimatedSprite spriteSet) {}
        
        @Override
        public Particle makeParticle(SpellBoltParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SpellBoltParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getTargetVec());
        }
    }
}
