package com.verdantartifice.primalmagick.common.runes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.StringRepresentable;

/**
 * Enum describing the type of a rune.
 * 
 * @author Daedalus4096
 */
public enum RuneType implements StringRepresentable {
    VERB("verb"),
    NOUN("noun"),
    SOURCE("source"),
    POWER("power");
    
    private final String name;
    
    private RuneType(String str) {
        this.name = str;
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.name;
    }
    
    @Nullable
    public static RuneType fromName(@Nullable String name) {
        for (RuneType rune : values()) {
            if (rune.getSerializedName().equals(name)) {
                return rune;
            }
        }
        return null;
    }
}
