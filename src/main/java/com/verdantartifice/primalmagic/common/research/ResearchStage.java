package com.verdantartifice.primalmagic.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.util.InventoryUtils;
import com.verdantartifice.primalmagic.common.util.ItemUtils;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ResearchStage {
    protected ResearchEntry researchEntry;
    protected String textTranslationKey;
    protected List<ResourceLocation> recipes = new ArrayList<>();
    protected List<Object> mustObtain = new ArrayList<>();  // Either a specific ItemStack or a tag ResourceLocation
    protected List<Object> mustCraft = new ArrayList<>();   // Either a specific ItemStack or a tag ResourceLocation
    protected List<Integer> craftReference = new ArrayList<>();
    protected List<Knowledge> requiredKnowledge = new ArrayList<>();
    protected CompoundResearchKey requiredResearch;
    
    protected ResearchStage(@Nonnull ResearchEntry entry, @Nonnull String textTranslationKey) {
        this.researchEntry = entry;
        this.textTranslationKey = textTranslationKey;
    }
    
    @Nullable
    public static ResearchStage create(@Nonnull ResearchEntry entry, @Nullable String textTranslationKey) {
        return (textTranslationKey == null) ? null : new ResearchStage(entry, textTranslationKey);
    }
    
    @Nonnull
    public static ResearchStage parse(@Nonnull ResearchEntry entry, @Nonnull JsonObject obj) throws Exception {
        ResearchStage stage = create(entry, obj.getAsJsonPrimitive("text").getAsString());
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
                        ("tag:" + craftObj.toString()).hashCode();
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
    
    public boolean hasPrerequisites() {
        return !this.mustObtain.isEmpty() || !this.mustCraft.isEmpty() || !this.requiredKnowledge.isEmpty() || this.requiredResearch != null;
    }
    
    public boolean arePrerequisitesMet(@Nullable PlayerEntity player) {
        if (player == null) {
            return false;
        }
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        }
        
        // Check if player is carrying must-obtain items
        for (Object obtainObj : this.mustObtain) {
            if (obtainObj instanceof ItemStack && !InventoryUtils.isPlayerCarrying(player, (ItemStack)obtainObj)) {
                return false;
            } else if (obtainObj instanceof ResourceLocation && !InventoryUtils.isPlayerCarrying(player, (ResourceLocation)obtainObj)) {
                return false;
            }
        }
        
        // Check if player knows research for must-craft items
        for (Integer craftRef : this.craftReference) {
            if (!knowledge.isResearchKnown(SimpleResearchKey.parse("#" + craftRef.intValue()))) {
                return false;
            }
        }
        
        // Check if player has required knowledge
        for (Knowledge knowPacket : this.requiredKnowledge) {
            if (knowledge.getKnowledge(knowPacket.getType(), knowPacket.getDiscipline()) < knowPacket.getAmount()) {
                return false;
            }
        }
        
        // Check if player has required research
        if (this.requiredResearch != null && !this.requiredResearch.isKnownByStrict(player)) {
            return false;
        }
        
        return true;
    }
}
