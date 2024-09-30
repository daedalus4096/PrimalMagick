package com.verdantartifice.primalmagick.common.registries;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public record RegistryItemForge<R, T extends R>(RegistryObject<T> inner) implements IRegistryItem<R, T> {
    @Override
    public T get() {
        return inner.get();
    }

    @Override
    public ResourceLocation getId() {
        return inner.getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResourceKey<R> getKey() {
        return (ResourceKey<R>)inner.getKey();
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable Holder<R> getHolder() {
        return inner.getHolder().isPresent() ? (Holder<R>)inner.getHolder().get() : null;
    }
}
