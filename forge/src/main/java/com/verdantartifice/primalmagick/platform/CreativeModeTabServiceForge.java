package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.creative.CreativeModeTabRegistration;
import com.verdantartifice.primalmagick.platform.services.ICreativeModeTabService;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

/**
 * Forge implementation of the block registry service.
 *
 * @author Daedalus4096
 */
public class CreativeModeTabServiceForge extends AbstractRegistryServiceForge<CreativeModeTab> implements ICreativeModeTabService {
    @Override
    protected Supplier<DeferredRegister<CreativeModeTab>> getDeferredRegisterSupplier() {
        return CreativeModeTabRegistration::getDeferredRegister;
    }

    @Override
    protected IForgeRegistry<CreativeModeTab> getRegistry() {
        // FIXME Refactor this to separate read and write functions in the abstract service
        throw new NotImplementedException();
    }
}
