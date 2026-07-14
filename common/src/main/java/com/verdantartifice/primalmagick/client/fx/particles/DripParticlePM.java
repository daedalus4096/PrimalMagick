package com.verdantartifice.primalmagick.client.fx.particles;

import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

/**
 * Definition for dripping fluid particle chains.
 * 
 * @author Daedalus4096
 */
public class DripParticlePM extends SingleQuadParticle {
    protected final SingleQuadParticle.Layer layer;
    protected final SpriteSet spriteSet;

    protected DripParticlePM(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet) {
        super(pLevel, pX, pY, pZ, spriteSet.first());
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.spriteSet = spriteSet;
        this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (!this.removed) {
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.postMoveUpdate();
            if (!this.removed) {
                this.xd *= 0.98F;
                this.yd *= 0.98F;
                this.zd *= 0.98F;
            }
        }
    }

    @Override
    @NotNull
    protected Layer getLayer() {
        return this.layer;
    }

    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }
    }

    protected void postMoveUpdate() {
        // Do nothing by default
    }
    
    public void setColor(int packedColor) {
        float r = (float)ARGB.red(packedColor) / 255F;
        float g = (float)ARGB.green(packedColor) / 255F;
        float b = (float)ARGB.blue(packedColor) / 255F;
        this.setColor(r, g, b);
    }

    public static class HangProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public HangProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull RandomSource randomSource) {
            DripParticlePM.Hang particle = new DripParticlePM.Hang(level, x, y, z, this.sprite, ParticleTypesPM.FALLING_BLOOD_DROP.get());
            particle.gravity *= 0.01F;
            particle.lifetime = 100;
            particle.setColor(Sources.BLOOD.getColor());
            return particle;
        }
    }

    public static class FallProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public FallProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull RandomSource randomSource) {
            DripParticlePM.Fall particle = new DripParticlePM.Fall(level, x, y, z, this.sprite, ParticleTypesPM.LANDING_BLOOD_DROP.get());
            particle.gravity = 0.01F;
            particle.setColor(Sources.BLOOD.getColor());
            return particle;
        }
    }

    public static class LandProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public LandProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull RandomSource randomSource) {
            DripParticlePM.Land particle = new DripParticlePM.Land(level, x, y, z, this.sprite);
            particle.lifetime = (int)(28.0D / (level.getRandom().nextDouble() * 0.8D + 0.2D));
            particle.setColor(Sources.BLOOD.getColor());
            return particle;
        }
    }

    public static class Hang extends DripParticlePM {
        protected final ParticleOptions fallParticle;

        public Hang(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, ParticleOptions fallParticle) {
            super(pLevel, pX, pY, pZ, spriteSet);
            this.fallParticle = fallParticle;
        }

        @Override
        protected void preMoveUpdate() {
            super.preMoveUpdate();
            if (this.removed) {
                this.level.addParticle(this.fallParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }
        }

        @Override
        protected void postMoveUpdate() {
            super.postMoveUpdate();
            this.xd *= 0.02D;
            this.yd *= 0.02D;
            this.zd *= 0.02D;
        }
    }
    
    public static class Fall extends DripParticlePM {
        protected final ParticleOptions landParticle;
        
        public Fall(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, ParticleOptions landParticle) {
            super(pLevel, pX, pY, pZ, spriteSet);
            this.landParticle = landParticle;
            this.lifetime = (int)(64.0D / (pLevel.getRandom().nextDouble() * 0.8D + 0.2D));
        }

        @Override
        protected void postMoveUpdate() {
            super.postMoveUpdate();
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    
    public static class Land extends DripParticlePM {
        public Land(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet) {
            super(pLevel, pX, pY, pZ, spriteSet);
            this.lifetime = (int)(16.0D / (pLevel.getRandom().nextDouble() * 0.8D + 0.2D));
        }
    }
}
