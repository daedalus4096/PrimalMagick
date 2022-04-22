package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodLogBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodLogBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

/**
 * Registration for mod tree worldgen features.
 * 
 * @author Daedalus4096
 */
public class TreeFeaturesPM {
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE_SUNWOOD_FULL;
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE_SUNWOOD_WAXING;
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE_SUNWOOD_WANING;
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE_SUNWOOD_FADED;

    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE_MOONWOOD_FULL;
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE_MOONWOOD_WAXING;
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE_MOONWOOD_WANING;
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE_MOONWOOD_FADED;
    
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE_HALLOWOOD;
    
    public static void setupTreeFeatures() {
        TREE_SUNWOOD_FULL = registerTreeFeature("tree_sunwood_full", Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(TreeFeaturesPM.States.SUNWOOD_LOG_FULL, 39).add(TreeFeaturesPM.States.PULSING_SUNWOOD_LOG_FULL, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(TreeFeaturesPM.States.SUNWOOD_LEAVES_FULL), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
        TREE_SUNWOOD_WAXING = registerTreeFeature("tree_sunwood_waxing", Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(TreeFeaturesPM.States.SUNWOOD_LOG_WAXING, 39).add(TreeFeaturesPM.States.PULSING_SUNWOOD_LOG_WAXING, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(TreeFeaturesPM.States.SUNWOOD_LEAVES_WAXING), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
        TREE_SUNWOOD_WANING = registerTreeFeature("tree_sunwood_waning", Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(TreeFeaturesPM.States.SUNWOOD_LOG_WANING, 39).add(TreeFeaturesPM.States.PULSING_SUNWOOD_LOG_WANING, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(TreeFeaturesPM.States.SUNWOOD_LEAVES_WANING), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
        TREE_SUNWOOD_FADED = registerTreeFeature("tree_sunwood_faded", Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(TreeFeaturesPM.States.SUNWOOD_LOG_FADED, 39).add(TreeFeaturesPM.States.PULSING_SUNWOOD_LOG_FADED, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(TreeFeaturesPM.States.SUNWOOD_LEAVES_FADED), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());

        TREE_MOONWOOD_FULL = registerTreeFeature("tree_moonwood_full", Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(TreeFeaturesPM.States.MOONWOOD_LOG_FULL, 39).add(TreeFeaturesPM.States.PULSING_MOONWOOD_LOG_FULL, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(TreeFeaturesPM.States.MOONWOOD_LEAVES_FULL), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
        TREE_MOONWOOD_WAXING = registerTreeFeature("tree_moonwood_waxing", Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(TreeFeaturesPM.States.MOONWOOD_LOG_WAXING, 39).add(TreeFeaturesPM.States.PULSING_MOONWOOD_LOG_WAXING, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(TreeFeaturesPM.States.MOONWOOD_LEAVES_WAXING), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
        TREE_MOONWOOD_WANING = registerTreeFeature("tree_moonwood_waning", Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(TreeFeaturesPM.States.MOONWOOD_LOG_WANING, 39).add(TreeFeaturesPM.States.PULSING_MOONWOOD_LOG_WANING, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(TreeFeaturesPM.States.MOONWOOD_LEAVES_WANING), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
        TREE_MOONWOOD_FADED = registerTreeFeature("tree_moonwood_faded", Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(TreeFeaturesPM.States.MOONWOOD_LOG_FADED, 39).add(TreeFeaturesPM.States.PULSING_MOONWOOD_LOG_FADED, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(TreeFeaturesPM.States.MOONWOOD_LEAVES_FADED), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
        
        TREE_HALLOWOOD = registerTreeFeature("tree_hallowood", Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(TreeFeaturesPM.States.HALLOWOOD_LOG), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(TreeFeaturesPM.States.HALLOWOOD_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
    }

    private static <FC extends FeatureConfiguration> Holder<ConfiguredFeature<FC, ?>> registerTreeFeature(String key, Feature<FC> baseFeature, FC featureConfig) {
        return FeatureUtils.register(PrimalMagick.MODID + ":" + key, baseFeature, featureConfig);
    }
    
    protected static final class States {
        protected static final BlockState SUNWOOD_LOG_FULL = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.FULL);
        protected static final BlockState SUNWOOD_LOG_WAXING = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState SUNWOOD_LOG_WANING = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.WANING);
        protected static final BlockState SUNWOOD_LOG_FADED = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.FADED);
        protected static final BlockState PULSING_SUNWOOD_LOG_FULL = SUNWOOD_LOG_FULL.setValue(SunwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_SUNWOOD_LOG_WAXING = SUNWOOD_LOG_WAXING.setValue(SunwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_SUNWOOD_LOG_WANING = SUNWOOD_LOG_WANING.setValue(SunwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_SUNWOOD_LOG_FADED = SUNWOOD_LOG_FADED.setValue(SunwoodLogBlock.PULSING, true);
        protected static final BlockState SUNWOOD_LEAVES_FULL = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.FULL);
        protected static final BlockState SUNWOOD_LEAVES_WAXING = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState SUNWOOD_LEAVES_WANING = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.WANING);
        protected static final BlockState SUNWOOD_LEAVES_FADED = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.FADED);
        protected static final BlockState SUNWOOD_SAPLING = BlocksPM.SUNWOOD_SAPLING.get().defaultBlockState();
        protected static final BlockState MOONWOOD_LOG_FULL = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.FULL);
        protected static final BlockState MOONWOOD_LOG_WAXING = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState MOONWOOD_LOG_WANING = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.WANING);
        protected static final BlockState MOONWOOD_LOG_FADED = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.FADED);
        protected static final BlockState PULSING_MOONWOOD_LOG_FULL = MOONWOOD_LOG_FULL.setValue(MoonwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_MOONWOOD_LOG_WAXING = MOONWOOD_LOG_WAXING.setValue(MoonwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_MOONWOOD_LOG_WANING = MOONWOOD_LOG_WANING.setValue(MoonwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_MOONWOOD_LOG_FADED = MOONWOOD_LOG_FADED.setValue(MoonwoodLogBlock.PULSING, true);
        protected static final BlockState MOONWOOD_LEAVES_FULL = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.FULL);
        protected static final BlockState MOONWOOD_LEAVES_WAXING = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState MOONWOOD_LEAVES_WANING = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.WANING);
        protected static final BlockState MOONWOOD_LEAVES_FADED = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.FADED);
        protected static final BlockState MOONWOOD_SAPLING = BlocksPM.MOONWOOD_SAPLING.get().defaultBlockState();
        protected static final BlockState HALLOWOOD_LOG = BlocksPM.HALLOWOOD_LOG.get().defaultBlockState();
        protected static final BlockState HALLOWOOD_LEAVES = BlocksPM.HALLOWOOD_LEAVES.get().defaultBlockState();
        protected static final BlockState HALLOWOOD_SAPLING = BlocksPM.HALLOWOOD_SAPLING.get().defaultBlockState();
    }
}
