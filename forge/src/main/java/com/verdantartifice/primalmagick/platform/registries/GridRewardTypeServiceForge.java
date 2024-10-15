package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.books.grids.rewards.GridRewardType;
import com.verdantartifice.primalmagick.common.books.grids.rewards.GridRewardTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IGridRewardTypeService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the grid reward type registry service.
 *
 * @author Daedalus4096
 */
public class GridRewardTypeServiceForge extends AbstractCustomRegistryServiceForge<GridRewardType<?>> implements IGridRewardTypeService {
    @Override
    protected Supplier<DeferredRegister<GridRewardType<?>>> getDeferredRegisterSupplier() {
        return GridRewardTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<GridRewardType<?>>> getRegistry() {
        return GridRewardTypeRegistration.getRegistry();
    }
}
