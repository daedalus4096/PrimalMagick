package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

/**
 * Particle type shown when the ring on the spellcrafting altar pauses on a rune.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingRuneParticle extends SingleQuadParticle {
    private static final float BASE_QUAD_SIZE = 0.25F;

    protected final SingleQuadParticle.Layer layer;
    protected final SpriteSet spriteSet;

    public SpellcraftingRuneParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.first());
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize = BASE_QUAD_SIZE;
        this.gravity = 0.0F;
        this.lifetime = 10;
        this.spriteSet = spriteSet;
        this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
    }

    @Override
    public void tick() {
        super.tick();
        this.quadSize = BASE_QUAD_SIZE * Mth.lerp((float)this.age / (float)this.lifetime, 1F, 2F);
        this.setSpriteFromAge(this.spriteSet);
    }

    @Override
    @NotNull
    protected Layer getLayer() {
        return this.layer;
    }

    public static class Provider implements ParticleProvider<ColorParticleOption> {
        protected final SpriteSet spriteSet;
        
        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull ColorParticleOption options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull RandomSource randomSource) {
            SpellcraftingRuneParticle p = new SpellcraftingRuneParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            p.setColor(options.getRed(), options.getGreen(), options.getBlue());
            return p;
        }
    }
}
