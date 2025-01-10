package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleType;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellVehicleTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the spell vehicle type registry service.
 *
 * @author Daedalus4096
 */
public class SpellVehicleTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<SpellVehicleType<?>> implements ISpellVehicleTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<SpellVehicleType<?>>> getDeferredRegisterSupplier() {
        return SpellVehicleTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<SpellVehicleType<?>> getRegistry() {
        return SpellVehicleTypeRegistration.TYPES;
    }
}
