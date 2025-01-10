package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardType;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IRewardTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the theorycrafting reward type registry service.
 *
 * @author Daedalus4096
 */
public class RewardTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<RewardType<?>> implements IRewardTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<RewardType<?>>> getDeferredRegisterSupplier() {
        return RewardTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<RewardType<?>> getRegistry() {
        return RewardTypeRegistration.TYPES;
    }
}
