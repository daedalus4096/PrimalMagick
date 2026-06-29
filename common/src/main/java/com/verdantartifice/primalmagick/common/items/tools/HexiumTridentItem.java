package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.HexiumTridentEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Definition for a trident made of the magickal metal hexium.
 * 
 * @author Daedalus4096
 */
public class HexiumTridentItem extends AbstractTieredTridentItem {
    public HexiumTridentItem(Item.Properties properties) {
        super(ToolMaterialsPM.HEXIUM, properties);
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack) {
        return new HexiumTridentEntity(world, thrower, stack);
    }
}
