package com.verdantartifice.primalmagic.common.util;

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
}
