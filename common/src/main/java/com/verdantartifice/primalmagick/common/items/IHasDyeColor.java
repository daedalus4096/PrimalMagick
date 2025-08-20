package com.verdantartifice.primalmagick.common.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.Nullable;

public interface IHasDyeColor {
    @Nullable
    default DyeColor getDyeColor(ItemStack stack) {
        if (stack.has(DataComponents.DYED_COLOR)) {
            return DyeColor.byFireworkColor(stack.get(DataComponents.DYED_COLOR).rgb());
        } else {
            return null;
        }
    }

    default void setDyeColor(ItemStack stack, DyeColor color) {
        if (color == null) {
            return;
        }
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getFireworkColor()));
    }

    default void removeDyeColor(ItemStack stack) {
        stack.remove(DataComponents.DYED_COLOR);
    }
}
