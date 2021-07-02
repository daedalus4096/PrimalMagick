package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Definition for a thrown primalite trident entity.
 * 
 * @author Daedalus4096
 */
public class PrimaliteTridentEntity extends AbstractTridentEntity {
    public PrimaliteTridentEntity(EntityType<? extends PrimaliteTridentEntity> type, World worldIn) {
        super(type, worldIn);
        this.thrownStack = new ItemStack(ItemsPM.PRIMALITE_TRIDENT.get());
    }
    
    public PrimaliteTridentEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypesPM.PRIMALITE_TRIDENT.get(), worldIn, thrower, thrownStackIn);
    }
    
    public PrimaliteTridentEntity(World worldIn, double x, double y, double z) {
        super(EntityTypesPM.PRIMALITE_TRIDENT.get(), worldIn, x, y, z);
        this.thrownStack = new ItemStack(ItemsPM.PRIMALITE_TRIDENT.get());
    }

    @Override
    protected float getBaseDamage() {
        return 9.5F;
    }
}
