package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ResearchKeyType<T extends AbstractResearchKey<T>>(ResourceLocation id, Codec<T> codec, FriendlyByteBuf.Reader<T> networkReader) {
}
