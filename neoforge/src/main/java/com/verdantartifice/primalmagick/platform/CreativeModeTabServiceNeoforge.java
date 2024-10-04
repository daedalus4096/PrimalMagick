package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.creative.CreativeModeTabRegistration;
import com.verdantartifice.primalmagick.platform.services.ICreativeModeTabService;
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
public class CreativeModeTabServiceNeoforge extends AbstractRegistryServiceNeoforge<CreativeModeTab> implements ICreativeModeTabService {
    @Override
    protected Supplier<DeferredRegister<CreativeModeTab>> getDeferredRegisterSupplier() {
        return CreativeModeTabRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<CreativeModeTab> getRegistry() {
        return BuiltInRegistries.CREATIVE_MODE_TAB;
    }
}
