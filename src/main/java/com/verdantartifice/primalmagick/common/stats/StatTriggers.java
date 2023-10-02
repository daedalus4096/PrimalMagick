package com.verdantartifice.primalmagick.common.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.server.level.ServerPlayer;

/**
 * Collection of triggers which grant a player research when they meet or exceed a given threshold
 * for a given statistic.
 * 
 * @author Daedalus4096
 */
public class StatTriggers {
    private static final Map<Stat, Map<Integer, List<SimpleResearchKey>>> REGISTRY = new HashMap<>();
    
    public static void register(@Nullable Stat stat, int threshold, @Nonnull Optional<SimpleResearchKey> researchOpt) {
        register(stat, threshold, researchOpt.orElseThrow());
    }
    
    public static void register(@Nullable Stat stat, int threshold, @Nullable SimpleResearchKey research) {
        // Don't allow null keys or values in the registry
        if (stat != null && research != null) {
            // Get the map of thresholds to research for the stat, creating an empty one if it doesn't exist
            Map<Integer, List<SimpleResearchKey>> statMap = REGISTRY.computeIfAbsent(stat, k -> new HashMap<>());
            
            // Get the list of research keys for that stat threshold, creating an empty one if it doesn't exist
            List<SimpleResearchKey> keyList = statMap.computeIfAbsent(Integer.valueOf(threshold), k -> new ArrayList<>());
            
            // Add the given research key to the list
            keyList.add(research);
        }
    }
    
    public static void checkTriggers(@Nullable ServerPlayer player, @Nullable Stat stat, int value) {
        if (player != null && stat != null) {
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                boolean found = false;
                
                // Iterate through the map of thresholds to research for the stat, or an empty map if it doesn't exist
                Map<Integer, List<SimpleResearchKey>> statMap = REGISTRY.getOrDefault(stat, Collections.emptyMap());
                for (Map.Entry<Integer, List<SimpleResearchKey>> entry : statMap.entrySet()) {
                    // If the given value meets or exceeds the threshold, process the trigger
                    if (value >= entry.getKey().intValue()) {
                        for (SimpleResearchKey research : entry.getValue()) {
                            // Grant each unknown research key
                            if (!knowledge.isResearchComplete(research)) {
                                ResearchManager.completeResearch(player, research, false);  // Save syncing for a batch at the end
                                found = true;
                            }
                        }
                    }
                }
                
                // If any research was granted, do a sync
                if (found) {
                    ResearchManager.scheduleSync(player);
                }
            });
        }
    }
}
