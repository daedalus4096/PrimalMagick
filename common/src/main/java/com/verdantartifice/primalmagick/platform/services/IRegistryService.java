package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Base interface for a service which provides access to a Minecraft object registry. Meant to be extended with a
 * parameterized type for implementation.
 *
 * @param <T> the type of object contained in the registry
 * @author Daedalus4096
 */
public interface IRegistryService<T> {
    /**
     * Register the supplied value with this registry under the given name.
     *
     * @param name the name under which to register the value
     * @param supplier a supplier which provides the value to be registered
     * @return the resource location with which the value can be retrieved
     */
    ResourceLocation register(String name, Supplier<? extends T> supplier);

    /**
     * Retrieve the registered value with the given name.
     *
     * @param id the ID of the value to be retrieved
     * @return the named value, or null if not present in this registry
     */
    @Nullable
    T get(ResourceLocation id);

    /**
     * Determine whether an object is present in this registry for the given resource location.
     *
     * @param id the ID of the value to be queried
     * @return true if the identified value is present in this registry, false otherwise
     */
    boolean containsKey(ResourceLocation id);
}
