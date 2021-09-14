package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

/**
 * Interface for a crafting recipe that optionally requires mod research to unlock.
 * 
 * @author Daedalus4096
 */
public interface IHasRequiredResearch {
    /**
     * Get the required research for the recipe.
     * 
     * @return the required research for the recipe
     */
    public CompoundResearchKey getRequiredResearch();
}
