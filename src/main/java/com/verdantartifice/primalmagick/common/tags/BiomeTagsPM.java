package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * Collection of custom-defined biome tags for the mod.  Used to determine tag contents and for
 * data file generation.
 * 
 * @author Daedalus4096
 */
public class BiomeTagsPM {
    public static final TagKey<Biome> HAS_EARTH_SHRINE = tag("has_structure/earth_shrine");
    public static final TagKey<Biome> HAS_SEA_SHRINE = tag("has_structure/sea_shrine");
    public static final TagKey<Biome> HAS_SKY_SHRINE = tag("has_structure/sky_shrine");
    public static final TagKey<Biome> HAS_SUN_SHRINE = tag("has_structure/sun_shrine");
    public static final TagKey<Biome> HAS_MOON_SHRINE = tag("has_structure/moon_shrine");
    
    public static final TagKey<Biome> HAS_MARBLE = tag("has_feature/ore_marble_raw");
    public static final TagKey<Biome> HAS_ROCK_SALT = tag("has_feature/ore_rock_salt");
    public static final TagKey<Biome> HAS_QUARTZ = tag("has_feature/ore_quartz");
    public static final TagKey<Biome> HAS_WILD_SUNWOOD = tag("has_feature/tree_wild_sunwood");
    public static final TagKey<Biome> HAS_WILD_MOONWOOD = tag("has_feature/tree_wild_moonwood");
    
    public static final TagKey<Biome> HAS_TREEFOLK = tag("has_spawn/treefolk");
    
    private static TagKey<Biome> tag(String name) {
        return TagKey.create(Registries.BIOME, PrimalMagick.resource(name));
    }
}
