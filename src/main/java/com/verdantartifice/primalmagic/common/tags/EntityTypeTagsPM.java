package com.verdantartifice.primalmagic.common.tags;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ResourceLocation;
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

    private static IOptionalNamedTag<EntityType<?>> tag(String name) {
        return EntityTypeTags.createOptional(new ResourceLocation(PrimalMagic.MODID, name));
    }
}
