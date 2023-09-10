package com.verdantartifice.primalmagick.common.books;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableInt;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * Primary access point for linguistics-related methods.
 * 
 * @author Daedalus4096
 */
public class LinguisticsManager {
    // Set of unique IDs of players that need their linguistics data synced to their client
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
    
    public static int getComprehension(@Nullable Player player, @Nullable BookLanguage language) {
        MutableInt retVal = new MutableInt(0);
        if (player != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                retVal.setValue(linguistics.getComprehension(language.languageId()));
            });
        }
        return retVal.intValue();
    }
    
    public static void setComprehension(@Nullable Player player, @Nullable BookLanguage language, int comprehension) {
        if (player != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                linguistics.setComprehension(language.languageId(), Mth.clamp(comprehension, 0, language.complexity()));
                scheduleSync(player);
            });
        }
    }
    
    public static void incrementComprehension(@Nullable Player player, @Nullable BookLanguage language) {
        incrementComprehension(player, language, 1);
    }
    
    public static void incrementComprehension(@Nullable Player player, @Nullable BookLanguage language, int delta) {
        if (player != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                linguistics.setComprehension(language.languageId(), Mth.clamp(linguistics.getComprehension(language.languageId()) + delta, 0, language.complexity()));
                scheduleSync(player);
            });
        }
    }
}
