package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.stats.StatTriggers;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

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
        StatTriggers.register(StatsPM.SPELLS_CAST, 10, SimpleResearchKey.find("t_spells_cast_expert"));
        StatTriggers.register(StatsPM.SPELLS_CAST, 50, SimpleResearchKey.find("t_spells_cast_master"));
        StatTriggers.register(StatsPM.SPELLS_CAST, 250, SimpleResearchKey.find("t_spells_cast_supreme"));
        StatTriggers.register(StatsPM.SPELLS_CRAFTED, 1, SimpleResearchKey.find("t_spells_crafted_expert"));
        StatTriggers.register(StatsPM.SPELLS_CRAFTED_MAX_COST, 50, SimpleResearchKey.find("t_spell_cost_master"));
        StatTriggers.register(StatsPM.SPELLS_CRAFTED_MAX_COST, 250, SimpleResearchKey.find("t_spell_cost_supreme"));
        StatTriggers.register(StatsPM.SHRINE_FOUND_EARTH, 1, SimpleResearchKey.find("m_found_shrine_earth"));
        StatTriggers.register(StatsPM.SHRINE_FOUND_SEA, 1, SimpleResearchKey.find("m_found_shrine_sea"));
        StatTriggers.register(StatsPM.SHRINE_FOUND_SKY, 1, SimpleResearchKey.find("m_found_shrine_sky"));
        StatTriggers.register(StatsPM.SHRINE_FOUND_SUN, 1, SimpleResearchKey.find("m_found_shrine_sun"));
        StatTriggers.register(StatsPM.SHRINE_FOUND_MOON, 1, SimpleResearchKey.find("m_found_shrine_moon"));
        StatTriggers.register(StatsPM.MANA_SPENT_EARTH, 100, SimpleResearchKey.find("t_mana_spent_earth_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_SEA, 100, SimpleResearchKey.find("t_mana_spent_sea_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_SKY, 100, SimpleResearchKey.find("t_mana_spent_sky_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_SUN, 100, SimpleResearchKey.find("t_mana_spent_sun_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_MOON, 100, SimpleResearchKey.find("t_mana_spent_moon_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_BLOOD, 100, SimpleResearchKey.find("t_mana_spent_blood_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_INFERNAL, 100, SimpleResearchKey.find("t_mana_spent_infernal_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_VOID, 100, SimpleResearchKey.find("t_mana_spent_void_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_HALLOWED, 100, SimpleResearchKey.find("t_mana_spent_hallowed_expert"));
        StatTriggers.register(StatsPM.CRAFTED_ALCHEMY, 50, SimpleResearchKey.find("b_crafted_alchemy_expert"));
        StatTriggers.register(StatsPM.CRAFTED_ALCHEMY, 250, SimpleResearchKey.find("b_crafted_alchemy_master"));
        StatTriggers.register(StatsPM.CRAFTED_ALCHEMY, 1000, SimpleResearchKey.find("b_crafted_alchemy_supreme"));
        StatTriggers.register(StatsPM.CRAFTED_MANAWEAVING, 10, SimpleResearchKey.find("b_crafted_manaweaving_expert"));
        StatTriggers.register(StatsPM.CRAFTED_MANAWEAVING, 50, SimpleResearchKey.find("b_crafted_manaweaving_master"));
        StatTriggers.register(StatsPM.CRAFTED_MANAWEAVING, 250, SimpleResearchKey.find("b_crafted_manaweaving_supreme"));
        StatTriggers.register(StatsPM.CRAFTED_RUNEWORKING, 10, SimpleResearchKey.find("b_crafted_runeworking_expert"));
        StatTriggers.register(StatsPM.CRAFTED_RUNEWORKING, 50, SimpleResearchKey.find("b_crafted_runeworking_master"));
        StatTriggers.register(StatsPM.CRAFTED_RUNEWORKING, 250, SimpleResearchKey.find("b_crafted_runeworking_supreme"));
        StatTriggers.register(StatsPM.RITUALS_COMPLETED, 2, SimpleResearchKey.find("t_rituals_completed_expert"));
        StatTriggers.register(StatsPM.RITUALS_COMPLETED, 10, SimpleResearchKey.find("t_rituals_completed_master"));
        StatTriggers.register(StatsPM.RITUALS_COMPLETED, 50, SimpleResearchKey.find("t_rituals_completed_supreme"));
        StatTriggers.register(StatsPM.RITUAL_MISHAPS, 1, SimpleResearchKey.find("t_ritual_mishaps_basic"));
        StatTriggers.register(StatsPM.CRAFTED_MAGITECH, 5, SimpleResearchKey.find("b_crafted_magitech_expert"));
        StatTriggers.register(StatsPM.CRAFTED_MAGITECH, 25, SimpleResearchKey.find("b_crafted_magitech_master"));
        StatTriggers.register(StatsPM.CRAFTED_MAGITECH, 100, SimpleResearchKey.find("b_crafted_magitech_supreme"));
        StatTriggers.register(StatsPM.ITEMS_RUNESCRIBED, 2, SimpleResearchKey.find("t_items_runescribed_expert"));
        StatTriggers.register(StatsPM.ITEMS_RUNESCRIBED, 10, SimpleResearchKey.find("t_items_runescribed_master"));
        StatTriggers.register(StatsPM.ITEMS_RUNESCRIBED, 50, SimpleResearchKey.find("t_items_runescribed_supreme"));
        StatTriggers.register(StatsPM.RESEARCH_PROJECTS_COMPLETED, 10, SimpleResearchKey.find("t_research_projects_completed"));
        StatTriggers.register(StatsPM.ITEMS_ANALYZED, 25, SimpleResearchKey.find("t_items_analyzed"));
        StatTriggers.register(StatsPM.OBSERVATIONS_MADE, 1, SimpleResearchKey.find("t_observations_made_basics"));
        StatTriggers.register(StatsPM.OBSERVATIONS_MADE, 25, SimpleResearchKey.find("t_observations_made_expert"));
        StatTriggers.register(StatsPM.THEORIES_FORMED, 1, SimpleResearchKey.find("t_theories_formed_basics"));
        StatTriggers.register(StatsPM.BLOCKS_BROKEN_BAREHANDED, 50, SimpleResearchKey.find("m_blocks_broken_barehanded_expert"));
        StatTriggers.register(StatsPM.DISTANCE_TELEPORTED_CM, 10000, SimpleResearchKey.find("m_teleport_a_lot"));
        StatTriggers.register(StatsPM.MANA_SIPHONED, 10, SimpleResearchKey.find("t_mana_siphoned_basics"));
        StatTriggers.register(StatsPM.MANA_SIPHONED, 1000, SimpleResearchKey.find("t_mana_siphoned_expert"));
        StatTriggers.register(StatsPM.MANA_SPENT_TOTAL, 20, SimpleResearchKey.find("t_mana_spent_total_basics"));
        StatTriggers.register(StatsPM.SHEARS_USED, 50, SimpleResearchKey.find("b_shears_used_expert"));
    }
}
