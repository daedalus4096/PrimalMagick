package com.verdantartifice.primalmagic.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

public class WeightedRandomBag<T> {
    protected List<Item> items = new ArrayList<>();
    protected double totalWeight = 0.0D;
    protected Random rng = new Random();
    
    public boolean add(@Nullable T object, double weight) {
        this.totalWeight += weight;
        Item item = new Item();
        item.obj = object;
        item.weight = weight;
        return this.items.add(item);
    }
    
    @Nullable
    public T getRandom() {
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
    
    protected class Item {
        public double weight;
        public T obj;
    }
}
