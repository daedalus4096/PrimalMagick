package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyType;
import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IResearchKeyTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the research key type registry service.
 *
 * @author Daedalus4096
 */
public class ResearchKeyTypeServiceForge extends AbstractCustomRegistryServiceForge<ResearchKeyType<?>> implements IResearchKeyTypeService {
    @Override
    protected Supplier<DeferredRegister<ResearchKeyType<?>>> getDeferredRegisterSupplier() {
        return ResearchKeyTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<ResearchKeyType<?>>> getRegistry() {
        return ResearchKeyTypeRegistration.getRegistry();
    }
}
