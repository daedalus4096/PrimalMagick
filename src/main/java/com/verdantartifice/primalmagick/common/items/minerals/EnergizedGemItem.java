package com.verdantartifice.primalmagick.common.items.minerals;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Definition for an energized gem item, used to create budding gem blocks.
 * 
 * @author Daedalus4096
 */
public class EnergizedGemItem extends Item {
    public EnergizedGemItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }
}
