package com.verdantartifice.primalmagic.common.init;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.worldgen.features.FeatureConfigsPM;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;
import com.verdantartifice.primalmagic.common.worldgen.features.ShrineConfig;
import com.verdantartifice.primalmagic.common.worldgen.features.ShrineStructure;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Point of registration for mod worldgen features and structures.  Also performs actual worldgen changes
 * when called during common setup.
 * 
 * @author Daedalus4096
 */
public class InitWorldGen {
    public static void initFeatures(IForgeRegistry<Feature<?>> registry) {
        registry.register(new ShrineStructure(ShrineConfig::deserialize).setRegistryName(PrimalMagic.MODID, "shrine"));
    }

    public static void initWorldGen() {
        // Add raw marble seams to all non-Nether, non-End biomes
        ForgeRegistries.BIOMES.getValues().stream().filter(InitWorldGen::shouldSpawnMarble).forEach((biome) -> {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlocksPM.MARBLE_RAW.getDefaultState(), 33)).withPlacement(Placement.COUNT_RANGE.func_227446_a_(new CountRangeConfig(10, 0, 0, 80))));
        });
        
        // Add Earth shrines to plains and savanna type biomes
        BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS).stream().forEach((biome) -> {
            addShrine(biome, Source.EARTH);
        });
        BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA).stream().forEach((biome) -> {
            addShrine(biome, Source.EARTH);
        });
        
        // Add Sea shrines to river, beach, and swamp type biomes
        BiomeDictionary.getBiomes(BiomeDictionary.Type.RIVER).stream().forEach((biome) -> {
            addShrine(biome, Source.SEA);
        });
        BiomeDictionary.getBiomes(BiomeDictionary.Type.BEACH).stream().forEach((biome) -> {
            addShrine(biome, Source.SEA);
        });
        BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP).stream().forEach((biome) -> {
            addShrine(biome, Source.SEA);
        });
        
        // Add Sky shrines to mountain and hill type biomes
        BiomeDictionary.getBiomes(BiomeDictionary.Type.MOUNTAIN).stream().forEach((biome) -> {
            addShrine(biome, Source.SKY);
        });
        BiomeDictionary.getBiomes(BiomeDictionary.Type.HILLS).stream().forEach((biome) -> {
            addShrine(biome, Source.SKY);
        });
        
        // Add Sun shrines to hot+dry and sandy type biomes
        BiomeDictionary.getBiomes(BiomeDictionary.Type.HOT).stream().filter((biome) -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY)).forEach((biome) -> {
            addShrine(biome, Source.SUN);
        });
        BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY).stream().forEach((biome) -> {
            addShrine(biome, Source.SUN);
        });
        
        // Add Moon shrines to forest type biomes.  Also add sunwood and moonwood trees.
        BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).stream().forEach((biome) -> {
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(FeatureConfigsPM.SUNWOOD_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.func_227446_a_(new AtSurfaceWithExtraConfig(0, 0.1F, 1))));
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(FeatureConfigsPM.MOONWOOD_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.func_227446_a_(new AtSurfaceWithExtraConfig(0, 0.1F, 1))));
            addShrine(biome, Source.MOON);
        });
    }

    private static boolean shouldSpawnMarble(@Nonnull Biome biome) {
        if (biome.equals(Biomes.STONE_SHORE)) {
            // Stone Shore has a category of None, but it still has default stone types
            return true;
        } else {
            Biome.Category cat = biome.getCategory();
            return !Biome.Category.NONE.equals(cat) && !Biome.Category.NETHER.equals(cat) && !Biome.Category.THEEND.equals(cat);
        }
    }
    
    private static void addShrine(@Nonnull Biome biome, @Nonnull Source source) {
        // Structures must be added as both a structure and a feature of the biome in order to spawn
        ShrineConfig config = new ShrineConfig(source);
        biome.addStructure(FeaturesPM.SHRINE.withConfiguration(config));
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, FeaturesPM.SHRINE.withConfiguration(config).withPlacement(Placement.NOPE.func_227446_a_(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }
}
