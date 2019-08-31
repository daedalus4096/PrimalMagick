package com.verdantartifice.primalmagic.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.util.ItemUtils;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ResearchStage {
    protected String textTranslationKey;
    protected List<ResourceLocation> recipes = new ArrayList<>();
    protected List<Object> mustObtain = new ArrayList<>();
    protected List<Object> mustCraft = new ArrayList<>();
    protected List<Integer> craftReference = new ArrayList<>();
    protected List<Knowledge> requiredKnowledge = new ArrayList<>();
    protected CompoundResearchKey requiredResearch;
    
    protected ResearchStage(@Nonnull String textTranslationKey) {
        this.textTranslationKey = textTranslationKey;
    }
    
    @Nullable
    public static ResearchStage create(@Nullable String textTranslationKey) {
        return (textTranslationKey == null) ? null : new ResearchStage(textTranslationKey);
    }
    
    @Nonnull
    public static ResearchStage parse(@Nonnull JsonObject obj) throws Exception {
        ResearchStage stage = create(obj.getAsJsonPrimitive("text").getAsString());
        if (stage == null) {
            throw new Exception("Illegal stage text in research JSON");
        }
        if (obj.has("recipes")) {
            stage.recipes = JsonUtils.toResourceLocations(obj.get("recipes").getAsJsonArray());
        }
        if (obj.has("required_item")) {
            stage.mustObtain = JsonUtils.toOres(obj.get("required_item").getAsJsonArray());
        }
        if (obj.has("required_craft")) {
            stage.mustCraft = JsonUtils.toOres(obj.get("required_craft").getAsJsonArray());
            List<Integer> references = new ArrayList<>();
            for (Object craftObj : stage.mustCraft) {
                int code = (craftObj instanceof ItemStack) ? 
                        ItemUtils.getHashCode((ItemStack)craftObj) :
                        ("oredict:" + craftObj.toString()).hashCode();
                references.add(Integer.valueOf(code));
                ResearchManager.addCraftingReference(code);
            }
            stage.craftReference = references;
        }
        if (obj.has("required_knowledge")) {
            List<String> knowledgeStrs = JsonUtils.toStrings(obj.get("required_knowledge").getAsJsonArray());
            stage.requiredKnowledge = knowledgeStrs.stream()
                                        .map(s -> Knowledge.parse(s))
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList());
        }
        if (obj.has("required_research")) {
            stage.requiredResearch = CompoundResearchKey.parse(obj.get("required_research").getAsJsonArray());
        }
        return stage;
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return this.textTranslationKey;
    }
    
    @Nonnull
    public List<ResourceLocation> getRecipes() {
        return Collections.unmodifiableList(this.recipes);
    }
    
    @Nonnull
    public List<Object> getMustObtain() {
        return Collections.unmodifiableList(this.mustObtain);
    }
    
    @Nonnull
    public List<Object> getMustCraft() {
        return Collections.unmodifiableList(this.mustCraft);
    }
    
    @Nonnull
    public List<Integer> getCraftReference() {
        return Collections.unmodifiableList(this.craftReference);
    }
    
    @Nonnull
    public List<Knowledge> getRequiredKnowledge() {
        return Collections.unmodifiableList(this.requiredKnowledge);
    }
    
    @Nullable
    public CompoundResearchKey getRequiredResearch() {
        return this.requiredResearch;
    }
}
