package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Definition for a thrown hexium trident entity.
 * 
 * @author Daedalus4096
 */
public class HexiumTridentEntity extends AbstractTridentEntity {
    public HexiumTridentEntity(EntityType<? extends HexiumTridentEntity> type, World worldIn) {
        super(type, worldIn);
        this.thrownStack = new ItemStack(ItemsPM.HEXIUM_TRIDENT.get());
    }
    
    public HexiumTridentEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypesPM.HEXIUM_TRIDENT.get(), worldIn, thrower, thrownStackIn);
    }
    
    public HexiumTridentEntity(World worldIn, double x, double y, double z) {
        super(EntityTypesPM.HEXIUM_TRIDENT.get(), worldIn, x, y, z);
        this.thrownStack = new ItemStack(ItemsPM.HEXIUM_TRIDENT.get());
    }

    @Override
    protected float getBaseDamage() {
        return 11.0F;
    }
}
