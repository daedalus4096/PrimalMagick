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
        StatTriggers.register(StatsPM.SHRINE_FOUND_EARTH, 1, SimpleResearchKey.parse("m_found_shrine_earth"));
        StatTriggers.register(StatsPM.SHRINE_FOUND_SEA, 1, SimpleResearchKey.parse("m_found_shrine_sea"));
        StatTriggers.register(StatsPM.SHRINE_FOUND_SKY, 1, SimpleResearchKey.parse("m_found_shrine_sky"));
        StatTriggers.register(StatsPM.SHRINE_FOUND_SUN, 1, SimpleResearchKey.parse("m_found_shrine_sun"));
        StatTriggers.register(StatsPM.SHRINE_FOUND_MOON, 1, SimpleResearchKey.parse("m_found_shrine_moon"));
        StatTriggers.register(StatsPM.MANA_SPENT_EARTH, 100, SimpleResearchKey.parse("t_mana_spent_earth_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_SEA, 100, SimpleResearchKey.parse("t_mana_spent_sea_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_SKY, 100, SimpleResearchKey.parse("t_mana_spent_sky_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_SUN, 100, SimpleResearchKey.parse("t_mana_spent_sun_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_MOON, 100, SimpleResearchKey.parse("t_mana_spent_moon_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_BLOOD, 100, SimpleResearchKey.parse("t_mana_spent_blood_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_INFERNAL, 100, SimpleResearchKey.parse("t_mana_spent_infernal_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_VOID, 100, SimpleResearchKey.parse("t_mana_spent_void_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_HALLOWED, 100, SimpleResearchKey.parse("t_mana_spent_hallowed_expert"));
        StatTriggers.register(StatsPM.CRAFTED_ALCHEMY, 50, SimpleResearchKey.parse("b_crafted_alchemy_expert"));
        StatTriggers.register(StatsPM.CRAFTED_ALCHEMY, 250, SimpleResearchKey.parse("b_crafted_alchemy_master"));
        StatTriggers.register(StatsPM.CRAFTED_ALCHEMY, 1000, SimpleResearchKey.parse("b_crafted_alchemy_supreme"));
        StatTriggers.register(StatsPM.CRAFTED_MANAWEAVING, 10, SimpleResearchKey.parse("b_crafted_manaweaving_expert"));
        StatTriggers.register(StatsPM.CRAFTED_MANAWEAVING, 50, SimpleResearchKey.parse("b_crafted_manaweaving_master"));
        StatTriggers.register(StatsPM.CRAFTED_MANAWEAVING, 250, SimpleResearchKey.parse("b_crafted_manaweaving_supreme"));
        StatTriggers.register(StatsPM.CRAFTED_RUNEWORKING, 10, SimpleResearchKey.parse("b_crafted_runeworking_expert"));
        StatTriggers.register(StatsPM.CRAFTED_RUNEWORKING, 50, SimpleResearchKey.parse("b_crafted_runeworking_master"));
        StatTriggers.register(StatsPM.CRAFTED_RUNEWORKING, 250, SimpleResearchKey.parse("b_crafted_runeworking_supreme"));
        StatTriggers.register(StatsPM.RITUALS_COMPLETED, 5, SimpleResearchKey.parse("t_rituals_completed_expert"));
        StatTriggers.register(StatsPM.RITUALS_COMPLETED, 25, SimpleResearchKey.parse("t_rituals_completed_master"));
        StatTriggers.register(StatsPM.RITUALS_COMPLETED, 100, SimpleResearchKey.parse("t_rituals_completed_supreme"));
        StatTriggers.register(StatsPM.CRAFTED_MAGITECH, 10, SimpleResearchKey.parse("b_crafted_magitech_expert"));
        StatTriggers.register(StatsPM.CRAFTED_MAGITECH, 50, SimpleResearchKey.parse("b_crafted_magitech_master"));
        StatTriggers.register(StatsPM.CRAFTED_MAGITECH, 250, SimpleResearchKey.parse("b_crafted_magitech_supreme"));
        StatTriggers.register(StatsPM.ITEMS_RUNESCRIBED, 2, SimpleResearchKey.parse("t_items_runescribed_expert"));
        StatTriggers.register(StatsPM.ITEMS_RUNESCRIBED, 10, SimpleResearchKey.parse("t_items_runescribed_master"));
        StatTriggers.register(StatsPM.ITEMS_RUNESCRIBED, 50, SimpleResearchKey.parse("t_items_runescribed_supreme"));
        StatTriggers.register(StatsPM.RESEARCH_PROJECTS_COMPLETED, 10, SimpleResearchKey.parse("t_research_projects_completed"));
        StatTriggers.register(StatsPM.ITEMS_ANALYZED, 25, SimpleResearchKey.parse("t_items_analyzed"));
    }
}
