package com.verdantartifice.primalmagick.common.crafting;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;

import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Interface for a crafting recipe that grants expertise upon being crafted.
 * 
 * @author Daedalus4096
 */
public interface IHasExpertise extends IHasRequirement {
    /**
     * Get the amount of expertise to be granted to the player any time this recipe is crafted.
     * 
     * @param registryAccess a registry access object
     * @return the recipe's expertise reward
     */
    int getExpertiseReward(RegistryAccess registryAccess);
    
    /**
     * Get the amount of bonus expertise to be granted to the player the first time this recipe,
     * or a recipe in its group, is crafted.
     * 
     * @param registryAccess a registry access object
     * @return the recipe's bonus expertise reward
     */
    int getBonusExpertiseReward(RegistryAccess registryAccess);
    
    /**
     * Determine whether this recipe has an expertise reward.
     * 
     * @param registryAccess a registry access object
     * @return true if the recipe has a total expertise reward greater than zero, false otherwise
     */
    default boolean hasExpertiseReward(RegistryAccess registryAccess) {
        return (this.getExpertiseReward(registryAccess) > 0) || (this.getBonusExpertiseReward(registryAccess) > 0);
    }
    
    /**
     * Get the identifier of the group of recipes that this recipe belongs to, if any, for purposes
     * of determining first-craft expertise bonuses.  Only the first recipe in a group grants bonus
     * expertise.
     * 
     * @return the recipe's expertise group, if any
     */
    Optional<ResourceLocation> getExpertiseGroup();
    
    /**
     * Get the localized text describing this recipe's expertise group for tooltip rendering.
     * 
     * @return the localized description of this recipe's expertise group
     */
    default Optional<Component> getExpertiseGroupDescription() {
        return this.getExpertiseGroup().map(loc -> Component.translatable(Util.makeDescriptionId("expertise_group", loc)));
    }
    
    /**
     * Calculates the research tier that the recipe belongs to based on its research requirements.
     * 
     * @param registryAccess a registry access object
     * @return the recipe's research tier, if any
     */
    default Optional<ResearchTier> getResearchTier(RegistryAccess registryAccess) {
        return this.getRequirement().flatMap(req -> req.getResearchTier(registryAccess));
    }
    
    /**
     * Get the manually-specified override research discipline for this recipe, if any.
     * 
     * @return the recipe's discipline override, if any
     */
    Optional<ResearchDisciplineKey> getResearchDisciplineOverride();
    
    /**
     * Get the research discipline for which expertise should be credited for this recipe, if any.
     * 
     * @param registryAccess a registry access object
     * @return the recipe's research discipline, if any
     */
    default Optional<ResearchDisciplineKey> getResearchDiscipline(RegistryAccess registryAccess, ResourceLocation recipeId) {
        return this.getResearchDisciplineOverride().or(() -> {
            return this.getRequirement().flatMap(req -> {
                Set<ResearchDisciplineKey> foundDisciplines = new HashSet<>();
                for (AbstractResearchKey<?> rawKey : req.streamKeys().toList()) {
                    if (rawKey instanceof ResearchEntryKey entryKey) {
                        ResearchEntry entry = ResearchEntries.getEntry(registryAccess, entryKey);
                        if (entry != null) {
                            entry.disciplineKeyOpt().ifPresent(foundDisciplines::add);
                        }
                    }
                }
                if (foundDisciplines.size() > 1) {
                    LogManager.getLogger().warn("Multiple disciplines found for recipe {}", recipeId);
                }
                return foundDisciplines.stream().findFirst();
            });
        });
    }
}
