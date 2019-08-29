package com.verdantartifice.primalmagic.common.research;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchDisciplines {
    protected static final Map<String, ResearchDiscipline> DISCIPLINES = new HashMap<>();
    
    @Nullable
    public static ResearchDiscipline getDiscipline(String key) {
        return DISCIPLINES.get(key);
    }
    
    @Nonnull
    public static Collection<ResearchDiscipline> getAllDisciplines() {
        return Collections.unmodifiableCollection(DISCIPLINES.values());
    }
    
    @Nullable
    public static ResearchDiscipline registerDiscipline(@Nullable String key, @Nullable String unlockResearchKey) {
        if (key == null || DISCIPLINES.containsKey(key)) {
            return null;
        } else {
            ResearchDiscipline discipline = ResearchDiscipline.create(key, unlockResearchKey);
            if (discipline != null) {
                DISCIPLINES.put(key, discipline);
            }
            return discipline;
        }
    }
}
