package com.verdantartifice.primalmagick.common.books;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.mutable.MutableObject;
import org.joml.Vector2ic;

import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.books.grids.IGridDefinitionSerializer;
import com.verdantartifice.primalmagick.common.books.grids.PlayerGrid;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.resources.ResourceLocation;
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
    
    public static final IGridDefinitionSerializer GRID_DEFINITION_SERIALIZER = new GridDefinition.Serializer();
    protected static final Map<ResourceLocation, GridDefinition> GRID_DEFINITIONS = new HashMap<>();

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
    
    public static boolean isLanguageKnown(@Nullable Player player, @Nullable BookLanguage language) {
        // All players know the default language automatically
        if (BookLanguagesPM.DEFAULT.get().equals(language)) {
            return true;
        }
        
        MutableBoolean retVal = new MutableBoolean(false);
        if (player != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                retVal.setValue(linguistics.isLanguageKnown(language.languageId()));
            });
        }
        return retVal.booleanValue();
    }
    
    public static void markRead(@Nullable Player player, @Nullable BookDefinition book, @Nullable BookLanguage language) {
        if (player != null && book != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                if (linguistics.markRead(book.bookId(), language.languageId())) {
                    // If the book/language combination is new, increment the unique books statistic
                    StatsManager.incrementValue(player, StatsPM.ANCIENT_BOOKS_READ);
                }
                scheduleSync(player);
            });
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
    
    public static int getVocabulary(@Nullable Player player, @Nullable BookLanguage language) {
        MutableInt retVal = new MutableInt(0);
        if (player != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                retVal.setValue(linguistics.getVocabulary(language.languageId()));
            });
        }
        return retVal.intValue();
    }
    
    public static void setVocabulary(@Nullable Player player, @Nullable BookLanguage language, int vocabulary) {
        if (player != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                linguistics.setVocabulary(language.languageId(), Math.max(0, vocabulary));
                scheduleSync(player);
            });
        }
    }
    
    public static void incrementVocabulary(@Nullable Player player, @Nullable BookLanguage language) {
        incrementVocabulary(player, language, 1);
    }
    
    public static void incrementVocabulary(@Nullable Player player, @Nullable BookLanguage language, int delta) {
        if (player != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                linguistics.setVocabulary(language.languageId(), Math.max(0, linguistics.getVocabulary(language.languageId()) + delta));
                scheduleSync(player);
            });
        }
    }
    
    public static int getTimesStudied(@Nullable Player player, @Nullable BookDefinition book, @Nullable BookLanguage language) {
        MutableInt retVal = new MutableInt(0);
        if (player != null && book != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                retVal.setValue(linguistics.getTimesStudied(book.bookId(), language.languageId()));
            });
        }
        return retVal.intValue();
    }
    
    public static void setTimesStudied(@Nullable Player player, @Nullable BookDefinition book, @Nullable BookLanguage language, int studyCount) {
        if (player != null && book != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                linguistics.setTimesStudied(book.bookId(), language.languageId(), Math.max(0, studyCount));
                scheduleSync(player);
            });
        }
    }
    
    public static void incrementTimesStudied(@Nullable Player player, @Nullable BookDefinition book, @Nullable BookLanguage language) {
        incrementTimesStudied(player, book, language, 1);
    }
    
    public static void incrementTimesStudied(@Nullable Player player, @Nullable BookDefinition book, @Nullable BookLanguage language, int delta) {
        if (player != null && book != null && language != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                linguistics.setTimesStudied(book.bookId(), language.languageId(), Math.max(0, linguistics.getTimesStudied(book.bookId(), language.languageId()) + delta));
                scheduleSync(player);
            });
        }
    }
    
    public static ScribeTableMode getScribeTableMode(@Nullable Player player) {
        MutableObject<ScribeTableMode> retVal = new MutableObject<>(ScribeTableMode.STUDY_VOCABULARY);
        if (player != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                retVal.setValue(linguistics.getScribeTableMode());
            });
        }
        return retVal.getValue();
    }
    
    public static void setScribeTableMode(@Nullable Player player, @Nullable ScribeTableMode mode) {
        if (player != null && mode != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                linguistics.setScribeTableMode(mode);
                scheduleSync(player);
            });
        }
    }
    
    public static void clearAllGridDefinitions() {
        GRID_DEFINITIONS.clear();
    }
    
    public static Map<ResourceLocation, GridDefinition> getAllGridDefinitions() {
        return Collections.unmodifiableMap(GRID_DEFINITIONS);
    }
    
    public static boolean registerGridDefinition(ResourceLocation definitionKey, GridDefinition gridDefinition) {
        if (GRID_DEFINITIONS.containsKey(definitionKey)) {
            return false;
        } else {
            GRID_DEFINITIONS.put(definitionKey, gridDefinition);
            return true;
        }
    }
    
    @Nullable
    protected static GridDefinition getGridDefinition(@Nonnull ResourceLocation gridKey) {
        return GRID_DEFINITIONS.get(gridKey);
    }
    
    protected static Set<Vector2ic> getUnlockedGridNodes(@Nullable Player player, @Nullable ResourceLocation gridKey) {
        MutableObject<Set<Vector2ic>> retVal = new MutableObject<>(Collections.emptySet());
        if (player != null && gridKey != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                retVal.setValue(linguistics.getUnlockedNodes(gridKey));
            });
        }
        return retVal.getValue();
    }
    
    protected static long getGridLastModified(@Nullable Player player, @Nullable ResourceLocation gridKey) {
        MutableLong retVal = new MutableLong(0L);
        if (player != null && gridKey != null) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                retVal.setValue(linguistics.getGridLastModified(gridKey));
            });
        }
        return retVal.getValue();
    }
    
    @Nullable
    public static PlayerGrid getPlayerGrid(@Nonnull Player player, @Nonnull ResourceLocation gridKey) {
        GridDefinition gridDef = getGridDefinition(gridKey);
        return gridDef == null ? null : new PlayerGrid(player, gridDef, getUnlockedGridNodes(player, gridKey), getGridLastModified(player, gridKey));
    }
}
