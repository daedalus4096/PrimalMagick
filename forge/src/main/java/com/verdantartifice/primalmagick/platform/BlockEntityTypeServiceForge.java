package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.entities.EntityTypeRegistration;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IBlockEntityTypeService;
import com.verdantartifice.primalmagick.platform.services.IEntityTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the block entity type registry service.
 *
 * @author Daedalus4096
 */
public class BlockEntityTypeServiceForge extends AbstractRegistryServiceForge<BlockEntityType<?>> implements IBlockEntityTypeService {
    @Override
    protected Supplier<DeferredRegister<BlockEntityType<?>>> getDeferredRegisterSupplier() {
        return BlockEntityTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<BlockEntityType<?>> getRegistry() {
        return BuiltInRegistries.BLOCK_ENTITY_TYPE;
    }
}
