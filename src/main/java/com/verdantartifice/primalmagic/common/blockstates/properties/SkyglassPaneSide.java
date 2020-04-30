package com.verdantartifice.primalmagic.common.blockstates.properties;

import net.minecraft.util.IStringSerializable;

/**
 * Representation of a connection, or lack thereof, to a skyglass pane.
 * 
 * @author Daedalus4096
 */
public enum SkyglassPaneSide implements IStringSerializable {
    NONE("none"),
    GLASS("glass"),
    OTHER("other");
    
    private final String name;
    
    private SkyglassPaneSide(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
}
