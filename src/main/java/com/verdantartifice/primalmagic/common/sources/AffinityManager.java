package com.verdantartifice.primalmagic.common.sources;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class AffinityManager {
    protected static final Map<Integer, SourceList> REGISTRY = new ConcurrentHashMap<>();
    protected static final int MAX_AFFINITY = 100;
    
    public static void registerAffinities(@Nullable ItemStack stack, @Nullable SourceList sources) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (sources == null) {
            sources = new SourceList();
        }
        REGISTRY.put(Integer.valueOf(ItemUtils.getHashCode(stack)), sources);
    }
    
    public static void registerAffinities(@Nullable ResourceLocation tag, @Nullable SourceList sources) {
        if (tag == null) {
            return;
        }
        if (sources == null) {
            sources = new SourceList();
        }
        for (Item item : ItemTags.getCollection().getOrCreate(tag).getAllElements()) {
            registerAffinities(new ItemStack(item, 1), sources);
        }
    }
    
    public static boolean isRegistered(@Nullable ItemStack stack) {
        return REGISTRY.containsKey(Integer.valueOf(ItemUtils.getHashCode(stack, false))) ||
               REGISTRY.containsKey(Integer.valueOf(ItemUtils.getHashCode(stack, true)));
    }
    
    @Nonnull
    public static SourceList getAffinities(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return new SourceList();
        }
        // First try a straight lookup of the item
        SourceList retVal = REGISTRY.get(Integer.valueOf(ItemUtils.getHashCode(stack, false)));
        if (retVal == null) {
            // If that doesn't work, do a lookup of an NBT-stripped copy of the item
            retVal = REGISTRY.get(Integer.valueOf(ItemUtils.getHashCode(stack, true)));
            if (retVal == null) {
                // If that doesn't work either, generate affinities for the item and return those
                retVal = generateAffinities(stack);
                if (retVal == null) {
                    // And if no affinities can be generated, just return an empty source list
                    retVal = new SourceList();
                }
            }
        }
        return capAffinities(retVal, MAX_AFFINITY);
    }

    @Nonnull
    protected static SourceList capAffinities(@Nonnull SourceList sources, int maxAmount) {
        SourceList retVal = new SourceList();
        for (Source source : sources.getSources()) {
            retVal.merge(source, Math.min(maxAmount, sources.getAmount(source)));
        }
        return retVal;
    }

    @Nullable
    protected static SourceList generateAffinities(@Nonnull ItemStack stack) {
        // TODO Auto-generated method stub
        return null;
    }
}
