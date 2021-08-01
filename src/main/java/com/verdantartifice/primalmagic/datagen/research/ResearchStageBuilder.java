package com.verdantartifice.primalmagic.datagen.research;

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
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ResearchStageBuilder {
    protected final String modId;
    protected final List<String> requiredItems = new ArrayList<>();
    protected final List<String> requiredCrafts = new ArrayList<>();
    protected final List<String> requiredKnowledge = new ArrayList<>();
    protected final List<SimpleResearchKey> requiredResearch = new ArrayList<>();
    protected final List<ResourceLocation> recipes = new ArrayList<>();
    protected SourceList attunements;
    
    protected ResearchStageBuilder(@Nonnull String modId) {
        this.modId = modId;
    }
    
    public static ResearchStageBuilder stage(@Nonnull String modId) {
        return new ResearchStageBuilder(modId);
    }
    
    public static ResearchStageBuilder stage() {
        return new ResearchStageBuilder(PrimalMagic.MODID);
    }
    
    public ResearchStageBuilder requiredItemStack(@Nonnull ItemLike item) {
        return requiredItemStack(item, 1);
    }
    
    public ResearchStageBuilder requiredItemStack(@Nonnull ItemLike item, int count) {
        return requiredItemStack(new ItemStack(item, count));
    }
    
    public ResearchStageBuilder requiredItemStack(@Nonnull ItemStack stack) {
        this.requiredItems.add(ItemUtils.serializeItemStack(stack));
        return this;
    }
    
    public ResearchStageBuilder requiredItemTag(@Nonnull String namespace, @Nonnull String path) {
        return requiredItemTag(new ResourceLocation(namespace, path));
    }
    
    public ResearchStageBuilder requiredItemTag(@Nonnull ResourceLocation tagLoc) {
        this.requiredItems.add("tag:" + tagLoc.toString());
        return this;
    }
    
    public ResearchStageBuilder requiredCraftStack(@Nonnull ItemLike item) {
        return requiredCraftStack(item, 1);
    }
    
    public ResearchStageBuilder requiredCraftStack(@Nonnull ItemLike item, int count) {
        return requiredCraftStack(new ItemStack(item, count));
    }
    
    public ResearchStageBuilder requiredCraftStack(@Nonnull ItemStack stack) {
        this.requiredCrafts.add(ItemUtils.serializeItemStack(stack));
        return this;
    }
    
    public ResearchStageBuilder requiredCraftTag(@Nonnull ResourceLocation tagLoc) {
        this.requiredCrafts.add("tag:" + tagLoc.toString());
        return this;
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
    
    public ResearchStageBuilder recipe(@Nonnull ItemLike item) {
        return recipe(item.asItem().getRegistryName());
    }
    
    public ResearchStageBuilder recipe(@Nonnull ResourceLocation loc) {
        this.recipes.add(loc);
        return this;
    }
    
    private void validate() {
        if (this.modId == null) {
            throw new IllegalStateException("No mod ID for research stage");
        }
    }
    
    public IFinishedResearchStage build() {
        this.validate();
        return new ResearchStageBuilder.Result(this.modId, this.requiredItems, this.requiredCrafts, this.requiredKnowledge, this.requiredResearch, this.recipes, this.attunements);
    }
    
    public static class Result implements IFinishedResearchStage {
        protected final String modId;
        protected final List<String> requiredItems;
        protected final List<String> requiredCrafts;
        protected final List<String> requiredKnowledge;
        protected final List<SimpleResearchKey> requiredResearch;
        protected final List<ResourceLocation> recipes;
        protected final SourceList attunements;
        protected String entryKey;
        protected int stageIndex;
        
        public Result(@Nonnull String modId, @Nonnull List<String> requiredItems, @Nonnull List<String> requiredCrafts, @Nonnull List<String> requiredKnowledge, @Nonnull List<SimpleResearchKey> requiredResearch, @Nonnull List<ResourceLocation> recipes, @Nullable SourceList attunements) {
            this.modId = modId;
            this.requiredItems = requiredItems;
            this.requiredCrafts = requiredCrafts;
            this.requiredKnowledge = requiredKnowledge;
            this.requiredResearch = requiredResearch;
            this.recipes = recipes;
            this.attunements = attunements;
        }

        @Override
        public IFinishedResearchStage setEntryKey(String key) {
            this.entryKey = key;
            return this;
        }
        
        @Override
        public IFinishedResearchStage setStageIndex(int index) {
            this.stageIndex = index;
            return this;
        }

        private String getTextTranslationKey() {
            return this.modId.toLowerCase() + ".research." + this.entryKey.toLowerCase() + ".text.stage." + this.stageIndex;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("text", this.getTextTranslationKey());
            
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
