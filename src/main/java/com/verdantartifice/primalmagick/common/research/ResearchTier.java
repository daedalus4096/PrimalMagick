package com.verdantartifice.primalmagick.common.research;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

public enum ResearchTier implements StringRepresentable {
    BASIC("basic"),
    EXPERT("expert"),
    MASTER("master"),
    SUPREME("supreme");
    
    public static final Codec<ResearchTier> CODEC = StringRepresentable.fromEnum(ResearchTier::values);
    
    private final String name;
    
    private ResearchTier(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
