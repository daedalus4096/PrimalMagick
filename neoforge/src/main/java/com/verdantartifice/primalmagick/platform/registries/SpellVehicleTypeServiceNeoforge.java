package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyType;
import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyTypeRegistration;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleType;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IResearchKeyTypeService;
import com.verdantartifice.primalmagick.platform.services.ISpellVehicleTypeService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the spell vehicle type registry service.
 *
 * @author Daedalus4096
 */
public class SpellVehicleTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<SpellVehicleType<?>> implements ISpellVehicleTypeService {
    @Override
    protected Supplier<DeferredRegister<SpellVehicleType<?>>> getDeferredRegisterSupplier() {
        return SpellVehicleTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<SpellVehicleType<?>> getRegistry() {
        return SpellVehicleTypeRegistration.TYPES;
    }
}
