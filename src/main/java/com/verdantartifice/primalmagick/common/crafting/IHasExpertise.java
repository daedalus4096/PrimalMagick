package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Interface for a crafting recipe that grants expertise upon being crafted.
 * 
 * @author Daedalus4096
 */
public interface IHasExpertise {
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
}
