package com.verdantartifice.primalmagic.common.init;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class InitWorldGen {
    public static void initWorldGen() {
        initFeatures();
    }

    private static void initFeatures() {
        ForgeRegistries.BIOMES.getValues().stream().filter(InitWorldGen::shouldSpawnMarble).forEach((biome) -> {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlocksPM.MARBLE_RAW.getDefaultState(), 33), Placement.COUNT_RANGE, new CountRangeConfig(10, 0, 0, 80)));
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
