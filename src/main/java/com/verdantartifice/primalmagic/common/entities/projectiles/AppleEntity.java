package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * Definition for a thrown apple entity.  Thrown by treefolk.  Does light damage on impact.
 * 
 * @author Daedalus4096
 */
public class AppleEntity extends ThrowableItemProjectile {
    public AppleEntity(EntityType<? extends AppleEntity> type, Level worldIn) {
        super(type, worldIn);
    }
    
    public AppleEntity(Level world, LivingEntity thrower) {
        super(EntityTypesPM.APPLE.get(), thrower, world);
    }
    
    public AppleEntity(Level world, double x, double y, double z) {
        super(EntityTypesPM.APPLE.get(), x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.APPLE;
    }

    @OnlyIn(Dist.CLIENT)
    private ParticleOptions makeParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, this.getItem());
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particleData = this.makeParticle();
            for (int index = 0; index < 8; index++) {
                this.level.addParticle(particleData, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 2.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
