package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.books.grids.rewards.GridRewardType;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.platform.services.registries.IGridRewardTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Forge implementation of the grid reward type registry service.
 *
 * @author Daedalus4096
 */
public class GridRewardTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<GridRewardType<?>> implements IGridRewardTypeRegistryService {
    private static final DeferredRegister<GridRewardType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.GRID_REWARD_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<GridRewardType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    @Override
    protected Supplier<DeferredRegister<GridRewardType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Supplier<IForgeRegistry<GridRewardType<?>>> getRegistry() {
        return TYPES;
    }
}
