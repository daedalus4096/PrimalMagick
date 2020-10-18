package com.verdantartifice.primalmagic.common.rituals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.IStringSerializable;

/**
 * Enum describing a type of ritual step.
 * 
 * @author Daedalus4096
 */
public enum RitualStepType implements IStringSerializable {
    OFFERING("offering"),
    PROP("prop");
    
    private final String name;
    
    private RitualStepType(String str) {
        this.name = str;
    }

    @Override
    @Nonnull
    public String getString() {
        return this.name;
    }
    
    @Nullable
    public static RitualStepType fromName(@Nullable String name) {
        for (RitualStepType step : values()) {
            if (step.getString().equals(name)) {
                return step;
            }
        }
        return null;
    }
}
