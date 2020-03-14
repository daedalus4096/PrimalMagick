package com.verdantartifice.primalmagic.common.blockstates.properties;

import net.minecraft.util.IStringSerializable;

/**
 * Representation of a connection, or lack thereof, to a salt trail.
 * 
 * @author Daedalus4096
 */
public enum SaltSide implements IStringSerializable {
    UP("up"),
    SIDE("side"),
    NONE("none");
    
    private final String name;
    
    private SaltSide(String name) {
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
