package com.verdantartifice.primalmagic.common.blockstates.properties;

import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IWorld;

public enum TimePhase implements IStringSerializable {
    FULL("full", 0.0F, 0.0F),
    FADED("faded", -1.0F, 3600000.0F);
    
    private final String name;
    private final float hardness;
    private final float resistance;
    
    private TimePhase(String name, float hardness, float resistance) {
        this.name = name;
        this.hardness = hardness;
        this.resistance = resistance;
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
    
    public float getHardness() {
        return this.hardness;
    }
    
    public float getResistance() {
        return this.resistance;
    }
}
