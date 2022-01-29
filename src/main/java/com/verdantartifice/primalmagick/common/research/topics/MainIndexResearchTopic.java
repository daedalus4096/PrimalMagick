package com.verdantartifice.primalmagick.common.research.topics;

/**
 * Research topic that points to the main index of the Grimoire.
 * 
 * @author Daedalus4096
 */
public class MainIndexResearchTopic extends AbstractResearchTopic {
    public static final MainIndexResearchTopic INSTANCE = new MainIndexResearchTopic();
    
    protected MainIndexResearchTopic() {
        super(AbstractResearchTopic.Type.MAIN_INDEX, "", 0);
    }
}
