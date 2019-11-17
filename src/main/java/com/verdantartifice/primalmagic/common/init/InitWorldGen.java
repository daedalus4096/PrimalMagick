package com.verdantartifice.primalmagic.common.init;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;
import com.verdantartifice.primalmagic.common.worldgen.features.MoonwoodTreeFeature;
import com.verdantartifice.primalmagic.common.worldgen.features.SunwoodTreeFeature;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class InitWorldGen {
    public static void initFeatures(IForgeRegistry<Feature<?>> registry) {
        registry.register(new SunwoodTreeFeature(NoFeatureConfig::deserialize, false).setRegistryName(PrimalMagic.MODID, "sunwood_tree"));
        registry.register(new MoonwoodTreeFeature(NoFeatureConfig::deserialize, false).setRegistryName(PrimalMagic.MODID, "moonwood_tree"));
    }

    public static void initWorldGen() {
        ForgeRegistries.BIOMES.getValues().stream().filter(InitWorldGen::shouldSpawnMarble).forEach((biome) -> {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlocksPM.MARBLE_RAW.getDefaultState(), 33), Placement.COUNT_RANGE, new CountRangeConfig(10, 0, 0, 80)));
        });
        BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).stream().forEach((biome) -> {
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(FeaturesPM.SUNWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(0, 0.1F, 1)));
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(FeaturesPM.MOONWOOD_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(0, 0.1F, 1)));
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
}
