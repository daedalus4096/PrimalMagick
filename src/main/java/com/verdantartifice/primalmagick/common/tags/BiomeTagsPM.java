package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * Collection of custom-defined biome tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class BiomeTagsPM {
    public static final TagKey<Biome> IS_END = tag("is_end");
    
    private static TagKey<Biome> tag(String name) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(PrimalMagick.MODID, name));
    }
}
