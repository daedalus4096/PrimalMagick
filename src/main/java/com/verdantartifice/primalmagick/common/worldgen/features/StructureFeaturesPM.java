package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod worldgen features.
 * 
 * @author Daedalus4096
 */
public class StructureFeaturesPM {
    private static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, PrimalMagick.MODID);
    
    public static void init() {
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<StructureType<ShrineStructure>> SHRINE = STRUCTURES.register("shrine", () -> () -> ShrineStructure.CODEC);
}
