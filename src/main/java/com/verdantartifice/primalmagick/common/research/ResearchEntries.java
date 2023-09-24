package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    public static List<ResearchEntry> getEntries(@Nullable CompoundResearchKey key) {
        List<ResearchEntry> retVal = Collections.synchronizedList(new ArrayList<>());
        if (key != null) {
            for (SimpleResearchKey simpleKey : key.getKeys()) {
                ResearchEntry entry = getEntry(simpleKey);
                if (entry != null) {
                    retVal.add(entry);
                }
            }
        }
        return retVal;
    }
    
    @Nonnull
    public static Collection<ResearchEntry> getAllEntries() {
        Set<ResearchEntry> entries = Collections.synchronizedSet(new HashSet<>());
        for (ResearchDiscipline discipline : ResearchDisciplines.getAllDisciplines()) {
            for (ResearchEntry entry : discipline.getEntries()) {
                entries.add(entry);
            }
        }
        return entries;
    }
}
