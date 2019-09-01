package com.verdantartifice.primalmagic.common.research;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ResearchManager {
    private static final Set<Integer> CRAFTING_REFERENCES = new HashSet<>();
    private static final Map<String, Boolean> SYNC_LIST = new ConcurrentHashMap<>();
    
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
            return entry.getParentResearch().isKnownByStrict(player);
        }
    }
    
    public static boolean completeResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        return completeResearch(player, key, true);
    }
    
    public static boolean completeResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key, boolean sync) {
        boolean retVal = false;
        while (progressResearch(player, key, sync)) {
            retVal = true;
        }
        return retVal;
    }
    
    public static void forceGrantWithAllParents(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        if (player != null && key != null) {
            key = key.stripStage();
            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge != null && !knowledge.isResearchComplete(key)) {
                ResearchEntry entry = ResearchEntries.getEntry(key);
                if (entry != null) {
                    if (entry.getParentResearch() != null) {
                        for (SimpleResearchKey parentKey : entry.getParentResearch().getKeys()) {
                            forceGrantWithAllParents(player, parentKey);
                        }
                    }
                    for (ResearchStage stage : entry.getStages()) {
                        if (stage.getRequiredResearch() != null) {
                            for (SimpleResearchKey requiredKey : stage.getRequiredResearch().getKeys()) {
                                completeResearch(player, requiredKey);
                            }
                        }
                    }
                }
                completeResearch(player, key);
                
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
    
    public static boolean progressResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key) {
        return progressResearch(player, key, true);
    }
    
    public static boolean progressResearch(@Nullable PlayerEntity player, @Nullable SimpleResearchKey key, boolean sync) {
        if (player == null || key == null) {
            return false;
        }
        
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        }
        if (knowledge.isResearchComplete(key) || !hasPrerequisites(player, key)) {
            return false;
        }
        
        if (!knowledge.isResearchKnown(key)) {
            knowledge.addResearch(key);
        }
        
        ResearchEntry entry = ResearchEntries.getEntry(key);
        boolean popups;
        if (entry != null) {
            popups = true;
            ResearchStage currentStage = null;
            if (!entry.getStages().isEmpty()) {
                int cs = knowledge.getResearchStage(key);
                if (cs > 0) {
                    cs = Math.min(cs, entry.getStages().size());
                    currentStage = entry.getStages().get(cs - 1);
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
                    // TODO process attunement grants
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
    
    public static boolean addKnowledge(PlayerEntity player, IPlayerKnowledge.KnowledgeType type, ResearchDiscipline discipline, int points) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        }
        int levelsBefore = knowledge.getKnowledge(type, discipline);
        boolean success = knowledge.addKnowledge(type, discipline, points);
        if (!success) {
            return false;
        }
        if (points > 0) {
            int levelsAfter = knowledge.getKnowledge(type, discipline);
            for (int index = 0; index < (levelsAfter - levelsBefore); index++) {
                // TODO send knowledge gain packet to player
            }
        }
        SYNC_LIST.put(player.getName().getString(), Boolean.TRUE);
        return true;
    }
    
    public static void parseAllResearch() {
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
                    JsonObject obj = parser.parse(new InputStreamReader(stream)).getAsJsonObject();
                    JsonArray entries = obj.get("entries").getAsJsonArray();
                    int index = 0;
                    for (JsonElement element : entries) {
                        try {
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
}
