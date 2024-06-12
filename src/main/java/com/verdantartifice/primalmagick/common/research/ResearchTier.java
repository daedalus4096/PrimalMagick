package com.verdantartifice.primalmagick.common.research;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

public enum ResearchTier implements StringRepresentable {
    BASIC("basic", 1, 4),
    EXPERT("expert", 5, 20),
    MASTER("master", 25, 100),
    SUPREME("supreme", 125, 500);
    
    public static final Codec<ResearchTier> CODEC = StringRepresentable.fromEnum(ResearchTier::values);
    
    private final String name;
    private final int defaultExpertise;
    private final int defaultBonusExpertise;
    
    private ResearchTier(String name, int defaultExp, int defaultBonusExp) {
        this.name = name;
        this.defaultExpertise = defaultExp;
        this.defaultBonusExpertise = defaultBonusExp;
    }
    
    public int getDefaultExpertise() {
        return this.defaultExpertise;
    }
    
    public int getDefaultBonusExpertise() {
        return this.defaultBonusExpertise;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
