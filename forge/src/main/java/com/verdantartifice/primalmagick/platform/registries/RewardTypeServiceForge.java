package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardType;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.RewardTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IRewardTypeService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the theorycrafting reward type registry service.
 *
 * @author Daedalus4096
 */
public class RewardTypeServiceForge extends AbstractCustomRegistryServiceForge<RewardType<?>> implements IRewardTypeService {
    @Override
    protected Supplier<DeferredRegister<RewardType<?>>> getDeferredRegisterSupplier() {
        return RewardTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<RewardType<?>>> getRegistry() {
        return RewardTypeRegistration.getRegistry();
    }
}
