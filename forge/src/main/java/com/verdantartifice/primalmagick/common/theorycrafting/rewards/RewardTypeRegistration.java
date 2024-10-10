package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod reward types in Forge.
 * 
 * @author Daedalus4096
 */
public class RewardTypeRegistration {
    private static final DeferredRegister<RewardType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.PROJECT_REWARD_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<RewardType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    public static DeferredRegister<RewardType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static Supplier<IForgeRegistry<RewardType<?>>> getRegistry() {
        return TYPES;
    }
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
