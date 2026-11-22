package com.verdantartifice.primalmagick.common.entities;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.Optional;
import java.util.UUID;

public class EntityDataSerializersPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.ENTITY_DATA_SERIALIZERS_REGISTRY.init();
    }

    public static final IRegistryItem<EntityDataSerializer<?>, EntityDataSerializer<Optional<UUID>>> OPTIONAL_UUID = register("optional_uuid", EntityDataSerializer.forValueType(UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs::optional)));

    private static <T> IRegistryItem<EntityDataSerializer<?>, EntityDataSerializer<T>> register(String id, EntityDataSerializer<T> serializer) {
        return Services.ENTITY_DATA_SERIALIZERS_REGISTRY.register(id, () -> serializer);
    }
}
