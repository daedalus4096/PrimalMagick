package com.verdantartifice.primalmagic.common.items.tools;

import com.verdantartifice.primalmagic.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.HallowsteelTridentEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a trident made of the magical metal hallowsteel.
 * 
 * @author Daedalus4096
 */
public class HallowsteelTridentItem extends AbstractTieredTridentItem {
    public HallowsteelTridentItem(Item.Properties properties) {
        super(ItemTierPM.HALLOWSTEEL, properties);
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack) {
        return new HallowsteelTridentEntity(world, thrower, stack);
    }
}
