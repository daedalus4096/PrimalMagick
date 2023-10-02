package com.verdantartifice.primalmagick.common.research;

/**
 * Definition of a registered research name, used as a component in and for validation of simple and 
 * compound research keys.
 * 
 * @author Daedalus4096
 */
public record ResearchName(String rootName) {
    public SimpleResearchKey simpleKey() {
        return SimpleResearchKey.of(this);
    }
    
    public SimpleResearchKey simpleKey(int stage) {
        return SimpleResearchKey.of(this, stage);
    }
    
    public CompoundResearchKey compoundKey() {
        return CompoundResearchKey.from(this.simpleKey());
    }
    
    public boolean matches(String toMatch) {
        return this.rootName().equals(toMatch);
    }
}
