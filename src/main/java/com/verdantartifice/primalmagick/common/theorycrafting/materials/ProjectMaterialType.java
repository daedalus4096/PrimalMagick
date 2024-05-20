package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ProjectMaterialType<T extends AbstractProjectMaterial<T>>(ResourceLocation id, Codec<T> codec, FriendlyByteBuf.Reader<T> networkReader) {
}
