package com.verdantartifice.primalmagick.common.registries;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;

public record RegistryItemNeoforge<R, T extends R>(DeferredHolder<R, T> inner) implements IRegistryItem<R, T> {
    @Override
    public T get() {
        return inner.get();
    }

    @Override
    public ResourceLocation getId() {
        return inner.getId();
    }

    @Override
    public ResourceKey<R> getKey() {
        return inner.getKey();
    }

    @Override
    public @Nullable Holder<R> getHolder() {
        return inner;
    }
}
