package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.sources.Source;

/**
 * Research topic that points to an attunement entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class SourceResearchTopic extends AbstractResearchTopic {
    public SourceResearchTopic(Source source) {
        super(AbstractResearchTopic.Type.SOURCE, source.getTag());
    }
    
    @Nullable
    public Source getData() {
        return Source.getSource(this.data);
    }
}
