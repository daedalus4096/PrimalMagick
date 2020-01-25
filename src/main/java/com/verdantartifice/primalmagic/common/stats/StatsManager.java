package com.verdantartifice.primalmagic.common.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Primary access point for statistics-related methods.  Also stores the sorted list of stat definitions
 * in a static registry.
 * 
 * @author Daedalus4096
 */
public class StatsManager {
    protected static final List<Stat> SORTED_STATS = new ArrayList<>();
    
    public static List<Stat> getSortedStats() {
        return Collections.unmodifiableList(SORTED_STATS);
    }
    
    public static boolean registerStat(Stat stat) {
        return SORTED_STATS.add(stat);
    }
}
