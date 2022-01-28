package com.verdantartifice.primalmagick.common.research.topics;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.StringRepresentable;

/**
 * Base research topic that points to a specific page in the Grimoire.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractResearchTopic {
    protected final AbstractResearchTopic.Type type;
    protected final String data;
    protected final int page;
    
    protected AbstractResearchTopic(AbstractResearchTopic.Type type, String data, int page) {
        this.type = type;
        this.data = data;
        this.page = page;
    }
    
    public int getPage() {
        return this.page;
    }
    
    public AbstractResearchTopic withPage(int newPage) {
        return ResearchTopicFactory.create(this.type, this.data, newPage);
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
        buf.writeUtf(this.data);
        buf.writeVarInt(this.page);
    }
    
    protected static enum Type implements StringRepresentable {
        MAIN_INDEX("main_index"),
        RESEARCH_DISCIPLINE("research_discipline"),
        RESEARCH_ENTRY("research_entry"),
        SOURCE("source"),
        ENCHANTMENT("enchantment"),
        OTHER("other");
        
        private final String name;
        
        private Type(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.getSerializedName();
        }
    }
}
