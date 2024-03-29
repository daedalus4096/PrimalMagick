package com.verdantartifice.primalmagick.common.entities.projectiles;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a thrown hexium trident entity.
 * 
 * @author Daedalus4096
 */
public class HexiumTridentEntity extends AbstractTridentEntity {
    public HexiumTridentEntity(EntityType<? extends AbstractTridentEntity> type, Level worldIn) {
        super(type, worldIn);
        this.thrownStack = new ItemStack(ItemsPM.HEXIUM_TRIDENT.get());
    }
    
    public HexiumTridentEntity(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypesPM.HEXIUM_TRIDENT.get(), worldIn, thrower, thrownStackIn);
    }
    
    public HexiumTridentEntity(Level worldIn, double x, double y, double z) {
        super(EntityTypesPM.HEXIUM_TRIDENT.get(), worldIn, x, y, z);
        this.thrownStack = new ItemStack(ItemsPM.HEXIUM_TRIDENT.get());
    }

    @Override
    public double getBaseDamage() {
        return 11.0D;
    }
}
