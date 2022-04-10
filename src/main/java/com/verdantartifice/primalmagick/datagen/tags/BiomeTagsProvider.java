package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.tags.BiomeTagsPM;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all of the mod's biome tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class BiomeTagsProvider extends TagsProvider<Biome> {
    @SuppressWarnings("deprecation")
    public BiomeTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, BuiltinRegistries.BIOME, PrimalMagick.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Primal Magick Biome Tags";
    }

    @Override
    protected void addTags() {
        // Create custom tags
        this.tag(BiomeTagsPM.IS_END).add(Biomes.THE_END, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS);
        this.tag(BiomeTagsPM.HAS_EARTH_SHRINE).add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);
        this.tag(BiomeTagsPM.HAS_SEA_SHRINE).addTag(BiomeTags.IS_RIVER).addTag(BiomeTags.IS_BEACH).add(Biomes.SWAMP, Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES);
        this.tag(BiomeTagsPM.HAS_SKY_SHRINE).addTag(BiomeTags.IS_HILL).addTag(BiomeTags.IS_MOUNTAIN);
        this.tag(BiomeTagsPM.HAS_SUN_SHRINE).addTag(BiomeTags.IS_BADLANDS).add(Biomes.DESERT);
        this.tag(BiomeTagsPM.HAS_MOON_SHRINE).addTag(BiomeTags.IS_FOREST);
    }
}
