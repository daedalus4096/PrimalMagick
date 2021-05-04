package com.verdantartifice.primalmagic.common.research;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;

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
    public boolean matches(ServerPlayerEntity player, Object obj) {
        if (obj instanceof EntityType<?>) {
            return this.target.equals((EntityType<?>)obj);
        } else {
            return false;
        }
    }
}
