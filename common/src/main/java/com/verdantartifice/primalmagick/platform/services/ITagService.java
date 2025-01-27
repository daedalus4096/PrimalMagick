package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public interface ITagService {
    default <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registry, String namespace, String name) {
        return TagKey.create(registry, ResourceLocation.fromNamespaceAndPath(namespace, name));
    }

    default <T> TagKey<T> createCommon(ResourceKey<? extends Registry<T>> registry, String name) {
        return create(registry, commonNamespace(), name);
    }

    default <T> TagKey<T> createSplit(ResourceKey<? extends Registry<T>> registry, String name) {
        return create(registry, splitNamespace(), name);
    }

    default String commonNamespace() {
        return "c";
    }

    String splitNamespace();

    <T> TagKey<T> cobblestonesNormal(ResourceKey<? extends Registry<T>> registry);
    <T> TagKey<T> cobblestonesInfested(ResourceKey<? extends Registry<T>> registry);
    <T> TagKey<T> cobblestonesMossy(ResourceKey<? extends Registry<T>> registry);
    <T> TagKey<T> cobblestonesDeepslate(ResourceKey<? extends Registry<T>> registry);

    <T> TagKey<T> gravels(ResourceKey<? extends Registry<T>> registry);
    <T> TagKey<T> gunpowders(ResourceKey<? extends Registry<T>> registry);
    <T> TagKey<T> netherracks(ResourceKey<? extends Registry<T>> registry);

    <T> TagKey<T> sands(ResourceKey<? extends Registry<T>> registry);
    <T> TagKey<T> sandsColorless(ResourceKey<? extends Registry<T>> registry);
    <T> TagKey<T> sandsRed(ResourceKey<? extends Registry<T>> registry);
}
