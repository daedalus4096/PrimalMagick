package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.common.wands.WandCore;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

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
