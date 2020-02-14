package com.verdantartifice.primalmagic.common.worldgen.features;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Object holder for mod worldgen features.  Actual values populated by Forge post-registration.
 * 
 * @author Daedalus4096
 */
@ObjectHolder(PrimalMagic.MODID)
public class FeaturesPM {
    public static final Feature<TreeFeatureConfig> PHASING_TREE = null;
    public static final Structure<ShrineConfig> SHRINE = null;
}
