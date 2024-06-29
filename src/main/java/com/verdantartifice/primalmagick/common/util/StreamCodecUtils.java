package com.verdantartifice.primalmagick.common.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class StreamCodecUtils {
    public static <T> StreamCodec<ByteBuf, TagKey<T>> tagKey(ResourceKey<? extends Registry<T>> pRegistry) {
        return ResourceLocation.STREAM_CODEC.map(loc -> TagKey.create(pRegistry, loc), TagKey::location);
    }
}
