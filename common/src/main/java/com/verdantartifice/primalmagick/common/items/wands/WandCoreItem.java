package com.verdantartifice.primalmagick.common.items.wands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.verdantartifice.primalmagick.common.wands.WandCore;

import net.minecraft.world.item.Item;

/**
 * Item definition for a wand core.  May be used to construct modular wands.
 * 
 * @author Daedalus4096
 */
public class WandCoreItem extends Item {
    protected static final List<WandCoreItem> CORES = new ArrayList<>();
    
    protected final WandCore core;

    public WandCoreItem(WandCore core, Properties properties) {
        super(properties.rarity(core.getRarity()));
        this.core = core;
        CORES.add(this);
    }

    public WandCore getWandCore() {
        return this.core;
    }
    
    public static Collection<WandCoreItem> getAllCores() {
        return Collections.unmodifiableCollection(CORES);
    }
}
