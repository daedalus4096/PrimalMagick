package com.verdantartifice.primalmagick.common.entities;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;

import java.util.Optional;

public class EntityDataSerializersPM {
    @SuppressWarnings("unchecked")
    public static final EntityDataSerializer<Optional<EntityReference<Entity>>> OPTIONAL_ENTITY_REFERENCE = register(EntityDataSerializer.<Entity>forValueType(EntityReference.streamCodec().apply(ByteBufCodecs::optional)));

    private static <T> EntityDataSerializer<T> register(EntityDataSerializer<T> serializer) {
        EntityDataSerializers.registerSerializer(serializer);
        return serializer;
    }
}
