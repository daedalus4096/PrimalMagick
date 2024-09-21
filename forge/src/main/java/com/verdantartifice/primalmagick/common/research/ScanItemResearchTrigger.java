package com.verdantartifice.primalmagick.common.research;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ItemLike;

/**
 * Definition of a trigger that grants a specified research entry upon scanning a given block/item.
 * 
 * @author Daedalus4096
 */
public class ScanItemResearchTrigger extends AbstractScanResearchTrigger {
    protected final ItemLike target;
    
    public ScanItemResearchTrigger(ItemLike target, ResourceKey<ResearchEntry> toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanItemResearchTrigger(ItemLike target, ResourceKey<ResearchEntry> toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayer player, Object obj) {
        if (obj instanceof ItemLike itemLike) {
            return target.asItem().equals(itemLike.asItem());
        } else {
            return false;
        }
    }
}
