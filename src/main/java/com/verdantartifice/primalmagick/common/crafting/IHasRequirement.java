package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;

/**
 * Interface for a crafting recipe that optionally requires mod research to unlock.
 * 
 * @author Daedalus4096
 */
public interface IHasRequirement {
    /**
     * Get the requirement for the recipe.
     * 
     * @return the requirement for the recipe
     */
    public Optional<AbstractRequirement<?>> getRequirement();
}
