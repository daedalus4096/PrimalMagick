package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod weight function types in Forge.
 * 
 * @author Daedalus4096
 */
public class WeightFunctionTypeRegistration {
    private static final DeferredRegister<WeightFunctionType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.PROJECT_WEIGHT_FUNCTION_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<WeightFunctionType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    public static DeferredRegister<WeightFunctionType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static Supplier<IForgeRegistry<WeightFunctionType<?>>> getRegistry() {
        return TYPES;
    }
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
