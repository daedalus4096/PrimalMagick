package com.verdantartifice.primalmagick.common.entities.projectiles;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a thrown forbidden trident entity.
 * 
 * @author Daedalus4096
 */
public class ForbiddenTridentEntity extends AbstractTridentEntity {
    private static final ItemStack DEFAULT_THROWN_STACK = new ItemStack(ItemRegistration.FORBIDDEN_TRIDENT.get());
    
    public ForbiddenTridentEntity(EntityType<? extends AbstractTridentEntity> type, Level worldIn) {
        super(type, worldIn);
    }
    
    public ForbiddenTridentEntity(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypesPM.FORBIDDEN_TRIDENT.get(), worldIn, thrower, thrownStackIn);
    }
    
    @Override
    public double getBaseDamage() {
        return 11.0D;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return DEFAULT_THROWN_STACK;
    }
}
