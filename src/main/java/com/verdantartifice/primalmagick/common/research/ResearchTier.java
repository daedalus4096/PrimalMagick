package com.verdantartifice.primalmagick.common.research;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.util.StringRepresentable;

public enum ResearchTier implements StringRepresentable {
    BASIC("basic", 1, 4, Optional.empty()),
    EXPERT("expert", 5, 20, Optional.of(IconDefinition.of(PrimalMagick.resource("textures/research/expertise_expert.png")))),
    MASTER("master", 25, 100, Optional.of(IconDefinition.of(PrimalMagick.resource("textures/research/expertise_master.png")))),
    SUPREME("supreme", 125, 500, Optional.of(IconDefinition.of(PrimalMagick.resource("textures/research/expertise_supreme.png"))));
    
    public static final Codec<ResearchTier> CODEC = StringRepresentable.fromEnum(ResearchTier::values);
    
    private final String name;
    private final int defaultExpertise;
    private final int defaultBonusExpertise;
    private final Optional<IconDefinition> iconDef;
    
    private ResearchTier(String name, int defaultExp, int defaultBonusExp, Optional<IconDefinition> iconDef) {
        this.name = name;
        this.defaultExpertise = defaultExp;
        this.defaultBonusExpertise = defaultBonusExp;
        this.iconDef = iconDef;
    }
    
    public int getDefaultExpertise() {
        return this.defaultExpertise;
    }
    
    public int getDefaultBonusExpertise() {
        return this.defaultBonusExpertise;
    }
    
    public Optional<IconDefinition> getIconDefinition() {
        return this.iconDef;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
