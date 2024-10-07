package com.verdantartifice.primalmagick.common.worldgen.structures;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod structure types in Neoforge.
 *
 * @author Daedalus4096
 */
public class StructureTypeRegistration {
    private static final DeferredRegister<StructureType<?>> TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Constants.MOD_ID);

    public static DeferredRegister<StructureType<?>> getDeferredRegister() {
        return TYPES;
    }

    public static void init() {
        TYPES.register(PrimalMagick.getEventBus());
    }
}
