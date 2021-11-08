package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

/**
 * Collection of custom-defined entity type tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class EntityTypeTagsPM {
    public static final IOptionalNamedTag<EntityType<?>> ENCHANTED_GOLEMS = tag("enchanted_golems");
    public static final IOptionalNamedTag<EntityType<?>> PIXIES = tag("pixies");
    
    public static final IOptionalNamedTag<EntityType<?>> FLYING_CREATURES = tag("flying_creatures");
    public static final IOptionalNamedTag<EntityType<?>> GOLEMS = tag("golems");
    
    public static final IOptionalNamedTag<EntityType<?>> DROPS_BLOODY_FLESH = tag("drops_bloody_flesh");
    public static final IOptionalNamedTag<EntityType<?>> DROPS_BLOOD_NOTES_HIGH = tag("drops_blood_notes_high");
    public static final IOptionalNamedTag<EntityType<?>> DROPS_BLOOD_NOTES_LOW = tag("drops_blood_notes_low");
    public static final IOptionalNamedTag<EntityType<?>> DROPS_RELIC_FRAGMENTS_HIGH = tag("drops_relic_fragments_high");
    public static final IOptionalNamedTag<EntityType<?>> DROPS_RELIC_FRAGMENTS_LOW = tag("drops_relic_fragments_low");

    private static IOptionalNamedTag<EntityType<?>> tag(String name) {
        return EntityTypeTags.createOptional(new ResourceLocation(PrimalMagick.MODID, name));
    }
}
