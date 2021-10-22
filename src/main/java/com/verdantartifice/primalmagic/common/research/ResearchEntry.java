package com.verdantartifice.primalmagic.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Definition of a research entry, the primary component of the research system.  A research entry
 * represents a single node in the mod research tree and a single named entry in the grimoire.  A
 * research entry is made up of one or more stages, which are progressed through in sequence using
 * the grimoire.  It may have one or more parent research entries, which are required to unlock 
 * access to the entry.  It may also have zero or more addenda to be unlocked after the entry is
 * completed.
 * 
 * @author Daedalus4096
 */
public class ResearchEntry {
    protected SimpleResearchKey key;
    protected String disciplineKey;
    protected String nameTranslationKey;
    protected CompoundResearchKey parentResearch;
    protected boolean hidden;
    protected List<ResearchStage> stages = new ArrayList<>();
    protected List<ResearchAddendum> addenda = new ArrayList<>();
    
    protected ResearchEntry(@Nonnull SimpleResearchKey key, @Nonnull String disciplineKey, @Nonnull String nameTranslationKey) {
        this.key = key;
        this.disciplineKey = disciplineKey;
        this.nameTranslationKey = nameTranslationKey;
        this.hidden = false;
    }
    
    @Nullable
    public static ResearchEntry create(@Nullable SimpleResearchKey key, @Nullable String disciplineKey, @Nullable String nameTranslationKey) {
        if (key == null || disciplineKey == null || nameTranslationKey == null) {
            return null;
        } else {
            // ResearchEntry main keys should never have a stage
            return new ResearchEntry(key.stripStage(), disciplineKey, nameTranslationKey);
        }
    }
    
    @Nonnull
    public static ResearchEntry parse(@Nonnull JsonObject obj) throws Exception {
        // Parse a research entry from a research definition file
        ResearchEntry entry = create(
            SimpleResearchKey.parse(obj.getAsJsonPrimitive("key").getAsString()),
            obj.getAsJsonPrimitive("discipline").getAsString(),
            obj.getAsJsonPrimitive("name").getAsString()
        );
        if (entry == null) {
            throw new JsonParseException("Invalid entry data in research JSON");
        }
        
        if (obj.has("hidden")) {
            entry.hidden = obj.getAsJsonPrimitive("hidden").getAsBoolean();
        }
        
        if (obj.has("parents")) {
            entry.parentResearch = CompoundResearchKey.parse(obj.get("parents").getAsJsonArray());
        }
        
        for (JsonElement element : obj.get("stages").getAsJsonArray()) {
            entry.stages.add(ResearchStage.parse(entry, element.getAsJsonObject()));
        }
        
        if (obj.has("addenda")) {
            for (JsonElement element : obj.get("addenda").getAsJsonArray()) {
                entry.addenda.add(ResearchAddendum.parse(entry, element.getAsJsonObject()));
            }
        }
        
        return entry;
    }
    
    @Nonnull
    public static ResearchEntry fromNetwork(FriendlyByteBuf buf) {
        SimpleResearchKey key = SimpleResearchKey.parse(buf.readUtf());
        String discipline = buf.readUtf();
        String name = buf.readUtf();
        ResearchEntry entry = create(key, discipline, name);
        entry.hidden = buf.readBoolean();
        entry.parentResearch = CompoundResearchKey.parse(buf.readUtf());
        int stageCount = buf.readVarInt();
        for (int index = 0; index < stageCount; index++) {
            entry.stages.add(ResearchStage.fromNetwork(buf, entry));
        }
        int addendumCount = buf.readVarInt();
        for (int index = 0; index < addendumCount; index++) {
            entry.addenda.add(ResearchAddendum.fromNetwork(buf, entry));
        }
        return entry;
    }
    
    public static void toNetwork(FriendlyByteBuf buf, ResearchEntry entry) {
        buf.writeUtf(entry.key.toString());
        buf.writeUtf(entry.disciplineKey);
        buf.writeUtf(entry.nameTranslationKey);
        buf.writeBoolean(entry.hidden);
        buf.writeUtf(entry.parentResearch == null ? "" : entry.parentResearch.toString());
        buf.writeVarInt(entry.stages.size());
        for (ResearchStage stage : entry.stages) {
            ResearchStage.toNetwork(buf, stage);
        }
        buf.writeVarInt(entry.addenda.size());
        for (ResearchAddendum addendum : entry.addenda) {
            ResearchAddendum.toNetwork(buf, addendum);
        }
    }
    
