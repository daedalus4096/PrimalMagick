package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.common.wands.WandCore;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import net.minecraft.world.item.Item.Properties;

/**
 * Item definition for a wand core.  May be used to construct modular wands.
 * 
 * @author Daedalus4096
 */
public class WandCoreItem extends Item {
    protected final WandCore core;

    public WandCoreItem(WandCore core, Properties properties) {
        super(properties);
        this.core = core;
    }

    public WandCore getWandCore() {
        return this.core;
    }
    
    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.core.getRarity();
    }
}
