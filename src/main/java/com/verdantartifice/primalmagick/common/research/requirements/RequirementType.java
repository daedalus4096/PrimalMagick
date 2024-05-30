package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record RequirementType<T extends AbstractRequirement<T>>(ResourceLocation id, Supplier<Codec<T>> codecSupplier, FriendlyByteBuf.Reader<T> networkReader) {
}
