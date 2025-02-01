package com.verdantartifice.primalmagick.common.util;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class RecipeContainerWrapper implements Container {
    protected final IItemHandlerModifiable inv;

    public RecipeContainerWrapper(IItemHandlerModifiable inv) {
        this.inv = inv;
    }

    @Override
    public int getContainerSize() {
        return this.inv.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int index = 0; index < this.inv.getSlots(); index++) {
            if (!this.inv.getStackInSlot(index).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inv.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = this.inv.getStackInSlot(slot);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack s = this.getItem(index);
        if (s.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.setItem(index, ItemStack.EMPTY);
            return s;
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inv.setStackInSlot(slot, stack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        for(int index = 0; index < this.inv.getSlots(); index++) {
            this.inv.setStackInSlot(index, ItemStack.EMPTY);
        }
    }
}
