package com.verdantartifice.primalmagick.common.entities.projectiles;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a thrown hallowsteel trident entity.
 * 
 * @author Daedalus4096
 */
public class HallowsteelTridentEntity extends AbstractTridentEntity {
    public HallowsteelTridentEntity(EntityType<? extends AbstractTridentEntity> type, Level worldIn) {
        super(type, worldIn);
        this.thrownStack = new ItemStack(ItemsPM.HALLOWSTEEL_TRIDENT.get());
    }
    
    public HallowsteelTridentEntity(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypesPM.HALLOWSTEEL_TRIDENT.get(), worldIn, thrower, thrownStackIn);
    }
    
    public HallowsteelTridentEntity(Level worldIn, double x, double y, double z) {
        super(EntityTypesPM.HALLOWSTEEL_TRIDENT.get(), worldIn, x, y, z);
        this.thrownStack = new ItemStack(ItemsPM.HALLOWSTEEL_TRIDENT.get());
    }

    @Override
    public double getBaseDamage() {
        return 12.5D;
    }
}
