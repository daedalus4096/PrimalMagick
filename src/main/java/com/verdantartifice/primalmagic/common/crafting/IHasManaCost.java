package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.sources.SourceList;

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
