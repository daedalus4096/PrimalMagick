package com.verdantartifice.primalmagick.common.entities.projectiles;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/**
 * Definition for a thrown ignyx entity.  Explodes on impact.
 * 
 * @author Daedalus4096
 */
public class IgnyxEntity extends ThrowableItemProjectile {
    public IgnyxEntity(EntityType<? extends IgnyxEntity> type, Level level) {
        super(type, level);
    }
    
    public IgnyxEntity(Level level, LivingEntity thrower) {
        super(EntityTypesPM.IGNYX.get(), thrower, level);
    }
    
    public IgnyxEntity(Level level, double x, double y, double z) {
        super(EntityTypesPM.IGNYX.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemsPM.IGNYX.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        Level level = this.level();
        if (!level.isClientSide) {
            level.explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, Level.ExplosionInteraction.TNT);
            this.discard();
        }
    }
}
