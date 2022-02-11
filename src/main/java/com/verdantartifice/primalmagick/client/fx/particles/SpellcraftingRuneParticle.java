package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * Particle type shown when the ring on the spellcrafting altar pauses on a rune.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingRuneParticle extends TextureSheetParticle {
    protected final SpriteSet spriteSet;

    public SpellcraftingRuneParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteSet) {
        this(world, x, y, z, 0.0D, 0.0D, 0.0D, spriteSet);
    }
    
    public SpellcraftingRuneParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize = 0.25F;
        this.gravity = 0.0F;
        this.lifetime = 10;
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(this.spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.spriteSet);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet spriteSet;
        
        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SpellcraftingRuneParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
        
    }
}
