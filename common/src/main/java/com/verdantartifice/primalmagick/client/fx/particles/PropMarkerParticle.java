package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

/**
 * Particle type shown to highlight an open prop during a ritual.
 * 
 * @author Daedalus4096
 */
public class PropMarkerParticle extends SingleQuadParticle {
    protected final SingleQuadParticle.Layer layer;
    protected final SpriteSet spriteSet;
    protected final double initY;
    
    public PropMarkerParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteSet) {
        super(world, x, y, z, spriteSet.first());
        this.initY = y;
        this.quadSize = 0.33F;
        this.lifetime = 6000;
        this.spriteSet = spriteSet;
        this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
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

    @Override
    @NotNull
    protected Layer getLayer() {
        return this.layer;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet spriteSet;
        
        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull RandomSource randomSource) {
            return new PropMarkerParticle(level, x, y, z, this.spriteSet);
        }
    }
}
