package com.verdantartifice.primalmagick.common.research;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ItemLike;

/**
 * Definition of a trigger that grants a specified research entry upon scanning a given block/item.
 * 
 * @author Daedalus4096
 */
public class ScanItemResearchTrigger extends AbstractScanResearchTrigger {
    protected final ItemLike target;
    
    public ScanItemResearchTrigger(ItemLike target, SimpleResearchKey toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanItemResearchTrigger(ItemLike target, SimpleResearchKey toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayer player, Object obj) {
        if (obj instanceof ItemLike) {
            return target.asItem().equals(((ItemLike)obj).asItem());
        } else {
            return false;
        }
    }
}
