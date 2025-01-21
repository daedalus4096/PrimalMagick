package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.IStructureTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the structure type registry service.
 *
 * @author Daedalus4096
 */
public class StructureTypeRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<StructureType<?>> implements IStructureTypeRegistryService {
    private static final DeferredRegister<StructureType<?>> TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<StructureType<?>>> getDeferredRegisterSupplier() {
        return () -> TYPES;
    }

    @Override
    protected Registry<StructureType<?>> getRegistry() {
        return BuiltInRegistries.STRUCTURE_TYPE;
    }
}
