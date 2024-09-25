package com.verdantartifice.primalmagick.common.worldgen.structures;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.worldgen.structures.library.LibraryStructure;
import com.verdantartifice.primalmagick.common.worldgen.structures.library.NetherLibraryStructure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod worldgen features.
 * 
 * @author Daedalus4096
 */
public class StructureFeaturesPM {
    private static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Constants.MOD_ID);
    
    public static void init() {
        STRUCTURES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<StructureType<ShrineStructure>> SHRINE = STRUCTURES.register("shrine", () -> () -> ShrineStructure.CODEC);
    public static final RegistryObject<StructureType<LibraryStructure>> LIBRARY = STRUCTURES.register("library", () -> () -> LibraryStructure.CODEC);
    public static final RegistryObject<StructureType<NetherLibraryStructure>> NETHER_LIBRARY = STRUCTURES.register("nether_library", () -> () -> NetherLibraryStructure.CODEC);
}
