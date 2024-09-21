package com.verdantartifice.primalmagick.common.misc;

/**
 * Helper enum to keep track of what harvest levels correspond to what materials.
 * 
 * @author Daedalus4096
 */
public enum HarvestLevel {
    NONE(-1),
    WOOD(0),
    STONE(1),
    IRON(2),
    DIAMOND(3);
    
    protected final int level;
    
    private HarvestLevel(int level) {
        this.level = level;
    }
    
    public int getLevel() {
        return this.level;
    }
}
