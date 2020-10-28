package com.verdantartifice.primalmagic.common.blockstates.properties;

import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IWorld;

/**
 * Representation of the current phase of a block that phases in and out over time.
 * 
 * @author Daedalus4096
 */
public enum TimePhase implements IStringSerializable {
    FULL("full", 0.0F, 0.0F, 10),           // The block is fully phased in; use default attributes and full light
    WAXING("waxing", 6.0F, 12.0F, 5),       // The block is mostly phased in; increased hardness/resistance and half light
    WANING("waning", 50.0F, 1200.0F, 0),    // The block is mostly phased out; very high hardness/resistance and no light
    FADED("faded", -1.0F, 3600000.0F, 0);   // The block is fully phased out; unbreakable and no light
    
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
        float angle = world.func_242415_f(1.0F);
        if (angle < 0.1875F) {
            return FULL;    // Afternoon
        } else if (angle < 0.25F) {
            return WAXING;  // Just before sunset
        } else if (angle < 0.3125F) {
            return WANING;  // Just after sunset
        } else if (angle < 0.6875F) {
            return FADED;   // Night
        } else if (angle < 0.75F) {
            return WANING;  // Just before sunrise
        } else if (angle < 0.8125F) {
            return WAXING;  // Just after sunrise
        } else {
            return FULL;    // Morning
        }
    }
    
    public static TimePhase getMoonPhase(IWorld world) {
        float angle = world.func_242415_f(1.0F);
        if (angle < 0.1875F) {
            return FADED;   // Afternoon
        } else if (angle < 0.25F) {
            return WANING;  // Just before sunset
        } else if (angle < 0.3125F) {
            return WAXING;  // Just after sunset
        } else if (angle < 0.6875F) {
            return FULL;    // Night
        } else if (angle < 0.75F) {
            return WAXING;  // Just before sunrise
        } else if (angle < 0.8125F) {
            return WANING;  // Just after sunrise
        } else {
            return FADED;   // Morning
        }
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getString() {
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
