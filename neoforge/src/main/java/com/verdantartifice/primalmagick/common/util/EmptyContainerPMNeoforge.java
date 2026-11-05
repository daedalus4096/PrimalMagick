package com.verdantartifice.primalmagick.common.util;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EmptyContainerPMNeoforge implements Container {
    public static final EmptyContainerPMNeoforge INSTANCE = new EmptyContainerPMNeoforge();

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        throw new IndexOutOfBoundsException(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        throw new IndexOutOfBoundsException(slot);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        throw new IndexOutOfBoundsException(slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        throw new IndexOutOfBoundsException(slot);
    }

    @Override
    public void setChanged() {
        // Do nothing
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        // Do nothing
    }
}
