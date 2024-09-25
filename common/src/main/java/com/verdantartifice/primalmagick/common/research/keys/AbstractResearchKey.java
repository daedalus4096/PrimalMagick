package com.verdantartifice.primalmagick.common.research.keys;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.registries.RegistryCodecs;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

/**
 * Base class representing an atom in the research hierarchy.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractResearchKey<T extends AbstractResearchKey<T>> {
    public static Codec<AbstractResearchKey<?>> dispatchCodec() {
        return RegistryCodecs.codec(ResearchKeyTypesPM.TYPES).dispatch("key_type", AbstractResearchKey::getType, ResearchKeyType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractResearchKey<?>> dispatchStreamCodec() {
        return RegistryCodecs.registryFriendlyStreamCodec(ResearchKeyTypesPM.TYPES).dispatch(AbstractResearchKey::getType, ResearchKeyType::streamCodec);
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
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
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
