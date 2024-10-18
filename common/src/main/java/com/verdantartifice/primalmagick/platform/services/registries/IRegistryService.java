package com.verdantartifice.primalmagick.platform.services.registries;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
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
}
