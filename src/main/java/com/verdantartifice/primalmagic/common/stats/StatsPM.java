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
    public static final Stat ITEMS_ANALYZED = Stat.create("items_analyzed", IStatFormatter.DEFAULT, false);
    public static final Stat ENTITIES_ANALYZED = Stat.create("entities_analyzed", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_MANAWEAVING = Stat.create("crafted_manaweaving", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_ALCHEMY = Stat.create("crafted_alchemy", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_SORCERY = Stat.create("crafted_sorcery", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_RUNEWORKING = Stat.create("crafted_runeworking", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_RITUAL = Stat.create("crafted_ritual", IStatFormatter.DEFAULT, false);
    public static final Stat CRAFTED_MAGITECH = Stat.create("crafted_magitech", IStatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CAST = Stat.create("spells_cast", IStatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CRAFTED = Stat.create("spells_crafted", IStatFormatter.DEFAULT, false);
    public static final Stat SPELLS_CRAFTED_MAX_COST = Stat.create("spells_crafted_max_cost", IStatFormatter.DEFAULT, false);
    public static final Stat RESEARCH_PROJECTS_COMPLETED = Stat.create("research_projects_completed", IStatFormatter.DEFAULT, false);
    public static final Stat ITEMS_RUNESCRIBED = Stat.create("items_runescribed", IStatFormatter.DEFAULT, false);
    public static final Stat RITUALS_COMPLETED = Stat.create("rituals_completed", IStatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_EARTH = Stat.create("mana_spent_earth", IStatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_SEA = Stat.create("mana_spent_sea", IStatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_SKY = Stat.create("mana_spent_sky", IStatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_SUN = Stat.create("mana_spent_sun", IStatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_MOON = Stat.create("mana_spent_moon", IStatFormatter.DEFAULT, false);
    public static final Stat MANA_SPENT_BLOOD = Stat.create("mana_spent_blood", IStatFormatter.DEFAULT, true);
    public static final Stat MANA_SPENT_INFERNAL = Stat.create("mana_spent_infernal", IStatFormatter.DEFAULT, true);
    public static final Stat MANA_SPENT_VOID = Stat.create("mana_spent_void", IStatFormatter.DEFAULT, true);
    public static final Stat MANA_SPENT_HALLOWED = Stat.create("mana_spent_hallowed", IStatFormatter.DEFAULT, true);
    public static final Stat SHRINE_FOUND_EARTH = Stat.create("shrine_found_earth", IStatFormatter.DEFAULT, false);
    public static final Stat SHRINE_FOUND_SEA = Stat.create("shrine_found_sea", IStatFormatter.DEFAULT, false);
    public static final Stat SHRINE_FOUND_SKY = Stat.create("shrine_found_sky", IStatFormatter.DEFAULT, false);
    public static final Stat SHRINE_FOUND_SUN = Stat.create("shrine_found_sun", IStatFormatter.DEFAULT, false);
    public static final Stat SHRINE_FOUND_MOON = Stat.create("shrine_found_moon", IStatFormatter.DEFAULT, false);
    public static final Stat TREANTS_NAMED = Stat.create("treants_named", IStatFormatter.DEFAULT, false);
}
