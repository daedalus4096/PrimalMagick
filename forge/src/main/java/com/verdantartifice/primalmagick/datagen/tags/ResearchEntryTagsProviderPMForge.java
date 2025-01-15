package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.tags.ResearchEntryTagsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all the mod's research entry tags.
 * 
 * @author Daedalus4096
 */
public class ResearchEntryTagsProviderPMForge extends TagsProvider<ResearchEntry> {
    public ResearchEntryTagsProviderPMForge(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, RegistryKeysPM.RESEARCH_ENTRIES, future, Constants.MOD_ID, helper);
    }

    @Override
    protected void addTags(Provider pProvider) {
        this.tag(ResearchEntryTagsPM.OPAQUE).add(ResearchEntries.DISCOVER_BLOOD, ResearchEntries.DISCOVER_INFERNAL, ResearchEntries.DISCOVER_VOID, ResearchEntries.DISCOVER_HALLOWED, ResearchEntries.DISCOVER_FORBIDDEN);
    }
}
