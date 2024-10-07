package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.worldgen.structures.StructurePieceTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IStructurePieceTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the structure piece type registry service.
 *
 * @author Daedalus4096
 */
public class StructurePieceTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<StructurePieceType> implements IStructurePieceTypeService {
    @Override
    protected Supplier<DeferredRegister<StructurePieceType>> getDeferredRegisterSupplier() {
        return StructurePieceTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<StructurePieceType> getRegistry() {
        return BuiltInRegistries.STRUCTURE_PIECE;
    }
}
