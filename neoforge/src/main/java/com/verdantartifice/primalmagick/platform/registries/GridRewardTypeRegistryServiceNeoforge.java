package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.books.grids.rewards.GridRewardType;
import com.verdantartifice.primalmagick.common.books.grids.rewards.GridRewardTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IGridRewardTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the grid reward type registry service.
 *
 * @author Daedalus4096
 */
public class GridRewardTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<GridRewardType<?>> implements IGridRewardTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<GridRewardType<?>>> getDeferredRegisterSupplier() {
        return GridRewardTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<GridRewardType<?>> getRegistry() {
        return GridRewardTypeRegistration.TYPES;
    }
}
