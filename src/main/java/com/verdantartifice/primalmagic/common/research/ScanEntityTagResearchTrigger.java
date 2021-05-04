package com.verdantartifice.primalmagic.common.research;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tags.ITag;

/**
 * Definition of a trigger that grants a specified research entry upon scanning an entity type in a
 * given tag.
 * 
 * @author Daedalus4096
 */
public class ScanEntityTagResearchTrigger extends AbstractScanResearchTrigger {
    protected final ITag<EntityType<?>> target;

    public ScanEntityTagResearchTrigger(ITag<EntityType<?>> target, SimpleResearchKey toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanEntityTagResearchTrigger(ITag<EntityType<?>> target, SimpleResearchKey toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayerEntity player, Object obj) {
        if (obj instanceof EntityType<?>) {
            return this.target.contains((EntityType<?>)obj);
        } else {
            return false;
        }
    }
}
