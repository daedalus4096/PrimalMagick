package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.stats.Stat;

import net.minecraft.resources.ResourceLocation;

/**
 * Collection of all defined research disciplines and their defining JSON data files.
 * 
 * @author Daedalus4096
 */
public class ResearchDisciplines {
    protected static final Map<String, ResearchDiscipline> DISCIPLINES = new HashMap<>();
    protected static final List<ResearchDiscipline> DISCIPLINES_SORTED = new ArrayList<>();
    
    @Nullable
    public static ResearchDiscipline getDiscipline(String key) {
        return DISCIPLINES.get(key);
    }
    
    @Nonnull
    public static Collection<ResearchDiscipline> getAllDisciplines() {
        return Collections.unmodifiableCollection(DISCIPLINES.values());
    }
    
    @Nonnull
    public static List<ResearchDiscipline> getAllDisciplinesSorted() {
        return Collections.unmodifiableList(DISCIPLINES_SORTED);
    }
    
    @Nullable
    public static ResearchDiscipline registerDiscipline(@Nullable String key, @Nullable CompoundResearchKey unlockResearchKey, @Nullable ResourceLocation icon, @Nullable Stat craftingStat) {
        if (key == null || DISCIPLINES.containsKey(key)) {
            // Don't allow null or duplicate disciplines in the collection
            return null;
        } else {
            ResearchDiscipline discipline = ResearchDiscipline.create(key, unlockResearchKey, icon, craftingStat);
            if (discipline != null) {
                DISCIPLINES.put(key, discipline);
                DISCIPLINES_SORTED.add(discipline);
            }
            return discipline;
        }
    }
    
    static void clearAllResearch() {
        for (ResearchDiscipline discipline : DISCIPLINES.values()) {
            discipline.clearEntries();
        }
    }
}
