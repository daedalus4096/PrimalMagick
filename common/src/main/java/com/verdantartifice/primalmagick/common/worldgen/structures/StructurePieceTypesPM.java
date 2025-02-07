package com.verdantartifice.primalmagick.common.worldgen.structures;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.worldgen.structures.library.LibraryPiece;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.function.Supplier;

/**
 * Holder for mod structure piece types.
 * 
 * @author Daedalus4096
 */
public class StructurePieceTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.STRUCTURE_PIECE_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<StructurePieceType, StructurePieceType> SHRINE = register("shrine", () -> ShrinePiece::new);
    public static final IRegistryItem<StructurePieceType, StructurePieceType> LIBRARY = register("library", () -> LibraryPiece::new);

    private static IRegistryItem<StructurePieceType, StructurePieceType> register(String name, Supplier<StructurePieceType> supplier) {
        return Services.STRUCTURE_PIECE_TYPES_REGISTRY.register(name, supplier);
    }
}
