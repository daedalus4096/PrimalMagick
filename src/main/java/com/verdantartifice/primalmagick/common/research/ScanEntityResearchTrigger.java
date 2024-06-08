package com.verdantartifice.primalmagick.common.research;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

/**
 * Definition of a trigger that grants a specified research entry upon scanning a given entity type.
 * 
 * @author Daedalus4096
 */
public class ScanEntityResearchTrigger extends AbstractScanResearchTrigger {
    protected final EntityType<?> target;
    
    public ScanEntityResearchTrigger(EntityType<?> target, ResourceKey<ResearchEntry> toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanEntityResearchTrigger(EntityType<?> target, ResourceKey<ResearchEntry> toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayer player, Object obj) {
        if (obj instanceof EntityType<?> entityType) {
            return this.target.equals(entityType);
        } else {
            return false;
        }
    }
}
