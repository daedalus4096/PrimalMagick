package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.worldgen.features.ConfiguredFeaturesPM;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class TreeGrowersPM {
    public static final TreeGrower SUNWOOD = new TreeGrower("primalmagick:sunwood", Optional.empty(), Optional.of(ConfiguredFeaturesPM.TREE_SUNWOOD_FULL), Optional.empty());
    public static final TreeGrower MOONWOOD = new TreeGrower("primalmagick:moonwood", Optional.empty(), Optional.of(ConfiguredFeaturesPM.TREE_MOONWOOD_FULL), Optional.empty());
    public static final TreeGrower HALLOWOOD = new TreeGrower("primalmagick:hallowood", Optional.empty(), Optional.of(ConfiguredFeaturesPM.TREE_HALLOWOOD), Optional.empty());
}
