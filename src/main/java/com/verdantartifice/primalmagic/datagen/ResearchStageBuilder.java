package com.verdantartifice.primalmagic.datagen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class ResearchStageBuilder {
    protected final String textTranslationKey;
    protected final List<String> requiredItems = new ArrayList<>();
    protected final List<String> requiredCrafts = new ArrayList<>();
    protected final List<String> requiredKnowledge = new ArrayList<>();
    protected final List<SimpleResearchKey> requiredResearch = new ArrayList<>();
    protected final List<ResourceLocation> recipes = new ArrayList<>();
    protected SourceList attunements;
    
    protected ResearchStageBuilder(@Nonnull String text) {
        this.textTranslationKey = text;
    }
    
    public static ResearchStageBuilder stage(@Nonnull String text) {
        return new ResearchStageBuilder(text);
    }
    
    public ResearchStageBuilder requiredItemStack(@Nonnull IItemProvider item) {
        return requiredItemStack(item, 1);
    }
    
    public ResearchStageBuilder requiredItemStack(@Nonnull IItemProvider item, int count) {
        return requiredItemStack(new ItemStack(item, count));
    }
    
    public ResearchStageBuilder requiredItemStack(@Nonnull ItemStack stack) {
        this.requiredItems.add(this.serializeItemStack(stack));
        return this;
    }
    
    public ResearchStageBuilder requiredItemTag(@Nonnull String namespace, @Nonnull String path) {
        return requiredItemTag(new ResourceLocation(namespace, path));
    }
    
    public ResearchStageBuilder requiredItemTag(@Nonnull ResourceLocation tagLoc) {
        this.requiredItems.add("tag:" + tagLoc.toString());
        return this;
    }
    
    public ResearchStageBuilder requiredCraftStack(@Nonnull IItemProvider item) {
        return requiredCraftStack(item, 1);
    }
    
    public ResearchStageBuilder requiredCraftStack(@Nonnull IItemProvider item, int count) {
        return requiredCraftStack(new ItemStack(item, count));
    }
    
    public ResearchStageBuilder requiredCraftStack(@Nonnull ItemStack stack) {
        this.requiredCrafts.add(this.serializeItemStack(stack));
        return this;
    }
    
    public ResearchStageBuilder requiredCraftTag(@Nonnull ResourceLocation tagLoc) {
        this.requiredCrafts.add("tag:" + tagLoc.toString());
        return this;
    }
    
    protected String serializeItemStack(@Nonnull ItemStack stack) {
        StringBuilder sb = new StringBuilder(stack.getItem().getRegistryName().toString());
        if (stack.getCount() > 1 || stack.hasTag()) {
            sb.append(';');
            sb.append(stack.getCount());
        }
        if (stack.hasTag()) {
            sb.append(';');
            sb.append(stack.getTag().toString().replaceAll("\"", "'"));
        }
        return sb.toString();
    }
    
    public ResearchStageBuilder requiredKnowledge(@Nonnull IPlayerKnowledge.KnowledgeType type, int count) {
        this.requiredKnowledge.add(type.toString() + ";" + count);
        return this;
    }
    
    public ResearchStageBuilder requiredResearch(@Nonnull String keyStr) {
        return requiredResearch(SimpleResearchKey.parse(keyStr));
    }
    
    public ResearchStageBuilder requiredResearch(@Nonnull SimpleResearchKey key) {
        this.requiredResearch.add(key);
        return this;
    }
    
    public ResearchStageBuilder attunement(@Nonnull SourceList sources) {
        this.attunements = sources.copy();
        return this;
    }
    
    public ResearchStageBuilder attunement(@Nonnull Source source, int amount) {
        if (this.attunements == null) {
            this.attunements = new SourceList();
        }
        this.attunements.add(source, amount);
        return this;
    }
    
    public ResearchStageBuilder recipe(@Nonnull String name) {
        return recipe(PrimalMagic.MODID, name);
    }
    
    public ResearchStageBuilder recipe(@Nonnull String modId, @Nonnull String name) {
        return recipe(new ResourceLocation(modId, name));
    }
    
    public ResearchStageBuilder recipe(@Nonnull IItemProvider item) {
        return recipe(item.asItem().getRegistryName());
    }
    
    public ResearchStageBuilder recipe(@Nonnull ResourceLocation loc) {
        this.recipes.add(loc);
        return this;
    }
    
    private void validate() {
        if (this.textTranslationKey == null) {
            throw new IllegalStateException("No text for research stage");
        }
    }
    
    public IFinishedResearchStage build() {
        this.validate();
        return new ResearchStageBuilder.Result(this.textTranslationKey, this.requiredItems, this.requiredCrafts, this.requiredKnowledge, this.requiredResearch, this.recipes, this.attunements);
    }
    
    public static class Result implements IFinishedResearchStage {
        protected final String textTranslationKey;
        protected final List<String> requiredItems;
        protected final List<String> requiredCrafts;
        protected final List<String> requiredKnowledge;
        protected final List<SimpleResearchKey> requiredResearch;
        protected final List<ResourceLocation> recipes;
        protected final SourceList attunements;
        
        public Result(@Nonnull String text, @Nonnull List<String> requiredItems, @Nonnull List<String> requiredCrafts, @Nonnull List<String> requiredKnowledge, @Nonnull List<SimpleResearchKey> requiredResearch, @Nonnull List<ResourceLocation> recipes, @Nullable SourceList attunements) {
            this.textTranslationKey = text;
            this.requiredItems = requiredItems;
            this.requiredCrafts = requiredCrafts;
            this.requiredKnowledge = requiredKnowledge;
            this.requiredResearch = requiredResearch;
            this.recipes = recipes;
            this.attunements = attunements;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("text", this.textTranslationKey);
            
            if (!this.requiredItems.isEmpty()) {
                JsonArray itemArray = new JsonArray();
                for (String itemStr : this.requiredItems) {
                    itemArray.add(itemStr);
                }
                json.add("required_item", itemArray);
            }
            
            if (!this.requiredCrafts.isEmpty()) {
                JsonArray craftArray = new JsonArray();
                for (String craftStr : this.requiredCrafts) {
                    craftArray.add(craftStr);
                }
                json.add("required_craft", craftArray);
            }
            
            if (!this.requiredKnowledge.isEmpty()) {
                JsonArray knowledgeArray = new JsonArray();
                for (String knowledgeStr : this.requiredKnowledge) {
                    knowledgeArray.add(knowledgeStr);
                }
                json.add("required_knowledge", knowledgeArray);
            }
            
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
