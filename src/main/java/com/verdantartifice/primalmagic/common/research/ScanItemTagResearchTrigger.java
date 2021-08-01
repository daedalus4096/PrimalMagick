package com.verdantartifice.primalmagic.common.research;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

/**
 * Definition of a trigger that grants a specified research entry upon scanning a block/item in a
 * given tag.
 * 
 * @author Daedalus4096
 */
public class ScanItemTagResearchTrigger extends AbstractScanResearchTrigger {
    protected final Tag<Item> target;
    
    public ScanItemTagResearchTrigger(Tag<Item> target, SimpleResearchKey toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanItemTagResearchTrigger(Tag<Item> target, SimpleResearchKey toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayer player, Object obj) {
        if (obj instanceof ItemLike) {
            return this.target.contains(((ItemLike)obj).asItem());
        } else {
            return false;
        }
    }
}
