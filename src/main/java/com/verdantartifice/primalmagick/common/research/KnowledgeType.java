package com.verdantartifice.primalmagick.common.research;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;

public enum KnowledgeType {
    OBSERVATION(16, PrimalMagick.resource("textures/research/knowledge_observation.png")),
    THEORY(32, PrimalMagick.resource("textures/research/knowledge_theory.png"));
    
    private short progression;  // How many points make a complete level for this knowledge type
    private ResourceLocation iconLocation;
    
    private KnowledgeType(int progression, @Nonnull ResourceLocation iconLocation) {
        this.progression = (short)progression;
        this.iconLocation = iconLocation;
    }
    
    public int getProgression() {
        return this.progression;
    }
    
    @Nonnull
    public ResourceLocation getIconLocation() {
        return this.iconLocation;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return String.join(".", "knowledge_type", PrimalMagick.MODID, this.name());
    }
}