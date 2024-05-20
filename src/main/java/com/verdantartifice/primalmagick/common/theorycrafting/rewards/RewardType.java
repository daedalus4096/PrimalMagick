package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record RewardType<T extends AbstractReward<T>>(ResourceLocation id, Codec<T> codec, FriendlyByteBuf.Reader<T> networkReader) {
}
