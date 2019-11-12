package com.verdantartifice.primalmagic.common.blockstates.properties;

import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IWorld;

public enum TimePhase implements IStringSerializable {
    FULL("full"),
    FADED("faded");
    
    private final String name;
    
    private TimePhase(String name) {
        this.name = name;
    }
    
    public static TimePhase getSunPhase(IWorld world) {
        float angle = world.getCelestialAngle(1.0F);
        if (angle > 0.25F && angle <= 0.75F) {
            return FADED;
        } else {
            return FULL;
        }
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
