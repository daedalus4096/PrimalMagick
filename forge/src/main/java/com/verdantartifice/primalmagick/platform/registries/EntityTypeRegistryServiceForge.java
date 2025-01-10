package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.entities.EntityTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IEntityTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the entity type registry service.
 *
 * @author Daedalus4096
 */
public class EntityTypeRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<EntityType<?>> implements IEntityTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<EntityType<?>>> getDeferredRegisterSupplier() {
        return EntityTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<EntityType<?>> getRegistry() {
        return BuiltInRegistries.ENTITY_TYPE;
    }
}
