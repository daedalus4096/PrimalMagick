package com.verdantartifice.primalmagick.platform.services.registries;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.tags.ITagValue;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Base interface for a service which provides access to a Minecraft object registry. Meant to be extended with a
 * parameterized type for implementation.
 *
 * @param <R> the type of object contained in the registry
 * @author Daedalus4096
 */
public interface IRegistryService<R> {
    /**
     * Initialize this service's deferred register.
     */
    void init();

    /**
     * Register the supplied value with this registry under the given name.
     *
     * @param name the name under which to register the value
     * @param supplier a supplier which provides the value to be registered
     * @return the resource location with which the value can be retrieved
     */
    <T extends R> IRegistryItem<R, T> register(String name, Supplier<T> supplier);

    /**
     * Retrieve the registered value with the given name.
     *
     * @param id the ID of the value to be retrieved
     * @return the named value, or null if not present in this registry
     */
    @Nullable R get(ResourceLocation id);

    /**
     * Retrieve all registered values in this registry.
     *
     * @return a collection of all values in this registry
     */
    Collection<R> getAll();

    /**
     * Retrieve the keys of all registered values in this registry.
     *
     * @return a set of the keys of all registered values in this registry
     */
    Set<ResourceLocation> getAllKeys();

    /**
     * Get the entry set of all mapping entries of resource key to registered value for this registry.
     *
     * @return the entry set of all mapping entries of resource key to registered value for this registry
     */
    Set<Map.Entry<ResourceKey<R>, R>> getEntries();

    /**
     * Determine whether an object is present in this registry for the given resource location.
     *
     * @param id the ID of the value to be queried
     * @return true if the identified value is present in this registry, false otherwise
     */
    boolean containsKey(ResourceLocation id);

    /**
     * Get the resource key for the given value, if it exists in the registry.
     *
     * @param value the value to be queried
     * @return an optional containing the value's resource key if it is present in the registry, or empty otherwise
     */
    Optional<ResourceKey<R>> getResourceKey(R value);

    /**
     * Get the key for the given value, if it exists in the registry.
     *
     * @param value the value to be queried
     * @return the resource location identifying the given value if it exists in the registry, or null otherwise
     */
    @Nullable default ResourceLocation getKey(R value) { return this.getResourceKey(value).map(ResourceKey::location).orElse(null); }

    Optional<Holder<R>> getHolder(ResourceKey<R> key);

    Optional<Holder<R>> getHolder(ResourceLocation loc);

    Optional<Holder<R>> getHolder(R value);

    /**
     * Retrieve the serialization codec for this registry.
     *
     * @return the serialization codec for this registry
     */
    Codec<R> codec();

    /**
     * Retrieve a networking stream codec for this registry, built on a {@link RegistryFriendlyByteBuf}.
     *
     * @return a networking stream codec for this registry
     */
    StreamCodec<RegistryFriendlyByteBuf, R> registryFriendlyStreamCodec();

    /**
     * Retrieve a networking stream codec for this registry, built on a {@link FriendlyByteBuf}.
     *
     * @return a networking stream codec for this registry
     */
    StreamCodec<FriendlyByteBuf, R> friendlyStreamCodec();

    ITagValue<R> getTag(TagKey<R> key);

    boolean tagExists(TagKey<R> key);
}