    @Nonnull
    public SimpleResearchKey getKey() {
        return this.key;
    }
    
    @Nonnull
    public String getDisciplineKey() {
        return this.disciplineKey;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return this.nameTranslationKey;
    }
    
    @Nullable
    public CompoundResearchKey getParentResearch() {
        return this.parentResearch;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    @Nonnull
    public List<ResearchStage> getStages() {
        return Collections.unmodifiableList(this.stages);
    }
    
    public boolean appendStage(@Nullable ResearchStage stage) {
        return (stage == null) ? false : this.stages.add(stage);
    }
    
    @Nonnull
    public List<ResearchAddendum> getAddenda() {
        return Collections.unmodifiableList(this.addenda);
    }
    
    public boolean appendAddendum(@Nullable ResearchAddendum addendum) {
        return (addendum == null) ? false : this.addenda.add(addendum);
    }
    
    @Nonnull
    protected IPlayerKnowledge getKnowledge(@Nonnull Player player) {
        return PrimalMagicCapabilities.getKnowledge(player).orElseThrow(() -> new IllegalStateException("No knowledge provider for player"));
    }
    
    public boolean isNew(@Nonnull Player player) {
        return this.getKnowledge(player).hasResearchFlag(this.getKey(), IPlayerKnowledge.ResearchFlag.NEW);
    }
    
    public boolean isUpdated(@Nonnull Player player) {
        return this.getKnowledge(player).hasResearchFlag(this.getKey(), IPlayerKnowledge.ResearchFlag.UPDATED);
    }
    
    public boolean isComplete(@Nonnull Player player) {
        return this.getKnowledge(player).getResearchStatus(this.getKey()) == IPlayerKnowledge.ResearchStatus.COMPLETE;
    }
    
    public boolean isInProgress(@Nonnull Player player) {
        return this.getKnowledge(player).getResearchStatus(this.getKey()) == IPlayerKnowledge.ResearchStatus.IN_PROGRESS;
    }
    
    public boolean isAvailable(@Nonnull Player player) {
        return this.getParentResearch() == null || this.getParentResearch().isKnownByStrict(player);
    }
    
    public boolean isUpcoming(@Nonnull Player player) {
        for (SimpleResearchKey parentKey : this.getParentResearch().getKeys()) {
            if (ResearchManager.isOpaque(parentKey)) {
                return false;
            } else {
                ResearchEntry parent = ResearchEntries.getEntry(parentKey);
                if (parent != null && !parent.isAvailable(player)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Nonnull
    public Set<ResourceLocation> getAllRecipeIds() {
        Set<ResourceLocation> retVal = new HashSet<>();
        
        ResearchStage lastStage = this.stages.isEmpty() ? null : this.stages.get(this.stages.size() - 1);
        if (lastStage != null) {
            retVal.addAll(lastStage.getRecipes());
        }
        
        for (ResearchAddendum addendum : this.addenda) {
            retVal.addAll(addendum.getRecipes());
        }
        
        return retVal;
    }
    
    @Nonnull
    public Set<ResourceLocation> getKnownRecipeIds(Player player) {
        Set<ResourceLocation> retVal = new HashSet<>();
        IPlayerKnowledge knowledge = this.getKnowledge(player);
        
        ResearchStage currentStage = null;
        int currentStageNum = knowledge.getResearchStage(key);
        if (currentStageNum >= 0) {
            currentStage = this.getStages().get(Math.min(currentStageNum, this.getStages().size() - 1));
        }
        boolean entryComplete = (currentStageNum >= this.getStages().size());
        
        if (currentStage != null) {
            retVal.addAll(currentStage.getRecipes());
        }
        if (entryComplete) {
            for (ResearchAddendum addendum : this.getAddenda()) {
                if (addendum.getRequiredResearch() == null || addendum.getRequiredResearch().isKnownByStrict(player)) {
                    retVal.addAll(addendum.getRecipes());
                }
            }
            for (ResearchEntry searchEntry : ResearchEntries.getAllEntries()) {
                if (!searchEntry.getAddenda().isEmpty() && knowledge.isResearchComplete(searchEntry.getKey())) {
                    for (ResearchAddendum addendum : searchEntry.getAddenda()) {
                        if (addendum.getRequiredResearch() != null && addendum.getRequiredResearch().contains(this.getKey()) && addendum.getRequiredResearch().isKnownByStrict(player)) {
                            retVal.addAll(addendum.getRecipes());
                        }
                    }
                }
            }
        }
        
        return retVal;
    }
}
