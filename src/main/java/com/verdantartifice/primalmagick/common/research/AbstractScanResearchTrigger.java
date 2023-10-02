package com.verdantartifice.primalmagick.common.research;

import java.util.function.Supplier;

import net.minecraft.server.level.ServerPlayer;

/**
 * Definition of a trigger that grants a specified research entry upon finding a scan match.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractScanResearchTrigger implements IScanTrigger {
    protected static final Supplier<SimpleResearchKey> SCANS_KEY = ResearchNames.simpleKey(ResearchNames.UNLOCK_SCANS);

    protected final SimpleResearchKey toUnlock;
    protected final boolean unlockScansPage;
    
    protected AbstractScanResearchTrigger(SimpleResearchKey toUnlock, boolean unlockScansPage) {
        this.toUnlock = toUnlock.copy();
        this.unlockScansPage = unlockScansPage;
    }

    @Override
    public void onMatch(ServerPlayer player, Object obj) {
        if (this.unlockScansPage) {
            ResearchManager.completeResearch(player, SCANS_KEY.get());
        }
        ResearchManager.completeResearch(player, this.toUnlock);
    }
}
