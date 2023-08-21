package com.verdantartifice.primalmagick.common.research;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public enum KnowledgeType implements StringRepresentable {
    OBSERVATION("observation", 16, PrimalMagick.resource("textures/research/knowledge_observation.png")),
    THEORY("theory", 32, PrimalMagick.resource("textures/research/knowledge_theory.png"));
    
    private final String name;
    private final short progression;  // How many points make a complete level for this knowledge type
    private final ResourceLocation iconLocation;
    
    private KnowledgeType(String name, int progression, @Nonnull ResourceLocation iconLocation) {
        this.name = name;
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
        return String.join(".", "knowledge_type", PrimalMagick.MODID, this.getSerializedName());
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
    
    @Nullable
    public static KnowledgeType fromName(@Nullable String name) {
        for (KnowledgeType knowledgeType : values()) {
            if (knowledgeType.getSerializedName().equals(name)) {
                return knowledgeType;
            }
        }
        return null;
    }
}