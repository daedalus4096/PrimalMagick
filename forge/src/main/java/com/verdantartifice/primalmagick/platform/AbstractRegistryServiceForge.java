package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IRegistryService;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Base implementation of a generic registry service for Forge. Provides basic access to {@link IForgeRegistry}
 * operations as needed by common code.
 *
 * @param <T> the type of object stored in the encapsulated Forge registry
 * @author Daedalus4096
 */
abstract class AbstractRegistryServiceForge<T> implements IRegistryService<T> {
    protected abstract Supplier<DeferredRegister<T>> getDeferredRegisterSupplier();
    protected abstract IForgeRegistry<T> getRegistry();

    @Override
    public ResourceLocation register(String name, Supplier<? extends T> supplier) {
        return this.getDeferredRegisterSupplier().get().register(name, supplier).getId();
    }

    @Override
    public @Nullable T get(ResourceLocation id) {
        return this.getRegistry().getValue(id);
    }

    @Override
    public boolean containsKey(ResourceLocation id) {
        return this.getRegistry().containsKey(id);
    }
}
