package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod worldgen features.
 * 
 * @author Daedalus4096
 */
public class StructureFeaturesPM {
    private static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, PrimalMagick.MODID);
    
    public static void init() {
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<StructureFeature<ShrineConfig>> SHRINE = STRUCTURES.register("shrine", () -> new ShrineStructure(ShrineConfig.CODEC));
}
