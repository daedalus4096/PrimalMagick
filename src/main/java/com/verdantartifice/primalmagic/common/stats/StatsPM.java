package com.verdantartifice.primalmagic.common.stats;

import net.minecraft.stats.IStatFormatter;

/**
 * Collection of statistics tracked by the mod.  The order of definition here is the order in which
 * they will be displayed in the grimoire.
 * 
 * @author Daedalus4096
 */
public class StatsPM {
    public static final Stat GRIMOIRE_READ = Stat.create("grimoire_read", IStatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CAST = Stat.create("spells_cast", IStatFormatter.DEFAULT, false);
}
