package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

/**
 * Research topic that points to a mod research entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class EntryResearchTopic extends AbstractResearchTopic {
    public EntryResearchTopic(ResearchEntry entry, int page) {
        super(AbstractResearchTopic.Type.RESEARCH_ENTRY, entry.getKey().getRootKey(), page);
    }
    
    @Nullable
    public ResearchEntry getData() {
        return ResearchEntries.getEntry(SimpleResearchKey.parse(this.data));
    }
}
