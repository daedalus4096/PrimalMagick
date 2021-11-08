package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * Particle type shown to highlight an open prop during a ritual.
 * 
 * @author Daedalus4096
 */
public class PropMarkerParticle extends TextureSheetParticle {
    protected final SpriteSet spriteSet;
    protected final double initY;
    
    public PropMarkerParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.initY = y;
        this.quadSize = 0.33F;
        this.lifetime = 6000;
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(this.spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.y = this.initY + Math.abs(Math.cos(this.age * (Math.PI / 20.0D)));
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet spriteSet;
        
        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PropMarkerParticle particle = new PropMarkerParticle(worldIn, x, y, z, this.spriteSet);
            return particle;
        }
    }
}
