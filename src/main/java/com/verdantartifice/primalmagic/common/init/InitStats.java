package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.stats.StatTriggers;
import com.verdantartifice.primalmagic.common.stats.StatsPM;

/**
 * Point of registration for mod stat triggers.
 * 
 * @author Daedalus4096
 */
public class InitStats {
    public static void initStats() {
        registerStatTriggers();
    }

    private static void registerStatTriggers() {
        StatTriggers.register(StatsPM.SPELLS_CAST, 10, SimpleResearchKey.parse("t_spells_cast_expert"));
        StatTriggers.register(StatsPM.SPELLS_CAST, 50, SimpleResearchKey.parse("t_spells_cast_master"));
        StatTriggers.register(StatsPM.SPELLS_CAST, 250, SimpleResearchKey.parse("t_spells_cast_supreme"));
        StatTriggers.register(StatsPM.SPELLS_CRAFTED, 1, SimpleResearchKey.parse("t_spells_crafted_expert"));
        StatTriggers.register(StatsPM.SPELLS_CRAFTED_MAX_COST, 50, SimpleResearchKey.parse("t_spell_cost_master"));
        StatTriggers.register(StatsPM.SPELLS_CRAFTED_MAX_COST, 250, SimpleResearchKey.parse("t_spell_cost_supreme"));
    }
}
