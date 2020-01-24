package com.verdantartifice.primalmagic.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

/**
 * Collection of elements from which they can be pulled randomly, based on given weights.
 * 
 * @author Daedalus4096
 * @param <T> the type of element to be contained in the collection
 */
public class WeightedRandomBag<T> {
    protected List<Item> items = new ArrayList<>();
    protected double totalWeight = 0.0D;
    protected Random rng = new Random();
    
    /**
     * Add the given object to the collection with the given weight.
     * 
     * @param object the object to be added to the collection
     * @param weight the weight to be assigned to the object during random selection
     * @return true if the item was successfully added to the collection, false otherwise
     */
    public boolean add(@Nullable T object, double weight) {
        this.totalWeight += weight; // FIXME don't add to total weight unless addition was a success
        Item item = new Item();
        item.obj = object;
        item.weight = weight;
        return this.items.add(item);
    }
    
    /**
     * Get a random element from the collection, based on the weights defined while adding them.
     * 
     * @return a random element from the collection
     */
    @Nullable
    public T getRandom() {
        // TODO accept an rng as a parameter instead of having one as a field of the collection
        double threshold = this.rng.nextDouble() * this.totalWeight;
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
