package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.sources.SourceList;

/**
 * Interface for a crafting recipe that has a mana cost.
 * 
 * @author Daedalus4096
 */
public interface IHasManaCost {
    /**
     * Get the mana cost for the recipe.
     * 
     * @return the mana cost for the recipe
     */
    public SourceList getManaCosts();
}
