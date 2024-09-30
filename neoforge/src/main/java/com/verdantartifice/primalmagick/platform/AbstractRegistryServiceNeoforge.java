package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemNeoforge;
import com.verdantartifice.primalmagick.platform.services.IRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

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
    public <T extends R> IRegistryItem<R, T> register(String name, Supplier<T> supplier) {
        return new RegistryItemNeoforge<>(this.getDeferredRegisterSupplier().get().register(name, supplier));
    }

    @Override
    public @Nullable R get(ResourceLocation id) {
        return this.getRegistry().get(id);
    }

    @Override
    public boolean containsKey(ResourceLocation id) {
        return this.getRegistry().containsKey(id);
    }
}
