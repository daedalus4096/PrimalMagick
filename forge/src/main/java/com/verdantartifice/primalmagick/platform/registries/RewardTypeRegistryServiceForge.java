package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardType;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IRewardTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the theorycrafting reward type registry service.
 *
 * @author Daedalus4096
 */
public class RewardTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<RewardType<?>> implements IRewardTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<RewardType<?>>> getDeferredRegisterSupplier() {
        return RewardTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<RewardType<?>>> getRegistry() {
        return RewardTypeRegistration.getRegistry();
    }
}
