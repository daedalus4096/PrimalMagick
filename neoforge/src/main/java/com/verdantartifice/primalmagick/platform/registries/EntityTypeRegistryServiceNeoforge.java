package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemNeoforge;
import com.verdantartifice.primalmagick.platform.services.registries.IEntityTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Neoforge implementation of the entity type registry service.
 *
 * @author Daedalus4096
 */
public class EntityTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<EntityType<?>> implements IEntityTypeRegistryService {
    private static final DeferredRegister.Entities TYPES = DeferredRegister.createEntities(Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<EntityType<?>>> getDeferredRegisterSupplier() {
        return () -> TYPES;
    }

    @Override
    protected Registry<EntityType<?>> getRegistry() {
        return BuiltInRegistries.ENTITY_TYPE;
    }

    @Override
    public <E extends Entity> IRegistryItem<EntityType<?>, EntityType<E>> registerEntityType(String name, EntityType.EntityFactory<E> factory, MobCategory category, UnaryOperator<EntityType.Builder<E>> builder) {
        return new RegistryItemNeoforge<>(TYPES.registerEntityType(name, factory, category, builder));
    }
}
