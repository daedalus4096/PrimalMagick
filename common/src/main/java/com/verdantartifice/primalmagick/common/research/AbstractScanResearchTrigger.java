package com.verdantartifice.primalmagick.common.research;

import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;

/**
 * Definition of a trigger that grants a specified research entry upon finding a scan match.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractScanResearchTrigger implements IScanTrigger {
    protected final ResearchEntryKey toUnlock;
    protected final boolean unlockScansPage;
    
    protected AbstractScanResearchTrigger(ResourceKey<ResearchEntry> toUnlock, boolean unlockScansPage) {
        this.toUnlock = new ResearchEntryKey(toUnlock);
        this.unlockScansPage = unlockScansPage;
    }

    @Override
    public void onMatch(ServerPlayer player, Object obj) {
        if (this.unlockScansPage) {
            ResearchManager.completeResearch(player, ResearchEntries.UNLOCK_SCANS);
        }
        ResearchManager.completeResearch(player, this.toUnlock);
    }
}
