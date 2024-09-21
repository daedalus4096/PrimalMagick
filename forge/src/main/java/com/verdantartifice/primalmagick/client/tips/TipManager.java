package com.verdantartifice.primalmagick.client.tips;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

/**
 * Container for mod tip definitions.
 * 
 * @author Daedalus4096
 */
public class TipManager {
    protected static final Map<ResourceLocation, TipDefinition> REGISTRY = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static void clearTips() {
        REGISTRY.clear();
    }

    public static void setTipDefinition(ResourceLocation id, TipDefinition tip) {
        if (REGISTRY.containsKey(id)) {
            LOGGER.warn("Tip definition {} is already registered", id.toString());
        } else {
            REGISTRY.put(id, tip);
        }
    }
    
    public static Optional<TipDefinition> getTipDefinition(ResourceLocation id) {
        return Optional.ofNullable(REGISTRY.get(id));
    }
    
    public static Collection<TipDefinition> getAllTipDefinitions() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
    
    public static TipDefinition getRandomTipForPlayer(Player player, RandomSource random) {
        List<TipDefinition> filteredTips = getAllTipDefinitions().stream().filter(tip -> tip.shouldShow(player)).toList();
        return filteredTips.get(random.nextInt(filteredTips.size()));
    }
}
