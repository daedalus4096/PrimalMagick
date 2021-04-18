package com.verdantartifice.primalmagic.common.research;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;

/**
 * Definition of a trigger that grants a specified research entry upon scanning a block/item in a
 * given tag.
 * 
 * @author Daedalus4096
 */
public class ScanTagResearchTrigger extends AbstractScanResearchTrigger {
    protected final ITag<Item> target;
    
    public ScanTagResearchTrigger(ITag<Item> target, SimpleResearchKey toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanTagResearchTrigger(ITag<Item> target, SimpleResearchKey toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayerEntity player, IItemProvider itemProvider) {
        return itemProvider.asItem().isIn(this.target);
    }
}
