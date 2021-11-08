package com.verdantartifice.primalmagick.common.research;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;

/**
 * Definition of a trigger that grants a specified research entry upon scanning an entity type in a
 * given tag.
 * 
 * @author Daedalus4096
 */
public class ScanEntityTagResearchTrigger extends AbstractScanResearchTrigger {
    protected final Tag<EntityType<?>> target;

    public ScanEntityTagResearchTrigger(Tag<EntityType<?>> target, SimpleResearchKey toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanEntityTagResearchTrigger(Tag<EntityType<?>> target, SimpleResearchKey toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayer player, Object obj) {
        if (obj instanceof EntityType<?>) {
            return this.target.contains((EntityType<?>)obj);
        } else {
            return false;
        }
    }
}
