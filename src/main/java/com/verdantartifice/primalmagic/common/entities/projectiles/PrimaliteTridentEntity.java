package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a thrown primalite trident entity.
 * 
 * @author Daedalus4096
 */
public class PrimaliteTridentEntity extends AbstractTridentEntity {
    public PrimaliteTridentEntity(EntityType<? extends PrimaliteTridentEntity> type, Level worldIn) {
        super(type, worldIn);
        this.thrownStack = new ItemStack(ItemsPM.PRIMALITE_TRIDENT.get());
    }
    
    public PrimaliteTridentEntity(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypesPM.PRIMALITE_TRIDENT.get(), worldIn, thrower, thrownStackIn);
    }
    
    public PrimaliteTridentEntity(Level worldIn, double x, double y, double z) {
        super(EntityTypesPM.PRIMALITE_TRIDENT.get(), worldIn, x, y, z);
        this.thrownStack = new ItemStack(ItemsPM.PRIMALITE_TRIDENT.get());
    }

    @Override
    protected float getBaseDamage() {
        return 9.5F;
    }
}
