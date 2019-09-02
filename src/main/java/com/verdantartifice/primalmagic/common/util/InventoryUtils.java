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
        for (ItemStack searchStack : player.inventory.mainInventory) {
            if (ItemStack.areItemsEqual(stack, searchStack) && searchStack.getCount() >= stack.getCount()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPlayerCarrying(@Nullable PlayerEntity player, @Nullable ResourceLocation tagName) {
        if (player == null) {
            return false;
        }
        if (tagName == null) {
            return true;
        }
        Tag<Item> tag = ItemTags.getCollection().getOrCreate(tagName);
        for (ItemStack searchStack : player.inventory.mainInventory) {
            if (!searchStack.isEmpty() && tag.contains(searchStack.getItem())) {
                return true;
            }
        }
        return false;
    }
}
