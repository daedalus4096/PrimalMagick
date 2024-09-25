package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

/**
 * Special crafting inventory for holding a wand.
 * 
 * @author Daedalus4096
 */
public class WandInventory implements Container {
    protected final NonNullList<ItemStack> stackWand = NonNullList.withSize(1, ItemStack.EMPTY);
    protected final AbstractContainerMenu menu;
    
    public WandInventory(AbstractContainerMenu menu) {
        this.menu = menu;
    }

    @Override
    public void clearContent() {
        this.stackWand.clear();
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.stackWand.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.stackWand.get(0);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return this.removeItemNoUpdate(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        // Update the callback container's crafting state when a wand is removed from this inventory
        ItemStack stack = ContainerHelper.takeItem(this.stackWand, 0);
        this.menu.slotsChanged(this);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        // Update the callback container's crafting state when this inventory's contents are changed
        this.stackWand.set(0, stack);
        this.menu.slotsChanged(this);
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
    
    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        // Only wands can be added to this inventory
        return stack.getItem() instanceof IWand;
    }
}
