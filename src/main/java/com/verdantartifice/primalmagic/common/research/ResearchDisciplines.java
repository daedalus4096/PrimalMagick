package com.verdantartifice.primalmagic.common.research;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

public class ResearchDisciplines {
    protected static final Map<String, ResearchDiscipline> DISCIPLINES = new HashMap<>();
    protected static final Map<String, ResourceLocation> DATA_FILES = new HashMap<>();
    
    @Nullable
    public static ResearchDiscipline getDiscipline(String key) {
        return DISCIPLINES.get(key);
    }
    
    @Nonnull
    public static Collection<ResearchDiscipline> getAllDisciplines() {
        return Collections.unmodifiableCollection(DISCIPLINES.values());
    }
    
    @Nonnull
    public static Collection<ResourceLocation> getAllDataFileLocations() {
        return Collections.unmodifiableCollection(DATA_FILES.values());
    }
    
    @Nullable
    public static ResearchDiscipline registerDiscipline(@Nullable String key, @Nullable CompoundResearchKey unlockResearchKey, @Nullable ResourceLocation icon) {
        if (key == null || DISCIPLINES.containsKey(key)) {
            return null;
        } else {
            ResearchDiscipline discipline = ResearchDiscipline.create(key, unlockResearchKey, icon);
            if (discipline != null) {
                DISCIPLINES.put(key, discipline);
            }
            return discipline;
        }
    }
    
    public static void registerResearchLocation(ResourceLocation location) {
        if (location != null && !DATA_FILES.containsKey(location.toString())) {
            DATA_FILES.put(location.toString(), location);
        }
    }
}
