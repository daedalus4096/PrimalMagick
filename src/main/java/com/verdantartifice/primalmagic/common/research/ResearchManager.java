package com.verdantartifice.primalmagic.common.research;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.attunements.AttunementManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementType;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Primary access point for research-related methods.
 * 
 * @author Daedalus4096
 */
public class ResearchManager {
    // Hash codes of items that must be crafted to complete one or more research stages
    private static final Set<Integer> CRAFTING_REFERENCES = new HashSet<>();
    
    // Map of names of players that need their research synced to their client
    private static final Map<String, Boolean> SYNC_LIST = new ConcurrentHashMap<>();
    
    // Registry of all defined scan triggers
    private static final List<IScanTrigger> SCAN_TRIGGERS = new ArrayList<>();
    
    // Whether the "new" research flag should be suppressed during progressResearch
    public static boolean noFlags = false;
    
    public static Set<Integer> getAllCraftingReferences() {
        return Collections.unmodifiableSet(CRAFTING_REFERENCES);
    }
    
    public static boolean addCraftingReference(int reference) {
        return CRAFTING_REFERENCES.add(Integer.valueOf(reference));
    }
    
    @Nullable
    public static Boolean popSyncList(@Nullable String name) {
        return SYNC_LIST.remove(name);
    }
    
    public static boolean hasPrerequisites(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        if (player == null) {
            return false;
        }
        if (key == null) {
            return true;
        }
        ResearchEntry entry = ResearchEntries.getEntry(key);
        if (entry == null || entry.getParentResearch() == null) {
            return true;
        } else {
            // Perform a strict completion check on the given entry's parent research
            return entry.getParentResearch().isKnownByStrict(player);
        }
    }
    
