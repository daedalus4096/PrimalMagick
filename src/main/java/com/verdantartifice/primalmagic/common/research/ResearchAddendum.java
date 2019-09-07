package com.verdantartifice.primalmagic.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.util.ResourceLocation;

public class ResearchAddendum {
    protected ResearchEntry researchEntry;
    protected String textTranslationKey;
    protected List<ResourceLocation> recipes = new ArrayList<>();
    protected CompoundResearchKey requiredResearch;
    
    protected ResearchAddendum(@Nonnull ResearchEntry entry, @Nonnull String textTranslationKey) {
        this.researchEntry = entry;
        this.textTranslationKey = textTranslationKey;
    }
    
    @Nullable
    public static ResearchAddendum create(@Nonnull ResearchEntry entry, @Nullable String textTranslationKey) {
        return (textTranslationKey == null) ? null : new ResearchAddendum(entry, textTranslationKey);
    }
    
    @Nonnull
    public static ResearchAddendum parse(@Nonnull ResearchEntry entry, @Nonnull JsonObject obj) throws Exception {
        ResearchAddendum addendum = create(entry, obj.getAsJsonPrimitive("text").getAsString());
        if (addendum == null) {
            throw new Exception("Illegal addendum text in research JSON");
        }
        if (obj.has("recipes")) {
            addendum.recipes = JsonUtils.toResourceLocations(obj.get("recipes").getAsJsonArray());
        }
        if (obj.has("required_research")) {
            addendum.requiredResearch = CompoundResearchKey.parse(obj.get("required_research").getAsJsonArray());
        }
        return addendum;
    }
    
    @Nonnull
    public ResearchEntry getResearchEntry() {
        return this.researchEntry;
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return this.textTranslationKey;
    }
    
    @Nonnull
    public List<ResourceLocation> getRecipes() {
        return Collections.unmodifiableList(this.recipes);
    }
    
    @Nullable
    public CompoundResearchKey getRequiredResearch() {
        return this.requiredResearch;
    }
}
