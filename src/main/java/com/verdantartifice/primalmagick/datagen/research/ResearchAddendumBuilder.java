package com.verdantartifice.primalmagick.datagen.research;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchName;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class ResearchAddendumBuilder {
    protected final String modId;
    protected final List<SimpleResearchKey> requiredResearch = new ArrayList<>();
    protected final List<ResourceLocation> recipes = new ArrayList<>();
    protected final List<SimpleResearchKey> siblings = new ArrayList<>();
    protected final SourceList.Builder attunements = SourceList.builder();

    protected ResearchAddendumBuilder(@Nonnull String modId) {
        this.modId = modId;
    }
    
    public static ResearchAddendumBuilder addendum(@Nonnull String modId) {
        return new ResearchAddendumBuilder(modId);
    }
    
    public static ResearchAddendumBuilder addendum() {
        return new ResearchAddendumBuilder(PrimalMagick.MODID);
    }
    
    public ResearchAddendumBuilder requiredResearch(@Nonnull String keyStr) {
        return requiredResearch(ResearchNames.find(keyStr).orElseThrow());
    }
    
    public ResearchAddendumBuilder requiredResearch(@Nonnull ResearchName key) {
        return requiredResearch(key.simpleKey());
    }
    
    public ResearchAddendumBuilder requiredResearch(@Nonnull SimpleResearchKey key) {
        this.requiredResearch.add(key);
        return this;
    }
    
    public ResearchAddendumBuilder attunement(@Nonnull SourceList sources) {
        this.attunements.with(sources);
        return this;
    }
    
    public ResearchAddendumBuilder attunement(@Nonnull Source source, int amount) {
        this.attunements.with(source, amount);
        return this;
    }
    
    public ResearchAddendumBuilder recipe(@Nonnull String name) {
        return recipe(PrimalMagick.MODID, name);
    }
    
    public ResearchAddendumBuilder recipe(@Nonnull String modId, @Nonnull String name) {
        return recipe(new ResourceLocation(modId, name));
    }
    
    public ResearchAddendumBuilder recipe(@Nonnull ItemLike item) {
        return recipe(ForgeRegistries.ITEMS.getKey(item.asItem()));
    }
    
    public ResearchAddendumBuilder recipe(@Nonnull ResourceLocation loc) {
        this.recipes.add(loc);
        return this;
    }
    
    public ResearchAddendumBuilder sibling(@Nonnull String keyStr) {
        return sibling(ResearchNames.find(keyStr).orElseThrow());
    }
    
    public ResearchAddendumBuilder sibling(@Nonnull ResearchName key) {
        return sibling(key.simpleKey());
    }
    
    public ResearchAddendumBuilder sibling(@Nonnull SimpleResearchKey key) {
        this.siblings.add(key);
        return this;
    }
    
    private void validate() {
        if (this.modId == null) {
            throw new IllegalStateException("No mod ID for research addendum");
        }
    }
    
    public IFinishedResearchAddendum build() {
        this.validate();
        return new ResearchAddendumBuilder.Result(this.modId, this.requiredResearch, this.recipes, this.siblings, this.attunements.build());
    }
    
    public static class Result implements IFinishedResearchAddendum {
        protected final String modId;
        protected final List<SimpleResearchKey> requiredResearch;
        protected final List<ResourceLocation> recipes;
        protected final List<SimpleResearchKey> siblings;
        protected final SourceList attunements;
        protected String entryKey;
        protected int stageIndex;

        public Result(@Nonnull String modId, @Nonnull List<SimpleResearchKey> requiredResearch, @Nonnull List<ResourceLocation> recipes, @Nonnull List<SimpleResearchKey> siblings, 
                @Nullable SourceList attunements) {
            this.modId = modId;
            this.requiredResearch = requiredResearch;
            this.recipes = recipes;
            this.siblings = siblings;
            this.attunements = attunements;
        }

        @Override
        public IFinishedResearchAddendum setEntryKey(String key) {
            this.entryKey = key;
            return this;
        }
        
        @Override
        public IFinishedResearchAddendum setAddendumIndex(int index) {
            this.stageIndex = index;
            return this;
        }

        private String getTextTranslationKey() {
            return String.join(".", "research", this.modId.toLowerCase(), this.entryKey.toLowerCase(), "text", "addenda", Integer.toString(this.stageIndex));
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("text", this.getTextTranslationKey());
            
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
            
            if (!this.siblings.isEmpty()) {
                JsonArray siblingArray = new JsonArray();
                for (SimpleResearchKey key : this.siblings) {
                    siblingArray.add(key.toString());
                }
                json.add("siblings", siblingArray);
            }
        }
    }
}
