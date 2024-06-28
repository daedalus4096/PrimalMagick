package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.MapCodec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ResearchKeyType<T extends AbstractResearchKey<T>>(ResourceLocation id, MapCodec<T> codec, StreamCodec<ByteBuf, T> streamCodec) {
}
