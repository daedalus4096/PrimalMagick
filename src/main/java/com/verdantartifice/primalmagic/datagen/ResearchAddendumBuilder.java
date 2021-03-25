package com.verdantartifice.primalmagic.datagen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.util.ResourceLocation;

public class ResearchAddendumBuilder {
    protected final String textTranslationKey;
    protected final List<SimpleResearchKey> requiredResearch = new ArrayList<>();
    protected final List<ResourceLocation> recipes = new ArrayList<>();
    protected SourceList attunements;

    protected ResearchAddendumBuilder(@Nonnull String text) {
        this.textTranslationKey = text;
    }
    
    public static ResearchAddendumBuilder addendum(@Nonnull String text) {
        return new ResearchAddendumBuilder(text);
    }
    
    public ResearchAddendumBuilder requiredResearch(@Nonnull String keyStr) {
        return requiredResearch(SimpleResearchKey.parse(keyStr));
    }
    
    public ResearchAddendumBuilder requiredResearch(@Nonnull SimpleResearchKey key) {
        this.requiredResearch.add(key);
        return this;
    }
    
    public ResearchAddendumBuilder attunement(@Nonnull SourceList sources) {
        this.attunements = sources.copy();
        return this;
    }
    
    public ResearchAddendumBuilder attunement(@Nonnull Source source, int amount) {
        if (this.attunements == null) {
            this.attunements = new SourceList();
        }
        this.attunements.add(source, amount);
        return this;
    }
    
    public ResearchAddendumBuilder recipe(@Nonnull String name) {
        return recipe(PrimalMagic.MODID, name);
    }
    
    public ResearchAddendumBuilder recipe(@Nonnull String modId, @Nonnull String name) {
        return recipe(new ResourceLocation(modId, name));
    }
    
    public ResearchAddendumBuilder recipe(@Nonnull ResourceLocation loc) {
        this.recipes.add(loc);
        return this;
    }
    
    private void validate() {
        if (this.textTranslationKey == null) {
            throw new IllegalStateException("No text for research addendum");
        }
    }
    
    public IFinishedResearchAddendum build() {
        this.validate();
        return new ResearchAddendumBuilder.Result(this.textTranslationKey, this.requiredResearch, this.recipes, this.attunements);
    }
    
    public static class Result implements IFinishedResearchAddendum {
        protected final String textTranslationKey;
        protected final List<SimpleResearchKey> requiredResearch;
        protected final List<ResourceLocation> recipes;
        protected final SourceList attunements;

        public Result(@Nonnull String text, @Nonnull List<SimpleResearchKey> requiredResearch, @Nonnull List<ResourceLocation> recipes, @Nullable SourceList attunements) {
            this.textTranslationKey = text;
            this.requiredResearch = requiredResearch;
            this.recipes = recipes;
            this.attunements = attunements;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("text", this.textTranslationKey);
            
            if (!this.requiredResearch.isEmpty()) {
                JsonArray researchArray = new JsonArray();
                for (SimpleResearchKey key : this.requiredResearch) {
                    researchArray.add(key.toString());
                }
                json.add("required_research", researchArray);
            }
            
            if (this.attunements != null && !this.attunements.isEmpty()) {
                json.add("attunements", this.attunements.serializeJson());
            }
            
            if (!this.recipes.isEmpty()) {
                JsonArray recipeArray = new JsonArray();
                for (ResourceLocation recipe : this.recipes) {
                    recipeArray.add(recipe.toString());
                }
                json.add("recipes", recipeArray);
            }
        }
    }
}
