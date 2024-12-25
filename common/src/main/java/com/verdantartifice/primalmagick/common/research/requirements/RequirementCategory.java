package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nullable;

public enum RequirementCategory implements StringRepresentable {
    MUST_OBTAIN("must_obtain"),
    MUST_CRAFT("must_craft"),
    KNOWLEDGE("knowledge"),
    RESEARCH("research"),
    STAT("stat"),
    COMPOUND("compound");
    
    public static final Codec<RequirementCategory> CODEC = StringRepresentable.fromValues(RequirementCategory::values);
    
    private final String name;
    
    private RequirementCategory(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
    
    @Nullable
    public static RequirementCategory fromName(@Nullable String name) {
        for (RequirementCategory val : values()) {
            if (val.getSerializedName().equals(name)) {
                return val;
            }
        }
        return null;
    }
}
