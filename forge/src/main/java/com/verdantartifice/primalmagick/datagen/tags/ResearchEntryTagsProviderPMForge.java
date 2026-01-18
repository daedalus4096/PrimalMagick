package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.tags.ResearchEntryTagsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all the mod's research entry tags.
 * 
 * @author Daedalus4096
 */
public class ResearchEntryTagsProviderPMForge extends KeyTagProvider<ResearchEntry> {
    public ResearchEntryTagsProviderPMForge(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, RegistryKeysPM.RESEARCH_ENTRIES, future, Constants.MOD_ID, helper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider pProvider) {
        this.tag(ResearchEntryTagsPM.OPAQUE).add(ResearchEntries.DISCOVER_BLOOD).add(ResearchEntries.DISCOVER_INFERNAL).add(ResearchEntries.DISCOVER_VOID).add(ResearchEntries.DISCOVER_HALLOWED).add(ResearchEntries.DISCOVER_FORBIDDEN);
    }
}
