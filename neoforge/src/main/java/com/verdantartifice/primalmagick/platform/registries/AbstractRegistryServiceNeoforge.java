package com.verdantartifice.primalmagick.platform.registries;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemNeoforge;
import com.verdantartifice.primalmagick.common.tags.ITagValue;
import com.verdantartifice.primalmagick.common.tags.TagValueNeoforge;
import com.verdantartifice.primalmagick.platform.services.registries.IRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Base implementation of a generic registry service for Neoforge. Provides basic access to {@link Registry}
 * operations as needed by common code.
 *
 * @param <R> the type of object stored in the encapsulated Forge registry
 * @author Daedalus4096
 */
abstract class AbstractRegistryServiceNeoforge<R> implements IRegistryService<R> {
    protected abstract Supplier<DeferredRegister<R>> getDeferredRegisterSupplier();
    protected abstract Registry<R> getRegistry();

    @Override
    public void init() {
        this.getDeferredRegisterSupplier().get().register(PrimalMagick.getEventBus());
    }

    @Override
    public <T extends R> IRegistryItem<R, T> register(String name, Supplier<T> supplier) {
        return new RegistryItemNeoforge<>(this.getDeferredRegisterSupplier().get().register(name, supplier));
    }

    @Override
    public @Nullable R get(Identifier id) {
        return this.getRegistry().getValue(id);
    }

    @Override
    public Collection<R> getAll() {
        return this.getRegistry().stream().toList();
    }

    @Override
    public Set<Identifier> getAllKeys() {
        return this.getRegistry().keySet();
    }

    @Override
    public Set<Map.Entry<ResourceKey<R>, R>> getEntries() {
        return this.getRegistry().entrySet();
    }

    @Override
    public boolean containsKey(Identifier id) {
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
                Identifier id = Identifier.parse(Utf8String.read(pBuffer, 32767));
                return AbstractRegistryServiceNeoforge.this.getRegistry().getValue(id);
            }

            @Override
            public void encode(FriendlyByteBuf pBuffer, R pValue) {
                Identifier id = AbstractRegistryServiceNeoforge.this.getRegistry().getKey(pValue);
                Utf8String.write(pBuffer, id.toString(), 32767);
            }
        };
    }

    @Override
    public Optional<ITagValue<R>> getTag(TagKey<R> key) {
        return this.getRegistry().getTags().filter(n -> n.key().equals(key)).findFirst().map(TagValueNeoforge::new);
    }
}
