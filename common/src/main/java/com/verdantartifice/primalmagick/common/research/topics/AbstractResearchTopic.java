package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Base research topic that points to a specific page in the Grimoire.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractResearchTopic<T extends AbstractResearchTopic<T>> {
    public static Codec<AbstractResearchTopic<?>> dispatchCodec() {
        return Services.RESEARCH_TOPIC_TYPES_REGISTRY.codec().dispatch("topic_type", AbstractResearchTopic::getType, ResearchTopicType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractResearchTopic<?>> dispatchStreamCodec() {
        return Services.RESEARCH_TOPIC_TYPES_REGISTRY.registryFriendlyStreamCodec().dispatch(AbstractResearchTopic::getType, ResearchTopicType::streamCodec);
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

    public boolean isUnread(Player player) {
        return false;
    }

    public Optional<Component> getUnreadTooltip(Player player) {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AbstractResearchTopic<?> that)) return false;
        return page == that.page;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(page);
    }
}
