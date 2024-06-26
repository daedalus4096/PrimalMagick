package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;

import net.minecraft.resources.ResourceLocation;

/**
 * Research topic that points to an attunement entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class SourceResearchTopic extends AbstractResearchTopic {
    public SourceResearchTopic(Source source, int page) {
        super(AbstractResearchTopic.Type.SOURCE, source.getId().toString(), page);
    }
    
    @Nullable
    public Source getData() {
        return Sources.get(ResourceLocation.parse(this.data));
    }
}
