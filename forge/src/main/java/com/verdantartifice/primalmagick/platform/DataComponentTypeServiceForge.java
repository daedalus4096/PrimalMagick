package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import com.verdantartifice.primalmagick.common.components.DataComponentTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IBlockService;
import com.verdantartifice.primalmagick.platform.services.IDataComponentTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the data component type registry service.
 *
 * @author Daedalus4096
 */
public class DataComponentTypeServiceForge extends AbstractRegistryServiceForge<DataComponentType<?>> implements IDataComponentTypeService {
    @Override
    protected Supplier<DeferredRegister<DataComponentType<?>>> getDeferredRegisterSupplier() {
        return DataComponentTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<DataComponentType<?>> getRegistry() {
        return BuiltInRegistries.DATA_COMPONENT_TYPE;
    }
}
