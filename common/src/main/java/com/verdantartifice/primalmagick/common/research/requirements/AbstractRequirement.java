package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.RegistryCodecs;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

/**
 * Represents a predicate determining whether a player meets a given requirement for
 * progressing in the research system.
 */
public abstract class AbstractRequirement<T extends AbstractRequirement<T>> {
    public static Codec<AbstractRequirement<?>> dispatchCodec() {
        return RegistryCodecs.codec(RequirementsPM.TYPES).dispatch("requirement_type", AbstractRequirement::getType, type -> type.codecSupplier().get());
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractRequirement<?>> dispatchStreamCodec() {
        return RegistryCodecs.registryFriendlyStreamCodec(RequirementsPM.TYPES).dispatch(AbstractRequirement::getType, type -> type.streamCodecSupplier().get());
    }
    
    public abstract boolean isMetBy(@Nullable Player player);
    public abstract void consumeComponents(@Nullable Player player);
    public abstract boolean forceComplete(@Nullable Player player);
    
    public abstract RequirementCategory getCategory();
    public abstract Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category);
    
    /**
     * Returns true if the given research key is part of this requirement.
     * 
     * @param researchKey the research key to be tested
     * @return whether the given research key is part of this requirement
     */
    public boolean contains(AbstractResearchKey<?> researchKey) {
        return false;
    }
    
    /**
     * Returns a stream of the research keys used in this requirement, if any.
     * 
     * @return a stream of the research keys used in this requirement
     */
    public Stream<AbstractResearchKey<?>> streamKeys() {
        return Stream.empty();
    }
    
    /**
     * Returns the highest research tier represented by the keys comprising this requirement, if any.
     * 
     * @param registryAccess a registry access object
     * @return the highest research tier represented by the keys comprising this requirement, if any
     */
    public Optional<ResearchTier> getResearchTier(RegistryAccess registryAccess) {
        Optional<ResearchTier> retVal = Optional.empty();
        for (AbstractResearchKey<?> rawKey : this.streamKeys().toList()) {
            if (rawKey instanceof ResearchEntryKey entryKey) {
                ResearchEntry entry = ResearchEntries.getEntry(registryAccess, entryKey);
                if (entry != null) {
                    Optional<ResearchTier> tierOpt = entry.tierOpt();
                    if (retVal.isEmpty() || (tierOpt.isPresent() && tierOpt.get().compareTo(retVal.get()) > 0)) {
                        retVal = tierOpt;
                    }
                }
            }
        }
        return retVal;
    }
    
    protected abstract RequirementType<T> getType();
    
    public static AbstractRequirement<?> fromNetwork(RegistryFriendlyByteBuf buf) {
        return AbstractRequirement.dispatchStreamCodec().decode(buf);
    }
    
    public void toNetwork(RegistryFriendlyByteBuf buf) {
        AbstractRequirement.dispatchStreamCodec().encode(buf, this);
    }
}
