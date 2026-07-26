package com.verdantartifice.primalmagick.common.util;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

public class RecipeContainerWrapper implements Container {
    protected final ResourceHandler<ItemResource> inv;

    public RecipeContainerWrapper(ResourceHandler<ItemResource> inv) {
        this.inv = inv;
    }

    @Override
    public int getContainerSize() {
        return this.inv.size();
    }

    private ItemStack getStackInSlot(int index) {
        return ItemUtil.getStack(this.inv, index);
    }

    @Override
    public boolean isEmpty() {
        for (int index = 0; index < this.inv.size(); index++) {
            if (!this.getStackInSlot(index).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @NotNull
    public ItemStack getItem(int slot) {
        return this.getStackInSlot(slot);
    }

    @Override
    @NotNull
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = this.getStackInSlot(slot);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
    }

    @Override
    @NotNull
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
    public void setItem(int slot, @NotNull ItemStack stack) {
        try (Transaction tx = Transaction.openRoot()) {
            ItemResource resource = this.inv.getResource(slot);
            if (!resource.isEmpty()) {
                this.inv.extract(slot, resource, this.inv.getAmountAsInt(slot), tx);
            }
            if (!stack.isEmpty()) {
                this.inv.insert(slot, ItemResource.of(stack), stack.count(), tx);
            }
            tx.commit();
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        try (Transaction tx = Transaction.openRoot()) {
            for (int index = 0; index < this.inv.size(); index++) {
                ItemResource resource = this.inv.getResource(index);
                if (!resource.isEmpty()) {
                    this.inv.extract(index, resource, this.inv.getAmountAsInt(index), tx);
                }
            }
            tx.commit();
        }
    }
}
