package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.books.grids.rewards.GridRewardType;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.platform.services.registries.IGridRewardTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the grid reward type registry service.
 *
 * @author Daedalus4096
 */
public class GridRewardTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<GridRewardType<?>> implements IGridRewardTypeRegistryService {
    public static final Registry<GridRewardType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.GRID_REWARD_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<GridRewardType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<GridRewardType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<GridRewardType<?>> getRegistry() {
        return TYPES;
    }
}
