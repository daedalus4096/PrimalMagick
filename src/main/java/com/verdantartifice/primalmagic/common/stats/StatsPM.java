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
    
    public static void init() {
        // Do nothing.  Calling this method just forces the class loader to load this class and run its 
        // static initializers.  The alternative would be to create a custom Forge registry to hold mod
        // stats, and that comes with a lot of hoops to jump through.
    }
}
