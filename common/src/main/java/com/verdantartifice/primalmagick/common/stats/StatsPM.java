package com.verdantartifice.primalmagick.common.stats;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;

/**
 * Collection of statistics tracked by the mod.  The order of definition here is the order in which
 * they will be displayed in the grimoire.
 * 
 * @author Daedalus4096
 */
public class StatsPM {
    // Commonly used research icons
    private static final ResourceLocation ICON_BAG = PrimalMagick.resource("textures/research/research_bag.png");
    private static final ResourceLocation ICON_MAP = PrimalMagick.resource("textures/research/research_map.png");
    private static final ResourceLocation ICON_TUBE = PrimalMagick.resource("textures/research/research_tube.png");
    
    // Display stats
    public static final Stat GRIMOIRE_READ = Stat.builder("grimoire_read").icon(ICON_BAG).build();
    public static final Stat ITEMS_ANALYZED = Stat.builder("items_analyzed").icon(ICON_TUBE).build();
    public static final Stat ENTITIES_ANALYZED = Stat.builder("entities_analyzed").icon(ICON_TUBE).build();
    public static final Stat MANA_SIPHONED = Stat.builder("mana_siphoned").icon(ICON_MAP).build();
    public static final Stat OBSERVATIONS_MADE = Stat.builder("observations_made").icon(ICON_TUBE).hasHint().build();
    public static final Stat RESEARCH_PROJECTS_COMPLETED = Stat.builder("research_projects_completed").icon(ICON_TUBE).build();
    public static final Stat THEORIES_FORMED = Stat.builder("theories_formed").icon(ICON_TUBE).hasHint().build();
    public static final Stat EXPERTISE_MANAWEAVING = Stat.builder("expertise_manaweaving").icon(ICON_BAG).hasHint().build();
    public static final Stat EXPERTISE_ALCHEMY = Stat.builder("expertise_alchemy").icon(ICON_BAG).hasHint().build();
    public static final Stat EXPERTISE_SORCERY = Stat.builder("expertise_sorcery").icon(ICON_BAG).hasHint().build();
    public static final Stat EXPERTISE_RUNEWORKING = Stat.builder("expertise_runeworking").icon(ICON_BAG).hasHint().build();
    public static final Stat EXPERTISE_RITUAL = Stat.builder("expertise_ritual").icon(ICON_BAG).hasHint().build();
    public static final Stat EXPERTISE_MAGITECH = Stat.builder("expertise_magitech").icon(ICON_BAG).hasHint().build();
    public static final Stat CRAFTED_MANAWEAVING = Stat.builder("crafted_manaweaving").icon(ICON_BAG).build();
    public static final Stat CRAFTED_ALCHEMY = Stat.builder("crafted_alchemy").icon(ICON_BAG).build();
    public static final Stat CRAFTED_SORCERY = Stat.builder("crafted_sorcery").icon(ICON_BAG).build();
    public static final Stat CRAFTED_RUNEWORKING = Stat.builder("crafted_runeworking").icon(ICON_BAG).build();
    public static final Stat CRAFTED_RITUAL = Stat.builder("crafted_ritual").icon(ICON_BAG).build();
    public static final Stat CRAFTED_MAGITECH = Stat.builder("crafted_magitech").icon(ICON_BAG).build();
    public static final Stat SPELLS_CAST = Stat.builder("spells_cast").icon(ICON_TUBE).build();
    public static final Stat SPELLS_CRAFTED = Stat.builder("spells_crafted").icon(ICON_TUBE).build();
    public static final Stat SPELLS_CRAFTED_MAX_COST = Stat.builder("spells_crafted_max_cost").icon(ICON_TUBE).build();
    public static final Stat ITEMS_RUNESCRIBED = Stat.builder("items_runescribed").icon(ICON_TUBE).build();
    public static final Stat RITUALS_COMPLETED = Stat.builder("rituals_completed").icon(ICON_TUBE).build();
    public static final Stat RITUAL_MISHAPS = Stat.builder("ritual_mishaps").icon(ICON_TUBE).build();
    public static final Stat CONCOCTIONS_USED = Stat.builder("concoctions_used").icon(ICON_BAG).build();
    public static final Stat DISTANCE_TELEPORTED_CM = Stat.builder("distance_teleported_cm").formatter(StatFormatter.DISTANCE).icon(ICON_MAP).hasHint().build();
    public static final Stat MANA_SPENT_TOTAL = Stat.builder("mana_spent_total").icon(ICON_TUBE).hasHint().build();
    public static final Stat MANA_SPENT_EARTH = Stat.builder("mana_spent_earth").icon(ICON_TUBE).hasHint().build();
    public static final Stat MANA_SPENT_SEA = Stat.builder("mana_spent_sea").icon(ICON_TUBE).hasHint().build();
    public static final Stat MANA_SPENT_SKY = Stat.builder("mana_spent_sky").icon(ICON_TUBE).hasHint().build();
    public static final Stat MANA_SPENT_SUN = Stat.builder("mana_spent_sun").icon(ICON_TUBE).hasHint().build();
    public static final Stat MANA_SPENT_MOON = Stat.builder("mana_spent_moon").icon(ICON_TUBE).hasHint().build();
    public static final Stat MANA_SPENT_BLOOD = Stat.builder("mana_spent_blood").icon(ICON_TUBE).hidden().hasHint().build();
    public static final Stat MANA_SPENT_INFERNAL = Stat.builder("mana_spent_infernal").icon(ICON_TUBE).hidden().hasHint().build();
    public static final Stat MANA_SPENT_VOID = Stat.builder("mana_spent_void").icon(ICON_TUBE).hidden().hasHint().build();
    public static final Stat MANA_SPENT_HALLOWED = Stat.builder("mana_spent_hallowed").icon(ICON_TUBE).hidden().hasHint().build();
    public static final Stat SHRINE_FOUND_EARTH = Stat.builder("shrine_found_earth").icon(ICON_MAP).build();
    public static final Stat SHRINE_FOUND_SEA = Stat.builder("shrine_found_sea").icon(ICON_MAP).build();
    public static final Stat SHRINE_FOUND_SKY = Stat.builder("shrine_found_sky").icon(ICON_MAP).build();
    public static final Stat SHRINE_FOUND_SUN = Stat.builder("shrine_found_sun").icon(ICON_MAP).build();
    public static final Stat SHRINE_FOUND_MOON = Stat.builder("shrine_found_moon").icon(ICON_MAP).build();
    public static final Stat ANCIENT_BOOKS_READ = Stat.builder("ancient_books_read").icon(ICON_BAG).build();
    public static final Stat VOCABULARY_STUDIED = Stat.builder("vocabulary_studied").icon(ICON_BAG).build();
    public static final Stat BLOCKS_BROKEN_BAREHANDED = Stat.builder("blocks_broken_barehanded").icon(ICON_MAP).hasHint().build();
    public static final Stat TREANTS_NAMED = Stat.builder("treants_named").icon(ICON_MAP).build();
}
