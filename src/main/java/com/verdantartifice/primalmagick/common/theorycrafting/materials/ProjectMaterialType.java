package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;

public record ProjectMaterialType<T extends AbstractProjectMaterial<T>>(ResourceLocation id, Supplier<Codec<T>> codecSupplier, AbstractProjectMaterial.Reader<T> networkReader) {
}
