package com.verdantartifice.primalmagick.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.RandomSource;

/**
 * Collection of elements from which they can be pulled randomly, based on given weights.
 * 
 * @author Daedalus4096
 * @param <T> the type of element to be contained in the collection
 */
public class WeightedRandomBag<T> {
    protected List<Item> items = new ArrayList<>();
    protected double totalWeight = 0.0D;
    
    /**
     * Add the given object to the collection with the given weight.
     * 
     * @param object the object to be added to the collection
     * @param weight the weight to be assigned to the object during random selection
     * @return true if the item was successfully added to the collection, false otherwise
     */
    public boolean add(@Nullable T object, double weight) {
        Item item = new Item();
        item.obj = object;
        item.weight = weight;
        boolean success = this.items.add(item);
        if (success) {
            this.totalWeight += weight;
        }
        return success;
    }
    
    /**
     * Get a random element from the collection, based on the weights defined while adding them.
     * 
     * @param rng the random number generator to use for selection
     * @return a random element from the collection
     */
    @Nullable
    public T getRandom(@Nonnull RandomSource rng) {
        double threshold = rng.nextDouble() * this.totalWeight;
        double accumulatedWeight = 0.0D;
        for (Item item : this.items) {
            accumulatedWeight += item.weight;
            if (accumulatedWeight >= threshold) {
                return item.obj;
            }
        }
        return null;
    }
    
    /**
     * Pair of a collection item and its assigned weight.
     * 
     * @author Daedalus4096
     */
    protected class Item {
        public double weight;
        public T obj;
    }
}
