package com.verdantartifice.primalmagick.common.worldgen.structures;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod structure piece types in Neoforge.
 *
 * @author Daedalus4096
 */
public class StructurePieceTypeRegistration {
    private static final DeferredRegister<StructurePieceType> TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, Constants.MOD_ID);

    public static DeferredRegister<StructurePieceType> getDeferredRegister() {
        return TYPES;
    }

    public static void init() {
        TYPES.register(PrimalMagick.getEventBus());
    }
}
