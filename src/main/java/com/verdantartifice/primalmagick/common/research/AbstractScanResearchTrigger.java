package com.verdantartifice.primalmagick.common.research;

import java.util.function.Supplier;

import net.minecraft.server.level.ServerPlayer;

/**
 * Definition of a trigger that grants a specified research entry upon finding a scan match.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractScanResearchTrigger implements IScanTrigger {
    protected final SimpleResearchKey toUnlock;
    protected final boolean unlockScansPage;
    
    protected AbstractScanResearchTrigger(SimpleResearchKey toUnlock, boolean unlockScansPage) {
        this.toUnlock = toUnlock.copy();
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
