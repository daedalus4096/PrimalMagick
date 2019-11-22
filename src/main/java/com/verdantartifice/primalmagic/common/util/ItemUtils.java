package com.verdantartifice.primalmagic.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public class ItemUtils {
    public static int getHashCode(@Nullable ItemStack stack) {
        return getHashCode(stack, false);
    }
    
    public static int getHashCode(@Nullable ItemStack stack, boolean stripNBT) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        ItemStack temp = stack.copy();
        temp.setCount(1);
        if (stripNBT) {
            temp.setTag(null);
        }
        return temp.write(new CompoundNBT()).toString().hashCode();
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    public static ItemStack parseItemStack(@Nullable String str) {
        if (str == null) {
            return ItemStack.EMPTY;
        }
        
        String[] tokens = str.split(";");
        String name = tokens[0];
        int count = -1;
        String nbt = null;
        if (tokens.length >= 2) {
            count = MathHelper.getInt(tokens[1], -1);
        }
        if (tokens.length >= 3 && tokens[2].startsWith("{")) {
            nbt = tokens[2];
            nbt.replaceAll("'", "\"");
        }
        if (count < 1) {
            count = 1;
        }
        
        ItemStack stack = ItemStack.EMPTY;
        try {
            Item item = Registry.ITEM.getOrDefault(new ResourceLocation(name));
            if (item != null) {
                stack = new ItemStack(item, count);
                if (nbt != null) {
                    stack.setTag(JsonToNBT.getTagFromJson(nbt));
                }
            }
        } catch (Exception e) {}
        
        return stack;
    }
    
    @Nonnull
    public static List<ItemStack> copyItemStackList(List<ItemStack> list) {
        List<ItemStack> output = new ArrayList<>();
        for (ItemStack stack : list) {
            output.add(stack.copy());
        }
        return output;
    }
    
    @Nonnull
    public static List<ItemStack> mergeItemStackIntoList(@Nonnull List<ItemStack> list, @Nonnull ItemStack stackToCopy) {
        List<ItemStack> output = copyItemStackList(list);
        ItemStack stack = stackToCopy.copy();
        
        // First, scan for existing stacks that can be added to
        for (ItemStack outStack : output) {
            if (outStack.isItemEqual(stack)) {
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
    
    @Nonnull
    public static List<ItemStack> mergeItemStackLists(@Nonnull List<ItemStack> list1, @Nonnull List<ItemStack> list2) {
        List<ItemStack> output = list1;
        for (ItemStack stack : list2) {
            output = mergeItemStackIntoList(output, stack);
        }
        return output;
    }
}
