package com.verdantartifice.primalmagick.common.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Primary access point for statistics-related methods.  Also stores the sorted list of stat definitions
 * in a static registry.
 * 
 * @author Daedalus4096
 */
public class StatsManager {
    protected static final Map<ResourceLocation, Stat> REGISTRY = new HashMap<>();
    protected static final List<Stat> DISPLAY_STATS = new ArrayList<>();
    
    // Set of unique IDs of players that need their research synced to their client
    private static final Set<UUID> SYNC_SET = ConcurrentHashMap.newKeySet();
    
    public static boolean isSyncScheduled(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            return SYNC_SET.remove(player.getUUID());
        }
    }
    
    public static void scheduleSync(@Nullable Player player) {
        if (player != null) {
            SYNC_SET.add(player.getUUID());
        }
    }
    
    public static Set<ResourceLocation> getStatLocations() {
        return Collections.unmodifiableSet(REGISTRY.keySet());
    }
    
    public static List<Stat> getDisplayStats() {
        return Collections.unmodifiableList(DISPLAY_STATS);
    }
    
    public static boolean registerStat(@Nullable Stat stat) {
        if (stat != null) {
            REGISTRY.put(stat.getLocation(), stat);
            if (stat.isInternal()) {
                return true;    // Don't register internal stats in the display list
            } else {
                return DISPLAY_STATS.add(stat);
            }
        } else {
            return false;
        }
    }
    
    public static Stat getStat(@Nullable ResourceLocation loc) {
        return REGISTRY.get(loc);
    }
    
    public static int getValue(@Nullable Player player, @Nullable Stat stat) {
        if (player != null) {
            IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
            if (stats != null) {
                // Get the value from the player capability
                return stats.getValue(stat);
            }
        }
        return 0;
    }
    
    @Nonnull
    public static Component getFormattedValue(@Nullable Player player, @Nullable Stat stat) {
        return Component.literal(stat.getFormatter().format(getValue(player, stat)));
    }
    
    public static void incrementValue(@Nullable Player player, @Nullable Stat stat) {
        incrementValue(player, stat, 1);
    }
    
    public static void incrementValue(@Nullable Player player, @Nullable Stat stat, int delta) {
        setValue(player, stat, delta + getValue(player, stat));
    }
    
    public static void setValue(@Nullable Player player, @Nullable Stat stat, int value) {
        if (player instanceof ServerPlayer) {
            ServerPlayer spe = (ServerPlayer)player;
            IPlayerStats stats = PrimalMagickCapabilities.getStats(spe);
            if (stats != null) {
                // Set the new value into the player capability
                stats.setValue(stat, value);
                scheduleSync(spe);
                
                // Check stat triggers for updates
                StatTriggers.checkTriggers(spe, stat, value);
            }
        }
    }
    
    public static void setValueIfMax(@Nullable Player player, @Nullable Stat stat, int newVal) {
        if (newVal > getValue(player, stat)) {
            setValue(player, stat, newVal);
        }
    }
    
    public static void discoverShrine(@Nullable Player player, @Nullable Source shrineSource, @Nullable BlockPos shrinePos) {
        if (player instanceof ServerPlayer && shrineSource != null && shrinePos != null) {
            Stat stat = getShrineStatForSource(shrineSource);
            ServerPlayer spe = (ServerPlayer)player;
            IPlayerStats stats = PrimalMagickCapabilities.getStats(spe);
            if (stat != null && stats != null && !stats.isLocationDiscovered(shrinePos)) {
                // If the location has not yet been discovered, mark it as such and increment the appropriate stat
                int value = 1 + stats.getValue(stat);
                stats.setLocationDiscovered(shrinePos);
                stats.setValue(stat, value);
                scheduleSync(spe);
                
                // Check stat triggers for updates
                StatTriggers.checkTriggers(spe, stat, value);
            }
        }
    }
    
    @Nullable
    protected static Stat getShrineStatForSource(@Nonnull Source source) {
        if (source.equals(Source.EARTH)) {
            return StatsPM.SHRINE_FOUND_EARTH;
        } else if (source.equals(Source.SEA)) {
            return StatsPM.SHRINE_FOUND_SEA;
        } else if (source.equals(Source.SKY)) {
            return StatsPM.SHRINE_FOUND_SKY;
        } else if (source.equals(Source.SUN)) {
            return StatsPM.SHRINE_FOUND_SUN;
        } else if (source.equals(Source.MOON)) {
            return StatsPM.SHRINE_FOUND_MOON;
        } else {
            return null;
        }
    }
}
