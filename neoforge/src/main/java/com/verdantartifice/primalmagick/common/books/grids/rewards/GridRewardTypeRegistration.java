package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod grid reward types in Neoforge.
 *
 * @author Daedalus4096
 */
public class GridRewardTypeRegistration {
    public static final Registry<GridRewardType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.GRID_REWARD_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<GridRewardType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<GridRewardType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}
