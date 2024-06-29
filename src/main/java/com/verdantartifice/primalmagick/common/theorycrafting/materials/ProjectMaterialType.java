package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ProjectMaterialType<T extends AbstractProjectMaterial<T>>(ResourceLocation id, Supplier<MapCodec<T>> codecSupplier, Supplier<StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecSupplier) {
}
