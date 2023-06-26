package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.tags.BiomeTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Data provider for all of the mod's biome tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class BiomeTagsProviderPM extends IntrinsicHolderTagsProvider<Biome> {
    public BiomeTagsProviderPM(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.BIOME, lookupProvider, b -> ForgeRegistries.BIOMES.getResourceKey(b).orElseThrow(), PrimalMagick.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Primal Magick Biome Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // Create custom tags
        this.tag(BiomeTagsPM.HAS_EARTH_SHRINE).add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);
        this.tag(BiomeTagsPM.HAS_SEA_SHRINE).addTag(BiomeTags.IS_RIVER).addTag(BiomeTags.IS_BEACH).add(Biomes.SWAMP, Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES);
        this.tag(BiomeTagsPM.HAS_SKY_SHRINE).addTag(BiomeTags.IS_HILL).addTag(BiomeTags.IS_MOUNTAIN);
        this.tag(BiomeTagsPM.HAS_SUN_SHRINE).addTag(BiomeTags.IS_BADLANDS).add(Biomes.DESERT);
        this.tag(BiomeTagsPM.HAS_MOON_SHRINE).addTag(BiomeTags.IS_FOREST);
    }
}