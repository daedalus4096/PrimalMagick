package com.verdantartifice.primalmagick.common.registries;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Interface providing cross-platform access to mod-registered objects.
 *
 * @param <R> the type of object held by the registry
 * @param <T> the type of this specific registered object
 */
public interface IRegistryItem<R, T extends R> extends Supplier<T> {
    /**
     * Get the registered object.
     *
     * @return the registered object
     */
    T get();

    /**
     * Get the identifier of the object in the registry.
     *
     * @return the identifier of the object in the registry
     */
    ResourceLocation getId();

    /**
     * Get the fully-qualified resource key for this registry object.
     *
     * @return the fully-qualified registry key
     */
    ResourceKey<R> getKey();

    /**
     * Get a holder for this registry object
     *
     * @return a holder for the registered object
     */
    @Nullable Holder<R> getHolder();
}
