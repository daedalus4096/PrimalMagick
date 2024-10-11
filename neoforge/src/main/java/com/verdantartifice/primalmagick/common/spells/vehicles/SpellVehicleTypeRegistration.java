package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod spell vehicle types in Neoforge.
 *
 * @author Daedalus4096
 */
public class SpellVehicleTypeRegistration {
    public static final Registry<SpellVehicleType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.SPELL_VEHICLE_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<SpellVehicleType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<SpellVehicleType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}
