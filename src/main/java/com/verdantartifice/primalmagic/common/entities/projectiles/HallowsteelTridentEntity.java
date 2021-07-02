package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Definition for a thrown hallowsteel trident entity.
 * 
 * @author Daedalus4096
 */
public class HallowsteelTridentEntity extends AbstractTridentEntity {
    public HallowsteelTridentEntity(EntityType<? extends HallowsteelTridentEntity> type, World worldIn) {
        super(type, worldIn);
        this.thrownStack = new ItemStack(ItemsPM.HALLOWSTEEL_TRIDENT.get());
    }
    
    public HallowsteelTridentEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypesPM.HALLOWSTEEL_TRIDENT.get(), worldIn, thrower, thrownStackIn);
    }
    
    public HallowsteelTridentEntity(World worldIn, double x, double y, double z) {
        super(EntityTypesPM.HALLOWSTEEL_TRIDENT.get(), worldIn, x, y, z);
        this.thrownStack = new ItemStack(ItemsPM.HALLOWSTEEL_TRIDENT.get());
    }

    @Override
    protected float getBaseDamage() {
        return 12.5F;
    }
}
