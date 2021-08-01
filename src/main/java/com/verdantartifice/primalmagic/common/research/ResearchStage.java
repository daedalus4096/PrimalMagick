package com.verdantartifice.primalmagic.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.InventoryUtils;
import com.verdantartifice.primalmagic.common.util.ItemUtils;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of a research stage, a portion of a research entry.  A research stage contains text to be
 * displayed in the grimoire, an optional list of granted recipes, an optional list of attunement 
 * sources, and an optional set of completion requirements.  A player must satisfy those requirements in
 * order to complete a stage and progress to the next one in the entry.  In the most common case, a 
 * research entry contains two stages: one with a set of requirements, and a second that grants recipes.
 * 
 * @author Daedalus4096
 */
public class ResearchStage {
    protected ResearchEntry researchEntry;
    protected String textTranslationKey;
    protected List<ResourceLocation> recipes = new ArrayList<>();
    protected List<Object> mustObtain = new ArrayList<>();  // Either a specific ItemStack or a tag ResourceLocation
    protected List<Object> mustCraft = new ArrayList<>();   // Either a specific ItemStack or a tag ResourceLocation
    protected List<Integer> craftReference = new ArrayList<>();
    protected List<Knowledge> requiredKnowledge = new ArrayList<>();
    protected CompoundResearchKey requiredResearch;
    protected SourceList attunements = new SourceList();
    
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
        // Parse a research entry from a research definition file
        ResearchStage stage = create(entry, obj.getAsJsonPrimitive("text").getAsString());
        if (stage == null) {
            throw new JsonParseException("Illegal stage text in research JSON");
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
                // For each item or tag that must be crafted, compute its hash code and store it for future comparison 
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
        if (obj.has("attunements")) {
            stage.attunements = JsonUtils.toSourceList(obj.get("attunements").getAsJsonObject());
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
    
    @Nonnull
    public SourceList getAttunements() {
        return this.attunements;
    }
    
    public boolean hasPrerequisites() {
        return !this.mustObtain.isEmpty() || !this.mustCraft.isEmpty() || !this.requiredKnowledge.isEmpty() || this.requiredResearch != null;
    }
    
    public boolean arePrerequisitesMet(@Nullable Player player) {
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
            } else if (obtainObj instanceof ResourceLocation && !InventoryUtils.isPlayerCarrying(player, (ResourceLocation)obtainObj, 1)) {
                return false;
            }
        }
        
        // Check if player knows research for must-craft items
        for (Integer craftRef : this.craftReference) {
            if (!knowledge.isResearchKnown(SimpleResearchKey.parseCrafted(craftRef))) {
                return false;
            }
        }
        
        // Check if player has required knowledge
        for (Knowledge knowPacket : this.requiredKnowledge) {
            if (knowledge.getKnowledge(knowPacket.getType()) < knowPacket.getAmount()) {
                return false;
            }
        }
        
        // Check if player has required research
        if (this.requiredResearch != null && !this.requiredResearch.isKnownByStrict(player)) {
            return false;
        }
        
        return true;
    }
    
    public List<Boolean> getObtainRequirementCompletion(@Nullable Player player) {
        if (this.mustObtain.isEmpty()) {
            return Collections.emptyList();
        }
        if (player == null) {
            // If the player is invalid, return false for all requirements
            return Collections.nCopies(this.mustObtain.size(), Boolean.FALSE);
        }
        
        List<Boolean> retVal = new ArrayList<>();
        for (Object obj : this.mustObtain) {
            if (obj instanceof ItemStack) {
                // If the obtain requirement is a specific itemstack, check if the player is carrying some
                retVal.add(Boolean.valueOf(InventoryUtils.isPlayerCarrying(player, (ItemStack)obj)));
            } else if (obj instanceof ResourceLocation) {
                // If the obtain requirement is a tag, check if the player is carrying one of any of the tag's contents
                retVal.add(Boolean.valueOf(InventoryUtils.isPlayerCarrying(player, (ResourceLocation)obj, 1)));
            } else {
                // If the obtain requirement is invalid, just assume the player has it
                retVal.add(Boolean.TRUE);
            }
        }
        return retVal;
    }
    
    public List<Boolean> getCraftRequirementCompletion(@Nullable Player player) {
        if (this.craftReference.isEmpty()) {
            return Collections.emptyList();
        }
        if (player == null) {
            // If the player is invalid, return false for all requirements
            return Collections.nCopies(this.craftReference.size(), Boolean.FALSE);
        }
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            // If the player's knowledge capability is invalid, return false for all requirements
            return Collections.nCopies(this.craftReference.size(), Boolean.FALSE);
        }
        
        List<Boolean> retVal = new ArrayList<>();
        for (Integer craftRef : this.craftReference) {
            // Check if the player knows the special research entry corresponding to the required hash code
            retVal.add(Boolean.valueOf(knowledge.isResearchKnown(SimpleResearchKey.parseCrafted(craftRef))));
        }
        return retVal;
    }
    
    public List<Boolean> getKnowledgeRequirementCompletion(@Nullable Player player) {
        if (this.requiredKnowledge.isEmpty()) {
            return Collections.emptyList();
        }
        if (player == null) {
            // If the player is invalid, return false for all requirements
            return Collections.nCopies(this.requiredKnowledge.size(), Boolean.FALSE);
        }
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            // If the player's knowledge capability is invalid, return false for all requirements
            return Collections.nCopies(this.requiredKnowledge.size(), Boolean.FALSE);
        }
        
        List<Boolean> retVal = new ArrayList<>();
        for (Knowledge knowPacket : this.requiredKnowledge) {
            // Check if the player has enough levels in each required type of knowledge
            retVal.add(Boolean.valueOf(knowledge.getKnowledge(knowPacket.getType()) >= knowPacket.getAmount()));
        }
        return retVal;
    }
    
    public List<Boolean> getResearchRequirementCompletion(@Nullable Player player) {
        if (this.requiredResearch == null || this.requiredResearch.getKeys().isEmpty()) {
            return Collections.emptyList();
        }
        if (player == null) {
            // If the player is invalid, return false for all requirements
            return Collections.nCopies(this.requiredResearch.getKeys().size(), Boolean.FALSE);
        }
        
        List<Boolean> retVal = new ArrayList<>();
        for (SimpleResearchKey key : this.requiredResearch.getKeys()) {
            // Do a strict knowledge check for each required research entry
            retVal.add(Boolean.valueOf(key.isKnownByStrict(player)));
        }
        return retVal;
    }
}
