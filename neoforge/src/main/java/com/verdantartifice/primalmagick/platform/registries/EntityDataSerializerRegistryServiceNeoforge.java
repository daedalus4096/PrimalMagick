package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.IEntityDataSerializerRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the affinity type registry service.
 *
 * @author Daedalus4096
 */
public class EntityDataSerializerRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<EntityDataSerializer<?>> implements IEntityDataSerializerRegistryService {
    public static final Registry<EntityDataSerializer<?>> SERIALIZERS = NeoForgeRegistries.ENTITY_DATA_SERIALIZERS;
    private static final DeferredRegister<EntityDataSerializer<?>> DEFERRED_TYPES = DeferredRegister.create(SERIALIZERS, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<EntityDataSerializer<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<EntityDataSerializer<?>> getRegistry() {
        return SERIALIZERS;
    }
}
