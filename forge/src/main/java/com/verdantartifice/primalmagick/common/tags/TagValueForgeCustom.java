package com.verdantartifice.primalmagick.common.tags;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public class TagValueForgeCustom<T> implements ITagValue<T> {
    private final ITag<T> value;

    public TagValueForgeCustom(final ITag<T> value) {
        this.value = value;
    }

    @Override
    public TagKey<T> getKey() {
        return this.value.getKey();
    }

    @Override
    public Stream<T> stream() {
        return this.value.stream();
    }

    @Override
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    @Override
    public int size() {
        return this.value.size();
    }

    @Override
    public boolean contains(T value) {
        return this.value.contains(value);
    }

    @Override
    public boolean contains(Holder<T> value) {
        return this.value.contains(value.value());
    }

    @Override
    public Optional<T> getRandomElement(RandomSource random) {
        return this.value.getRandomElement(random);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.value.iterator();
    }
}
