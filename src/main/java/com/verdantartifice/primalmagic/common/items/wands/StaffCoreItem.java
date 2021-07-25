package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.common.wands.WandCore;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import net.minecraft.world.item.Item.Properties;

/**
 * Item definition for a staff core.  May be used to construct modular staves.
 * 
 * @author Daedalus4096
 */
public class StaffCoreItem extends Item {
    protected final WandCore core;

    public StaffCoreItem(WandCore core, Properties properties) {
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
