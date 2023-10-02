package com.verdantartifice.primalmagick.common.research;

import net.minecraft.server.level.ServerPlayer;

/**
 * Definition of a trigger that grants a specified research entry upon finding a scan match.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractScanResearchTrigger implements IScanTrigger {
    protected static final SimpleResearchKey SCANS_KEY = ResearchNames.UNLOCK_SCANS.get().simpleKey();

    protected final SimpleResearchKey toUnlock;
    protected final boolean unlockScansPage;
    
    protected AbstractScanResearchTrigger(SimpleResearchKey toUnlock, boolean unlockScansPage) {
        this.toUnlock = toUnlock.copy();
        this.unlockScansPage = unlockScansPage;
    }

    @Override
    public void onMatch(ServerPlayer player, Object obj) {
        if (this.unlockScansPage) {
            ResearchManager.completeResearch(player, SCANS_KEY);
        }
        ResearchManager.completeResearch(player, this.toUnlock);
    }
}
