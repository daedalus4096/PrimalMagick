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
    public static final Stat CRAFTED_MANAWEAVING = Stat.create("crafted_manaweaving", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_ALCHEMY = Stat.create("crafted_alchemy", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_SORCERY = Stat.create("crafted_sorcery", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_RUNEWORKING = Stat.create("crafted_runeworking", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_RITUAL = Stat.create("crafted_ritual", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_MAGITECH = Stat.create("crafted_magitech", IStatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CAST = Stat.create("spells_cast", IStatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CRAFTED = Stat.create("spells_crafted", IStatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CRAFTED_MAX_COST = Stat.create("spells_crafted_max_cost", IStatFormatter.DEFAULT, false);
}
