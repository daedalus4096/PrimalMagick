package com.verdantartifice.primalmagick.common.crafting;

/**
 * Interface denoting a type of recipe that can show up in a player's arcane recipe book.
 * 
 * @author Daedalus4096
 */
public interface IArcaneRecipeBookItem {
    /**
     * If true, the recipoe is special and should not show up in the arcane recipe book.
     * 
     * @return whether the recipe should be excluded from the arcane recipe book
     */
    public default boolean isArcaneSpecial() {
        return false;
    }
}
