package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.creative.CreativeModeTabRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.ICreativeModeTabRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the creative mode tab registry service.
 *
 * @author Daedalus4096
 */
public class CreativeModeTabRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<CreativeModeTab> implements ICreativeModeTabRegistryService {
    @Override
    protected Supplier<DeferredRegister<CreativeModeTab>> getDeferredRegisterSupplier() {
        return CreativeModeTabRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<CreativeModeTab> getRegistry() {
        return BuiltInRegistries.CREATIVE_MODE_TAB;
    }
}
