package com.verdantartifice.primalmagic.common.blockstates.properties;

import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IWorld;

public enum TimePhase implements IStringSerializable {
    FULL("full", 0.0F, 0.0F),   // Default to block-specified hardness and resistance
    WAXING("waxing", 6.0F, 12.0F),
    WANING("waning", 50.0F, 1200.0F),
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
}
