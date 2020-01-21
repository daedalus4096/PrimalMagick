package com.verdantartifice.primalmagic.common.research;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Convenience class for accessing research entries without having to manually navigate the
 * discipline hierarchy.
 * 
 * @author Daedalus4096
 */
public class ResearchEntries {
    @Nullable
    public static ResearchEntry getEntry(SimpleResearchKey key) {
        for (ResearchDiscipline discipline : ResearchDisciplines.getAllDisciplines()) {
            ResearchEntry entry = discipline.getEntry(key);
            if (entry != null) {
                return entry;
            }
        }
        return null;
    }
    
    @Nonnull
    public static Collection<ResearchEntry> getAllEntries() {
        Set<ResearchEntry> entries = new HashSet<>();
        for (ResearchDiscipline discipline : ResearchDisciplines.getAllDisciplines()) {
            for (ResearchEntry entry : discipline.getEntries()) {
                entries.add(entry);
            }
        }
        return entries;
    }
}
