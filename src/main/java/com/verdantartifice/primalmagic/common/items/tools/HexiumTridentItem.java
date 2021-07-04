package com.verdantartifice.primalmagic.common.items.tools;

import com.verdantartifice.primalmagic.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.HexiumTridentEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Definition for a trident made of the magical metal hexium.
 * 
 * @author Daedalus4096
 */
public class HexiumTridentItem extends AbstractTieredTridentItem {
    public HexiumTridentItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
    }

    @Override
    protected AbstractTridentEntity getThrownEntity(World world, LivingEntity thrower, ItemStack stack) {
        return new HexiumTridentEntity(world, thrower, stack);
    }
}
