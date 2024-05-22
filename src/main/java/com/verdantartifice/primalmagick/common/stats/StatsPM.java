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
    public static final Stat GRIMOIRE_READ = Stat.builder("grimoire_read").build();
    public static final Stat ITEMS_ANALYZED = Stat.builder("items_analyzed").build();
    public static final Stat ENTITIES_ANALYZED = Stat.builder("entities_analyzed").build();
    public static final Stat MANA_SIPHONED = Stat.builder("mana_siphoned").build();
    public static final Stat OBSERVATIONS_MADE = Stat.builder("observations_made").build();
    public static final Stat RESEARCH_PROJECTS_COMPLETED = Stat.builder("research_projects_completed").build();
    public static final Stat THEORIES_FORMED = Stat.builder("theories_formed").build();
    public static final Stat CRAFTED_MANAWEAVING = Stat.builder("crafted_manaweaving").build();
    public static final Stat CRAFTED_ALCHEMY = Stat.builder("crafted_alchemy").build();
    public static final Stat CRAFTED_SORCERY = Stat.builder("crafted_sorcery").build();
    public static final Stat CRAFTED_RUNEWORKING = Stat.builder("crafted_runeworking").build();
    public static final Stat CRAFTED_RITUAL = Stat.builder("crafted_ritual").build();
    public static final Stat CRAFTED_MAGITECH = Stat.builder("crafted_magitech").build();
    public static final Stat SPELLS_CAST = Stat.builder("spells_cast").build();
    public static final Stat SPELLS_CRAFTED = Stat.builder("spells_crafted").build();
    public static final Stat SPELLS_CRAFTED_MAX_COST = Stat.builder("spells_crafted_max_cost").build();
    public static final Stat ITEMS_RUNESCRIBED = Stat.builder("items_runescribed").build();
    public static final Stat RITUALS_COMPLETED = Stat.builder("rituals_completed").build();
    public static final Stat RITUAL_MISHAPS = Stat.builder("ritual_mishaps").build();
    public static final Stat CONCOCTIONS_USED = Stat.builder("concoctions_used").build();
    public static final Stat DISTANCE_TELEPORTED_CM = Stat.builder("distance_teleported_cm").formatter(StatFormatter.DISTANCE).build();
    public static final Stat MANA_SPENT_TOTAL = Stat.builder("mana_spent_total").build();
    public static final Stat MANA_SPENT_EARTH = Stat.builder("mana_spent_earth").build();
    public static final Stat MANA_SPENT_SEA = Stat.builder("mana_spent_sea").build();
    public static final Stat MANA_SPENT_SKY = Stat.builder("mana_spent_sky").build();
    public static final Stat MANA_SPENT_SUN = Stat.builder("mana_spent_sun").build();
    public static final Stat MANA_SPENT_MOON = Stat.builder("mana_spent_moon").build();
    public static final Stat MANA_SPENT_BLOOD = Stat.builder("mana_spent_blood").hidden().build();
    public static final Stat MANA_SPENT_INFERNAL = Stat.builder("mana_spent_infernal").hidden().build();
    public static final Stat MANA_SPENT_VOID = Stat.builder("mana_spent_void").hidden().build();
    public static final Stat MANA_SPENT_HALLOWED = Stat.builder("mana_spent_hallowed").hidden().build();
    public static final Stat SHRINE_FOUND_EARTH = Stat.builder("shrine_found_earth").build();
    public static final Stat SHRINE_FOUND_SEA = Stat.builder("shrine_found_sea").build();
    public static final Stat SHRINE_FOUND_SKY = Stat.builder("shrine_found_sky").build();
    public static final Stat SHRINE_FOUND_SUN = Stat.builder("shrine_found_sun").build();
    public static final Stat SHRINE_FOUND_MOON = Stat.builder("shrine_found_moon").build();
    public static final Stat ANCIENT_BOOKS_READ = Stat.builder("ancient_books_read").hidden().build();   // FIXME Make non-hidden for 1.21 release
    public static final Stat VOCABULARY_STUDIED = Stat.builder("vocabulary_studied").hidden().build();   // FIXME Make non-hidden for 1.21 release
    public static final Stat BLOCKS_BROKEN_BAREHANDED = Stat.builder("blocks_broken_barehanded").build();
    public static final Stat SHEARS_USED = Stat.builder("shears_used").build();
    public static final Stat TREANTS_NAMED = Stat.builder("treants_named").build();
}
