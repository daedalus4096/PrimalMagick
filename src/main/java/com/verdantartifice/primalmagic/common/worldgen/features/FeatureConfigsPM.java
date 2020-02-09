package com.verdantartifice.primalmagic.common.worldgen.features;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraftforge.common.IPlantable;

public class FeatureConfigsPM {
    private static final BlockState MOONWOOD_LOG = BlocksPM.MOONWOOD_LOG.getDefaultState();
    private static final BlockState MOONWOOD_LEAVES = BlocksPM.MOONWOOD_LEAVES.getDefaultState();
    private static final BlockState SUNWOOD_LOG = BlocksPM.SUNWOOD_LOG.getDefaultState();
    private static final BlockState SUNWOOD_LEAVES = BlocksPM.SUNWOOD_LEAVES.getDefaultState();
    
    public static final TreeFeatureConfig MOONWOOD_TREE_CONFIG = new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(MOONWOOD_LOG), new SimpleBlockStateProvider(MOONWOOD_LEAVES), new BlobFoliagePlacer(2, 0)).baseHeight(5).heightRandA(2).foliageHeight(3).ignoreVines().setSapling((IPlantable)BlocksPM.MOONWOOD_SAPLING).build();
    public static final TreeFeatureConfig SUNWOOD_TREE_CONFIG = new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SUNWOOD_LOG), new SimpleBlockStateProvider(SUNWOOD_LEAVES), new BlobFoliagePlacer(2, 0)).baseHeight(5).heightRandA(2).foliageHeight(3).ignoreVines().setSapling((IPlantable)BlocksPM.SUNWOOD_SAPLING).build();
}
