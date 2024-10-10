package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.worldgen.structures.StructureTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IStructureTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the structure type registry service.
 *
 * @author Daedalus4096
 */
public class StructureTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<StructureType<?>> implements IStructureTypeService {
    @Override
    protected Supplier<DeferredRegister<StructureType<?>>> getDeferredRegisterSupplier() {
        return StructureTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<StructureType<?>> getRegistry() {
        return BuiltInRegistries.STRUCTURE_TYPE;
    }
}
