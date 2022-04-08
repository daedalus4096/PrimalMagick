package com.verdantartifice.primalmagick.common.research;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * Definition of a trigger that grants a specified research entry upon scanning a block/item in a
 * given tag.
 * 
 * @author Daedalus4096
 */
public class ScanItemTagResearchTrigger extends AbstractScanResearchTrigger {
    protected final TagKey<Item> target;
    
    public ScanItemTagResearchTrigger(TagKey<Item> target, SimpleResearchKey toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanItemTagResearchTrigger(TagKey<Item> target, SimpleResearchKey toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayer player, Object obj) {
        if (obj instanceof ItemLike itemLike) {
            return new ItemStack(itemLike).is(this.target);
        } else {
            return false;
        }
    }
}
