package com.verdantartifice.primalmagick.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
     * Calculate a unique hash code for the given itemstack and its NBT data.
     * 
     * @param stack the stack to be hashed
     * @return a unique hash code for the stack
     */
    public static int getHashCode(@Nullable ItemStack stack) {
        return getHashCode(stack, false);
    }
    
    /**
     * Calculate a unique hash code for the given itemstack, optionally stripping its NBT data first.
     * 
     * @param stack the stack to be hashed
     * @param stripNBT whether to strip the stack's NBT data before hashing
     * @return a unique hash code for the stack
     */
    public static int getHashCode(@Nullable ItemStack stack, boolean stripNBT) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        ItemStack temp = stack.copy();
        temp.setCount(1);
        if (stripNBT) {
            // Strip the stack's NBT data if requested
            temp.setTag(null);
        }
        return temp.save(new CompoundTag()).toString().hashCode();
    }
    
    /**
     * Parse a string representation into an item stack.  Used for loading item references from
     * research definition files.
     * 
     * @param str the string to be parsed
     * @return the itemstack represented by the given string, or the empty stack upon parse failure
     */
    @Nonnull
    public static ItemStack parseItemStack(@Nullable String str) {
        if (str == null) {
            return ItemStack.EMPTY;
        }
        
        // Strings are expected to be of the format: "<item resource location>[;<count>[;<NBT data>]]"
        String[] tokens = str.split(";");
        String name = tokens[0];
        int count = -1;
        String nbt = null;
        if (tokens.length >= 2) {
            // Parse the count, if present
            try {
                count = Integer.parseInt(tokens[1]);
            } catch (NumberFormatException e) {}
        }
        if (tokens.length >= 3 && tokens[2].startsWith("{")) {
            // Parse the JSON-ified NBT data, if present
            nbt = tokens[2];
            nbt.replaceAll("\\\"", "\"");
        }
        if (count < 1) {
            count = 1;
        }
        
        ItemStack stack = ItemStack.EMPTY;
        try {
            // Get the named item definition from the item registry
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
            if (item != null) {
                stack = new ItemStack(item, count);
                if (nbt != null) {
                    stack.setTag(TagParser.parseTag(nbt));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Exception while parsing item stack string " + str, e);
        }
        
        return stack;
    }
    
    /**
     * Serialize an item stack into the string representation used in research and theorycrafting data
     * definition files.
     * 
     * @param stack the item stack to be serialized
     * @return a string representation of the item stack
     */
    @Nonnull
    public static String serializeItemStack(@Nonnull ItemStack stack) {
        StringBuilder sb = new StringBuilder(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        if (stack.getCount() > 1 || stack.hasTag()) {
            sb.append(';');
            sb.append(stack.getCount());
        }
        if (stack.hasTag()) {
            sb.append(';');
            sb.append(stack.getTag().toString().replaceAll("\"", "\\\""));
        }
        return sb.toString();
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
