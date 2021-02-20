package com.verdantartifice.primalmagic.client.fx.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Particle type shown when a ritual offering is being drawn into the altar.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class OfferingParticle extends SpriteTexturedParticle {
    protected final float uVal;
    protected final float vVal;
    
    protected final double targetX;
    protected final double targetY;
    protected final double targetZ;

    @SuppressWarnings("deprecation")
    protected OfferingParticle(ClientWorld world, double x, double y, double z, double tx, double ty, double tz, ItemStack stack) {
        super(world, x, y, z);
        this.setSprite(Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, world, (LivingEntity)null).getParticleTexture());
        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;
        this.particleGravity = 0.01F;
        this.uVal = this.rand.nextFloat() * 3.0F;
        this.vVal = this.rand.nextFloat() * 3.0F;
        
        double dx = this.targetX - this.posX;
        double dy = this.targetY - this.posY;
        double dz = this.targetZ - this.posZ;
        int base = (int)(MathHelper.sqrt(dx * dx + dy * dy + dz * dz) * 10.0F);
        if (base < 1) {
            base = 1;
        }
        this.maxAge = (base / 2 + this.rand.nextInt(base));
        
        this.motionX = (0.03D * this.rand.nextGaussian());
        this.motionY = (0.03D * this.rand.nextGaussian());
        this.motionZ = (0.03D * this.rand.nextGaussian());
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.TERRAIN_SHEET;
    }

    @Override
    protected float getMinU() {
        return this.sprite.getInterpolatedU((double)((this.uVal + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    protected float getMaxU() {
        return this.sprite.getInterpolatedU((double)(this.uVal / 4.0F * 16.0F));
    }

    @Override
    protected float getMinV() {
        return this.sprite.getInterpolatedV((double)(this.vVal / 4.0F * 16.0F));
    }

    @Override
    protected float getMaxV() {
        return this.sprite.getInterpolatedV((double)((this.vVal + 1.0F) / 4.0F * 16.0F));
    }
    
    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge || this.hasReachedTarget()) {
            this.setExpired();
        } else {
            this.move(this.motionX, this.motionY, this.motionZ);

            this.motionX *= 0.985D;
            this.motionY *= 0.95D;
            this.motionZ *= 0.985D;
            
            double dx = this.targetX - this.posX;
            double dy = this.targetY - this.posY;
            double dz = this.targetZ - this.posZ;
            double distance = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
            double clamp = Math.min(0.25D, distance / 15.0D);
            if (distance < 2.0D) {
                this.particleScale *= 0.9F;
            }
            dx /= distance;
            dy /= distance;
            dz /= distance;
            
            this.motionX += dx * clamp;
            this.motionY += dy * clamp;
            this.motionZ += dz * clamp;
            
            this.motionX = MathHelper.clamp(this.motionX, -clamp, clamp);
            this.motionY = MathHelper.clamp(this.motionY, -clamp, clamp);
            this.motionZ = MathHelper.clamp(this.motionZ, -clamp, clamp);
            
            this.motionX += this.rand.nextGaussian() * 0.005D;
            this.motionY += this.rand.nextGaussian() * 0.005D;
            this.motionZ += this.rand.nextGaussian() * 0.005D;
        }
    }
    
    protected boolean hasReachedTarget() {
        return (MathHelper.floor(this.posX) == MathHelper.floor(this.targetX)) &&
                (MathHelper.floor(this.posY) == MathHelper.floor(this.targetY)) &&
                (MathHelper.floor(this.posZ) == MathHelper.floor(this.targetZ));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<ItemParticleData> {
        public Factory(IAnimatedSprite spriteSet) {}

        @Override
        public Particle makeParticle(ItemParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double tx, double ty, double tz) {
            return new OfferingParticle(worldIn, x, y, z, tx, ty, tz, typeIn.getItemStack());
        }
    }
}
