package com.verdantartifice.primalmagick.common.blockstates.properties;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.NotNull;

/**
 * Representation of the current phase of a block that phases in and out over time.
 * 
 * @author Daedalus4096
 */
public enum TimePhase implements StringRepresentable {
    FULL("full", 10),        // The block is fully phased in; use full light
    WAXING("waxing", 5),    // The block is mostly phased in; half light
    WANING("waning", 0),    // The block is mostly phased out; no light
    FADED("faded", 0);        // The block is fully phased out; no light
    
    private final String name;
    private final int light;
    
    TimePhase(String name, int light) {
        this.name = name;
        this.light = light;
    }
    
    public static TimePhase getSunPhase(@NotNull LevelReader world, @NotNull BlockPos pos) {
        return getPhaseFromAngle(world.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, pos));
    }
    
    public static TimePhase getMoonPhase(@NotNull LevelReader world, @NotNull BlockPos pos) {
        return getPhaseFromAngle(world.environmentAttributes().getValue(EnvironmentAttributes.MOON_ANGLE, pos));
    }

    private static TimePhase getPhaseFromAngle(float angle) {
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
    
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }
    
    public int getLightLevel() {
        return this.light;
    }
}
