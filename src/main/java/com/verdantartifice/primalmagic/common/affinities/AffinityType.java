package com.verdantartifice.primalmagic.common.affinities;

import net.minecraft.util.IStringSerializable;

/**
 * Type of affinity entry.
 * 
 * @author Daedalus4096
 */
public enum AffinityType implements IStringSerializable {
    ITEM("item"),
    POTION_BONUS("potion_bonus"),
    ENCHANTMENT_BONUS("enchantment_bonus");
    
    private final String name;
    
    private AffinityType(String name) {
        this.name = name;
    }

    @Override
    public String getString() {
        return this.name;
    }
    
    public static AffinityType parse(String str) {
        for (AffinityType type : AffinityType.values()) {
            if (type.name.equals(str)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown affinity type " + str);
    }
}
