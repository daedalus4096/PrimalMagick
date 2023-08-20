package com.verdantartifice.primalmagick.common.items.wands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.verdantartifice.primalmagick.common.wands.WandCore;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

/**
 * Item definition for a staff core.  May be used to construct modular staves.
 * 
 * @author Daedalus4096
 */
public class StaffCoreItem extends Item {
    protected static final List<StaffCoreItem> CORES = new ArrayList<>();
    
    protected final WandCore core;

    public StaffCoreItem(WandCore core, Properties properties) {
        super(properties);
        this.core = core;
        CORES.add(this);
    }

    public WandCore getWandCore() {
        return this.core;
    }
    
    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.core.getRarity();
    }
    
    public static Collection<StaffCoreItem> getAllCores() {
        return Collections.unmodifiableCollection(CORES);
    }
}
