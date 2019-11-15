package com.verdantartifice.primalmagic.common.blockstates.properties;

import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IWorld;

public enum TimePhase implements IStringSerializable {
    FULL("full", 0.0F, 0.0F, 2),    // Default to block-specified hardness and resistance
    WAXING("waxing", 6.0F, 12.0F, 1),
    WANING("waning", 50.0F, 1200.0F, 0),
    FADED("faded", -1.0F, 3600000.0F, 0);
    
    private final String name;
    private final float hardness;
    private final float resistance;
    private final int light;
    
    private TimePhase(String name, float hardness, float resistance, int light) {
        this.name = name;
        this.hardness = hardness;
        this.resistance = resistance;
        this.light = light;
    }
    
    public static TimePhase getSunPhase(IWorld world) {
        float angle = world.getCelestialAngle(1.0F);
        if (angle < 0.1875F) {
            return FULL;
        } else if (angle < 0.25F) {
            return WAXING;
        } else if (angle < 0.3125F) {
            return WANING;
        } else if (angle < 0.6875F) {
            return FADED;
        } else if (angle < 0.75F) {
            return WANING;
        } else if (angle < 0.8125F) {
            return WAXING;
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
    
    public int getLightLevel() {
        return this.light;
    }
}
