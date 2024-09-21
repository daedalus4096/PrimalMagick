package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.RegistryCodecs;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Base research topic that points to a specific page in the Grimoire.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractResearchTopic<T extends AbstractResearchTopic<T>> {
    public static Codec<AbstractResearchTopic<?>> dispatchCodec() {
        return RegistryCodecs.codec(ResearchTopicTypesPM.TYPES).dispatch("topic_type", AbstractResearchTopic::getType, ResearchTopicType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractResearchTopic<?>> dispatchStreamCodec() {
        return RegistryCodecs.registryFriendlyStreamCodec(ResearchTopicTypesPM.TYPES).dispatch(AbstractResearchTopic::getType, ResearchTopicType::streamCodec);
    }
    
    protected final int page;
    
    protected AbstractResearchTopic(int page) {
        this.page = page;
    }
    
    public abstract ResearchTopicType<T> getType();
    
    public int getPage() {
        return this.page;
    }
    
    public abstract T withPage(int newPage);
}
