package com.verdantartifice.primalmagick.common.stats;

import net.minecraft.stats.StatFormatter;

/**
 * Collection of statistics tracked by the mod.  The order of definition here is the order in which
 * they will be displayed in the grimoire.
 * 
 * @author Daedalus4096
 */
public class StatsPM {
    // Display stats
    public static final Stat GRIMOIRE_READ = Stat.create("grimoire_read", StatFormatter.DEFAULT, false);
    public static final Stat ITEMS_ANALYZED = Stat.create("items_analyzed", StatFormatter.DEFAULT, false);
    public static final Stat ENTITIES_ANALYZED = Stat.create("entities_analyzed", StatFormatter.DEFAULT, false);
    public static final Stat MANA_SIPHONED = Stat.create("mana_siphoned", StatFormatter.DEFAULT, false);
    public static final Stat OBSERVATIONS_MADE = Stat.create("observations_made", StatFormatter.DEFAULT, false);
    public static final Stat RESEARCH_PROJECTS_COMPLETED = Stat.create("research_projects_completed", StatFormatter.DEFAULT, false);
    public static final Stat THEORIES_FORMED = Stat.create("theories_formed", StatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_MANAWEAVING = Stat.create("crafted_manaweaving", StatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_ALCHEMY = Stat.create("crafted_alchemy", StatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_SORCERY = Stat.create("crafted_sorcery", StatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_RUNEWORKING = Stat.create("crafted_runeworking", StatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_RITUAL = Stat.create("crafted_ritual", StatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_MAGITECH = Stat.create("crafted_magitech", StatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CAST = Stat.create("spells_cast", StatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CRAFTED = Stat.create("spells_crafted", StatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CRAFTED_MAX_COST = Stat.create("spells_crafted_max_cost", StatFormatter.DEFAULT, false);
    public static final Stat ITEMS_RUNESCRIBED = Stat.create("items_runescribed", StatFormatter.DEFAULT, false);
    public static final Stat RITUALS_COMPLETED = Stat.create("rituals_completed", StatFormatter.DEFAULT, false);
    public static final Stat RITUAL_MISHAPS = Stat.create("ritual_mishaps", StatFormatter.DEFAULT, false);
    public static final Stat CONCOCTIONS_USED = Stat.create("concoctions_used", StatFormatter.DEFAULT, false);
    public static final Stat DISTANCE_TELEPORTED_CM = Stat.create("distance_teleported_cm", StatFormatter.DISTANCE, false);
    public static final Stat MANA_SPENT_TOTAL = Stat.create("mana_spent_total", StatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_EARTH = Stat.create("mana_spent_earth", StatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_SEA = Stat.create("mana_spent_sea", StatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_SKY = Stat.create("mana_spent_sky", StatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_SUN = Stat.create("mana_spent_sun", StatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_MOON = Stat.create("mana_spent_moon", StatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_BLOOD = Stat.create("mana_spent_blood", StatFormatter.DEFAULT, true);
    public static final Stat MANA_SPENT_INFERNAL = Stat.create("mana_spent_infernal", StatFormatter.DEFAULT, true);
    public static final Stat MANA_SPENT_VOID = Stat.create("mana_spent_void", StatFormatter.DEFAULT, true);
    public static final Stat MANA_SPENT_HALLOWED = Stat.create("mana_spent_hallowed", StatFormatter.DEFAULT, true);
    public static final Stat SHRINE_FOUND_EARTH = Stat.create("shrine_found_earth", StatFormatter.DEFAULT, false);
    public static final Stat SHRINE_FOUND_SEA = Stat.create("shrine_found_sea", StatFormatter.DEFAULT, false);
    public static final Stat SHRINE_FOUND_SKY = Stat.create("shrine_found_sky", StatFormatter.DEFAULT, false);
    public static final Stat SHRINE_FOUND_SUN = Stat.create("shrine_found_sun", StatFormatter.DEFAULT, false);
    public static final Stat SHRINE_FOUND_MOON = Stat.create("shrine_found_moon", StatFormatter.DEFAULT, false);
    public static final Stat BLOCKS_BROKEN_BAREHANDED = Stat.create("blocks_broken_barehanded", StatFormatter.DEFAULT, false);
    public static final Stat SHEARS_USED = Stat.create("shears_used", StatFormatter.DEFAULT, false);
    public static final Stat TREANTS_NAMED = Stat.create("treants_named", StatFormatter.DEFAULT, false);
}
