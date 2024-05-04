package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record RequirementType<T extends AbstractRequirement<T>>(ResourceLocation id, Codec<T> codec, FriendlyByteBuf.Reader<T> networkReader) {
}
