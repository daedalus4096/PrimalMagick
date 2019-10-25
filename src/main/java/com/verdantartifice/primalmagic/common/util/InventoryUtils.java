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
    
    @Nullable
    public static IItemHandler getItemHandler(@Nonnull World world, @Nonnull BlockPos pos, @Nullable Direction side) {
        LazyOptional<Pair<IItemHandler, Object>> optional = VanillaInventoryCodeHooks.getItemHandler(world, pos.getX(), pos.getY(), pos.getZ(), side);
        Pair<IItemHandler, Object> pair = optional.orElse(null);
        if (pair != null && pair.getLeft() != null) {
            return pair.getLeft();
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof IInventory) {
                return wrapInventory((IInventory)tile, side);
            } else {
                return null;
            }
        }
    }
    
    @Nonnull
    public static IItemHandler wrapInventory(@Nonnull IInventory inv, @Nullable Direction side) {
        if (inv instanceof ISidedInventory) {
            return new SidedInvWrapper((ISidedInventory)inv, side);
        } else {
            return new InvWrapper(inv);
        }
    }
}
