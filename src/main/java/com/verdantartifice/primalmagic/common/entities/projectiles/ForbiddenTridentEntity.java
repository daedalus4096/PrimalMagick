package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

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
    public ForbiddenTridentEntity(EntityType<? extends AbstractTridentEntity> type, Level worldIn) {
        super(type, worldIn);
        this.thrownStack = new ItemStack(ItemsPM.FORBIDDEN_TRIDENT.get());
    }
    
    public ForbiddenTridentEntity(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypesPM.FORBIDDEN_TRIDENT.get(), worldIn, thrower, thrownStackIn);
    }
    
    public ForbiddenTridentEntity(Level worldIn, double x, double y, double z) {
        super(EntityTypesPM.FORBIDDEN_TRIDENT.get(), worldIn, x, y, z);
        this.thrownStack = new ItemStack(ItemsPM.FORBIDDEN_TRIDENT.get());
    }

    @Override
    protected float getBaseDamage() {
        return 11.0F;
    }
}
