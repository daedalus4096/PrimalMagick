package com.verdantartifice.primalmagick.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Collection of utility methods pertaining to items.
 * 
 * @author Daedalus4096
 */
public class ItemUtils {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    /**
     * Calculate a unique hash code for the given itemstack and its data components.
     * 
     * @param stack the stack to be hashed
     * @return a unique hash code for the stack
     */
    public static int getHashCode(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        ItemStack temp = stack.copyWithCount(1);
        return Objects.hash(ForgeRegistries.ITEMS.getKey(temp.getItem()), temp.getComponents());
    }
    
    /**
     * Perform a deep copy of the given itemstack list.
     * 
     * @param list the itemstack list to be copied
     * @return a deep copy of the given itemstack list
     */
    @Nonnull
    public static List<ItemStack> copyItemStackList(List<ItemStack> list) {
        List<ItemStack> output = new ArrayList<>();
        for (ItemStack stack : list) {
            output.add(stack.copy());
        }
        return output;
    }
    
    /**
     * Merge the given itemstack into the given itemstack list.  Attempts to grow any existing stacks
     * to their maximum capacity before adding new stacks to the list.  Returns a modified deep copy of
     * the given list.
     * 
     * @param list the original list to merge into
     * @param stackToCopy the itemstack to be merged into the list
     * @return the merge result
     */
    @Nonnull
    public static List<ItemStack> mergeItemStackIntoList(@Nonnull List<ItemStack> list, @Nonnull ItemStack stackToCopy) {
        List<ItemStack> output = copyItemStackList(list);
        ItemStack stack = stackToCopy.copy();
        
        // First, scan for existing stacks that can be added to
        for (ItemStack outStack : output) {
            if (ItemStack.isSameItem(outStack, stack)) {
                if (stack.getCount() + outStack.getCount() <= outStack.getMaxStackSize()) {
                    // If the output stack can fully absorb the input, grow it and return
                    outStack.grow(stack.getCount());
                    return output;
                } else {
                    // Otherwise, grow it as much as possible and keep looking
                    int toGrow = outStack.getMaxStackSize() - outStack.getCount();
                    outStack.grow(toGrow);
                    stack.shrink(toGrow);
                }
            }
        }
        
        // Then scan for empty slots that can absorb what's left
        for (int index = 0; index < output.size(); index++) {
            if (output.get(index).isEmpty()) {
                output.set(index, stack);
                return output;
            }
        }
        
        // Finally, append whatever's left as a new element in the output list
        output.add(stack);
        
        return output;
    }
    
    /**
     * Merge the two given itemstack lists.  Attempts to combine stacks to their maximum capacity before
     * adding new stacks to the output list.  Returns a modified deep copy of the first list.
     * 
     * @param list1 the first itemstack list
     * @param list2 the second itemstack list
     * @return the merge result
     */
    @Nonnull
    public static List<ItemStack> mergeItemStackLists(@Nonnull List<ItemStack> list1, @Nonnull List<ItemStack> list2) {
        List<ItemStack> output = copyItemStackList(list1);
        for (ItemStack stack : list2) {
            output = mergeItemStackIntoList(output, stack);
        }
        return output;
    }
}
