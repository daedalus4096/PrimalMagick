package com.verdantartifice.primalmagic.common.concoctions;

import javax.annotation.Nullable;

import net.minecraft.util.IStringSerializable;

/**
 * Definition of a type of alchemical bomb fuse.  Determines how long it takes for the bomb to go off.
 * 
 * @author Daedalus4096
 */
public enum FuseType implements IStringSerializable {
    IMPACT(-1, "impact"),
    SHORT(20, "short"),
    MEDIUM(60, "medium"),
    LONG(100, "long");
    
    private final int fuseLength;
    private final String tag;
    
    private FuseType(int fuseLength, String tag) {
        this.fuseLength = fuseLength;
        this.tag = tag;
    }
    
    public int getFuseLength() {
        return this.fuseLength;
    }

    @Override
    public String getString() {
        return this.tag;
    }

    public boolean hasTimer() {
        return this.fuseLength > 0;
    }
    
    @Nullable
    public FuseType getNext() {
        switch(this) {
        case IMPACT:
            return SHORT;
        case SHORT:
            return MEDIUM;
        case MEDIUM:
            return LONG;
        case LONG:
            return IMPACT;
        default:
            return null;
        }
    }
    
    public String getTranslationKey() {
        return "concoctions.primalmagic.fuse." + this.tag;
    }
    
    @Nullable
    public static FuseType fromName(@Nullable String name) {
        for (FuseType type : FuseType.values()) {
            if (type.getString().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
