package com.verdantartifice.primalmagick.platform.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemForge;
import com.verdantartifice.primalmagick.common.tags.ITagValue;
import com.verdantartifice.primalmagick.common.tags.TagValueForgeCustom;
import com.verdantartifice.primalmagick.platform.services.registries.IRegistryService;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Base implementation of a generic registry service for Forge. Provides basic access to custom registry
 * operations as needed by common code.
 *
 * @param <R> the type of object stored in the encapsulated Forge registry
 * @author Daedalus4096
 */
abstract class AbstractCustomRegistryServiceForge<R> implements IRegistryService<R> {
    protected abstract Supplier<DeferredRegister<R>> getDeferredRegisterSupplier();
    protected abstract Supplier<IForgeRegistry<R>> getRegistry();

    @Override
    public <T extends R> IRegistryItem<R, T> register(String name, Supplier<T> supplier) {
        return new RegistryItemForge<>(this.getDeferredRegisterSupplier().get().register(name, supplier));
    }

    @Override
    public @Nullable R get(ResourceLocation id) {
        return this.getRegistry().get().getValue(id);
    }

    @Override
    public Collection<R> getAll() {
        return this.getRegistry().get().getValues();
    }

    @Override
    public Set<ResourceLocation> getAllKeys() {
        return this.getRegistry().get().getKeys();
    }

    @Override
    public Set<Map.Entry<ResourceKey<R>, R>> getEntries() {
        return this.getRegistry().get().getEntries();
    }

    @Override
    public boolean containsKey(ResourceLocation id) {
        return this.getRegistry().get().containsKey(id);
    }

    @Override
    public Optional<ResourceKey<R>> getResourceKey(R value) {
        return this.getRegistry().get().getResourceKey(value);
    }

    @Override
    public Optional<Holder<R>> getHolder(ResourceKey<R> key) {
        return this.getRegistry().get().getHolder(key);
    }

    @Override
    public Optional<Holder<R>> getHolder(ResourceLocation loc) {
        return this.getRegistry().get().getHolder(loc);
    }

    @Override
    public Optional<Holder<R>> getHolder(R value) {
        return this.getRegistry().get().getHolder(value);
    }

    @Override
    public Codec<R> codec() {
        return ResourceLocation.CODEC.flatXmap(loc -> {
            return Optional.ofNullable(this.getRegistry().get().getValue(loc)).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry key in " + this.getRegistry().get().getRegistryKey() + ": " + loc;
                });
            });
        }, element -> {
            return this.getRegistry().get().getResourceKey(element).map(ResourceKey::location).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry element in " + this.getRegistry().get().getRegistryKey() + ": " + element;
                });
            });
        });
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, R> registryFriendlyStreamCodec() {
        return new StreamCodec<>() {
            @Override
            public R decode(RegistryFriendlyByteBuf pBuffer) {
                ResourceLocation id = ResourceLocation.parse(Utf8String.read(pBuffer, 32767));
                return AbstractCustomRegistryServiceForge.this.getRegistry().get().getValue(id);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf pBuffer, R pValue) {
                ResourceLocation id = AbstractCustomRegistryServiceForge.this.getRegistry().get().getKey(pValue);
                Utf8String.write(pBuffer, id.toString(), 32767);
            }
        };
    }

    @Override
    public StreamCodec<FriendlyByteBuf, R> friendlyStreamCodec() {
        return new StreamCodec<>() {
            @Override
            public R decode(FriendlyByteBuf pBuffer) {
                ResourceLocation id = ResourceLocation.parse(Utf8String.read(pBuffer, 32767));
                return AbstractCustomRegistryServiceForge.this.getRegistry().get().getValue(id);
            }

            @Override
            public void encode(FriendlyByteBuf pBuffer, R pValue) {
                ResourceLocation id = AbstractCustomRegistryServiceForge.this.getRegistry().get().getKey(pValue);
                Utf8String.write(pBuffer, id.toString(), 32767);
            }
        };
    }

    @Override
    public ITagValue<R> getTag(TagKey<R> key) {
        return new TagValueForgeCustom<>(this.getRegistry().get().tags().getTag(key));
    }

    @Override
    public boolean tagExists(TagKey<R> key) {
        return this.getRegistry().get().tags().isKnownTagName(key);
    }
}
