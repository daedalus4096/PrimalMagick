package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.tags.BiomeTagsPM;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
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
    }
}
