package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod grid reward types in Forge.
 * 
 * @author Daedalus4096
 */
public class GridRewardTypeRegistration {
    private static final DeferredRegister<GridRewardType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.GRID_REWARD_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<GridRewardType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    public static DeferredRegister<GridRewardType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static Supplier<IForgeRegistry<GridRewardType<?>>> getRegistry() {
        return TYPES;
    }
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
