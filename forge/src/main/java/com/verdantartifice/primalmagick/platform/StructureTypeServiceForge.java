package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.worldgen.structures.StructureTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IStructureTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the structure type registry service.
 *
 * @author Daedalus4096
 */
public class StructureTypeServiceForge extends AbstractRegistryServiceForge<StructureType<?>> implements IStructureTypeService {
    @Override
    protected Supplier<DeferredRegister<StructureType<?>>> getDeferredRegisterSupplier() {
        return StructureTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<StructureType<?>> getRegistry() {
        return BuiltInRegistries.STRUCTURE_TYPE;
    }
}
