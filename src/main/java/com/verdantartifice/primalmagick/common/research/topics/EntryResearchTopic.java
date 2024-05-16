package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Research topic that points to a mod research entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class EntryResearchTopic extends AbstractResearchTopic {
    public EntryResearchTopic(ResearchEntryKey entryKey, int page) {
        super(AbstractResearchTopic.Type.RESEARCH_ENTRY, entryKey.getRootKey().location().toString(), page);
    }
    
    @Nullable
    public ResearchEntryKey getData() {
        return new ResearchEntryKey(ResourceKey.create(RegistryKeysPM.RESEARCH_ENTRIES, new ResourceLocation(this.data)));
    }
}
