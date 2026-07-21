package com.verdantartifice.primalmagick.common.research;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;

/**
 * Definition of a trigger that grants a specified research entry upon scanning an entity type in a
 * given tag.
 * 
 * @author Daedalus4096
 */
public class ScanEntityTagResearchTrigger extends AbstractScanResearchTrigger {
    protected final TagKey<EntityType<?>> target;

    public ScanEntityTagResearchTrigger(TagKey<EntityType<?>> target, ResourceKey<ResearchEntry> toUnlock) {
        this(target, toUnlock, true);
    }
    
    public ScanEntityTagResearchTrigger(TagKey<EntityType<?>> target, ResourceKey<ResearchEntry> toUnlock, boolean unlockScansPage) {
        super(toUnlock, unlockScansPage);
        this.target = target;
    }

    @Override
    public boolean matches(ServerPlayer player, Object obj) {
        if (obj instanceof EntityReference<?> entityReference) {
            @SuppressWarnings("unchecked")
            EntityReference<Entity> typedRef = (EntityReference<Entity>)entityReference;
            Entity entity = EntityReference.getEntity(typedRef, player.level());
            if (entity != null) {
                return entity.is(this.target);
            }
        }
        return false;
    }
}
