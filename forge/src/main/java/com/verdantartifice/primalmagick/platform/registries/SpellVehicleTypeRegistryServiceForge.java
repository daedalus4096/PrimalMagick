package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleType;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellVehicleTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the spell vehicle type registry service.
 *
 * @author Daedalus4096
 */
public class SpellVehicleTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<SpellVehicleType<?>> implements ISpellVehicleTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<SpellVehicleType<?>>> getDeferredRegisterSupplier() {
        return SpellVehicleTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<SpellVehicleType<?>>> getRegistry() {
        return SpellVehicleTypeRegistration.getRegistry();
    }
}
