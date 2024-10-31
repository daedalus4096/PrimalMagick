package com.verdantartifice.primalmagick.common.tags;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface providing cross-platform access to vanilla and mod-registered tags.
 *
 * @param <T> the registry type of the tag value
 */
public interface ITagValue<T> extends Iterable<T> {
    /**
     * Gets the tag key corresponding to this tag value.
     *
     * @return the tag key corresponding to this value
     */
    TagKey<T> getKey();

    /**
     * Gets a stream of all objects in this tag.
     *
     * @return a stream of all objects in this tag
     */
    Stream<T> stream();

    /**
     * Determines whether this tag is empty.
     *
     * @return true if there are no objects in this tag, false otherwise
     */
    boolean isEmpty();

    /**
     * Gets the number of objects in this tag.
     *
     * @return the number of objects in this tag
     */
    int size();

    /**
     * Determines whether this tag contains the object held by the given holder.
     *
     * @param value the holder of the value to be queried
     * @return true if this tag contains the held value, false otherwise
     */
    boolean contains(Holder<T> value);

    /**
     * Gets a random object from this tag's contents.
     *
     * @param random a random source to use for determining the object to select
     * @return a random object from this tag's contents
     */
    Optional<T> getRandomElement(RandomSource random);
}
