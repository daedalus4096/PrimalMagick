package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * Particle type shown when a Zephyr Engine is active.
 * 
 * @author Daedalus4096
 */
public class AirCurrentParticle extends TextureSheetParticle {
    public AirCurrentParticle(ClientLevel world, double x, double y, double z) {
        this(world, x, y, z, 0.0D, 0.0D, 0.0D);
    }
    
    public AirCurrentParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize = 0.75F;
        this.gravity = 0.0F;
        this.lifetime = 24;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AirCurrentParticle acp = new AirCurrentParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            acp.pickSprite(this.sprite);
            return acp;
        }
    }
}
