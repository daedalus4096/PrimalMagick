package com.verdantartifice.primalmagic.common.init;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.worldgen.features.FeatureConfigsPM;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;
import com.verdantartifice.primalmagic.common.worldgen.features.ShrineConfig;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Point of registration for mod worldgen features and structures.  Also performs actual worldgen changes
 * when called during common setup.
 * 
 * @author Daedalus4096
 */
public class InitWorldGen {
    public static void initWorldGen() {
        // Add sunwood and moonwood trees.
        BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).stream().forEach((biome) -> {
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(FeatureConfigsPM.SUNWOOD_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.func_227446_a_(new AtSurfaceWithExtraConfig(0, 0.1F, 1))));
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(FeatureConfigsPM.MOONWOOD_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.func_227446_a_(new AtSurfaceWithExtraConfig(0, 0.1F, 1))));
        });
    }
}
