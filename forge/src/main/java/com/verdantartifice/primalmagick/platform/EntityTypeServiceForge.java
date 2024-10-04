package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.entities.EntityTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IEntityTypeService;
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
public class EntityTypeServiceForge extends AbstractRegistryServiceForge<EntityType<?>> implements IEntityTypeService {
    @Override
    protected Supplier<DeferredRegister<EntityType<?>>> getDeferredRegisterSupplier() {
        return EntityTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<EntityType<?>> getRegistry() {
        return BuiltInRegistries.ENTITY_TYPE;
    }
}
