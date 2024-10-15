package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.worldgen.structures.StructurePieceTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IStructurePieceTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the structure piece type registry service.
 *
 * @author Daedalus4096
 */
public class StructurePieceTypeServiceForge extends AbstractBuiltInRegistryServiceForge<StructurePieceType> implements IStructurePieceTypeService {
    @Override
    protected Supplier<DeferredRegister<StructurePieceType>> getDeferredRegisterSupplier() {
        return StructurePieceTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<StructurePieceType> getRegistry() {
        return BuiltInRegistries.STRUCTURE_PIECE;
    }
}
