package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod spell vehicle types in Forge.
 * 
 * @author Daedalus4096
 */
public class SpellVehicleTypeRegistration {
    private static final DeferredRegister<SpellVehicleType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.SPELL_VEHICLE_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<SpellVehicleType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    public static DeferredRegister<SpellVehicleType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static Supplier<IForgeRegistry<SpellVehicleType<?>>> getRegistry() {
        return TYPES;
    }
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
