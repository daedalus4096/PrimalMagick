package com.verdantartifice.primalmagic.common.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * Primary access point for statistics-related methods.  Also stores the sorted list of stat definitions
 * in a static registry.
 * 
 * @author Daedalus4096
 */
public class StatsManager {
    protected static final Map<ResourceLocation, Stat> REGISTRY = new HashMap<>();
    protected static final List<Stat> SORTED_STATS = new ArrayList<>();
    
    public static Set<ResourceLocation> getStatLocations() {
        return Collections.unmodifiableSet(REGISTRY.keySet());
    }
    
    public static List<Stat> getSortedStats() {
        return Collections.unmodifiableList(SORTED_STATS);
    }
    
    public static boolean registerStat(@Nullable Stat stat) {
        if (stat != null) {
            REGISTRY.put(stat.getLocation(), stat);
            return SORTED_STATS.add(stat);
        } else {
            return false;
        }
    }
    
    public static Stat getStat(@Nullable ResourceLocation loc) {
        return REGISTRY.get(loc);
    }
    
    public static int getValue(@Nullable PlayerEntity player, @Nullable Stat stat) {
        if (player != null) {
            IPlayerStats stats = PrimalMagicCapabilities.getStats(player);
            if (stats != null) {
                return stats.getValue(stat);
            }
        }
        return 0;
    }
    
    @Nonnull
    public static ITextComponent getFormattedValue(@Nullable PlayerEntity player, @Nullable Stat stat) {
        return new StringTextComponent(stat.getFormatter().format(getValue(player, stat)));
    }
    
    public static void incrementValue(@Nullable ServerPlayerEntity player, @Nullable Stat stat) {
        incrementValue(player, stat, 1);
    }
    
    public static void incrementValue(@Nullable ServerPlayerEntity player, @Nullable Stat stat, int delta) {
        if (player != null) {
            IPlayerStats stats = PrimalMagicCapabilities.getStats(player);
            if (stats != null) {
                int oldVal = stats.getValue(stat);
                stats.setValue(stat, oldVal + delta);
                stats.sync(player);
            }
        }
    }
    
    public static void setValueIfMax(@Nullable ServerPlayerEntity player, @Nullable Stat stat, int newVal) {
        if (player != null) {
            IPlayerStats stats = PrimalMagicCapabilities.getStats(player);
            if (stats != null) {
                int oldVal = stats.getValue(stat);
                if (newVal > oldVal) {
                    stats.setValue(stat, newVal);
                    stats.sync(player);
                }
            }
        }
    }
}
