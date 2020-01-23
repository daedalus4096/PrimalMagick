package com.verdantartifice.primalmagic.common.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * Collection of utility methods pertaining to a player inventories.
 * 
 * @author Daedalus4096
 */
public class InventoryUtils {
    /**
     * Determine if the given player is carrying the given item in their main inventory.  Does not
     * consider equipped items or nested inventories (e.g. backpacks).  If the given stack has a count
     * higher than one, this method will search for any combination of itemstacks in the player's
     * inventory that total the given count, rather than requiring a single stack of that size.
     * 
     * @param player the player whose inventory to search
     * @param stack the itemstack being searched for
     * @return true if the player is carrying at least that many of the given item, false otherwise
     */
    public static boolean isPlayerCarrying(@Nullable PlayerEntity player, @Nullable ItemStack stack) {
        if (player == null) {
            return false;
        }
        if (stack == null || stack.isEmpty()) {
            return true;
        }
        int count = stack.getCount();
        for (ItemStack searchStack : player.inventory.mainInventory) {
            // Only the items need match, not the NBT data
            if (ItemStack.areItemsEqual(stack, searchStack)) {
                count -= searchStack.getCount();
                if (count <= 0) {
                    // Once a sufficient number of the given item are found, return true
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Determine if the given player is carrying items of the given tag in their main inventory.  Does
     * not consider equipped items or nested inventories (e.g. backpacks).  If the given amount is
     * greater than one, this method will search for any combination of itemstacks in the player's
     * inventory that total the given count, rather than requiring a single stack of that size.  The
     * found items need not be the same, so long as all of them belong to the tag.
     * 
     * @param player the player whose inventory to search
     * @param tagName the name of the tag containing the items to be searched for
     * @param amount the minimum number of items the player must be carrying for success
     * @return true if the player is carrying at least that many of the given tag's items, false otherwise
     */
    public static boolean isPlayerCarrying(@Nullable PlayerEntity player, @Nullable ResourceLocation tagName, int amount) {
        if (player == null) {
            return false;
        }
        if (tagName == null) {
            return true;
        }
        Tag<Item> tag = ItemTags.getCollection().getOrCreate(tagName);
        for (ItemStack searchStack : player.inventory.mainInventory) {
            // Only the items need match, not the NBT data
            if (!searchStack.isEmpty() && tag.contains(searchStack.getItem())) {
                amount -= searchStack.getCount();
                if (amount <= 0) {
                    // Once a sufficient number of the given item are found, return true
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Attempts to remove the given quantity of the given item from the player's main inventory.  Does
     * not consider equipped items or nested inventories (e.g. backpacks).  If the given stack has a
     * count higher than one, this method will search for any combination of itemstacks in the player's
     * inventory that total the given count, rather than requiring a single stack of that size.
     * 
     * @param player the player whose inventory to search
     * @param stack the item and quantity to be removed
     * @return true if the given item was removed in the given quantity, false otherwise
     */
    public static boolean consumeItem(@Nullable PlayerEntity player, @Nullable ItemStack stack) {
        if (player == null) {
            return false;
        }
        if (stack == null || stack.isEmpty()) {
            return true;
        }
        if (!isPlayerCarrying(player, stack)) {
            // If the player is not carrying enough of the given item, return false immediately
            return false;
        }
        int count = stack.getCount();
        for (int index = 0; index < player.inventory.mainInventory.size(); index++) {
            ItemStack searchStack = player.inventory.mainInventory.get(index);
            // Only the items need match, not the NBT data
            if (ItemStack.areItemsEqual(stack, searchStack)) {
                if (searchStack.getCount() > count) {
                    searchStack.shrink(count);
                    count = 0;
                } else {
                    count -= searchStack.getCount();
                    player.inventory.mainInventory.set(index, ItemStack.EMPTY);
                }
                if (count <= 0) {
                    // Once a sufficient number of the given item are removed, return true
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Attempts to remove the given quantity of items in the given tag from the player's main inventory.
     * Does not consider equipped items or nested inventories (e.g. backpacks).  If the given amount is
     * greater than one, this method will search for any combination of itemstacks in the player's
     * inventory that total the given count, rather than requiring a single stack of that size.  The found
     * items need not be the same, so long as all of them belong to the tag.
     * 
     * @param player the player whose inventory to search
     * @param tagName the tag containing the items to be removed
     * @param amount the quantity of items to be removed
     * @return true if the given tag's items were removed in the given quantity, false otherwise
     */
    public static boolean consumeItem(@Nullable PlayerEntity player, @Nullable ResourceLocation tagName, int amount) {
        if (player == null) {
            return false;
        }
        if (tagName == null || amount <= 0) {
            return true;
        }
        if (!isPlayerCarrying(player, tagName, amount)) {
            // If the player is not carrying enough of the given items, return false immediately
            return false;
        }
        Tag<Item> tag = ItemTags.getCollection().getOrCreate(tagName);
        for (int index = 0; index < player.inventory.mainInventory.size(); index++) {
            ItemStack searchStack = player.inventory.mainInventory.get(index);
            // Only the items need match, not the NBT data
            if (!searchStack.isEmpty() && tag.contains(searchStack.getItem())) {
                if (searchStack.getCount() > amount) {
                    searchStack.shrink(amount);
                    amount = 0;
                } else {
                    amount -= searchStack.getCount();
                    player.inventory.mainInventory.set(index, ItemStack.EMPTY);
                }
                if (amount <= 0) {
                    // Once a sufficient number of the given items are removed, return true
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Attempts to get an item handler capability for the given side of the given position in the given world.
     * First searches for tiles that directly implement the capability, then attempts to wrap instances of the
     * vanilla inventory interface.
     * 
     * @param world the world containing the desired tile entity
     * @param pos the position of the desired tile entity
     * @param side the side of the tile entity to be queried
     * @return the item handler of the tile entity, or null if no such capability could be found
     */
    @Nullable
    public static IItemHandler getItemHandler(@Nonnull World world, @Nonnull BlockPos pos, @Nullable Direction side) {
        LazyOptional<Pair<IItemHandler, Object>> optional = VanillaInventoryCodeHooks.getItemHandler(world, pos.getX(), pos.getY(), pos.getZ(), side);
        Pair<IItemHandler, Object> pair = optional.orElse(null);
        if (pair != null && pair.getLeft() != null) {
            // If the tile entity directly provides an item handler capability, return that
            return pair.getLeft();
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof IInventory) {
                // If the tile entity does not provide an item handler but does have an inventory, return a wrapper around that
                return wrapInventory((IInventory)tile, side);
            } else {
                // If the tile entity does not have an inventory at all, return null
                return null;
            }
        }
    }
    
    /**
     * Wraps the given inventory object in a (possibly sided) item handler capability compliant wrapper.
     * 
     * @param inv the inventory to be wrapped
     * @param side the side of the inventory to be exposed by the wrapper
     * @return an item handler capability compliant wrapper of the given inventory
     */
    @Nonnull
    public static IItemHandler wrapInventory(@Nonnull IInventory inv, @Nullable Direction side) {
        if (inv instanceof ISidedInventory) {
            // Return a sided wrapper for the given side if the inventory is sided
            return new SidedInvWrapper((ISidedInventory)inv, side);
        } else {
            return new InvWrapper(inv);
        }
    }
}
