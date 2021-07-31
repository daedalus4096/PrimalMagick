package com.verdantartifice.primalmagic.common.rituals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.StringRepresentable;

/**
 * Enum describing a type of ritual step.
 * 
 * @author Daedalus4096
 */
public enum RitualStepType implements StringRepresentable {
    OFFERING("offering"),
    PROP("prop");
    
    private final String name;
    
    private RitualStepType(String str) {
        this.name = str;
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.name;
    }
    
    @Nullable
    public static RitualStepType fromName(@Nullable String name) {
        for (RitualStepType step : values()) {
            if (step.getSerializedName().equals(name)) {
                return step;
            }
        }
        return null;
    }
}
