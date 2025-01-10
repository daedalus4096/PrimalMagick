package com.verdantartifice.primalmagick.platform.registries;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemForge;
import com.verdantartifice.primalmagick.common.tags.ITagValue;
import com.verdantartifice.primalmagick.common.tags.TagValueForgeBuiltIn;
import com.verdantartifice.primalmagick.platform.services.registries.IRegistryService;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    public Set<ResourceLocation> getAllKeys() {
        return this.getRegistry().keySet();
    }

    @Override
    public Set<Map.Entry<ResourceKey<R>, R>> getEntries() {
        return this.getRegistry().entrySet();
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
    public Optional<Holder<R>> getHolder(ResourceKey<R> key) {
        return this.getRegistry().getHolder(key).flatMap(Optional::of);
    }

    @Override
    public Optional<Holder<R>> getHolder(ResourceLocation loc) {
        return this.getRegistry().getHolder(loc).flatMap(Optional::of);
    }

    @Override
    public Optional<Holder<R>> getHolder(R value) {
        return this.getRegistry().getResourceKey(value).flatMap(this::getHolder);
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

    @Override
    public ITagValue<R> getTag(TagKey<R> key) {
        return new TagValueForgeBuiltIn<>(this.getRegistry().getOrCreateTag(key));
    }

    @Override
    public boolean tagExists(TagKey<R> key) {
        return this.getRegistry().getTag(key).isPresent();
    }
}
