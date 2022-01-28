package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

/**
 * Research topic that points to a specific page in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class OtherResearchTopic extends AbstractResearchTopic {
    public OtherResearchTopic(String data, int page) {
        super(AbstractResearchTopic.Type.OTHER, data, page);
    }
    
    @Nullable
    public String getData() {
        return this.data;
    }
}