    public static boolean isResearchComplete(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        if (player == null || key == null) {
            return false;
        }
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        } else {
            return knowledge.isResearchComplete(key);
        }
    }
    
    public static boolean completeResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        // Complete the given research and sync it to the player's client
        return completeResearch(player, key, true);
    }
    
    public static boolean completeResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key, boolean sync) {
        // Repeatedly progress the given research until it is completed, optionally syncing it to the player's client
        boolean retVal = false;
        while (progressResearch(player, key, sync)) {
            retVal = true;
        }
        return retVal;
    }
    
    public static void forceGrantWithAllParents(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        if (player != null && key != null) {
            key = key.stripStage(); // When we force-grant, we fully complete the entry, not partially
            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge != null && !knowledge.isResearchComplete(key)) {
                ResearchEntry entry = ResearchEntries.getEntry(key);
                if (entry != null) {
                    if (entry.getParentResearch() != null) {
                        // Recursively force-grant all of this entry's parent entries, even if not all of them are required
                        for (SimpleResearchKey parentKey : entry.getParentResearch().getKeys()) {
                            forceGrantWithAllParents(player, parentKey);
                        }
                    }
                    for (ResearchStage stage : entry.getStages()) {
                        // Complete any research required as a prerequisite for any of the entry's stages
                        if (stage.getRequiredResearch() != null) {
                            for (SimpleResearchKey requiredKey : stage.getRequiredResearch().getKeys()) {
                                completeResearch(player, requiredKey);
                            }
                        }
                    }
                }
                
                // Once all prerequisites are out of the way, complete this entry itself
                completeResearch(player, key);
                
                // Mark as updated any research entry that has a stage which requires completion of this entry
                for (ResearchEntry searchEntry : ResearchEntries.getAllEntries()) {
                    for (ResearchStage searchStage : searchEntry.getStages()) {
                        if (searchStage.getRequiredResearch() != null && searchStage.getRequiredResearch().contains(key)) {
                            knowledge.addResearchFlag(searchEntry.getKey(), IPlayerKnowledge.ResearchFlag.UPDATED);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public static void forceRevokeWithAllChildren(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        if (player != null && key != null) {
            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge != null && knowledge.isResearchComplete(key)) {
                // Revoke all child research of this entry
                for (ResearchEntry entry : ResearchEntries.getAllEntries()) {
                    CompoundResearchKey parentResearch = entry.getParentResearch();
                    if (parentResearch != null && parentResearch.containsStripped(key)) {
                        forceRevokeWithAllChildren(player, entry.getKey());
                    }
                }
                
                // Once all children are revoked, revoke this entry itself
                revokeResearch(player, key.stripStage());
            }
        }
    }
    
    public static boolean revokeResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        // Revoke the given research and sync it to the player's client
        return revokeResearch(player, key, true);
    }
    
    public static boolean revokeResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key, boolean sync) {
        // Remove the given research from the player's known list and optionally sync to the player's client
        if (player == null || key == null) {
            return false;
        }
        
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        }

        knowledge.removeResearch(key);
        if (sync) {
            SYNC_LIST.put(player.getName().getString(), Boolean.TRUE);
        }
        return true;
    }
    
    public static boolean progressResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        // Progress the given research to its next stage and sync to the player's client
        return progressResearch(player, key, true);
    }
    
    public static boolean progressResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key, boolean sync) {
        // Progress the given research to its next stage and optionally sync to the player's client
        if (player == null || key == null) {
            return false;
        }
        
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        }
        if (knowledge.isResearchComplete(key) || !hasPrerequisites(player, key)) {
            // If the research is already complete or the player doesn't have the prerequisites, abort
            return false;
        }
        
        // If the research is not started yet, start it
        if (!knowledge.isResearchKnown(key)) {
            knowledge.addResearch(key);
        }
        
        ResearchEntry entry = ResearchEntries.getEntry(key);
        boolean popups = true;
        if (entry != null) {
            ResearchStage currentStage = null;
            if (!entry.getStages().isEmpty()) {
                // Get the current stage of the research entry
                int cs = knowledge.getResearchStage(key);
                if (cs > 0) {
                    cs = Math.min(cs, entry.getStages().size());
                    currentStage = entry.getStages().get(cs - 1);   // Remember, it's one-based
                }
                if (entry.getStages().size() == 1 && cs == 0 && !entry.getStages().get(0).hasPrerequisites()) {
                    cs++;
                } else if (entry.getStages().size() > 1 && cs == (entry.getStages().size() - 1) && !entry.getStages().get(cs).hasPrerequisites()) {
                    cs++;
                }
                knowledge.setResearchStage(key, Math.min(entry.getStages().size() + 1, cs + 1));
                popups = (cs >= entry.getStages().size());
                
                if (popups) {
                    cs = Math.min(cs, entry.getStages().size());
                    currentStage = entry.getStages().get(cs - 1);
                }
                
                if (currentStage != null) {
                    // Process attunement grants
                    SourceList attunements = currentStage.getAttunements();
                    for (Source source : attunements.getSources()) {
                        int amount = attunements.getAmount(source);
                        if (amount > 0) {
                            AttunementManager.incrementAttunement(player, source, AttunementType.PERMANENT, amount);
                        }
                    }
                }
            }
        }
        if (popups) {
            if (sync) {
                knowledge.addResearchFlag(key, IPlayerKnowledge.ResearchFlag.POPUP);
                if (!noFlags) {
                    knowledge.addResearchFlag(key, IPlayerKnowledge.ResearchFlag.NEW);
                } else {
                    noFlags = false;
                }
            }
            for (ResearchEntry searchEntry : ResearchEntries.getAllEntries()) {
                if (!searchEntry.getAddenda().isEmpty() && knowledge.isResearchComplete(searchEntry.getKey())) {
                    for (ResearchAddendum addendum : searchEntry.getAddenda()) {
                        if (addendum.getRequiredResearch() != null && addendum.getRequiredResearch().contains(key)) {
                            ITextComponent nameComp = new TranslationTextComponent(searchEntry.getNameTranslationKey());
                            player.sendMessage(new TranslationTextComponent("event.primalmagic.add_addendum", nameComp));
                            knowledge.addResearchFlag(searchEntry.getKey(), IPlayerKnowledge.ResearchFlag.UPDATED);
                            
                            // Process attunement grants
                            SourceList attunements = addendum.getAttunements();
                            for (Source source : attunements.getSources()) {
                                int amount = attunements.getAmount(source);
                                if (amount > 0) {
                                    AttunementManager.incrementAttunement(player, source, AttunementType.PERMANENT, amount);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sync) {
            SYNC_LIST.put(player.getName().getString(), Boolean.TRUE);
            if (entry != null) {
                player.giveExperiencePoints(5);
            }
        }
        
        return true;
    }
    
    public static boolean addKnowledge(PlayerEntity player, IPlayerKnowledge.KnowledgeType type, int points) {
        // Add the given number of knowledge points to the player and sync to their client
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        }
        int levelsBefore = knowledge.getKnowledge(type);
        boolean success = knowledge.addKnowledge(type, points);
        if (!success) {
            return false;
        }
        if (points > 0) {
            int levelsAfter = knowledge.getKnowledge(type);
            for (int index = 0; index < (levelsAfter - levelsBefore); index++) {
                // TODO send knowledge gain packet to player to show client effects for each level gained
            }
        }
        SYNC_LIST.put(player.getName().getString(), Boolean.TRUE);
        return true;
    }
    
    public static void parseAllResearch() {
        // Parse all research definition files and populate the mod's research data
        CRAFTING_REFERENCES.clear();
        JsonParser parser = new JsonParser();
        for (ResourceLocation location : ResearchDisciplines.getAllDataFileLocations()) {
            String locStr = "/data/" + location.getNamespace() + "/" + location.getPath();
            if (!locStr.endsWith(".json")) {
                locStr += ".json";
            }
            InputStream stream = ResearchManager.class.getResourceAsStream(locStr);
            if (stream != null) {
                try {
                    // Get the definition file contents as a JSON object
                    JsonObject obj = parser.parse(new InputStreamReader(stream)).getAsJsonObject();
                    JsonArray entries = obj.get("entries").getAsJsonArray();
                    int index = 0;
                    for (JsonElement element : entries) {
                        try {
                            // Parse each defined entry and add it to its respective discipline
                            ResearchEntry entry = ResearchEntry.parse(element.getAsJsonObject());
                            ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(entry.getDisciplineKey());
                            if (discipline == null || !discipline.addEntry(entry)) {
                                PrimalMagic.LOGGER.warn("Could not add invalid entry: {}", entry.getKey());
                            } else {
                                index++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrimalMagic.LOGGER.warn("Invalid research entry #{} found in {}", index, location.toString());
                        }
                    }
                    PrimalMagic.LOGGER.info("Loaded {} research entries from {}", index, location.toString());
                } catch (Exception e) {
                    PrimalMagic.LOGGER.warn("Invalid research file: {}", location.toString());
                }
            } else {
                PrimalMagic.LOGGER.warn("Research file not found: {}", location.toString());
            }
        }
    }
    
    public static boolean registerScanTrigger(@Nullable IScanTrigger trigger) {
        if (trigger == null) {
            return false;
        }
        return SCAN_TRIGGERS.add(trigger);
    }
    
    public static void checkScanTriggers(ServerPlayerEntity player, IItemProvider itemProvider) {
        for (IScanTrigger trigger : SCAN_TRIGGERS) {
            if (trigger.matches(player, itemProvider)) {
                trigger.onMatch(player, itemProvider);
            }
        }
    }
}
