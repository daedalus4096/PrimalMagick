package com.verdantartifice.primalmagick.common.blockstates.properties;

import net.minecraft.util.StringRepresentable;

/**
 * Representation of a connection, or lack thereof, to a skyglass pane.
 * 
 * @author Daedalus4096
 */
public enum SkyglassPaneSide implements StringRepresentable {
    NONE("none"),
    GLASS("glass"),
    OTHER("other");
    
    private final String name;
    
    private SkyglassPaneSide(String name) {
        this.name = name;
    }
    
    @Override
    public String getSerializedName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.getSerializedName();
    }
}
