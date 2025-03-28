package com.verdantartifice.primalmagick.common.worldgen.structures;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.worldgen.structures.library.LibraryStructure;
import com.verdantartifice.primalmagick.common.worldgen.structures.library.NetherLibraryStructure;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Supplier;

/**
 * Deferred registry for mod worldgen features.
 * 
 * @author Daedalus4096
 */
public class StructureTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.STRUCTURE_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<StructureType<?>, StructureType<ShrineStructure>> SHRINE = register("shrine", () -> () -> ShrineStructure.CODEC);
    public static final IRegistryItem<StructureType<?>, StructureType<LibraryStructure>> LIBRARY = register("library", () -> () -> LibraryStructure.CODEC);
    public static final IRegistryItem<StructureType<?>, StructureType<NetherLibraryStructure>> NETHER_LIBRARY = register("nether_library", () -> () -> NetherLibraryStructure.CODEC);

    private static <T extends Structure> IRegistryItem<StructureType<?>, StructureType<T>> register(String name, Supplier<StructureType<T>> supplier) {
        return Services.STRUCTURE_TYPES_REGISTRY.register(name, supplier);
    }
}
