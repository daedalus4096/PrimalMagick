package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.util.CodecUtils;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition of a research discipline.  A discipline represents a collection of research entries of
 * similar type (e.g. alchemy).  They are unlocked, yielding access to their entries, by completing
 * other research entries.
 * 
 * @author Daedalus4096
 */
public record ResearchDiscipline(ResearchDisciplineKey key, Optional<AbstractRequirement<?>> unlockRequirementOpt, ResourceLocation iconLocation, Optional<Stat> craftingStat, 
        Optional<Stat> expertiseStat, OptionalInt indexSortOrder) {
    public static Codec<ResearchDiscipline> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                ResearchDisciplineKey.CODEC.fieldOf("key").forGetter(ResearchDiscipline::key),
                AbstractRequirement.dispatchCodec().optionalFieldOf("unlockRequirementOpt").forGetter(ResearchDiscipline::unlockRequirementOpt),
                ResourceLocation.CODEC.fieldOf("iconLocation").forGetter(ResearchDiscipline::iconLocation),
                ResourceLocation.CODEC.optionalFieldOf("craftingStat").xmap(locOpt -> locOpt.map(loc -> StatsManager.getStat(loc)), statOpt -> statOpt.map(stat -> stat.key())).forGetter(ResearchDiscipline::craftingStat),
                ResourceLocation.CODEC.optionalFieldOf("expertiseStat").xmap(locOpt -> locOpt.map(loc -> StatsManager.getStat(loc)), statOpt -> statOpt.map(stat -> stat.key())).forGetter(ResearchDiscipline::expertiseStat),
                CodecUtils.asOptionalInt(Codec.INT.optionalFieldOf("indexSortOrder")).forGetter(ResearchDiscipline::indexSortOrder)
            ).apply(instance, ResearchDiscipline::new));
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return String.join(".", "research_discipline", PrimalMagick.MODID, this.key.getRootKey().location().getPath());
    }
    
    public Stream<ResearchEntry> getEntryStream(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).stream().filter(e -> e.isForDiscipline(this.key));
    }
    
    /**
     * Get the list of all research entries, from any discipline, which serve as a finale to this discipline.
     * 
     * @return finale research entries for this discipline
     */
    @Nonnull
    public List<ResearchEntry> getFinaleEntries(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).stream().filter(e -> e.isFinaleFor(this.key.getRootKey())).toList();
    }
    
    public static Builder builder(ResourceKey<ResearchDiscipline> key) {
        return new Builder(new ResearchDisciplineKey(key));
    }

    public static class Builder {
        protected final ResearchDisciplineKey key;
        protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
        protected ResourceLocation iconLocation = null;
        protected Optional<Stat> craftingStat = Optional.empty();
        protected Optional<Stat> expertiseStat = Optional.empty();
        protected OptionalInt indexSortOrder = OptionalInt.empty();
        
        public Builder(ResearchDisciplineKey key) {
            this.key = Preconditions.checkNotNull(key);
        }
        
        public Builder unlock(AbstractRequirement<?> requirement) {
            this.requirements.add(requirement);
            return this;
        }
        
        public Builder unlock(ResourceKey<ResearchEntry> requiredResearchEntry) {
            return this.unlock(new ResearchRequirement(new ResearchEntryKey(requiredResearchEntry)));
        }
        
        public Builder icon(ResourceLocation iconLocation) {
            this.iconLocation = iconLocation;
            return this;
        }
        
        public Builder craftingStat(Stat stat) {
            this.craftingStat = Optional.ofNullable(stat);
            return this;
        }
        
        public Builder expertiseStat(Stat stat) {
            this.expertiseStat = Optional.ofNullable(stat);
            return this;
        }
        
        public Builder indexSortOrder(int order) {
            this.indexSortOrder = OptionalInt.of(order);
            return this;
        }
        
        protected Optional<AbstractRequirement<?>> getFinalRequirement() {
            if (this.requirements.isEmpty()) {
                return Optional.empty();
            } else if (this.requirements.size() == 1) {
                return Optional.of(this.requirements.get(0));
            } else {
                return Optional.of(new AndRequirement(this.requirements));
            }
        }
        
        private void validate() {
            if (this.iconLocation == null) {
                throw new IllegalStateException("Research discipline must have an icon");
            }
        }
        
        public ResearchDiscipline build() {
            this.validate();
            return new ResearchDiscipline(this.key, this.getFinalRequirement(), this.iconLocation, this.craftingStat, this.expertiseStat, this.indexSortOrder);
        }
    }
}
