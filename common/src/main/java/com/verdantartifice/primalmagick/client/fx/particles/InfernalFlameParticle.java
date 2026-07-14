package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

/**
 * Particle type shown when calcinating items in an essence furnace.
 * 
 * @author Daedalus4096
 */
public class InfernalFlameParticle extends RisingParticle {
    private final SingleQuadParticle.Layer layer;
    private final SpriteSet spriteSet;

    public InfernalFlameParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.first());
        this.spriteSet = spriteSet;
        this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
    }

    @Override
    public void move(double xd, double yd, double zd) {
        this.setBoundingBox(this.getBoundingBox().move(xd, yd, zd));
        this.setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(float partialTicks) {
        float f = ((float)this.age + partialTicks) / (float)this.lifetime;
        return this.quadSize * (1.0F - f * f * 0.5F);
    }

    @Override
    @NotNull
    protected Layer getLayer() {
        return this.layer;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.spriteSet);
    }

    @Override
    public int getLightCoords(float partialTicks) {
        return LightCoordsUtil.addSmoothBlockEmission(super.getLightCoords(partialTicks), ((float)this.age + partialTicks) / (float)this.lifetime);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull RandomSource randomSource) {
            return new InfernalFlameParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
        }
    }
}
