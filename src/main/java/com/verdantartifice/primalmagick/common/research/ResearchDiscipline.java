package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.stats.Stat;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition of a research discipline.  A discipline represents a collection of research entries of
 * similar type (e.g. alchemy).  They are unlocked, yielding access to their entries, by completing
 * other research entries.
 * 
 * @author Daedalus4096
 */
public record ResearchDiscipline(ResearchDisciplineKey key, Optional<AbstractRequirement<?>> unlockRequirement, ResourceLocation iconLocation, Optional<Stat> craftingStat, OptionalInt indexSortOrder) {
    @Nonnull
    public String getNameTranslationKey() {
        return String.join(".", "research_discipline", PrimalMagick.MODID, this.key.getRootKey());
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
    
    public static class Builder {
        protected final ResearchDisciplineKey key;
        protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
        protected ResourceLocation iconLocation = null;
        protected Optional<Stat> craftingStat = Optional.empty();
        protected OptionalInt indexSortOrder = OptionalInt.empty();
        
        public Builder(ResearchDisciplineKey key) {
            this.key = Preconditions.checkNotNull(key);
        }
        
        public Builder unlock(AbstractRequirement<?> requirement) {
            this.requirements.add(requirement);
            return this;
        }
        
        public Builder icon(ResourceLocation iconLocation) {
            this.iconLocation = iconLocation;
            return this;
        }
        
        public Builder craftingStat(Stat stat) {
            this.craftingStat = Optional.of(stat);
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
            return new ResearchDiscipline(this.key, this.getFinalRequirement(), this.iconLocation, this.craftingStat, this.indexSortOrder);
        }
    }
}
