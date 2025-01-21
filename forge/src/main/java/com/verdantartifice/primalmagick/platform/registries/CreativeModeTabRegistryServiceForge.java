package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.ICreativeModeTabRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the creative mode tab registry service.
 *
 * @author Daedalus4096
 */
public class CreativeModeTabRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<CreativeModeTab> implements ICreativeModeTabRegistryService {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<CreativeModeTab>> getDeferredRegisterSupplier() {
        return () -> TABS;
    }

    @Override
    protected Registry<CreativeModeTab> getRegistry() {
        return BuiltInRegistries.CREATIVE_MODE_TAB;
    }
}
