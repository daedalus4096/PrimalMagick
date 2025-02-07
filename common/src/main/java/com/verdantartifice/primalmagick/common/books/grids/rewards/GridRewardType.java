package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record GridRewardType<T extends AbstractReward<T>>(ResourceLocation id, MapCodec<T> codec, StreamCodec<? super FriendlyByteBuf, T> streamCodec) {
}
