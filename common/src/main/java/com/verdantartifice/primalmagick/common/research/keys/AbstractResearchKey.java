package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.mutable.MutableBoolean;

import javax.annotation.Nullable;

/**
 * Base class representing an atom in the research hierarchy.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractResearchKey<T extends AbstractResearchKey<T>> {
    public static Codec<AbstractResearchKey<?>> dispatchCodec() {
        return Services.RESEARCH_KEY_TYPES.codec().dispatch("key_type", AbstractResearchKey::getType, ResearchKeyType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractResearchKey<?>> dispatchStreamCodec() {
        return Services.RESEARCH_KEY_TYPES.registryFriendlyStreamCodec().dispatch(AbstractResearchKey::getType, ResearchKeyType::streamCodec);
    }
    
    @Override
    public abstract String toString();
    
    @Override
    public abstract int hashCode();
    
    @Override
    public abstract boolean equals(Object obj);
    
    /**
     * Returns the category of requirement to be used when this key is part of a {@link com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement}.
     * 
     * @return this key's corresponding requirement category
     */
    public abstract RequirementCategory getRequirementCategory();

    protected abstract ResearchKeyType<T> getType();
    
    public abstract IconDefinition getIcon(RegistryAccess registryAccess);
    
    public boolean isKnownBy(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            MutableBoolean retVal = new MutableBoolean(false);
            Services.CAPABILITIES.knowledge(player).ifPresent(knowledge -> {
                retVal.setValue(knowledge.isResearchComplete(player.level().registryAccess(), this));
            });
            return retVal.booleanValue();
        }
    }
    
    public static AbstractResearchKey<?> fromNetwork(RegistryFriendlyByteBuf buf) {
        return AbstractResearchKey.dispatchStreamCodec().decode(buf);
    }
    
    public void toNetwork(RegistryFriendlyByteBuf buf) {
        AbstractResearchKey.dispatchStreamCodec().encode(buf, this);
    }
}
