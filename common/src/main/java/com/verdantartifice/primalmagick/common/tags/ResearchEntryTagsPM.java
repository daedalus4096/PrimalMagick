package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.tags.TagKey;

public class ResearchEntryTagsPM {
    public static final TagKey<ResearchEntry> OPAQUE = create("opaque");
    
    private static TagKey<ResearchEntry> create(String name) {
        return TagKey.create(RegistryKeysPM.RESEARCH_ENTRIES, ResourceUtils.loc(name));
    }
}
