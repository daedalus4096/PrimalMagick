package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * Particle type shown when a ritual offering is being drawn into the altar.
 * 
 * @author Daedalus4096
 */
public class OfferingParticle extends SingleQuadParticle {
    protected final float uVal;
    protected final float vVal;
    
    protected final double targetX;
    protected final double targetY;
    protected final double targetZ;

    protected OfferingParticle(ClientLevel world, double x, double y, double z, double tx, double ty, double tz, TextureAtlasSprite sprite) {
        super(world, x, y, z, sprite);
        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;
        this.gravity = 0.01F;
        this.uVal = this.random.nextFloat() * 3.0F;
        this.vVal = this.random.nextFloat() * 3.0F;
        
        double dx = this.targetX - this.x;
        double dy = this.targetY - this.y;
        double dz = this.targetZ - this.z;
        int base = (int)(Math.sqrt(dx * dx + dy * dy + dz * dz) * 10.0F);
        if (base < 1) {
            base = 1;
        }
        this.lifetime = (base / 2 + this.random.nextInt(base));
        
        this.xd = (0.03D * this.random.nextGaussian());
        this.yd = (0.03D * this.random.nextGaussian());
        this.zd = (0.03D * this.random.nextGaussian());
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uVal + 1.0F) / 4.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uVal / 4.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.vVal / 4.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.vVal + 1.0F) / 4.0F);
    }

    @Override
    @NotNull
    protected SingleQuadParticle.Layer getLayer() {
        return Layer.OPAQUE_ITEMS;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime || this.hasReachedTarget()) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);

            this.xd *= 0.985D;
            this.yd *= 0.95D;
            this.zd *= 0.985D;
            
            double dx = this.targetX - this.x;
            double dy = this.targetY - this.y;
            double dz = this.targetZ - this.z;
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double clamp = Math.min(0.25D, distance / 15.0D);
            if (distance < 2.0D) {
                this.quadSize *= 0.9F;
            }
            dx /= distance;
            dy /= distance;
            dz /= distance;
            
            this.xd += dx * clamp;
            this.yd += dy * clamp;
            this.zd += dz * clamp;
            
            this.xd = Mth.clamp(this.xd, -clamp, clamp);
            this.yd = Mth.clamp(this.yd, -clamp, clamp);
            this.zd = Mth.clamp(this.zd, -clamp, clamp);
            
            this.xd += this.random.nextGaussian() * 0.005D;
            this.yd += this.random.nextGaussian() * 0.005D;
            this.zd += this.random.nextGaussian() * 0.005D;
        }
    }
    
    protected boolean hasReachedTarget() {
        return (Mth.floor(this.x) == Mth.floor(this.targetX)) &&
                (Mth.floor(this.y) == Mth.floor(this.targetY)) &&
                (Mth.floor(this.z) == Mth.floor(this.targetZ));
    }

    public static class Provider implements ParticleProvider<ItemParticleOption> {
        private final ItemStackRenderState scratchRenderState = new ItemStackRenderState();

        @Override
        public Particle createParticle(@NotNull ItemParticleOption options, @NotNull ClientLevel level, double x, double y, double z, double tx, double ty, double tz, @NotNull RandomSource randomSource) {
            return new OfferingParticle(level, x, y, z, tx, ty, tz, this.getSprite(options.getItem(), level, randomSource));
        }

        protected TextureAtlasSprite getSprite(ItemStackTemplate item, ClientLevel level, RandomSource random) {
            Minecraft.getInstance().getItemModelResolver().updateForTopItem(this.scratchRenderState, item.create(), ItemDisplayContext.GROUND, level, null, 0);
            Material.Baked material = this.scratchRenderState.pickParticleMaterial(random);
            return material != null ? material.sprite() : Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS).missingSprite();
        }
    }
}
