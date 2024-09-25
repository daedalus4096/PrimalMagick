package com.verdantartifice.primalmagick.common.misc;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

/**
 * Enum describing the tier of a device.  Typically a tiered device will have a tier corresponding
 * to its research level (basic research == basic tier, expert research == enchanted tier, etc).
 * 
 * @author Daedalus4096
 */
public enum DeviceTier implements StringRepresentable {
    BASIC("basic"),
    ENCHANTED("enchanted"),
    FORBIDDEN("forbidden"),
    HEAVENLY("heavenly"),
    CREATIVE("creative");
    
    public static final Codec<DeviceTier> CODEC = StringRepresentable.fromEnum(DeviceTier::values);
    
    private final String name;
    
    private DeviceTier(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
