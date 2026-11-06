package com.verdantartifice.primalmagick.common.entities;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.UUID;

public class EntityDataSerializersPM {
    public static final EntityDataSerializer<Optional<UUID>> OPTIONAL_UUID = register(EntityDataSerializer.forValueType(UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs::optional)));

    private static <T> EntityDataSerializer<T> register(EntityDataSerializer<T> serializer) {
        EntityDataSerializers.registerSerializer(serializer);
        return serializer;
    }
}
