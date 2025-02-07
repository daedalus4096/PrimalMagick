package com.verdantartifice.primalmagick.common.tags;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public class TagValueNeoforge<T> implements ITagValue<T> {
    private final HolderSet.Named<T> value;

    public TagValueNeoforge(final HolderSet.Named<T> value) {
        this.value = value;
    }

    @Override
    public TagKey<T> getKey() {
        return this.value.key();
    }

    @Override
    public Stream<T> stream() {
        return this.value.stream().map(Holder::value);
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public int size() {
        return this.value.size();
    }

    @Override
    public boolean contains(T value) {
        return this.stream().anyMatch(v -> v == value);
    }

    @Override
    public boolean contains(Holder<T> value) {
        return this.value.contains(value);
    }

    @Override
    public Optional<T> getRandomElement(RandomSource random) {
        return this.value.getRandomElement(random).map(Holder::value);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        Iterator<Holder<T>> iterator = this.value.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next().value();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }
}
