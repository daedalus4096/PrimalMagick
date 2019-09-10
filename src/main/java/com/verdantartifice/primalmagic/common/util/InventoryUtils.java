package com.verdantartifice.primalmagic.common.util;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class InventoryUtils {
    public static boolean isPlayerCarrying(@Nullable PlayerEntity player, @Nullable ItemStack stack) {
        if (player == null) {
            return false;
        }
        if (stack == null || stack.isEmpty()) {
            return true;
        }
        int count = stack.getCount();
        for (ItemStack searchStack : player.inventory.mainInventory) {
            if (ItemStack.areItemsEqual(stack, searchStack)) {
                count -= searchStack.getCount();
                if (count <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isPlayerCarrying(@Nullable PlayerEntity player, @Nullable ResourceLocation tagName, int amount) {
        if (player == null) {
            return false;
        }
        if (tagName == null) {
            return true;
        }
        Tag<Item> tag = ItemTags.getCollection().getOrCreate(tagName);
        for (ItemStack searchStack : player.inventory.mainInventory) {
            if (!searchStack.isEmpty() && tag.contains(searchStack.getItem())) {
                amount -= searchStack.getCount();
                if (amount <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean consumeItem(@Nullable PlayerEntity player, @Nullable ItemStack stack) {
        if (player == null) {
            return false;
        }
        if (stack == null || stack.isEmpty()) {
            return true;
        }
        if (!isPlayerCarrying(player, stack)) {
            return false;
        }
        int count = stack.getCount();
        for (int index = 0; index < player.inventory.mainInventory.size(); index++) {
            ItemStack searchStack = player.inventory.mainInventory.get(index);
            if (ItemStack.areItemsEqual(stack, searchStack)) {
                if (searchStack.getCount() > count) {
                    searchStack.shrink(count);
                    count = 0;
                } else {
                    count -= searchStack.getCount();
                    player.inventory.mainInventory.set(index, ItemStack.EMPTY);
                }
                if (count <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean consumeItem(@Nullable PlayerEntity player, @Nullable ResourceLocation tagName, int amount) {
        if (player == null) {
            return false;
        }
        if (tagName == null || amount <= 0) {
            return true;
        }
        if (!isPlayerCarrying(player, tagName, amount)) {
            return false;
        }
        Tag<Item> tag = ItemTags.getCollection().getOrCreate(tagName);
        for (int index = 0; index < player.inventory.mainInventory.size(); index++) {
            ItemStack searchStack = player.inventory.mainInventory.get(index);
            if (!searchStack.isEmpty() && tag.contains(searchStack.getItem())) {
                if (searchStack.getCount() > amount) {
                    searchStack.shrink(amount);
                    amount = 0;
                } else {
                    amount -= searchStack.getCount();
                    player.inventory.mainInventory.set(index, ItemStack.EMPTY);
                }
                if (amount <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
