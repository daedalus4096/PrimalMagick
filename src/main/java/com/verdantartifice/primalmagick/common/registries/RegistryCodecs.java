package com.verdantartifice.primalmagick.common.registries;

import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryCodecs {
    public static <T> Codec<T> codec(Supplier<IForgeRegistry<T>> registrySupplier) {
        return ResourceLocation.CODEC.flatXmap(loc -> {
            return Optional.ofNullable(registrySupplier.get().getValue(loc)).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry key in " + registrySupplier.get().getRegistryKey() + ": " + loc;
                });
            });
        }, element -> {
            return registrySupplier.get().getResourceKey(element).map(ResourceKey::location).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry element in " + registrySupplier.get().getRegistryKey() + ": " + element;
                });
            });
        });
    }
}
