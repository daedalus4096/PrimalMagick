package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record WeightFunctionType<T extends AbstractWeightFunction<T>>(ResourceLocation id, Codec<T> codec, FriendlyByteBuf.Reader<T> networkReader) {
}
