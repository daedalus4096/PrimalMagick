package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;

/**
 * An extension of StackedContents that also tracks the NBT data of each of the stacks
 * for each item type.
 * 
 * @author Daedalus4096
 */
public class StackedNbtContents extends StackedContents {
    protected final Int2ObjectMap<List<CompoundTag>> nbtData = new Int2ObjectOpenHashMap<>();

    @Override
    public void accountStack(ItemStack stack, int maxCount) {
        super.accountStack(stack, maxCount);
        if (!stack.isEmpty() && stack.hasTag()) {
            this.nbtData.computeIfAbsent(getStackingIndex(stack), key -> new ArrayList<>()).add(stack.getTag());
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.nbtData.clear();
    }
    
    @Nullable
    public List<CompoundTag> getNbtData(int itemId) {
        return this.nbtData.get(itemId);
    }
    
    public boolean hasNbtData(int itemId) {
        return this.nbtData.containsKey(itemId) && !this.nbtData.get(itemId).isEmpty();
    }
}
