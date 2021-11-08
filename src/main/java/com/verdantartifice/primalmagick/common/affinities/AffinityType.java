package com.verdantartifice.primalmagick.common.affinities;

import net.minecraft.util.StringRepresentable;

/**
 * Type of affinity entry.
 * 
 * @author Daedalus4096
 */
public enum AffinityType implements StringRepresentable {
    ITEM("item", "items"),
    POTION_BONUS("potion_bonus", "potions"),
    ENCHANTMENT_BONUS("enchantment_bonus", "enchantments"),
    ENTITY_TYPE("entity_type", "entity_types");
    
    private final String name;
    private final String folder;
    
    private AffinityType(String name, String folder) {
        this.name = name;
        this.folder = folder;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
    
    public String getFolder() {
        return this.folder;
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
