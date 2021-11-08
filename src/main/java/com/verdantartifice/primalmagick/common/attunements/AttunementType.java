package com.verdantartifice.primalmagick.common.attunements;

import javax.annotation.Nonnull;

/**
 * Represents a type of magical attunement.  Permanent attunement is gained through research and
 * cannot be removed.  Induced attunement is gained or lost through rituals, but does not decay
 * over time.  Temporary attunement is gained by crafting items and casting spells, but decays
 * slowly over time.
 * 
 * @author Daedalus4096
 */
public enum AttunementType {
    PERMANENT(-1),
    INDUCED(50),
    TEMPORARY(50);
    
    private int maximum;    // The maximum attunement amount of this type that the player can have at once
    
    private AttunementType(int max) {
        this.maximum = max;
    }
    
    public boolean isCapped() {
        return (this.maximum > 0);
    }
    
    public int getMaximum() {
        return this.maximum;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return "primalmagick.attunement_type." + this.name();
    }
}
