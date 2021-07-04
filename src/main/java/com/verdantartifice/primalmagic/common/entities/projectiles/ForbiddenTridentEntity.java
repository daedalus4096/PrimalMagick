package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Definition for a thrown forbidden trident entity.
 * 
 * @author Daedalus4096
 */
public class ForbiddenTridentEntity extends AbstractTridentEntity {
    public ForbiddenTridentEntity(EntityType<? extends ForbiddenTridentEntity> type, World worldIn) {
        super(type, worldIn);
        this.thrownStack = new ItemStack(ItemsPM.FORBIDDEN_TRIDENT.get());
    }
    
    public ForbiddenTridentEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypesPM.FORBIDDEN_TRIDENT.get(), worldIn, thrower, thrownStackIn);
    }
    
    public ForbiddenTridentEntity(World worldIn, double x, double y, double z) {
        super(EntityTypesPM.FORBIDDEN_TRIDENT.get(), worldIn, x, y, z);
        this.thrownStack = new ItemStack(ItemsPM.FORBIDDEN_TRIDENT.get());
    }

    @Override
    protected float getBaseDamage() {
        return 11.0F;
    }
}
