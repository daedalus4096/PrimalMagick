package com.verdantartifice.primalmagic.common.concoctions;

import javax.annotation.Nullable;

import net.minecraft.util.IStringSerializable;

/**
 * Definition of a type of alchemical concoction.  Determines the maximum dosage of the vial.
 * 
 * @author Daedalus4096
 */
public enum ConcoctionType implements IStringSerializable {
    WATER(1, "water"),
    TINCTURE(3, "tincture"),
    PHILTER(6, "philter"),
    ELIXIR(9, "elixir"),
    BOMB(6, "bomb");
    
    private final int maxDoses;
    private final String tag;
    
    private ConcoctionType(int maxDoses, String tag) {
        this.maxDoses = maxDoses;
        this.tag = tag;
    }
    
    public int getMaxDoses() {
        return this.maxDoses;
    }

    @Override
    public String getString() {
        return this.tag;
    }
    
    public boolean hasDrinkablePotion() {
        return this == TINCTURE || this == PHILTER || this == ELIXIR;
    }
    
    @Nullable
    public static ConcoctionType fromName(@Nullable String name) {
        for (ConcoctionType type : ConcoctionType.values()) {
            if (type.getString().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
