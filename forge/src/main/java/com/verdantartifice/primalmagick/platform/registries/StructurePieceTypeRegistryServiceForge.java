package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.worldgen.structures.StructurePieceTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IStructurePieceTypeRegistryService;
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
public class StructurePieceTypeRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<StructurePieceType> implements IStructurePieceTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<StructurePieceType>> getDeferredRegisterSupplier() {
        return StructurePieceTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<StructurePieceType> getRegistry() {
        return BuiltInRegistries.STRUCTURE_PIECE;
    }
}
