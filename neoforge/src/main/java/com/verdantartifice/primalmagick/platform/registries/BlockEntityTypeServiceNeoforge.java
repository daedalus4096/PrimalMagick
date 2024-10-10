package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IBlockEntityTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the block entity type registry service.
 *
 * @author Daedalus4096
 */
public class BlockEntityTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<BlockEntityType<?>> implements IBlockEntityTypeService {
    @Override
    protected Supplier<DeferredRegister<BlockEntityType<?>>> getDeferredRegisterSupplier() {
        return BlockEntityTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<BlockEntityType<?>> getRegistry() {
        return BuiltInRegistries.BLOCK_ENTITY_TYPE;
    }
}
