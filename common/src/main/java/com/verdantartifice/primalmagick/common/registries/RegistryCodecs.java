package com.verdantartifice.primalmagick.common.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Optional;
import java.util.function.Supplier;

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
    
    public static <T> StreamCodec<RegistryFriendlyByteBuf, T> registryFriendlyStreamCodec(Supplier<IForgeRegistry<T>> registrySupplier) {
        return new StreamCodec<RegistryFriendlyByteBuf, T>() {
            @Override
            public T decode(RegistryFriendlyByteBuf pBuffer) {
                ResourceLocation id = ResourceLocation.parse(Utf8String.read(pBuffer, 32767));
                return registrySupplier.get().getValue(id);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf pBuffer, T pValue) {
                ResourceLocation id = registrySupplier.get().getKey(pValue);
                Utf8String.write(pBuffer, id.toString(), 32767);
            }
        };
    }
    
    public static <T> StreamCodec<FriendlyByteBuf, T> friendlyStreamCodec(Supplier<IForgeRegistry<T>> registrySupplier) {
        return new StreamCodec<FriendlyByteBuf, T>() {
            @Override
            public T decode(FriendlyByteBuf pBuffer) {
                ResourceLocation id = ResourceLocation.parse(Utf8String.read(pBuffer, 32767));
                return registrySupplier.get().getValue(id);
            }

            @Override
            public void encode(FriendlyByteBuf pBuffer, T pValue) {
                ResourceLocation id = registrySupplier.get().getKey(pValue);
                Utf8String.write(pBuffer, id.toString(), 32767);
            }
        };
    }
}
