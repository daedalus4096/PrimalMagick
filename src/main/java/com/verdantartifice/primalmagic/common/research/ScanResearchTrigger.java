package com.verdantartifice.primalmagic.common.research;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.IItemProvider;

/**
 * Definition of a trigger that grants a specified research entry upon scanning a given block/item.
 * 
 * @author Daedalus4096
 */
public class ScanResearchTrigger extends AbstractScanResearchTrigger {
    protected final IItemProvider target;
    
    public ScanResearchTrigger(IItemProvider target, SimpleResearchKey toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanResearchTrigger(IItemProvider target, SimpleResearchKey toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayerEntity player, IItemProvider itemProvider) {
        return target.asItem().equals(itemProvider.asItem());
    }
}
