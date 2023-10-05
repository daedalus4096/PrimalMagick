package com.verdantartifice.primalmagick.client.fx.particles;

import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;

/**
 * Definition for dripping fluid particle chains.
 * 
 * @author Daedalus4096
 */
public class DripParticlePM extends TextureSheetParticle {
    protected DripParticlePM(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
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
        this.preMoveUpdate();
        if (!this.removed) {
            this.yd -= (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.postMoveUpdate();
            if (!this.removed) {
                this.xd *= (double)0.98F;
                this.yd *= (double)0.98F;
                this.zd *= (double)0.98F;
            }
        }
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
        float r = (float)FastColor.ARGB32.red(packedColor) / 255F;
        float g = (float)FastColor.ARGB32.green(packedColor) / 255F;
        float b = (float)FastColor.ARGB32.blue(packedColor) / 255F;
        this.setColor(r, g, b);
    }

    public static TextureSheetParticle createBloodDropHangParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        DripParticlePM.Hang particle = new DripParticlePM.Hang(pLevel, pX, pY, pZ, ParticleTypesPM.FALLING_BLOOD_DROP.get());
        particle.gravity *= 0.01F;
        particle.lifetime = 100;
        particle.setColor(Source.BLOOD.getColor());
        return particle;
    }
    
    public static TextureSheetParticle createBloodDropFallParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        DripParticlePM.Fall particle = new DripParticlePM.Fall(pLevel, pX, pY, pZ, ParticleTypesPM.LANDING_BLOOD_DROP.get());
        particle.gravity = 0.01F;
        particle.setColor(Source.BLOOD.getColor());
        return particle;
    }
    
    public static TextureSheetParticle createBloodDropLandParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        DripParticlePM.Land particle = new DripParticlePM.Land(pLevel, pX, pY, pZ);
        particle.lifetime = (int)(28.0D / (Math.random() * 0.8D + 0.2D));
        particle.setColor(Source.BLOOD.getColor());
        return particle;
    }
    
    public static class Hang extends DripParticlePM {
        protected final ParticleOptions fallParticle;
        
        public Hang(ClientLevel pLevel, double pX, double pY, double pZ, ParticleOptions pFallParticle) {
            super(pLevel, pX, pY, pZ);
            this.fallParticle = pFallParticle;
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
        
        public Fall(ClientLevel pLevel, double pX, double pY, double pZ, ParticleOptions pFallParticle) {
            super(pLevel, pX, pY, pZ);
            this.landParticle = pFallParticle;
            this.lifetime = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
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
        public Land(ClientLevel pLevel, double pX, double pY, double pZ) {
            super(pLevel, pX, pY, pZ);
            this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        }
    }
}
