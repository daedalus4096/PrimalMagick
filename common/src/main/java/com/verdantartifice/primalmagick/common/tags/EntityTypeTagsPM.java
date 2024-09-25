package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

/**
 * Collection of custom-defined entity type tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class EntityTypeTagsPM {
    public static final TagKey<EntityType<?>> ENCHANTED_GOLEMS = tag("enchanted_golems");
    public static final TagKey<EntityType<?>> PIXIES = tag("pixies");
    
    public static final TagKey<EntityType<?>> BLOOD_ROSE_IMMUNE = tag("blood_rose_immune");
    public static final TagKey<EntityType<?>> FLYING_CREATURES = tag("flying_creatures");
    public static final TagKey<EntityType<?>> GOLEMS = tag("golems");
    
    public static final TagKey<EntityType<?>> POLYMORPH_ALLOW = tag("polymorph_allow");
    public static final TagKey<EntityType<?>> POLYMORPH_BAN = tag("polymorph_ban");
    
    public static final TagKey<EntityType<?>> DROPS_BLOODY_FLESH = tag("drops_bloody_flesh");
    public static final TagKey<EntityType<?>> DROPS_BLOOD_NOTES_HIGH = tag("drops_blood_notes_high");
    public static final TagKey<EntityType<?>> DROPS_BLOOD_NOTES_LOW = tag("drops_blood_notes_low");
    public static final TagKey<EntityType<?>> DROPS_RELIC_FRAGMENTS_HIGH = tag("drops_relic_fragments_high");
    public static final TagKey<EntityType<?>> DROPS_RELIC_FRAGMENTS_LOW = tag("drops_relic_fragments_low");
    
    public static final TagKey<EntityType<?>> GUILLOTINE_ZOMBIE_HEAD = tag("guillotine/zombie_head");
    public static final TagKey<EntityType<?>> GUILLOTINE_SKELETON_SKULL = tag("guillotine/skeleton_skull");
    public static final TagKey<EntityType<?>> GUILLOTINE_CREEPER_HEAD = tag("guillotine/creeper_head");
    public static final TagKey<EntityType<?>> GUILLOTINE_DRAGON_HEAD = tag("guillotine/dragon_head");
    public static final TagKey<EntityType<?>> GUILLOTINE_WITHER_SKELETON_SKULL = tag("guillotine/wither_skeleton_skull");
    public static final TagKey<EntityType<?>> GUILLOTINE_PIGLIN_HEAD = tag("guillotine/piglin_head");
    public static final TagKey<EntityType<?>> GUILLOTINE_PLAYER_HEAD = tag("guillotine/player_head");

    private static TagKey<EntityType<?>> tag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, PrimalMagick.resource(name));
    }
}
