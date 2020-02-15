package com.verdantartifice.primalmagic.common.worldgen.features;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod worldgen features.
 * 
 * @author Daedalus4096
 */
public class FeaturesPM {
    private static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, PrimalMagic.MODID);
    
    public static void init() {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<Feature<TreeFeatureConfig>> PHASING_TREE = FEATURES.register("phasing_tree", () -> new PhasingTreeFeature(TreeFeatureConfig::func_227338_a_));
    public static final RegistryObject<Structure<ShrineConfig>> SHRINE = FEATURES.register("shrine", () -> new ShrineStructure(ShrineConfig::deserialize));
}
