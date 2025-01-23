package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleType;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellVehicleTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the spell vehicle type registry service.
 *
 * @author Daedalus4096
 */
public class SpellVehicleTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<SpellVehicleType<?>> implements ISpellVehicleTypeRegistryService {
    public static final Registry<SpellVehicleType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.SPELL_VEHICLE_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<SpellVehicleType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<SpellVehicleType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<SpellVehicleType<?>> getRegistry() {
        return TYPES;
    }
}
