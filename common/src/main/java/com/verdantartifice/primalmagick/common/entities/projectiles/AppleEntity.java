package com.verdantartifice.primalmagick.common.entities.projectiles;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

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
        super(EntityTypesPM.APPLE.get(), thrower, world, Items.APPLE.getDefaultInstance());
    }
    
    @Override
    @NotNull
    protected Item getDefaultItem() {
        return Items.APPLE;
    }

    private ParticleOptions makeParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, this.getItem().getItem());
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particleData = this.makeParticle();
            for (int index = 0; index < 8; index++) {
                this.level().addParticle(particleData, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level() instanceof ServerLevel serverLevel) {
            result.getEntity().hurtServer(serverLevel, serverLevel.damageSources().thrown(this, this.getOwner()), 2.0F);
        }
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        Level level = this.level();
        if (!level.isClientSide()) {
            level.broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }
}
