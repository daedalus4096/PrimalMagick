package com.verdantartifice.primalmagic.common.research;

import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerPlayer;

/**
 * Definition of a trigger that grants a specified research entry upon scanning a given entity type.
 * 
 * @author Daedalus4096
 */
public class ScanEntityResearchTrigger extends AbstractScanResearchTrigger {
    protected final EntityType<?> target;
    
    public ScanEntityResearchTrigger(EntityType<?> target, SimpleResearchKey toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanEntityResearchTrigger(EntityType<?> target, SimpleResearchKey toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayer player, Object obj) {
        if (obj instanceof EntityType<?>) {
            return this.target.equals((EntityType<?>)obj);
        } else {
            return false;
        }
    }
}
