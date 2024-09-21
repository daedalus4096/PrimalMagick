package com.verdantartifice.primalmagick.common.blockstates.properties;

import net.minecraft.util.StringRepresentable;

/**
 * Representation of a connection, or lack thereof, to a salt trail.
 * 
 * @author Daedalus4096
 */
public enum SaltSide implements StringRepresentable {
    UP("up"),
    SIDE("side"),
    NONE("none");
    
    private final String name;
    
    private SaltSide(String name) {
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
