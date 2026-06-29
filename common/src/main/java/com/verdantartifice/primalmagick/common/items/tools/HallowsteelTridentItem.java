package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.HallowsteelTridentEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a trident made of the magickal metal hallowsteel.
 * 
 * @author Daedalus4096
 */
public class HallowsteelTridentItem extends AbstractTieredTridentItem {
    public HallowsteelTridentItem(Item.Properties properties) {
        super(ToolMaterialsPM.HALLOWSTEEL, properties);
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack) {
        return new HallowsteelTridentEntity(world, thrower, stack);
    }
}
