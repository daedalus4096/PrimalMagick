package com.verdantartifice.primalmagick.platform.registries;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemForge;
import com.verdantartifice.primalmagick.platform.services.IRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Base implementation of a generic registry service for Forge. Provides basic access to built-in registry
 * operations as needed by common code.
 *
 * @param <R> the type of object stored in the encapsulated Forge registry
 * @author Daedalus4096
 */
abstract class AbstractBuiltInRegistryServiceForge<R> implements IRegistryService<R> {
    protected abstract Supplier<DeferredRegister<R>> getDeferredRegisterSupplier();
    protected abstract Registry<R> getRegistry();

    @Override
    public <T extends R> IRegistryItem<R, T> register(String name, Supplier<T> supplier) {
        return new RegistryItemForge<>(this.getDeferredRegisterSupplier().get().register(name, supplier));
    }

    @Override
    public @Nullable R get(ResourceLocation id) {
        return this.getRegistry().get(id);
    }

    @Override
    public Collection<R> getAll() {
        return this.getRegistry().stream().toList();
    }

    @Override
    public boolean containsKey(ResourceLocation id) {
        return this.getRegistry().containsKey(id);
    }

    @Override
    public Optional<ResourceKey<R>> getResourceKey(R value) {
        return this.getRegistry().getResourceKey(value);
    }

    @Override
    public Codec<R> codec() {
        return this.getRegistry().byNameCodec();
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, R> registryFriendlyStreamCodec() {
        return ByteBufCodecs.registry(this.getRegistry().key());
    }

    @Override
    public StreamCodec<FriendlyByteBuf, R> friendlyStreamCodec() {
        return new StreamCodec<>() {
            @Override
            public R decode(FriendlyByteBuf pBuffer) {
                ResourceLocation id = ResourceLocation.parse(Utf8String.read(pBuffer, 32767));
                return AbstractBuiltInRegistryServiceForge.this.getRegistry().get(id);
            }

            @Override
            public void encode(FriendlyByteBuf pBuffer, R pValue) {
                ResourceLocation id = AbstractBuiltInRegistryServiceForge.this.getRegistry().getKey(pValue);
                Utf8String.write(pBuffer, id.toString(), 32767);
            }
        };
    }
}
